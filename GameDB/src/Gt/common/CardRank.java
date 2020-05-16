package Gt.common;

public enum CardRank {
	HR_TRIO(10,"Trio"),
	HR_PURE_SEQUENCE(9,"Pure Sequence"),
	HR_NORMAL_RUN(8,"Normal Run"),
	HR_COLOR(7,"Color"),
	HR_PAIR(6,"Pair"),
	HR_HIGH_CARD(5,"High Card");
	
	private String ranking;
	private int rankNumber;

	CardRank(int rankNumber,String ranking)
	{
		this.rankNumber = rankNumber;
		this.ranking = ranking;
	}
	
	@Override
	public String toString() {
		return this.ranking;
	}

	public String getRanking() {
		return ranking;
	}

	public int getRankNumber() {
		return rankNumber;
	}
	
	public CardRank getCardRankById(int rankNumber){
		
		for(CardRank rank : CardRank.values())
	    {
	        if(rank.getRankNumber() == rankNumber){ 
	        	return rank;
	        }
	    }
	    return null;
		
	}
}
