package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class HighestCard {
	BaseGameLogic game =new GameLogicObject().getGameLogicObj("normal");
	@Test
	public void highCard(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		assertEquals(4,  game.highestCard(list));	
		
		
	}
	
	@Test
	public void test2(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		
		assertEquals(14,  game.highestCard(list));	
		
		
	}
	
}
