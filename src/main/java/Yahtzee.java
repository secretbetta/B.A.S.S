import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

/**
 * Yahtzee game
 * Multiplayer
 * 
 * @author Andrew
 */
public class Yahtzee {
	
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
		this.diceChoice = new boolean[5];
		this.dice = new int[5];
		this.scores = new int[13];
		this.scoresChoice = new boolean[13];
	}
	
	/**
	 * Testing purposes
	 */
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
	 * Rolls dice
	 * 
	 * @return true if rolled, false if cannot roll
	 */
	public boolean roll() {
		boolean flag = false;
		for (boolean d : this.diceChoice) {
			if (!d) {
				flag = true;
				break;
			}
		}
		if (!flag) {
			return false;
		}
		for (int x = 0; x < 5; x++) {
			this.dice[x] = (int) (Math.random() * 6 + 1);
		}
		this.diceChoice = new boolean[5];
		return true;
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
			System.out.println("Points " + this.scores[c]);
			return (this.scoresChoice[c] = true);
		}
		
		return false;
	}
	
	/**
	 * Gets dice
	 * 
	 * @return String of dice in [][][][][] format
	 */
	public String getDice() {
		String dice = "";
		for (int d : this.dice) {
			dice += String.format("[%d]", d);
		}
		return dice;
	}
	
	/**
	 * Gets scoresheet in EmbedBuilder type
	 * 
	 * @return Scoresheet
	 */
	public EmbedBuilder getScoresheet() {
		int lower = 0;
		int upper = 0;
		
		for (int x = 0; x < 6; x++) {
			lower += this.scores[x];
		}
		for (int x = 6; x < 13; x++) {
			upper += this.scores[x];
		}
		
		EmbedBuilder scoresheet = new EmbedBuilder();
		scoresheet.setTitle("Scoresheet");
		scoresheet.setColor(Color.yellow);
		scoresheet.addField("Upper Section", String.format("```css\n"
			+ "[A] One's: \t\t\t\t\t%d\n"
			+ "[B] Two's: \t\t\t\t\t%d\n"
			+ "[C] Three's:   \t\t\t\t%d\n"
			+ "[D] Four's:\t\t\t\t\t%d\n"
			+ "[E] Five's:\t\t\t\t\t%d\n"
			+ "[F] Six's: \t\t\t\t\t%d\n"
			+ "Upper Bonus:   \t\t\t\t%d\n"
			+ "```",
			this.scores[0],
			this.scores[1],
			this.scores[2],
			this.scores[3],
			this.scores[4],
			this.scores[5],
			lower >= 63 ? 35 : 0), false);
		scoresheet.addField("Lower Section", String.format("```css\n"
			+ "[G] Three of a kind:   \t\t%2d\n"
			+ "[H] Four of a kind:\t\t\t%2d\n"
			+ "[I] Full house:    \t\t\t%2d\n"
			+ "[J] Low Straight:  \t\t\t%2d\n"
			+ "[K] High Straight: \t\t\t%2d\n"
			+ "[L] Yahtzee:   \t\t\t\t%2d\n"
			+ "[M] Chance:\t\t\t\t\t%2d\n"
			+ "Yahtzee Bonus: \t\t\t\t%3d\n"
			+ "```",
			this.scores[6],
			this.scores[7],
			this.scores[9],
			this.scores[10],
			this.scores[11],
			this.scores[8],
			this.scores[12],
			100), false);
		
		scoresheet.addField("Totals", String.format("```css\n"
			+ "Total of lower section:\t\t%3d\n"
			+ "Total of upper section:\t\t%3d\n"
			+ "Grand total:   \t\t\t\t%3d\n"
			+ "```",
			lower,
			upper,
			lower + upper), false);
		return scoresheet;
	}
	
	public static void main(String[] args) {
		Yahtzee game1 = new Yahtzee();
		Yahtzee game2 = new Yahtzee();
		Scanner scan = new Scanner(System.in);
		
		int round = 13;
		
		while (round > 0) {
			
			int t = 1;
			System.out.println("------------------------");
			System.out.println("Player 1\nTurn number: " + t);
			System.out.println("Rolling dice...");
			game1.roll();
			System.out.println("Your dice are...");
			System.out.println(game1.getDice());
			for (int turn = 1; turn <= 3; turn++) {
				// 1-13 = put on scoresheet (A-M for emojis)
				// 0 to roll (N for emoji)
				int choice = scan.nextInt();
				if (turn == 3) {
					while (choice == 0) {
						System.out.println("No more turns. You must choose a score");
						choice = scan.nextInt();
					}
				}
				/**
				 * Actual game in Yahtzee Event:
				 * Emojis = choice
				 * if player enters ~~
				 */
				if (choice == 0) {
					game1.roll();
					System.out.println("\n------------------------");
					System.out.println("Turn number: " + (t + turn));
					System.out.println("Your dice are...");
					System.out.println(game1.getDice());
				} else if (choice >= 1 && choice <= 13) {
					if (game1.move(choice - 1)) {
						turn = 4;
						System.out.println("Your score is...");
						List<Field> scoresheet1 = game1.getScoresheet().getFields();
						for (Field score : scoresheet1) {
							System.out.println(score.getName());
							System.out.println(score.getValue());
						}
					} else {
						turn--;
						System.out.println("You have already made a move here,"
							+ " cannot enter score here. Please choose another score");
					}
				} else {
					System.out.println("Invalid move. "
						+ "Please enter 1-13 for score on scoresheet or 0 to roll dice.");
					turn--;
				}
			}
			
			System.out.println();
			System.out.println("------------------------");
			System.out.println("Player 2\nTurn number: " + t);
			System.out.println("Rolling dice...");
			game2.roll();
			System.out.println("Your dice are...");
			System.out.println(game2.getDice());
			for (int turn = 1; turn <= 3; turn++) {
				// 1-13 = put on scoresheet (A-M for emojis)
				// 0 to roll (N for emoji)
				int choice = scan.nextInt();
				if (turn == 3) {
					while (choice == 0) {
						System.out.println("No more turns. You must choose a score");
						choice = scan.nextInt();
					}
				}
				if (choice == 0) {
					game2.roll();
					System.out.println("\n------------------------");
					System.out.println("Turn number: " + (t + turn));
					System.out.println("Your dice are...");
					System.out.println(game2.getDice());
				} else if (choice >= 1 && choice <= 13) {
					if (game2.move(choice - 1)) {
						turn = 4;
						System.out.println("Your score is...");
						List<Field> scoresheet2 = game2.getScoresheet().getFields();
						for (Field score : scoresheet2) {
							System.out.println(score.getName());
							System.out.println(score.getValue());
						}
					} else {
						turn--;
						System.out.println("You have already made a move here,"
							+ " cannot enter score here. Please choose another score");
					}
				} else {
					System.out.println("Invalid move. "
						+ "Please enter 1-13 for score on scoresheet or 0 to roll dice.");
					turn--;
				}
			}
			round--;
		}
		
		scan.close();
		
	}
}
