package ca.mcgill.mail.stauch.justin.ninefivetwo.main;

import java.util.List;
import java.util.Random;
import java.util.Set;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Deck;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;
import ca.mcgill.mail.stauch.justin.ninefivetwo.players.AIPlayer;
import ca.mcgill.mail.stauch.justin.ninefivetwo.players.HumanPlayer;
import ca.mcgill.mail.stauch.justin.ninefivetwo.players.Player;

/**
 * Main class of the 952 game. Loads up the game, and starts it.
 * 
 * The game has three players, each going for a different amount of tricks. The dealer goes for 9, the player to the dealer's left goes for 5 and
 * leads for the first trick, and the last player goes for 2. The dealer deals 16 cards to each player, leaving 4 cards for the kitty. The dealer calls
 * trump, then picks up the kitty, and discards any 4 cards in his or her hand. If a player gets more tricks than his or her contract, he or she gets a
 * point for every trick that they went over. Similarly, a player loses a point for every trick that they went under. In addition, a player who goes over
 * gets to pass as many cards as tricks that they won to the player(s) who went under at the start of the next round. The passing is based on how many tricks
 * each player went over or under. If two players are over, the higher contract gets to pass first. Players who went under have to giveup the highest card
 * of the suits that were passed to them. Passing is done before the call of trump. The game ends when someone reaches +15 or -15. Whoever has the highest
 * score wins.
 * 
 * @author Justin Stauch
 * @since 23, January, 2015
 */
public final class NineFiveTwo {
    private static GameScreen screen;
    private static HumanPlayer player;
    private static AIPlayer computer1, computer2;
    private static Deck deck;
    private static int lead, cardsPlayedInTrick;
    private static Cards[] playedCard, kitty;
    private static Suit trump;
    private static boolean endOfTrick;
    private static int[] diff = {0, 0, 0};
	
    private NineFiveTwo() {
    	
    }
    
	public static void main(String[] args) {
		player = new HumanPlayer("Justin");
		computer1 = new AIPlayer("Kwok");
		computer2 = new AIPlayer("Repko");
		playedCard = new Cards[3];
		screen = new GameScreen(player.getName(), computer1.getName(), computer2.getName());
		Random random = new Random();
		setupRound(random.nextInt(3));
	}
	
	public static HumanPlayer getPlayer() {
		return player;
	}
	
	public static boolean isEndOfTrick() {
		return endOfTrick;
	}
	
	public static void startNextTrick() {
		endOfTrick = false;
		screen.endOfTrick();
		updateScores();
		if ((player.getTricks() + computer1.getTricks() + computer2.getTricks()) >= 16) {
			endRound();
			return;
		}
		cardsPlayedInTrick = 0;
		switch (lead) {
	        case 1:
	    	    playedCard[1] = computer1.playCard();
	    	    cardsPlayedInTrick++;
	    	    screen.cardPlayed(playedCard[1], 1);
	    	    playedCard[2] = computer2.playCard(playedCard[1]);
	    	    cardsPlayedInTrick++;
	    	    screen.cardPlayed(playedCard[2], 2);
	    	    break;
	        case 2:
	    	    playedCard[2] = computer2.playCard();
	    	    cardsPlayedInTrick++;
	    	    screen.cardPlayed(playedCard[2], 2);
	    	    break;
	    }
	    promptHumanPlayer();
	}
	
	private static void setupRound(int dealer) {
		deck = new Deck();
		List<Cards>[] hands = deck.shuffleAndDeal(1000);
		lead = dealer == 2 ? 0 : dealer + 1;
		kitty = hands[3].toArray(new Cards[4]);
		switch (dealer) {
		    case 0:
		    	player.setContract(9);
		    	computer1.setContract(5);
		    	computer2.setContract(2);
		    	player.setHand(hands[2]);
		    	computer1.setHand(hands[0]);
		    	computer2.setHand(hands[1]);
		    	break;
		    case 1:
		    	player.setContract(2);
		    	computer1.setContract(9);
		    	computer2.setContract(5);
		    	player.setHand(hands[1]);
		    	computer1.setHand(hands[2]);
		    	computer2.setHand(hands[0]);
		    	break;
		    case 2:
		    	player.setContract(5);
		    	computer1.setContract(2);
		    	computer2.setContract(9);
		    	player.setHand(hands[0]);
		    	computer1.setHand(hands[1]);
		    	computer2.setHand(hands[2]);
		    	break;
		}
		screen.displayPlayersHand(player.getHand());
		setupCardPassing();
		player.setTricks(0);
		computer1.setTricks(0);
		computer2.setTricks(0);
		updateScores();
	}
	
