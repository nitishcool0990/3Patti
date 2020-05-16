package Gt.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.common.Player.PlayerStatus;
import Gt.common.Player.Status;
import Gt.user.utils.GtUserAccountUtils;

public class Table {
	private int turnTime = 30;					// default set to 30
	private int disconectTime;
	private int minPlayers;
	private List<Player> players = null;
	private List<Player> activePlayers = null;
	private SeatInfo seatInfo = null;
	private PotInfo potInfo = null;
	private String tableName = "";
	private int maxPlayers = 9;
	private int dealerId = 0;
	private int ante = 10;						// default set to 10
	private Player whoseTurn = null;
	private int dealerSeatId = 0;
	private Integer gameTableId = 0;
	private String chipType = "";
	private Player previousPlayer = null;
	private List<Card> deck = null;
	private String gameVariant = "";
	private int gameConfigId = -1;
	private int gameId = 0;
	private BuyInChipInfo buyinchipInfo = null;
	private String gameType = "dummy";
	private boolean createSidePot = false;
	private int maxPotLimit = -1;   			// default set to -1
	private double minBuyIn = 0;
	private double maxBuyIn = 0;
	private WinnerInfo winnerInfo = new WinnerInfo();
	private boolean lastActionOnTable = false;
	private int rakePercentage = 5;				// default set to 5			
	private int turnCounter = 0;
	private boolean isMixVariant = false;
	private Logger log = LoggerFactory.getLogger("Gt.controller.GameController");
	private List<Card> jokers = new ArrayList<Card>();
	private int bootMultiplier = 1;
	private int sideShowPlayerId = -1;
	private int seeCardsTimer = -1;
	private int maxBet = -1;					// default set to -1
	private String domainName = "http://192.168.0.51:8090/tp-api/"; 		 // "http://192.168.0.51:8090/tp-api/"; 		// "http://159.203.109.94/tp-api/"
	private int roundCounter = 0;
	private boolean allSeen = false;
	private boolean allBlind = true;
 	private List<String> dealerChatLogs = new LinkedList<String>();
	private RoundInfo roundInfo = null;
	private boolean isPrivate = false;

	public int getRoundCounter() {
		return roundCounter;
	}

	public void setRoundCounter(int roundCounter) {
		this.roundCounter = roundCounter;
	}

	public boolean isAllSeen() {
		return allSeen;
	}

