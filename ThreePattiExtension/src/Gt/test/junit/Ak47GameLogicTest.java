package Gt.test.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.common.CardRank;
import Gt.base.BaseGameLogic;
import Gt.base.GameLogicObject;
import Gt.common.Card;
import Gt.common.Player;
import Gt.common.PlayerRank;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;
import Gt.room.Logic.Ak47GameLogic;
import Gt.room.Logic.SimpleGameLogic;

public class Ak47GameLogicTest {
	@Test
	public void assignPlayerRankTest_trio(){
		Player p = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_TRIO.toString());
		assertEquals(pr.getRank(), 10);
	}
	
	@Test
	public void assignPlayerRankTest_pure_sequence(){
		Player p = new Player(426,"ram");
		Player p2 = new Player(989,"kk");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.SPADES));
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_PURE_SEQUENCE.toString());
		assertEquals(pr.getRank(), 9);
	}
	
	@Test
	public void assignPlayerRankTest_normalrun(){
		Player p = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_NORMAL_RUN.toString());
		assertEquals(pr.getRank(), 8);
	}
	
	@Test
	public void assignPlayerRankTest_sameSuit(){
		Player p = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_COLOR.toString());
		assertEquals(pr.getRank(), 7);
	}
	
	@Test
	public void assignPlayerRankTest_pair(){
		Player p = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_PAIR.toString());
		assertEquals(pr.getRank(), 6);
	}
	
	@Test
	public void assignPlayerRankTest_highcard(){
		Player p = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		
		p.setHandCards(list1);
		sgl.assignPlayerRank(p);
		
		PlayerRank pr = p.getPlayerRank();
		System.out.print(pr.getHandRank() + "   ");
		System.out.println(pr.getRank());
		
		assertEquals(pr.getHandRank(), Gt.common.CardRank.HR_HIGH_CARD.toString());
		assertEquals(pr.getRank(), 5);
	}
	

	@Test
	public void compare2playersTest1(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
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
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest2(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list1.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.SPADES));
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest3(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.SIX,Suit.CLUBS));
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest4(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list1.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list1.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertFalse(p1.isWinner());
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothPureSEquence(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothPureSequence1(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.SPADES));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothNormalRUn(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.JACK,Suit.SPADES));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.SPADES));
		list2.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothSAmeSuit(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothPair(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list1.add(new Card(FaceValue.SIX,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothHighCard(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothHighCard1(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothHighCard2(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	

	@Test
	public void compare2playersTest_2jokers(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(427,"shaam");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.SIX,Suit.CLUBS));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.KING,Suit.CLUBS));
		list2.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothAcePair(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(427,"shaam");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
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
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		System.out.println(p1.getPlayerRank() + " " + p2.getPlayerRank());
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void A23Sequence789Sequence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void A23Sequence489Sequence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void Sequence423Sequence489(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		Ak47GameLogic sgl = new Ak47GameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void SequenceA23Sequence234JokerK(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.KING,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.THREE,Suit.HEARTS));
		list2.add(new Card(FaceValue.FOUR,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void SequenceA23SequenceAKQJokerK(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.KING,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
	}
	
	@Test
	public void SequenceA23SequenceAKQJoker5(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
	}
	
	@Test
	public void SequenceA23SequenceJKQJoker5(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void SequenceA23SequenceJKQJokerJ(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
	}
	
	@Test
	public void SequenceA23Sequence789JokerJ(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.JACK,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void SequenceA23Sequence789Joker7(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.SEVEN,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void SequenceA23PureSequence789Joker7(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.SEVEN,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.TWO,Suit.CLUBS));
		list1.add(new Card(FaceValue.THREE,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.SPADES));
		list2.add(new Card(FaceValue.NINE,Suit.SPADES));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
	}
	
	@Test
	public void CheckTrioInJokerGame(){
		/*2017-11-10 09:30:51 PLAYER CARDS | PLAYER: preetam CARDS: [C3, H3, H8] 
				2017-11-10 09:30:51 PLAYER CARDS | PLAYER: kaku CARDS: [H4, S11, C11] 
				2017-11-10 09:30:51 PLAYER CARDS | PLAYER: arun CARDS: [C4, D1, S1] 
				2017-11-10 09:30:51 PLAYER CARDS | PLAYER: sheila CARDS: [S2, S4, C12] 
				2017-11-10 09:30:51 PLAYER CARDS | PLAYER: karenjit CARDS: [H13, S3, D12] */
		
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		
		List<Card> jokers = new ArrayList<Card>();
		jokers.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		
		GameLogicObject game = new GameLogicObject(jokers);
		BaseGameLogic joker = game.getGameLogicObj("joker");
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list1.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list1.add(new Card(FaceValue.JACK,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list2.add(new Card(FaceValue.ACE,Suit.SPADES));
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		joker.assignPlayerRank(p1);
		joker.assignPlayerRank(p2);
		System.out.println("p1 - " + p1.getPlayerRank().getHandRank() );
		System.out.println("p2 - " + p2.getPlayerRank().getHandRank() );
		joker.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
		
	}
	
	
	@Test
	public void compare2playersTest_bothPairSameSuit(){
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list1.add(new Card(FaceValue.KING,Suit.HEARTS));
		list1.add(new Card(FaceValue.ACE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.SIX,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p1.isWinner());
	}
	
	@Test
	public void compare2PlayersTestBothHavingColorWithJoker() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.EIGHT,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.HEARTS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p1.isWinner());
	}
	
	
	@Test
	public void checkPureSeq() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		list2.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list2.add(new Card(FaceValue.SEVEN,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		System.out.println(p1.getPlayerRank().getHandRank());
		System.out.println(p2.getPlayerRank().getHandRank());
		assertTrue(p1.isWinner());
		
	}
	
	@Test
	public void comparePairs() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.KING,Suit.HEARTS));
		list1.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list2.add(new Card(FaceValue.THREE,Suit.SPADES));
		list2.add(new Card(FaceValue.QUEEN,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p1.isWinner());
		System.out.println(p1.getPlayerRank().getHandRank());
	}
	
	
	@Test
	public void compare2PureSeqA23() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.HEARTS));
		list1.add(new Card(FaceValue.TWO,Suit.HEARTS));
		list1.add(new Card(FaceValue.THREE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list2.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list2.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		System.out.println(p1.getPlayerRank().getHandRank());
		System.out.println(p2.getPlayerRank().getHandRank());
		assertTrue(p1.isWinner());
		
	}
	
	@Test
	public void compare2PureSeqAKQ() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.HEARTS));
		list1.add(new Card(FaceValue.KING,Suit.HEARTS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		list2.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list2.add(new Card(FaceValue.TWO,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p1.isWinner());
		System.out.println(p1.getPlayerRank().getHandRank());
	}
	
	@Test
	public void comparePuraAndHighCard() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.NINE,Suit.DIAMONDS));
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.HEARTS));
		list1.add(new Card(FaceValue.KING,Suit.HEARTS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		list2.add(new Card(FaceValue.THREE,Suit.CLUBS));
		list2.add(new Card(FaceValue.TWO,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p1.isWinner());
		System.out.println(p1.getPlayerRank().getHandRank());
	}
	
	
	
	@Test
	public void compareFlushWuthNormalRun() {
		Player p1 = new Player(426,"ram");
		Player p2 = new Player(426,"ram");
		List<Card> jokers =new  ArrayList<Card>();
		jokers.add(new Card(FaceValue.TEN,Suit.CLUBS));

		Ak47GameLogic sgl = new Ak47GameLogic(jokers);
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TWO,Suit.HEARTS));
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		list2.add(new Card(FaceValue.SIX,Suit.CLUBS));
		list2.add(new Card(FaceValue.JACK,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		assertTrue(p2.isWinner());
		System.out.println(p1.getPlayerRank().getHandRank());
	}
	
	
	
}
