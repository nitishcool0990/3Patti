package Gt.common;

public class PlayerRoundInfo {
	private int playerId=0;
	private String playerName = "";
	private String cards = "";
	private boolean isWinner = false;
	private double winningAmount = 0;
	private double betAmount = 0;
	private double rake = 0;
	
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getCards() {
		return cards;
	}
	public void setCards(String cards) {
		this.cards = cards;
	}
	public double getWinningAmount() {
		return winningAmount;
	}
	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}
	public boolean isWinner() {
		return isWinner;
	}
	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}
	@Override
	public String toString() {
		return "PlayerRoundInfo [playerId=" + playerId + ", playerName=" + playerName + ", cards=" + cards
				+ ", isWinner=" + isWinner + ", winningAmount=" + winningAmount + "]";
	}
	public double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(double betAmount) {
		this.betAmount = betAmount;
	}
	public double getRake() {
		return rake;
	}
	public void setRake(double rake) {
		this.rake = rake;
	}
	
	
	
}
