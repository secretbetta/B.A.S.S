package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.api.client.util.ArrayMap;

public class Poker {
	
	/* Main deck */
	private ArrayList<String> cards;
	
	/* Player Hands */
	private Map<Integer, ArrayList<String>> playerHands;
	
	/* Player Bets */
	private int[] bets;
	
	/* Player money */
	private int[] money;
	
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
				cards.add(String.format("%s%c", num.charAt(n),
					suites.charAt(s)));
			}
		}
		return cards;
	}
	
	/**
	 * Collects all cards from players to main deck
	 */
	public void collectCards() {
		for (ArrayList<String> playercards : this.playerHands.values()) {
			this.cards.addAll(playercards);
		}
	}
	
	/**
	 * Deals n cards to player
	 * 
	 * @param n      Number of cards
	 * @param player 0 = Player1 or 1 = Player2
	 */
	public void deal(int n, int player) {
		for (int p = 0; p < n; p++) {
			this.playerHands.get(player).add(this.cards.remove((int) Math.random() * this.cards.size()));
		}
	}
	
	/*
	 * Deals n cards to all players
	 */
	public void dealAll(int n) {
		for (int player : this.playerHands.keySet()) {
			this.deal(n, player);
		}
	}
	
	public void raise() {
		
	}
	
	public void check() {
		
	}
	
	public boolean isRoyalFlush() {
		return false;
	}
	
	public boolean isStraightFlush() {
		return false;
	}
	
	public boolean isFourOfAKind() {
		return false;
	}
	
	public boolean isFullHouse() {
		return false;
	}
	
	public boolean isFlush() {
		return false;
	}
	
	public boolean isStraight() {
		return false;
	}
	
	public boolean isThreeOfAKind() {
		return false;
	}
	
	public boolean isTwoPairs() {
		return false;
	}
	
	public boolean isPair() {
		return false;
	}
	
	public String getPairs() {
		return null;
	}
	
	public String getHighCard(ArrayList<String> cards) {
		return null;
	}
	
	public String getDeck() {
		String deck = "";
		for (String card : this.cards) {
			deck += String.format("%s ", card.contains("D") ? "10" + card.substring(1) : card);
		}
		
		return deck;
	}
	
	public static void main(String[] args) {
		Poker game = new Poker();
		System.out.println(game.getDeck());
		Collections.sort(game.cards);
		System.out.println(game.getDeck()); // Might make card and deck classes
	}
}