	private static void endRound() {
		player.adjustScore(player.getTricks() - player.getContract());
		computer1.adjustScore(computer1.getTricks() - computer1.getContract());
		computer2.adjustScore(computer2.getTricks() - computer2.getContract());
		diff[0] = player.getTricks() - player.getContract();
		diff[1] = computer1.getTricks() - computer1.getContract();
		diff[2] = computer2.getTricks() - computer2.getContract();
		if (player.getScore() == 15 || player.getScore() == -15 || computer1.getScore() == 15 || computer1.getScore() == -15 || computer2.getScore() == 15 || computer2.getScore() == -15) {
			endGame();
		}
		int dealer = 0;
		if (computer1.getContract() == 2) {
			dealer = 1;
		}
		else if (computer2.getContract() == 2) {
			dealer = 2;
		}
		updateScores();
		setupRound(dealer);
	}
	
	private static void endGame() {
		
	}
	
	private static void setupCardPassing() {
		if (player.getContract() == 9) {
			if (diff[0] > 0) {
				if (diff[1] < 0) {
					player.passCards(computer1, diff[0] < -diff[1] ? diff[0] : -diff[1]);
					screen.allowCardPassing();
					return;
				}
				if (diff[2] < 0) {
					player.passCards(computer2, diff[0] < -diff[2] ? diff[0] : -diff[2]);
					screen.allowCardPassing();
					return;
				}
			}
			if (diff[1] > 0) {
				if (diff[0] < 0) {
					computer1.getCardsBack(player.receiveCards(computer1.passCards(player, diff[1] < -diff[0] ? diff[1] : -diff[0]), computer1));
				}
				if (diff[2] < 0) {
					computer1.getCardsBack(computer2.receiveCards(computer1.passCards(computer2, diff[1] < -diff[2] ? diff[1] : -diff	[2])));
				}
			}
			if (diff[2] > 0) {
				if (diff[0] < 0) {
					computer2.getCardsBack(player.receiveCards(computer2.passCards(player, diff[2] < -diff[0] ? diff[2] : -diff[2]), computer2));
				}
				if (diff[1] < 0) {
					computer2.getCardsBack(computer1.receiveCards(computer2.passCards(computer1, diff[2] < -diff[1] ? diff[2] : -diff	[1])));
				}
			}
		}
		else if (computer1.getContract() == 9) {
			if (diff[1] > 0) {
				if (diff[0] < 0) {
					computer1.getCardsBack(player.receiveCards(computer1.passCards(player, diff[1] < -diff[0] ? diff[1] : -diff[0]), computer1));
				}
				if (diff[2] < 0) {
					computer1.getCardsBack(computer2.receiveCards(computer1.passCards(computer2, diff[1] < -diff[2] ? diff[1] : -diff	[2])));
				}
			}
			if (diff[2] > 0) {
				if (diff[0] < 0) {
					computer2.getCardsBack(player.receiveCards(computer2.passCards(player, diff[2] < -diff[0] ? diff[2] : -diff[2]), computer2));
				}
				if (diff[1] < 0) {
					computer2.getCardsBack(computer1.receiveCards(computer2.passCards(computer1, diff[2] < -diff[1] ? diff[2] : -diff	[1])));
				}
			}
			if (diff[0] > 0) {
				if (diff[1] < 0) {
					player.passCards(computer1, diff[0] < -diff[1] ? diff[0] : -diff[1]);
					screen.allowCardPassing();
					return;
				}
				if (diff[2] < 0) {
					player.passCards(computer2, diff[0] < -diff[2] ? diff[0] : -diff[2]);
					screen.allowCardPassing();
					return;
				}
			}
		}
		else if (computer2.getContract() == 9) {
			if (diff[2] > 0) {
				if (diff[0] < 0) {
					computer2.getCardsBack(player.receiveCards(computer2.passCards(player, diff[2] < -diff[0] ? diff[2] : -diff[2]), computer2));
				}
				if (diff[1] < 0) {
					computer2.getCardsBack(computer1.receiveCards(computer2.passCards(computer1, diff[2] < -diff[1] ? diff[2] : -diff	[1])));
				}
			}
			if (diff[0] > 0) {
				if (diff[1] < 0) {
					player.passCards(computer1, diff[0] < -diff[1] ? diff[0] : -diff[1]);
					screen.allowCardPassing();
					return;
				}
				if (diff[2] < 0) {
					player.passCards(computer2, diff[0] < -diff[2] ? diff[0] : -diff[2]);
					screen.allowCardPassing();
					return;
				}
			}
			if (diff[1] > 0) {
				if (diff[0] < 0) {
					computer1.getCardsBack(player.receiveCards(computer1.passCards(player, diff[1] < -diff[0] ? diff[1] : -diff[0]), computer1));
				}
				if (diff[2] < 0) {
					computer1.getCardsBack(computer2.receiveCards(computer1.passCards(computer2, diff[1] < -diff[2] ? diff[1] : -diff	[2])));
				}
			}
		}
		setupTrump();
	}
	
