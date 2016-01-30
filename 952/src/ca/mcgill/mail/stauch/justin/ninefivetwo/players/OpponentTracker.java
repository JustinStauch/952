package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;

public class OpponentTracker {
    private Opponent left, right;
    private Suit trump;
    private Cards[] hand;
    private Set<Cards> playedCards;
	
    public void startRound(Suit trump, Cards[] hand) {
    	this.trump = trump;
    	this.hand = hand;
    	playedCards = new HashSet<Cards>();
    	left = new Opponent();
    	right = new Opponent();
    }
    
    public void discardKitty(Cards...kitty) {
    	Collections.addAll(playedCards, kitty);
    }
    
    public void cardsPlayedThisHand(Cards lead, Cards leftsCard, Cards rightsCard) {
    	playedCards.add(leftsCard);
    	playedCards.add(rightsCard);
    	if (leftsCard.getSuit() != lead.getSuit()) {
    		left.voidedSuit(lead.getSuit());
    	}
    	if (rightsCard.getSuit() != lead.getSuit()) {
    		right.voidedSuit(lead.getSuit());
    	}
    	countCards();
    }
    
    public boolean shouldWin(Cards card) {
    	Suit suit = card.getSuit();
    	
    	if ((right.isVoid(suit) && !right.isVoid(trump)) || (left.isVoid(suit) && !left.isVoid(trump))) {
    		return false;
    	}
    	
    	return isHighestCardLeftOfSuit(card);
    }
    
    private boolean isHighestCardLeftOfSuit(Cards card) {
    	Suit suit = card.getSuit();
    	int value = card.getValue();
    	int higherCardsInPlay = 12 - value;
    	
    	Iterator<Cards> played = playedCards.iterator();
    	
    	while (played.hasNext() && higherCardsInPlay > 0) {
    		Cards sample = played.next();
    		if (sample.getSuit() == suit && sample.getValue() > value) {
    			higherCardsInPlay--;
    		}
    	}
    	
    	for (Cards sample : hand) {
    		if (higherCardsInPlay <= 0) {
    			break;
    		}
    		
    		if (sample.getSuit() == suit && sample.getValue() > value) {
    			higherCardsInPlay--;
    		}
    	}
    	
    	return higherCardsInPlay <= 0;
    }
    
    private void countCards() {
    	final Suit[] suits = Suit.values();
    	
    	for (Suit suit : suits) {
    		if (left.isVoid(suit) && right.isVoid(suit)) {
    			continue;
    		}
    		
    		int cardsFoundOfSuit = 0;
    		
    		for (Cards card : hand) {
    			if (card.getSuit() == suit) {
    				cardsFoundOfSuit++;
    			}
    		}
    		
    		Iterator<Cards> played = playedCards.iterator();
    		
    		while (played.hasNext()) {
    			if (played.next().getSuit() == suit) {
    				cardsFoundOfSuit++;
    			}
    		}
    		
    		if (cardsFoundOfSuit == 13) {
    			right.voidedSuit(suit);
    			left.voidedSuit(suit);
    		}
    	}
    }
}