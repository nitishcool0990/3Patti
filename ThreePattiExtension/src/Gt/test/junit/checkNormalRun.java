package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class checkNormalRun {
	
	private static BaseGameLogic game ;
	private static BaseGameLogic jokerGame ;
	
	@BeforeClass
	public static void setup(){
		
		game = new GameLogicObject().getGameLogicObj("normal");
		
		jokerGame = new GameLogicObject().getGameLogicObj("joker");
		
	}
	
	
	@Test
	public void NormalRun(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		assertTrue(game.checkNormalRun(list));	
		
		
	}
	
	@Test
	public void NoNormalRun(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		assertFalse(game.checkNormalRun(list));	
		
		
	}
	
	@Test
	public void NormalRunAKQ(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		Collections.sort(list);
		
		assertTrue(game.checkNormalRun(list));	
		
		
	}
	
	@Test
	public void NormalRunWithJoker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.TWO,Suit.SPADES));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		 List<Card> jokers = new ArrayList<Card>();
		 
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertTrue(jokerGame.checkNormalRun(list,jokers));	
		
		
	}
	
	@Test
	public void NoNormalRunWithJoker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.TWO,Suit.SPADES));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		list.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		 List<Card> jokers = new ArrayList<Card>();
		 
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertFalse(jokerGame.checkNormalRun(list,jokers));	
		
		
	}
	
	@Test
	public void NormalRunWithJokerUsedAsNormalCard(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		 List<Card> jokers = new ArrayList<Card>();
		 
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		 
		assertTrue(jokerGame.checkNormalRun(list,jokers));	
		
		
	}
	
	
	@Test
	public void AceKingTwoNotNormalRunl(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		
		System.out.println(list);
		Collections.sort(list);
		System.out.println(list);
		
		assertFalse(game.checkNormalRun(list));	
		
		
	}
	
	
	@Test
	public void AceKingQueenNormalRunl(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.CLUBS));
		
		System.out.println(list);
		Collections.sort(list);
		System.out.println(list);
		
		assertTrue(game.checkNormalRun(list));	
		
		
	}
	
	@Test
	public void AceKingWithOneJoker(){
		
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.KING,Suit.HEARTS));
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		
		List<Card> jokers = new ArrayList<Card>();
		 
		jokers.add(new Card(FaceValue.FOUR , Suit.HEARTS));
		
		assertTrue(jokerGame.checkNormalRun(list,jokers));
		
		
	}
	
	@Test
	public void AceQueenWithOneJoker(){
		
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.QUEEN,Suit.HEARTS));
		list.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		
		List<Card> jokers = new ArrayList<Card>();
		 
		jokers.add(new Card(FaceValue.EIGHT , Suit.HEARTS));
		
		assertTrue(jokerGame.checkNormalRun(list,jokers));
		
		
	}
	
	@Test
	public void NormalRunWithoutJoker(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.THREE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.HEARTS));
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		
		List<Card> jokers = new ArrayList<Card>();
		 
		jokers.add(new Card(FaceValue.SEVEN , Suit.HEARTS));
		
		assertTrue(jokerGame.checkNormalRun(list,jokers));
		
		
	}
	
}
