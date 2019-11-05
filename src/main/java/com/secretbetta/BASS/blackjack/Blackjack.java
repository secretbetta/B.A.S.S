package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Scanner;

import com.google.api.client.util.ArrayMap;

/**
 * Blackjack game
 * 
 * @author Andrew
 */
public class Blackjack {
	
	/* Main deck */
	private ArrayList<String> cards;
	
	/* Player Hands */
	private Map<Integer, ArrayList<String>> playerHands;
	
	/**
	 * Initializes deck of cards with 52 cards (in sorted order)
	 */
	public Blackjack() {
		this.cards = newDeck();
		this.playerHands = new ArrayMap<>();
	}
	
	/**
	 * Initializes deck of cards and number of players
	 * 
	 * @param players Number of players
	 */
	public Blackjack(int players) {
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
	
	/**
	 * Gets Undrawn pile size
	 * 
	 * @return size of deck
	 */
	public int deckSize() {
		return this.cards.size();
	}
	
	/**
	 * Gets hand of player in ArrayList
	 * 
	 * @param player Player to get hand from
	 * @return ArrayList of player's hand
	 */
	public ArrayList<String> getHand(int player) {
		return this.playerHands.get(player);
	}
	
	/**
	 * Prints player hand
	 * 
	 * @param player Which player to get hand
	 */
	public String printHand(int player) {
		return this.playerHands.get(player).toString();
	}
	
	/**
	 * Shuffles Deck
	 */
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	/**
	 * Custom sorting method
	 * 
	 * @param hand Hand to sort cards
	 * @return Custom sorted cards in hand
	 */
	public static ArrayList<String> sortHand(ArrayList<String> hand) {
		int ace = 0;
		for (int x = 0; x < hand.size(); x++) {
			if (hand.get(x).charAt(0) == 'A') {
				ace++;
				hand.add(hand.remove(x));
			}
		}
		Collections.sort(hand.subList(0, hand.size() - ace));
		return hand;
	}
	
	public int addCards(int player) {
		return addCards(this.playerHands.get(player));
	}
	
	/**
	 * <h1>Sum of cards in hand.</h1>
	 * Aces = 1 or 11, Face cards = 10, Number cards = their corresponding number
	 * 
	 * @param hand Hand to count
	 * @return Sum of cards
	 */
	public static int addCards(ArrayList<String> hand) {
		hand = sortHand(hand);
		int sum = 0;
		for (String card : hand) {
			switch (card.substring(0, 1)) {
				case "J":
				case "Q":
				case "K":
					sum += 10;
					break;
				case "A":
					if (sum + 11 <= 21) {
						sum += 11;
					} else {
						sum += 1;
					}
					break;
				default:
					if (card.length() == 2) {
						sum += Integer.parseInt(card.substring(0, 1));
					} else {
						sum += Integer.parseInt(card.substring(0, 2));
					}
					break;
			}
		}
		return sum;
	}
	
	/**
	 * Returns winner with custom msgs
	 * 
	 * @return Winner or draw
	 */
	public String winner() {
		ArrayList<Integer> sum = new ArrayList<>();
		boolean[] busted = new boolean[this.playerHands.size()];
		
		for (int player : this.playerHands.keySet()) {
			sum.add(addCards(this.playerHands.get(player)));
			busted[player] = sum.get(player) > 21;
		}
		
		boolean bust = true;
		
		for (int p = 0; p < this.playerHands.size(); p++) {
			if (!busted[p]) {
				bust = false;
				break;
			}
		}
		
		String winners = "Winner(s): \n";
		if (bust) {
			return "All players busted. Draw";
		} else {
			int highest = 0;
			for (int p = 0; p < this.playerHands.size(); p++) {
				if (!busted[p] && highest < sum.get(p)) {
					highest = sum.get(p);
				}
			}
			for (int p = 0; p < this.playerHands.size(); p++) {
				if (sum.get(p) == highest) {
					winners += String.format("Player %d\n", p + 1);
				}
			}
		}
		
		return winners;
	}
	
	public static void main(String[] args) {
		int players = 4;
		
		Blackjack game = new Blackjack(players);
		Scanner scan = new Scanner(System.in);
		game.shuffle();
		game.dealAll(2);
		
		for (int p = 0; p < players; p++) {
			System.out.println(String.format("Player %d: ", p + 1));
			System.out.println(game.printHand(p));
		}
		System.out.println();
		
		int[] sums = new int[players];
		
		for (int p = 0; p < players; p++) {
			sums[p] = addCards(game.getHand(p));
			System.out.println(String.format("Player %d Sum: %d", p + 1, sums[p]));
		}
		System.out.println();
		
		int player = 0;
		boolean[] stay = new boolean[players];
		
		boolean gameover = false;
		
		while (!gameover) {
			if (!stay[player]) {
				System.out.println(String.format("Player %d's turn", player + 1));
				System.out.println("1 = hit, 2 = stay");
				int play = scan.nextInt();
				System.out.println();
				switch (play) {
					case 1:
						game.deal(1, player);
						break;
					case 2:
						System.out.println(String.format("Player %d stayed.", player + 1));
						stay[player] = true;
						break;
				}
				sums[player] = addCards(game.getHand(player));
				if (sums[player] >= 21) {
					stay[player] = true;
				}
				System.out.println(String.format("Player %d's hand: %s", player + 1, game.getHand(player)));
				System.out.println(String.format("Player %d's Sum: %d", player + 1, sums[player]));
				System.out.println();
			}
			player++;
			player %= players;
			
			gameover = true;
			for (int x = 0; x < players; x++) {
				if (stay[x] == false) {
					gameover = false;
					break;
				}
			}
		}
		
		System.out.println(game.winner());
		for (int p = 0; p < players; p++) {
			System.out.print(String.format("Player %d: ", p + 1));
			System.out.println(game.printHand(p));
		}
	}
}