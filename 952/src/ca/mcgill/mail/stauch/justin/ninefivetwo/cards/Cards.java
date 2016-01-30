package ca.mcgill.mail.stauch.justin.ninefivetwo.cards;

import java.awt.Image;

import javax.swing.ImageIcon;

public enum Cards {
	
    TWO_OF_CLUBS(new ImageIcon("49.png").getImage()),
    THREE_OF_CLUBS(new ImageIcon("45.png").getImage()),
    FOUR_OF_CLUBS(new ImageIcon("41.png").getImage()),
    FIVE_OF_CLUBS(new ImageIcon("37.png").getImage()),
    SIX_OF_CLUBS(new ImageIcon("33.png").getImage()),
    SEVEN_OF_CLUBS(new ImageIcon("29.png").getImage()),
    EIGHT_OF_CLUBS(new ImageIcon("25.png").getImage()),
    NINE_OF_CLUBS(new ImageIcon("21.png").getImage()),
    TEN_OF_CLUBS(new ImageIcon("17.png").getImage()),
    JACK_OF_CLUBS(new ImageIcon("13.png").getImage()),
    QUEEN_OF_CLUBS(new ImageIcon("9.png").getImage()),
    KING_OF_CLUBS(new ImageIcon("5.png").getImage()),
    ACE_OF_CLUBS(new ImageIcon("1.png").getImage()),
	
    TWO_OF_DIAMONDS(new ImageIcon("52.png").getImage()),
    THREE_OF_DIAMONDS(new ImageIcon("48.png").getImage()),
    FOUR_OF_DIAMONDS(new ImageIcon("44.png").getImage()),
    FIVE_OF_DIAMONDS(new ImageIcon("40.png").getImage()),
    SIX_OF_DIAMONDS(new ImageIcon("36.png").getImage()),
    SEVEN_OF_DIAMONDS(new ImageIcon("32.png").getImage()),
    EIGHT_OF_DIAMONDS(new ImageIcon("28.png").getImage()),
    NINE_OF_DIAMONDS(new ImageIcon("24.png").getImage()),
    TEN_OF_DIAMONDS(new ImageIcon("20.png").getImage()),
    JACK_OF_DIAMONDS(new ImageIcon("16.png").getImage()),
    QUEEN_OF_DIAMONDS(new ImageIcon("12.png").getImage()),
    KING_OF_DIAMONDS(new ImageIcon("8.png").getImage()),
    ACE_OF_DIAMONDS(new ImageIcon("4.png").getImage()),
    
    TWO_OF_SPADES(new ImageIcon("50.png").getImage()),
    THREE_OF_SPADES(new ImageIcon("46.png").getImage()),
    FOUR_OF_SPADES(new ImageIcon("42.png").getImage()),
    FIVE_OF_SPADES(new ImageIcon("38.png").getImage()),
    SIX_OF_SPADES(new ImageIcon("34.png").getImage()),
    SEVEN_OF_SPADES(new ImageIcon("30.png").getImage()),
    EIGHT_OF_SPADES(new ImageIcon("26.png").getImage()),
    NINE_OF_SPADES(new ImageIcon("22.png").getImage()),
    TEN_OF_SPADES(new ImageIcon("18.png").getImage()),
    JACK_OF_SPADES(new ImageIcon("14.png").getImage()),
    QUEEN_OF_SPADES(new ImageIcon("10.png").getImage()),
    KING_OF_SPADES(new ImageIcon("6.png").getImage()),
    ACE_OF_SPADES(new ImageIcon("2.png").getImage()),
    
    TWO_OF_HEARTS(new ImageIcon("51.png").getImage()),
    THREE_OF_HEARTS(new ImageIcon("47.png").getImage()),
    FOUR_OF_HEARTS(new ImageIcon("43.png").getImage()),
    FIVE_OF_HEARTS(new ImageIcon("39.png").getImage()),
    SIX_OF_HEARTS(new ImageIcon("35.png").getImage()),
    SEVEN_OF_HEARTS(new ImageIcon("31.png").getImage()),
    EIGHT_OF_HEARTS(new ImageIcon("27.png").getImage()),
    NINE_OF_HEARTS(new ImageIcon("23.png").getImage()),
    TEN_OF_HEARTS(new ImageIcon("19.png").getImage()),
    JACK_OF_HEARTS(new ImageIcon("15.png").getImage()),
    QUEEN_OF_HEARTS(new ImageIcon("11.png").getImage()),
    KING_OF_HEARTS(new ImageIcon("7.png").getImage()),
    ACE_OF_HEARTS(new ImageIcon("3.png").getImage()),
    
    EMPTY(null);//For when a card gets played.
    
    private Image image;
    private Suit suit;

	private Cards(Image image) {
        this.image = image;
        switch (ordinal() / 13) {
            case 0:  suit = Suit.CLUBS;
                     break;
            case 1:  suit = Suit.DIAMONDS;
                     break;
            case 2:  suit = Suit.SPADES;
                     break;
            case 3:  suit = Suit.HEARTS;
                     break;
            default: suit = Suit.NULL;
                     break;
        }
	}
	    
	public Image getImage() {
	    return image;
    }
	
	public Suit getSuit() {
		return suit;
	}
	
	public int getValue() {
		int value = ordinal();
		while (value > 12) {
			value -= 13;
		}
		return value;
	}
	
	/**
	 * Gets the card one above it in rank of the same suit.
	 * 
	 * The purpose of this is to store consecutive cards as a linked list.
	 * 
	 * @pre This card must not be an ace.
	 * 
	 * @return The next card in rank.
	 */
	public Cards nextCard() {
		assert ordinal() % 13 != 12;
		return values()[ordinal() + 1];
	}
	
	/**
	 * Gets the card of the same suit one rank below this card.
	 * 
	 * @pre This card must not be a two.
	 * 
	 * @return The card one below it in rank.
	 */
	public Cards previousCard() {
		assert ordinal() % 13 != 0;
		return values()[ordinal() - 1];
	}
}