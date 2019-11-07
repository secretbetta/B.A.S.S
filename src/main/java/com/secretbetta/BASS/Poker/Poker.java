package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.google.api.client.util.ArrayMap;
import com.secretbetta.BASS.Cards.Card;
import com.secretbetta.BASS.Cards.Deck;

public class Poker {
	
	/* Main deck */
	private Deck deck;
	
	/* River */
	private ArrayList<Card> river;
	
	/* Player Hands */
	private Map<Integer, ArrayList<Card>> playerHands;
	
	/* Player Bets */
	private int[] bets;
	
	/* Player money */
	private int[] money;
	
	/**
	 * Initializes deck of cards with 52 cards (in sorted order)
	 */
	public Poker() {
		this.deck = newDeck();
		this.river = new ArrayList<>();
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
			this.playerHands.put(x, new ArrayList<Card>());
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
		for (ArrayList<Card> playercards : this.playerHands.values()) {
			// this.deck.addAll(playercards);
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
			this.playerHands.get(player).add(this.deck.remove((int) Math.random() * this.deck.size()));
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
	 * Checks if there is a straight of 5 cards that exists in hand
	 * 
	 * @param hand Hand to check
	 * @return True if a straight exists, false if not
	 */
	public boolean isStraight(ArrayList<Card> hand) {
		return this.getStraight(hand).size() == 5;
	}
	
	/**
	 * Gets highest straight in hand
	 * 
	 * @param hand Hand to get straight from
	 * @return Highest straight of 5 cards
	 */
	public ArrayList<Card> getStraight(ArrayList<Card> hand) {
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
	 * Checks if three of a kind is present
	 * 
	 * @param hand Hand to check
	 * @return true if there exists a three of a kind, false if not
	 */
	public boolean isThreeOfAKind(ArrayList<Card> hand) {
		return this.getThree(hand).size() >= 1;
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
	 * Sees if there are 2 or more pairs in hand
	 * 
	 * @param hand Hand to check
	 * @return True if there are two or more pairs
	 */
	public boolean isTwoPairs(ArrayList<Card> hand) {
		return this.getPairs(hand).size() >= 2;
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
	
	@Override
	public String toString() {
		return this.deck.toString();
	}
	
	public static void main(String[] args) {
		Poker game = new Poker();
		game.deck.shuffle();
		System.out.println(game);
		game.deck.sort();
		System.out.println(game); // Might make card and deck classes
		
		ArrayList<Card> hand = new ArrayList<>();
		
		for (int c = 0; c < 20; c++) {
			hand.add(game.deck.remove(c));
		}
		System.out.println(hand);
		System.out.println(game.getHighestFlush(hand));
	}
}