	private static void setupTrump() {
		int dealer = lead == 0 ? 2 : lead - 1;
		switch (dealer) {
	        case 0:
	    	    player.chooseTrump();
		        screen.displayTrumpChoices();
		        return;
	        case 1:
	    	    trump = computer1.chooseTrump();
	    	    computer1.takeKitty(kitty);
	    	    break;
	        case 2:
	    	    trump = computer2.chooseTrump();
	    	    computer2.takeKitty(kitty);
	    	    break;
	    }
		startNextTrick();
	}
	
	public static void playerPassedCards(Set<Cards> cardsToPass, Player passedTo) {
	    player.getCardsBack(passedTo.receiveCards(cardsToPass), passedTo);
	    screen.displayPlayersHand(player.getHand());
	    if (passedTo.equals(computer1) && diff[2] < 0) {
			player.passCards(computer2, diff[0] < -diff[2] ? diff[0] : -diff[2]);
			screen.allowCardPassing();
			return;
	    }
	    screen.hideDoneButton();
	    switch (player.getContract()) {
	        case 9:
	        	if (diff[1] > 0) {
					if (diff[0] < 0) {
						computer1.getCardsBack(player.receiveCards(computer1.passCards(player, diff[1] < -diff[0] ? diff[1] : -diff[0]), computer1));
					}
					if (diff[2] < 0) {
						computer1.getCardsBack(computer2.receiveCards(computer1.passCards(computer2, diff[1] < -diff[2] ? diff[1] : -diff	[2])));
					}
				}
	        	if (diff[2] > 0) {
					if (diff[0] < 0) {
						computer2.getCardsBack(player.receiveCards(computer2.passCards(player, diff[2] < -diff[0] ? diff[2] : -diff[2]), computer2));
					}
					if (diff[1] < 0) {
						computer2.getCardsBack(computer1.receiveCards(computer2.passCards(computer1, diff[2] < -diff[1] ? diff[2] : -diff	[1])));
					}
				}
				break;
	        case 5:
	        	if (diff[1] > 0) {
					if (diff[0] < 0) {
						computer1.getCardsBack(player.receiveCards(computer1.passCards(player, diff[1] < -diff[0] ? diff[1] : -diff[0]), computer1));
					}
					if (diff[2] < 0) {
						computer1.getCardsBack(computer2.receiveCards(computer1.passCards(computer2, diff[1] < -diff[2] ? diff[1] : -diff	[2])));
					}
				}
	        	break;
	    }
	    setupTrump();
	}
	
