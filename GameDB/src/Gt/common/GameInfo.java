package Gt.common;

import java.util.ArrayList;
import java.util.List;

import com.smartfoxserver.v2.protocol.serialization.SerializableSFSType;

public class GameInfo implements SerializableSFSType {

	public int turnTimer = -1;
	public int whoseTurnId = -1;
	public int dealerId = -1;
	public int dealerSeatId = -1;
	public int gameTimer = -1;
	public PotInfo potInfo = new PotInfo();
	public WinnerInfo winInfo = new WinnerInfo();
	public int roundId = 0;
	public String gameState;
	public PlayersInfo playersInfo;
	public int mySeatId = -1;
	public int totalPot = 0;
	public int lastBetAmt = -1;
	public int rake = 0;
	public int sideShowPlayerId = -1;
	public int sideShowTimer = -1;
	public int seeCardsTimer = -1;
	public String whoseTurnPlayerName = "";
	public List<Double> playerBets = new ArrayList<Double>();
	public List<String> myCards = new ArrayList<String>();
	public String myRank = "";
	public List<String> cards = new ArrayList<String>();
	public List<String> ranks = new ArrayList<String>();
	public String gameVariant = "";
	public boolean isTopUpPending = false;
	public List<String> playerActions = new ArrayList<String>();
	public List<String> jokers = new ArrayList<String>();
	public List<String> dealerChatLogs = new ArrayList<String>();
	public String advancePlayerAction = "";
	public int advanceBetAmount = 0;
	
	
	public int getAdvanceBetAmount() {
		return advanceBetAmount;
	}
	public void setAdvanceBetAmount(int advanceBetAmount) {
		this.advanceBetAmount = advanceBetAmount;
	}
	public String getAdvancePlayerAction() {
		return advancePlayerAction;
	}
	public void setAdvancePlayerAction(String advancePlayerAction) {
		this.advancePlayerAction = advancePlayerAction;
	}
	public String getWhoseTurnPlayerName() {
		return whoseTurnPlayerName;
	}
	public void setWhoseTurnPlayerName(String whoseTurnPlayerName) {
		this.whoseTurnPlayerName = whoseTurnPlayerName;
	}
	
	public boolean isTopUpPending() {
		return isTopUpPending;
	}
	public void setTopUpPending(boolean isTopUpPending) {
		this.isTopUpPending = isTopUpPending;
	}
	public List<String> getJokers() {
		return jokers;
	}
	public void setJokers(List<String> jokers) {
		this.jokers = jokers;
	}
	
	public List<String> getDealerChatLogs() {
		return dealerChatLogs;
	}
	public void setDealerChatLogs(List<String> dealerChatLogs) {
		this.dealerChatLogs = dealerChatLogs;
	}
	public String getGameVariant() {
		return gameVariant;
	}
	public void setGameVariant(String gameVariant) {
		this.gameVariant = gameVariant;
	}
	public int getSeeCardsTimer() {
		return seeCardsTimer;
	}
	public void setSeeCardsTimer(int seeCardsTimer) {
		this.seeCardsTimer = seeCardsTimer;
	}
	public List<String> getCards() {
		return cards;
	}
	public void setCards(List<String> cards) {
		this.cards = cards;
	}
	public List<String> getRanks() {
		return ranks;
	}
	public void setRanks(List<String> ranks) {
		this.ranks = ranks;
	}
	public String getMyRank() {
		return myRank;
	}
	public void setMyRank(String myRank) {
		this.myRank = myRank;
	}
	public List<String> getMyCards() {
		return myCards;
	}
	public void setMyCards(List<String> myCards) {
		this.myCards = myCards;
	}
	public int getSideShowTimer() {
		return sideShowTimer;
	}
	public void setSideShowTimer(int sideShowTimer) {
		this.sideShowTimer = sideShowTimer;
	}
	public int getSideShowPlayerId() {
		return sideShowPlayerId;
	}
	public void setSideShowPlayerId(int sideShowPlayerId) {
		this.sideShowPlayerId = sideShowPlayerId;
	}
	public int getRake() {
		return rake;
	}
	public void setRake(int rake) {
		this.rake = rake;
	}
	public List<Double> getPlayerBets() {
		return playerBets;
	}
	public void setPlayerBets(List<Double> playerBets) {
		this.playerBets = playerBets;
	}
	public int getDealerSeatId() {
		return dealerSeatId;
	}
	public void setDealerSeatId(int dealerSeatId) {
		this.dealerSeatId = dealerSeatId;
	}
	public WinnerInfo getWinInfo() {
		return winInfo;
	}
	public void setWinInfo(WinnerInfo winInfo) {
		this.winInfo = winInfo;
	}
	public int getTurnTimer() {
		return turnTimer;
	}
	public void setTurnTimer(int turnTimer) {
		this.turnTimer = turnTimer;
	}
	public int getWhoseTurnId() {
		return whoseTurnId;
	}
	public void setWhoseTurnId(int whoseTurnId) {
		this.whoseTurnId = whoseTurnId;
	}
	public int getDealerId() {
		return dealerId;
	}
	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}
	public int getGameTimer() {
		return gameTimer;
	}
	public void setGameTimer(int gameTimer) {
		this.gameTimer = gameTimer;
	}
	public PotInfo getPotInfo() {
		return potInfo;
	}
	public void setPotInfo(PotInfo potInfo) {
		this.potInfo = potInfo;
	}
	public int getRoundId() {
		return roundId;
	}
	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}
	public String getGameState() {
		return gameState;
	}
	public void setGameState(String gameState) {
		this.gameState = gameState;
	}
	public PlayersInfo getPlayersInfo() {
		return playersInfo;
	}
	public void setPlayersInfo(PlayersInfo playersInfo) {
		this.playersInfo = playersInfo;
	}
	public int getMySeatId() {
		return mySeatId;
	}
	public void setMySeatId(int mySeatId) {
		this.mySeatId = mySeatId;
	}
	public int getTotalPot() {
		return totalPot;
	}
	public void setTotalPot(int totalPot) {
		this.totalPot = totalPot;
	}
	public int getLastBetAmt() {
		return lastBetAmt;
	}
	public void setLastBetAmt(int lastBetAmt) {
		this.lastBetAmt = lastBetAmt;
	}
	public List<String> getPlayerActions() {
		return playerActions;
	}
	
	public void setPlayerActions(List<String> playerActions) {
		this.playerActions = playerActions;
	}
	@Override
	public String toString() {
		return "GameInfo [turnTimer=" + turnTimer + ", whoseTurnId=" + whoseTurnId + ", dealerId=" + dealerId
				+ ", gameTimer=" + gameTimer + ", potInfo=" + potInfo + ", roundId=" + roundId + ", gameState="
				+ gameState + ", playersInfo=" + playersInfo + ", mySeatId=" + mySeatId + ", totalPot=" + totalPot
				+ ", lastBetAmt=" + lastBetAmt + ", playerActions=" + playerActions + "]";
	}
	
	
	
}
