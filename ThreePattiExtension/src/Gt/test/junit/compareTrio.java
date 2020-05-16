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
import Gt.common.Player;

public class compareTrio {
BaseGameLogic game =new GameLogicObject().getGameLogicObj("ak47");
	
	@Test
	public void trio(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		game.compareTrio(p1,p2,jokers);
		
		if(p1.isWinner()){
			System.out.println("p1 winner");
		}else if(p2.isWinner()){
			System.out.println("p2 winner");
		}else{
			System.out.println("tied");
		}

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void trio1(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FIVE,Suit.SPADES));
		list1.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		game.compareTrio(p1,p2,jokers);

		
		
		if(p1.isWinner()){
			System.out.println("p1 winner");
		}else if(p2.isWinner()){
			System.out.println("p2 winner");
		}else{
			System.out.println("tied");
		}

		//assertTrue(game.checkTrio(list));
	}
	
	@Test
	public void trio2(){	
		Player p1 = new Player(456,"ooo");
		Player p2 = new Player(456,"ooo");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.SEVEN,Suit.SPADES));
		list1.add(new Card(FaceValue.SEVEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.SPADES));
		list2.add(new Card(FaceValue.SEVEN,Suit.CLUBS));
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.ACE , Suit.SPADES));
		jokers.add(new Card(FaceValue.KING, Suit.SPADES));
		jokers.add(new Card(FaceValue.FOUR, Suit.SPADES));
		jokers.add(new Card(FaceValue.SEVEN, Suit.SPADES));
		
		
		game.compareTrio(p1,p2,jokers);

		
		
		if(p1.isWinner()){
			System.out.println("p1 winner");
		}else if(p2.isWinner()){
			System.out.println("p2 winner");
		}else{
			System.out.println("tied");
		}

		//assertTrue(game.checkTrio(list));
	}
}