	public static void playerPlayedCard(Cards card) {
		playedCard[0] = card;
		screen.cardPlayed(card, 0);
		screen.displayPlayersHand(player.getHand());
		cardsPlayedInTrick++;
		switch (cardsPlayedInTrick) {
		    case 1:
		    	playedCard[1] = computer1.playCard(playedCard[0]);
		    	screen.cardPlayed(playedCard[1], 1);
		    	playedCard[2] = computer2.playCard(playedCard[0], playedCard[1]);
		    	screen.cardPlayed(playedCard[2], 2);
		    	break;
		    case 2:
		    	playedCard[1] = computer1.playCard(playedCard[2], playedCard[0]);
		    	screen.cardPlayed(playedCard[1], 1);
		    	break;
		}
		int winner = getWinner();
		switch (winner) {
		    case 0:
		    	displayMessage(player.getName() + " won the trick");
		    	player.wonATrick();
		    	break;
		    case 1:
		    	displayMessage(computer1.getName() + " won the trick");
		    	computer1.wonATrick();
		    	break;
		    case 2:
		    	displayMessage(computer2.getName() + " won the trick");
		    	computer2.wonATrick();
		    	break;
		}
		lead = winner;
		cardsPlayedInTrick = 0;
		endOfTrick = true;
	}
	
	public static Suit getTrump() {
		return trump;
	}
	
	public static void setDoneButtonEnabled(boolean enable) {
		screen.setDoneButtonEnabled(enable);
	}

	public static void doneWithTrump(Suit trump) {
		NineFiveTwo.trump = trump;
		screen.clearKittyOrTrump();
		screen.displayTrump(trump);
		player.takeKitty(kitty);
		screen.displayKitty(kitty);
	}
	
	public static void updateKitty(Cards[] kitty) {
		screen.displayPlayersHand(player.getHand());
		screen.displayKitty(kitty);
	}
	
	public static void doneWithKitty() {
		screen.clearKittyOrTrump();
		startNextTrick();
	}
	
	public static void highliteKittyIndex(int index) {
		screen.highliteKittyIndex(index);
	}
	
    public static void highliteHandIndex(int index) {
		screen.highliteHandIndex(index);
	}
    
    public static void displayMessage(String message) {
    	screen.displayMessage(message);
    }
	
	private static int getWinner() {
		int winner = 0;
		for (int x = 1; x < playedCard.length; x++) {
			if (playedCard[lead].getSuit() == playedCard[x].getSuit()) {
				if (playedCard[winner].getSuit() == trump && playedCard[x].getSuit() != trump) {
					continue;
				}
				if (playedCard[winner].getSuit() != trump && playedCard[winner].getSuit() != playedCard[lead].getSuit()) {
					winner = x;
				}
				else if (playedCard[winner].ordinal() < playedCard[x].ordinal()) {
					winner = x;
				}
			}
			else if (playedCard[x].getSuit() == trump) {
				if (playedCard[winner].getSuit() != trump) {
					winner = x;
				}
				else if (playedCard[winner].ordinal() < playedCard[x].ordinal()) {
					winner = x;
				}
			}
		}
		return winner;
	}
	
	private static void promptHumanPlayer() {
		displayMessage("Your turn");
		switch (lead) {
		    case 0:
		    	player.playCard();
		    	break;
		    case 1:
		    	player.playCard(playedCard[1]);
		    	break;
		    case 2:
		    	player.playCard(playedCard[2], playedCard[1]);
		    	break;
		}
	}
	
	private static void updateScores() {
		screen.updateScores(player.getName(), player.getScore(), player.getTricks(), player.getContract(),
				computer1.getName(), computer1.getScore(), computer1.getTricks(), computer1.getContract(), 
				computer2.getName(), computer2.getScore(), computer2.getTricks(), computer2.getContract());
	}
}