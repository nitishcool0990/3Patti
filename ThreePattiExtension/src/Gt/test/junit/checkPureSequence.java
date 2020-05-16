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

public class checkPureSequence {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	
	@Test
	public void normalSequence(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));

		assertFalse(game.checkPureSequence(list));
		
		
	}
	@Test
	public void pureSequence(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));

		assertTrue(game.checkPureSequence(list));
		
		
	}
	
	@Test
	public void pureSequence2(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));

		assertTrue(game.checkPureSequence(list));
		
		
	}
	
	@Test
	public void pureSequence3(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.QUEEN,Suit.CLUBS));

		assertTrue(game.checkPureSequence(list));
		
		
	}
	
	@Test
	public void NoPureSequence(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list.add(new Card(FaceValue.QUEEN,Suit.CLUBS));

		assertFalse(game.checkPureSequence(list));
		
		
	}
	
	@Test
	public void NoPureSequence1(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.DIAMONDS));

		assertFalse(game.checkPureSequence(list));
		
	}

	
	@Test
	public void pureSequenceJokers(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(game.checkPureSequence(list,jokers));
		
		
	}
	@Test
public void pureSequenceJokers1(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(game.checkPureSequence(list,jokers));
		
		
	}
	@Test
public void pureSequenceJokers2(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.KING,Suit.SPADES));
	list.add(new Card(FaceValue.ACE,Suit.CLUBS));
	list.add(new Card(FaceValue.THREE,Suit.CLUBS));
	
	List<Card> jokers = new ArrayList<Card>();
	 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
	 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
	 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
	 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	
	assertTrue(game.checkPureSequence(list,jokers));
	
	
}
	
	@Test
public void pureSequenceJokers3(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.KING,Suit.SPADES));
	list.add(new Card(FaceValue.ACE,Suit.CLUBS));
	list.add(new Card(FaceValue.QUEEN,Suit.SPADES));
	
	List<Card> jokers = new ArrayList<Card>();
	 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
	 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
	 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
	 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	
	assertTrue(game.checkPureSequence(list,jokers));
	
	
}
	
	@Test
public void NoPureSequenceJokers(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.KING,Suit.SPADES));
	list.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
	list.add(new Card(FaceValue.THREE,Suit.CLUBS));
	
	List<Card> jokers = new ArrayList<Card>();
	 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
	 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
	 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
	 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	
	assertFalse(game.checkPureSequence(list,jokers));
	
	
}
	
	@Test
public void NoPureSequenceJokers1(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.ACE,Suit.SPADES));
	list.add(new Card(FaceValue.SIX,Suit.CLUBS));
	list.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
	
	List<Card> jokers = new ArrayList<Card>();
	 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
	 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
	 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
	 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	
	assertFalse(game.checkPureSequence(list,jokers));
	
	
}



	
}
