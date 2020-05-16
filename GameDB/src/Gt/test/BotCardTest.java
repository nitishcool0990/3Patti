package Gt.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Gt.common.Card;
import Gt.common.CardRank;
import Gt.common.Table;

public class BotCardTest {
	List<Card> deck =null;
	@Before
	public void fillDeck(){
		Card.fillDeck();
		deck = Card.newDeck();
		Table table = new Table();
		table.shuffle(deck);
	}
	
	@Test
	public void CheckTrio(){
		System.out.println(deck);
		List<Card> cards = Card.getCardByRank(CardRank.HR_TRIO.toString());
		System.out.println("----------------  "+deck);
		int cardFace = 0;
		for(Card card : cards){
			System.out.println(card);
			if(cardFace == 0){
				cardFace = card.getFaceValue().getValue();
			}else{
				if(cardFace != card.getFaceValue().getValue()){
					assertTrue("CheckAceTrio will fail!", false);
				}
			}
		}
		assertTrue("CheckAceTrio will Pass!", true);
	}
	
	@Test
	public void CheckPureSequence(){
		List<Card> cards = Card.getCardByRank(CardRank.HR_PURE_SEQUENCE.toString());
		int cardFace = 0;
		for(Card card : cards){
			System.out.println(card);
			
			
		}
		assertTrue("CheckAceTrio will Pass!", true);
	}
	
	@Test
	public void CheckSequence(){
		List<Card> cards = Card.getCardByRank(CardRank.HR_NORMAL_RUN.toString());
		int cardFace = 0;
		for(Card card : cards){
			System.out.println(card);
			
			
		}
		assertTrue("CheckAceTrio will Pass!", true);
	}
	
	@Test
	public void CheckColor(){
		List<Card> cards = Card.getCardByRank(CardRank.HR_COLOR.toString());
		int cardFace = 0;
		for(Card card : cards){
			System.out.println(card);
			
			
		}
		assertTrue("CheckAceTrio will Pass!", true);
	}
	
	@Test
	public void CheckHigh(){
		
		List<Card> cards = Card.getCardByRank(CardRank.HR_HIGH_CARD.toString());
		int cardFace = 0;
		for(Card card : cards){
			System.out.println(card);
			
			
		}
		assertTrue("CheckAceTrio will Pass!", true);
	}


}
