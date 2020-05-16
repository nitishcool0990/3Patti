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

public class checkTrio {
	BaseGameLogic simpleGame =new GameLogicObject().getGameLogicObj("normal");
	
	BaseGameLogic jokersGame =new GameLogicObject().getGameLogicObj("ak47");
	
	
	@Test
	public void trio(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));

		assertTrue(simpleGame.checkTrio(list));
	}
	
	@Test
	public void trio1(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));

		assertTrue(simpleGame.checkTrio(list));
	}
	
	@Test
	public void NoTrio(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));

		assertFalse(simpleGame.checkTrio(list));
	}
	
	@Test
	public void trioJokers(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void trioJokers1(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void trioJokers2(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void NoTrioJokers(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertFalse(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void NoTrioJokers1(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.TWO,Suit.SPADES));
		list.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertFalse(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void TrioJokers2(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.SPADES));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.HEARTS));
		 jokers.add(new Card(FaceValue.FOUR, Suit.DIAMONDS));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		assertTrue(jokersGame.checkTrio(list,jokers));
	}
	
	@Test
	public void trioJoker3(){	
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.THREE,Suit.SPADES));
		list.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 GameLogicObject obj = new GameLogicObject(jokers);
		 BaseGameLogic base = obj.getGameLogicObj(GameLogicObject.JOKER);
		 System.out.println(base.checkTrio(list,jokers));
		assertTrue(base.checkTrio(list,jokers));
	}
	
}
