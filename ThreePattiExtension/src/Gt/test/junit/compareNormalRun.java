package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Player;
import Gt.room.Logic.Ak47GameLogic;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

public class compareNormalRun {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("normal");
	@Test
	public void sequence(){	
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
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
		
		game.compareNormalRun(p1, p2, jokers);
		
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sequqnce1(){	
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FIVE,Suit.SPADES));
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
		
		game.compareNormalRun(p1,p2,jokers);

		assertTrue(p2.isWinner());
	}
	
	@Test
	public void sequqnce2(){	
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
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
		Ak47GameLogic game = new Ak47GameLogic();
		game.assignPlayerRank(p1);
		game.assignPlayerRank(p2);
		game.compare2Players(p1,p2);

		assertTrue(p1.isWinner());
	}
	
	@Test
	public void nosequqnce2(){	
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareNormalRun(p1,p2,jokers);

		//assertFalse(p1.isWinner());
	}
}
