package Gt.common;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Card implements Comparable<Card> {
	
	public enum FaceValue{TWO(2),THREE(3),FOUR(4),FIVE(5),SIX(6),SEVEN(7),EIGHT(8),NINE(9),TEN(10),JACK(11),QUEEN(12),KING(13),ACE(14);
		
	public int value;
	private FaceValue(int value){
		this.value=value;
	}
	
	public int getValue() {
		return value;
	}
		
	
	
	}
	
	public enum Suit{ CLUBS("C"),DIAMONDS("D"),HEARTS("H"),SPADES("S");
	
		public String SuitCode;
		private Suit(String code){
			this.SuitCode = code;
		}
		public String getSuitCode() {
			return SuitCode;
		}
		
	}
	
	private final FaceValue faceValue;
	private final Suit suit;
	
	
	
	
	public Card(FaceValue faceValue, Suit suit) {
		this.faceValue = faceValue;
		this.suit = suit;
	}



	public FaceValue getFaceValue() {
		return faceValue;
	}



	public Suit getSuit() {
		return suit;
	}
	@Override
	public String toString() {
		return "" + suit.SuitCode  + faceValue.value ;
	}
	
	
	
	private static final List<Card> deck = new ArrayList<Card>();
	
	public static void fillDeck(){
		deck.clear();
		for(Suit s: Suit.values()){
			for(FaceValue r: FaceValue.values()){
				deck.add(new Card(r,s));
			}
		}
	}
	
	public static List<Card> newDeck() {
		return deck;
	}
	
	public List<Card> shuffle(List<Card> d){
		int n = d.size();
		List<Card> shuffled = new ArrayList<Card>(); 
		Random random = new Random();
		for(int i=n-1;i>0;i--){
			int j = random.nextInt(i);
			Card temp = d.get(i);
			d.set(i,d.get(j));
			d.set(j,temp);
		}
		
		return d;
		
	}
	
	public static void main(String[] args) {
		System.out.println(Suit.HEARTS.toString());
	}
	
	public static List<Card> getCardByRank(String cardRank){
		Random rand = new Random();
		//int  faceVal = Card.FaceValue.ACE.getValue();
		List<Card> cards = null;
		Map<Integer, List<Card>> cardPos = new HashMap<Integer, List<Card>>();
		Map<String, List<Card>> sameColorPos = new HashMap<String, List<Card>>();
		int index = 0;
		List<Card> cardIndex = null;
		
		for(Card card:deck){
			index++;
			if(cardRank.equalsIgnoreCase(CardRank.HR_TRIO.toString())){
				if(cardPos.containsKey(card.getFaceValue().getValue())){
					cardPos.get(card.getFaceValue().getValue()).add(card);
					if(cardPos.get(card.getFaceValue().getValue()).size() == 3){
						cards = cardPos.get(card.getFaceValue().getValue());
						break;
					}
				}else{
					cardIndex = new ArrayList<Card>();
					cardIndex.add(card);
					cardPos.put(card.getFaceValue().getValue(), cardIndex);
				}
			}else if(cardRank.equalsIgnoreCase(CardRank.HR_PURE_SEQUENCE.toString()) || cardRank.equalsIgnoreCase(CardRank.HR_NORMAL_RUN.toString())){
				if(card.getSuit().getSuitCode().equalsIgnoreCase("H") || cardRank.equalsIgnoreCase(CardRank.HR_NORMAL_RUN.toString())){
					if(cardPos.containsKey(card.getFaceValue().getValue())){
						cardPos.get(card.getFaceValue().getValue()).add(card);
						if(card.getFaceValue().getValue() != 1){
							if(cardPos.containsKey(card.getFaceValue().getValue()-1)){
								
								cardPos.get(card.getFaceValue().getValue()-1).add(card);
								
							}else{
								cardIndex = new ArrayList<Card>();
								cardIndex.add(card);
								cardPos.put(card.getFaceValue().getValue()-1, cardIndex);
							}
							
						}
						if(card.getFaceValue().getValue() != 2){
							if(cardPos.containsKey(card.getFaceValue().getValue()-2)){
								cardPos.get(card.getFaceValue().getValue()-2).add(card);
								
							}else{
								cardIndex = new ArrayList<Card>();
								cardIndex.add(card);
								cardPos.put(card.getFaceValue().getValue()-2, cardIndex);
							}
							
						}
					}else{
						cardIndex = new ArrayList<Card>();
						cardIndex.add(card);
						cardPos.put(card.getFaceValue().getValue(), cardIndex);
						if(card.getFaceValue().getValue() != 1){
							if(cardPos.containsKey(card.getFaceValue().getValue()-1)){
								cardPos.get(card.getFaceValue().getValue()-1).add(card);
								
							}else{
								cardIndex = new ArrayList<Card>();
								cardIndex.add(card);
								cardPos.put(card.getFaceValue().getValue()-1, cardIndex);
							}
							
						}
						if(card.getFaceValue().getValue() != 2){
							if(cardPos.containsKey(card.getFaceValue().getValue()-2)){
								cardPos.get(card.getFaceValue().getValue()-2).add(card);
								
							}else{
								cardIndex = new ArrayList<Card>();
								cardIndex.add(card);
								cardPos.put(card.getFaceValue().getValue()-2, cardIndex);
							}
							
						}
					}
				}
				boolean flag =false;
				for (Map.Entry<Integer, List<Card>> entry : cardPos.entrySet())
				{
					if(entry.getValue().size() ==3){
						flag = true;
						cards = entry.getValue();
						break;
					}
				}
				if(flag){
					break;
				}
			}else if(cardRank.equalsIgnoreCase(CardRank.HR_COLOR.toString())){
				if(sameColorPos.containsKey(card.getSuit().getSuitCode())){
					sameColorPos.get(card.getSuit().getSuitCode()).add(card);
					if(sameColorPos.get(card.getSuit().getSuitCode()).size() ==3){
						cards = sameColorPos.get(card.getSuit().getSuitCode());
						break;
					}
				}else {
					cardIndex = new ArrayList<Card>();
					cardIndex.add(card);
					sameColorPos.put(card.getSuit().getSuitCode(), cardIndex);
				}
			}else if(cardRank.equalsIgnoreCase(CardRank.HR_HIGH_CARD.toString())){
				if(cards == null){
					cards = new ArrayList<Card>();
				}
				cards.add(card);
				if(cards.size() ==3){
					break;
				}
			}
		}
		
		for(Card card:cards){
			deck.remove(card);
		}
		return cards;
		
	}

	public static FaceValue getFaceValueByVal(int faceValue){
		for(FaceValue faceVal : FaceValue.values()){
			if(faceVal.getValue() == faceValue){
				return faceVal;
			}
		}
		return null;
	}
	
	public static CardRank getCardRankById(int rankNumber){
		
		for(CardRank rank : CardRank.values())
	    {
	        if(rank.getRankNumber() == rankNumber){ 
	        	return rank;
	        }
	    }
	    return null;
		
	}
	
	@Override
	public int compareTo(Card o) {
		
		return this.faceValue.compareTo(o.getFaceValue());
		
	}


	
	
	

}
