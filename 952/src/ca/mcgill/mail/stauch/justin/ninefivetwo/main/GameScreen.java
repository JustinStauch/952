package ca.mcgill.mail.stauch.justin.ninefivetwo.main;

import java.awt.Color;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;
import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Suit;

public class GameScreen extends JFrame {
	private static final long serialVersionUID = 1L;
	private PlayingCard[] cards, kitty;
	private JPanel panel;
	private JLabel[] inPlay, info;
	private JButton doneButton;
	private JLabel message, trump, trumpHeader;
	
	public GameScreen(String playerName, String computer1Name, String computer2Name) {
		setSize(800, 600);
		setTitle("952");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel = new JPanel();
		panel.setBackground(Color.GREEN);
		panel.setBounds(0, 0, 800, 600);
		panel.addMouseListener(new MouseClickResetter());
		setVisible(true);
		cards = new PlayingCard[16];
		for (int x = cards.length - 1; x >= 0; x--) {
			cards[x] = new PlayingCard();
			cards[x].setPressedIcon(null);
			cards[x].setBackground(Color.GREEN);
			cards[x].setBorderPainted(false);
			cards[x].setBorder(new LineBorder(Color.YELLOW, 2));
			cards[x].setBounds((int) ((600.0/(16)) * x) + 75, 419, 72, 96);
			cards[x].setVisible(false);
			cards[x].setEnabled(false);
			cards[x].setForeground(Color.GREEN);
			cards[x].setIconTextGap(0);
			cards[x].addActionListener(NineFiveTwo.getPlayer());
			panel.setLayout(null);
			panel.add(cards[x]);
		}
		kitty = new PlayingCard[4];
		for (int x = 0; x < kitty.length; x++) {
			kitty[x] = new PlayingCard();
			kitty[x].setPartOfKitty(true);
			kitty[x].setPressedIcon(null);
			kitty[x].setBackground(Color.GREEN);
			kitty[x].setBorderPainted(false);
			kitty[x].setBorder(new LineBorder(Color.YELLOW, 2));
			kitty[x].setBounds((int) ((300.0/(4)) * x) + 250, 300, 72, 96);
			kitty[x].setVisible(false);
			kitty[x].setEnabled(false);
			kitty[x].setForeground(Color.GREEN);
			kitty[x].setIconTextGap(0);
			kitty[x].addActionListener(NineFiveTwo.getPlayer());
			panel.setLayout(null);
			panel.add(kitty[x]);
		}
		doneButton = new JButton();
		doneButton.setBounds(350, 260, 100, 30);
		doneButton.setVisible(false);
		doneButton.setEnabled(false);
		doneButton.addActionListener(NineFiveTwo.getPlayer());
		panel.add(doneButton);
		inPlay = new JLabel[3];
		for (int x = 0; x < inPlay.length; x++) {
			inPlay[x] = new JLabel();
			inPlay[x].setVisible(false);
			inPlay[x].setBackground(Color.GREEN);
			inPlay[x].setForeground(Color.GREEN);
			panel.add(inPlay[x]);
		}
		inPlay[2].setBounds(404, 150, 72, 96);
		inPlay[1].setBounds(324, 150, 72, 96);
		inPlay[0].setBounds(364, 220, 72, 96);
		info = new JLabel[3];
		for (int x = 0; x < info.length; x++) {
			info[x] = new JLabel();
			info[x].setVisible(true);
			panel.add(info[x]);
		}
		info[0].setText("<html>" + playerName + "<br>Score: " + 0 + "<br>Tricks: " + 0 + "</html>");
		info[1].setText("<html>" + computer1Name + "<br>Score: " + 0 + "<br>Tricks: " + 0 + "</html>");
		info[2].setText("<html>" + computer2Name + "<br>Score: " + 0 + "<br>Tricks: " + 0 + "</html>");
		info[0].setBounds(600, 275, 70, 200);
		info[1].setBounds(25, 50, 70, 200);
		info[2].setBounds(700, 50, 70, 200);
		message = new JLabel();
		message.setVisible(true);
		message.setHorizontalAlignment(SwingConstants.CENTER);
		message.setBounds(100, 25, 600, 50);
		panel.add(message);
		trump = new JLabel();
		trump.setVisible(false);
		trump.setBounds(600, 35, 72, 96);
		trump.setBackground(Color.GREEN);
		panel.add(trump);
		trumpHeader = new JLabel();
		trumpHeader.setVisible(true);
		trumpHeader.setBounds(600, 1, 72, 50);
		trumpHeader.setHorizontalAlignment(SwingConstants.CENTER);
		trumpHeader.setVerticalTextPosition(SwingConstants.TOP);
		trumpHeader.setText("Trump");
		trumpHeader.setBackground(Color.GREEN);
		panel.add(trumpHeader);
		add(panel);
	}
	
	public void setDoneButtonEnabled(boolean enable) {
		doneButton.setEnabled(enable);
	}
	
