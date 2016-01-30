package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import java.util.HashMap;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;

public enum Opponent {
	
	LEFT, RIGHT, ME;
	
	public Opponent next() {
		switch (this) {
		    case LEFT:  return RIGHT;
		    case RIGHT: return ME;
		    default:    return RIGHT;
		}
	}
	
    private final HashMap<Suit, Boolean> voids;
	
	private Opponent() {
		voids = new HashMap<Suit, Boolean>();
		voids.put(Suit.CLUBS, false);
		voids.put(Suit.DIAMONDS, false);
		voids.put(Suit.SPADES, false);
		voids.put(Suit.HEARTS, false);
	}
	
	public void voidedSuit(Suit suit) {
		voids.put(suit, true);
	}
	
	public boolean isVoid(Suit suit) {
		return voids.get(suit);
	}
}