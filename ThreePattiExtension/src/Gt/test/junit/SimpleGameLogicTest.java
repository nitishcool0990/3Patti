package Gt.test.junit;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Gt.common.CardRank;
import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;
import Gt.common.Player;
import Gt.common.PlayerRank;
import Gt.room.Logic.SimpleGameLogic;
public class SimpleGameLogicTest {
	
	@Test
	public void assignPlayerRankTest_trio(){
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
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
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.JACK,Suit.SPADES));
		list1.add(new Card(FaceValue.KING,Suit.SPADES));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
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
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
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
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
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
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		
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
		Player p = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();

		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest2(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.CLUBS));
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothPureSEquence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.CLUBS));
		list1.add(new Card(FaceValue.KING,Suit.CLUBS));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothPureSequence1(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.JACK,Suit.CLUBS));
		list1.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
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
	public void compare2playersTest_bothNormalRUn(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.TEN,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
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
	public void compare2playersTest_bothHighCard(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.TEN,Suit.CLUBS));
		list1.add(new Card(FaceValue.NINE,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	@Test
	public void compare2playersTest_bothHighCard1(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
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
	public void TwoCardSameTest(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"ooo");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.QUEEN,Suit.SPADES));
		list1.add(new Card(FaceValue.FIVE,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.JACK,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.SIX,Suit.HEARTS));
		list2.add(new Card(FaceValue.JACK,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		
		
		assertTrue(p2.isWinner());
		
		
	}
	
	
	@Test
	public void A23Sequence789Sequence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SEVEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.NINE,Suit.CLUBS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
		
		
	}
	
	@Test
	public void A23SequenceAKQSequence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.THREE,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
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
	public void A2KSequenceAKQSequence(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.ACE,Suit.SPADES));
		list1.add(new Card(FaceValue.TWO,Suit.DIAMONDS));
		list1.add(new Card(FaceValue.KING,Suit.DIAMONDS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.ACE,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.TEN,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.DIAMONDS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
		
	}
	
	
	@Test
	public void compareSeqAndPair(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.FIVE,Suit.HEARTS));
		list1.add(new Card(FaceValue.SIX,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.QUEEN,Suit.CLUBS));
		list2.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
		
	}
	
	@Test
	public void comparePairsWithSameFaceValue(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.SPADES));
		list1.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list1.add(new Card(FaceValue.SIX,Suit.HEARTS));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.FOUR,Suit.CLUBS));
		list2.add(new Card(FaceValue.FOUR,Suit.DIAMONDS));
		list2.add(new Card(FaceValue.QUEEN,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
		
	}

	
	@Test
	public void comparePairs(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.FOUR,Suit.HEARTS));
		list1.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list1.add(new Card(FaceValue.TEN,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.EIGHT,Suit.CLUBS));
		list2.add(new Card(FaceValue.EIGHT,Suit.HEARTS));
		list2.add(new Card(FaceValue.ACE,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p1.isWinner());
		
	}
	
	
	@Test
	public void compareHighCard(){
		Player p1 = new Player(444,"ooo");
		Player p2 = new Player(444,"bbb");
		SimpleGameLogic sgl = new SimpleGameLogic();
		
		List<Card> list1 = new ArrayList<Card>();
		list1.add(new Card(FaceValue.JACK,Suit.HEARTS));
		list1.add(new Card(FaceValue.FIVE,Suit.CLUBS));
		list1.add(new Card(FaceValue.TWO,Suit.SPADES));
		
		List<Card> list2 = new ArrayList<Card>();
		list2.add(new Card(FaceValue.SIX,Suit.CLUBS));
		list2.add(new Card(FaceValue.TEN,Suit.HEARTS));
		list2.add(new Card(FaceValue.QUEEN,Suit.HEARTS));
		
		p1.setHandCards(list1);
		p2.setHandCards(list2);
		
		sgl.assignPlayerRank(p1);
		sgl.assignPlayerRank(p2);
		sgl.compare2Players(p1, p2);
		
		assertTrue(p2.isWinner());
		
	}
	
}
