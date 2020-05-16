package Gt.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.HibernateMapping.GtGameConfig;
import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.APIActionType;
import Gt.common.BuyInChipInfo;
import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;
import Gt.common.CardRank;
import Gt.common.ChipInfo;
import Gt.common.Commands;
import Gt.common.GameInfo;
import Gt.common.GameVariants;
import Gt.common.MessageConstants;
import Gt.common.Player;
import Gt.common.Player.PlayerStatus;
import Gt.common.Player.Status;
import Gt.common.PlayerActionType;
import Gt.common.PlayerDetails;
import Gt.common.PlayerRoundInfo;
import Gt.common.PlayersInfo;
import Gt.common.Pot;
import Gt.common.PotInfo;
import Gt.common.RoundInfo;
import Gt.common.Seat;
import Gt.common.SeatInfo;
import Gt.common.Table;
import Gt.common.UrlCall;
import Gt.common.Winner;
import Gt.common.WinnerInfo;
import Gt.interfaces.Controller;
import Gt.interfaces.IGameExtension;
import Gt.loggers.ParserLogger;
import Gt.room.Extension.GameExtension;
import Gt.test.WebSericeCalling;
import Gt.user.hibernateMapping.GtGameAccounts;
import Gt.user.utils.GtGameAccountsUtils;
import Gt.user.utils.GtTransactionHistoryUtils;
import Gt.user.utils.GtUserAccountUtils;
import Gt.user.utils.GtUserUtils;
/**
 * Analogous to Controller of the MVC(Model-View-Controller) design
 * architecture.
 * 
 * This class consists of synchronized methods that operate on data received
 * from GameExtension to process requests and actions performed by the user. It
 * contains a synchronized method runStateMachine which maintains the state of
 * the game.
 * 
 * It contains methods to send manipulated data to GameExtension to be sent to
 * the Client.
 * 
 * @author Kaustubh
 * @see GameExtension
 * 
 */

