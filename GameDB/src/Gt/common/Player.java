package Gt.common;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Player implements Comparable<Player>{
	private double chips = 10000;
	private double chipsLeft = 10000;
	private PlayerStatus playerStatus = PlayerStatus.BLIND;
	private PlayerActionType playerTookAction = PlayerActionType.PACK;
	private List<String> playerActions = new ArrayList<String>();
	private double minBet = 0;
	private double maxBet = 0;
	private Status status = Status.INACTIVE;
	
	private boolean isPack = false;
	
	private List<Card> handCards = new ArrayList<Card>();
	private boolean isAway = false;
	private String name = "";
	private int playerId = -1;
	private Seat seat = null;
	// PlayerRank
	private boolean isWinner = false;
	private boolean isBlind = true;
	private PlayerRank playerRank = null;
	private boolean turnDataSent = false;
	private double lastBetAmount = 0;
	private int turnCount = 0;
	private double AllInAmount = 0;
	private boolean isAllin =false;
	public ScheduledFuture<?> taskHandle;
	private int noActivityCount = 0;
	private int orderId = 0;								// needed by operator/platform to make an entry in gt_tansaction_history using order_id as foreign key.
	private int advanceBetAmount = -1;
	private PlayerActionType advanceAction = null;
	private double bet=0;
	private double minCurrentStake =0;
    private DecimalFormat df = new DecimalFormat("0.000");
    private boolean isAdvanceActionTaken = false;

	public int getAdvanceBetAmount() {
		return advanceBetAmount;
	}

	public void setAdvanceBetAmount(int advanceBetAmount) {
		this.advanceBetAmount = advanceBetAmount;
	}

	public PlayerActionType getAdvanceAction() {
		return advanceAction;
	}

	public void setAdvanceAction(PlayerActionType advanceAction) {
		this.advanceAction = advanceAction;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public boolean isPlayerAllIn(){
    	if(this.lastBetAmount >= this.chipsLeft){
    		return true;
    	}else{
    		return false;
    	}
    }
    
	public Player(int playerId,String name){
		this.playerId =playerId;
		this.name =name;
	}
	public Player() {
		
	}

	public enum Action {
		CHAAL, PACK, RAISE;
	}

	public enum PlayerStatus {
		BLIND, SEEN;
	}
	
	public enum Status{
		TAKESEAT, ACTIVE, INACTIVE;
	}

	public double getChips() {
		return chips;
	}

	public void setChips(double chips) {
        chips = Double.valueOf(df.format(chips));
		if (this.seat != null) {
			this.seat.setChips(chips);
		}
		this.chips = chips;
	}

	public double getChipsLeft() {
		return chipsLeft;
	}

	public void setChipsLeft(double chipsLeft) {
        chipsLeft = Double.valueOf(df.format(chipsLeft));
		if (this.seat != null) {
			this.seat.setChipsLeft(chipsLeft);
		}
		this.chipsLeft = chipsLeft;
	}
	public PlayerStatus getPlayerStatus() {
		return playerStatus;
	}
	public void setPlayerStatus(PlayerStatus playerStatus) {
		this.playerStatus = playerStatus;
	}

	public List<Card> getHandCards() {
		return handCards;
	}

	public void setHandCards(List<Card> handCards) {
		this.handCards = handCards;
	}

	public boolean isAway() {
		return isAway;
	}

	public void setAway(boolean isAway) {
		if (this.seat != null) {
			this.seat.setAway(isAway);
		}
		this.isAway = isAway;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		if (seat != null) {
			seat.setPlayerId(this.playerId);
			seat.setOccupied(true);
			seat.setReserved(false);
			seat.setPlayerName(this.name);
		}
		this.seat = seat;
	}

	public PlayerRank getPlayerRank() {
		return playerRank;
	}

	public void setPlayerRank(PlayerRank playerRank) {
		this.playerRank = playerRank;
	}

	public boolean isWinner() {
		return isWinner;
	}

	public void setWinner(boolean isWinner) {
		this.isWinner = isWinner;
	}

	public PlayerActionType getPlayerTookAction() {
		return playerTookAction;
	}

	public void setPlayerTookAction(PlayerActionType playerTookAction) {
		this.playerTookAction = playerTookAction;
		if(this.seat != null){
			
			if(!playerTookAction.equals(PlayerActionType.PACK)){
				seat.setLastAction(playerTookAction.toString());
			}
			
		}
	}
	public List<String> getPlayerActions(boolean reset) {
		if(reset){
			this.playerActions.clear();
			if(this.playerStatus.equals(PlayerStatus.BLIND)){
				this.playerActions.add(PlayerActionType.BLIND.toString());
			}else{
				this.playerActions.add(PlayerActionType.CHAAL.toString());
			}
		}
		
		
		return playerActions;
	}
	

	public boolean isBlind() {
		return isBlind;
	}

	public void setBlind(boolean isBlind) {
		this.isBlind = isBlind;
	}
	public double getMinBet() {
		return minBet;
	}
	public void setMinBet(double minBet) {
		this.minBet = minBet;
	}
	public double getMaxBet() {
		return maxBet;
	}
	public void setMaxBet(double maxBet) {
		this.maxBet = maxBet;
	}
	public boolean isTurnDataSent() {
		return turnDataSent;
	}
	public void setTurnDataSent(boolean turnDataSent) {
		this.turnDataSent = turnDataSent;
	}
	public boolean isPack() {
		return isPack;
	}
	public void setPack(boolean isPack) {
		if(this.seat!=null){
			this.seat.setPack(isPack);
		}
		this.isPack = isPack;
	}
	public double getLastBetAmount() {
		return lastBetAmount;
	}
	public void setLastBetAmount(double lastBetAmount) {
		if (this.seat != null) {
			this.seat.setLastBetAmount(lastBetAmount);

			this.seat.setBet(this.seat.getBet()+lastBetAmount);
			this.bet = this.bet+lastBetAmount;
		}
		
		this.lastBetAmount = lastBetAmount;
	}
	public void reset() {
		//this.chips=0;
		//this.chipsLeft =0;
		
		this.playerTookAction =  PlayerActionType.PACK;
		this.playerActions = new ArrayList<String>();
		this.playerStatus = PlayerStatus.BLIND;
		this.status = Status.INACTIVE;
		this.seat.setStatus(2);
		this.isAllin =false;
		
		
		this.minBet = 0;
		this.maxBet = 0;
		
		this.isPack = false;
		
		//this.handCards = null;
		//this.isAway = false;
		//this.name="";
		//this.playerId =-1;
		
		this.isWinner = false;
		this.isBlind = true;
		this.playerRank = null;
		this.turnDataSent = false;
		this.lastBetAmount = 0;
		this.turnCount = 0;
		//this.orderId = 0;
		this.handCards.clear();
		this.advanceAction = null;
		this.advanceBetAmount = 0;
		this.bet =0;
		this.minCurrentStake = 0;
	}

	public double getAllInAmount() {
		return AllInAmount;
	}

	public void setAllInAmount(double allInAmount) {
		AllInAmount = allInAmount;
	}

	@Override
	public int compareTo(Player o) {
		Double diff = this.chips - o.chips;
		return diff.intValue();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
		if(this.seat != null){
			if(status.equals(Status.TAKESEAT)){
				this.seat.setStatus(0) ;
			}
			else if(status.equals(Status.ACTIVE)){
				this.seat.setStatus(1);
			}
			else if(status.equals(Status.INACTIVE)){
				this.seat.setStatus(2);
			}
			else{
				System.out.println("INvalid player status");
			}

		}
	}

	@Override
	public String toString() {
		return "Player [ name=" + name + ", playerId=" + playerId+ " ,seat = "+seat
				+ "chips= " + chips + ", chipsLeft=" + chipsLeft + ", playerStatus=" + playerStatus
				+ ", playerTookAction=" + playerTookAction + ", playerActions=" + playerActions + ", status=" + status
				+ ", isPack=" + isPack + ", isAway=" + isAway + ", isWinner=" + isWinner + ", isBlind=" + isBlind + ", lastBetAmount=" + lastBetAmount
				+ ", AllInAmount=" + AllInAmount + ", isAllin=" + isAllin + "mainBet=" + minBet + "minCurrentStake=" + minCurrentStake +"maxBet=" + maxBet + "]";
	}
	
	

	public boolean isAllin() {
		return isAllin;
	}

	public void setAllin(boolean isAllin) {
		if (this.seat != null) {
			this.seat.setAllIn(isAllin);
		}
		this.isAllin = isAllin;
		
	}

	public int getTurnCount() {
		return turnCount;
	}

	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}

	public int getNoActivityCount() {
		return noActivityCount;
	}

	public void setNoActivityCount(int noActivityCount) {
		this.noActivityCount = noActivityCount;
	}

	public double getBet() {
		return bet;
	}

	public void setBet(double bet) {
		this.bet = bet;
	}

	public double getMinCurrentStake() {
		return minCurrentStake;
	}

	public void setMinCurrentStake(double minCurrentStake) {
		this.minCurrentStake = minCurrentStake;
	}
	
	 public boolean isAdvanceActionTaken() {
			return isAdvanceActionTaken;
		}

	public void setAdvanceActionTaken(boolean isAdvanceActionTaken) {
		this.isAdvanceActionTaken = isAdvanceActionTaken;
	}

}
