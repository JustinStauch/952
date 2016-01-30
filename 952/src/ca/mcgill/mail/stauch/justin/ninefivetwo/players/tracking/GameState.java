package ca.mcgill.mail.stauch.justin.ninefivetwo.players.tracking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.players.Opponent;

public class GameState {
	private final BlockTracker tracker;
	private final Opponent lead;
	private final Map<Opponent, Integer> scores;
	private final int handsLeft;
	private final Suit trump;
	
	public GameState(Opponent lead, Suit trump, BlockTracker tracker, int handsLeft, int leadScore, int secondScore, int thirdScore) {
		this.lead = lead;
		this.tracker = tracker;
		this.handsLeft = handsLeft;
		this.trump = trump;
		
		scores = new HashMap<Opponent, Integer>();
		scores.put(lead, leadScore);
		scores.put(lead.next(), secondScore);
		scores.put(lead.next().next(), thirdScore);
	}
	
	public List<GameState> getChildren() {
		if (lead == Opponent.ME) {
			
		}
	}
	
	public GameState endOfState(Cards cardLead, Cards second, Cards third) {
		Opponent winner = getWinner(cardLead, second, third);
		
		scores.put(winner, scores.get(winner) + 1);
		
		BlockTracker newTrack = new BlockTracker(tracker, cardLead, second, third);
		
		if (second.getSuit() != cardLead.getSuit()) {
			newTrack.opponentFailedToFollowSuit(lead.next(), cardLead.getSuit());
		}
		
		if (third.getSuit() != cardLead.getSuit()) {
			newTrack.opponentFailedToFollowSuit(lead.next().next(), cardLead.getSuit());
		}
		
		return new GameState(winner, trump, newTrack, handsLeft - 1, scores.get(winner), scores.get(winner.next()), scores.get(winner.next().next()));
	}
	
	private Opponent getWinner(Cards cardLead, Cards second, Cards third) {
		Opponent winner = lead;
		Cards win = cardLead;
		
		if (second.getSuit() == cardLead.getSuit()) {
			if (second.compareTo(cardLead) > 0) {
				winner = lead.next();
				win = second;
			}
		}
		else if (second.getSuit() == trump) {
			winner = lead.next();
			win = second;
		}
		
		if (third.getSuit() == win.getSuit()) {
			if (third.compareTo(win) > 0) {
				winner = lead.next().next();
			}
		}
		else if (third.getSuit() == trump) {
			winner = lead.next().next();
		}
		
		return winner;
	}
}