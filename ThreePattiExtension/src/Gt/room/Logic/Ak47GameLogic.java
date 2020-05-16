package Gt.room.Logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.base.BaseGameLogic;
import Gt.common.Card;
import Gt.common.CardRank;
import Gt.common.Player;
import Gt.common.PlayerRank;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class Ak47GameLogic extends BaseGameLogic {
	
	private Logger log = LoggerFactory.getLogger("Gt.controller.GameController");
	
	private List<Card> jokers = new ArrayList<Card>();
	
	public Ak47GameLogic(List<Card> jokers) {
		this.jokers = jokers;
	}
	
	public Ak47GameLogic() {
		 jokers.clear();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	}




	public void assignPlayerRank(Player p){
		 try {
			 
			 if(p.getPlayerRank() != null){
				 return;
			 }
			 
			List<Card>	cards = p.getHandCards();
			 Suit s = cards.get(0).getSuit();
			 int rank=0;
			 List<Card> holeCards = cards;
			 String handRank;
			 
			/* List<Card> jokers = new ArrayList<Card>();
			 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
			 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
			 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
			 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));*/
			 
			 if(checkTrio(cards,jokers)){
				 rank = CardRank.HR_TRIO.getRankNumber();
				 handRank = CardRank.HR_TRIO.toString(); 
				 
			 }else if(checkPureSequence(cards,jokers)){
				 rank = CardRank.HR_PURE_SEQUENCE.getRankNumber();
				 handRank = CardRank.HR_PURE_SEQUENCE.toString();
			 }else if(checkNormalRun(cards,jokers)){
				 rank = CardRank.HR_NORMAL_RUN.getRankNumber();
				 handRank = CardRank.HR_NORMAL_RUN.toString();
			 }else if(checkSameSuit(cards,jokers)){
				 rank = CardRank.HR_COLOR.getRankNumber();
				 handRank = CardRank.HR_COLOR.toString();
			 }else if(checkPair(cards,jokers)){
				 rank = CardRank.HR_PAIR.getRankNumber();
			 	 handRank = CardRank.HR_PAIR.toString(); 
			}
			 else{
				rank = CardRank.HR_HIGH_CARD.getRankNumber();
				handRank = CardRank.HR_HIGH_CARD.toString();
			}
			
			 PlayerRank pr = new PlayerRank(rank, holeCards, handRank);
			 p.setPlayerRank(pr);
		} catch (Exception e) {
			log.error("error inside assign PLayer Rank - ak47 " , e);
			e.printStackTrace();
			throw e;
		}
	   }
	
	
	public void compare2Players(Player p1,Player p2){
		try {
			PlayerRank pr1 = p1.getPlayerRank();
			PlayerRank pr2 = p2.getPlayerRank();
			
			if(pr1.getRank()> pr2.getRank()){
				p1.setWinner(true);
			}else if(pr2.getRank()> pr1.getRank()){
				p2.setWinner(true);
			}else{
				
				//p1.handcards sort them
				//then compare on face value
				List<Card> p1HandCards = p1.getHandCards();
				List<Card> p2HandCards = p2.getHandCards();
				
				Collections.sort(p1HandCards);
				Collections.sort(p2HandCards);
				
				int rank = pr1.getRank();
				
				switch (rank){
				case 10: // Both players have a trio
					compareTrio(p1,p2,jokers);		
					
					break;
				case 9:
					comparePureSequence(p1,p2,jokers);
					break;
					
				case 8:
					compareNormalRun(p1,p2,jokers);
					break;
				
				case 7:
					compareSameSuit(p1,p2,jokers);
					break;
				case 6: 
					comparePair(p1,p2,jokers);
					
					break;					
				case 5: 
					
					for(int i=0;i<3;i++){
						if(p1HandCards.get(2-i).getFaceValue().compareTo(p2HandCards.get(2-i).getFaceValue()) > 0){
							p1.setWinner(true);
							return;
						}else if(p1HandCards.get(2-i).getFaceValue().compareTo(p2HandCards.get(2-i).getFaceValue()) < 0){
							p2.setWinner(true);
							return;
						}else{
							continue;
							
						}					
					}
					
					break;
				}
				
			}
		} catch (Exception e) {
			log.error("error inside compare 2 players - ak47 " , e);
			e.printStackTrace();
			throw e;
		}
		
	}

	@Override
	public int getTrioCard(List<Card> handCards){
		if(!checkTrio(handCards,jokers)){
			return -1;
		}
		Card temp = null;
		for(Card card : handCards){
			temp = card;
			if(isJoker(card, jokers)){
				continue;
			}else{
				return card.getFaceValue().value;
			}
		}
		return FaceValue.ACE.value;
	}


	
}
