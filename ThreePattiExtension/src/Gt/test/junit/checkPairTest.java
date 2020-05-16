package Gt.test.junit;

import org.junit.Test;

import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;




public class checkPairTest {
	BaseGameLogic game = new  GameLogicObject().getGameLogicObj("ak47");
@Test
public void checkPairTestWhenListofCardsHasAPair(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.ACE,Suit.CLUBS));
	list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
	list.add(new Card(FaceValue.JACK,Suit.HEARTS));
	
	assertTrue(game.checkPair(list));	
	if(game.checkPair(list)){
		System.out.println("Pair present");
	}
	
}

@Test
public void checkPairTestWhenNoPairInListOfCards(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.NINE,Suit.CLUBS));
	list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
	list.add(new Card(FaceValue.JACK,Suit.HEARTS));
	
	assertFalse(game.checkPair(list));
	//assertTrue(game.checkPair(list));
	
	if(game.checkPair(list)){
		System.out.println("Pair present");
	}else{
		System.out.println("No Pair present");
	}
}

@Test
public void checkPairTestWhenListEmpty(){
	List<Card> list = new ArrayList<Card>();
	
	assertFalse("List empty",game.checkPair(list));
	//assertTrue(game.checkPair(list));
	
	
}

@Test
public void checkPairTestWhenListSizeMoreThan3(){
	
	List<Card> list = new ArrayList<Card>();
	list.add(new Card(FaceValue.NINE,Suit.CLUBS));
	list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
	list.add(new Card(FaceValue.JACK,Suit.HEARTS));
	list.add(new Card(FaceValue.ACE,Suit.CLUBS));
	list.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
	list.add(new Card(FaceValue.JACK,Suit.HEARTS));
	assertFalse("List empty",game.checkPair(list));
	//assertTrue(game.checkPair(list));
	
	
}




}
