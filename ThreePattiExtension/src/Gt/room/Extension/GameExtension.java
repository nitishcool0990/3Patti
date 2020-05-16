package Gt.room.Extension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.HibernateMapping.GtDomainName;
import Gt.HibernateMapping.GtGameConfig;
import Gt.HibernateMapping.GtGameProp;
import Gt.base.GameLogicObject;
import Gt.common.BuyInChipInfo;
import Gt.common.Card;
import Gt.common.Commands;
import Gt.common.GameInfo;
import Gt.common.Player;
import Gt.common.PotInfo;
import Gt.common.RoundInfo;
import Gt.common.SeatInfo;
import Gt.common.WinnerInfo;
import Gt.controller.GameController;
import Gt.interfaces.Controller;
import Gt.interfaces.IGameExtension;
import Gt.interfaces.IThreePattiExtension;
import Gt.main.Extension.ThreePattiExtension;
import Gt.user.utils.GtUserUtils;
import Gt.utils.GtDomainNameUtils;
import Gt.utils.GtGameConfigUtils;
import Gt.utils.GtGamePropUtils;

import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.exceptions.SFSRoomException;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class GameExtension extends SFSExtension implements IGameExtension {
	Controller controller = null;
	List<User> userList = null;
	private int minPlayers = -1;
	private int players = -1;
	private int minBuyIn = 0;
	private int maxBuyIn = 10000;
	private int maxPotLimit = 100000;
	private String chipType = "";
	private String gameVariant = GameLogicObject.NORMAL;
	private String winningAmtStr = "";
	private int turnTime = 30;
	private Integer logKey = Integer.valueOf(-1);
	private int tableId = -1;
	private int ante = -1;
	private int rakePercentage = 0;
	private int bootMultiplier = 1;
	private int seeCardsTimer = -1;
	private int maxBet = -1;
	private String domain = "";
	private String gameName = "";

	public static Logger log = LoggerFactory.getLogger("Gt.room.Extension.GameExtension");

	public void debug(String s) {
		if ((this.log != null) && (this.logKey != null)) {
			this.log.debug(this.logKey + ",[GameExtension]," + s);
		}
	}

	public void error(String s, Exception e) {
		if ((this.log != null) && (this.logKey != null)) {
			this.log.error(this.logKey + ",[GameExtension]," + s, e);
		}
	}

	public void info(String s) {
		if ((this.log != null) && (this.logKey != null)) {
			this.log.info(this.logKey + ",[GameExtension]," + s);
		}
	}

	public void fatal(String s) {
		if ((this.log != null) && (this.logKey != null)) {
			this.log.error(this.logKey + ",[GameExtension]," + s);
		}
	}

	public void init() {
		IThreePattiExtension tp = (IThreePattiExtension) getParentZone().getExtension();
		addEventHandler(SFSEventType.USER_JOIN_ROOM, JoinRoomHandler.class);
		addEventHandler(SFSEventType.USER_LEAVE_ROOM, LeaveRoomHandler.class);
		addEventHandler(SFSEventType.USER_DISCONNECT, UserDisconnectHandler.class);
		addEventHandler(SFSEventType.USER_RECONNECTION_TRY, UserReconnectionTryHandler.class);
		addEventHandler(SFSEventType.USER_RECONNECTION_SUCCESS, UserReconnectionSuccessHandler.class);

		addRequestHandler("game", GameRequestHandler.class);
		RoomVariable configId = (RoomVariable) getParentRoom().getVariable("configId");

		Room room = getParentRoom();
		this.controller = new GameController(this);
		this.tableId = room.getVariable("gametable.id").getIntValue();
		this.logKey = this.tableId;
		// GtGameProp gtProp = null;
		GtGameConfig config = GtGameConfigUtils.getGameConfigById(configId.getIntValue());
		GtGameProp gtPropTT = GtGamePropUtils.findByProperty(configId.getIntValue(), "turn_time");
		GtGameProp gtSeeCardsTimer = GtGamePropUtils.findByProperty(configId.getIntValue(), "see_cards_timer");
		GtGameProp gtPropAnte = GtGamePropUtils.findByProperty(configId.getIntValue(), "ante");
		GtGameProp gtPropMaxPotLimit = GtGamePropUtils.findByProperty(configId.getIntValue(), "max_pot_limit");
		GtGameProp gtRakePercentage = GtGamePropUtils.findByProperty(configId.getIntValue(), "rake");
		
		GtGameProp gtBootMultiplier = GtGamePropUtils.findByProperty(configId.getIntValue(), "boot_multiplier");
		
		GtGameProp gtMaxBet = GtGamePropUtils.findByProperty(configId.getIntValue(), "max_bet");
		GtGameProp gtPrivateTable = GtGamePropUtils.findByProperty(configId.getIntValue(), "table_password");
		int isPrivate = config.getIsPrivate();
		
		String tableName = room.getVariable("gameName").getStringValue();
		int gameTableId = room.getVariable("gametable.id").getIntValue();
		
		this.domain = tp.getDomainUrl();
		this.controller.createTable(config,tableName,gameTableId,domain);
		
		String gameName = config.getRemark() + tableName.substring(tableName.lastIndexOf('#') + 1);
		this.gameName  = gameName;
		
		
		log.info("setting domain " + domain);
		this.minPlayers = config.getMinPlayers();
		this.players = config.getMaxPlayers();
		
		if(isPrivate == 1){
			this.getController().getTable().setPrivate(true);
			if(gtPrivateTable != null){
				this.getController().setPrivatePasscode(gtPrivateTable.getPropValue().toString());
			}
		}
		
		if(gtBootMultiplier != null){
			this.bootMultiplier  = Integer.parseInt(gtBootMultiplier.getPropValue());
			this.getController().getTable().setBootMultiplier(this.bootMultiplier);
		}
		
		if (gtPropAnte != null) {
			this.ante = Integer.parseInt(gtPropAnte.getPropValue());
			this.controller.getTable().setAnte(this.ante * this.bootMultiplier );
		}

		if (gtPropMaxPotLimit != null) {
			this.maxPotLimit = Integer.parseInt(gtPropMaxPotLimit.getPropValue());
			this.getController().getTable().setMaxPotLimit(this.maxPotLimit);
		}
		
		if(gtRakePercentage != null){
			this.rakePercentage = Integer.parseInt(gtRakePercentage.getPropValue());
			this.getController().getTable().setRakePercentage(this.rakePercentage);
		}
		
		if(gtMaxBet != null){
			this.maxBet = Integer.parseInt(gtMaxBet.getPropValue());
			this.getController().getTable().setMaxBet(this.maxBet);
		}
		
		this.chipType = config.getChipType();
		this.gameVariant = config.getGameVariant();
		this.maxBuyIn = config.getMaxBuyIn().intValue();
		this.minBuyIn = config.getMinBuyIn().intValue();

		this.getController().getTable().setTableName(tableName);
		this.getController().getTable().setGameTableId(gameTableId);

		if (gtPropTT != null) {
			this.turnTime = Integer.parseInt(gtPropTT.getPropValue());
			this.getController().getTable().setTurnTime(this.turnTime);
		}
		
		if (gtSeeCardsTimer != null){
			this.seeCardsTimer = Integer.parseInt(gtSeeCardsTimer.getPropValue());
			this.getController().getTable().setSeeCardsTimer(this.seeCardsTimer);
		}
		
		userList = new ArrayList<User>();
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void handleJoinRequest(User user) {
		try {
			int playerId =getPlayerId(user);
			/*if (!userList.contains(user)) {
					userList.add(user);
					info("player Id " + playerId + " added to user list");
				}*/
			addToUserList(user, playerId);
			this.sendRoomDetails(playerId);
			info("room details sent to player Id " + playerId);
			Room room = getParentRoom();
			savePlayerJoinedRoom(playerId, room);
		} catch (Exception e) {
			log.info("error in handle join request ",e);
			log.error("handle join request ", e);
		}

	}

	public void handleLeaveRoom(User user) {
		IThreePattiExtension ext = (IThreePattiExtension) getParentZone().getExtension();
		int playerId = getPlayerId(user);
		this.controller.setPlayerStatus(playerId, false, true);
		this.getController().handleExitRoom(playerId);
		ext.getJoinedRoomsUtil().leaveRoom(playerId, getParentRoom().getName());
		synchronized (this) {
			if (userList.contains(user)) {
				userList.remove(user);
			}
		}
		// remove from map that player left room
	}

	public void sendRoomDetails(int playerId) {

		try {
			SFSObject sfsobj = new SFSObject();
			sfsobj.putInt("minPlayers", this.minPlayers);
			sfsobj.putInt("maxPlayers", this.players);
			sfsobj.putInt("minBuyin", this.minBuyIn);
			sfsobj.putInt("maxBuyin", this.maxBuyIn);
			sfsobj.putInt("maxPot", this.maxPotLimit);
			
			String chipType = this.chipType.split("_")[0];
			if(chipType.equals("dummy")){
				chipType = "Free";
			}else{
				chipType = "Real";
			}
			
			sfsobj.putUtfString("chipType", chipType);
			sfsobj.putUtfString("gameType", this.gameVariant);
			sfsobj.putInt("bootAmt", this.getController().getTable().getAnte());
			sfsobj.putInt("turnTime", this.turnTime);
			sfsobj.putInt("rakeAmount",  this.rakePercentage);
			sfsobj.putInt("tableId",  this.getController().getTable().getGameTableId());
			sfsobj.putInt("gameId",  this.getController().getTable().getGameId());
			sfsobj.putInt("maxTableBet", this.getController().getTable().getMaxBet());
			sfsobj.putUtfString("gameName", this.gameName);
			info("sending room details" + sfsobj.toString() + " to player Id " + playerId);
			sendCommand(Commands.CMD_ROOMDATA.toString(), sfsobj, playerId);
		} catch (Exception e) {
			log.error("error in send room details ",e);
		}
	}

	public void destroy() {
		removeEventHandler(SFSEventType.USER_JOIN_ROOM);
		removeEventHandler(SFSEventType.USER_LEAVE_ROOM);
		removeEventHandler(SFSEventType.USER_RECONNECTION_TRY);
		removeEventHandler(SFSEventType.USER_DISCONNECT);
		removeEventHandler(SFSEventType.USER_RECONNECTION_SUCCESS);
		
		removeRequestHandler("game");
		this.controller.destroy();
		this.controller = null;
		super.destroy();
	}

	public ScheduledFuture<?> scheduleRunStateMachine(int delay) {
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		return sfs.getTaskScheduler().schedule(new GameControllerTask(this), delay, TimeUnit.SECONDS);
		// = scheduler.schedule(new GameControllerTask(this), delay,
		// TimeUnit.SECONDS);
	}

	public ScheduledFuture<?> schedulePlayerDisconnectionTask(int playerId, int delay) {
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		return sfs.getTaskScheduler().schedule(new PlayerDisconnectionTask(this.controller, playerId), delay,
				TimeUnit.SECONDS);
	}

	public class GameControllerTask implements Runnable {
		GameController controller = null;

		public GameControllerTask(IGameExtension ext) {
			System.out.println("Running GameController Scheduler");

			this.controller = (GameController) ext.getController();
			controller.info("reschedule run state machine with delay ");
		}

		public void run() {
			try {
				this.controller.runStateMachine(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class PlayerDisconnectionTask implements Runnable {
		int playerId = -1;
		Controller controller = null;

		public PlayerDisconnectionTask(Controller gm, int playerId) {
			this.controller = gm;
			this.playerId = playerId;
		}

		public void run() {
			try {
				this.controller.playerDisconnectionTimeout(this.playerId);
			} catch (Exception e) {
				info("TaskRunner : Exception throwns by runStateMachine:" + e.getMessage() + e);
				e.printStackTrace();
				error("Player Disconnection" , e);
			}
		}
	}

	@Override
	public Controller getController() {
		// TODO Auto-generated method stub
		return this.controller;
	}

	public void sendCommand(String cmd, SFSObject obj, int playerID) {
		info("sending Command " + cmd + " to player id " + playerID);
		User user = getUserById(playerID);
		if (user != null) {
			send(cmd, obj, user);
			info("Command " + cmd + " sent to player id " + playerID);
		} else {
			info("No user found at game extension");
		}
	}

	public void sendCommand(String cmd, ISFSObject obj, int playerID) {
		info("sending Command " + cmd + " to player id " + playerID);
		User user = getUserById(playerID);
		if (user != null) {
			send(cmd, obj, user);
			info("Command " + cmd + " SFS Object " + obj.toString() + " sent to player id " + playerID);
		} else {
			log.info("No user found at game extension");
		}
	}

	public void sendMassage(String type,int msgCode, String msg, int playerId) {
		User user = getUserById(playerId);
		
		
		if (user != null) {
			SFSObject obj = new SFSObject();
			obj.putUtfString("type", type);
			obj.putUtfString("msg", msg);
			obj.putInt("msgCode", msgCode);
			send(Commands.CMD_MASSAGE.toString(), obj, user);
			info("Message " + msg + " of type " + type + " sent to player id " + playerId);
		} else {
			System.out.println("No user found at game extension");
		}
	}

	@Override
	public void sendMassageToAll(String type, String msg, int msgCode) {
		SFSObject obj = new SFSObject();
		obj.putUtfString("type", type);
		obj.putUtfString("msg", msg);
		obj.putInt("msgCode", msgCode);
		send(Commands.CMD_MASSAGE.toString(), obj, userList);
		info("Message " + msg + " of type " + type + "sent to all");

	}
	
	@Override
	public void sendSideShow(int prevPLayer, String currentPlayerName, int sideShowTimer) {
		log.info("sending side show to player id " + prevPLayer + " from player " + currentPlayerName + " side show timer " + sideShowTimer );
		SFSObject obj = new SFSObject();
		obj.putUtfString("msg", currentPlayerName + " is requesting side show ");
		obj.putInt("sideShowTimer", sideShowTimer);
		obj.putUtfString("whoseTurnPlayerName", currentPlayerName);
		sendCommand(Commands.CMD_SIDESHOWREQUEST.toString(), obj, prevPLayer);

	}
	
	@Override
	public void sendPlayerActions(int playerId, List<String> playerActions, double minBet, double maxBet, boolean isUserTurn, int advanceBetAmount) {
		info("Sending player Actions " + playerActions.toString() + " minBet = " + minBet + "maxBet = " + maxBet
				+ " to player Id " + playerId);
		SFSObject sfs = new SFSObject();
		sfs.putUtfString("playerActions", playerActions.toString());
		sfs.putDouble("minBet", minBet);
		sfs.putDouble("maxBet", maxBet);
		sfs.putBool("isUserTurn", isUserTurn);
		sfs.putInt("advanceBetAmount", advanceBetAmount);
		sendCommand(Commands.CMD_PLAYERCHOICES.toString(), sfs, playerId);
		info("Player Actions " + playerActions.toString() + " minimum Bet=" + minBet + " maximum bet=" + maxBet
				+ " sent to player id " + playerId);
	}

	@Override
	public void sendPotInfo(PotInfo pots) {
		info("sending potInfo to all players");
		info(pots.toString());
		send(Commands.CMD_POTINFO.toString(), pots.toSFSObject(), userList);

	}

	@Override
	public void sendPlayerCards(List<String> playerCards,String playerRank, int timer, int playerId) {
		info("Sending player hand cards " + playerCards + " to player id " + playerId);
		SFSObject sfs = new SFSObject();
		
		
		playerCards.add(playerRank);
		sfs.putUtfStringArray("playerCards", playerCards);
		sfs.putInt("timer", timer);

		sendCommand(Commands.CMD_PLAYERCARDS.toString(), sfs, playerId);
		info("Player hand cards " + playerCards + " sent to player Id " + playerId);

	}



public ISFSApi getApiGame(){
	return SmartFoxServer.getInstance().getAPIManager().getSFSApi();
}



	
	public void sendSeatInfo(SeatInfo seatInfo) {
		info("Sending to seatInfo to all players");
		info(seatInfo.toString());
		send(Commands.CMD_SEATINFO.toString(), seatInfo.toSFSObject(), userList);
	}

	public void sendBuyinChipInfo(BuyInChipInfo buyinchipInfo, int playerId) {
		info("sending buy in chip info " + buyinchipInfo.toString() + " to player id " + playerId);
		
		sendCommand(Commands.CMD_BUYINDATA.toString(), buyinchipInfo.toSFSObject(), playerId);
	}

	public void sendDealer(int playerId, int seatId){
		info("sending dealer with player id " + playerId + " at seat id " + seatId);
		SFSObject obj = new SFSObject();
		obj.putInt("playerId", playerId);
		obj.putInt("seatId", seatId);
		send(Commands.CMD_DEALER.toString(), obj, userList);
	}

	

	public boolean handleSeatReservation(User user, int seatId, boolean forReservation, boolean isWaiting) {
		int playerId = getPlayerId(user);
		info("Handled Seat reservation for User with playerId=" + playerId);
		if (forReservation) {
			if (this.controller.reserveSeat(playerId, seatId, isWaiting)) {
				info("Prompting playerId=" + playerId + " about the granting of seat reservation on seatId=" + seatId);
				sendTakeseatAllowedToUser(playerId, seatId, false);
			}
		} else {
			this.controller.cancelSeatReservation(playerId, seatId);
		}
		return true;
	}

	public void handlePlayerTookAction(User user, String playerAction, int seatId, int chipAmount, boolean isUserTurn) {
		int playerId = getPlayerId(user);
		if (playerId <= 0) {
			info("Playerid is -1");
			return;
		}
		info("Handling Player Took Action for User with playerId=" + playerId + " for action  " + playerAction
				+ " on seat id " + seatId + " with chip amount " + chipAmount);

		this.controller.playerTookAction(playerAction, playerId, seatId, chipAmount,isUserTurn);
		// this.sendPlayerStatus(playerId, false, cardSeen)

	}

	protected int getPlayerId(User user) {
		int ret = -1;
		if (user == null) {
			debug(" Value of user is NULL . Returning from getPlayerId().");
			return -1;
		}
		if (user.getVariable("PlayerId") != null) {
			ret = user.getVariable("PlayerId").getIntValue().intValue();
		} else {
			ret = ((Integer) user.getSession().getProperty("PlayerId")).intValue();
		}
		info("getPlayerId: PlayerId of User " + user.getName() + " is " + ret);
		return ret;
	}
	
	@Override
	public String getSessionKey(int playerId) {
		String ret = null;
		User user = getUserById(playerId);
		if (user == null) {
			debug(" Value of user is NULL . Returning from getSessionKey().");
			return null;
		}
		
		ret = user.getSession().getProperty("sessionToken").toString();
		info("getSessionKey: PlayerId of User " + user.getName() + " is " + ret);
		return ret;
	}
	
	@Override
	public String getPlayerName(int playerId) {
		String ret = null;
		User user = getUserById(playerId);
		if (user == null) {
			debug(" Value of user is NULL . Returning from getSessionKey().");
			return null;
		}
		
		ret = user.getSession().getProperty("playerName").toString();
		info("getSessionKey: PlayerId of User " + user.getName() + " is " + ret);
		return ret;
	}
	
	@Override
	public String getEmailId(int playerId) {
		String ret = null;
		User user = getUserById(playerId);
		if (user == null) {
			debug(" Value of user is NULL . Returning from getSessionKey().");
			return null;
		}
		if(user.getSession().getProperty("emailId") ==null || user.getSession().getProperty("emailId") == "") {
			return "";
		}
		ret = user.getSession().getProperty("emailId").toString();
		info("getEmailId: PlayerId of User " + user.getName() + " is " + ret);
		return ret;
	}
	
	@Override
	public void sendSideShowResult(String string, boolean isWinner, int pId2) {
		SFSObject obj = new SFSObject();
		obj.putUtfString("SideShow", string);
		obj.putBool("isWinner", isWinner);
		sendCommand(Commands.CMD_MASSAGE.toString(), obj, pId2);

	}

	private String getAppName(User user) {
		String appNAme = "";
		if (user == null) {
			debug(" Value of user is NULL . Returning from getPlayerId().");
			return "";
		}
		if (user.getVariable("appName") != null) {
			appNAme = user.getVariable("appName").getStringValue();
		} else {
			appNAme = (String) user.getSession().getProperty("appName");
		}
		info("getAppName: getAppName of User " + user.getName() + " is " + appNAme);
		return appNAme;
	}

	public User getUserById(int playerId) {
		List<User> list = this.userList;				// changed from getParentRoom().getUserList()
		info("Number of user in game Area  "+list.size());
		//List<User> list = getUserList();
		for (int i = 0; i < list.size(); i++) {
			User u = (User) list.get(i);
			try {
				int id = u.getVariable("PlayerId").getIntValue().intValue();
				if (id == playerId) {
					info("getUserById- User name against PlayerId=" + playerId + " is " + u.getName());
					return u;
				}
			} catch (NullPointerException localNullPointerException) {
			}
		}
		debug("getUserById- PlayerId=" + playerId + " was not found.");
		return null;
	}

	public synchronized void sendTakeseatAllowedToUser(int playerId, int seatId, boolean asWaiting) {
		info("sending allow take seat to player id " + playerId + " on seat id  " + seatId);
		SFSObject sfsobj = new SFSObject();
		sfsobj.putInt("seatId", seatId);
		sfsobj.putBool("asWaiting", asWaiting);
		sendCommand(Commands.CMD_ALLOW_TAKESEAT.toString(), sfsobj, playerId);
	}

	@Override
	public void sendWinnerToAll(WinnerInfo winners) {
		info("sending winner to all players");
		send(Commands.CMD_WINNER.toString(), winners.toSFSObject(), userList);

	}

	@Override
	public ScheduledFuture<?> scheduleSeatReservationExpireTask(int playerId, int seatId, int delay) {
		SmartFoxServer sfs = SmartFoxServer.getInstance();
		return sfs.getTaskScheduler().schedule(new SeatReservationExpireTask(this.controller, playerId, seatId), delay,
				TimeUnit.SECONDS);
	}

	@Override
	public void sendPlayerSeentoAll(Player p) {
		info("sending player status seen to all of  player " + p.toString());
		SFSObject sfs = new SFSObject();
		sfs.putUtfString("SEEN", p.toString());
		send(Commands.CMD_PLAYERSEEN.toString(), sfs, userList);
	}

	public class SeatReservationExpireTask implements Runnable {
		int playerId = -1;
		int seatId = -1;
		Controller controller = null;
		boolean reserveReEntry = true;

		public SeatReservationExpireTask(Controller gm, int playerId, int seatId) {
			this.controller = gm;
			this.playerId = playerId;
			this.seatId = seatId;
		}

		public void run() {
			try {
				this.controller.cancelSeatReservation(this.playerId, this.seatId);
			} catch (Exception e) {
				// GameExtension.this.error("TaskRunner : Exception throwns by
				// runStateMachine:" + e.getMessage(), e);
				e.printStackTrace();
				error("Seat reservation Expire", e);
			}
		}
	}

	public boolean handleTakeSeat(User user, int seatId, int amount, boolean isTopUp) {
		boolean isFlag = true;
		int playerId = getPlayerId(user);
		this.controller.setPlayerStatus(playerId, false, true);
		if(isTopUp){
			this.controller.topUp(playerId,amount);
			info("Handled top up for User with playerId=" + playerId);
		}else{
			if (this.controller.handleTakeSeat(playerId, seatId, amount)) {
				spectToPlayer(user);
			}
		}
		info("Handled Take Seat for User with playerId=" + playerId);
		return true;
	}

	public void handleSeeCards(User user, int seeIndex) {
		int playerId = getPlayerId(user);
		if (playerId <= 0) {
			log.info("player id -1");
			return;
		}
		log.info("handling see cards request for player id " + playerId);
		this.controller.sendCards(playerId, seeIndex);
	}

	private void spectToPlayer(User user) {
		if ((user != null) && (user.isSpectator(getParentRoom()))) {
			log.info("spectToPlayer: for player: " + user.getName());
			try {
				getApi().spectatorToPlayer(user, getParentRoom(), true, true);
			} catch (SFSRoomException e) {
				e.printStackTrace();
				log.error("spectToPlayer: excpetion for player:" + user.getName(), e);
			} catch (NullPointerException e) {
				log.error("spectToPlayer: excpetion for player:" + user.getName(), e);
			}
		}
	}

	private void playerToSpec(User user) {
		if ((user != null) && (user.isPlayer(getParentRoom()))) {
			log.info("playerToSpec: for player: " + user.getName());
			try {
				getApi().playerToSpectator(user, getParentRoom(), true, true);
			} catch (SFSRoomException e) {
				e.printStackTrace();
				log.error("playerToSpec: excpetion for player:" + user.getName(), e);
			} catch (NullPointerException e) {
				log.error("playerToSpec: excpetion for player:" + user.getName(), e);
			}
		}
	}

	public boolean sendPlayerStatus(int playerId, boolean isAway, String status) {
		log.info("[TeenPatti]sendPlayerStatus: PlayerId=" + playerId + " isAway=" + isAway);
		if (playerId < 0) {
			log.debug("[TeenPatti]sendPlayerStatus: Player Id=" + playerId + " not valid.");
			return false;
		}

		SFSObject sfso = new SFSObject();
		sfso.putInt("PlayerId", playerId);
		sfso.putBool("isAway", isAway);
		sfso.putUtfString("status", status);
		return sendCommand(Commands.CMD_PLAYER_STATUS.toString(), sfso, this.userList);
	}

	protected boolean sendCommand(String cmd, ISFSObject sfso, List<User> users) {
		log.debug("sendCommand - cmd: " + cmd + " to all players=");
		String roomName = getParentRoom().getName();
		sfso.putUtfString("roomName", roomName);
		sfso.putBool("useConverter", false);
		send(cmd, sfso, users);
		return true;
	}

	@Override
	public boolean handleClientReady(User user) {
		// TODO Auto-generated method stub
		int playerId = getPlayerId(user);
		String playerName = this.getPlayerName(playerId);
		info("Handling Client Ready Response for User with playerId=" + playerId);
		if (playerId > 0) {
			
			addToUserList(user, playerId);
			this.controller.handleJoinRoom(playerId,playerName);
			this.controller.setPlayerStatus(playerId, false, false);
			
		}
		return true;
	}

	private synchronized void addToUserList(User user, int playerId) {
		if (user == null) {
			return;
		}
		for (int i = this.userList.size() - 1; i >= 0; i--) {
			User u = (User) this.userList.get(i);
			int uid = getPlayerId(u);
			if ((uid > 0) && (uid == playerId)) {
				this.userList.remove(i);
				debug("addToUserList: user already found in userlist for playerid:" + uid);
			}
		}
		this.userList.add(user);
	}

	public void sendGameInfo(int playerId, GameInfo gameInfo) {
		info("sending game info " + gameInfo.toString() + " to player id " + playerId);
		if (gameInfo == null) {
			debug("GameInfo was found null. Cant send SFSObject for GameInfo.");
			return;
		}

		SFSObject sfso = new SFSObject();
		sfso.putClass("gameinfo", gameInfo);
		sendCommand(Commands.CMD_GAMEINFO.toString(), sfso, playerId);
	}

	public void handleBuyInChipInfo(User user, boolean isTopUpRequest) {
		info("handling buyin chip info from user with player id " + getPlayerId(user));
		int playerId = getPlayerId(user);
		if (playerId <= 0) {
			debug("During buyin chipinfo request playerId is -1");
			return;
		}
		if(isTopUpRequest){
			this.getController().handleTopUp(playerId);
		}else{
			this.getController().handleBuyInChipInfo(playerId);
		}
		
	}

	@Override
	public void sendGameStart(int gameStartTimer) {
		if (userList.size() <= 0) {
			info("No player found during game start in game extension");
			return;
		}
		SFSObject obj = new SFSObject();
		obj.putInt("timer", gameStartTimer);
		info("sending game start to all players " + obj.toString());
		sendCommand(Commands.CMD_GAMESTART.toString(), obj, userList);

	}

	@Override
	public void sendGameStarted(int gameId) {
		SFSObject sfs = new SFSObject();
		sfs.putInt("gameId", gameId);
		info("sending game started to all players, game id " + gameId);
		sendCommand(Commands.CMD_GAMESTARTED.toString(), sfs, userList);
	}

@Override
public void sendRoundEnd() {
	// TODO Auto-generated method stub
	info("Sending round end to all");
	SFSObject sfs = new SFSObject();
	sendCommand(Commands.CMD_ROUNDEND.toString(), sfs, userList);
	
}

@Override
public void sendIdleState() {
	// TODO Auto-generated method stub
	info("Sending Idle state to all");
	SFSObject sfs = new SFSObject();
	sendCommand(Commands.CMD_IDLE.toString(), sfs, userList);
	
}


	@Override
	public void sendWhoseTurn(int seatId, int playerId, int turnTime) {
		info("sending CMD WHOSETURN with player id " + playerId + " seat id " + seatId + "turn time " + turnTime
				+ " to all players");
		SFSObject sfs = new SFSObject();
		sfs.putInt("seatId", seatId);
		sfs.putInt("playerId", playerId);
		sfs.putInt("turnTime", turnTime);
		sendCommand(Commands.CMD_WHOSETURN.toString(), sfs, userList);

	}

	public void handleLeaveSeat(User user) {
		if (user == null) {
			info("User is null");
			return;
		}
		if (!userList.contains(user)) {
			info("User is not in the room");
			return;
		}
		this.controller.setPlayerStatus(getPlayerId(user), false, true);
		boolean isSuccess = this.getController().handleLeaveSeat(getPlayerId(user));
		if (isSuccess) {
			try {
				IThreePattiExtension tp = (IThreePattiExtension) getParentZone().getExtension();
				tp.sendMyAccount(user);
				info("Player with player ID " + getPlayerId(user) + " has been leave seat and become spec");
			} catch (Exception e) {
				error("leave seat, my account." , e);
			}
			//playerToSpec(user);
		}

	}

	public void sendDealtCard() {
		info("Sending CMD DEALTCARDS to all players");
		SFSObject sfs = new SFSObject();
		sendCommand(Commands.CMD_DEALTCARD.toString(), sfs, userList);
	}

	@Override
	public void handleSideShowRes(User user, int accOrDen) {
		// TODO Auto-generated method stub
		int playerId = getPlayerId(user);
		info(" Getting Side show respone from player id " + playerId + " with accorden " + accOrDen);
		this.getController().sideShow(playerId, accOrDen);

	}

	public void handleUserDisconnection(User user) {
		int playerId = getPlayerId(user);
		this.controller.setPlayerStatus(playerId, true, true);
		
	//	this.controller.handleExitRoom(playerId);
		synchronized (this) {
			// user not removed from userlist for getting session key later.
			//this.userList.remove(user);
		}
		// updateGameTableEmptyStatus();

		info("handleUserDisconnection: Handled User Disconnection of User with playerId=" + playerId);
	}

	public boolean handleUserReconnectionSuccess(User user) {
		int playerId = getPlayerId(user);
		info("Handling User Reconnection of User with playerId=" + playerId);
		if (playerId > 0) {
			this.controller.setPlayerStatus(playerId, false, true);
			info("Handled User Reconnection of User with playerId=" + playerId);
		}
		return true;
	}

	public boolean handleUserReconnectionTry(User user) {
		int playerId = getPlayerId(user);
		info("Handling User Reconnection Try for User with playerId=" + playerId);
		if (playerId > 0) {
			this.controller.setPlayerStatus(playerId, true, true);
			info("Handled User Reconnection Try for User with playerId=" + playerId);
		}
		return true;
	}

	public void leaveRoomOnDisconnection(int playerId, String msg) {
		User user = getUserById(playerId);
		if (user != null) {
			synchronized (this) {
				this.userList.remove(user);
			}
			getApi().leaveRoom(user, getParentRoom());
			sendMassage("INFO",800, msg, playerId);
		}
	}
	
	public void sendWinnerCards(List<String> playerCards,int playerId){
		SFSObject sfsObj = new SFSObject();
		
		/*if(playerId ==-1){
			debug("PlayerId is -1");
			return;
		}*/
		info("During winner animation sending  cards to all. cards are "+playerCards.toString());
		sfsObj.putUtfStringArray("cards", playerCards);
		//sfsObj.putInt("pId", playerId);
		sendCommand(Commands.CMD_SHOWCARDS.toString(), sfsObj, playerId);
		
	}
	
	  private void savePlayerJoinedRoom(int playerId, Room room)
	  {
	    if (room != null)
	    {
	      info("handleClientReady: user id" + playerId + " joined the room: " + room.getName());
	      IThreePattiExtension ext = (IThreePattiExtension)getParentZone().getExtension();
	      ext.getJoinedRoomsUtil().joinRoom(playerId, room.getName());
	    }
	  }

	@Override
	public void sendShow() {
		// TODO Auto-generated method stub
		info("Sending state Show to all");
		SFSObject sfs = new SFSObject();
		sendCommand(Commands.CMD_SHOWSTATE.toString(), sfs, userList);
	}
	
	public void sendBettingState(){
		info("Sending state Show to all");
		SFSObject sfs = new SFSObject();
		sendCommand(Commands.CMD_BETTINGSTATE.toString(), sfs, userList);
	}
	
	  
	  public void changeSpectatorToPlayer(int playerId)
	  {
	    info("changeSpectatorToPlayer : for player id " + playerId);
	    User user = getUserById(playerId);
	    if (user != null) {
	      spectToPlayer(user);
	    }
	  }
	  
	  public void changePlayerToSpectator(int playerId)
	  {
	    info("changePlayerToSpectator : for player id " + playerId);
	    User user = getUserById(playerId);
	    if (user != null) {
	      playerToSpec(user);
	    }
	  }

	@Override
	public void sendPlayerHandRanks(List<String> pRankList,int playerId) {
		// TODO Auto-generated method stub
		SFSObject sfs = new SFSObject();
		info("During winner animation sending  ranks to all. cards are "+pRankList.toString());
		sfs.putUtfStringArray("ranks", pRankList);
		//sfsObj.putInt("pId", playerId);
		sendCommand(Commands.CMD_PLAYERRANKS.toString(), sfs, playerId);
		
	}

	@Override
	public void sendJokers(List<String> jokerList) {
		// TODO Auto-generated method stub
		
		SFSObject sfs = new SFSObject();
		info("sending jokers. cards are "+jokerList.toString());
		sfs.putUtfStringArray("jokers", jokerList);
		//sfsObj.putInt("pId", playerId);
		sendCommand(Commands.CMD_JOKERS.toString(), sfs, userList);
		
		
	}

	@Override
	public void sendGameChoicesToDealer(int dealerId, List<String> games, int timer) {
		// TODO Auto-generated method stub
		SFSObject sfs = new SFSObject();
		info("sending game variants to dealer with player id " + dealerId);
		info(games.toString());
		sfs.putUtfStringArray("gameVariants", games);
		sfs.putInt("timer", timer );
		sendCommand(Commands.CMD_GAMEVARIANTS.toString(), sfs, dealerId);
	}

	public void handleDealerResponse(String gameVariant) {
		// TODO Auto-generated method stub
		info("Handling dealer response, setting game variant as " + gameVariant);
		if (gameVariant != null) {
			this.controller.setGameVariant(gameVariant);
		}
		
		
	}

	@Override
	public void sendGameVariantToAll(String gameVariant) {
		// TODO Auto-generated method stub
		SFSObject sfs = new SFSObject();
		sfs.putUtfString("gameVariant", gameVariant);
		info("sending game variant " + gameVariant + " to all" );
		sendCommand(Commands.CMD_SELECTEDVARIANT.toString(), sfs, userList);
		
	}

	public void handleRoundInfo(User user) {
		int playerId = getPlayerId(user);
		this.controller.sendRoundInfo(playerId);
	}

	@Override
	public void sendRoundInfo(RoundInfo roundInfo) {
		
		
	}


}
