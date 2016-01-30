package ca.mcgill.mail.stauch.justin.ninefivetwo.players.tracking;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;

public class Block {
    private Cards head;
    private Cards tail;
    private final Discard discard;
    private int size;
	
    public Block(Cards from, Cards to, int size, Discard discard) {
    	assert from.getSuit() == to.getSuit();
    	assert from.ordinal() < to.ordinal();
    	
    	head = from;
    	tail = to;
    	this.size = size;
    	this.discard = discard;
    }
    
    public Suit getSuit() {
    	return head.getSuit();
    }
    
    public Cards getHead() {
    	return head;
    }
    
    public Cards getTail() {
    	return tail;
    }
    
    public void removeCard(Cards card) {
    	if (card.getSuit() != head.getSuit()) {
    		return;
    	}
    	
    	if (head == tail && head == card) {
    		size = 0;
    		head = null;
    		tail = null;
    	}
    	
    	if (head == card) {
    		head = discard.nextCard(head);
    		size--;
    	}
    	else if (tail == card) {
    		tail = discard.previousCard(card);
    		size--;
    	}
    }
    
    public Block merge(Block block) {
    	if (getSuit() != block.getSuit()) {
    		return null;
    	}
    	
    	if (discard.nextCard(tail) == block.head) {
    		return new Block(head, block.tail, size + block.size, discard);
    	}
    	if (discard.previousCard(head) == block.tail) {
    		return new Block(block.head, tail, size + block.size, discard);
    	}
    	
    	return null;
    }
    
    /**
     * Compares this Block to the given Block based on how they are sorted in a hand.
     * 
     * @param block The block to compare to.
     * @return If this block should be placed to the left of the given block.
     */
    public boolean comesBefore(Block block) {
    	return tail.ordinal() < block.head.ordinal();
    }
    
    public boolean beats(Block block) {
    	if (getSuit() == block.getSuit()) {
    		return getHead().compareTo(block.getTail()) > 0;
    	}
    	
    	return true;
    }
    
    public boolean contains(Cards card) {
    	return head.ordinal() <= card.ordinal() && card.ordinal() <= tail.ordinal();
    }
    
    public boolean isEmpty() {
    	return size == 0;
    }
    
    public int size() {
    	return discard.cardsBetween(head, tail) + 1;
    }
    
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof Block)) {
    		return false;
    	}
    	
    	Block block = (Block) o;
    	
    	return block.head == head && block.tail == tail;
    }
}