package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;

public class PlayerProfile {
    private boolean[] voids;//(clubs, 0), (diamonds, 1), (spades, 2), (hearts, 3)
    private int[] plays;//Same key
    private int[] totalPlays;//Same key, counts every card played.
    private boolean[][] played;//Same key, tracks all the played cards, and the cards in the player's hand.
    private boolean kittyKnown;
    
    public void startNewRound() {
    	voids = new boolean[]{false, false, false, false};
    	plays = new int[]{0, 0, 0, 0};
    	totalPlays = new int[]{0, 0, 0, 0};
    	played = new boolean[4][13];
    	for (int x = 0; x < played.length; x++) {
    		for (int y = 0; y < played[x].length; y++) {
    			played[x][y] = false;
    		}
    	}
    }
        
    private int roundsLeft() {
    	int cardsPlayed = 0;
    	for (int x : totalPlays) {
    		cardsPlayed += x;
    	}
    	return (kittyKnown ? 52 : 48 - cardsPlayed) / 16;
    }
    
    /**
     * 
     * @return The number of cards that are unknown.
     */
    private int getCardsLeft() {
    	int cardsPlayed = 0;
    	for (int x : totalPlays) {
    		cardsPlayed += x;
    	}
    	return 52 - cardsPlayed - roundsLeft();
    }
    
    private int arrayIndex(char suit) {
    	switch (suit) {
    	    case 'c':
    	    	return 0;
    	    case 'd':
    	    	return 1;
    	    case 's':
    	    	return 2;
    	    case 'h':
    	    	return 3;
    	}
    	return 4;
    }
}