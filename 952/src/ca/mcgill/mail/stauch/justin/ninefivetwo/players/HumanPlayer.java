package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Deck;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.main.NineFiveTwo;
import ca.mcgill.mail.stauch.justin.ninefivetwo.main.PlayingCard;

public class HumanPlayer extends Player implements ActionListener {
	private boolean playersTurn, passingCards;
	private Set<Cards> cardsToPass;
	private int numOfCardsToPass;
    private int selectedKittyIndex = -1, selectedHandIndex = -1, selectedCards;
    private Player playerToPassTo;
    
    public HumanPlayer(String name) {
		super(name);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (NineFiveTwo.isEndOfTrick()) {
			NineFiveTwo.startNextTrick();
			return;
		}
		if (arg0.getSource() instanceof JButton && !(arg0.getSource() instanceof PlayingCard)) {
			JButton button = (JButton) arg0.getSource();
			if (button.getText().equalsIgnoreCase("Done")) {
				if (hasKitty) {
					hasKitty = false;
					NineFiveTwo.doneWithKitty();
					return;
				}
			}
			if (button.getText().equalsIgnoreCase("No trump") && contract == 9 && !hasChosenTrump) {
				hasChosenTrump = true;
				NineFiveTwo.doneWithTrump(Suit.NULL);
				return;
			}
			if (button.getText().equalsIgnoreCase("Pass Cards") && passingCards) {
				for (Cards card : cardsToPass) {
					if (card == Cards.EMPTY || card == null) {
						return;
					}
				}
				passingCards = false;
				for (Cards card : cardsToPass) {
					removeCard(card);
				}
				NineFiveTwo.playerPassedCards(cardsToPass, playerToPassTo);
			}
		}
		if (!(arg0.getSource() instanceof PlayingCard)) {
			return;
		}
		PlayingCard chosen = (PlayingCard) arg0.getSource();
		if (chosen.isPartOfKitty()) {
			if (contract == 9 && !hasChosenTrump) {
			    hasChosenTrump = true;
		    	NineFiveTwo.doneWithTrump(chosen.getCard().getSuit());
		    	return;
		    }
		    if (hasKitty) {
		    	for (int x = 0; x < kitty.length; x++) {
		    		if (kitty[x] == chosen.getCard()) {
		    			if (x == selectedKittyIndex) {
		    				NineFiveTwo.highliteKittyIndex(selectedKittyIndex);
		    				selectedKittyIndex = -1;
		    				return;
		    			}
		    			if (selectedKittyIndex >= 0) {
		    				NineFiveTwo.highliteKittyIndex(selectedKittyIndex);
		    			}
		    			selectedKittyIndex = x;
		    			break;
		    		}
		    	}
		    	NineFiveTwo.highliteKittyIndex(selectedKittyIndex);
		    	if (selectedHandIndex != -1) {
		    		Cards temp = kitty[selectedKittyIndex];
		    		kitty[selectedKittyIndex] = hand.get(selectedHandIndex);
		    		hand.set(selectedHandIndex, temp);
		    		Deck.sort(hand);
		    		selectedKittyIndex = -1;
		    		selectedHandIndex = -1;
		    		NineFiveTwo.updateKitty(kitty);
		    	}
		    }
		    return;
		}
		if (!handContainsCard(chosen.getCard())) {
			return;
		}
		if (hasKitty) {
			for (int x = 0; x < hand.size(); x++) {
	    		if (hand.get(x) == chosen.getCard()) {
	    			if (x == selectedHandIndex) {
	    				NineFiveTwo.highliteHandIndex(selectedHandIndex);
	    				selectedHandIndex = -1;
	    				return;
	    			}
	    			if (selectedHandIndex >= 0) {
	    				NineFiveTwo.highliteHandIndex(selectedHandIndex);
	    			}
	    			selectedHandIndex = x;
	    			break;
	    		}
	    	}
			NineFiveTwo.highliteHandIndex(selectedHandIndex);
	    	if (selectedKittyIndex != -1) {
	    		Cards temp = kitty[selectedKittyIndex];
	    		kitty[selectedKittyIndex] = hand.get(selectedHandIndex);
	    		hand.set(selectedHandIndex, temp);
	    		Deck.sort(hand);
	    		selectedKittyIndex = -1;
	    		selectedHandIndex = -1;
	    		NineFiveTwo.updateKitty(kitty);
	    	}
	    	return;
		}
		if (passingCards) {
			if (hasPassedCard(chosen.getCard())) {
				cardsToPass.remove(chosen.getCard());
				NineFiveTwo.highliteHandIndex(getHandIndex(chosen.getCard()));
				selectedCards--;
			}
			else if (numOfCardsToPass == selectedCards) {//All the slots are full.
				NineFiveTwo.highliteHandIndex(getHandIndex(cardsToPass.iterator().next()));//Any card.
				cardsToPass.add(chosen.getCard());
				NineFiveTwo.highliteHandIndex(getHandIndex(chosen.getCard()));
			}
			else {
				cardsToPass.add(chosen.getCard());
				selectedCards++;
				NineFiveTwo.highliteHandIndex(getHandIndex(chosen.getCard()));
			}
			NineFiveTwo.setDoneButtonEnabled(selectedCards == numOfCardsToPass);
			return;
		}
		if (!playersTurn) {
			return;
		}
		if (!followsSuit(chosen.getCard())) {
			return;
		}
		playersTurn = false;
		removeCard(chosen.getCard());
		NineFiveTwo.playerPlayedCard(chosen.getCard());
	}
	
	@Override
	public Set<Cards> passCards(Player player, int number) {
		playerToPassTo = player;
		passingCards = true;
		selectedCards = 0;
		numOfCardsToPass = number;
		cardsToPass = new HashSet<Cards>();
		NineFiveTwo.displayMessage("Pass " + number + " cards to " + player.getName());
		return cardsToPass;
 	}
	
	public Set<Cards> receiveCards(Set<Cards> cards, Player player) {
		NineFiveTwo.displayMessage(player.getName() + " sent you " + cards.size() + " cards");
		return super.receiveCards(cards);
	}
	
	public void getCardsBack(Set<Cards> cards, Player player) {
		NineFiveTwo.displayMessage(player.getName() + " sent you back " + cards.size() + " cards");
		super.getCardsBack(cards);
	}

	@Override
	public Cards playCard(Cards lead) {
		playersTurn = true;
		return super.playCard(lead);
	}
	
	private boolean hasPassedCard(Cards card) {
		for (Cards cards : cardsToPass) {
			if (cards == card) {
				return true;
			}
		}
		return false;
	}
	
	private int getHandIndex(Cards card) {
		return hand.indexOf(card);
	}
}