	public void setAllSeen(boolean allSeen) {
		this.allSeen = allSeen;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getChipType() {
		return chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	public Integer getGameTableId() {
		return gameTableId;
	}

	public void setGameTableId(Integer gameTableId) {
		this.gameTableId = gameTableId;
	}

	public void initial() {
		gameId = ((int) (getGameConfigId() * 100000L) + 1);
		players = new ArrayList<Player>();
		activePlayers = new ArrayList<Player>();

		this.buyinchipInfo = new BuyInChipInfo();
		this.buyinchipInfo.setBuyInHigh(this.maxBuyIn);
		this.buyinchipInfo.setBuyInLow(this.minBuyIn);
		this.buyinchipInfo.setChipType(this.chipType);
		this.buyinchipInfo.setGameType(this.gameVariant);
		this.buyinchipInfo.setChips(this.minBuyIn * 2);

		this.potInfo = new PotInfo();

		this.seatInfo = new SeatInfo();

		// ante
		// user balance

	}

	public Player getWhoseTurn() {
		return whoseTurn;
	}

	public void setWhoseTurn(Player whoseTurn) {
		this.whoseTurn = whoseTurn;
	}

	public void addPlayers(Player p) {
		if (!players.contains(p)) {
			players.add(p);
		}
	}

	public List<Player> getPlayers() {
		return players;

	}

	public void addActivePlayers(Player p) {
		if (!activePlayers.contains(p)) {
			activePlayers.add(p);
		}
	}

	public List<Player> getActivePlayers() {
		return this.activePlayers;
	}
	
	public List<Player> getActiveStatusPlayerSeated() {
		log.info("inside getPlayersOnSeat");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.getPlayers()) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null && !p.getSeat().isAllIn() && Status.ACTIVE.equals(p.getStatus())) {
				activePlayer.add(p);
			}
		}
		log.info("get active status players seated " + activePlayer.toString());
		return activePlayer;
	}

	public List<Player> getPlayersOnSeat() {
		log.info("inside getPlayersOnSeat");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.getPlayers()) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null && !p.isAway()) {
				activePlayer.add(p);
			}
		}
		log.info("get active players " + activePlayer.toString());
		return activePlayer;
	}
	
	
	public List<Player> getAllPlayersSeated() {
		log.info("inside getAllPlayersSeated");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.getPlayers()) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null) {
				activePlayer.add(p);
			}
		}
		log.info("get All Players Seated " + activePlayer.toString());
		return activePlayer;
	}

	public List<Player> getActivePlayerNoAwayOnSeated() {
		log.info("inside getActivePlayerNoAwayOnSeated");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.getPlayers()) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null && p.getChipsLeft() > 0 && !p.isAway()
					&& (Status.INACTIVE.equals(p.getStatus()) || Status.TAKESEAT.equals(p.getStatus()))) {
				activePlayer.add(p);
			}
		}

		log.info("get active player no away no seated " + activePlayer.toString());
		return activePlayer;
	}

	public List<Player> getActiveStatusPlayerNoAllInNoPackOnSeated(){
		log.info("inside getActiveStatusPlayerNoAllInNoPackOnSeated");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.activePlayers) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null && !p.isAllin() && !p.isPack() && Status.ACTIVE.equals(p.getStatus())) {
				activePlayer.add(p);
			}
		}

		log.info("get active player no away no all in no pack on seated " + activePlayer.toString());
		return activePlayer;
	}

	public List<Player> getActiveStatusPlayerNoPackedOnSeated() {
		log.info("inside getActiveStatusPlayerNoPackedOnSeated");
		List<Player> activePlayer = new ArrayList<Player>();
		for (Player p : this.activePlayers) {
			log.info("running for player " + p.toString());
			if (p.getSeat() != null && !p.isPack() && Status.ACTIVE.equals(p.getStatus())) {
				activePlayer.add(p);
			}
		}

		log.info("get active player no packed on seated " + activePlayer.toString());
		return activePlayer;
	}
	
	public int getAllSeatOccOrReserver(){
		log.info("inside getAllSeatOccOrReserver");
		int numOfSeatFilled = 0;
		for (Seat seat : this.getSeatInfo().getSeats()) {
			log.info("running for player " + seat.toString());
			if(seat.isOccupied || seat.isReserved) {
				numOfSeatFilled = numOfSeatFilled +1;
			}
			
		}
		log.info("get All Players Seated or reserved " +numOfSeatFilled);
		return numOfSeatFilled;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void setActivePlayers(List<Player> activePlayers) {
		this.activePlayers = activePlayers;
	}

	public void setPlayerActive(Player player) {
		activePlayers.add(player);
	}

	public SeatInfo getSeatInfo() {
		return seatInfo;
	}

	public void setSeatInfo(SeatInfo seatInfo) {
		this.seatInfo = seatInfo;
	}

	public int getTurnTime() {
		return turnTime;
	}

	public void setTurnTime(int turnTime) {
		this.turnTime = turnTime;
	}

	public int getDisconectTime() {
		return disconectTime;
	}

	public void setDisconectTime(int disconectTime) {
		this.disconectTime = disconectTime;
	}

	public int getMinPlayers() {
		return minPlayers;
	}

	public void setMinPlayers(int minPlayers) {
		this.minPlayers = minPlayers;
	}

	public Player getPlayerById(int playerId) {
		Player player = null;
		if (playerId == -1) {
			log.info("Player Id is -1");
		} else {
			for (Player p : getPlayers()) {
				if (p.getPlayerId() == playerId) {

					player = p;
					break;
				}
			}
		}
		return player;
	}

	public Player getPlayerOnSeatedById(int playerId) {
		Player player = null;
		if (playerId == -1) {
			log.info("Player Id is -1");
		} else {
			for (Player p : this.players) {
				log.info("Player With Player id " + p.getPlayerId() + " and havling status " + p.getStatus());
				if (p.getSeat() != null && !p.isAway() && p.getPlayerId() == playerId) {
					player = p;
					break;
				}
			}
		}
		return player;
	}

	public Player getActivePlayerById(int playerId) {
		Player player = null;
		if (playerId == -1) {
			log.info("Player Id is -1");
		} else {
			for (Player p : this.activePlayers) {
				log.info("Active Player With Player id " + p.getPlayerId() + " and havling status " + p.getStatus());
				if ( p.getPlayerId() == playerId) {
					player = p;
					break;
				}
			}
		}
		return player;
	}
	
	public Player getPlayerInGameById(int playerId) {
		log.info("getting player in game by id for player id " + playerId);
		Player player = null;
		if (playerId == -1) {
			log.info("Player Id is -1");
		} else {
			for (Player p : this.activePlayers) {
				log.info("Active Player With Player id " + p.getPlayerId() + " and havling status " + p.getStatus());
				if (p.getStatus().equals(Status.ACTIVE) && !p.isPack() && p.getPlayerId() == playerId) {
					player = p;
					break;
				}
			}
		}
		return player;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public int getAnte() {
		return ante;
	}

	public void setAnte(int ante) {
		this.ante = ante;
	}

	public void reset() {
		for (Player p : this.activePlayers) {
			log.info("inside reset table method " + p.toString());
			p.reset();
		}

		// this.ante = 0;
		// this.chipType = "";
		// this.dealerId = 0;
		// this.dealerSeatId = 0;
		// this.gameTableId = 0;
		// this.gameVariant = "";
		this.previousPlayer = null;
		// this.tableName = "";
		// this.activePlayers.clear();
		// this.pot = new Pot();
		// this.minPlayers =0;
		this.deck.clear();
		this.potInfo.pots.clear();
		this.winnerInfo.getWinnerList().clear();
		this.whoseTurn = null;
		this.turnCounter = 0;
		this.ante = this.ante / this.bootMultiplier ; 
		this.bootMultiplier = 1;
		// this.activePlayers.clear();
		this.dealerChatLogs.clear();
		this.lastActionOnTable = false;
		this.allSeen = false;
		this.allBlind = true;
		roundCounter = 0;
		this.jokers.clear();

	}

	private void swap(List<Card> cards, int i, int j) {
		Card iCard = cards.get(i), jCard = cards.get(j);
		cards.set(i, jCard);
		cards.set(j, iCard);
	}

	/**
	 * Fisher-Yates Shuffle
	 * https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
	 *
	 * -- To shuffle an array a of n elements (indices 0..n-1): for i from nâˆ’1
	 * downto 1 do j â†� random integer such that 0 â‰¤ j â‰¤ i exchange a[j]
	 * and a[i]
	 *
	 * @note: Does an in place shuffle
	 */
	public void shuffle(List<Card> cards) {
		int n = cards.size();
		Random random = new Random();
		for (int i = n - 1; i > 0; i--) {
			int j = random.nextInt(i);
			swap(cards, i, j);
		}

		this.deck = cards;

	}

	public int getDealerSeatId() {
		return dealerSeatId;
	}

	public void setDealerSeatId(int dealerSeatId) {
		this.dealerSeatId = dealerSeatId;
	}

	public Player getPreviousPlayer() {
		return previousPlayer;
	}

	public void setPreviousPlayer(Player previousPlayer) {
		this.previousPlayer = previousPlayer;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public void setDeck(List<Card> deck) {
		this.deck = deck;
	}

	public int getGameConfigId() {
		return gameConfigId;
	}

	public void setGameConfigId(int gameConfigId) {
		this.gameConfigId = gameConfigId;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public BuyInChipInfo getBuyinchipInfo() {
		return buyinchipInfo;
	}

	public void setBuyinchipInfo(BuyInChipInfo buyinchipInfo) {
		this.buyinchipInfo = buyinchipInfo;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getGameVariant() {
		return gameVariant;
	}

	public void setGameVariant(String gameVariant) {
		this.gameVariant = gameVariant;
	}

	public List<Player> getActivePlayersNotAwayNotPacked() {
		List<Player> list = new ArrayList<Player>();
		for (Player p : this.activePlayers) {
			if (!p.isPack() && !p.isAllin() && Status.ACTIVE.equals(p.getStatus())) {
				list.add(p);
			}
		}
		log.info("get active players not away not packed  " + list);
		return list;
	}

	public List<Player> getActivePlayersNotPacked() {
		List<Player> list = new ArrayList<Player>();
		for (Player p : this.activePlayers) {
			if (!p.isPack() && Status.ACTIVE.equals(p.getStatus())) {
				list.add(p);
			}
		}
		
		log.info("get active players not packed  " + list);
		return list;
	}

	public PotInfo getPotInfo() {
		return potInfo;
	}

	public void setPotInfo(PotInfo potInfo) {
		this.potInfo = potInfo;
	}

	public boolean isCreateSidePot() {
		return createSidePot;
	}

	public void setCreateSidePot(boolean createSidePot) {
		this.createSidePot = createSidePot;
	}

	public boolean isRoundComplete() {
		log.info("Inside isRoundComplete ");
		Player p = this.whoseTurn;
		int whoseTurnSeatId = p.getSeat().getSeatId();
		
		int id = this.dealerSeatId;
		
		log.info("whose turn player name " + p.getName() + " seat id " + p.getSeat().getSeatId());
		log.info("Dealer " + this.seatInfo.getSeatById(id).getPlayerName() + " seat id " + id +  " ");
		
		

		while (id != whoseTurnSeatId) {
			log.info("running for  " + this.seatInfo.getSeatById(id).getPlayerName() + " seat id " + id +  " ");
			if (this.seatInfo.getSeatById(id).isOccupied() && this.seatInfo.getSeatById(id).getStatus() == 1 && !this.seatInfo.getSeatById(id).isAllIn()) {
				
				log.info("returning false for " + this.seatInfo.getSeatById(id).getPlayerName() + " seat id " + id +  " ");
				return false;
				
			}
			
			id--;

			if (id < 1) {
				id = this.seatInfo.getSeats().size();
			}
		}
		
		log.info("returning true for " + this.seatInfo.getSeatById(id).getPlayerName() + " seat id " + id +  " ");
		return true;
	}

	public int findLeastBet() {
		// TODO Auto-generated method stub
		return 0;
	}


	public int getActivePlayerIndex(Player p) {
		if (p == null) {
			return -1;
		}
		int index = -1;
		for (int i = 0; i < this.activePlayers.size(); i++) {
			Player player = (Player) this.activePlayers.get(i);
			if (player == p) {
				index = i;
				break;
			}
		}
		return index;
	}

	public int getMaxPotLimit() {
		return maxPotLimit;
	}

	public void setMaxPotLimit(int maxPotLimit) {
		this.maxPotLimit = maxPotLimit;
	}

	public double getMinBuyIn() {
		return minBuyIn;
	}

	public void setMinBuyIn(double minBuyIn) {
		this.minBuyIn = minBuyIn;
	}

	public double getMaxBuyIn() {
		return maxBuyIn;
	}

	public void setMaxBuyIn(double maxBuyIn) {
		this.maxBuyIn = maxBuyIn;
	}

	public WinnerInfo getWinnerInfo() {
		return winnerInfo;
	}

	public void setWinnerInfo(WinnerInfo winnerInfo) {
		this.winnerInfo = winnerInfo;
	}

	public boolean isLastActionOnTable() {
		return lastActionOnTable;
	}

	public void setLastActionOnTable(boolean lastActionOnTable) {
		this.lastActionOnTable = lastActionOnTable;
	}
// old pot update method changed at 31/Aug/2017
	public void initPot() {
		try {
			log.info("Inside init pot");
			List<Pot> potList = this.getPotInfo().getPots();
			double smallestBet = 0;
			int countBets = 0;
			double potAmount = 0;
			boolean allIn = false;
			Pot pot = null;
			log.info("pot info " + potInfo.toString());
			
			ArrayList<Integer> playerIdList = new  ArrayList<Integer>();
			//potList.get(0).setChips(0);
			//List<TotalBets> tempMap = new  ArrayList<TotalBets>();
			//tempMap = potList.get(0).getPlayerWiseChip();
			
			//log.info("Player wise chip "  + tempMap.toString());
			
			while (smallestBet != 1000000000){
				log.info("Seat info " +  seatInfo.getSeats().toString());
				log.info("Pot list " + potList.toString());
				
				potAmount = 0;
				allIn = false;
				smallestBet = 1000000000;
				countBets = 0;
				for (Seat seat : seatInfo.getSeats()) {
					double seatBet = seat.getBet();
					if ((seatBet < smallestBet) && (seatBet != 0)) {
						smallestBet = seatBet;
					}
					if (seatBet != 0) {
						countBets++;
					}
				}
				
				log.info("smallest bet = " + smallestBet );
				for (Seat seat : seatInfo.getSeats()) {
					if ((seat.getBet() == smallestBet) && seat.isAllIn == true) {
						allIn = true;
						log.info("all in player " + seat.getPlayerName() + " with smallest bet " + smallestBet);
					}
				}
				if (smallestBet != 1000000000) {
					if(pot == null){
						pot = new Pot();
						playerIdList = new  ArrayList<Integer>();
						log.info("new pot created.");
						pot.setPlayerIds(playerIdList);
						potList.add(pot);
					}
					
					for (Seat seat : seatInfo.getSeats()) {
						if (seat.getStatus()==1 || seat.getStatus() ==3) {
							if((seat.getBet() != 0)){
								this.log.info("PlayerId " + +seat.getPlayerId() + " with non zero BET in pot ");
								this.log.info("This seat  " + seat.toString() );
								potAmount += smallestBet;
								log.info("current pot amount = " + potAmount);
								seat.setBet(seat.getBet() - smallestBet);

								if ( (seat.getPlayerId() > 0)) {
									if ((playerIdList.size() == 0)
											|| (!playerIdList.contains(Integer.valueOf(seat.getPlayerId())))) {
										playerIdList.add(Integer.valueOf(seat.getPlayerId()));
										this.log.info("PlayerId "
												+ getPlayerById(seat.getPlayerId()).toString() + " added in pot.");
									}
								}
							}else{
								if ((seat.getPlayerId() > 0)) {
									if (!seat.isAllIn
											&& (!playerIdList.contains(Integer.valueOf(seat.getPlayerId())))) {
										playerIdList.add(Integer.valueOf(seat.getPlayerId()));
										this.log.info("Allin Player with player id "
												+ getPlayerById(seat.getPlayerId()).toString() + " added in pot.");
									}
								}
							}
						}
					}
					
					pot.setChips(pot.getChips() + potAmount);
					if(pot.getPlayerIds().size() == 0){
						pot.setPlayerIds(playerIdList);
						log.info("player ids added to pot " + pot.toString());
					}
					if(allIn){
						playerIdList = new  ArrayList<Integer>();
						pot = null;
					}
				}
			}
			// for potinfo
			
			/*for(Iterator<Pot> iterator = potList.iterator();iterator.hasNext();){
				Pot pot = iterator.next();
				if(pot.getChips() == 0){
					iterator.remove();
				}
			}*/
			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("init pot error ",e);
//			return null;
//			throw e;
		}
	}
	
	public void updatePots(Player currentPlayer){
		try {
			log.info("Inside update pot");
			log.info("pot info " +this.getPotInfo().toString());
			List<Pot> potList = this.getPotInfo().getPots();
			double nextPotAmount = 0;
			Pot pot = new Pot();
			ArrayList<Integer> playerIdList = new ArrayList<Integer>();
			// potList.add(pot);
			Pot lastPot = potList.get(potList.size() - 1);
			double potAmount = lastPot.getChips();
			for (Seat seat : seatInfo.getSeats()) {
				if (!seat.isPack() && (seat.getBet() != 0 || (seat.getLastBetAmount() > 0 && !seat.isAllIn()))) {				// in current round, player has not bet as all other have gone in, but in previous rounds player has bet more money. 

					// Player player = getPlayerById(seat.getPlayerId());
					// if (player != null) {

					this.log.info("PlayerId with non zero BET in pot "
							+ seat.getPlayerName());
					this.log.info("PLayer :" + this.getPlayerById(seat.getPlayerId()));
					this.log.info("SEAT :" + seat.toString());
					//potAmount += seat.getBet();

					log.info("current pot amount = " + potAmount);

					seat.setBet(0);
					if (currentPlayer.getPlayerId() == seat.getPlayerId()
							&& seat.isAllIn()) {
						if(currentPlayer.getAllInAmount() < currentPlayer.getMinCurrentStake()){
							nextPotAmount = currentPlayer.getMinCurrentStake()
									- currentPlayer.getAllInAmount();
							if(nextPotAmount > potAmount){
								nextPotAmount = 0;
							}
							log.info("all in player" + currentPlayer.toString());
							log.info("next pot amount " + nextPotAmount );
						}
					} else {
						playerIdList.add(seat.getPlayerId());
					}
					/*
					 * if ( (seat.getPlayerId() > 0)) { if ((playerIdList.size()
					 * == 0) ||
					 * (!playerIdList.contains(Integer.valueOf(seat.getPlayerId
					 * ())))) {
					 * playerIdList.add(Integer.valueOf(seat.getPlayerId()));
					 * this.log.info("PlayerId added in pot " +
					 * getPlayerById(seat.getPlayerId()).toString()); } }
					 */
					// }
				}
			}
			log.info("pot info " +this.getPotInfo().toString());
			lastPot.setChips(potAmount - nextPotAmount);
			lastPot.setPlayerIds(lastPot.getPlayerIds());
			log.info("Pot info " +this.getPotInfo().toString());
			pot.setChips(nextPotAmount);
			pot.setPlayerIds(playerIdList);
			potList.add(pot);
			log.info("pots info " +this.getPotInfo().toString());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("update error ", e);
//			throw e;
		}
			
	}

	public int getRakePercentage() {
		return rakePercentage;
	}

	public void setRakePercentage(int rakePercentage) {
		this.rakePercentage = rakePercentage;
	}

	public int getTurnCounter() {
		return turnCounter;
	}

	public void setTurnCounter(int turnCounter) {
		this.turnCounter = turnCounter;
	}

	public boolean isMixVariant() {
		return isMixVariant;
	}

	public void setMixVariant(boolean isMixVariant) {
		this.isMixVariant = isMixVariant;
	}

	public List<Card> getJokers() {
		return jokers;
	}

	public void setJokers(List<Card> jokers) {
		this.jokers = jokers;
	}

	public int getBootMultiplier() {
		return bootMultiplier;
	}

	public void setBootMultiplier(int bootMultiplier) {
		this.bootMultiplier = bootMultiplier;
	}

	public int getSideShowPlayerId() {
		return sideShowPlayerId;
	}

	public void setSideShowPlayerId(int sideShowPlayerId) {
		this.sideShowPlayerId = sideShowPlayerId;
	}

	public int getSeeCardsTimer() {
		return seeCardsTimer;
	}

	public void setSeeCardsTimer(int seeCardsTimer) {
		this.seeCardsTimer = seeCardsTimer;
	}

	public int getMaxBet() {
		return maxBet;
	}

	public void setMaxBet(int maxBet) {
		this.maxBet = maxBet;
	}

	public List<String> getDealerChatLogs() {
		return dealerChatLogs;
	}

	public void setDealerChatLogs(List<String> dealerChatLogs) {
		this.dealerChatLogs = dealerChatLogs;
	}

	public RoundInfo getRoundInfo() {
		return roundInfo;
	}

	public void setRoundInfo(RoundInfo roundInfo) {
		this.roundInfo = roundInfo;
	}

	public Player getfirstBlindPlayerNextTo(int startingSeatId) {
		int seatId = startingSeatId;
		Player firstBlindPlayer = null;
		Seat currentSeat;
		while(true){
			seatId--;
			if (seatId < 1){
				seatId = this.getSeatInfo().getSeats().size();
			}

			currentSeat = this.getSeatInfo().getSeatById(seatId);
			if(currentSeat.isOccupied()){
				firstBlindPlayer = this.getPlayerById(currentSeat.getPlayerId());
				if(firstBlindPlayer.getPlayerStatus().equals(PlayerStatus.BLIND)){
					log.info("blind player found " + firstBlindPlayer.toString());
					return firstBlindPlayer;
				}
			}
			
			if (seatId == startingSeatId){
				log.info("No blind player found");
				break;
			}
		}
		return null;
	}

	public boolean isAllBlind() {
		return allBlind;
	}

	public void setAllBlind(boolean allBlind) {
		this.allBlind = allBlind;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

}
