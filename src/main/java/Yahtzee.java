import java.awt.Color;
import java.util.Arrays;
import java.util.TreeSet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

/**
 * Yahtzee game
 * Supports singleplayer and multiplayer //TODO WIP
 * 
 * @author Andrew
 */
public class Yahtzee {
	
	private User player1;
	private User player2;
	private int round; // 0-13
	private int roll; // 0-3
	private boolean[] diceChoice; // Which dice to store
	private int[] dice; // 5 Dice
	
	/**
	 * Field
	 * -Upper Section
	 * 0 - Count 1's
	 * 1 - Count 2's
	 * 2 - Count 3's
	 * 3 - Count 4's
	 * 4 - Count 5's
	 * 5 - Count 6's
	 * If upper section >= 63. Bonus 35 pts
	 * \n
	 * -Lower section
	 * 6 - 3 of a kind
	 * 7 - 4 of a kind
	 * 8 Yahtzee = 50
	 * 9 - Full house = 25
	 * 10 - Low Straight = 30
	 * 11- High Straight = 40
	 * 12- Chance = Total of all dice
	 * Yahtzee bonus = 100
	 * -Totals
	 * Total of lower section
	 * Total of upper section
	 * Grand total
	 */
	private int[] scores;
	private boolean[] scoresChoice;
	
	public Yahtzee() {
		this.newGame();
	}
	
	/**
	 * Makes a new game, resets everything
	 */
	public void newGame() {
		this.round = 0;
		this.roll = 0;
		this.diceChoice = new boolean[5];
		this.dice = new int[5];
		this.scores = new int[14];
		this.scoresChoice = new boolean[14];
	}
	
	public void changeDie() {
		this.dice = new int[] { 3, 3, 3, 3, 3 };
	}
	
	/**
	 * Choose which dice to not roll
	 * 
	 * @param nums String of ints to not roll
	 */
	public void chooseDie(String nums) {
		if (nums.length() > 5) {
			return;
		}
		for (char c : nums.toCharArray()) {
			this.diceChoice[Character.getNumericValue(c)] = true;
		}
	}
	
	/**
	 * Rolls dice/die
	 */
	public void roll() {
		for (int x = 0; x < 5; x++) {
			this.dice[x] = (int) (Math.random() * 6 + 1);
		}
		this.diceChoice = new boolean[5];
	}
	
	/**
	 * Makes a move at the choice c
	 * 
	 * @param c Choice
	 * @return True if valid move, false if invalid
	 */
	public boolean move(int c) {
		int points = 0;
		switch (c) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5: // Counts dice only with c+1 numbers on it
				for (int p : this.dice) {
					if (p == c + 1) {
						points += p;
					}
				}
				break;
			case 6:
			case 7:
			case 8: // Checks to see if there is a 3, 4, or 5 of a kind for valid choice
				boolean flag = false;
				int[] kinds = new int[6];
				for (int p : this.dice) {
					kinds[p - 1]++;
					if (kinds[p - 1] >= c - 3) {
						flag = true;
					}
				}
				if (!flag) {
					return false;
				}
				if (c == 8) {
					points = 50;
					break;
				}
			case 12: // Chance
				for (int p : this.dice) {
					points += p;
				}
				break;
			case 9: // Full House
				Arrays.sort(this.dice);
				if ((this.dice[0] == this.dice[1] && this.dice[1] == this.dice[2] && this.dice[3] == this.dice[4])
					|| (this.dice[0] == this.dice[1] && this.dice[2] == this.dice[3] && this.dice[3] == this.dice[4])) {
					points = 25;
				}
				break;
			case 10:
			case 11: // Small Straight or Large Straight
				TreeSet<Integer> dices = new TreeSet<>();
				for (int p : this.dice) {
					dices.add(p);
				}
				flag = false;
				if (dices.size() >= c - 6) {
					for (int p : dices.headSet(dices.last())) {
						if (dices.higher(p) != p + 1) {
							flag = false;
							break;
						} else {
							flag = true;
						}
					}
				}
				if (!flag) {
					return false;
				}
				if (c == 10) {
					points = 30;
				} else {
					points = 40;
				}
				break;
		}
		return move(c, points);
	}
	
	public int[] die() {
		return this.dice;
	}
	
	/**
	 * Helper method for {@link #move(int)}
	 * 
	 * @param c      Choice
	 * @param points Points to assign
	 * @return True if valid move, false if invalid
	 */
	private boolean move(int c, int points) {
		if (!this.scoresChoice[c]) {
			this.scores[c] = points;
			return (this.scoresChoice[c] = true);
		}
		
		return false;
	}
	
	/**
	 * Gets scoresheet in EmbedBuilder type
	 * 
	 * @return Scoresheet
	 */
	public EmbedBuilder getScoresheet() {
		EmbedBuilder scoresheet = new EmbedBuilder();
		scoresheet.setTitle("Scoresheet");
		scoresheet.setColor(Color.yellow);
		scoresheet.addField("Upper Section", String.format("```css\n"
			+ "One's: \t\t\t\t\t%d\n"
			+ "Two's: \t\t\t\t\t%d\n"
			+ "Three's:   \t\t\t\t%d\n"
			+ "Four's:\t\t\t\t\t%d\n"
			+ "Five's:\t\t\t\t\t%d\n"
			+ "Six's: \t\t\t\t\t%d\n"
			+ "Upper Bonus:   \t\t\t%d\n"
			+ "```",
			scores[0],
			scores[1],
			scores[2],
			scores[3],
			scores[4],
			scores[5],
			35), false);
		scoresheet.addField("Lower Section", String.format("```css\n"
			+ "Three of a kind:   \t\t%2d\n"
			+ "Four of a kind:\t\t\t%2d\n"
			+ "Full house:    \t\t\t%2d\n"
			+ "Low Straight:  \t\t\t%2d\n"
			+ "High Straight: \t\t\t%2d\n"
			+ "Yahtzee:   \t\t\t\t%2d\n"
			+ "Chance:\t\t\t\t\t%2d\n"
			+ "Yahtzee Bonus: \t\t\t%3d\n"
			+ "```",
			scores[6],
			scores[7],
			scores[9],
			scores[10],
			scores[11],
			scores[8],
			scores[12],
			100), false);
		scoresheet.addField("Totals", String.format("```css\n"
			+ "Total of lower section:\t%3d\n"
			+ "Total of upper section:\t%3d\n"
			+ "Grand total:   \t\t\t%3d\n"
			+ "```",
			100,
			100,
			100), false);
		return scoresheet;
	}
	
	public static void main(String[] args) {
		Yahtzee game = new Yahtzee();
		System.out.println(game.getScoresheet().toString());
	}
	
}
