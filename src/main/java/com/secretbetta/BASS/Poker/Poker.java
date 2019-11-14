package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.google.api.client.util.ArrayMap;
import com.secretbetta.BASS.Cards.Card;
import com.secretbetta.BASS.Cards.Deck;

public class Poker {
	
	/* Main deck */
	private Deck deck;
	
	/* River */
	private ArrayList<Card> river;
	
	/* Player Hands */
	// private Map<Integer, ArrayList<Card>> playerHands;
	private ArrayList<ArrayList<Card>> playerHands;
	
	/* Player Bets */
	private int[] bets;
	
	/* Player money */
	private int[] money;
	
	int button = 0; // First player to play
	int bigblind = 50;
	int player = 0; // Which player's turn is it
	int turn = 0; // How many draws. 0 is "flop". 3 is last turn
	
	/**
	 * Initializes deck of cards with 52 cards (in sorted order)
	 */
	public Poker() {
		this.deck = newDeck();
		this.river = new ArrayList<>();
		this.playerHands = new ArrayList<>();
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
		this.money = new int[n];
		for (int x = 0; x < n; x++) {
			this.playerHands.add(new ArrayList<Card>());
			this.money[x] = 1000;
		}
	}
	
	/**
	 * Makes a new deck
	 * 
	 * @return New deck of cards in default order
	 */
	public static Deck newDeck() {
		Deck deck = new Deck();
		return deck;
	}
	
	/**
	 * Collects all cards from players to main deck
	 */
	public void collectCards() {
		for (ArrayList<Card> playercards : this.playerHands) {
			this.deck.addAll(playercards);
		}
	}
	
	public void dealRiver(int c) {
		for (int x = 0; x < c; x++) {
			this.river.add(this.deck.remove(0));
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
			this.playerHands.get(player).add(this.deck.remove(0));
		}
	}
	
	/*
	 * Deals n cards to all players
	 */
	public void dealAll(int n) {
		for (int player = 0; player < this.playerHands.size(); player++) {
			this.deal(n, player);
		}
	}
	
	public void raise() {
		
	}
	
	public void check() {
		
	}
	
	public String getHand(int player) {
		return this.playerHands.get(player).toString();
	}
	
	public String getRiver() {
		return this.river.toString();
	}
	
	/**
	 * Checks if there is a royal flush in hand
	 * 
	 * @param hand Hand to check
	 */
	public boolean isRoyalFlush(ArrayList<Card> hand) {
		ArrayList<Card> straight = this.getStraightFlush(hand);
		Collections.sort(straight);
		return straight.size() == 5 && straight.get(4).getCardNumber() == 14;
	}
	
	/**
	 * Gets straight flush from hand
	 * 
	 * @param hand Hand to check straight flush from
	 * @return True if straight flush exists, false if not
	 */
	public boolean isStraightFlush(ArrayList<Card> hand) {
		return this.getStraightFlush(hand).size() == 5;
	}
	
	/**
	 * Checks if hand has four of a kind
	 * 
	 * @param hand
	 * @return
	 */
	public boolean isFourOfAKind(ArrayList<Card> hand) {
		return this.getFour(hand).size() == 4;
	}
	
	/**
	 * Checks if hand is a full house
	 * 
	 * @param hand Hand to check
	 * @return True if full house is present, false if not
	 */
	public boolean isFullHouse(ArrayList<Card> hand) {
		hand.removeAll(this.getThree(hand));
		return this.isPair(hand);
	}
	
	/**
	 * Checks if there is a flush in hand
	 * 
	 * @param hand Hand to check
	 * @return True if there is a flush, false if not
	 */
	public boolean isFlush(ArrayList<Card> hand) {
		return this.getHighestFlush(hand).size() == 5;
	}
	
	/**
	 * Checks if there is a straight of 5 cards that exists in hand
	 * 
	 * @param hand Hand to check
	 * @return True if a straight exists, false if not
	 */
	public boolean isStraight(ArrayList<Card> hand) {
		return this.getStraight(hand).size() == 5;
	}
	
	/**
	 * Checks if three of a kind is present
	 * 
	 * @param hand Hand to check
	 * @return true if there exists a three of a kind, false if not
	 */
	public boolean isThreeOfAKind(ArrayList<Card> hand) {
		return this.getThree(hand).size() >= 1;
	}
	
	/**
	 * Sees if there is at least one pair in hand
	 * 
	 * @param hand Hand to check
	 * @return True if there is at least one pair
	 */
	public boolean isPair(ArrayList<Card> hand) {
		return this.getPairs(hand).size() >= 1;
	}
	
	/**
	 * Sees if there are 2 or more pairs in hand
	 * 
	 * @param hand Hand to check
	 * @return True if there are two or more pairs
	 */
	public boolean isTwoPairs(ArrayList<Card> hand) {
		return this.getPairs(hand).size() >= 2;
	}
	
	/**
	 * Gets straight flush from hand
	 * 
	 * @param hand Hand to get straight flush from
	 * @return Straight flush from hand
	 */
	public ArrayList<Card> getStraightFlush(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		Collections.reverse(hand);
		
		ArrayList<ArrayList<Card>> hands = new ArrayList<>();
		ArrayList<Card> temp = new ArrayList<>();
		for (int s = 4; s >= 1; s--) {
			for (Card card : hand) {
				if (card.getCardSuit() == s) {
					temp.add(card);
				}
			}
			hands.add(temp);
			temp = new ArrayList<>();
		}
		int max = 0;
		Card card = hands.get(0).get(0);
		for (int c = 0; c < hands.size(); c++) {
			if (Collections.max(hands.get(c)).compareTo(card) >= 0 && this.isStraight(hands.get(c))) {
				card = Collections.max(hands.get(c));
				max = c;
			}
		}
		
		return this.getStraight(hands.get(max));
	}
	
