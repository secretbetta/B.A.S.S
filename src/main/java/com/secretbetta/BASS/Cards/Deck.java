package com.secretbetta.BASS.Cards;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class
 * 
 * @author Andrew
 */
public class Deck {
	
	ArrayList<Card> deck;
	
	/**
	 * Creates 1 new deck
	 */
	public Deck() {
		this.newDeck(1);
	}
	
	/**
	 * Creates multiple decks
	 * 
	 * @param decks Number of decks
	 */
	public Deck(int decks) {
		this.newDeck(decks);
	}
	
	/**
	 * @param decks
	 */
	public void newDeck(int decks) {
		this.deck = new ArrayList<>();
		for (int d = 0; d < decks; d++) {
			for (int x = 1; x <= 52; x++) {
				this.deck.add(new Card(x));
			}
		}
	}
	
	/**
	 * Shuffles deck
	 * 
	 * @param deck Deck to shuffle
	 * @return Shuffled deck
	 */
	public void shuffle() {
		Collections.shuffle(this.deck);
	}
	
	/**
	 * Removes card at index
	 * 
	 * @param index to remove
	 * @return Card that was removed
	 */
	public Card remove(int index) {
		return this.deck.remove(index);
	}
	
	/**
	 * Gets size of deck
	 * 
	 * @return Size of current deck
	 */
	public int size() {
		return this.deck.size();
	}
	
	/**
	 * Gets deck
	 * 
	 * @return
	 */
	public ArrayList<Card> getDeck() {
		return this.deck;
	}
	
	/**
	 * Sorts deck
	 */
	public void sort() {
		Collections.sort(this.deck);
	}
	
	@Override
	public String toString() {
		String deck = "";
		for (Card card : this.deck) {
			deck += String.format("[%s]", card);
		}
		return deck;
	}
}
