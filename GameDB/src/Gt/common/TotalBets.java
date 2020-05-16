package Gt.common;


public class TotalBets {
	private int playerId =-1;
	private double chip =0;
	
	public TotalBets(int playerId, int chip){
		this.playerId =playerId;
		this.chip = chip;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public double getChip() {
		return chip;
	}
	public void setChip(double chip) {
		this.chip = chip;
	}

	@Override
	public String toString() {
		return "TotalBets [playerId=" + playerId + ", chip=" + chip + "]";
	}
	
	
	 
}
