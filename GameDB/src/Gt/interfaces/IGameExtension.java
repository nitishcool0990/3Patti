package Gt.interfaces;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.smartfoxserver.v2.api.ISFSApi;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

import Gt.common.BuyInChipInfo;
import Gt.common.GameInfo;
import Gt.common.Player;
import Gt.common.PotInfo;
import Gt.common.RoundInfo;
import Gt.common.SeatInfo;
import Gt.common.WinnerInfo;

public interface IGameExtension {
	public void handleJoinRequest(User user);

	public void sendRoomDetails(int playerID);

	public void destroy();

	public Controller getController();
	
	public void handleLeaveRoom(User user);
	
	public ScheduledFuture<?> scheduleRunStateMachine(int delay);
	
	public void sendCommand(String cmd,SFSObject obj, int playerID);
	
	public void sendMassage(String type,int msgCode,String msg, int playereId);
	
	public void sendSeatInfo(SeatInfo seatInfo);
	
	public void sendDealer(int playerId,int seatId);
	
	public ISFSApi getApiGame();

	public void sendPlayerCards(List<String> playerCards,String playerRank,int timer, int playerId);

	public void sendPlayerActions(int playerId, List<String> playerActions,double minBet, double maxBet, boolean isUserTurn, int advanceBetAmount);

	public void sendSideShow(int prevPlayer, String currentPlayerName,int sideShowTimer);

	public void sendSideShowResult(String string, boolean winner, int pId2);

	public void sendWinnerToAll(WinnerInfo winners);

	public void sendPlayerSeentoAll(Player p);

	public void sendMassageToAll(String type, String msg, int msgCode);

	public void sendPotInfo(PotInfo pots);

	public boolean handleSeatReservation(User user, int seatId, boolean forReservation, boolean isWaiting);
	
	public ScheduledFuture<?> scheduleSeatReservationExpireTask(int playerId, int seatId, int delay);
	
	public boolean handleTakeSeat(User user, int seatId,int amount,boolean isTopUp);
	
	public boolean sendPlayerStatus(int playerId, boolean isAway,String status);
	
	public void sendBuyinChipInfo(BuyInChipInfo buyinchipInfo,int playerId);
	
	public boolean handleClientReady(User user);
	
	public void sendCommand(String cmd,ISFSObject obj, int playerID);
	
	public void sendGameInfo(int playerId, GameInfo gameInfo);
	
	public void handleBuyInChipInfo(User user, boolean isTopUpRequest);
	
	public void sendGameStart(int gameStartTimer);
	
	public void sendGameStarted(int gameId);

	public void sendWhoseTurn(int seatId, int playerId, int turnTime);
	
	public void sendDealtCard();
	
	public void handleSideShowRes(User user, int accOrDen);
	
	public boolean handleUserReconnectionSuccess(User user);
	
	public boolean handleUserReconnectionTry(User user);
	
	public void handleUserDisconnection(User user);
	
	 public ScheduledFuture<?> schedulePlayerDisconnectionTask(int playerId, int delay);
	 
	 public void leaveRoomOnDisconnection(int playerId, String msg);

	public void sendRoundEnd();
	
	public void sendWinnerCards(List<String> playerCards, int playerId);

	public void sendShow();
	
	public void sendBettingState();
	
	 public void changeSpectatorToPlayer(int playerId);

	public void sendIdleState();

	public void  sendTakeseatAllowedToUser(int playerId, int seatId, boolean asWaiting);

	public void sendPlayerHandRanks(List<String> pRankList, int playerId);
	
	  public void changePlayerToSpectator(int playerId);

	public void sendJokers(List<String> jokerList);

	public void sendGameChoicesToDealer(int dealerId, List<String> games, int timer);

	public void sendGameVariantToAll(String gameVariant);
	
	public String getSessionKey(int playerId);

	public void sendRoundInfo(RoundInfo roundInfo);
	
	public String getEmailId(int playerId);
	
	public String getPlayerName(int playerId);


}
