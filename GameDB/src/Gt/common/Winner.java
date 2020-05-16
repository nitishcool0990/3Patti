package Gt.common;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class Winner implements SerializableSFSType, Comparable<Winner> {
	public int potId =0;
	public int playerId =0;
	public double amount =0;
	
	public int getPotId() {
		return potId;
	}
	public void setPotId(int potId) {
		this.potId = potId;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "Winner [potId=" + potId + ", playerId=" + playerId + ", amount=" + amount + "]";
	}
	@Override
	public int compareTo(Winner o) {
		// TODO Auto-generated method stub
		return this.playerId - o.playerId;
	}
	
	

}
