package ca.mcgill.mail.stauch.justin.ninefivetwo.cards;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Deck {
    private final Stack<Cards> cards;
	
	public Deck() {
		cards = new Stack<Cards>();
	    for (Cards card : Cards.values()) {
	    	if (card != Cards.EMPTY) {
	    		cards.push(card);
	    	}
	    }
	}
	
	public void reset() {
		cards.clear();
		for (Cards card : Cards.values()) {
	    	if (card != Cards.EMPTY) {
	    		cards.push(card);
	    	}
	    }
	}
	
	public void shuffle() {
		Random random = new Random();
		List<Queue<Cards>> newDecks = new ArrayList<Queue<Cards>>();
		int length = random.nextInt(26) + 1;
		for (int i = 0; i < length; i++) {
			newDecks.add(new LinkedList<Cards>());
		}
		
		while (!cards.isEmpty()) {
			newDecks.get(random.nextInt(length)).add(cards.pop());
		}
		
		for (int x = 0; x < newDecks.size(); x++) {
			if (newDecks.get(x).isEmpty()) {
				newDecks.remove(x);
				x--;
			}
		}
		
		while (!newDecks.isEmpty()) {
			Queue<Cards> current = newDecks.get(random.nextInt(newDecks.size()));
			cards.push(current.poll());
			if (current.isEmpty()) {
				newDecks.remove(current);
			}
		}
	}
	
	public void shuffle(int times) {
		for (int x = 0; x < times; x++) {
			shuffle();
		}
	}
	
	public List<Cards>[] deal() {
		List<Cards>[] hands = new List[4];
		for (int x = 0; x < hands.length; x++) {
			hands[x] = new ArrayList<Cards>();
		}
		while (cards.size() > 4) {
			hands[0].add(cards.pop());
			hands[1].add(cards.pop());
			hands[2].add(cards.pop());
		}
		
		hands[3].addAll(cards);
		
		for (int x = 0; x < hands.length; x++) {
			hands[x] = sort(hands[x]);
		}
		
		return hands;
	}
	
	public List<Cards>[] shuffleAndDeal() {
		return shuffleAndDeal(1);
	}
	
	public List<Cards>[] shuffleAndDeal(int times) {
		shuffle(times);
		return deal();
	}
	
	public static List<Cards> sort(List<Cards> set) {
		int j;
	    boolean flag = true;
	    Cards temp;
	    while ( flag ) {
	        flag= false;
	        for(j = 0;  j < set.size() -1;  j++ ) {
	            if (set.get(j).ordinal() > set.get(j + 1).ordinal()) {
	                temp = set.get(j);
	                set.set(j, set.get(j + 1));
	                set.set(j + 1, temp);
	                flag = true; 
	            } 
	        } 
	    } 
	    return set;
	}
}
