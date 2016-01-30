package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Deck;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.main.NineFiveTwo;

public abstract class Player {
    protected List<Cards> hand;
    protected Cards[] kitty;
    protected Cards lead;
    protected int contract;
    protected int score = 0;
    protected int tricks = 0;
    protected boolean hasKitty, hasChosenTrump;
    private String name;
    
    public Player(String name) {
    	this.name = name;
    }
    
    public abstract Set<Cards> passCards(Player player, int number);
    
    public Set<Cards> receiveCards(Set<Cards> cards) {
    	Set<Cards> toReturn = new HashSet<Cards>();
    	
    	Iterator<Cards> cardIter = cards.iterator();
    	while (cardIter.hasNext()) {
    		Cards highestFound = cardIter.next();
    		hand.add(highestFound);
    		Iterator<Cards> handIter = hand.iterator();
    		
    		Cards cur;
    		while (handIter.hasNext()) {
    			cur = handIter.next();
    			if (cur.getSuit() == highestFound.getSuit()) {
    				if (cur.compareTo(highestFound) > 1) {
    					highestFound = cur;
    				}
    			}
    		}
    		toReturn.add(highestFound);
    		hand.remove(highestFound);
    	}
    	
    	Deck.sort(hand);
    	
    	return toReturn;
    }
    
    public void getCardsBack(Set<Cards> cards) {
    	hand.addAll(cards);
    	Deck.sort(hand);
    }
    
	public Cards playCard() {
		return playCard(Cards.EMPTY);
	}
	
	public Cards playCard(Cards lead) {
		this.lead = lead;
		return null;
	}
	
	public Cards playCard(Cards lead, Cards followed) {
		return playCard(lead);
	}
	
	public void setHand(List<Cards> hand) {
		this.hand = new ArrayList<Cards>(hand);
		Deck.sort(this.hand);
	}
	
	public List<Cards> getHand() {
		return new ArrayList<Cards>(hand);
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public void adjustScore(int adjustment) {
		score += adjustment;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setTricks(int tricks) {
		this.tricks = tricks;
	}
	
	public void wonATrick() {
		tricks++;
	}
	
	public int getTricks() {
		return tricks;
	}
	
	public void setContract(int contract) {
		this.contract = contract;
	}
	
	public int getContract() {
		return contract;
	}
	
	public void takeKitty(Cards[] kitty) {
		NineFiveTwo.displayMessage("Pick the cards from the kitty that you want, and discard");
		this.kitty = kitty;
		hasKitty = true;
	}
	
	public Cards[] getKitty() {
		return kitty;
	}
	
	public Suit chooseTrump() {
		NineFiveTwo.displayMessage("Choose trump");
		hasChosenTrump = false;
		Random random = new Random();
		return Suit.values()[random.nextInt(4)];
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param suit the suit to check. 'c' for clubs, 'd' for diamonds, 's' for spades, and 'h' for hearts.
	 * @return
	 */
	public boolean hasSuit(Suit suit) {
		if (suit == Suit.NULL) {
			return false;
		}
		for (Cards card : hand) {
			if (card.getSuit() == suit) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean followsSuit(Cards card) {
		if (lead == Cards.EMPTY) {
			return true;
		}
		if (!hasSuit(lead.getSuit())) {
			return true;
		}
		if (card.getSuit() == lead.getSuit()) {
			return true;
		}
		return false;
	}
	
	protected boolean handContainsCard(Cards card) {
		for (Cards cards : hand) {
			if (cards == card) {
				return true;
			}
		}
		return false;
	}
	
	protected void removeCard(Cards card) {
		hand.remove(card);
	}
}