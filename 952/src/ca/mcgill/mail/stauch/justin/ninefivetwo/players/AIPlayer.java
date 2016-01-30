package ca.mcgill.mail.stauch.justin.ninefivetwo.players;

import java.util.Random;
import java.util.Set;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.main.NineFiveTwo;

public class AIPlayer extends Player {

	public AIPlayer(String name) {
		super(name);
	}

	@Override
	public Cards playCard() {
		Random random = new Random();
		Cards play = hand.get(random.nextInt(hand.size()));
		removeCard(play);
		return play;
	}

	@Override
	public Cards playCard(Cards lead) {
		Cards toPlay = Cards.EMPTY;
		if (!canWin(lead)) {
			if (this.hasSuit(lead.getSuit())) {
				for (Cards card : hand) {
					if (card.getSuit() == lead.getSuit()) {
						if (toPlay == Cards.EMPTY) {
							toPlay = card;
						}
						else if (card.getValue() < toPlay.getValue()) {
							toPlay = card;
						}
					}
				}
			}
			else {
				Random random = new Random();
			    while (toPlay == Cards.EMPTY) {
			    	toPlay = hand.get(random.nextInt(hand.size()));
			    }
			}
		}
		else {
			toPlay = getLowestWinner(lead);
		}
		removeCard(toPlay);
		return toPlay;
	}

	@Override
	public Cards playCard(Cards lead, Cards followed) {
		if (!canWin(lead, followed)) {
			return playCard(lead);
		}
		Cards toPlay = getLowestWinner(lead, followed);
		removeCard(toPlay);
		return toPlay;
	}
	
	@Override
	public Set<Cards> passCards(Player player, int number) {
		return null;
	}
	
	private boolean canWin(Cards lead) {
		if (hasSuit(NineFiveTwo.getTrump()) && !hasSuit(lead.getSuit())) {
			return true;
		}
		if (!hasSuit(lead.getSuit()) && !hasSuit(NineFiveTwo.getTrump())) {
			return false;
		}
		for (Cards card : hand) {
			if (card.getSuit() == lead.getSuit() && card.getValue() > lead.getValue()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canWin(Cards lead, Cards followed) {
		if (followed.getSuit() != lead.getSuit() && followed.getSuit() != NineFiveTwo.getTrump()) {
			return canWin(lead);
		}
		if (hasSuit(NineFiveTwo.getTrump()) && !hasSuit(lead.getSuit()) && followed.getSuit() != NineFiveTwo.getTrump()) {
			return true;
		}
		if (!hasSuit(lead.getSuit()) && !hasSuit(NineFiveTwo.getTrump())) {
			return false;
		}
		if (hasSuit(lead.getSuit()) && followed.getSuit() == NineFiveTwo.getTrump() && lead.getSuit() != NineFiveTwo.getTrump()) {
			return false;
		}
		if (lead.getSuit() == followed.getSuit()) {
			for (Cards card : hand) {
				if (card.getSuit() == lead.getSuit() && card.ordinal() > lead.ordinal() && card.ordinal() > followed.ordinal()) {
					return true;
				}
			}
			return false;
		}
		if (lead.getSuit() != NineFiveTwo.getTrump() && followed.getSuit() == NineFiveTwo.getTrump()) {
			for (Cards card : hand) {
				if (card.getSuit() == NineFiveTwo.getTrump() && card.ordinal() > followed.ordinal()) {
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	private Cards getLowestWinner(Cards lead) {
		if (!canWin(lead)) {
			return Cards.EMPTY;
		}
		Suit targetSuit = hasSuit(lead.getSuit()) ? lead.getSuit() : NineFiveTwo.getTrump();
		if (targetSuit == Suit.NULL) {
			return Cards.EMPTY;
		}
		Cards lowestWinner = Cards.EMPTY;
		for (Cards card : hand) {
			if (card.getSuit() == targetSuit) {
				if (targetSuit == lead.getSuit() && card.ordinal() < lead.ordinal()) {
					continue;
				}
				if (card.ordinal() < lowestWinner.ordinal()) {
					lowestWinner = card;
				}
			}
		}
		return lowestWinner;
	}
	
	private Cards getLowestWinner(Cards lead, Cards followed) {
		if (!canWin(lead, followed)) {
			return Cards.EMPTY;
		}
		if (followed.getSuit() != lead.getSuit() && followed.getSuit() != NineFiveTwo.getTrump()) {
			return getLowestWinner(lead);
		}
		if (followed.getSuit() == lead.getSuit()) {//Just have to beat the highest card, and the highest card must be the same as the one lead.
			if (followed.ordinal() < lead.ordinal()) {
				return getLowestWinner(lead);
			}
			if (followed.ordinal() > lead.ordinal()) {
				return getLowestWinner(followed);
			}
		}
		//The player has to trump in order to win, because the followed card trumped the lead card.
		Suit trump = NineFiveTwo.getTrump();
		Cards lowestWinner= Cards.EMPTY;
		for (Cards card : hand) {
			if (card.getSuit() == trump) {
				if (card.ordinal() < followed.ordinal()) {//The card cannot win.
					continue;
				}
				if (lowestWinner == Cards.EMPTY) {
					lowestWinner = card;
				}
				if (card.ordinal() < lowestWinner.ordinal()) {
					lowestWinner = card;
				}
			}
		}
		return lowestWinner;
	}
}