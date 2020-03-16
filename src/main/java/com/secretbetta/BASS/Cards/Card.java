package com.secretbetta.BASS.Cards;

public class Card implements Comparable<Card> {
	
	private int num;
	private int suit;
	
	/**
	 * No card
	 */
	public Card() {
		this.num = 0;
		this.suit = 0;
	}
	
	/**
	 * Initiates card 1-52. 1 being the lowest card, 2 of clovers, to 52 being ace of spades
	 * 
	 * @param c
	 */
	public Card(int c) {
		switch (c % 4) {
			case 1:
				this.num = (c + 7) / 4;
				break;
			case 2:
				this.num = (c + 6) / 4;
				break;
			case 3:
				this.num = (c + 5) / 4;
				break;
			case 0:
				this.num = (c + 4) / 4;
				break;
		}
		
		switch (c % 4) {
			case 0:
				this.suit = 1;
				break;
			case 1:
				this.suit = 4;
				break;
			case 2:
				this.suit = 3;
				break;
			case 3:
				this.suit = 2;
				break;
		}
	}
	
	/**
	 * Gets card number
	 * 
	 * @return Number of card 1-13
	 */
	public int getCardNumber() {
		return this.num;
	}
	
	/**
	 * Get's card suit
	 * 
	 * @return Suit of card, 1-4. 1 being clovers, 2 being diamonds, 3 being hearts, 4 being spades
	 */
	public int getCardSuit() {
		return this.suit;
	}
	
	@Override
	public int compareTo(Card card) {
		int comp = Integer.compare(this.num, card.num);
		if (comp == 0) {
			return Integer.compare(this.suit, card.suit);
		}
		
		return comp;
	}
	
	@Override
	public String toString() {
		String card = "";
		
		switch (this.num) {
			case 11:
				card = "J";
				break;
			case 12:
				card = "Q";
				break;
			case 13:
				card = "K";
				break;
			case 14:
				card = "A";
				break;
			default:
				card = "" + this.num;
		}
		
		switch (this.suit) {
			case 4:
				card += "♠";
				break;
			case 3:
				card += "♥";
				break;
			case 2:
				card += "♦";
				break;
			case 1:
				card += "♣";
				break;
		}
		return card;
	}
}