	public void hideDoneButton() {
		doneButton.setVisible(false);
		doneButton.setEnabled(false);
	}
	
	public void displayPlayersHand(List<Cards> hand) {
		for (int x = 0; x < hand.size(); x++) {
			cards[x].setCard(hand.get(x));
			cards[x].setBorderPainted(false);
			cards[x].setBorder(null);
			cards[x].setBounds((int) ((600.0/(hand.size())) * (x)) + 75, 419, 72, 96);
			cards[x].setEnabled(true);
			cards[x].setVisible(true);
		}
		for (int x = hand.size(); x < cards.length; x++) {
			cards[x].setVisible(false);
			cards[x].setEnabled(false);
		}
	}
	
	public void cardPlayed(Cards card, int player) {
		inPlay[player].setIcon(new ImageIcon(card.getImage()));
		inPlay[player].setVisible(true);
	}
	
	public void endOfTrick() {
		for (JLabel card : inPlay) {
			card.setVisible(false);
		}
	}
	
	public void allowCardPassing() {
		doneButton.setText("Pass Cards");
		doneButton.setEnabled(false);
		doneButton.setVisible(true);
	}
	
	public void displayKitty(Cards[] kitty) {
		for (int x = 0; x < kitty.length; x++) {
			this.kitty[x].setCard(kitty[x]);
			this.kitty[x].setBorder(null);
			this.kitty[x].setVisible(true);
			this.kitty[x].setEnabled(true);
		}
		doneButton.setText("Done");
		doneButton.setVisible(true);
		doneButton.setEnabled(true);
	}
	
	public void clearKittyOrTrump() {
		for (int x = 0; x < kitty.length; x++) {
			kitty[x].setVisible(false);
			kitty[x].setEnabled(false);
		}
		doneButton.setVisible(false);
		doneButton.setEnabled(false);
	}
	
	public void displayTrumpChoices() {
		kitty[0].setCard(Cards.ACE_OF_CLUBS);
		kitty[1].setCard(Cards.ACE_OF_DIAMONDS);
		kitty[2].setCard(Cards.ACE_OF_SPADES);
		kitty[3].setCard(Cards.ACE_OF_HEARTS);
		for (int x = 0; x < kitty.length; x++) {
			kitty[x].setVisible(true);
			kitty[x].setEnabled(true);
		}
		doneButton.setText("No Trump");
		doneButton.setVisible(true);
		doneButton.setEnabled(true);
	}
	
	public void displayTrump(Suit suit) {
		switch (suit) {
		    case CLUBS:
		    	trump.setIcon(new ImageIcon(Cards.ACE_OF_CLUBS.getImage()));
		    	trump.setVisible(true);
		    	trumpHeader.setText("Trump");
		    	break;
		    case DIAMONDS:
		    	trump.setIcon(new ImageIcon(Cards.ACE_OF_DIAMONDS.getImage()));
		    	trump.setVisible(true);
		    	trumpHeader.setText("Trump");
		    	break;
		    case SPADES:
		    	trump.setIcon(new ImageIcon(Cards.ACE_OF_SPADES.getImage()));
		    	trump.setVisible(true);
		    	trumpHeader.setText("Trump");
		    	break;
		    case HEARTS:
		    	trump.setIcon(new ImageIcon(Cards.ACE_OF_HEARTS.getImage()));
		    	trump.setVisible(true);
		    	trumpHeader.setText("Trump");
		    	break;
		    default:
		    	trump.setVisible(false);
		    	trumpHeader.setText("No Trump");
		}
	}
	
	public void displayMessage(String message) {
		this.message.setText(message);
	}
	
    public void highliteKittyIndex(int index) {
    	kitty[index].setBorder(new LineBorder(Color.YELLOW, 2));
		kitty[index].setBorderPainted(!kitty[index].isBorderPainted());
	}
	
    public void highliteHandIndex(int index) {
    	cards[index].setBorder(new LineBorder(Color.YELLOW, 2));
    	cards[index].setBorderPainted(!cards[index].isBorderPainted());
	}
    
    public void updateScores(String playerName, int playerScore, int playerTricks, int playerContract, String computer1Name, int computer1Score, int computer1Tricks, int computer1Contract, String computer2Name, int computer2Score, int computer2Tricks, int computer2Contract) {
    	info[0].setText("<html>" + playerName + "<br>Score: " + playerScore + "<br>Tricks: " + playerTricks + "<br>Contract: " + playerContract + "</html>");
		info[1].setText("<html>" + computer1Name + "<br>Score: " + computer1Score + "<br>Tricks: " + computer1Tricks + "<br>Contract: " + computer1Contract + "</html>");
		info[2].setText("<html>" + computer2Name + "<br>Score: " + computer2Score + "<br>Tricks: " + computer2Tricks + "<br>Contract: " + computer2Contract + "</html>");
    }
}