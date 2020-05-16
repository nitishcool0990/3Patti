package Gt.common;

import java.util.List;

public class Pot {
	private double chips = 0;
	List<Integer> playerIds = null;
	//private transient List<TotalBets> playerWiseChip = null; // map to store each pot having chip according to player
	
	
	public Pot(double chips,  List<Integer> playerIds) {
		this.chips = chips;
		this.playerIds = playerIds;
	}
	
	public Pot(){
		
	}
	public List<Integer> getPlayerIds() {
		return playerIds;
	}


	public void setPlayerIds(List<Integer> playerIds) {
		this.playerIds = playerIds;
	}


	public double getChips() {
		return chips;
	}
	public void setChips(double chips) {
		this.chips = chips;
	}

	@Override
	public String toString() {
		return "Pot  [chips=" + this.chips + ", players= " + this.playerIds+" ]";
	}

	/*public List<TotalBets> getPlayerWiseChip() {
		return playerWiseChip;
	}

	public void setPlayerWiseChip(List<TotalBets> playerWiseChip) {
		this.playerWiseChip = playerWiseChip;
	}*/
	
	
	
	
}
