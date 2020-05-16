package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Player;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class checkSameSuitTest {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	
	@Test
	public void ListofCardsOfSameSuit(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.JACK,Suit.CLUBS));
		
		assertTrue(game.checkSameSuit(list, Suit.CLUBS));	
		
		
	}
	
	@Test
	public void ListofCardsOfDifferentSuit(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		assertFalse(game.checkSameSuit(list, Suit.CLUBS));	
		
		
	}
	
	@Test
	public void ListofCardsOfSameSuitWith1Joker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		list.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertTrue(game.checkSameSuit(list, jokers));	
		
		
	}
	
	@Test
	public void ListofCardsOfSameSuitWith2Joker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.HEARTS));
		list.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertTrue(game.checkSameSuit(list, jokers));	
		
		
	}
	
	@Test
	public void ListofCardsOfDifferentSuitWith1Joker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.HEARTS));
		list.add(new Card(FaceValue.JACK,Suit.SPADES));
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertFalse(game.checkSameSuit(list, jokers));	
		
		
	}
	
	
	
}
