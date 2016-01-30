package ca.mcgill.mail.stauch.justin.ninefivetwo.main;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import ca.mcgill.mail.stauch.justin.ninefivetwo.cards.Cards;

public class PlayingCard extends JButton {
	private static final long serialVersionUID = 1L;
	
	private Cards card;
	private boolean partOfKitty;
	
	public PlayingCard() {
		super();
		partOfKitty = false;
	}
	
	public void setCard(Cards card) {
		this.card = card;
		if (this.card == Cards.EMPTY) {
			setVisible(false);
			setEnabled(false);
			return;
		}
		setIcon(new ImageIcon(this.card.getImage()));
		setPressedIcon(new ImageIcon(this.card.getImage()));
	}
	
	public Cards getCard() {
		return card;
	}
	
	public void setPartOfKitty(boolean partOfKitty) {
		this.partOfKitty = partOfKitty;
	}
	
	public boolean isPartOfKitty() {
		return partOfKitty;
	}
}