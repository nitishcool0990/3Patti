package Gt.interfaces;

import com.smartfoxserver.v2.entities.User;

import Gt.HibernateMapping.GtGameConfig;
import Gt.common.ChipInfo;
import Gt.common.SeatInfo;
import Gt.common.Table;

public interface Controller {
	public void createTable( GtGameConfig config,String gameName, int gameTableId, String domain);
	public boolean handleJoinRoom(int playerId,String playerName);
	public boolean handleTakeSeat(int playerId,int SeatId,int amount);
	public Table getTable();
	public boolean handleLeaveSeat(int playerId);
	public boolean bettingManager();
	public void playerTookAction(String playerAction, Integer pId, Integer seatId, Integer chipAmount, boolean isUserTurn);
	public void sideShow(int playerId, int accOrDen);

	public void sendCards(int playerId, int seeIndex);
	public boolean reserveSeat(int playerId,int  seatId,boolean isWaiting);
	public void cancelSeatReservation(int playerId,int seatId);
	 public boolean setPlayerStatus(int playerId, boolean isAway, boolean checkAway);
	 public void handleBuyInChipInfo(int playerId);
	 public boolean handleExitRoom(int playerId);
	 public boolean playerDisconnectionTimeout(int playerId);
	 public void debug(String text);
	 public void error(String s, Exception e) ;
	public void destroy();
	public void setGameVariant(String gameVariant);
	public SeatInfo getSeatInfo();
	public void handleTopUp(int playerId);
	public void topUp(int playerId, int amount);
	public void sendRoundInfo(int playerId);
	public void setPrivatePasscode(String passcode);
	public String getPasscode();
}
