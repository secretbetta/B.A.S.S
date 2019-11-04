package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Blackjack game
 * 
 * @author Andrew
 */
public class Blackjack {
	
	/* Main deck */
	private ArrayList<String> cards;
	
	/* Player Hands */
	private ArrayList<String> p1hand;
	private ArrayList<String> p2hand;
	
	/**
	 * Initializes deck of cards with 52 cards (in sorted order)
	 */
	public Blackjack() {
		this.cards = newDeck();
		this.p1hand = new ArrayList<>();
		this.p2hand = new ArrayList<>();
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
	
	/**
	 * Collects all cards from players to main deck
	 */
	public void collectCards() {
		while (this.p1hand.size() != 0) {
			this.cards.add(this.p1hand.remove(0));
		}
		while (this.p2hand.size() != 0) {
			this.cards.add(this.p2hand.remove(0));
		}
	}
	
	/**
	 * Deals n cards to player
	 * 
	 * @param n      Number of cards
	 * @param player 0 = Player1 or 1 = Player2
	 */
	public void deal(int n, int player) {
		if (player == 0) {
			for (int p = 0; p < n; p++) {
				this.p1hand.add(this.cards.remove((int) Math.random() * this.cards.size()));
			}
		} else if (player == 1) {
			for (int p = 0; p < n; p++) {
				this.p2hand.add(this.cards.remove((int) Math.random() * this.cards.size()));
			}
		}
	}
	
	/*
	 * Deals n cards to all players
	 */
	public void dealAll(int n) {
		this.deal(n, 0);
		this.deal(n, 1);
	}
	
	/**
	 * Gets Undrawn pile size
	 * 
	 * @return size of deck
	 */
	public int deckSize() {
		return this.cards.size();
	}
	
	public ArrayList<String> getHand(int player) {
		if (player == 0) {
			return this.p1hand;
		} else if (player == 1) {
			return this.p2hand;
		} else {
			return null;
		}
	}
	
	/**
	 * <h1>Debug output</h1>
	 * Prints cards in array format
	 */
	public void printCards() {
		System.out.println(this.cards);
	}
	
	public void printHand(int player) {
		if (player == 0) {
			System.out.println(this.p1hand);
		} else if (player == 1) {
			System.out.println(this.p2hand);
		}
	}
	
	/**
	 * Shuffles Deck
	 */
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	public static void main(String[] args) {
		Blackjack game = new Blackjack();
		Scanner scan = new Scanner(System.in);
		game.shuffle();
		game.dealAll(2);
		System.out.print("Player 1: ");
		game.printHand(0);
		System.out.print("Player 2: ");
		game.printHand(1);
		
		int[] sums = new int[2];
		
		for (int p = 0; p < 2; p++) {
			for (String card : game.getHand(p)) {
				switch (card.substring(0, 1)) {
					case "D":
					case "J":
					case "Q":
					case "K":
						sums[p] += 10;
						break;
					case "A":
						if (sums[p] + 11 > 21) {
							sums[p] += 1;
						} else {
							sums[p] += 11;
						}
						break;
					default:
						sums[p] += Integer.parseInt(card.substring(0, 1));
						break;
				}
			}
		}
		System.out.println("Player 1 Sum: " + sums[0]);
		System.out.println("Player 2 Sum: " + sums[1]);
	}
}
