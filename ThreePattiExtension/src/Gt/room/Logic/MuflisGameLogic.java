package Gt.room.Logic;

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

public class MuflisGameLogic extends BaseGameLogic{
	
	private Logger log = LoggerFactory.getLogger("Gt.controller.GameController");
	
	public void assignPlayerRank(Player p){
		 try {
			 
			 if(p.getPlayerRank() != null){
				 return;
			 }
			 
			List<Card>	cards = p.getHandCards();
			 Suit s = cards.get(0).getSuit();
			 int rank;
			 List<Card> holeCards = cards;
			 String handRank;
			 
			 if(checkTrio(cards)){
				 rank = 5;
				 handRank = CardRank.HR_TRIO.toString(); 
				
			 }else if(checkPureSequence(cards)){
				 rank = 6;
				 handRank = CardRank.HR_PURE_SEQUENCE.toString();
			 }else if(checkNormalRun(cards)){
				 rank = 7;
				 handRank = CardRank.HR_NORMAL_RUN.toString();
			 }else if(checkSameSuit(cards,s)){
				 rank = 8;
				 handRank = CardRank.HR_COLOR.toString();
			 }else if(checkPair(cards)){
				 rank = 9;
			 	 handRank = CardRank.HR_PAIR.toString(); 
			}
			 else{
				rank = 10;
				handRank = CardRank.HR_HIGH_CARD.toString();
			}
			
			 PlayerRank pr = new PlayerRank(rank, holeCards, handRank);
			 p.setPlayerRank(pr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("error inside assign player rank muflis " ,e);
			throw e;
		}
	   }
	
	public void compare2Players(Player p1, Player p2){
		try {
			PlayerRank pr1 = p1.getPlayerRank();
			PlayerRank pr2 = p2.getPlayerRank();
			
			if(pr1.getRank()>pr2.getRank()){
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
				
				//if Hand rank is tied due to both players having a pair, then compare the face value of the pair first.
				if(pr1.getHandRank().equals(CardRank.HR_PAIR.toString())){
					FaceValue pairFaceValue_p1 = getPairFaceValue(p1HandCards);
					FaceValue pairFaceValue_p2 = getPairFaceValue(p2HandCards);
					
					FaceValue nonPairFaceValue_p1 = getNonPairFaceValue(p1HandCards,pairFaceValue_p1);
					FaceValue nonPairFaceValue_p2 = getNonPairFaceValue(p2HandCards,pairFaceValue_p2);
					
					if(pairFaceValue_p1.compareTo(pairFaceValue_p2) < 0){
						p1.setWinner(true);
						return;
					}else if(pairFaceValue_p1.compareTo(pairFaceValue_p2) > 0){
						p2.setWinner(true);
						return;
					}else{
						if(nonPairFaceValue_p1.compareTo(nonPairFaceValue_p2) < 0){
							p1.setWinner(true);
							return;
						}else if(nonPairFaceValue_p1.compareTo(nonPairFaceValue_p2) > 0){
							p2.setWinner(true);
							return;
						}else{	
							// split pot/winnings
						}		
					}		
				}
				else{
				
					for(int i=0;i<3;i++){
						if(p1HandCards.get(2-i).getFaceValue().compareTo(p2HandCards.get(2-i).getFaceValue()) < 0){
							p1.setWinner(true);
							return;
						}else if(p1HandCards.get(2-i).getFaceValue().compareTo(p2HandCards.get(2-i).getFaceValue()) > 0){
							p2.setWinner(true);
							return;
						}else{
							continue;
							
						}					
					}
					// if code reaches here, split winnings/pot.
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("error inside compare 2 players muflis ", e);
		}
	
	}

	@Override
	public int getTrioCard(List<Card> handCards) {
		return handCards.get(0).getFaceValue().value;
	}
}
