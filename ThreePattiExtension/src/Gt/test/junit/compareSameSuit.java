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

public class compareSameSuit {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	@Test
	public void sameSuit(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
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
		
		game.compareSameSuit(p1, p2, jokers);
		
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	

	@Test
	public void sameSuit1(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.SEVEN,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareSameSuit(p1, p2, jokers);
		
		assertTrue(p2.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sameSuit2(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareSameSuit(p1, p2, jokers);
		
		assertTrue(p2.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sameSuit3(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareSameSuit(p1, p2, jokers);
		
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sameSuit4(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareSameSuit(p1, p2, jokers);
		
		//assertTrue(p2.isWinner());
		assertTrue(p1.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void sameSuit5(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareSameSuit(p1, p2, jokers);
		
		//assertTrue(p1.isWinner());
		assertTrue(p2.isWinner());

		//assertTrue(game.checkTrio(list));
	}
	
	
	@Test
	public void sameSuitWith1Joker(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.SEVEN,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);			
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		
		/*game.assignPlayerRank(p1);
		game.assignPlayerRank(p2);
		
		System.out.println(p1.getPlayerRank().getHandRank() + " " + p1.getPlayerRank().getRank());
		System.out.println(p2.getPlayerRank().getHandRank() + " " + p2.getPlayerRank().getRank());*/
		
		game.compareSameSuit(p1,p2,jokers);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void sameSuit7(){	
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.SEVEN,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);			
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		
		/*game.assignPlayerRank(p1);
		game.assignPlayerRank(p2);
		
		System.out.println(p1.getPlayerRank().getHandRank() + " " + p1.getPlayerRank().getRank());
		System.out.println(p2.getPlayerRank().getHandRank() + " " + p2.getPlayerRank().getRank());*/
		
		game.compareSameSuit(p1,p2,jokers);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void bothPlayerSameSuitNoJokers(){
		Player p1 = new Player(123,"ooo");
		Player p2 = new Player(123,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.HEARTS));
		list1.add(new Card(FaceValue.THREE,Suit.HEARTS));
		list1.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);			
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		game.compareSameSuit(p1,p2,jokers);
		
		assertTrue(p2.isWinner());
		//assertFalse(p1.isWinner());
		
	}
	
	@Test
	public void bothPlayerHaveOneJoker(){
		Player p1 = new Player(123,"ooo");
		Player p2 = new Player(123,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.NINE,Suit.CLUBS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);			
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		game.compareSameSuit(p1,p2,jokers);
		
		assertTrue(p2.isWinner());
		assertFalse(p1.isWinner());
		
	}
	

	@Test
	public void bothPlayerHaveOneJokerAndAnotherSameCard(){
		Player p1 = new Player(123,"ooo");
		Player p2 = new Player(123,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		Collections.sort(list1);
		Collections.sort(list2);			
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		game.compareSameSuit(p1,p2,jokers);
		
		assertTrue(p2.isWinner());
		assertFalse(p1.isWinner());
		
	}
	
	
	
	
	
}
