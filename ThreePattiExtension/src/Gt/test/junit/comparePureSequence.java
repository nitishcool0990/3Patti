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

public class comparePureSequence {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	@Test
	public void sequence(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		
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
		
		game.comparePureSequence(p1, p2, jokers);
		
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sequqnce1(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		
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
		
		game.comparePureSequence(p1,p2,jokers);

		assertTrue(p2.isWinner());
	}
	
	@Test
	public void sequqnce2(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		//BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
		game.assignPlayerRank(p1);
		game.assignPlayerRank(p2);
		game.compare2Players(p1,p2);

		assertTrue(p1.isWinner());
	}
	
	@Test
	public void sequqnce3(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);
		
	
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.comparePureSequence(p1,p2,jokers);

		assertTrue(p2.isWinner());
	}
	
	@Test
	public void sequqnce5(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.assignPlayerRank(p1);
		game.assignPlayerRank(p2);
		game.compare2Players(p1,p2);
		
		game.comparePureSequence(p1,p2,jokers);

		assertTrue(p1.isWinner());
	}
	
	@Test
	public void sequqnce6(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);
		
		System.out.println(list1);
		System.out.println(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.comparePureSequence(p1,p2,jokers);

		assertFalse(p2.isWinner());
	}
	
	
}
