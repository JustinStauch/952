package ca.mcgill.mail.stauch.justin.ninefivetwo.players.tracking;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;

public class Discard {
    private List<Cards> cards;
	
    public Discard() {
    	cards = new ArrayList<Cards>();
    	
    	for (Cards card : Cards.values()) {
    		if (card != Cards.EMPTY) {
    			cards.add(card);
    		}
    	}
    }
    
    public Discard(Discard discard) {
    	cards = new ArrayList<Cards>(discard.cards);
    }
    
    public void removeCard(Cards card) {
    	cards.remove(card);
    }
    
    public void removeBlock(Block block) {
    	Cards cur = block.getHead();
    	Cards temp;
    	
    	while (cur != block.getTail()) {
    		temp = nextCard(cur);
    		cards.remove(cur);
    		cur = temp;
    	}
    	
    	cards.remove(cur);
    }
    
    public int cardsBetween(Cards card1, Cards card2) {
    	if (card1.getSuit() == card2.getSuit()) {
    		return cards.indexOf(card2) - cards.indexOf(card1) - 1;
    	}
    	
    	return -1;
    }
    
    public Cards nextCard(Cards card) {
    	Cards next = cards.get(cards.indexOf(card) + 1);
    	return next.getSuit() == card.getSuit() ? next : card;
    }
    
    public Cards previousCard(Cards card) {
    	Cards previous = cards.get(cards.indexOf(card) - 1);
    	return previous.getSuit() == card.getSuit() ? previous : card;
    }
    
    public boolean canBeMerged(Block block1, Block block2) {
    	return cardsBetween(block1.getTail(), block2.getHead()) == 0 || cardsBetween(block2.getTail(), block1.getHead()) == 0;
    }
}