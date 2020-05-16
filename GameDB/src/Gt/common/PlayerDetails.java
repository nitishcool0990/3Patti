package Gt.common;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class PlayerDetails implements SerializableSFSType {
	public int playerId;
	public String lastAction = PlayerActionType.PACK.toString();
	public Boolean away;
	public double chipsLeft;
	public String status="";
	public double lastBetAmount =0;

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getPlayerId() {
		return this.playerId;
	}

	public void setAway(Boolean away) {
		this.away = away;
	}

	public Boolean getAway() {
		return this.away;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public String getLastAction() {
		return this.lastAction;
	}

	public double getChipsLeft() {
		return chipsLeft;
	}

	public void setChipsLeft(double chipsLeft) {
		this.chipsLeft = chipsLeft;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getLastBetAmount() {
		return lastBetAmount;
	}

	public void setLastBetAmount(double lastBetAmount) {
		this.lastBetAmount = lastBetAmount;
	}

}
