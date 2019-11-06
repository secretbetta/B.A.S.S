package com.secretbetta.BASS.Cards;

import java.util.ArrayList;
import java.util.Collections;

public class Card implements Comparable<Card> {
	
	private int num;
	private int suite;
	
	public Card() {
		this.num = 0;
		this.suite = 0;
	}
	
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
			case 1:
				this.suite = 1;
				break;
			case 2:
				this.suite = 2;
				break;
			case 3:
				this.suite = 3;
				break;
			case 0:
				this.suite = 4;
				break;
		}
		
	}
	
	@Override
	public int compareTo(Card card) {
		int comp = Integer.compare(this.num, card.num);
		if (comp == 0) {
			return Integer.compare(this.suite, card.suite);
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
				card = "" + (this.num);
		}
		
		switch (this.suite) {
			case 1:
				card += "♠";
				break;
			case 2:
				card += "♥";
				break;
			case 3:
				card += "♦";
				break;
			case 4:
				card += "♣";
				break;
		}
		return card;
	}
	
	public static void main(String[] args) {
		ArrayList<Card> deck = new ArrayList<>();
		for (int x = 52; x > 0; x--) {
			deck.add(new Card(x));
		}
		System.out.println(deck);
		Collections.sort(deck);
		System.out.println(deck);
	}
}
