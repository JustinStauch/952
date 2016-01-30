package ca.mcgill.mail.stauch.justin.ninefivetwo.players.tracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.players.Opponent;

public class BlockTracker {
    private List<Block> hand;
    private List<Block> unclaimed;
    private Map<Opponent, List<Block>> opponentHands;
    private Discard discard;
    
    public BlockTracker(Discard discard) {
    	hand = new ArrayList<Block>();
    	unclaimed = new ArrayList<Block>();
    	opponentHands = new HashMap<Opponent, List<Block>>();
    	opponentHands.put(Opponent.RIGHT, new ArrayList<Block>());
    	opponentHands.put(Opponent.LEFT, new ArrayList<Block>());
    	this.discard = discard;
    }
    
    public BlockTracker() {
    	this(new Discard());
    }
    
    public BlockTracker(BlockTracker tracker) {
    	hand = new ArrayList<Block>(tracker.hand);
    	unclaimed = new ArrayList<Block>(tracker.unclaimed);
    	
    	opponentHands = new HashMap<Opponent, List<Block>>();
    	opponentHands.put(Opponent.RIGHT, new ArrayList<Block>(tracker.opponentHands.get(Opponent.RIGHT)));
    	opponentHands.put(Opponent.LEFT, new ArrayList<Block>(tracker.opponentHands.get(Opponent.LEFT)));
    	
    	discard = new Discard(tracker.discard);
    }
    
    public BlockTracker(BlockTracker tracker, Cards...cards) {
    	this(tracker);
    	
    	cardsPlayed(cards);
    }
    
    public void loadHand(List<Cards> cards) {
    	for (Cards card : Cards.values()) {
    		if (card != Cards.EMPTY) {
    			if (cards.contains(card)) {
    				hand.add(new Block(card, card, 1, discard));
    			}
    			else {
    				unclaimed.add(new Block(card, card, 1, discard));
    			}
    		}
    	}
    	
    	hand = mergeBlocks(hand);
    	unclaimed = mergeBlocks(unclaimed);
    }
    
    public void cardsPlayed(Cards...cards) {
    	for (Cards card : cards) {
    		boolean found = false;
    		
    		for (int i = 0; i < hand.size(); i++) {
    			if (hand.get(i).contains(card)) {
    				hand.get(i).removeCard(card);
    				if (hand.get(i).isEmpty()) {
    					hand.remove(i);
    					found = true;
    					break;
    				}
    			}
    		}
    		
    		if (found) {
				continue;
			}
    		
    		for (int i = 0; i < unclaimed.size(); i++) {
    			if (unclaimed.get(i).contains(card)) {
    				unclaimed.get(i).removeCard(card);
    				if (unclaimed.get(i).isEmpty()) {
    					unclaimed.remove(i);
    					found = true;
    					break;
    				}
    			}
    		}
    		
    		if (found) {
				continue;
			}
    		
    		for (List<Block> blocks : opponentHands.values()) {
    			for (int i = 0; i < blocks.size(); i++) {
        			if (blocks.get(i).contains(card)) {
        				blocks.get(i).removeCard(card);
        				if (blocks.get(i).isEmpty()) {
        					blocks.remove(i);
        					found = true;
        					break;
        				}
        			}
        		}
    			
    			if (found) {
    				break;
    			}
    		}
    	}
    	
    	hand = mergeBlocks(hand);
    	unclaimed = mergeBlocks(unclaimed);
    	opponentHands.put(Opponent.LEFT, mergeBlocks(opponentHands.get(Opponent.LEFT)));
    	opponentHands.put(Opponent.RIGHT, mergeBlocks(opponentHands.get(Opponent.RIGHT)));
    }
    
    public void opponentFailedToFollowSuit(Opponent opp, Suit suit) {
    	if (opp == Opponent.ME) {
    		return;
    	}
    	
    	final List<Block> blocks = opponentHands.get(opp == Opponent.LEFT ? Opponent.RIGHT : Opponent.LEFT);
    	
    	for (int i = 0; i < unclaimed.size(); i++) {
    		if (unclaimed.get(i).getSuit() == suit) {
    			blocks.add(unclaimed.get(i));
    			unclaimed.remove(i);
    			i--;
    		}
    	}
    	
    	mergeBlocks(blocks);
    	
    	final List<Block> dropped = opponentHands.get(opp);
    	
    	for (int i = 0; i < dropped.size(); i++) {
    		if (dropped.get(i).getSuit() == suit) {
    			discard.removeBlock(dropped.get(i));
    			dropped.remove(i);
    			i--;
    		}
    	}
    	
    	mergeBlocks(dropped);
    }
    
