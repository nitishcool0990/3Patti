package Gt.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
	//List<Card> deck = Card.newDeck();
	
	//Shuffle a deck of cards.
	public List<Card> shuffle(List<Card> d){
		int n = d.size();
		List<Card> shuffled = new ArrayList<Card>(); 
		Random random = new Random();
		for(int i=n-1;i>0;i--){
			int j = random.nextInt(i);
			Card temp = d.get(i);
			d.set(i,d.get(j));
			d.set(j,temp);
		}
		
		return d;
		
	}
}
