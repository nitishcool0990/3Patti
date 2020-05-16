package Gt.common;

import java.util.concurrent.ScheduledFuture;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class Seat implements SerializableSFSType{


	public boolean isOccupied =false;
	public boolean isReserved =false;
	public int seatId=-1;
	public int playerId=-1;
	public double chips =0;
	public double chipsLeft =0;
	public double lastBetAmount = 0;
	public String playerName ="";
	public transient ScheduledFuture<?> taskHandle;
	public boolean isAway=false;
	public boolean isAllIn=false;
	public transient double bet =0;
	public boolean isPack = false;
	public int status = -1; // 0 : takeseat , 1 : Active , 2: Inactive , 3: ALL in 
	public String lastAction = "";
	public String emailId="";
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isAway() {
		return isAway;
	}

	public void setAway(boolean isAway) {
		this.isAway = isAway;
	}

	public Seat(int seatId) {
		this.seatId = seatId;
		//this.seatId = seatId;
	}

	public boolean isOccupied() {
		return isOccupied;
	}
	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	public int getSeatId() {
		return seatId;
	}
	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public double getChips() {
		return chips;
	}

	public void setChips(double chips) {
		this.chips = chips;
	}

	public double getChipsLeft() {
		return chipsLeft;
	}

	public void setChipsLeft(double chipsLeft) {
		this.chipsLeft = chipsLeft;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public boolean isAllIn() {
		return isAllIn;
	}

	public void setAllIn(boolean isAllIn) {
		this.isAllIn = isAllIn;
	}
	
	
	
	public double getLastBetAmount() {
		return lastBetAmount;
	}

	public void setLastBetAmount(double lastBetAmount) {
		this.lastBetAmount = lastBetAmount;
	}

	@Override
	public String toString() {
		return "Seat [isOccupied=" + this.isOccupied + ", isReserved=" + this.isReserved + ", seatId=" + this.seatId + ", playerId="
				+ this.playerId + ", chipsLeft=" + this.chipsLeft + ", playerName=" + this.playerName + ", isAway=" + this.isAway
				+ ", isAllIn=" + this.isAllIn + ", status=" + this.status +  ", bet=" + this.bet + ", email = "+ this.emailId+"]";
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	public boolean isPack() {
		return isPack;
	}

	public void setPack(boolean isPack) {
		this.isPack = isPack;
	}

	public String getLastAction() {
		return lastAction;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
