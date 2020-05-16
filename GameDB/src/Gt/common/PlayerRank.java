package Gt.common;

import java.util.List;

public class PlayerRank {
	
	private int rank;
	private List<Card> handCards;
	//private String gameVariant;//handRank
	private String handRank;
	
	public PlayerRank(int rank, List<Card> handCards, String handRank) {
		this.rank = rank;
		this.handCards = handCards;
		this.handRank = handRank;
	}

	public int getRank() {
		return rank;
	}

	public List<Card> getHandCards() {
		return handCards;
	}

	

	public void setRank(int rank) {
		this.rank = rank;
	}

	public void setHandCards(List<Card> handCards) {
		this.handCards = handCards;
	}

	public String getHandRank() {
		return handRank;
	}

	public void setHandRank(String handRank) {
		this.handRank = handRank;
	}

	
	
	
	
	

}