    /**
     * Returns true if the given card is very likely to win if it is lead.
     * 
     * It first checks if the given Block is the higher than any Block in the suit held by an opponent.
     * If it is not, it will return false, as leading from a Block that is not the highest in the suit
     * will result in someone playing higher.
     * 
     * There is a possible situation where it returns false, but the higher card(s) were discarded in the kitty.
     * This situation is too unlikely to consider.
     * 
     * It then checks if it is guaranteed to be trumped,
     * i.e. an opponent is known void in the suit, and may have trump cards.
     * 
     * If none of these checks indicates a false, a true will be returned.
     * 
     * However true is is not a guaranteed win. It is possible that an opponent is void in the suit,
     * but there is not enough information to say for certain.
     * 
     * @param cardLead The card to lead in question.
     * @return If the card should win if it is lead.
     */
    public boolean willLeadWin(Block blockLead, Suit trump) {
    	int cardsBelowFound = 0;
    	final Map<Opponent, Boolean> hasTrump = new HashMap<Opponent, Boolean>();
    	
    	hasTrump.put(Opponent.LEFT, false);
    	hasTrump.put(Opponent.RIGHT, false);
    	
    	final Iterator<Block> unclaimItr = unclaimed.iterator();
    	
    	Block cur;
    	while (unclaimItr.hasNext()) {
    		cur = unclaimItr.next();
    		if (cur.getSuit() == blockLead.getSuit()) {
    			if (cur.beats(blockLead)) {
    				return false;
    			}
    			else {
    				cardsBelowFound += cur.size();
    			}
    		}
    		else if (cur.getSuit() == trump) {
    			hasTrump.put(Opponent.LEFT, true);
    	    	hasTrump.put(Opponent.RIGHT, true);
    		}
    	}
    	
        final Map<Opponent, Boolean> hasCardBelow = new HashMap<Opponent, Boolean>();
    	
    	hasCardBelow.put(Opponent.LEFT, cardsBelowFound >= 2);
    	hasCardBelow.put(Opponent.RIGHT, cardsBelowFound >= 2);
    	
    	final Iterator<Opponent> oppItr = opponentHands.keySet().iterator();
    	
    	Opponent curOpp;
    	while (oppItr.hasNext()) {
    		curOpp = oppItr.next();
    		final Iterator<Block> oppBlockItr = opponentHands.get(curOpp).iterator();
    		
    		while (oppBlockItr.hasNext()) {
    			cur = oppBlockItr.next();
    			if (cur.getSuit() == blockLead.getSuit()) {
        			if (cur.beats(blockLead)) {
        				return false;
        			}
        			else {
        				hasCardBelow.put(curOpp, true);
        			}
        		}
    			else if (cur.getSuit() == trump) {
    				hasTrump.put(curOpp, true);
    			}
    		}
    	}
    	
    	//If there is only one unclaimed card that can be beaten,
    	//then check if either of the opponents has claimed a card that can be beaten.
    	if (cardsBelowFound == 1) {
    		if (!hasCardBelow.get(Opponent.LEFT)) {
    			hasCardBelow.put(Opponent.LEFT, hasCardBelow.get(Opponent.RIGHT));
    		}
    		
    		if (!hasCardBelow.get(Opponent.RIGHT)) {
    			hasCardBelow.put(Opponent.RIGHT, hasCardBelow.get(Opponent.LEFT));
    		}
    	}
    	
    	//Return false if either opponent doesn't have a card below, but has a trump card.
    	return !((!hasCardBelow.get(Opponent.LEFT) && hasTrump.get(Opponent.LEFT)) || (!hasCardBelow.get(Opponent.RIGHT) && hasTrump.get(Opponent.RIGHT)));
    }
    
    private List<Block> mergeBlocks(List<Block> blocks) {
    	mergeSort(blocks, 0, blocks.size() - 1);
    	
    	final List<Block> newBlocks = new ArrayList<Block>();
    	
    	Block cur = blocks.get(0);
    	for (int i = 1; i < blocks.size(); i++) {
    		if (discard.canBeMerged(cur, blocks.get(i))) {
    			cur = cur.merge(blocks.get(i));
    		}
    		else {
    			newBlocks.add(cur);
    			cur = blocks.get(i);
    		}
    	}
    	newBlocks.add(cur);
    	
    	return newBlocks;
    }
    
    private void mergeSort(List<Block> blocks, int start, int end) {
    	if (start < end) {
    		int mid = (start + end) / 2;
    		mergeSort(blocks, start, mid);
    		mergeSort(blocks, mid + 1, end);
    		merge(blocks, start, mid, end);
    	}
    }
    
    private void merge(List<Block> blocks, int start, int mid, int end) {
    	final int n = mid - start + 1;
    	final int m = end - mid;
    	final Block[] left = new Block[n + 1];
    	final Block[] right = new Block[m + 1];
    	
    	for (int x = 0; x < n; x++) {
    		left[x] = blocks.get(start + x);
    	}
    	
    	for (int x = 0; x < m; x++) {
    		right[x] = blocks.get(mid + x);
    	}
    	
    	//Cards.EMPTY, has the highest ordinal(), so every card goes to the left of it.
    	left[n + 1] = new Block(Cards.EMPTY, Cards.EMPTY, 1, discard);
    	right[m + 1] = new Block(Cards.EMPTY, Cards.EMPTY, 1, discard);
    	
    	int i = 0;
    	int j = 0;
    	
    	for (int k = start; k <= end; k++) {
    		if (left[i].comesBefore(right[j])) {
    			blocks.add(k, left[i]);
    			i++;
    		}
    		else {
    			blocks.add(k, right[j]);
    			i++;
    		}
    	}
    }
}