	/**
	 * Get fours of a kind from hand
	 * 
	 * @param hand
	 * @return
	 */
	public ArrayList<Card> getFour(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		Collections.reverse(hand);
		
		ArrayList<Card> four = new ArrayList<>();
		for (int c = 0; c < hand.size() - 2; c++) {
			if (hand.get(c).getCardNumber() == hand.get(c + 1).getCardNumber()
				&& hand.get(c + 1).getCardNumber() == hand.get(c + 2).getCardNumber()
				&& hand.get(c + 2).getCardNumber() == hand.get(c + 3).getCardNumber()) {
				for (int x = c; x < c + 4; x++) {
					four.add(hand.get(x));
				}
				return four;
			}
		}
		
		return four;
	}
	
	/**
	 * Gets full house from cards
	 * 
	 * @param hand
	 * @return
	 */
	public ArrayList<Card> getFullHouse(ArrayList<Card> hand) {
		hand.addAll(this.river);
		ArrayList<Card> fullhouse = new ArrayList<>();
		
		fullhouse.addAll(this.getThree(hand));
		hand.removeAll(fullhouse);
		
		ArrayMap<Card, Card> pairs = this.getPairs(hand);
		fullhouse.add(pairs.getKey(pairs.size() - 1));
		fullhouse.add(pairs.get(pairs.getKey(pairs.size() - 1)));
		return fullhouse;
	}
	
	/**
	 * Gets highest flush in hand
	 * 
	 * @param hand Hand to get flush from
	 * @return 5 Cards of highest flush
	 */
	public ArrayList<Card> getHighestFlush(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		Collections.reverse(hand);
		
		ArrayList<Card> flush = new ArrayList<>();
		int[] suits = new int[4];
		
		for (Card card : hand) {
			suits[card.getCardSuit() - 1]++;
		}
		
		for (int x = 3; x >= 0; x--) {
			if (suits[x] >= 5) {
				int count = 5;
				for (Card card : hand) {
					if (card.getCardSuit() == x + 1 && count > 0) {
						flush.add(card);
						count--;
					}
				}
				return flush;
			}
		}
		
		return new ArrayList<>();
	}
	
	/**
	 * Gets highest straight in hand
	 * 
	 * @param hand Hand to get straight from
	 * @return Highest straight of 5 cards
	 */
	public ArrayList<Card> getStraight(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		Collections.reverse(hand);
		
		ArrayList<Card> straight = new ArrayList<>();
		
		straight.add(hand.get(0));
		for (int c = 0; c < hand.size() - 1; c++) {
			if (hand.get(c).getCardNumber() - 1 == hand.get(c + 1).getCardNumber()) {
				straight.add(hand.get(c + 1));
			} else if (hand.get(c).getCardNumber() != hand.get(c + 1).getCardNumber()) {
				straight = new ArrayList<>();
				straight.add(hand.get(c));
			}
			if (straight.size() == 5) {
				return straight;
			}
		}
		
		return new ArrayList<>();
	}
	
	/**
	 * Finds highest three of a kind and returns it
	 * 
	 * @param hand
	 * @return
	 */
	public ArrayList<Card> getThree(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		Collections.reverse(hand);
		
		ArrayList<Card> three = new ArrayList<>();
		for (int c = 0; c < hand.size() - 2; c++) {
			if (hand.get(c).getCardNumber() == hand.get(c + 1).getCardNumber()
				&& hand.get(c + 1).getCardNumber() == hand.get(c + 2).getCardNumber()) {
				for (int x = c; x < c + 3; x++) {
					three.add(hand.get(x));
				}
				c += 2;
				return three;
			}
		}
		
		return three;
	}
	
	/**
	 * Gets all pairs in hand
	 * 
	 * @param hand Hand to get pairs from
	 * @return All pairs in hand
	 */
	public ArrayMap<Card, Card> getPairs(ArrayList<Card> hand) {
		hand.addAll(this.river);
		Collections.sort(hand);
		ArrayMap<Card, Card> pairs = new ArrayMap<>();
		
		for (int c = 0; c < hand.size() - 1; c++) {
			if (hand.get(c).getCardNumber() == hand.get(c + 1).getCardNumber()) {
				pairs.put(hand.get(c), hand.get(c + 1));
				c++;
			}
		}
		
		return pairs;
	}
	
	/**
	 * Gets highest card in hand
	 * 
	 * @param hand Hand to use
	 * @return Highest singular card
	 */
	public Card getHighCard(ArrayList<Card> hand) {
		return Collections.max(hand);
	}
	
	public String getPlayerInfo(int player) {
		String info = "";
		info += String.format("Player %d\n"
			+ "Cards: %s\n"
			+ "Balance: %d\n", player + 1, this.playerHands.get(player), this.money[player]);
		return info;
	}
	
	@Override
	public String toString() {
		return this.deck.toString();
	}
	
	public static void main(String[] args) {
		Poker game = new Poker(2);
		game.deck.shuffle();
		System.out.println(game + "\n");
		game.dealAll(2);
		System.out.println(game.getPlayerInfo(0));
		System.out.println(game.getPlayerInfo(1));
		game.dealRiver(3);
		System.out.println("River: " + game.getRiver());
		
		Scanner scan = new Scanner(System.in);
		
		int choice = 0; // Raise, check, fold. check if current bet != raised bet
		int raise = 0;
		while (true) {
			
		}
	}
}
