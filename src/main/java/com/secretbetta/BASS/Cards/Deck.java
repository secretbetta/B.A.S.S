package com.secretbetta.BASS.Cards;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	
	ArrayList<Card> deck = new ArrayList<>();
	
	public Deck() {
		this(1);
	}
	
	public Deck(int decks) {
		for (int d = 0; d < decks; d++) {
			for (int x = 0; x <= 52; x++) {
				this.deck.add(new Card(x));
			}
		}
	}
	
	public static ArrayList<Card> shuffle(ArrayList<Card> deck) {
		Collections.shuffle(deck);
		return deck;
	}
	
	public ArrayList<Card> getDeck() {
		return this.deck;
	}
}