public class GameController implements Controller {
	private static final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);
	private ScheduledFuture<?> scheduleController = null;
	IGameExtension gameExtension = null;
	Table table = null;
	GameController controller = null;
	private SeatInfo seatInfo = null;
	GameControllerStates state = GameControllerStates.IDLE;
	List<String> playerActions = new ArrayList<String>();
	private String playerAction = "";
	private static final int SHORT_DELAY = 4;
	private static final int DEALER_RESPONSE_DELAY = 15;
	double minBet = 0;
	double maxBet = 0;
	private static final int SIDESHOW_TIMER = 15;
	private static final int MAX_DISCONNECTED_TIME = 180;
	private Logger log = LoggerFactory.getLogger("Gt.controller.GameController");
	private final static int MAX_SEAT_RESERVATION_TIME = 20;
	private double currentStake = 0;
	private static boolean sendBettingStateOnce = true;
	private static final int mixVariantTimer = 15;
	List<GameVariants> gameVariants =  new ArrayList<GameVariants>(Arrays.asList(GameVariants.values()));
	private ParserLogger parserLogger = new ParserLogger();
	private String msg = "";
	private static final int ADMIN_ID = 0;
	private static final int GAME_START_TIMER = 5;
	private static final int NO_ACTION_TAKEN_COUNT =3;
	private ConcurrentHashMap<Integer, Integer> playerTopUpRequest = new ConcurrentHashMap<>();
	RoundInfo roundInfo = new RoundInfo();
	private boolean firstGameRunning = true;
	private String passcode = "123456";
	private boolean botOnTable = false;
	private Map<Integer,List<Card>> botWiseCard =null;
	
	//Dont't change server key value !!!!!!!!!!
	private static final String SERVER_PRIVATE_KEY = "DC892D7D7EE2EE48F2426030C3D0A76F59400D851562165D8F283B4E3AEA5C82FF065847B49B4CC27F3DA907D7ADC3DAE7F643A9FB9133E5A1EC2EE549A5D9A4";
	private static final int DEALER_CHAT_CODE = 900;
	
	private HashMap<Integer, Integer> takeSeatTransactionMap = new HashMap<>();
	/**
	 * Constructor.
	 * 
	 * @param gameExtension
	 *            the SFS Extension for a game which needs to call the
	 *            controller
	 */

	public GameController(IGameExtension gameExtension) {
		this.gameExtension = gameExtension;
	}

	/**
	 * Constructor.
	 */
	public GameController() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create a new Table with a given configuration. Adds a shuffled Deck of
	 * Cards and PotInfo for information about the Pots to be placed on the
	 * Table.
	 * 
	 * @param config
	 *            must have content of game configuration details.
	 * @see Table
	 * @see Card
	 * @see PotInfo
	 */
	public void createTable(GtGameConfig config, String tableName,
			int gameTableId,String domain) {
		table = new Table();

		table.setGameConfigId(config.getGameConfigId());
		table.setMaxPlayers(config.getMaxPlayers());
		table.setMinPlayers(config.getMinPlayers());
		table.setChipType(config.getChipType());
		table.setGameVariant(config.getGameVariant());
		table.setMaxBuyIn(config.getMaxBuyIn());
		table.setMinBuyIn(config.getMinBuyIn());

		table.setGameTableId(gameTableId);
		table.setTableName(tableName);
		table.setDomainName(domain);
		if(table.getGameVariant().equals(GameVariants.ALL.toString())){
			table.setMixVariant(true);
		}

		table.initial();
		parserLogger.setThreePattiParser(this, table.getGameTableId());
		seatInfo = table.getSeatInfo();
		seatInfo.createSeats(config.getMaxPlayers());

		info("table created of gameConfigId " + config.getGameConfigId()
				+ " of game Variant " + config.getGameVariant() + " domain " + domain);

	}

	private void cancelFutureTask() {
		scheduleController.cancel(true);
	}

	/**
	 * Models the running of a state machine. Breaks down the game into 6 states
	 * each represented and transitioned by switch-case statements.
	 * 
	 * @param runningMachine
	 *            Indicates running status of state machine.True, if running,
	 *            False otherwise.
	 */
	public synchronized void runStateMachine(boolean runningMachine) {
		if (!runningMachine && Thread.currentThread().isInterrupted()) {
			this.cancelFutureTask();
		}
		try {
			while (runningMachine) {
				info("Runstate machine  state= " + state);
				switch (state) {
				case IDLE:
					// send client a massage
					//
					info("Room Idle");
					gameExtension.sendIdleState();
					
					table.getDealerChatLogs().clear();
					
					msg = MessageConstants.MSG_WAIT.getMessage();
					
					//gameExtension.sendMassageToAll("INFO", msg);
					
					for(Player p : table.getAllPlayersSeated()){
						gameExtension.sendMassage("INFO",MessageConstants.MSG_WAIT.getMsgCode(), msg, p.getPlayerId());
					}
					
					//updateDealerChatList(msg);
					
					for(Player p : this.table.getActivePlayers()){  		// If active players are found in Idle state, after exception has occurred or after player leaves seat during mix variant pop-up .
						info("in idle state, active players found, status set to Inactive");
						p.setStatus(Status.INACTIVE); 
					}
					
					if (table.getActivePlayerNoAwayOnSeated().size() >= table
							.getMinPlayers()) {
						state = GameControllerStates.GAME_START;
						runningMachine = true;
					} else {
						if (table.getActivePlayerNoAwayOnSeated().size() == 1) {
							this.gameExtension.sendMassage("INFO",
									MessageConstants.MSG_WAIT.getMsgCode(), msg,
									this.table.getActivePlayers().get(0)
											.getPlayerId());
						}
						rescheduleTask(5);// To see player join room or not
						runningMachine = false;
					}

					break;
				case GAME_START:
					botOnTable =false;
					botWiseCard =  new HashMap<Integer, List<Card>>();
					if (table.getActivePlayerNoAwayOnSeated().size() < table
							.getMinPlayers()) {
						info("Active Players less than minimum players required");

						state = GameControllerStates.IDLE;

						break;
					}

					if (table.getActivePlayerNoAwayOnSeated().size() >= table.getMinPlayers()) {
						info("ACtive players not away on seated more than minimum players required");
						
						sendBettingStateOnce = true;
						
						if(table.isMixVariant()){
							
							state = GameControllerStates.DEALER_RESPONSE;
							
						}else{
							
							state = GameControllerStates.GAME_STARTED;
							
						}
						
						if((table.getDealerId()==0)){
							
							int dealerSeatId = table.getSeatInfo().getNextOccupiedSeat(
									table.getDealerSeatId());
							
							table.setDealerSeatId(dealerSeatId);
							
							table.setDealerId(table.getSeatInfo()
									.getSeatById(dealerSeatId).getPlayerId());
							
							info("Dealer seat id set to " + dealerSeatId);

							
						}

						gameExtension.sendDealer(
								table.getDealerId(), table.getDealerSeatId());

						info("CMD game.Dealer sent to all players");
						
						msg = "Dealer : " + table.getPlayerById(table.getDealerId()).getName();
						gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
						updateDealerChatList(msg);
						
						gameExtension.sendGameStart(GAME_START_TIMER);
						info("CMD game.start sent to all players");

						runningMachine = false;

						
						
						msg = "Game starting in " + GAME_START_TIMER + " seconds";
						gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
						//updateDealerChatList(msg);
						
						rescheduleTask(GAME_START_TIMER );
						
						
						
					} else {
						info("Active players not away and seated less than 1");
						state = GameControllerStates.IDLE;

						break;
					}
					break;
				
				case DEALER_RESPONSE :
					
					table.setGameVariant(GameVariants.SIMPLE.toString());
					
					List<String> games = new ArrayList<String>();
					
					for(GameVariants var : gameVariants){
						if(!var.toString().equals(GameVariants.ALL.toString())){
							games.add(var.toString());
						}
						
					}
					
					gameExtension.sendGameChoicesToDealer(table.getDealerId(),games,mixVariantTimer);
					
					for(Player p : table.getPlayers()){
						if(p.getPlayerId() != table.getDealerId()){
							gameExtension.sendMassage("INFO",MessageConstants.MSG_GAME_VARIANT_INPROGRESS.getMsgCode(),
									MessageConstants.MSG_GAME_VARIANT_INPROGRESS.getMessage(), p.getPlayerId());
							gameExtension.sendMassage("DC", DEALER_CHAT_CODE, 
									MessageConstants.MSG_GAME_VARIANT_INPROGRESS.getMessage(), p.getPlayerId());
						}else{
							gameExtension.sendMassage("DC", DEALER_CHAT_CODE, " you are selecting game variant", p.getPlayerId());
						}
					}
					
					
					state = GameControllerStates.GAME_STARTED;
					runningMachine = false;
					rescheduleTask(DEALER_RESPONSE_DELAY + SHORT_DELAY);
					
					break;
					
				case GAME_STARTED:
					if (table.getActivePlayerNoAwayOnSeated().size() < table.getMinPlayers()) {

						info("Active players not away and seated less than 1");
						state = GameControllerStates.IDLE;
						break;
						
					}
					table.getActivePlayers().clear();
					if (table.getGameId() == ((int) (table.getGameConfigId() * 100000L) + 1) && firstGameRunning) {
						firstGameRunning = false;
					} else {
						table.setGameId(table.getGameId() + 1);
						info("game Id incremented to " + table.getGameId()
								+ ". next game started.");
					}
					int countNumActivePlayers = 0;
					for (Seat seat : table.getSeatInfo().getSeats()) {
						if(seat.getEmailId().contains("bot.com")){
							botOnTable =true;
							botWiseCard.put(seat.getPlayerId(),null);
						}
						
						seat.setBet(0);
						if (seat.isOccupied()) {
							Player player = table.getPlayerOnSeatedById(seat
									.getPlayerId());
							if (player != null) {

								if ((Status.TAKESEAT.equals(player.getStatus()) || Status.INACTIVE
										.equals(player.getStatus()))
										&& player.getChipsLeft() > 0) {

									if (!player.isAway()
											&& player.getSeat() != null) {
										player.setStatus(Status.ACTIVE);
										player.setTurnCount(0);
										seat.setBet(0);
										table.addActivePlayers(player);
										countNumActivePlayers++;
										info("Player id "
												+ player.getPlayerId()
												+ " sitting on seat id "
												+ seat.getSeatId()
												+ " added to active players and status set to "
												+ player.getStatus().toString());
									}

								}

								/*
								 * if (player.getChipsLeft() == 0) {
								 * this.handleBuyInChipInfo
								 * (player.getPlayerId()); }
								 */

							}

						}
					}

					if (this.table.getActivePlayers().size() == 1) {
						state = GameControllerStates.IDLE;
						break;
					}

					
					gameExtension.sendSeatInfo(table.getSeatInfo());
					gameExtension.sendGameStarted(table.getGameId());

					info("CMD game.started send to all players");
					msg = "GAME_STARTED | game_id: "
							+ this.table.getGameId() + " | "
							+ "number_of_active_players: " + countNumActivePlayers + " | "
							+"chip_type: " + this.table.getChipType() + " | "
							+ "table_id: " + this.table.getGameTableId() + " | "
						    + "game_variant: "
							+ this.table.getGameVariant() + " | "
							+ "buy_low: "
							+ this.table.getBuyinchipInfo().getBuyInLow()
							+ " | " + "buy_high: "
							+ this.table.getBuyinchipInfo().getBuyInHigh()
							+ " | " + "ante: " + this.table.getAnte()
							+ " | " + "dealer_id: " + table.getDealerId() ;
					
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
					
					msg = msg + " | " + "mix_variant: " + table.isMixVariant();
					writeParserLog(msg);
					
					state = GameControllerStates.ANTE;
					

					break;
				
				case ANTE:
					if (table.getActiveStatusPlayerNoAllInNoPackOnSeated()
							.size() == 1) {
						info("Only 1 active player seated");

						state = GameControllerStates.IDLE;
						break;
					} else {
						
						//table.getPotInfo().addPot(0, new ArrayList<Integer>());
						
						Seat seat = null;

						//Pot pot = table.getPotInfo().getPots().get(0);
						
						if(table.isMixVariant() ){
							gameExtension.sendGameVariantToAll(table.getGameVariant());
							// as per ratul's requirement.
							String upperCaseGameVariant = table.getGameVariant().toUpperCase();
							gameExtension.sendMassageToAll("INFO", MessageConstants.MSG_GAME_VARIANT_SELECTED.getMessage() +
									upperCaseGameVariant, MessageConstants.MSG_GAME_VARIANT_SELECTED.getMsgCode());
							if(table.getGameVariant().equals(GameVariants.FOURXBOOT.toString())){
								this.table.setBootMultiplier(4);
							}else if(table.getGameVariant().equals(GameVariants.TWOXBOOT.toString())){
								this.table.setBootMultiplier(2);
							}
						
							//update the game variant by variant selected and clear the map
							info("updated takeSeatTransactionMap : "+takeSeatTransactionMap);
							GtTransactionHistoryUtils.updateGameVariant(takeSeatTransactionMap, GameVariants.getGameVariantVal(table.getGameVariant()), table.getDomainName(), SERVER_PRIVATE_KEY);
							takeSeatTransactionMap.clear();
							
							this.table.setAnte(this.table.getAnte() * this.table.getBootMultiplier());
							if(table.getPlayerById(table.getDealerId()) != null){
							msg = "MIX VARIANT | " + " DEALER : "
									+ table.getPlayerById(table.getDealerId()).getName() + " | GAME TYPE SELECTED: "
									+ table.getGameVariant() + " ante = " + table.getAnte() ;
							
							writeParserLog(msg);
							info(msg);
							}
							
							gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
							updateDealerChatList(msg);
							
						}
						
						for (Player player : table.getActivePlayers()) {
							writeParserLog("PLAYER: " + player.getPlayerId()
									+ " | SEAT: "
									+ player.getSeat().getSeatId()
									+ " | ChipsLeft:  " + player.getChipsLeft()
									+ " | GAME_ID: " + this.table.getGameId());
						}

						this.currentStake = table.getAnte()*2;
						int bootAmount = table.getAnte();
						for (Player player : table.getActivePlayers()) {
							
							if(player.getPlayerId() == table.getDealerId()){
								bootAmount *= 2; 
							}else{
								bootAmount = table.getAnte();
							}
							player.setChips(player.getChipsLeft());
							if (player.getChips() > bootAmount) {
								player.setChipsLeft(player.getChipsLeft()
										- bootAmount);
								player.setLastBetAmount(bootAmount);
								//table.getPotInfo().updatePot(player.getPlayerId(), bootAmount);
								writeParserLog("ANTE | " + " PLAYER NAME: "
										+ player.getName() + " | CHIPSLEFT: "
										+ player.getChipsLeft() + " | STATUS: "
										+ player.getStatus());
								
								msg = "Compulsory Blind " + bootAmount
								+ " collected from player "
								+ player.getName()
								+ " and added to pot" ;
								
								info(msg);
								
								gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
								//updateDealerChatList(msg);
								

							} else {
								player.setAllin(true);
								player.setAllInAmount((int)player.getChipsLeft());
								player.setLastBetAmount((int)player.getChipsLeft());
								//table.getPotInfo().updatePot(player.getPlayerId(), player.getChipsLeft());
								player.setChipsLeft(player.getChipsLeft() - player.getAllInAmount());
								
								writeParserLog("ANTE | " + " PLAYER NAME: "
										+ player.getName() + " | CHIPSLEFT: "
										+ player.getChipsLeft()
										+ " | STATUS: ALL IN");
								info("Player id " + player.getPlayerId()
										+ " is All in with all in amount  "
										+ player.getAllInAmount());
								
								msg = "Player  " + player.getName()
								+ " is All in with all in amount  "
								+ player.getAllInAmount() ;
								
								gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
								updateDealerChatList(msg);
							}
							
							GtGameAccounts gameAccount = GtGameAccountsUtils.findGameAccounts(player.getPlayerId(), this.table.getTableName());
							if(gameAccount != null){
								gameAccount.setChipsInPlay(gameAccount.getChipsInPlay() +  player.getLastBetAmount());
								gameAccount.setModifiedDate(new Timestamp(System.currentTimeMillis()));
								GtGameAccountsUtils.updateGameAccount(gameAccount);
							}
							//pot.getPlayerIds().add(player.getPlayerId());
						}
						
						 table.initPot();

						info(" new pot created "
								+ table.getPotInfo().toString());
						Card.fillDeck();
						// set whose turn

						// int whoseTurnSeatId =
						// table.getSeatInfo().getNextOccupiedSeat(table.getDealerSeatId());
						List<Card> deck = Card.newDeck();
						info("New Deck " + deck.toString());

						table.shuffle(deck);
						info("Bot are plaeying on this table "+botOnTable);
						if(botOnTable){
							for (Map.Entry<Integer, List<Card>> entry : botWiseCard.entrySet())
							{
								info("Setting cards for bot and bot seatid is "+entry.getKey());
								List<Card> handCards = setBotCards(table.getGameVariant());
								info("Setting cards for bot and bot cards is "+handCards);
								botWiseCard.put(entry.getKey(),handCards);
							}
						}
					

						info("shuffled deck before setting player cards " + table.getDeck().toString());
						
						// set joker before setting player cards as joker needs to be removed from deck.
						if(table.getGameVariant().equals(GameVariants.JOKER.toString())){
							table.getJokers().clear();
							setJoker();
						}else if(table.getGameVariant().equals(GameVariants.AK47.toString())){
							List<Card> jokers = table.getJokers();
							jokers.clear();
							jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
							jokers.add(new Card(FaceValue.KING, Suit.SPADES));
							jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
							jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));

							table.setJokers(jokers);
							msg = "Joker Cards : " + table.getJokers().toString();
							gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
							updateDealerChatList(msg);
						}
						
//						//shuffle deck
//						table.shuffle(table.getDeck());
						
						// once joker is set, set player cards.
						setPlayerCards(table.getDeck(),table.getActiveStatusPlayerNoPackedOnSeated(), botWiseCard, 3);
						
						info("Player Cards set");
						gameExtension.sendSeatInfo(table.getSeatInfo());
						info("SeatInfo sent to all PLayers");
						gameExtension.sendDealtCard();
						info("CMD game.dealtCards sent to all players");
						
						// after sending dealt cards send jokers, as client clears all cards on table on receiving game.dealtCard.
						if(table.getGameVariant().equals(GameVariants.JOKER.toString())){
							sendJokers(table.getJokers());
							msg = "Joker Cards : " + table.getJokers().toString();
							gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
							updateDealerChatList(msg);
						}
						
						int whoseTurnPlayerId = findNextTurn(table.getSeatInfo().getSeatById(table.getDealerSeatId()));
						
						if(whoseTurnPlayerId == 0){
							// when 2 players are playing. One of them is set as dealer, the other player goes all in during boot. in this case findNextTurn() tries to find next turn 
							// after the dealer's seat id, but returns 0 as there is no player available to bet except the dealer himself. If such a scenario happens, the game should 
							// go to the SHOW state.
							state = GameControllerStates.SHOW;
							runningMachine = false;
							rescheduleTask(SHORT_DELAY);
		
							break;
						}else{
							Player whoseTurnPlayer = table.getWhoseTurn();
							
							table.setPreviousPlayer(findPreviousPlayer(whoseTurnPlayer
									.getSeat().getSeatId()));
			
							info("Turn set to player id "
									+ whoseTurnPlayer.getPlayerId());
							minBet = this.currentStake;
							maxBet = this.currentStake * 2;
			
							if (minBet > whoseTurnPlayer.getChipsLeft()) {
								minBet = (int)whoseTurnPlayer.getChipsLeft();
								maxBet = (int)whoseTurnPlayer.getChipsLeft();
							} else if (minBet <= whoseTurnPlayer.getChipsLeft()
									&& maxBet > whoseTurnPlayer.getChipsLeft()) {
								maxBet = (int)whoseTurnPlayer.getChipsLeft();
							}
			
							whoseTurnPlayer.setMinBet(minBet);
							whoseTurnPlayer.setMaxBet(maxBet);
			
							info("player id " + whoseTurnPlayer.getPlayerId()
									+ " minBet = " + minBet + " maxBet = " + maxBet);
			
							
			
							List<Player> remainingPlayers = table
									.getActivePlayersNotAwayNotPacked();
							if (remainingPlayers.size() == 2) {
								// gameExtension.sendShowOptions(remainingPlayers);
								table.getWhoseTurn().getPlayerActions(false)
										.add(PlayerActionType.SHOW.toString());
							}
			
							/*
							 * gameExtension.sendWhoseTurn(this.table.getWhoseTurn()
							 * . getSeat().getSeatId(),
							 * this.table.getWhoseTurn().getPlayerId(),
							 * this.table.getTurnTime());
							 * 
							 * gameExtension.sendPlayerActions(table.getWhoseTurn().
							 * getPlayerId(),
							 * table.getWhoseTurn().getPlayerActions(false), minBet,
							 * maxBet);
							 * 
							 * table.getWhoseTurn().setTurnDataSent(true);
							 */
							
							

							gameExtension.sendPotInfo(table.getPotInfo());
							info("Pot info sent to all players");
							
							state = GameControllerStates.BETTING;
							runningMachine = false;
							rescheduleTask(SHORT_DELAY);
							break;
						}
						
					}
					

				case BETTING:
					
					if (table.getActiveStatusPlayerNoPackedOnSeated().size() == 1) {
						info("Only 1 active player seated");
						state = GameControllerStates.SHOW;
						break;
					} else {
						// To send turn to only remaining all in player
						if(sendBettingStateOnce){
							gameExtension.sendBettingState();
							
							msg = "Betting round start";
							gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
							//updateDealerChatList(msg);
							
							sendBettingStateOnce = false;
						}
						gameExtension.sendSeatInfo(table.getSeatInfo());
						
						List<Player> NotAllInPlayers = table
								.getActiveStatusPlayerNoAllInNoPackOnSeated();
						if (NotAllInPlayers.size() == 1
								&& !NotAllInPlayers.get(0).isTurnDataSent()) {
							// if only 1 player is not all in, game should move to show state
							
							state = GameControllerStates.SHOW;
							break;
							
							/*
							int highestAllinAmount = 0;
							for (Seat seat : table.getSeatInfo().getSeats()) {
								if (seat.isAllIn()
										&& seat.getBet() > highestAllinAmount) {
									highestAllinAmount = seat.getBet();
								}
							}
							Player player = NotAllInPlayers.get(0);

							info("Only 1 Not All In Player seated "
									+ player.toString());
							
							msg = "Only 1 Not All In Player seated " + player.getName();
							gameExtension.sendMassageToAll("DC", msg);
							//updateDealerChatList(msg);
							
							
							
							// info("Allin PLayer with maximum chips in pot " +
							// pMax.toString());

							if (highestAllinAmount > player.getSeat().getBet()) {
								int Bet = highestAllinAmount
										- player.getSeat().getBet();
								List<String> playerActions = player
										.getPlayerActions(true);
								playerActions.add(PlayerActionType.SHOW
										.toString());
								if (player.getChipsLeft() > Bet) {
									player.setMaxBet(Bet);
									player.setMinBet(Bet);

								} else {
									player.setMaxBet(player.getChipsLeft());
									player.setMinBet(player.getChipsLeft());
								}

								table.setWhoseTurn(player);
								gameExtension.sendWhoseTurn(player.getSeat()
										.getSeatId(), player.getPlayerId(),
										table.getTurnTime());
								gameExtension.sendPlayerActions(
										player.getPlayerId(), playerActions,
										player.getMinBet(), player.getMaxBet());
								player.setTurnDataSent(true);
								writeParserLog("USER TURN | PLAYER: "
										+ player.getName()
										+ " | PLAYER ACTIONS: "
										+ player.getPlayerActions(false)
												.toString());
								rescheduleTask(table.getTurnTime()
										+ SHORT_DELAY);
								runningMachine = false;
							} else {
								state = GameControllerStates.SHOW;
							}
							this.table.setLastActionOnTable(true);

						*/
							} else if (bettingManager()) {

							runningMachine = false;
						}
						break;
					}

				case SHOW:
					
					//table.updatePot();
					gameExtension.sendPotInfo(table.getPotInfo());
					gameExtension.sendShow();
					this.returnExcessAmount(table.getPotInfo());
					this.sendWagerReport();
					this.prepareWinnerInfo(table.getActivePlayersNotPacked(), table.getPotInfo());
					// update final_amount in each round end
					this.updateRoundInfo();
					this.sendWinnerCards();

					this.gameExtension.sendSeatInfo(this.table.getSeatInfo());
					this.gameExtension.sendWinnerToAll(this.table.getWinnerInfo());
					info("Sending winnerInfo to all "
							+ table.getWinnerInfo().toString());

					state = GameControllerStates.SENDROUNDEND;
					runningMachine = false;
					rescheduleTask(5 + SHORT_DELAY);
					break;
					
				case SENDROUNDEND:
					gameExtension.sendRoundEnd();
					table.getSeatInfo().reset();
					gameExtension.sendSeatInfo(table.getSeatInfo());
					this.addTopUp();
					this.processRoundEnd();
					
					table.reset();
					
					info("Round end sent to all");
					state = GameControllerStates.GAME_START;
					runningMachine = false;
					rescheduleTask(1);
					break;
				}
			}
		} catch (Exception e) {
			
			info("error in runstate machine, reseting pot " + table.getPotInfo().toString());
			table.reset();
			state = GameControllerStates.IDLE;
			error("Exception runStateMachine" + " state = " + state, e);
			info(e.toString());

		}

	}

	private void updateRoundInfo() {
		//roundInfo 
		roundInfo.getPlayerRoundInfoList().clear();
		
		roundInfo.setGameId(table.getGameId());
		
		for(Player p : table.getAllPlayersSeated()){
			if(p.getHandCards() != null && p.getHandCards().size()==3){
				PlayerRoundInfo info = new PlayerRoundInfo();
				info.setPlayerName(p.getName());
				info.setPlayerId(p.getPlayerId());
				info.setCards(p.getHandCards().toString());
				info.setWinner(p.isWinner());
				info.setBetAmount(p.getBet());
				info.setRake(this.table.getRakePercentage());
				
				for(Winner w : table.getWinnerInfo().getWinnerList()){
					if(w.getPlayerId() == p.getPlayerId()){
						info.setWinningAmount(info.getWinningAmount() + w.getAmount());
					}
				}
				roundInfo.getPlayerRoundInfoList().add(info);
			}
		}
		
		table.setRoundInfo(roundInfo);
		
	}

	private void sendWagerReport() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));
		String url = table.getDomainName() + UrlCall.API_WAGER_RECORD;
		
		int chipTypeIntValue = 0;						// 0 - Real money , 1 - Dummy
		
		if(this.table.getChipType().equals(GtUserAccountUtils.DUMMY)){
			 chipTypeIntValue = 1;
		}
		
		
		for(Player p : table.getAllPlayersSeated()){
			
			if(p.getHandCards() != null && p.getHandCards().size() == 3){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("table_id", this.table.getGameTableId());
				map.put("game_id", this.table.getGameId());
				map.put("chip_type", chipTypeIntValue);
				map.put("session_key", gameExtension.getSessionKey(p.getPlayerId()));
				map.put("wager_amount", p.getBet());
				map.put("rake_percent", this.table.getRakePercentage());
				map.put("buy_in_amount", p.getChips());
				map.put("final_amount", p.getChipsLeft());
				map.put("modified_at", formatter.format(new Date()));
				map.put("is_private", table.isPrivate()?"1":"0");
				map.put("running_balance", p.getChipsLeft());
				map.put("game_variant", GameVariants.getGameVariantVal(table.getGameVariant()) );
				log.info("wager report for player id without parameters " + p.getPlayerId());
				log.info(map.toString());

				try {
					WebSericeCalling.sendPost(map, url, null);
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error("send wager report error", e);
					return;
				}
			}
			
			 
		}
		
	}
	
	private void sendWagerReport(int playerId,double wagerAmount){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("IST"));
		String url = table.getDomainName() + UrlCall.API_WAGER_RECORD;
		
		Player player = table.getPlayerById(playerId);
		
		int chipTypeIntValue = 0;						// 0 - Real money , 1 - Dummy
		
		if(this.table.getChipType().equals(GtUserAccountUtils.DUMMY)){
			 chipTypeIntValue = 1;
		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("table_id", this.table.getGameTableId());
		map.put("game_id", this.table.getGameId());
		map.put("chip_type", chipTypeIntValue);
		map.put("session_key", gameExtension.getSessionKey(playerId));
		map.put("wager_amount", wagerAmount);
		map.put("rake_percent", this.table.getRakePercentage());
		map.put("buy_in_amount", player.getChips());
		map.put("final_amount", player.getChipsLeft());
		map.put("modified_at", formatter.format(new Date()));
		map.put("is_private", table.isPrivate()?"1":"0");
		map.put("running_balance", player.getChipsLeft());
		map.put("game_variant", GameVariants.getGameVariantVal(table.getGameVariant()) );
		log.info("wager report for player id " + playerId);
		log.info(map.toString());

		try {
			log.info("sending " + formatter.format(new Date()));
			WebSericeCalling.sendPost(map, url, null);
			log.info("sent " + formatter.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("send wager report error", e);
			return;
		}
		
		
	}

	private void updateDealerChatList(String msg2) {
		// TODO Auto-generated method stub
		
		if(this.table.getDealerChatLogs().size() >= 13){
			
			table.getDealerChatLogs().remove(3);
			
		}
		
		table.getDealerChatLogs().add(msg2);
		
	}

	private void processRoundEnd() {
		
		playerTopUpRequest.clear();

		for (Player p : table.getPlayersOnSeat()) {

			if (p.getChipsLeft() < 1 || p.getNoActivityCount()>=this.NO_ACTION_TAKEN_COUNT) {
				int playerId = p.getPlayerId();
				int seatId = p.getSeat().getSeatId();
				if(p.getNoActivityCount()>=this.NO_ACTION_TAKEN_COUNT){
					this.handleLeaveSeat(playerId);
				}else{
					if (handleLeaveSeat(playerId)) {
	
						if (this.reserveSeat(playerId, seatId, true)) {
							info("Prompting playerId="
									+ playerId
									+ " about the granting of seat reservation on seatId="
									+ seatId);
							gameExtension.sendTakeseatAllowedToUser(playerId,
									seatId, false);
						}
	
					}
				}

			}else{
				
				GtGameAccounts gameAccount = GtGameAccountsUtils.findGameAccounts(p.getPlayerId(), this.table.getTableName());
				if(gameAccount != null){
					gameAccount.setChips(p.getChipsLeft());
					gameAccount.setChipsInPlay(0.0);
					gameAccount.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					GtGameAccountsUtils.updateGameAccount(gameAccount);
				}
				
				
			}
			
		}

	}
	
	
	private void addTopUp() {
		Player player;
		int amount;
		String chipType = this.table.getChipType();
		for(Entry<Integer, Integer> entry : playerTopUpRequest.entrySet()){
			player = table.getPlayerById(entry.getKey());
			if(player == null){
				continue;
			}
			amount = entry.getValue();
			Map<String, Object> userAccount = GtUserUtils.checkPlayer(table.getDomainName(), gameExtension.getSessionKey(player.getPlayerId()), player.getPlayerId());
			
			if(userAccount == null){
				gameExtension.sendMassage("INFO", MessageConstants.MSG_CONTACT_ADMIN.getMsgCode(),
						MessageConstants.MSG_CONTACT_ADMIN.getMessage(), player.getPlayerId());
				gameExtension.sendSeatInfo(table.getSeatInfo());
				continue;
			}
			
			int playerId = player.getPlayerId();
			String playerName = player.getName();
			
			if ((chipType.equals(GtUserAccountUtils.DUMMY) && ((Double)userAccount.get(GtUserAccountUtils.DUMMY)).intValue()>= amount)
					|| (chipType.equals(GtUserAccountUtils.REAL) && ((Double)userAccount.get(GtUserAccountUtils.REAL)).intValue() >= amount)) {
				
				ChipInfo chipInfo = new ChipInfo();
				chipInfo.setChipAmount(amount);
				chipInfo.setChipType(chipType);
				chipInfo.setSource("1");
				chipInfo.setSourceId("0");
				String remarks = "topup " + amount + " " + chipType + " debit from "
						+ playerName;
				HashMap<String, Integer> orderDetails = GtUserAccountUtils.crDR(playerId, chipInfo,
										table.getGameTableId(), remarks,gameExtension.getSessionKey(playerId),
										table.getDomainName(), false,this.table.getGameId(),APIActionType.TOP_UP.getApiActionType(),table.isPrivate(), GameVariants.getGameVariantVal(table.getGameVariant()));
				int orderId = orderDetails.get("orderId");
				player.setOrderId(orderId);

				player.setChipsLeft(player.getChipsLeft() + amount);
				player.setChips(player.getChipsLeft());
				
				info("chip amount " + amount + " of chip type " + chipType
						+ " debited from user account of player with playerId "
						+ playerId);
				
				gameExtension.sendMassage("INFO", MessageConstants.MSG_TOPUP_SUCCESS.getMsgCode(), amount + 
						MessageConstants.MSG_TOPUP_SUCCESS.getMessage(), playerId);
				gameExtension.sendSeatInfo(this.seatInfo);
				
			}else{
				gameExtension.sendMassage("INFO", MessageConstants.MSG_INSUFFICIENT_BALANCE.getMsgCode(), 
						MessageConstants.MSG_INSUFFICIENT_BALANCE.getMessage(), playerId);
			}
			
		}
		
	}

	private void setJoker() {
		// TODO Auto-generated method stub
		info("Selecting a random card as joker");
		
		List<Card> jokers = new ArrayList<Card>();
		Random random = new Random();
		int jokerPosition = random.nextInt(table.getDeck().size());
		
		jokers.add(table.getDeck().get(jokerPosition));
		//table.getDeck().remove(jokerPosition); // don not remove that joker card from deck
		table.setJokers(jokers);
		
		writeParserLog("JOKERS | " + jokers.toString());
		
	}
	
	private void sendJokers(List<Card> jokers) {
		// TODO Auto-generated method stub
		
		List<String> jokerList = new ArrayList<String>();
		
		for(Card card : jokers){
			jokerList.add(card.toString());
		}
		
		gameExtension.sendJokers(jokerList);
		
	}

	/**
	 * Method to set cards distributed to players. For each player, it takes the
	 * specified number of cards from the top of the deck and assigns them to
	 * the player's handCards in the Player object.
	 * 
	 * @param deck
	 *            List of Card Objects representing the deck.
	 * @param activePlayers
	 *            List of Player Objects which are to be distributed cards.
	 * @param noOfCardsToDistribute
	 *            number of cards to be distributed to each player
	 * @throws Exception 
	 */

	private void setPlayerCards(List<Card> deck, List<Player> activePlayers,Map<Integer,List<Card>> botOnSeatIds,int noOfCards) throws Exception {
		
		try {
			info("Setting Player Cards for all players");
			List<Card> playerHandCards = null;
			for (Player player : activePlayers) {
				if (!botOnSeatIds.containsKey(player.getPlayerId())) {
					playerHandCards = new ArrayList<Card>();
					for (int i = 0; i < noOfCards; i++) {
						playerHandCards.add(deck.get(0));

						info("PLayer " + player.getName() + " card "
								+ deck.get(0));
						deck.remove(0);
					}
					player.setHandCards(playerHandCards);
					
				}else{
					player.setHandCards(botOnSeatIds.get(player.getPlayerId()));
				}
				info("Player Cards of PLayer Id " + player.getPlayerId()
						+ " " + player.getHandCards().toString());
				writeParserLog("PLAYER CARDS | PLAYER: " + player.getName()
						+ " CARDS: " + player.getHandCards().toString());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("set player cards error " , e);
			throw e;
		}
			//return;
		
		// test case, multiple winner generation by same ranked cards
		
		  /*for (Player player : activePlayers) { 
			 
			  List<Card> playerHandCards =
			  new ArrayList<Card>(); 
			  playerHandCards.add(new Card(FaceValue.SIX,Suit.CLUBS));
			  playerHandCards.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
			  playerHandCards.add(new Card(FaceValue.EIGHT,Suit.SPADES));
			  player.setHandCards(playerHandCards);
			  info("PLayer Cards of PLayer Id " + player.getPlayerId() + " " +  playerHandCards.toString()); 
			  writeParserLog("PLAYER CARDS | PLAYER: " + player.getName()
				+ " CARDS: " + player.getHandCards().toString());
		  
		  }*/
		
		// biased cards to single player
		 
	/*	try {
			info("Biased Cards for demo");
			//List<Card> playerHandCards = null;
			
			String iskoJeetao = "DP_00033";
			List<Card> acheCardsDedo = new ArrayList<Card>();
			boolean cheatingKarniHai = false;

			acheCardsDedo.add(new Card(FaceValue.ACE,Suit.CLUBS));
			acheCardsDedo.add(new Card(FaceValue.TWO,Suit.CLUBS));
			acheCardsDedo.add(new Card(FaceValue.THREE,Suit.CLUBS));

			FaceValue sweetFace = acheCardsDedo.get(0).getFaceValue();
			FaceValue niceFace = acheCardsDedo.get(1).getFaceValue();
			FaceValue mastFace = acheCardsDedo.get(2).getFaceValue();

			Suit sweetSuit = acheCardsDedo.get(0).getSuit();
			Suit niceSuit = acheCardsDedo.get(1).getSuit();
			Suit mastsuit = acheCardsDedo.get(2).getSuit();
			
			
			Card tempTopcard;
			
			/*for (Player player : activePlayers) {
				if(player.getName().equalsIgnoreCase(iskoJeetao)){
					player.setHandCards(acheCardsDedo);
					info("biased Player Cards of PLayer Id " + player.getPlayerId() + " "
							+ acheCardsDedo.toString());
					continue;
				}
				
				playerHandCards = new ArrayList<Card>();
				while(playerHandCards.size() <3) {
					tempTopcard = deck.get(0);
					if(cheatingKarniHai){
						if((tempTopcard.getFaceValue().equals(sweetFace) && tempTopcard.getSuit().equals(sweetSuit))||
								   (tempTopcard.getFaceValue().equals(niceFace) && tempTopcard.getSuit().equals(niceSuit)) ||
								   (tempTopcard.getFaceValue().equals(mastFace) && tempTopcard.getSuit().equals(mastsuit))){
									
									deck.remove(0);
									continue;
								}
					}
					
					playerHandCards.add(tempTopcard);
					info("PLayer " + player.getName() + " card " + tempTopcard );
					deck.remove(0);
				}
				player.setHandCards(playerHandCards);
				info("Player Cards of PLayer Id " + player.getPlayerId() + " "
						+ playerHandCards.toString());
				writeParserLog("PLAYER CARDS | PLAYER: " + player.getName()
						+ " CARDS: " + player.getHandCards().toString());
			}
			
			for (Player player : activePlayers) {
				playerHandCards = new ArrayList<Card>();
				if(player.getName().equalsIgnoreCase(iskoJeetao)){
					player.setHandCards(acheCardsDedo);
					info("biased Player Cards of PLayer Id " + player.getPlayerId() + " "
							+ acheCardsDedo.toString());
					continue;
				}
				
				if (!botOnSeatIds.containsKey(player.getPlayerId())) {
					
					for (int i = 0; i < noOfCards; i++) {
						
						
						tempTopcard = deck.get(0);
						if(true){
							if((tempTopcard.getFaceValue().equals(sweetFace) && tempTopcard.getSuit().equals(sweetSuit))||
									   (tempTopcard.getFaceValue().equals(niceFace) && tempTopcard.getSuit().equals(niceSuit)) ||
									   (tempTopcard.getFaceValue().equals(mastFace) && tempTopcard.getSuit().equals(mastsuit))){
										
										deck.remove(0);
										continue;
									}
						}
						
						
						
						playerHandCards.add(deck.get(0));

						info("PLayer " + player.getName() + " card "
								+ deck.get(0));
						deck.remove(0);
					}
					player.setHandCards(playerHandCards);
					
				}else{
					player.setHandCards(botOnSeatIds.get(player.getPlayerId()));
				}
				info("Player Cards of PLayer Id " + player.getPlayerId()
						+ " " + player.getHandCards().toString());
				writeParserLog("PLAYER CARDS | PLAYER: " + player.getName()
						+ " CARDS: " + player.getHandCards().toString());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("set player cards error " , e);
			throw e;
		}*/

	}
	
	


	private void removeCardFromDeck(List<Card> deck, FaceValue face, Suit suit) {
		Iterator<Card> it = deck.iterator();
		Card card;
		while(it.hasNext()){
			card = it.next();
			if(card.getFaceValue().equals(face) && card.getSuit().equals(suit)){
				it.remove();
				break;
			}
		}
	}

	/**
	 * Method called by the runStateMachine() to manage the betting rounds
	 * during the game. It is called when the state machine reaches the
	 * 'BETTING' state. It gets the player who has the turn from the table,
	 * checks if the turn data data has been sent and the the player isn't
	 * packed, if not, it calculates the turn data for the player and sends it
	 * to the player, else, it gets the action taken by player and deducts money
	 * and updates pot.
	 * 
	 * At the end of each round, it creates a new pot for every player that is
	 * all in.
	 * 
	 */
	public synchronized boolean bettingManager(){
		info("Inside bettinManager");
		Player player = table.getWhoseTurn();
		info("Running for Player id " + player.toString());
		if (player.isTurnDataSent() && !player.isPack()) {

			PlayerActionType action = player.getPlayerTookAction();
			info("Player id " + player.getPlayerId() + " took action "
					+ action.toString());
			if(player.isAdvanceActionTaken()) {
				msg = "Pre selected";
				info("Advance action "+player.getAdvanceAction()+" by player id "+player.getPlayerId());
			}
			else msg = "";
			
			switch (action) {
			case BLIND:
				if ((int)player.getChipsLeft() <= player.getLastBetAmount()) {
					player.setAllin(true);
					
					player.setAllInAmount((int)player.getChipsLeft());
					player.setChipsLeft(player.getChipsLeft() - player.getAllInAmount());

					info("PLayer id " + player.getPlayerId()+ " is aLLin with all in amount "+ player.getAllInAmount());
					
					msg += " All in by  " + player.getName() + " , chip amount :  " + player.getAllInAmount();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);

				} else {
					this.currentStake = player.getLastBetAmount();
					double playerChipsLeft = player.getChipsLeft();

					player.setChipsLeft(playerChipsLeft
							- player.getLastBetAmount());

					info("player id " + player.getPlayerId()
							+ " chips left set to " + player.getChipsLeft());
					
					if(player.getLastBetAmount() == player.getMinBet())
						msg += " Blind by " +  player.getName() + ", chip amount : " + player.getLastBetAmount();
					else
						msg += " 2x Blind by " +  player.getName() + ", chip amount : " + player.getLastBetAmount();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
				}
				player.setTurnDataSent(false);
				info("Player id " + player.getPlayerId()
						+ " turnDataSent set to " + player.isTurnDataSent());
				
				
				break;

			case CHAAL:
				if ((int)player.getChipsLeft() <= player.getLastBetAmount()) {
					player.setAllin(true);
					player.setAllInAmount((int)player.getChipsLeft());
					player.setChipsLeft(player.getChipsLeft() - player.getAllInAmount());
					info("Player id " + player.getPlayerId()
							+ " is allin with all in amount "
							+ player.getAllInAmount());
					
					msg +=  " All in by  " + player.getName() + " , chip amount :  " + player.getAllInAmount();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
					
				} else {
					this.currentStake = player.getLastBetAmount();
					double playerChipsLeft = player.getChipsLeft();

					player.setChipsLeft(playerChipsLeft
							- player.getLastBetAmount());

					info("player id " + player.getPlayerId()
							+ " chips left set to " + player.getChipsLeft());
					
					if(player.getLastBetAmount() == player.getMinBet())
						msg += " Chaal by " +  player.getName() + ", chip amount : " + player.getLastBetAmount();
					else
						msg += " 2x Chaal by " +  player.getName() + ", chip amount : " + player.getLastBetAmount();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
				}
				player.setTurnDataSent(false);
				info("Player id " + player.getPlayerId()
						+ " turnDataSent set to " + player.isTurnDataSent());
				break;

			case PACK:
				player.setPack(true);
				player.setLastBetAmount(0);
				player.setStatus(Status.INACTIVE);
				player.setNoActivityCount(player.getNoActivityCount()+1);
				info("Player id " + player.getPlayerId() + " set Pack "
						+ player.isPack() + " and status set to "
						+ player.getStatus());
				
				msg += " Pack by player "+ player.getName();
				gameExtension.sendMassageToAll("DC",msg, DEALER_CHAT_CODE);
				updateDealerChatList(msg);
				
				
				break;

			case SIDE_SHOW:
				if ((int)player.getChipsLeft() > player.getLastBetAmount()) {
					this.currentStake = player.getLastBetAmount();
					player.setChipsLeft(player.getChipsLeft() - player.getLastBetAmount());
				} else {
					player.setAllin(true);
					player.setAllInAmount((int)player.getChipsLeft());
					//player.setLastBetAmount(player.getChipsLeft());

					player.setChipsLeft(player.getChipsLeft() - player.getAllInAmount());
					
				}
				this.table.setSideShowPlayerId(player.getPlayerId());
				player.setTurnDataSent(true); 															// don't change to false, trust me. if there is no response of the side show, turn will pass to next player.								
				Player playerToSendSideShow = this.findPreviousPlayer(player.getSeat().getSeatId());
				
				table.getPotInfo().updatePot(player.getPlayerId(), player.getLastBetAmount());
				
				player.setPlayerTookAction(PlayerActionType.SIDE_SHOW_DENIED);
				
				log.info("sending side show to player id " + playerToSendSideShow.getPlayerId() + " from player " + player.getName() + " side show timer " + SIDESHOW_TIMER );
				
				gameExtension.sendSideShow(playerToSendSideShow.getPlayerId(),
						player.getName(), SIDESHOW_TIMER);
				
				msg = "Player  " + player.getName() + " requested side show with " + playerToSendSideShow.getName();
				gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
				updateDealerChatList(msg);
				
				for(Player p : table.getActivePlayers()){
					
					if(p.getPlayerId() != player.getPlayerId()  && p.getPlayerId() != playerToSendSideShow.getPlayerId()){
						this.gameExtension.sendMassage("INFO",MessageConstants.MSG_SIDESHOW_REQUEST.getMsgCode(),
								player.getName() + MessageConstants.MSG_SIDESHOW_REQUEST.getMessage()
						+ playerToSendSideShow.getName(), p.getPlayerId());
					}
					
				}
				
				break;
				
			case SIDE_SHOW_DENIED:
				player.setTurnDataSent(false);
				//player.setPlayerTookAction(PlayerActionType.PACK); 				// removed for GTTP-23 
				this.table.setSideShowPlayerId(-1);
				info("Player side show request denied.");
				break;
			case SIDE_SHOW_ACCEPT:
				info("Player side show request accepted.");
				break;
			case SHOW:
				info("Player show request.");
				if ((int)player.getChipsLeft() <= player.getLastBetAmount()) {
					player.setAllin(true);
					player.setAllInAmount((int)player.getChipsLeft());
					player.setChipsLeft(player.getChipsLeft() - player.getAllInAmount());
					info("Player id " + player.getPlayerId()
							+ " is allin with all in amount "
							+ player.getAllInAmount());
					
					msg = "Player " + player.getName() + " is All in with amount " + player.getAllInAmount();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
					
				} else {
					this.currentStake = player.getLastBetAmount();
					double playerChipsLeft = player.getChipsLeft();

					player.setChipsLeft(playerChipsLeft
							- player.getLastBetAmount());

					info("player id " + player.getPlayerId()
							+ " chips left set to " + player.getChipsLeft());
				}
				player.setTurnDataSent(false);
				info("Player id " + player.getPlayerId()
						+ " turnDataSent set to " + player.isTurnDataSent());
				break;
			default:
				break;

			}
			
			player.setAdvanceActionTaken(false);
			if(table.isRoundComplete()){
				table.setRoundCounter(table.getRoundCounter() + 1);
				//GV-logs, round complete and roundCounter
			}
			//GV-logs, isAllBlind
			if(!this.table.isAllBlind()){
				if(table.getRoundCounter() == 3){
					Player firstBlindPlayerNextToDealer = table.getfirstBlindPlayerNextTo(this.table.getDealerSeatId());
					if(firstBlindPlayerNextToDealer != null){
						sendCards(firstBlindPlayerNextToDealer.getPlayerId(), -1);
					}
					if(!this.table.isAllSeen()){
						//GV-logs
						table.setRoundCounter(0);
					}
					
				}
			}
			double chipAmount = player.getLastBetAmount();
			if(player.isAllin()){
				chipAmount = player.getAllInAmount();
			}
			
			if(!PlayerActionType.SIDE_SHOW_ACCEPT.equals(player.getPlayerTookAction()) && !PlayerActionType.SIDE_SHOW_DENIED.equals(player.getPlayerTookAction())){
				table.getPotInfo().updatePot(player.getPlayerId(), chipAmount);
				
				if(PlayerActionType.SIDE_SHOW_DENIED.equals(player.getPlayerTookAction())){					// done for GTTP-23. 
					player.setPlayerTookAction(PlayerActionType.PACK);
				}
				
			}
			
			if(player.isAllin()){
				this.getTable().updatePots(player);
				this.gameExtension.sendPotInfo(this.getTable().getPotInfo());
			}
			
			String potInfo = "PLAYERS";
			Player ply = null;
			for (int totalPlayer : this.table.getPotInfo().getPots().get(this.table.getPotInfo().getPots().size()-1).getPlayerIds()) {
				ply = table.getPlayerById(totalPlayer);
				if (ply != null) {
					potInfo = potInfo + " ," + ply.getName();
				}
			}
			String act = player.getPlayerTookAction().toString();
			
			if (player.isAllin()) {
				act = "ALLIN";
			}
			
			GtGameAccounts gameAccount = GtGameAccountsUtils.findGameAccounts(player.getPlayerId(), this.table.getTableName());
			if(gameAccount != null){
				gameAccount.setChipsInPlay(gameAccount.getChipsInPlay() +  player.getLastBetAmount());
				gameAccount.setModifiedDate(new Timestamp(System.currentTimeMillis()));
				GtGameAccountsUtils.updateGameAccount(gameAccount);
			}
			
			if("chaal".equalsIgnoreCase(act) || "blind".equalsIgnoreCase(act)){
				if(player.getLastBetAmount() == player.getMinBet()) {			
					writeParserLog("USER TURN | PLAYER: " + player.getPlayerId()
						+ " | PLAYER_ACTION: " + act
						+ " | BET: " + chipAmount
						+ " | GAME_ID: " + table.getGameId()
						+ " | HAND_CARDS: " + player.getHandCards()
						+ " | STATUS: " + player.getPlayerStatus()
						+ " | CHIPS_LEFT: "+ player.getChipsLeft()
						+ " | SYSTEM_ACTIONS: " + player.getPlayerActions(false)
						+ " | REMARK: " + act
						+ " | POT_AMOUNT: " + table.getPotInfo().getPots().get(0).getChips()
						+ " | RAKE_PERCENT: " + table.getRakePercentage());
				}
				else {
					writeParserLog("USER TURN | PLAYER: " + player.getPlayerId()
						+ " | PLAYER_ACTION: 2x " + act
						+ " | BET: " + chipAmount
						+ " | GAME_ID: " + table.getGameId()
						+ " | HAND_CARDS: " + player.getHandCards()
						+ " | STATUS: " + player.getPlayerStatus()
						+ " | CHIPS_LEFT: "+ player.getChipsLeft()
						+ " | SYSTEM_ACTIONS: " + player.getPlayerActions(false)
						+ " | REMARK: " + act
						+ " | POT_AMOUNT: " + table.getPotInfo().getPots().get(0).getChips()
						+ " | RAKE_PERCENT: " + table.getRakePercentage());
				}
			}
			
			writeParserLog("PLAYER STATUS | PLAYER: " + player.getName()
					+ " | STATUS: " + player.getPlayerStatus().toString());
			
			writeParserLog("Pot "+table.getPotInfo().getPots().get(0).getChips() +potInfo);

			if (table.getPotInfo().getPots()
					.get(table.getPotInfo().getPots().size() - 1).getChips() >= table
					.getMaxPotLimit() && table.getMaxPotLimit() != -1) {
				state = GameControllerStates.SHOW;
			}
			// table.updatePot();
			if (this.table.isLastActionOnTable()) {
				state = GameControllerStates.SHOW;
			}

			gameExtension.sendPotInfo(table.getPotInfo());
			gameExtension.sendSeatInfo(table.getSeatInfo());
			this.gameExtension.sendPlayerStatus(player.getPlayerId(), false,
					action.toString());

			if (PlayerActionType.SIDE_SHOW.equals(action)) {
				rescheduleTask(SIDESHOW_TIMER + SHORT_DELAY);
				
				return true;
			} else if (table.getActiveStatusPlayerNoAllInNoPackOnSeated()
					.size() > 1) { // stop
									// when
									// no
									// other
									// player
									// without
									// allin
									// and
									// pack
				
				if(!player.getPlayerTookAction().equals(PlayerActionType.SHOW)){
					player.setPlayerTookAction(PlayerActionType.PACK);
				}
				table.setPreviousPlayer(player);
				Seat currentSeat = player.getSeat();
				this.findNextTurn(currentSeat);
			}

			info("returning for player " + player.toString());
			return false;

		} else {

			info("calculating turn data for player " + player.toString());
			Player prevPlayer =   findPreviousPlayer(player.getSeat().getSeatId());  // table.getPreviousPlayer();
			
			int prevPlayerSeatId =  prevPlayer.getSeat().getSeatId();
			
			table.setPreviousPlayer(prevPlayer);
			/*if(prevPlayer.isPack()){
				if(findPreviousPlayer(prevPlayerSeatId) != null){
					prevPlayer = findPreviousPlayer(prevPlayerSeatId);
					table.setPreviousPlayer(prevPlayer);
				}
				
			}*/

			PlayerStatus currentPlayerStatus = player.getPlayerStatus();
			calculatePlayerBets(player, prevPlayer);
			List<String> playerActions = player.getPlayerActions(true);
			List<Player> remainingPlayers = table
					.getActiveStatusPlayerNoAllInNoPackOnSeated();
			
			player.setTurnDataSent(true);
			
			table.setAllSeen(true);
			for(Player p : remainingPlayers){
				if(p.getPlayerStatus().equals(PlayerStatus.BLIND)){
					this.table.setAllSeen(false);
					break;
				}
			}
			
			if (remainingPlayers.size() == 2) {
				playerActions.add(PlayerActionType.SHOW.toString());
			} else if (table.isAllSeen() && table.getRoundCounter()>=3) {
				playerActions.add(PlayerActionType.SIDE_SHOW.toString());
			}
			
			gameExtension.sendWhoseTurn(player.getSeat().getSeatId(),
					player.getPlayerId(), table.getTurnTime());
			
			for(Player waitingPLayer : table.getActiveStatusPlayerNoAllInNoPackOnSeated()){
				//if(waitingPLayer.getPlayerId() != player.getPlayerId()){
					
					//List<String> advanceActions = waitingPLayer.getPlayerActions(true);
					//GV-logs, calculating player bet
					calculatePlayerBets(waitingPLayer, this.getTable().getPreviousPlayer());
					if(waitingPLayer.getAdvanceAction() != null && 
							((waitingPLayer.getAdvanceBetAmount() <= waitingPLayer.getMaxBet() && waitingPLayer.getAdvanceBetAmount() >= waitingPLayer.getMinBet()) || waitingPLayer.getAdvanceAction().equals(PlayerActionType.PACK))){
						if(waitingPLayer.getPlayerId() != player.getPlayerId()){
							List<String> advanceActions = waitingPLayer.getPlayerActions(true);
							gameExtension.sendPlayerActions(waitingPLayer.getPlayerId(),
									advanceActions, waitingPLayer.getMinBet(), waitingPLayer.getMaxBet(),false, waitingPLayer.getAdvanceBetAmount());
						}
						
					}else{
						if(waitingPLayer.getPlayerId() != player.getPlayerId()){
							List<String> advanceActions = waitingPLayer.getPlayerActions(true);
							gameExtension.sendPlayerActions(waitingPLayer.getPlayerId(),
									advanceActions, waitingPLayer.getMinBet(), waitingPLayer.getMaxBet(),false,-1);
						}
						
						waitingPLayer.setAdvanceAction(null);
						waitingPLayer.setAdvanceBetAmount(-1);
					}
					
					
					info("ACTIONS SENT | PLAYER: " + waitingPLayer.getName()
					+ " | PLAYER ACTIONS: "
					+ waitingPLayer.getPlayerActions(false).toString());
					
				//}
			}

			if(player.getAdvanceAction() != null){
				player.setTurnCount(player.getTurnCount() + 1);
				player.setNoActivityCount(0);
				player.setPlayerTookAction(player.getAdvanceAction());
				player.setTurnDataSent(true);
				if(!player.getPlayerTookAction().equals(PlayerActionType.PACK)){
					if(player.getAdvanceBetAmount() >= player.getMinBet() && player.getAdvanceBetAmount() <= player.getMaxBet()){
						player.setLastBetAmount(player.getAdvanceBetAmount());
						info("Player with playerID " + player.getPlayerId()
						+ " sent player took action in advance "
						+ playerAction +" with valid bet amount." + player.getAdvanceBetAmount() + ". min = " + player.getMinBet() 
						+ ". max = " + player.getMaxBet() );
					}else{
						info("Player with playerID " + player.getPlayerId()
								+ " sent player took action in advance "
								+ playerAction +" with invalid bet amount." + player.getAdvanceBetAmount() + ". min = " + player.getMinBet() 
								+ ". max = " + player.getMaxBet() );
						player.setLastBetAmount(player.getMinBet());
					}
				}
				player.setAdvanceAction(null);
				player.setAdvanceBetAmount(0);
				//rescheduleTask(1);
				return false;
			}else{
				gameExtension.sendPlayerActions(player.getPlayerId(),
						playerActions, player.getMinBet(), player.getMaxBet(),true,-1);
			}
			
			writeParserLog("USER TURN | PLAYER: " + player.getName()
					+ " | PLAYER ACTIONS: "
					+ player.getPlayerActions(false).toString());
			
			
			rescheduleTask(table.getTurnTime() + SHORT_DELAY);
			return true;

		}

	}

	private void setAllPlayersSeen() {
		// TODO Auto-generated method stub
		for (Player p : table.getActiveStatusPlayerNoAllInNoPackOnSeated()) {
			if (p.getPlayerStatus().equals(PlayerStatus.BLIND)) {
				sendCards(p.getPlayerId(),-1);
			}

		}

	}

	private void calculateNewPlayerBets(Player currentPlayer) {		
		double playerMinBet = currentPlayer.getMinBet();
		
		currentPlayer.setMinBet(playerMinBet * 2);
		currentPlayer.setMaxBet(playerMinBet * 4);//maxBet = twice the minBet
		currentPlayer.setMinCurrentStake(currentPlayer.getMinBet());
		info("player after self bets : "+currentPlayer);
	}
	
	private void calculatePlayerBets(Player currentPlayer, Player prevPlayer) {
		PlayerStatus currentPlayerStatus = currentPlayer.getPlayerStatus();
		info("players before calculations");
		info("currentPlayer : "+currentPlayer);
		info("prevPlayer : "+prevPlayer);
		info(this.toString());

		if (prevPlayer == null) {
			if (currentPlayerStatus.equals(PlayerStatus.SEEN)) {
				minBet = this.currentStake * 2;
				maxBet = this.currentStake * 4;
			} else if (currentPlayerStatus.equals(PlayerStatus.BLIND)) {
				minBet = this.currentStake;
				maxBet = this.currentStake * 2;
			}
		} else {
			PlayerStatus prevPlayerStatus = prevPlayer.getPlayerStatus();

			switch (currentPlayerStatus) {
			case BLIND:
				if (prevPlayerStatus.equals(PlayerStatus.BLIND)) {
					minBet = this.currentStake;
					maxBet = this.currentStake * 2;
				} else {
					minBet = this.currentStake / 2;
					maxBet = this.currentStake;
				}
				break;
			case SEEN:
				if (prevPlayerStatus.equals(PlayerStatus.BLIND)) {
					minBet = this.currentStake * 2;
					maxBet = this.currentStake * 4;
				} else {
					minBet = this.currentStake;
					maxBet = this.currentStake * 2;
				}
				break;
			default:
				break;
			}
		}
		
		if(minBet < currentPlayer.getMinBet()){
			minBet = currentPlayer.getMinBet();
			maxBet = 2*minBet;
		}
		
		if (minBet < table.getAnte() || maxBet < table.getAnte()) {
			minBet = table.getAnte();
			maxBet = minBet * 2;
		}
		currentPlayer.setMinCurrentStake(minBet); //to calcualte side pot after player want all-in
		if(this.table.getMaxBet() != -1){
			maxBet = this.table.getMaxBet();
		}

		double potAmount = table.getPotInfo().getPots().get(table.getPotInfo().getPots().size() - 1).getChips();
		
		if (potAmount + minBet > table.getMaxPotLimit()  && table.getMaxPotLimit() != -1) {
			maxBet = table.getMaxPotLimit() - potAmount;
			minBet = maxBet;
		} else if (potAmount + maxBet > table.getMaxPotLimit()  && table.getMaxPotLimit() != -1) {
			maxBet = table.getMaxPotLimit() - potAmount;
		}
		
		if (minBet > currentPlayer.getChipsLeft()) {
			minBet = (int)currentPlayer.getChipsLeft();
			maxBet = (int)currentPlayer.getChipsLeft();
		} else if (minBet <= currentPlayer.getChipsLeft()
				&& maxBet > currentPlayer.getChipsLeft()) {
			maxBet = (int)currentPlayer.getChipsLeft();
		}

		currentPlayer.setMinBet(minBet);
		currentPlayer.setMaxBet(maxBet);
		
		info("players after calculations");
		info("currentPlayer : "+currentPlayer);
		info("prevPlayer : "+prevPlayer);
		info(this.toString());
	}

	@Override
	public String toString() {
		return "GameController [table=" + table + ", minBet=" + minBet + ", maxBet=" + maxBet + ", currentStake="
				+ currentStake + "]";
	}

	private synchronized boolean rescheduleTask(long delay) {
		try {
			if ((this.scheduleController != null)
					&& (!this.scheduleController.isDone())) {
				this.scheduleController.cancel(true);
				info("scheduler cancelled");
			}
		} catch (NullPointerException e) {
			info("NULL Pointer Exception.Unable to cancel the Timer; timer exception.While Rescheduling Task."
					+ e);
			error("reschedule task", e);
		}
		this.scheduleController = this.gameExtension
				.scheduleRunStateMachine((int) delay);
		info("State machine scheduled for " + delay);

		return true;
	}

	/**
	 * Method to calculate the next turn during the game. Given the current Seat
	 * that has the turn, it sets the turn to the next Seat that is occupied by
	 * a Player with Status ACTIVE.
	 * 
	 * @param currentSeat
	 *            Seat of the player whose turn it is currently.
	 */
	private int findNextTurn(Seat currentSeat) {
		info("Finding nest trun and current turn for seat id is " + currentSeat);
		int sid = currentSeat.getSeatId();
		int initSeatId = sid;

		while (true) {
			sid--;

			if (sid < 1) {
				sid = table.getSeatInfo().getSeats().size();
			}

			if (sid == initSeatId) {
				info("Next turn can't be found");
				return 0;
			}

			if (table.getSeatInfo().getSeatById(sid).isOccupied()) {
				int playerId = table.getSeatInfo().getSeatById(sid)
						.getPlayerId();
				Player p = table.getPlayerInGameById(playerId);
				if (p == null) {
					info("Player Found null while gwttibg next turn");
				} else if (Status.ACTIVE.equals(p.getStatus()) && !p.isAllin()) {
					table.setWhoseTurn(table.getPlayerInGameById(table
							.getSeatInfo().getSeatById(sid).getPlayerId()));
					info("turn set to player Id + "
							+ table.getWhoseTurn().getPlayerId());
					return playerId;
				}
			} else {
				continue;
			}
		}

	}

	/**
	 * Create Side Pots for All In Players. Called by the betting manager at the
	 * end of each round.
	 * 
	 */
	private Player findPreviousPlayer(int sid) {
		info("finding previous player of seat id " + sid);
		Player p = null;
		while (true) {
			sid++;
			
			if (sid > table.getSeatInfo().getSeats().size()) {
				sid = 1;
			}
			
			if(table.getSeatInfo().getSeatById(sid).isOccupied()) {

				p = table.getPlayerInGameById(table.getSeatInfo().getSeatById(sid).getPlayerId());
				if (p == null || p.isPack() || p.isAllin()) {
					info("Player Found null or packed or allin while finding previous player");
				} else {
					info("previous player set to player with player id"
							+ table.getSeatInfo().getSeatById(sid)
									.getPlayerId());
					return p;
				}

			} else {
				continue;
			}
		}
		
	}



	/**
	 * Method to send SeatInfo and GameInfo to player that has joined a room. If
	 * player is joining room for the first time, a new Player object is created
	 * using name of the user and playerId.
	 * 
	 * @param user
	 *            SFS user that has joined the room.
	 * @param playerId
	 *            player id of the player that has joined the room.
	 */
	public boolean handleJoinRoom(int playerId , String playerName) {
		if (playerId <= 0) {
			return false;
		}

		synchronized (this) {
			Player p = table.getPlayerById(playerId);
			
			sendSeatInfo(playerId);
			sendGameInfo(playerId);
			
			
			if (p == null) {
				info("New Player to join room");
				// gameInfo when starts
				Player player = new Player(playerId, playerName);
				info("Player created with player Id " + playerId
						+ " and player name " + playerName);

				table.getPlayers().add(player);
				info("Player Id " + playerId + " added to table");
			} else {
				// when player already a watcher or a active player
				if (p.getSeat() != null) {
					this.gameExtension.changeSpectatorToPlayer(playerId);
				}
				
				if(this.state == GameControllerStates.GAME_STARTED &&
						playerId == table.getDealerId() && 
						table.isMixVariant()){
					
					table.setGameVariant(GameVariants.SIMPLE.toString());
					List<String> games = new ArrayList<String>();
					
					for(GameVariants var : gameVariants){
						if(!var.toString().equals(GameVariants.ALL.toString())){
							games.add(var.toString());
						}
						
					}
					
					int timer = 0;
					
					if(this.scheduleController != null && !this.scheduleController.isDone()){
						timer = (int) this.scheduleController.getDelay(TimeUnit.SECONDS);
					}
					
					if(timer > 3){
						gameExtension.sendGameChoicesToDealer(table.getDealerId(),games,timer);
					}
					
				}

			}

			
			info("GameInfo sent to player Id " + playerId);
		}
		return false;
	}

	/**
	 * Process request by client to take seat. Deducts money from user account,
	 * creates or updates entry in gt_game_account
	 * 
	 * @param playerId
	 * @param seatId
	 * @param amount
	 */
	public boolean handleTakeSeat(int playerId, int seatId, int amount) {
		if (playerId == -1 || seatId == -1) {
			return false;
		}

		synchronized (this) {

			info("Player with playerId " + playerId
					+ "requesting to take seat with seatId " + seatId
					+ " with chip amount " + amount);

			Player player = table.getPlayerById(playerId);

			writeParserLog("PLAYER TAKE SEAT REQUEST | PLAYER: "
					+ player.getName() + " | AMOUNT: " + amount);

			if (table.getSeatInfo().getSeatById(seatId).isReserved()
					&& table.getSeatInfo().getSeatById(seatId).getPlayerId() != playerId) {
				info("seat already reserved ");
				return false;
			}
			
			if( (table.getSeatInfo().getSeatById(seatId).isOccupied() ) ){
				info("seat id " + seatId + " alread occupied");
				return false;
			}

			if (player == null) {
				return false;
			}
			if (player.getSeat() != null) {
				info("Player already having a seat");
				return false;
			}
			if (table.getActivePlayers().contains(player)) {
				info("Already part of active players");
				return false;
			}

			// deduct money from user account
			// create an account in gt_game_account if exists than update

			GtGameAccounts gameAccount = GtGameAccountsUtils.findGameAccounts(
					playerId, table.getTableName());

			String chipType = this.table.getChipType();
			double chipAmount = amount;
			this.cancelSeatReservation(playerId, seatId);
			Map<String, Object> userAccount = GtUserUtils.checkPlayer(table.getDomainName(), gameExtension.getSessionKey(playerId), playerId);
			
			if(userAccount == null){
				this.cancelSeatReservation(playerId, seatId);
				gameExtension.sendMassage("INFO", MessageConstants.MSG_CONTACT_ADMIN.getMsgCode(),
						MessageConstants.MSG_CONTACT_ADMIN.getMessage(), playerId);
				gameExtension.sendSeatInfo(table.getSeatInfo());
				return false;
			}
			String remarks = "";
			int chips = 0;
			if ((chipType.equals(GtUserAccountUtils.DUMMY) && ((Double)userAccount.get(GtUserAccountUtils.DUMMY)).intValue()>= chipAmount)
					|| (chipType.equals(GtUserAccountUtils.REAL) && ((Double)userAccount.get(GtUserAccountUtils.REAL)).intValue() >= chipAmount)) {
				
				remarks = amount + " chips debited from "+ player.getName();

				ChipInfo chipInfo = new ChipInfo();
				chipInfo.setChipAmount(amount);
				chipInfo.setChipType(chipType);
				chipInfo.setSource("1");
				chipInfo.setSourceId("0"); 
				HashMap<String, Integer> orderDetails = GtUserAccountUtils.crDR(playerId, chipInfo,
						table.getGameTableId(), remarks,gameExtension.getSessionKey(playerId),table.getDomainName(), false,this.table.getGameId(),APIActionType.JOIN_GAME.getApiActionType(),table.isPrivate(),
						GameVariants.getGameVariantVal(table.getGameVariant()));
				int orderId = orderDetails.get("orderId");
				int transactionId = orderDetails.get("transId");
				takeSeatTransactionMap.put(playerId, transactionId);
				if(orderId == 0){
					this.cancelSeatReservation(playerId, seatId);
					gameExtension.sendMassage("INFO", MessageConstants.MSG_CONTACT_ADMIN.getMsgCode(),
							MessageConstants.MSG_CONTACT_ADMIN.getMessage(), playerId);
					gameExtension.sendSeatInfo(table.getSeatInfo());
					return false;
				}

				Seat seat = table.getSeatInfo().getSeatById(seatId);
				seat.setEmailId(gameExtension.getEmailId(playerId));
				player.setSeat(seat);
				player.setChips(chipAmount);
				player.setChipsLeft(chipAmount);

				info("player with playerid "
						+ playerId
						+ " given seat with seatId "
						+ seatId
						+ ". seat occupied set to true, seat reserved set to false. Playername set to "
						+ player.getName());

				writeParserLog("PLAYER SEATED | PLAYER: " + player.getName()
						+ " |  SEAT: " + seatId);

				player.setStatus(Status.TAKESEAT);
				table.addActivePlayers(player);
				//this.gameExtension.changeSpectatorToPlayer(playerId);
				info("player with playerID " + playerId
						+ " added to active players and status set to "
						+ Status.TAKESEAT);

				if (table.getPlayersOnSeat().size() >= table.getMinPlayers()
						&& GameControllerStates.IDLE.equals(state)) {
					info("SCheduled for 1 second. State set to "
							+ GameControllerStates.GAME_START);
					state = GameControllerStates.GAME_START;
					rescheduleTask(1);

				}

				
				
				player.setOrderId(orderId);
				
				info("chip amount " + amount + " of chip type " + chipType
						+ " debited from user account of player with playerId "
						+ playerId);

				gameExtension.sendSeatInfo(table.getSeatInfo());
				info("SeatInfo sent to all players");

				if (gameAccount == null) {
					GtGameAccountsUtils.saveGameAccount(playerId,
							table.getTableName(), chipAmount, chipAmount,
							GtGameAccountsUtils.IS_REG);
				} else {
					gameAccount.setChips(chipAmount);
					gameAccount.setChipsInPlay(0.0);
					gameAccount.setStatus(GtGameAccountsUtils.IS_REG);
					gameAccount.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					GtGameAccountsUtils.updateGameAccount(gameAccount);
				}
				info("playerId  " + playerId + " table name "
						+ table.getTableName() + "initial chips " + chips
						+ " chip bet amount " + chipAmount);

			} else {

				this.cancelSeatReservation(playerId, seatId);
				gameExtension.sendMassage("INFO", MessageConstants.MSG_INSUFFICIENT_BALANCE.getMsgCode(),
						MessageConstants.MSG_INSUFFICIENT_BALANCE.getMessage(), playerId);
				gameExtension.sendSeatInfo(table.getSeatInfo());

				// Commands cmd = Commands.InsifficientBalance
			}

			return true;
		}

	}

	@Override
	public boolean handleLeaveSeat(int PlayerId){
		try {
			info("Player id " + PlayerId + " doing leave seat");
			if (PlayerId == -1) {
				info("Player id " + PlayerId + ", found -1");
				return false;
			}

			synchronized (this) {
				if (!GameControllerStates.SHOW.equals(state)) {
					Player player = table.getPlayerById(PlayerId);

					if (player == null) {
						info("In handleLeaveSeat for Player id " + PlayerId
								+ " player found null");
						return false;
					}

					info("In handleLeaveSeat for Player id " + PlayerId
							+ " player found " + player.toString());

					
					if (!table.getAllPlayersSeated().contains(player)) {
						info("Player not part of active players");
						return false;
					} else {
						table.getActivePlayers().remove(player);
						info("player wit player id " + PlayerId + " removed ");
					}

					/*GtUserAccount userAccount = GtUserAccountUtils
							.findByPlayerId(PlayerId);*/

					GtGameAccounts gameAccount = GtGameAccountsUtils
							.findGameAccounts(PlayerId, table.getTableName());

					String chipType = table.getChipType();

					Integer Bal = 0;
					// Set player seat null **
					// remove from active players **
					// useraccount entry **
					// transaction history **
					// gameaccounts **

					Seat seat = table.getSeatInfo().getSeatByPlayerId(PlayerId);

					seat.setOccupied(false);
					seat.setPlayerId(-1);
					seat.setPlayerName("");
					seat.setAllIn(false);
					seat.setAway(false);
					
					seat.setLastBetAmount(0);
					seat.setLastAction("");

					seat.setChips(0);
					seat.setChipsLeft(0);
					
					// seat.setBet(0);
					seat.setPack(false);
					
					
					if(table.getDealerId() == PlayerId){
						table.setDealerId(0);
					}
					
					if(playerTopUpRequest.containsKey(PlayerId)){
						playerTopUpRequest.remove(PlayerId);
					}
					
					String msg =  player.getName() + " has left from seat.";
					
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					
					//updateDealerChatList(msg);
					
					writeParserLog("PLAYER LEAVE SEAT REQUEST | PLAYER: "
							+ player.getName() + " | CHIPS LEFT: "
							+ player.getChipsLeft());
					
					String remarks = player.getChipsLeft() + " chips credited in "
							+ player.getName();

					this.gameExtension.changePlayerToSpectator(player.getPlayerId());
					info("Current state of runstatemachine is "
							+ this.state
							+ " having player active count is "
							+ table.getActiveStatusPlayerNoAllInNoPackOnSeated()
									.size());
					if (GameControllerStates.ANTE == this.state || GameControllerStates.GAME_START == this.state
							) {
						// on seat one or less players
						if (table.getAllPlayersSeated().size() < 2) {
							info("Scheduler started for 1 second");
							info("State set to IDLE");
							state = GameControllerStates.IDLE;
							rescheduleTask(0);
						}

					} else if (GameControllerStates.BETTING == this.state) {
						sendWagerReport(PlayerId,player.getBet());
						if(table.getActiveStatusPlayerNoAllInNoPackOnSeated()
								.size() == 0 || (table.getActiveStatusPlayerNoAllInNoPackOnSeated()
										.size() == 1 && table.getWhoseTurn().getPlayerId() == PlayerId)){
							
								state = GameControllerStates.SHOW;
								info("State set to SHOW");
								rescheduleTask(0);	
										
						}
						 else {
							 if(table.getActiveStatusPlayerNoAllInNoPackOnSeated()
								.size() == 1){
									info("only 1 player not all in and not packed.  "  + table.getActiveStatusPlayerNoAllInNoPackOnSeated().get(0).toString());
									this.table.setLastActionOnTable(true);
									if(table.getActiveStatusPlayerSeated().size() <= 2){
										state = GameControllerStates.SHOW;
										info("State set to SHOW");
										rescheduleTask(0);	
									}
								}

							List<Player> remainingPlayers = table
									.getActiveStatusPlayerNoAllInNoPackOnSeated();
							boolean isItMe = false;

							if (table.getWhoseTurn().getPlayerId() == PlayerId) {
								this.findNextTurn(seat);
								table.setPreviousPlayer(findPreviousPlayer(seat
										.getSeatId()));
								isItMe = true;
							} else if (table.getPreviousPlayer() != null
									&& table.getPreviousPlayer().getPlayerId() == PlayerId) {
								
								if(table.getWhoseTurn().getPlayerTookAction().equals(PlayerActionType.SIDE_SHOW_DENIED)){
									
									table.getWhoseTurn().setTurnDataSent(false);
									findNextTurn(table.getWhoseTurn().getSeat());
									this.table.setSideShowPlayerId(-1);
									table.setPreviousPlayer(table.getWhoseTurn());
									rescheduleTask(0);
									
								}
								
							}

							Player currPlayer = table.getWhoseTurn();
							playerActions = currPlayer.getPlayerActions(true);

							boolean allSeen = true;
							
							for(Player p : remainingPlayers){
								if(p.getPlayerStatus().equals(PlayerStatus.BLIND)){
									allSeen = false;
									break;
								}
							}
							
							if (remainingPlayers.size() == 2) {
								playerActions.add(PlayerActionType.SHOW.toString());
								info("In handle leave seat active players not packed = 2");
								
							} else if (allSeen) {
								playerActions.add(PlayerActionType.SIDE_SHOW
										.toString());
							}

							if (isItMe){
								if(table.getSideShowPlayerId() == PlayerId){
									//gameExtension.sendMassageToAll("INFO", "SIDESHOW CANCELLED");
									table.setSideShowPlayerId(-1);
									currPlayer.setTurnDataSent(false);				
								}
								this.bettingManager();
							} else {
								gameExtension.sendPlayerActions(
										currPlayer.getPlayerId(), playerActions,
										currPlayer.getMinBet(),
										currPlayer.getMaxBet(),true,-1);
							}
							

						}
					}
					
					else if(GameControllerStates.DEALER_RESPONSE == this.state || GameControllerStates.GAME_STARTED == this.state){
						
						if (table.getActivePlayerNoAwayOnSeated().size() < table
								.getMinPlayers()) {
							info("Active Players less than minimum players required");

							state = GameControllerStates.IDLE;
							rescheduleTask(0);

							
						}else{
							
							if(table.getDealerId() == PlayerId && table.isMixVariant()){
								state = GameControllerStates.GAME_STARTED;
								table.setGameVariant(GameVariants.SIMPLE.toString());
								rescheduleTask(1);
							}
							
						}
						
					}
					player.reset();
					seat.setStatus(-1);
					player.setSeat(null);
					player.setNoActivityCount(0);
					
					ChipInfo chipInfo = new ChipInfo();
					chipInfo.setChipAmount(player.getChipsLeft());
					chipInfo.setChipType(chipType);
					chipInfo.setSource("0");
					chipInfo.setSourceId("2");
					HashMap<String, Integer> orderDetails = GtUserAccountUtils.crDR(PlayerId, chipInfo, table.getGameTableId(), remarks,gameExtension.getSessionKey(PlayerId),table.getDomainName(),
							true,this.table.getGameId(), APIActionType.LEAVE_SEAT.getApiActionType(),table.isPrivate(),GameVariants.getGameVariantVal(table.getGameVariant()));
					int orderId = orderDetails.get("orderId");
							if(orderId == 0){
						gameExtension.sendMassage("INFO", MessageConstants.MSG_CONTACT_ADMIN.getMsgCode(),
								MessageConstants.MSG_CONTACT_ADMIN.getMessage() + " credit transaction failed.", PlayerId);
						gameExtension.sendSeatInfo(table.getSeatInfo());
						return false;
					}
					
					info("Seat of player id" + PlayerId
							+ "set to null and occupied set to false");

					gameAccount.setStatus(GtGameAccountsUtils.IS_UNREG);
					gameAccount.setChips(0.0);
					gameAccount.setChipsInPlay(0.0);
					gameAccount.setModifiedDate(new Timestamp(System.currentTimeMillis()));
					GtGameAccountsUtils.updateGameAccount(gameAccount);

					info("Game account of player with player Id " + PlayerId
							+ " status changed to " + GtGameAccountsUtils.IS_UNREG);

					/*if (chipType.equals(GtUserAccountUtils.DUMMY)) {
						Bal = userAccount.getDummyBal();
						userAccount.setDummyBal(Bal + player.getChipsLeft());
					} else {
						Bal = userAccount.getRealBal();
						userAccount.setRealBal(Bal + player.getChipsLeft());
					}*/

				//	GtUserAccountUtils.updateUserAccount(userAccount);

				/*GtTransactionHistoryUtils.transcationHistory(PlayerId,
						chipType, table.getGameTableId(),
						player.getChipsLeft(), "cr", remarks);*/
				
				gameExtension.sendSeatInfo(table.getSeatInfo());
				info(remarks);

					return true;
				} else {
					this.gameExtension.sendMassage("ALERT",
							MessageConstants.MSG_SHOWDOWN.getMsgCode(),
							MessageConstants.MSG_SHOWDOWN.getMessage()+". Please wait till next round is over.", PlayerId);
					return false;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("leave seat error ", e);
			return false;
		}
		

	}

	private void sendSeatInfo(int playerId) {
		gameExtension.sendCommand(Commands.CMD_SEATINFO.toString(), table
				.getSeatInfo().toSFSObject(), playerId);
		info("SeatInfo sent to player with player id " + playerId);
	}

	private boolean sendGameInfo(int playerId) {

		
			info("Sending GameInfo to player id" + playerId);
			if (playerId <= 0) {
				info("Invalid playerId=" + playerId);
			}
			
			if (this.state != GameControllerStates.IDLE) {
				
				GameInfo gameInfo = new GameInfo();
				boolean isPlayer = false;
				boolean flag = false;
				PlayersInfo playersInfo = new PlayersInfo();

			

				flag = true;
				for (int i = 0; i < this.table.getActivePlayers().size(); i++) {
					Player player = (Player) this.table.getActivePlayers().get(i);
					if (player.getPlayerId() == playerId) {
						isPlayer = true;
					}
					PlayerDetails pDetails = new PlayerDetails();
					pDetails.setPlayerId(player.getPlayerId());
					if (isPlayer) {

						pDetails.setChipsLeft(player.getChipsLeft());
					} else if (player.getLastBetAmount() > 0) {
						pDetails.setChipsLeft(player.getLastBetAmount());
					}
					pDetails.setAway(player.isAway());
					pDetails.setLastAction(player.getPlayerTookAction().toString());
					pDetails.setLastBetAmount(player.getLastBetAmount());
					pDetails.setStatus(player.getStatus().toString());
					playersInfo.getPlayerInfoList().add(pDetails);
				}
				int userturnTimer = -1;
				int gameTimer = -1;
				int whoseTurnId = -1;
				int mySeatId = -1;
				int sideShowPlayerId = -1;
				int sideShowTimer = -1;
				String whoseTurnPlayerName = "";
				List<String> playerCards = new ArrayList<String>();

				if (this.state != GameControllerStates.IDLE
						&& this.state != GameControllerStates.SHOW
						&& this.state != GameControllerStates.SENDROUNDEND
						&& this.state != GameControllerStates.GAME_START) {
					if ((this.table.getWhoseTurn() != null)) {
						whoseTurnId = this.table.getWhoseTurn().getPlayerId();
						whoseTurnPlayerName = this.table.getWhoseTurn().getName();

						try {
							if ((this.scheduleController != null)
									&& (!this.scheduleController.isDone())) {
								userturnTimer = (int) (this.scheduleController
										.getDelay(TimeUnit.SECONDS) - SHORT_DELAY);
								if (userturnTimer < 0) {
									userturnTimer = 0;
								}
							}

						} catch (NullPointerException e) {
							error("sendGameinfo", e);
							e.printStackTrace();
						}

						if (whoseTurnId == playerId && table.getSideShowPlayerId() != playerId) {
							List<Double> playerBets = new ArrayList<Double>();
							playerBets.add(table.getWhoseTurn().getMinBet());
							playerBets.add(table.getWhoseTurn().getMaxBet());
							gameInfo.setPlayerActions(table.getWhoseTurn()
									.getPlayerActions(false));
							gameInfo.setPlayerBets(playerBets);

							if (userturnTimer <= 2) {
								userturnTimer = 0;
							}

						}

						if (this.table.getWhoseTurn().getPlayerTookAction()
								.equals(PlayerActionType.SIDE_SHOW_DENIED)) { // player
																				// took
																				// action
																				// of
																				// player
																				// that
																				// requested
																				// side
																				// show
																				// is
																				// side_show_denied
							sideShowPlayerId = this.findPreviousPlayer(
									table.getSeatInfo()
											.getSeatByPlayerId(whoseTurnId)
											.getSeatId()).getPlayerId();

							try {
								if ((this.scheduleController != null)
										&& (!this.scheduleController.isDone())) {
									sideShowTimer = (int) (this.scheduleController
											.getDelay(TimeUnit.SECONDS) - SHORT_DELAY);
									if (sideShowTimer < 0) {
										sideShowTimer = 0;
									}
								}

							} catch (NullPointerException e) {
								error("sendGameinfo side show timer", e);
								e.printStackTrace();
							}

						}
						
						if(table.getActivePlayerById(playerId) != null){
							if (table.getPlayerById(playerId).getPlayerStatus()
									.equals(PlayerStatus.SEEN)) {
								for (Card card : table.getPlayerById(playerId)
										.getHandCards()) {
									playerCards.add(card.toString());
								}
								playerCards.add(table.getPlayerById(playerId).getPlayerRank().getHandRank());
								gameInfo.setMyCards(playerCards);
								gameInfo.setMyRank(table.getPlayerById(playerId).getPlayerRank().getHandRank());
								gameInfo.setSeeCardsTimer(table.getSeeCardsTimer());
							}
						}
						

						/*
						 * if(this.table.getActivePlayers().size() ==2){
						 * gameInfo.getPlayerActions().add(PlayerActionType.SHOW.
						 * toString()); }
						 */
					}
					
					

				}
				if (this.state == GameControllerStates.GAME_STARTED) {
					try {
						if ((this.scheduleController != null)
								&& (!this.scheduleController.isDone())) {
							gameTimer = (int) this.scheduleController
									.getDelay(TimeUnit.SECONDS ) - SHORT_DELAY;
						}
					} catch (NullPointerException e) {
						error("sendGameinfo", e);
						e.printStackTrace();
					}
				}
				
						
				if (this.state == GameControllerStates.SHOW
						|| this.state == GameControllerStates.SENDROUNDEND) {

					if (table.getWinnerInfo() != null) {
						gameInfo.setWinInfo(table.getWinnerInfo());
					}
					List<String> pCardList = null;
					List<String> pRankList = null;
					pCardList = new ArrayList<String>();
					pRankList = new ArrayList<String>();
					String cards = "";
					
					for(Player p : this.table.getActiveStatusPlayerNoPackedOnSeated()) {
						cards = "";
						for (Card card : p.getHandCards()) {
							cards = cards + card + "$";
						}

						cards = p.getPlayerId() + "#"
								+ cards.substring(0, cards.length() - 1);
						pCardList.add(cards);

						
						pRankList.add(p.getPlayerId() + "#" + p.getPlayerRank().getHandRank());
						
					}
					
					gameInfo.setCards(pCardList);
					gameInfo.setRanks(pRankList);
					
				}
				
				if(this.state == GameControllerStates.BETTING || this.state == GameControllerStates.SHOW || this.state == GameControllerStates.SENDROUNDEND){
					
					if(table.getGameVariant().equals(GameVariants.JOKER.toString())){
						List<String> jokers = new ArrayList<String>();
						
						for(Card c : table.getJokers()){
							jokers.add(c.toString());
						}
						
						gameInfo.setJokers(jokers);
					}
					if(this.table.getPlayerById(playerId) != null &&
							this.table.getPlayerById(playerId).getAdvanceAction() != null){
						gameInfo.setAdvancePlayerAction(this.table.getPlayerById(playerId).getAdvanceAction().toString());
						gameInfo.setAdvanceBetAmount(this.table.getPlayerById(playerId).getAdvanceBetAmount());
					}
					
					if(table.getWhoseTurn().getPlayerId() != playerId ){
						Player waitingPLayer = table.getActivePlayerById(playerId);
						if(waitingPLayer != null && waitingPLayer.getSeat() != null && waitingPLayer.getStatus().equals(Status.ACTIVE)){
							waitingPLayer.setTurnDataSent(false);
							waitingPLayer.setPlayerTookAction(PlayerActionType.PACK);
							List<String> advanceActions = waitingPLayer.getPlayerActions(true);
							
							calculatePlayerBets(waitingPLayer, findPreviousPlayer(waitingPLayer.getSeat().getSeatId()));
							gameExtension.sendPlayerActions(waitingPLayer.getPlayerId(),
									advanceActions, waitingPLayer.getMinBet(), waitingPLayer.getMaxBet(),false,waitingPLayer.getAdvanceBetAmount());
							info("ACTIONS SENT in game info after disconnection | PLAYER: " + waitingPLayer.getName()
							+ " | PLAYER ACTIONS: "
							+ waitingPLayer.getPlayerActions(false).toString());
						}
					}
					
				}
				
				boolean isTopUpPending = false;
				
				if(this.playerTopUpRequest.containsKey(playerId)){
					isTopUpPending = true;
				}
				
				gameInfo.setTopUpPending(isTopUpPending);
				gameInfo.setWhoseTurnId(whoseTurnId);
				gameInfo.setDealerId(table.getDealerId());
				gameInfo.setDealerSeatId(table.getDealerSeatId());
				gameInfo.setTurnTimer(userturnTimer);
				gameInfo.setGameTimer(gameTimer);
				gameInfo.setGameState(this.state.toString());
				gameInfo.setRake(table.getRakePercentage());
				gameInfo.setSideShowPlayerId(sideShowPlayerId);
				gameInfo.setSideShowTimer(sideShowTimer);
				gameInfo.setWhoseTurnPlayerName(whoseTurnPlayerName);
				gameInfo.setGameVariant(table.getGameVariant());

				for (int i = 0; i < this.table.getPlayers().size(); i++) {
					Player player = (Player) this.table.getPlayers().get(i);
					if (((player.isAway()))
							&& (player.getSeat() != null)
							&& ((this.table.getActivePlayerIndex(player) == -1) || (!flag))) {
						PlayerDetails pDetails = new PlayerDetails();
						pDetails.setPlayerId(player.getPlayerId());
						pDetails.setLastBetAmount(0);
						pDetails.setLastAction(PlayerActionType.PACK.toString());
						pDetails.setAway(Boolean.valueOf(player.isAway()));

						playersInfo.getPlayerInfoList().add(pDetails);
					}
					if (player.getPlayerId() == playerId) {
						if (player.getSeat() != null) {
							mySeatId = player.getSeat().getSeatId();
						}
					}
				}
				gameInfo.setPotInfo(this.getTable().getPotInfo());
				gameInfo.setMySeatId(mySeatId);
				gameInfo.setPlayersInfo(playersInfo);
				gameInfo.setDealerChatLogs(table.getDealerChatLogs());
				
				this.gameExtension.sendGameInfo(playerId, gameInfo);
				
				info("game Info sent " + gameInfo.toString());
			}
			
			
			return true;
		
	}
	

	@Override
	public Table getTable() {
		// TODO Auto-generated method stub
		return table;
	}

	@Override
	public synchronized void playerTookAction(String playerAction,
			Integer playerId, Integer seatId, Integer chipAmount,boolean isUserTurn) {
		Player player = table.getPlayerById(playerId);
		player.setAdvanceActionTaken(false);
		if(isUserTurn){
			if (GameControllerStates.BETTING == this.state) {
				if(table.getWhoseTurn().getPlayerId() == playerId){
					//Player player = table.getPlayerById(playerId);
					
					player.setTurnCount(player.getTurnCount() + 1);
					player.setNoActivityCount(0);
					player.setPlayerTookAction(PlayerActionType
							.fromString(playerAction));
					
		
					if (!playerAction.equals(PlayerActionType.PACK.toString())) {
						
						if(chipAmount >= player.getMinBet() && chipAmount <= player.getMaxBet()){
							player.setLastBetAmount(chipAmount);
						}else{
							info("Player with playerID " + playerId
									+ " sent player took action  "
									+ playerAction +" with invalid bet amount. Minimum bet selected");	
							player.setLastBetAmount(player.getMinBet());
							
						}
					}/*else{
						msg = "Player  " + player.getName() + " has packed." ;
						gameExtension.sendMassageToAll("DC",msg, DEALER_CHAT_CODE);
						updateDealerChatList(msg);
					}*/
		
					if (playerAction.equals(PlayerActionType.SHOW.toString())) {
						msg = "Player  " + player.getName() + " has asked for show." ;
						gameExtension.sendMassageToAll("DC",msg, DEALER_CHAT_CODE);
						updateDealerChatList(msg);
						this.table.setLastActionOnTable(true);
					}
					rescheduleTask(0);
				}else{
					info("Player with playerID " + playerId
							+ " send player took action cmd with action type is "
							+ playerAction +" But turn is not of this player");
					//this.gameExtension.sendMassage("INFO","Your Turn time ",playerId);
				}
			} else {
				info("Player with playerID " + playerId
						+ " send player took action cmd with action type is "
						+ playerAction);
				this.gameExtension.sendMassage("INFO",
						MessageConstants.MSG_BETTING_OVER.getMsgCode(),
						MessageConstants.MSG_BETTING_OVER.getMessage(), playerId);
			}
		}else{
			
			if(player != null){
				player.setAdvanceAction(PlayerActionType.fromString(playerAction));
				player.setAdvanceBetAmount(chipAmount);
				player.setAdvanceActionTaken(true);
				info("Player with playerID " + playerId
				+ " sent player took action in advance "
				+ playerAction +" with bet amount " + chipAmount);
			}
		}
		
	}

	@Override
	public void sideShow(int playerId, int accOrDen){
		
		if( table.getSideShowPlayerId() != table.getWhoseTurn().getPlayerId()  ){
			
			writeParserLog("SIDESHOW CANCELLED | PLAYER LEFT SEAT" );
			log.info("SIDESHOW CANCELLED | PLAYER LEFT SEAT");
			
			gameExtension.sendMassageToAll("INFO", MessageConstants.MSG_SIDESHOW_CANCELLED.getMessage(),
					MessageConstants.MSG_SIDESHOW_CANCELLED.getMsgCode());
			
			this.table.setSideShowPlayerId(-1);
			gameExtension.sendSeatInfo(table.getSeatInfo());

			//rescheduleTask(1);
			
			return;
			
		}
		
		Player currPlayer = table.getPlayerInGameById(table.getSideShowPlayerId());
		Player prePlayer = table.getPlayerInGameById(playerId);
		
		currPlayer.setTurnDataSent(false);
		currPlayer.setPlayerTookAction(PlayerActionType.PACK);
		
		if (accOrDen != 0 ){
			try{
			writeParserLog("SIDESHOW ACCEPT | " + " PLAYER : " + prePlayer.getName());
			log.info("SIDESHOW ACCEPT | " + " PLAYER : " + prePlayer.getName());
			
			msg = "Side show accepted by " + prePlayer.getName();
			gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
			updateDealerChatList(msg);

			GameLogicObject object = new GameLogicObject(table.getJokers());
			BaseGameLogic gameLogic = object.getGameLogicObj(table
					.getGameVariant());

			
				gameLogic.assignPlayerRank(currPlayer);
			
			gameLogic.assignPlayerRank(prePlayer);

			gameLogic.compare2Players(currPlayer, prePlayer);

			if (currPlayer.isWinner() || prePlayer.isWinner()) { // if one
																	// winner
																	// than here
				if (!currPlayer.isWinner()) {

					writeParserLog("SIDESHOW  | " + " WINNER : " +prePlayer.getName());
					log.info("SIDESHOW  | " + " WINNER : " +prePlayer.getName());
					
					msg = "Side show winner : " + prePlayer.getName();
					gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
					updateDealerChatList(msg);
					
					currPlayer.setPack(true);
					currPlayer.setStatus(Status.INACTIVE);
					gameExtension.sendPlayerStatus(currPlayer.getPlayerId(), false,  PlayerActionType.PACK.toString());
					
					prePlayer.setWinner(false);
				} else {
					currPlayer.setWinner(false);
					if (!prePlayer.isWinner()) {
						writeParserLog("SIDESHOW  | " + " WINNER : " +currPlayer.getName());
						log.info("SIDESHOW  | " + " WINNER : " +currPlayer.getName());
						
						msg = "Side show winner : " + currPlayer.getName();
						gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
						updateDealerChatList(msg);
						
						prePlayer.setPack(true);
						prePlayer.setStatus(Status.INACTIVE);
						gameExtension.sendPlayerStatus(prePlayer.getPlayerId(), false,  PlayerActionType.PACK.toString());
					} else {
						prePlayer.setWinner(false);
					}
				}
				
				currPlayer.setWinner(false);
				prePlayer.setWinner(false);
				
			} else {// if no winner means both have same rank and card
				writeParserLog("SIDESHOW  | " + " NO WINNER | PACKED PLAYER : "
						+ currPlayer.getName());
				
				msg = "Side show tied, " + currPlayer.getName() + " packed";
				gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
				updateDealerChatList(msg);
				
				currPlayer.setPack(true);
				currPlayer.setStatus(Status.INACTIVE);
				gameExtension.sendPlayerStatus(currPlayer.getPlayerId(), false,
						PlayerActionType.PACK.toString());
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				error("While side show", e);
				e.printStackTrace();
			}
		} else {
			
			for(Player p : table.getActivePlayers()){
				if(p.getPlayerId() != playerId){
					gameExtension.sendMassage("INFO", MessageConstants.MSG_SIDESHOW_DENIED.getMsgCode(),
							MessageConstants.MSG_SIDESHOW_DENIED.getMessage(), p.getPlayerId());
				}
			}
			
		}
		
		this.table.setSideShowPlayerId(-1);
		gameExtension.sendSeatInfo(table.getSeatInfo());
		table.setPreviousPlayer(currPlayer);
		Seat currentSeat = currPlayer.getSeat();
		this.findNextTurn(currentSeat);

		rescheduleTask(1);

	}
	
	

	@Override
	public synchronized void sendCards(int playerId, int seeIndex) {
		
		// update boolean variable allBlind to false since a player has seen his cards.
		// now after 3 rounds, first blind player from dealer will have to see his cards.
		table.setAllBlind(false);
		
		Player seenPlayer = table.getPlayerInGameById(playerId);
		if (seenPlayer == null) {
			info("Player is not active");
			return;
		}
		
		// Calculate Hand rank of player
		GameLogicObject obj = new GameLogicObject(table.getJokers());
		BaseGameLogic gameLogic = obj.getGameLogicObj(table.getGameVariant());
		try {
			gameLogic.assignPlayerRank(seenPlayer);
		} catch (Exception e) {
			error("While see cards", e);
			e.printStackTrace();
		}
		String playerRank = seenPlayer.getPlayerRank().getHandRank();
		
		// get see cards timer
		int timer  = this.table.getSeeCardsTimer();
		
		// prepare cards to send if only 1 card is to be sent
		List<String> playerCards =new ArrayList<String>();
		if(seeIndex == -1){
			for (Card card : seenPlayer.getHandCards()) {
				playerCards.add(card.toString());
			}																							//Removed cards and rank details against ticket GTTP-1039
			gameExtension.sendMassage("DC", DEALER_CHAT_CODE  , "You have seen your cards.", playerId);// Cards : " + seenPlayer.getHandCards().toString() + " , card rank : " + seenPlayer.getPlayerRank().getHandRank(), playerId);
		}else{
			playerCards.add(seenPlayer.getHandCards().get(seeIndex).toString());
		}
		
		// set no activity count and player status to seen
		seenPlayer.setNoActivityCount(0);
		if (PlayerStatus.SEEN.equals(seenPlayer.getPlayerStatus()) || this.table.getSideShowPlayerId() == playerId) {
			gameExtension.sendPlayerCards(playerCards,playerRank,timer, playerId);
			seenPlayer.setPlayerStatus(PlayerStatus.SEEN);
			return;
		} 
		seenPlayer.setPlayerStatus(PlayerStatus.SEEN);
		info("Player id " + playerId + " player status set to seen");
		
		// update logs - debugging, info, dealer chat
		writeParserLog("PLAYER SEE CARDS | PLAYER: " + seenPlayer.getName() +  " | STATUS: " + seenPlayer.getPlayerStatus() + " CARDS: " + seenPlayer.getHandCards().toString());
		msg = "Player " + seenPlayer.getName() + " has seen cards." ;
		gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
		updateDealerChatList(msg);

		// send cards to player
		gameExtension.sendPlayerCards(playerCards,playerRank,timer, playerId);
		info("cards sent to player id " + playerId);
		
		// update current turn player's actions and bets.
		Player whoseTurn = table.getWhoseTurn();
		info("Player with player ID " + whoseTurn.getPlayerId()
				+ " having turn so setting chaal");
		List<String> playerActions = whoseTurn.getPlayerActions(true);

		List<Player> remainingPlayers = table
				.getActivePlayersNotAwayNotPacked();
		boolean allSeen = true;
		for(Player player : remainingPlayers){
			if(player.getPlayerStatus().equals(PlayerStatus.BLIND)){
				allSeen = false;
				break;
			}
		}
		if (remainingPlayers.size() == 2) {
			playerActions.add(PlayerActionType.SHOW.toString());
		} else if (table.isAllSeen() && table.getRoundCounter()>=3) {
			playerActions.add(PlayerActionType.SIDE_SHOW.toString());
		}
		//GV-logs-print both players befre and after calculations(b4 sending)
		//calculatePlayerBets(whoseTurn, table.getPreviousPlayer());
		if(whoseTurn.getPlayerId() == seenPlayer.getPlayerId()) {
			calculatePlayerBets(whoseTurn, findPreviousPlayer(whoseTurn.getSeat().getSeatId()));
			// update logs
			info("player actions sending " + playerActions);
			info(" player bets " + whoseTurn.getMinBet() + " " + whoseTurn.getMaxBet());
		}
		
		// send player actions
		if(whoseTurn.isTurnDataSent()){
			gameExtension.sendPlayerActions(whoseTurn.getPlayerId(), playerActions,
					whoseTurn.getMinBet(), whoseTurn.getMaxBet(),true,-1);
		}
		
		// send advance actions and updated bets to currently seen player
		if(seenPlayer.getPlayerId() != whoseTurn.getPlayerId() && !seenPlayer.isTurnDataSent()){
			seenPlayer.setAdvanceAction(null);
			seenPlayer.setAdvanceBetAmount(0);
			List<String> advanceActions = seenPlayer.getPlayerActions(true);
			//calculatePlayerBets(seenPlayer, findPreviousPlayer(seenPlayer.getSeat().getSeatId()));
			calculateNewPlayerBets(seenPlayer);
			gameExtension.sendPlayerActions(seenPlayer.getPlayerId(),
					advanceActions, seenPlayer.getMinBet(), seenPlayer.getMaxBet(),false,-1);
			
			info("ACTIONS SENT | PLAYER: " + seenPlayer.getName()
			+ " | PLAYER ACTIONS: "
			+ seenPlayer.getPlayerActions(false).toString());
			
			// update logs
			info("player actions sent " + playerActions);
			info(" player bets sent" + seenPlayer.getMinBet() + " " + seenPlayer.getMaxBet());
			
		}
		
		/*// update logs
		info("player actions sent " + playerActions);
		info(" player bets sent" + whoseTurn.getMinBet() + " " + whoseTurn.getMaxBet());
		*/
		// send player status
		gameExtension.sendPlayerStatus(playerId, false,
				PlayerActionType.SEEN.toString());
		info("player status of player id " + playerId + " sent to all players");
		
		// when any player sees cards, update round counter to 0. then wait for 3 rounds,
		// after 3 rounds if no other player has seen cards, then make the first blind player seen.
		table.setRoundCounter(0);
		info("round counter updated to 0.");
	}

	@Override
	public boolean reserveSeat(int playerId, int seatId, boolean isWaiting) {
		log.info("[3Patti],[" + table.getGameConfigId() + "],["
				+ table.getGameId()
				+ "], Request for reserve seat by playerId = " + playerId
				+ " for seatId = " + seatId);
		Seat seat = table.getSeatInfo().getSeatById(seatId);
		
		Seat seatOccupied = table.getSeatInfo().getSeatByPlayerId(playerId);
		
		if(seatOccupied != null){
			log.info("player id " + playerId + " already sitting on seat id " + seatId );
			return false;
		}
		
		if (seat == null) {
			log.info("[3Patti],[" + table.getGameConfigId() + "],["
					+ table.getGameId() + "], Seat is null");
		}
		synchronized (this) {
			if(table.getPlayerById(playerId) == null){
				info("player not part of table");
				return false;
			}
			
			if (seat.isOccupied()) {
				log.info("[3Patti],[" + table.getGameConfigId()
						+ "], Seat already occupied.");
				sendSeatInfo(playerId);
				return false;
			}
			if (seat.isReserved()) {
				sendSeatInfo(playerId);
				log.info("[3Patti],[" + table.getGameConfigId()
						+ "], Seat already occupied.");
				return false;
			}
			// hit to check having sufficient chips or not
			Map<String, Object> account = GtUserUtils.checkPlayer(table.getDomainName(), gameExtension.getSessionKey(playerId), playerId);
			if(account == null){
				info("account found null");
				gameExtension.sendMassage("INFO", MessageConstants.MSG_SESSION_EXPIRED.getMsgCode(),
						MessageConstants.MSG_SESSION_EXPIRED.getMessage(), playerId);
				sendSeatInfo(playerId);
				return false;
			}
			int userBalance = 0;
			if (this.table.getChipType().equals(GtUserAccountUtils.DUMMY)) {
				userBalance = ((Double) account.get(GtUserAccountUtils.DUMMY)).intValue();
			} else {
				userBalance = ((Double) account.get(GtUserAccountUtils.REAL)).intValue();
			}
			
			boolean hasSufficiebtAmount = false;
			if(userBalance >= table.getMinBuyIn()){
				hasSufficiebtAmount = true;
			}
			if (!hasSufficiebtAmount) {
				sendSeatInfo(playerId);
				this.gameExtension.sendMassage("ALERT",
						MessageConstants.MSG_INSUFFICIENT_BALANCE.getMsgCode(),
						MessageConstants.MSG_INSUFFICIENT_BALANCE.getMessage(), playerId);
				return false;
			} else {
				// this.gameExtension.sendBuyinChipInfo(this.table.getBuyinchipInfo(),
				// playerId);
			}
			seat.setReserved(true);
			seat.setPlayerId(playerId);
			table.getPlayerById(playerId).setStatus(Status.TAKESEAT);
			info("scheduling seat reservation of " + MAX_SEAT_RESERVATION_TIME
					+ " seconds for playerId=" + playerId + " on seatId="
					+ seatId);
			seat.taskHandle = this.gameExtension
					.scheduleSeatReservationExpireTask(playerId, seatId,
							MAX_SEAT_RESERVATION_TIME + 3);
			this.gameExtension.sendSeatInfo(table.getSeatInfo());

			return true;
		}
	}

	@Override
	public void cancelSeatReservation(int playerId, int seatId) {
		info("Player Id Cancel seat Reservation " + playerId
				+ " for seat Id = " + seatId);
		Seat seat = table.getSeatInfo().getSeatById(seatId);
		if (seat == null) {
			info("Seat is null or no seat available with this ID");
			return;
		}
		synchronized (this) {
			if (!seat.isReserved()) {
				info("Seat is not reserved");
				sendSeatInfo(playerId);
				return;
			}
			if (seat.isOccupied()) {
				info("Seat is not Occupied");
				sendSeatInfo(playerId);
				return;
			}
			seat.setReserved(false);
			seat.setPlayerId(-1);
			if (seat.taskHandle != null) {
				info("cancelling seat reservation scheduler on seatId="
						+ seatId
						+ " requested by playerId="
						+ playerId
						+ " timeLeft="
						+ Long.valueOf(seat.taskHandle
								.getDelay(TimeUnit.SECONDS)));
				seat.taskHandle.cancel(false);
				seat.taskHandle = null;
			}
			this.gameExtension.sendSeatInfo(table.getSeatInfo());
		}
	}

	public void info(String text) {
		log.info("[3Patti],[" + table.getGameConfigId() + "],["
				+ table.getGameId() + "]," + "[ " + table.getGameTableId()
				+ "]" + text);
	}

	public void debug(String text) {
		this.log.debug("[3Patti],[" + table.getGameConfigId() + "],["
				+ table.getGameId() + "]," + "[ " + table.getGameTableId()
				+ "]" + text);
	}

	public void error(String s, Exception e) {
		this.log.error("[3Patti],[" + table.getGameConfigId() + "],["
				+ table.getGameId() + "]," + "[ " + table.getGameTableId()
				+ "]", e);
	}

	@Override
	public boolean setPlayerStatus(int playerId, boolean isAway,
			boolean checkAway) {
		// TODO Auto-generated method stub
		if (playerId == -1) {
			info("Player Id is -1 ");
			return false;
		}
		Player p = this.table.getPlayerById(playerId);
		if (p == null) {
			return false;
		}
		info("setPlayerStatus: playerId=" + playerId);

		if ((checkAway) && (p.isAway() == isAway)) {
			info("setPlayerStatus: playerId=" + playerId + " status is same.");
			return false;
		}

		p.setAway(isAway);

		if (p.getSeat() != null) {

			this.gameExtension.sendSeatInfo(table.getSeatInfo());
		}

		try {
			if (p.taskHandle != null && !p.taskHandle.isDone()) {
				p.taskHandle.cancel(false);
			}
		} catch (NullPointerException localNullPointerException) {
			error("setPlayerStatus", localNullPointerException);
		}
		if (isAway) {

			p.taskHandle = this.gameExtension.schedulePlayerDisconnectionTask(
					playerId, MAX_DISCONNECTED_TIME);
			writeParserLog("PLAYER DISCONNECTED | PLAYER: " + p.getName()
					+ " | STATUS: " + p.getPlayerStatus().toString());
		} else {
			writeParserLog("PLAYER RECONNECTED | PLAYER: " + p.getName()
					+ " | STATUS: " + p.getPlayerStatus().toString());
		}
		return false;
	}

	@Override
	public synchronized void handleBuyInChipInfo(int playerId) {
		// TODO Auto-generated method stub
		if (this.table.getBuyinchipInfo() == null) {
			info("table having buyin chipInfo null");
			return;
		}
		Map<String, Object> account = GtUserUtils.checkPlayer(table.getDomainName(), gameExtension.getSessionKey(playerId), playerId);
		Double userBalance = 0.0;
		if (this.table.getChipType().equals(GtUserAccountUtils.DUMMY)) {
			userBalance = (Double) account.get(GtUserAccountUtils.DUMMY);
		} else {
			userBalance = (Double) account.get(GtUserAccountUtils.REAL);
		}

		this.table.getBuyinchipInfo().setBootAmount(this.table.getAnte());
		this.table.getBuyinchipInfo().setUserBalance(userBalance);
		this.gameExtension.sendBuyinChipInfo(this.table.getBuyinchipInfo(),
				playerId);
		info("buy in chip info sent to player Id " + playerId);

	}
	
	@Override
	public void handleTopUp(int playerId) {
		synchronized (this) {
			Player player = table.getPlayerById(playerId);
			if (player == null) {
				info("player found null while top up");
				return;
			}
			if (this.playerTopUpRequest.containsKey(playerId)) {
				playerTopUpRequest.remove(playerId);
			}
			Map<String, Object> account = GtUserUtils.checkPlayer(table.getDomainName(),
					gameExtension.getSessionKey(playerId), playerId);
			Double userBalance = 0.0;
			if (this.table.getChipType().equals(GtUserAccountUtils.DUMMY)) {
				userBalance = (Double) account.get(GtUserAccountUtils.DUMMY);
			} else {
				userBalance = (Double) account.get(GtUserAccountUtils.REAL);
			}
			BuyInChipInfo buyInChipInfo = new BuyInChipInfo();
			buyInChipInfo.setBootAmount(this.table.getAnte());
			buyInChipInfo.setChipType(this.table.getChipType());
			buyInChipInfo.setUserBalance(userBalance);
			buyInChipInfo.setGameType(this.table.getGameVariant());
			if (player.getSeat() == null) {
				gameExtension.sendMassage("INFO", MessageConstants.MSG_TAKE_SEAT.getMsgCode(),
						MessageConstants.MSG_TAKE_SEAT.getMessage(), player.getPlayerId());
				return;
			}
			if (player.getChipsLeft() >= this.table.getMaxBuyIn()) {
				gameExtension.sendMassage("INFO", MessageConstants.MSG_MAX_MONEY.getMsgCode(),
						MessageConstants.MSG_MAX_MONEY.getMessage(), player.getPlayerId());
				return;
			} else if (player.getChipsLeft() >= table.getMinBuyIn()) {
				buyInChipInfo.setBuyInLow(1);
				buyInChipInfo.setBuyInHigh(this.table.getMaxBuyIn() - (int)player.getChipsLeft());
			} else {
				buyInChipInfo.setBuyInLow(this.table.getMinBuyIn() - (int)player.getChipsLeft());
				buyInChipInfo.setBuyInHigh(this.table.getMaxBuyIn() - (int)player.getChipsLeft());
			}
			
			if(userBalance < buyInChipInfo.getBuyInLow()){
				this.gameExtension.sendMassage("INFO", MessageConstants.MSG_INSUFFICIENT_BALANCE.getMsgCode(),
						MessageConstants.MSG_INSUFFICIENT_BALANCE.getMessage(), playerId);
				info("buy in chip info NOT sent to player Id " + playerId + ". Insufficient funds." + userBalance + " " + buyInChipInfo.toString());
				return;
			}else if(userBalance <= buyInChipInfo.getBuyInHigh()){
				buyInChipInfo.setBuyInHigh(userBalance.intValue());
			}
			this.gameExtension.sendBuyinChipInfo(buyInChipInfo, playerId);
			info("buy in chip info sent to player Id " + playerId);
		}
		
	}

	public boolean handleExitRoom(int playerId) {
		if (playerId <= 0) {
			info("While exit Room PlayerId is 0 or less");
			return false;
		}
		synchronized (this) {
			Player p = table.getPlayerById(playerId);
			if (p == null) {
				info("No player with player id " + playerId
						+ " while exit room");
				return false;
			}
			if (!handleLeaveSeat(playerId)) {
				info("Player not able to leave seat or not seating on a seat");
			}
			try {
				if (p.getSeat() == null) {
					this.table.getPlayers().remove(p);
				}
				info("Player details removed from the Table details.");
			} catch (Exception e) {
				info("Player P is NULL. Returning from leaveTable().");
				info("Exception during removing Player corresponding to playerId="
						+ playerId + "  from Player list." + e);
				error("handleEXitRoom", e);
				return false;
			}
		}

		return false;

	}

	private int getRemainingTurnTime() {
		int userturnTimer = -1;
		try {
			if ((this.scheduleController != null)
					&& (!this.scheduleController.isDone())) {
				userturnTimer = (int) (this.scheduleController
						.getDelay(TimeUnit.SECONDS) - SHORT_DELAY);
				info("Player having turn time remaining is = " + userturnTimer);
				if (userturnTimer < 0) {
					userturnTimer = 0;
				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			error("getRemainingTurnTime", e);
		}
		return userturnTimer;
	}

	private void returnExcessAmount(PotInfo potInfo) {
		info("Returning access amount to players");

		for (Pot pot : potInfo.getPots()) {

			 if(pot.getChips()>0){
			boolean flag = false;
			//if (pot.getPlayerIds().size() == 1) {
		//	for(Seat seat : this.getSeatInfo().getSeats()){
				
				//int chip = seat.getBet();
				if(pot.getPlayerIds().size() == 1 ){
					Player player = this.table.getPlayerById(pot.getPlayerIds().get(0));
					if (player != null) {
						if (player.getSeat() != null) {
							player.setChipsLeft(player.getChipsLeft()
									+pot.getChips());
							player.setBet(player.getBet()-(int)pot.getChips());
							GtTransactionHistoryUtils.transcationHistory(player.getPlayerId(), this.table.getChipType(), 
									this.table.getGameTableId(), pot.getChips(), APIActionType.RETURN_ACCESS_AMOUNT.getApiActionType(),
									"Return Exccess Amount", table.getDomainName(), SERVER_PRIVATE_KEY, this.table.getGameId(), player.getOrderId(),table.isPrivate()?1:0,player.getChipsLeft(),
									GameVariants.getGameVariantVal(table.getGameVariant()));
						} else {
							flag = true;
	
						}
					} else {
						flag = true;
					}
					
					if (flag) {
						ChipInfo chipInfo = new ChipInfo();
						chipInfo.setChipAmount(pot.getChips());
						chipInfo.setChipType(this.table.getChipType());
						chipInfo.setSource("0");
						chipInfo.setSourceId("10");
						String remarks = " Returning excess amount";
						GtUserAccountUtils.crDR(pot.getPlayerIds().get(0), chipInfo,this.table.getGameTableId(), remarks,gameExtension.getSessionKey(pot.getPlayerIds().get(0)),
								table.getDomainName(), true,this.table.getGameId(),APIActionType.RETURN_ACCESS_AMOUNT.getApiActionType(),table.isPrivate(),
								GameVariants.getGameVariantVal(table.getGameVariant()));
							
					}
					writeParserLog("RETURNING EXCESS AMOUNT | PLAYER : " + ((player == null)? pot.getPlayerIds().get(0) : player.getName()) + " | AMOUNT : " + pot.getChips());
					this.gameExtension.sendMassageToAll("DC", " Returning excess amount "+pot.getChips()+" Chips to Player "+((player == null)? "N/A" : player.getName()), DEALER_CHAT_CODE);
					pot.setChips(0);
					
				}
			//}
				
			//}

			 }
		}

	}

	@SuppressWarnings("unchecked")
	
	private void prepareWinnerInfo(List<Player> playerList, PotInfo potInfo) throws Exception {
		WinnerInfo winnerInfo = new WinnerInfo();
		info("Inside prepareWinnerInfo");
		
		info("Final PotInfo " + potInfo.toString());
		info("Rake percentage " + table.getRakePercentage());
		GameLogicObject obj = new GameLogicObject(table.getJokers());
		BaseGameLogic gameLogic = obj.getGameLogicObj(table.getGameVariant());
		Pot pot = null;
		double amtToDeduct = 0;
		int rakePercent = table.getRakePercentage();
		double rakePerPot = 0;
		double rakeCollected = 0;
		List<Player> winnerList =null;
		Map<Integer, Double> lostPlayerMoney = new HashMap<Integer,Double>();
		Player playerLostBet =null;
		try {
			for (int i = 0; i < potInfo.getPots().size(); i++) {
				log.info("Pot info " + potInfo.toString());
				winnerList = new ArrayList<Player>();
				pot = potInfo.getPots().get(i);
				if (pot.getChips() > 0) {

					
					int count = 0;
					// call method to get winnerlist
					List<Integer> playerIds = new ArrayList<Integer>();
					log.info("Player wise chip for pot " + pot.getPlayerIds());
					for(int playerId : pot.getPlayerIds() ){
						if(!lostPlayerMoney.containsKey(playerId)) {
							playerLostBet = this.getTable().getActivePlayerById(playerId);
							if(playerLostBet !=null) {
								lostPlayerMoney.put(playerId, (double)playerLostBet.getBet());
							}
						}
						Player player = this.table.getPlayerInGameById(playerId);
						if(player!=null){
							playerIds.add(playerId);
						}
						
					}
					if(playerIds.size() >1){
						
						winnerList = this.calculatePotWinner(playerIds, gameLogic);
						
					}else{
						if(playerIds.size() == 1){
							Player player = this.table.getPlayerInGameById(playerIds.get(0));
							winnerList.add(player);
							gameLogic.assignPlayerRank(player);
						}
					}
					
					// i = 0 is main pot, winner of main pot will be dealer
					if(i == 0 && !winnerList.isEmpty()){
						table.setDealerId(winnerList.get(0).getPlayerId());
						table.setDealerSeatId(winnerList.get(0).getSeat().getSeatId());
						info("Dealer id set to " + table.getDealerId());
						info("Dealer seat id set to " + table.getDealerSeatId());
					}
					
					info("List of Winners  " + winnerList.toString());
					
					if(!winnerList.isEmpty()){
						rakePerPot = pot.getChips() * rakePercent / 100;
						rakeCollected = rakeCollected + rakePerPot;
						pot.setChips(pot.getChips() - rakePerPot);
						info("rake for this pot " + rakePerPot);
					}
					
					writeParserLog("POT INFO | " + pot.getChips());
					for (Player player : winnerList) {
						if (playerIds != null
								&& playerIds.contains(player.getPlayerId())) {
							
							count++;
						}
					}

					if (count > 0) {
						amtToDeduct =   Math.round(pot.getChips() / count);
					}
					
					Winner winner = null;
					for (Player player : winnerList) {
						winner = new Winner();

						if (playerIds.contains(player.getPlayerId())) {
							winner.setPotId(i);
							winner.setPlayerId(player.getPlayerId());
							
						
							if(count == 1){
								
								winner.setAmount(pot.getChips());
								lostPlayerMoney.put(player.getPlayerId(), lostPlayerMoney.get(player.getPlayerId())-pot.getChips());
							}else{
								
								winner.setAmount(amtToDeduct);
								lostPlayerMoney.put(player.getPlayerId(), lostPlayerMoney.get(player.getPlayerId())-amtToDeduct);
							}
							
							count--;
							
							pot.setChips(pot.getChips() - winner.getAmount());
							player.setChipsLeft(player.getChipsLeft()
									+ winner.getAmount());
							
							winnerInfo.getWinnerList().add(winner);
							String remarks = table.getPlayerById(winner.getPlayerId()).getName() 
									+ " won " + winner.getAmount();
							
							writeParserLog("WINNER_LIST  | " + " WINNER : "
									+ player.getPlayerId()
									+ " | GAME_ID : " + this.table.getGameId()
									+ " | WIN_AMOUNT : " + winner.getAmount()
									+ " | CHIPS_LEFT : " + player.getChipsLeft()
									+ " | STATUS : winner"
									+ " | REMARK : " + remarks
									+ " | RAKE_PERCENT: " + table.getRakePercentage()
									+ " | HAND_CARDS: "+player.getHandCards());
							//0-Admin, 1-JoinGame, 2-GameCancel 3-GameWon, 4-FriendRefferalBonus, 5-BonusExpired, 6-Promocode, 7-Deposit, 8-Withdraw, 9-BonusOnDeposit, 10-LeaveSeat, 11-Affiliate_deposit, 12-Affiliate_withdraw,13 - Retrun Access Amount
							GtTransactionHistoryUtils.transcationHistory(
									winner.getPlayerId(),
									this.table.getChipType(),
									this.table.getGameTableId(),
									winner.getAmount(), APIActionType.GAME_WON.getApiActionType(), remarks,table.getDomainName(), SERVER_PRIVATE_KEY,this.table.getGameId(),player.getOrderId(),table.isPrivate()?1:0,player.getChipsLeft(),
									GameVariants.getGameVariantVal(table.getGameVariant()));
							info("Winner data set " + winner.toString());
						}

					}

				}else{
					
				}
			}
			
			String msg = "";
			
			for (Map.Entry<Integer,Double> playerMap : lostPlayerMoney.entrySet())
			{
				if(playerMap.getValue() >0) {
					playerLostBet = this.getTable().getActivePlayerById(playerMap.getKey());
					GtTransactionHistoryUtils.transcationHistory(
							playerMap.getKey(),
							this.table.getChipType(),
							this.table.getGameTableId(),
							playerMap.getValue(), APIActionType.GAME_LOST.getApiActionType(), "Player Lost amount "+playerMap.getValue(),table.getDomainName(), SERVER_PRIVATE_KEY,this.table.getGameId(),playerLostBet.getOrderId(),table.isPrivate()?1:0,playerLostBet.getChipsLeft(),
							GameVariants.getGameVariantVal(table.getGameVariant()));
					info("Lost data set " + playerMap.getKey()+ "  amount "+ playerMap.getValue());
				}
			}
			
			for(Winner w : winnerInfo.getWinnerList()){
				
				msg =  table.getPlayerById(w.getPlayerId()).getName() + " won " + w.getAmount() + " chips.";// Removed against ticket GTTP-1039: Hand Rank is "+table.getPlayerById(w.getPlayerId()).getPlayerRank().getHandRank();
				
				gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
				
				updateDealerChatList(msg);
				
			}

			info("rake collected " + rakeCollected);
			/*GtUserAccount golden = GtUserAccountUtils
					.findByPlayerId(GOLDEN_PLAYER_ID);

			if (table.getChipType().equals(GtUserAccountUtils.DUMMY)) {

				golden.setDummyBal(golden.getDummyBal() + rakeCollected);

			} else if (table.getChipType().equals(GtUserAccountUtils.REAL)) {

				golden.setRealBal(golden.getRealBal() + rakeCollected);
			}*/

			//GtUserAccountUtils.updateUserAccount(golden);
			String remarks = "Admin gets Rake Amount "+rakeCollected;
			
			/*ChipInfo chipInfo = new ChipInfo();
			chipInfo.setChipAmount(rakeCollected);
			chipInfo.setChipType(table.getChipType());
			chipInfo.setSource("0");*/
			info("WinnerList "+winnerInfo.getWinnerList().toString()+" size of winnerlist is "+winnerInfo.getWinnerList().size());
			if(winnerInfo.getWinnerList().size() > 0 ){
				info("Winner List Non Zero "+remarks);
				info("Paremeters "+ADMIN_ID+" "+ this.table.getChipType() +" "+this.table.getGameTableId()+" "+rakeCollected+" "+APIActionType.RAKE.getApiActionType()+" "+remarks+""+table.getDomainName()+" "+this.table.getGameId()+" "+SERVER_PRIVATE_KEY +"");
				GtTransactionHistoryUtils.transcationHistory(
						ADMIN_ID ,
						this.table.getChipType(),
						this.table.getGameTableId(),
						rakeCollected,APIActionType.RAKE.getApiActionType(), remarks,table.getDomainName(), SERVER_PRIVATE_KEY,this.table.getGameId(),0,table.isPrivate()?1:0,0,
						GameVariants.getGameVariantVal(this.table.getGameVariant()));
			}
			
			
			table.setWinnerInfo(winnerInfo);
		} catch (Exception e) {
			log.error("error inside Prepare winner info ", e);
			throw e;
		}
	}


	public boolean playerDisconnectionTimeout(int playerId) {
		Player p = this.table.getPlayerById(playerId);
		if (p == null) {
			debug("playerDisconnectionTimeout: Player was found for PlayerId="
					+ playerId);
			return false;
		}
		if (p.isAway() && !Status.ACTIVE.equals(p.getStatus())) {
			handleExitRoom(playerId);
			this.gameExtension
					.leaveRoomOnDisconnection(playerId,
							"Re connection failed. You are now being redirected to the lobby.");

			info("Player Disconnected Event due to time out for the player="
					+ Integer.valueOf(playerId) + " so kicked him from table.");
		} else {
			debug("playerDisconnectionTimeout: player is connected, but this timer was not canceled");
			if (Status.ACTIVE.equals(p.getStatus())) {
				this.setPlayerStatus(p.getPlayerId(), p.isAway(), false);
			}

		}
		return true;
	}

	/*
	 * @desc : sending all player cards to all
	 * 
	 * @arguments : NA
	 * 
	 * @return : NA
	 */
	private void sendWinnerCards() {
		info("Sending all player cards in show state.");
		List<String> pCardList = null;
		List<String> pRankList = null;
		pCardList = new ArrayList<String>();
		pRankList = new ArrayList<String>();
		String cards = "";
		String msg = "";
		for (Player p : this.table.getActiveStatusPlayerNoPackedOnSeated()) {
			cards = "";
			for (Card card : p.getHandCards()) {
				cards = cards + card + "$";
			}

			cards = p.getPlayerId() + "#"
					+ cards.substring(0, cards.length() - 1);
			pCardList.add(cards);

			
			pRankList.add(p.getPlayerId() + "#" + p.getPlayerRank().getHandRank());
			
			msg =   p.getName() + " | Cards : " + p.getHandCards().toString() + " | Rank : " + p.getPlayerRank().getHandRank();
			
			log.info(msg);
			
			/*if(this.table.getActivePlayersNotPacked().size() == 1){
				gameExtension.sendMassage("DC", DEALER_CHAT_CODE, msg, this.table.getActivePlayersNotPacked().get(0).getPlayerId());
			}else{
				gameExtension.sendMassageToAll("DC", msg, DEALER_CHAT_CODE);
			}*/
			
			
		}
		
		if(this.table.getActivePlayersNotPacked().size() == 1){
			for(Player player : this.table.getActivePlayersNotPacked()){
				
				this.gameExtension.sendWinnerCards(pCardList,player.getPlayerId());
				this.gameExtension.sendPlayerHandRanks(pRankList,player.getPlayerId());
				
			}
		}else{
			
			for(Player p : this.table.getPlayers()){
				this.gameExtension.sendWinnerCards(pCardList,p.getPlayerId());
				this.gameExtension.sendPlayerHandRanks(pRankList,p.getPlayerId());
			}
			
		}
		
		
		
		

	}

	private List<Player> calculatePotWinner(List<Integer> playerIds,BaseGameLogic gameLogic ) throws Exception {

		// need to change when all player goes all in in ante
		List<Player> winnerList = new ArrayList<Player>();
		//GameLogicObject obj = new GameLogicObject();
	//	BaseGameLogic gameLogic = obj.getGameLogicObj(table.getGameVariant());
		info("game variant - " + table.getGameVariant());

		try {
			int i = 1;
			Player p1 = table.getPlayerInGameById(playerIds.get(i - 1));
			Player p2 = table.getPlayerInGameById(playerIds.get(i));

			do {
				
				if (p1 != null && p2 != null) {

					p1.setWinner(false);
					p2.setWinner(false);
					gameLogic.assignPlayerRank(p1);
					gameLogic.assignPlayerRank(p2);

					info("determining winner");
					info("Player 1 = " + p1.toString() + " hand cards "
							+ p1.getHandCards());
					info("Player 2 = " + p2.toString() + " hand cards "
							+ p2.getHandCards());

					gameLogic.compare2Players(p1, p2);

					if (p1.isWinner()) {
						info("Winner is " + p1.toString());
						if(!winnerList.contains(p1)){
							winnerList.clear();
							winnerList.add(p1);
						}
						p2 =null;
						

					} else if (p2.isWinner()) {
						info("Winner is " + p2.toString());
						if(!winnerList.contains(p2)){
							winnerList.clear();
							winnerList.add(p2);
						}
						p1=null;
						
					} else {
						info("both players tied");
						info("p1 rank" + p1.getPlayerRank().getHandRank());
						info("p2 rank" + p2.getPlayerRank().getHandRank());
						if(p1.getPlayerRank().getHandRank().equals(CardRank.HR_TRIO.toString())
								&& gameLogic.getTrioCard(p1.getHandCards()) == FaceValue.ACE.value){

							if(!winnerList.contains(p1)){
								winnerList.add(p1);
							}
							
							if(!winnerList.contains(p2)){
								winnerList.add(p2);
							}
							
							info("both players trio of ace " + winnerList.toString());
							if (playerIds.size() >= i + 1) {
								info(playerIds + " .more players are there. i = " + i);
								p2 = null;
							}
						}else{
							if (!winnerList.contains(p1) && (p2.getPlayerTookAction().equals(PlayerActionType.SHOW) || p2.isAllin())) {
								info("both players tied but " + p2.getName() + " asked for show or all in");
								winnerList.clear();
								winnerList.add(p1);
								p2 = null;
							}
							else if (!winnerList.contains(p2) && (p1.getPlayerTookAction().equals(PlayerActionType.SHOW) || p1.isAllin())) {
								info("both players tied but this time " + p1.getName() + " asked for show or all in");
								winnerList.clear();
								winnerList.add(p2);
								p1 = null;
							}else{

								if(!winnerList.contains(p1)){
									winnerList.add(p1);
								}
								
								if(!winnerList.contains(p2)){
									winnerList.add(p2);
								}
								
								info("both players added " + winnerList.toString());
								if (playerIds.size() >= i + 1) {
									info(playerIds + " . even more players are there. i = " + i);
									p2 = null;
								}else{
									break;
								}
							}
						}
						
					}
				}else{
					i = i+1;
					if(p1 == null && p2!=null){
						if (playerIds.size() > i) {
							p1 = table.getPlayerInGameById(playerIds.get(i));
						}
					}
					else if(p2 ==null && p1!=null){
						if (playerIds.size() > i) {
							p2 = table.getPlayerInGameById(playerIds.get(i));
						}
					}else if(p1 == null && p2 ==null){
						p1 = table.getPlayerInGameById(playerIds.get(i));
						p2 = table.getPlayerInGameById(playerIds.get(i+1));
					}
				}
				//i++;

			} while (i < playerIds.size());
			
			
			
			
		} catch (Exception e) {
			log.error("inside calculate pot winner " , e );
			throw e;
		}
		return winnerList;

	}

	private void writeParserLog(String str) {
		this.parserLogger.writeLog(str);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		info("destroy method inside controller");
		for(Player p : this.table.getPlayers()){
			handleExitRoom(p.getPlayerId());
		}
		
		
		
	}

	@Override
	public void setGameVariant(String gameVariant) {
		if(GameControllerStates.GAME_STARTED == this.state) {
			if(gameVariant != null && !gameVariant.isEmpty()){
				info("setting game variant " + gameVariant);
				this.table.setGameVariant(gameVariant);
				rescheduleTask(0);
			}else{
				info("game variant received empty or null");
			}
		}else {
			info("game variant received Late after game started.");
		}
		
	}

	@Override
	public SeatInfo getSeatInfo() {
		log.info("inside get seat Info ");
		return this.seatInfo;
	}

	@Override
	public void topUp(int playerId, int amount) {
		
		synchronized (this) {
			Player player = this.table.getPlayerById(playerId);
			if(player == null){
				info("player found null in top up");
				return;
			}
			
			playerTopUpRequest.put(player.getPlayerId(), amount);
			gameExtension.sendMassage("INFO", MessageConstants.MSG_TOPUP_REQUEST_SUCCESS.getMsgCode(), 
					amount + MessageConstants.MSG_TOPUP_REQUEST_SUCCESS.getMessage(), playerId);
		}
		
		
	}

	@Override
	public void sendRoundInfo(int playerId) {
		if(table.getRoundInfo() != null){
			info("sending round info to player id " + playerId + " " + table.getRoundInfo().toString());
			gameExtension.sendCommand(Commands.CMD_ROUNDINFO.toString(), table.getRoundInfo().toSFSObject(), playerId);
		}else{
			gameExtension.sendMassage("INFO", MessageConstants.MSG_NO_ROUND_HISTORY.getMsgCode(),
					MessageConstants.MSG_NO_ROUND_HISTORY.getMessage(), playerId);
		}
	}
	
	private List<Card> setBotCards(String gameVariant){
		Random rand = new Random();
		int  rankNumber =  7;
		if(gameVariant.equalsIgnoreCase(GameVariants.AK47.toString())){
			rankNumber =  CardRank.HR_HIGH_CARD.getRankNumber() + rand.nextInt(CardRank.HR_TRIO.getRankNumber() - CardRank.HR_HIGH_CARD.getRankNumber() + 1);
			if(rankNumber==6){
				rankNumber =8;
			}
		}else if(gameVariant.equalsIgnoreCase(GameVariants.MUFLIS.toString())){
			rankNumber =  CardRank.HR_HIGH_CARD.getRankNumber() + rand.nextInt(CardRank.HR_COLOR.getRankNumber() - CardRank.HR_HIGH_CARD.getRankNumber() + 1);
			if(rankNumber==6){
				rankNumber =5;
			}
		}
		else{
			rankNumber =  CardRank.HR_HIGH_CARD.getRankNumber() + rand.nextInt(CardRank.HR_NORMAL_RUN.getRankNumber() - CardRank.HR_HIGH_CARD.getRankNumber() + 1);
			if(rankNumber==6){
				rankNumber =5;
			}
		} 
		info("Card rank for bot is "+rankNumber);
		return Card.getCardByRank(Card.getCardRankById(rankNumber).getRanking());
		
	}
	
	@Override
	public void setPrivatePasscode(String passcode) {
		this.passcode = passcode;		
	}

	public String getPasscode() {
		return passcode;
	}



}
