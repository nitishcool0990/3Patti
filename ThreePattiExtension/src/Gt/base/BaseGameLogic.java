package Gt.base;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Gt.common.Card;
import Gt.common.Card.FaceValue;
import Gt.common.Card.Suit;
import Gt.common.Player;

public abstract class BaseGameLogic {

	public abstract void assignPlayerRank(Player p);

	public abstract void compare2Players(Player p1, Player p2);

	public abstract int getTrioCard(List<Card> handCards);

	private Logger log = LoggerFactory.getLogger("Gt.controller.GameController");

	public boolean checkPair(List<Card> list) {

		if (list.isEmpty() | list.size() > 3) {
			return false;
		}
		FaceValue ra = list.get(0).getFaceValue();
		FaceValue rb = list.get(1).getFaceValue();
		FaceValue rc = list.get(2).getFaceValue();

		int fa = ra.value;
		int fb = rb.value;
		int fc = rc.value;

		if (fa == fb || fa == fc || fb == fc) {
			return true;
		} else {
			return false;
		}

	}

	public boolean checkPair(List<Card> list, List<Card> jokers) {
		try {
			if (list == null | list.size() > 3) {
				return false;
			}
			Collections.sort(list);
			Card tempA = null;
			for (Card c : list) {
				if (isJoker(c, jokers)) {
					return true;
				} else {
					if (tempA == null) {
						tempA = c;
						continue;
					}
					if (c.getFaceValue().value == tempA.getFaceValue().value) {
						return true;
					} else {
						tempA = c;
					}

				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error inside check pair ", e);
			throw e;

		}
	}

	public boolean checkSameSuit(List<Card> list, Suit s) {
		if (list == null | list.size() > 3) {
			return false;
		}
		for (Card c : list) {
			if (!c.getSuit().equals(s)) {
				return false;
			}
		}
		return true;
	}

	public boolean checkSameSuit(List<Card> list, List<Card> jokers) {

		try {
			if (list == null | list.size() > 3) {
				return false;
			}
			Card temp = list.get(0);
			for (Card c : list) {
				if (isJoker(c, jokers)) {
					continue;
				} else if (isJoker(temp, jokers) & !isJoker(c, jokers)) {
					temp = c;
					continue;
				} else if (!isJoker(temp, jokers) & !c.getSuit().equals(temp.getSuit())) {
					return false;
				} else if (!isJoker(temp, jokers) & c.getSuit().equals(temp.getSuit())) {
					continue;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error inside check same suit jokers ", e);
			throw e;
		}
	}

	public boolean checkNormalRun(List<Card> list) {
		if (list == null || list.size() > 3) {
			return false;
		}

		Collections.sort(list);

		FaceValue face_value_cardA = list.get(0).getFaceValue();
		FaceValue face_value_cardB = list.get(1).getFaceValue();
		FaceValue face_value_cardC = list.get(2).getFaceValue();

		int value_a = face_value_cardA.value;
		int value_b = face_value_cardB.value;
		int value_c = face_value_cardC.value;

		if ((value_a == 2) && (value_b == 13) && (value_c == 14)) {
			return false;
		}

		if (Math.abs(value_a - value_b) * Math.abs(value_a - value_c) * Math.abs(value_b - value_c) == 2
				|| Math.abs(value_a - value_b) * Math.abs(value_a - value_c) * Math.abs(value_b - value_c) == 132) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkNormalRun(List<Card> list, List<Card> jokers) {
		try {
			if (list == null | list.size() > 3) {
				return false;
			}
			if (checkNormalRun(list)) {
				return true;
			} else {
				
				if(jokers.size() ==1) {
					Card tempA = null;
					Card tempB = null;
					Card joker = null;
					for (Card c : list) {
						if (isJoker(c, jokers)) {
							joker = c;
						}else {
							if(tempA == null) {
								tempA =c;
							}else {
								tempB = c;
							}
							
						}
					}
					if(tempA.getFaceValue().getValue() == Card.FaceValue.TWO.value && tempB.getFaceValue().getValue() == Card.FaceValue.KING.value) {
						return false;
					}
				}
				
				Card tempA = null;
				Card tempB = null;
				for (Card c : list) {
					if (isJoker(c, jokers)) {
						continue;
					} else {
						if (tempA == null) {
							tempA = c;
							continue;
						}
						else if (tempB == null && (Math.abs(c.getFaceValue().value - tempA.getFaceValue().value) == 2
								|| Math.abs(c.getFaceValue().value - tempA.getFaceValue().value) == 1)) {
							tempB = c;
							continue;
						} else if (tempB == null && (Math.abs(c.getFaceValue().value - tempA.getFaceValue().value) == 11
								|| (c.getFaceValue().value == 14 && tempA.getFaceValue().value == 3))) {

							tempB = c;
							continue;

						}else if (tempB == null && (Math.abs(c.getFaceValue().value - tempA.getFaceValue().value) == 12
								|| (c.getFaceValue().value == 14 && tempA.getFaceValue().value == 2))) {

							tempB = c;
							continue;

						} 
						else {

							return false;

						}

					}

				}

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("inside check normal run jokers ", e);
			throw e;
		}
	}

	public int highestCard(List<Card> list) {

		FaceValue ra = list.get(0).getFaceValue();
		FaceValue rb = list.get(1).getFaceValue();
		FaceValue rc = list.get(2).getFaceValue();

		int a = ra.value;
		int b = rb.value;
		int c = rc.value;
		if (a == 14 | b == 14 | c == 14) {
			return 14;
		} else {
			return ((a >= b) ? ((a >= c) ? a : c) : ((b >= c) ? b : c));
		}

	}

	public boolean checkPureSequence(List<Card> list) {
		if (list == null | list.size() > 3) {
			return false;
		}

		Suit s = list.get(0).getSuit();
		if (!checkSameSuit(list, s)) {
			return false;
		} else {
			if (checkNormalRun(list)) {
				return true;
			} else {
				return false;
			}
		}

	}

	public boolean checkPureSequence(List<Card> list, List<Card> jokers) {
		try {
			if (list == null | list.size() > 3) {
				return false;
			}
			if (!checkSameSuit(list, jokers)) {
				return false;
			} else {
				if (checkNormalRun(list, jokers)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error inside check pure sequence jokers ", e);
			throw e;
		}
	}

	// method to check if a trio exists in a list of 3 Cards
	public boolean checkTrio(List<Card> list) {
		if (list == null | list.size() > 3) {
			return false;
		}
		Card temp = list.get(0);
		for (Card c : list) {
			if (!c.getFaceValue().equals(temp.getFaceValue())) {
				return false;
			}
		}
		return true;

	}

	public boolean checkTrio(List<Card> list, List<Card> jokers) {
		try {
			if (list == null || list.size() > 3) {
				return false;
			}
			Card temp = list.get(0);
			for (Card c : list) {
				if (isJoker(c, jokers)) {
					continue;
				} else if (isJoker(temp, jokers) & !isJoker(c, jokers)) {
					temp = c;
					continue;
				} else if (!isJoker(temp, jokers) & !c.getFaceValue().equals(temp.getFaceValue())) {
					return false;
				} else if (!isJoker(temp, jokers) & c.getFaceValue().equals(temp.getFaceValue())) {
					continue;
				}
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			log.error("error inside check trio jokers ", e);
			throw e;
		}

	}

	public boolean isJoker(Card card, List<Card> jokers) {

		for (Card c : jokers) {
			if (card.getFaceValue().equals(c.getFaceValue())) {
				return true;
			}
		}
		return false;
	}

	public FaceValue getPairFaceValue(List<Card> list) {
		for (int i = 0; i < 2; i++) {
			if (list.get(i).getFaceValue() == list.get(i + 1).getFaceValue()) {
				return list.get(i).getFaceValue();
			}
		}
		return null;
	}

	public FaceValue getNonPairFaceValue(List<Card> list,FaceValue pairFaceValue) {
		for (int i = 0; i <=2; i++) {
			if (list.get(i).getFaceValue() != pairFaceValue) {
				return list.get(i).getFaceValue();
			}
		}
		return null;
	}

	public int highestCardInTrio(List<Card> list, List<Card> jokers) {
		Collections.sort(list);
		for (Card c : list) {
			if (!isJoker(c, jokers) && c.getFaceValue().getValue() != Card.FaceValue.ACE.getValue()) {
				return c.getFaceValue().value;
			}
		}

		return 14;
	}

	public int highestCardInSequence(List<Card> list, List<Card> jokers) {
		Collections.sort(list);
		int faceVal1 = 0;
		int faceVal2 = 0;
		boolean joker = false;
		for (int i = 2; i >= 0; i--) {
			if (isJoker(list.get(i), jokers)) {
				joker = true;
				continue;
			} else if(faceVal1 ==0 ) {
				faceVal1 = list.get(i).getFaceValue().value;
			}else {
				faceVal2 = list.get(i).getFaceValue().value;
			}
		}
		
		if((faceVal1 == FaceValue.ACE.value && faceVal2 == FaceValue.TWO.value) || (faceVal1 == FaceValue.ACE.value && faceVal2 == FaceValue.THREE.value) || (faceVal1 == FaceValue.THREE.value && faceVal2 == FaceValue.TWO.value)) {
			return 14;
		}
		else if ((faceVal2 == 12 && joker) || (faceVal2 == 12 && list.get(2).getFaceValue().equals(FaceValue.ACE))) {
			return 15; // for AKQ
		} else {
			return faceVal2 + 2;
		}
	}
	
	/*public int highestCardInSequence(List<Card> list, List<Card> jokers) {
		Collections.sort(list);
		int faceVal = 0;
		boolean joker = false;
		for (int i = 2; i >= 0; i--) {
			if (isJoker(list.get(i), jokers)) {
				joker = true;
				continue;
			} else {
				faceVal = list.get(i).getFaceValue().value;
			}
		}
		if (faceVal == 0 || ((faceVal == 2 || faceVal == 3 )&& joker)
				|| ((faceVal == 2 || faceVal == 3 )&& list.get(2).getFaceValue().equals(FaceValue.ACE))) {
			return 14; // for A23
		} else if (((faceVal == 12 || faceVal == 13) && joker) || ((faceVal == 12 || faceVal == 13) && list.get(2).getFaceValue().equals(FaceValue.ACE))) {
			return 15; // for AKQ
		} else {
			return faceVal + 2;
		}
	}
*/
	public void compareTrio(Player p1, Player p2, List<Card> jokers) {
		try {
			List<Card> p1HandCards = p1.getHandCards();
			List<Card> p2HandCards = p2.getHandCards();
			if (highestCardInTrio(p1HandCards, jokers) > highestCardInTrio(p2HandCards, jokers)) {
				p1.setWinner(true);
				return;
			} else if (highestCardInTrio(p1HandCards, jokers) < highestCardInTrio(p2HandCards, jokers)) {
				p2.setWinner(true);
				return;
			} else {
				// split winnings
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error inside compare trio jokers", e);
			throw e;
		}

	}

	public void comparePureSequence(Player p1, Player p2, List<Card> jokers) {
		try {
			List<Card> p1HandCards = p1.getHandCards();
			List<Card> p2HandCards = p2.getHandCards();

			Collections.sort(p1HandCards);
			Collections.sort(p2HandCards);

			if (highestCardInSequence(p1HandCards, jokers) > highestCardInSequence(p2HandCards, jokers)) {
				p1.setWinner(true);
			} else if (highestCardInSequence(p1HandCards, jokers) < highestCardInSequence(p2HandCards, jokers)) {
				p2.setWinner(true);
			} else {
				// split winnings
			}

		} catch (Exception e) {
			log.error("error inside compare pure sequence jokers", e);
			throw e;

		}

	}

	public void compareNormalRun(Player p1, Player p2, List<Card> jokers) {
		try {
			List<Card> p1HandCards = p1.getHandCards();
			List<Card> p2HandCards = p2.getHandCards();

			if (highestCardInSequence(p1HandCards, jokers) > highestCardInSequence(p2HandCards, jokers)) {
				p1.setWinner(true);
				return;
			} else if (highestCardInSequence(p1HandCards, jokers) < highestCardInSequence(p2HandCards, jokers)) {
				p2.setWinner(true);
				return;
			} else {
				// split winnings
			}
		} catch (Exception e) {
			log.error("error inside compare pure sequence jokers", e);
			throw e;

		}
	}

	public void compareSameSuit(Player p1, Player p2, List<Card> jokers) {
		try {
			List<Card> p1HandCards = p1.getHandCards();
			List<Card> p2HandCards = p2.getHandCards();

			Collections.sort(p1HandCards);
			Collections.sort(p2HandCards);

			Card p1max1 = null, p1max2 = null, p1max3 = null;
			Card p2max1 = null, p2max2 = null, p2max3 = null;

			Iterator<Card> iter = p1HandCards.iterator();
			while (iter.hasNext()) {
				Card temp = iter.next();
				if (isJoker(temp, jokers)) {
					p1max1 = temp;
					// iter.remove();
					break;
				}
			}

			/*
			 * for(Card c:p1HandCards){ if(isJoker(c,jokers)){ p1max1 = c;
			 * p1HandCards.remove(c); } }
			 */

			if (p1max1 == null) {
				p1max1 = p1HandCards.get(2);
				p1max2 = p1HandCards.get(1);
				p1max3 = p1HandCards.get(0);
			} else {
				for (Card c : p1HandCards) {
					if (isJoker(c, jokers)) {
						continue;
					} else if (p1max3 == null) {
						p1max3 = c;
						continue;
					} else if (p1max2 == null) {
						p1max2 = c;
					} else {
						p1max1 = c;
					}

				}
			}

			Iterator<Card> iter2 = p2HandCards.iterator();
			while (iter2.hasNext()) {
				Card temp = iter2.next();
				if (isJoker(temp, jokers)) {
					p2max1 = temp;
					// iter2.remove();
					break;
				}
			}

			/*
			 * for(Card c:p2HandCards){ if(isJoker(c,jokers)){ p2max1 = c;
			 * p2HandCards.remove(c); } }
			 */

			if (p2max1 == null) {
				p2max1 = p2HandCards.get(2);
				p2max2 = p2HandCards.get(1);
				p2max3 = p2HandCards.get(0);
			} else {
				for (Card c : p2HandCards) {
					if (isJoker(c, jokers)) {
						continue;
					}
					if (p2max3 == null) {
						p2max3 = c;
						continue;
					} else if (p2max2 == null) {
						p2max2 = c;
					} else {
						p2max1 = c;
					}

				}
			}

			int p1Card1 = p1max1.getFaceValue().value;
			int p1Card2 = p1max2.getFaceValue().value;
			int p1Card3 = p1max3.getFaceValue().value;

			int p2Card1 = p2max1.getFaceValue().value;
			int p2Card2 = p2max2.getFaceValue().value;
			int p2Card3 = p2max3.getFaceValue().value;

			if (isJoker(p1max1, jokers) & isJoker(p2max1, jokers)) {
				if (p1Card2 > p2Card2) {
					p1.setWinner(true);
					return;
				} else if (p2Card2 > p1Card2) {
					p2.setWinner(true);
					return;
				} else {
					if (p1Card3 > p2Card3) {
						p1.setWinner(true);
						return;
					} else if (p2Card3 > p1Card3) {
						p2.setWinner(true);
						return;
					} else {
						// split winnings
					}
				}

			} else if (!isJoker(p1max1, jokers) & !isJoker(p2max1, jokers)) {
				cardComparisonForSameSuit(p1, p2, p1Card1, p1Card2, p1Card3, p2Card1, p2Card2, p2Card3);

			} else if (isJoker(p1max1, jokers) & !isJoker(p2max1, jokers)) {
				cardComparisonForSameSuit(p1, p2, FaceValue.ACE.value, p1Card2, p1Card3, p2Card1, p2Card2, p2Card3);
			} else {
				cardComparisonForSameSuit(p1, p2, p1Card1, p1Card2, p1Card3, FaceValue.ACE.value, p2Card2, p2Card3);
			}
		} catch (Exception e) {
			log.error("error inside compare same suit jokers", e);
			// throw e;
		}

	}

	private void cardComparisonForSameSuit(Player p1, Player p2, int p1Card1, int p1Card2, int p1Card3, int p2Card1,
			int p2Card2, int p2Card3) {
		if (p1Card1 > p2Card1) {
			p1.setWinner(true);
			return;
		} else if (p1Card1 < p2Card1) {
			p2.setWinner(true);
			return;
		} else {
			if (p1Card2 > p2Card2) {
				p1.setWinner(true);
				return;
			} else if (p2Card2 > p1Card2) {
				p2.setWinner(true);
				return;
			} else {
				if (p1Card3 > p2Card3) {
					p1.setWinner(true);
					return;
				} else if (p2Card3 > p1Card3) {
					p2.setWinner(true);
					return;
				} else {
					// split winnings
				}
			}
		}

	}

	public void comparePair(Player p1, Player p2, List<Card> jokers) {
		List<Card> p1HandCards = p1.getHandCards();
		List<Card> p2HandCards = p2.getHandCards();
		Collections.sort(p1HandCards);
		Collections.sort(p2HandCards);

		System.out.println(p1HandCards.toString());

		Card pairP1 = null;
		Card nonPairP1 = null;

		/*
		 * for(int i=0;i<3;i++){ System.out.println(p1HandCards.get(i).toString()); if(i
		 * == p1HandCards.size()-1){ pairP1 = p1HandCards.get(p1HandCards.size() - 2);
		 * 
		 * nonPairP1 = p1HandCards.get(0);
		 * 
		 * System.out.println("pair Card " + pairP1.toString());
		 * 
		 * System.out.println("non pair Card " + nonPairP1.toString());
		 * 
		 * //p1HandCards.remove(p1HandCards.size() - 1);
		 * //p1HandCards.remove(p1HandCards.size() - 1); //break; } else
		 * if(p1HandCards.get(i).compareTo(p1HandCards.get(i+1)) == 0){ pairP1 =
		 * p1HandCards.get(i); System.out.println("pair Card " + pairP1.toString());
		 * 
		 * nonPairP1 = p1HandCards.get(p1HandCards.size() - 1);
		 * 
		 * //p1HandCards.remove(i); //p1HandCards.remove(i);
		 * 
		 * //break; } else if(isJoker(p1HandCards.get(i),jokers)){
		 * p1HandCards.remove(i); pairP1 = p1HandCards.get(p1HandCards.size() - 1);
		 * System.out.println("pair Card by joker" + pairP1.toString());
		 * p1HandCards.remove(p1HandCards.size() - 1); //break; } }
		 */

		for (int i = 0; i < 3; i++) {
			System.out.println(p1HandCards.get(i).toString());
			if (isJoker(p1HandCards.get(i), jokers)) {

				if (i == 1) {
					nonPairP1 = p1HandCards.get(0);
					pairP1 = p1HandCards.get(2);
				} else if (i == 2) {
					nonPairP1 = p1HandCards.get(0);
					pairP1 = p1HandCards.get(1);
				} else {
					nonPairP1 = p1HandCards.get(1);
					pairP1 = p1HandCards.get(2);
				}

				System.out.println("pair Card by joker" + pairP1.toString());
				break;

			} else if (p1HandCards.get(i).compareTo(p1HandCards.get(i + 1)) == 0) {

				pairP1 = p1HandCards.get(i);
				nonPairP1 = p1HandCards.get(1);

				if (i == 0) {
					nonPairP1 = p1HandCards.get(2);
					pairP1 = p1HandCards.get(1);
				} else if (i == 1) {
					nonPairP1 = p1HandCards.get(0);
					pairP1 = p1HandCards.get(1);
				}

				System.out.println("pair Card " + pairP1.toString());

				break;

			}
		}

		System.out.println("non pair Card " + nonPairP1.toString());

		Card pairP2 = null;
		Card nonPairP2 = null;

		/*
		 * for(int i=0;i<3;i++){ if(i == p2HandCards.size()-1){ pairP2 =
		 * p2HandCards.get(p2HandCards.size() - 2);
		 * p2HandCards.remove(p2HandCards.size() - 1);
		 * p2HandCards.remove(p2HandCards.size() - 1); break; }
		 * if(p2HandCards.get(i).compareTo(p2HandCards.get(i+1)) == 0){ pairP2 =
		 * p2HandCards.get(i); p2HandCards.remove(i); p2HandCards.remove(i); break; }
		 * else if(isJoker(p2HandCards.get(i),jokers)){ p2HandCards.remove(i); pairP2 =
		 * p2HandCards.get(p2HandCards.size() - 1);
		 * p2HandCards.remove(p2HandCards.size() - 1); break; }else{ continue; } }
		 * nonPairP2 = p2HandCards.get(p2HandCards.size() - 1);
		 */

		for (int i = 0; i < 3; i++) {
			System.out.println(p2HandCards.get(i).toString());
			if (isJoker(p2HandCards.get(i), jokers)) {

				if (i == 1) {
					nonPairP2 = p2HandCards.get(0);
					pairP2 = p2HandCards.get(2);
				} else if (i == 2) {
					nonPairP2 = p2HandCards.get(0);
					pairP2 = p2HandCards.get(1);
				} else {
					nonPairP2 = p2HandCards.get(1);
					pairP2 = p2HandCards.get(2);
				}

				System.out.println("pair Card by joker" + pairP1.toString());
				break;

			} else if (p2HandCards.get(i).compareTo(p2HandCards.get(i + 1)) == 0) {

				pairP2 = p2HandCards.get(i);
				nonPairP2 = p2HandCards.get(1);

				if (i == 0) {
					nonPairP2 = p2HandCards.get(2);
					pairP2 = p2HandCards.get(1);
				} else if (i == 1) {
					nonPairP2 = p2HandCards.get(0);
					pairP2 = p2HandCards.get(1);
				}

				System.out.println("pair Card " + pairP2.toString());
				break;
			}
		}

		System.out.println("non pair Card " + nonPairP2.toString());

		if (pairP1.compareTo(pairP2) > 0) {
			p1.setWinner(true);

		} else if (pairP1.compareTo(pairP2) < 0) {
			p2.setWinner(true);
		} else {

			if (nonPairP1.compareTo(nonPairP2) > 0) {
				p1.setWinner(true);
			} else if (nonPairP1.compareTo(nonPairP2) < 0) {
				p2.setWinner(true);
			} else {
				// split winnings
			}

		}

		return;

	}

}
