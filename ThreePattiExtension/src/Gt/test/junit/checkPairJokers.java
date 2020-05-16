package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Player;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;
import Gt.room.Logic.Ak47GameLogic;

public class checkPairJokers {
	BaseGameLogic game = new GameLogicObject().getGameLogicObj("ak47");
	 
	 
	 
	@Test
	public void ListofCardsHasPairOfJokers(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		assertTrue(game.checkPair(list,jokers));	
		
	}
@Test		
public void ListofCardsHasPairOfNonJokers(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list.add(new Card(FaceValue.TEN,Suit.HEARTS));
		
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		assertTrue(game.checkPair(list,jokers));	
		
	}


@Test		
public void ListofCardsHasNoPair(){
		
		List<Card> list = new ArrayList<Card>();
		list.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		list.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		
		 List<Card> jokers = new ArrayList<Card>();
		 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		assertFalse(game.checkPair(list,jokers));	
		
	}
@Test
public void ListofCardsHasOneJoker(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
	list.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
	list.add(new Card(FaceValue.ACE,Suit.HEARTS));
	
	
	 List<Card> jokers = new ArrayList<Card>();
	 jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
	 jokers.add(new Card(FaceValue.KING, Suit.SPADES));
	 jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
	 jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
	
	
	assertTrue(game.checkPair(list,jokers));	
	
}

@Test
public void compare2playersTestTwoCardSameOneIsJoker(){
	Player p1 = new Player(426,"ram");
	Player p2 = new Player(427,"shaam");
	List<Card> jokers = new ArrayList<Card>();
	jokers.add(new Card(FaceValue.THREE,Suit.HEARTS));
	GameLogicObject obj =new GameLogicObject(jokers);
	BaseGameLogic sgl = obj.getGameLogicObj(GameLogicObject.JOKER);
	
	List<Card> list1 = new ArrayList<Card>();
	list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
	list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
	list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
	
	List<Card> list2 = new ArrayList<Card>();
	list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
	list2.add(new Card(FaceValue.THREE,Suit.HEARTS));
	list2.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
	
	p1.setHandCards(list1);
	p2.setHandCards(list2);
	
	sgl.assignPlayerRank(p1);
	sgl.assignPlayerRank(p2);
	sgl.compare2Players(p1, p2);
	
	System.out.println(p1.getPlayerRank() + " " + p2.getPlayerRank());
	
	assertTrue(p2.isWinner());
	
	
}



}
