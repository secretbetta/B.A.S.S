package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.Map;

import com.google.api.client.util.ArrayMap;

public class Poker {
	
	/* Main deck */
	private ArrayList<String> cards;
	
	/* Player Hands */
	private Map<Integer, ArrayList<String>> playerHands;
	
	/**
	 * Initializes deck of cards with 52 cards (in sorted order)
	 */
	public Poker() {
		this.cards = newDeck();
		this.playerHands = new ArrayMap<>();
	}
	
	/**
	 * Initializes deck of cards and number of players
	 * 
	 * @param players Number of players
	 */
	public Poker(int players) {
		this();
		this.initializePlayers(players);
	}
	
	/**
	 * Initializes number of player hands
	 * 
	 * @param n Number of players
	 */
	public void initializePlayers(int n) {
		for (int x = 0; x < n; x++) {
			this.playerHands.put(x, new ArrayList<String>());
		}
	}
	
	/**
	 * Makes a new deck
	 * 
	 * @return New deck of cards in default order
	 */
	public static ArrayList<String> newDeck() {
		ArrayList<String> cards = new ArrayList<>();
		String suites = "♠♥♦♣";
		String num = "A23456789DJQK";
		for (int s = 0; s < suites.length(); s++) {
			for (int n = 0; n < num.length(); n++) {
				cards.add(String.format("%s%c", (num.charAt(n) == 'D' ? "10" : num.charAt(n)),
					suites.charAt(s)));
			}
		}
		return cards;
	}
	
	public void raise() {
		
	}
	
	public void check() {
		
	}
}
