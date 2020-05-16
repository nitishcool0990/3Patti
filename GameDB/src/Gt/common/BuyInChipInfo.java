package Gt.common;

import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class BuyInChipInfo implements SerializableSFSType {

	private String chipType;
	private String gameType;
	private double buyInLow;
	private double buyInHigh;
	private double realChips;
	private double chips;
	private int bootAmount;
	private double userBalance;
	

	public String getChipType() {
		return chipType;
	}

	public void setChipType(String chipType) {
		this.chipType = chipType;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public double getBuyInLow() {
		return buyInLow;
	}

	public void setBuyInLow(double buyInLow) {
		this.buyInLow = buyInLow;
	}

	public double getBuyInHigh() {
		return buyInHigh;
	}

	public void setBuyInHigh(double buyInHigh) {
		this.buyInHigh = buyInHigh;
	}

	public double getRealChips() {
		return realChips;
	}

	public void setRealChips(double realChips) {
		this.realChips = realChips;
	}

	public double getChips() {
		return chips;
	}

	public void setChips(double chips) {
		this.chips = chips;
	}

	public ISFSObject toSFSObject()
	  {
	    ISFSObject sfso = new SFSObject();
	    sfso.putClass("buyInchips", this);
	    return sfso;
	  }

	@Override
	public String toString() {
		return "BuyInChipInfo [chipType=" + chipType + ", gameType=" + gameType + ", buyInLow=" + buyInLow
				+ ", buyInHigh=" + buyInHigh + ", realChips=" + realChips + ", chips=" + chips + "]";
	}

	public int getBootAmount() {
		return bootAmount;
	}

	public void setBootAmount(int bootAmount) {
		this.bootAmount = bootAmount;
	}

	public double getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(double userBalance) {
		this.userBalance = userBalance;
	}
	
	
}
