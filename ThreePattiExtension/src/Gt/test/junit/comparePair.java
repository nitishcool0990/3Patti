package Gt.test.junit;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Player;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class comparePair {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	@Test
	public void pair(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list1.add(new Card(FaceValue.NINE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.comparePair(p1, p2, jokers);
		
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void pair1(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list1.add(new Card(FaceValue.FIVE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.comparePair(p1, p2, jokers);
		
		assertTrue(p2.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	
	@Test
	public void pairAce(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		list1.add(new Card(FaceValue.ACE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.HEARTS));
		list2.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.comparePair(p1, p2, jokers);
		
		assertTrue(p2.isWinner());

		//assertTrue(game.checkTrio(list));
	}
}
