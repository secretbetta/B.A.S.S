/**
 * TicTacToe Game Class
 * 
 * @author Andrew
 */
public class TicTacToe {
	
	/* Game board */
	private char[] board;
	
	/**
	 * Initializes TicTacToe board
	 */
	public TicTacToe() {
		this.board = new char[9];
	}
	
	/**
	 * The move maker method
	 * 
	 * @param x      coord x
	 * @param y      coord y
	 * @param player x, o
	 * @return
	 */
	public boolean move(int move, char player) {
		if (this.board[move] == '\0') {
			this.board[move] = player;
			return true;
		}
		return false;
	}
	
	/**
	 * Creates a new board
	 */
	public void newGame() {
		this.board = new char[9];
	}
	
	/**
	 * Checks for winner, draw, or game is still on going
	 * 
	 * @return "x" or "o" for winner, none for on going game, draw for a tie
	 */
	public String winner() {
		for (int x = 0; x < 8; x++) {
			String line = null;
			switch (x) {
				case 0:
					line = "" + this.board[0] + this.board[1] + this.board[2];
					break;
				case 1:
					line = "" + this.board[3] + this.board[4] + this.board[5];
					break;
				case 2:
					line = "" + this.board[6] + this.board[7] + this.board[8];
					break;
				case 3:
					line = "" + this.board[0] + this.board[3] + this.board[6];
					break;
				case 4:
					line = "" + this.board[1] + this.board[4] + this.board[7];
					break;
				case 5:
					line = "" + this.board[2] + this.board[5] + this.board[8];
					break;
				case 6:
					line = "" + this.board[0] + this.board[4] + this.board[8];
					break;
				case 7:
					line = "" + this.board[2] + this.board[4] + this.board[6];
					break;
			}
			
			if (line.equals("xxx")) {
				return "x";
			} else if (line.equals("ooo")) {
				return "o";
			}
		}
		
		for (int r = 0; r < 9; r++) {
			if (this.board[r] == '\0') {
				return "none";
			}
		}
		
		return "draw";
	}
	
	/**
	 * To String of tic tac toe board
	 */
	@Override
	public String toString() {
		String content = "```\nTic Tac Toe Game\n";
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				content += String.format("[%c]", (this.board[x * 3 + y] != '\0') ? this.board[x * 3 + y] : ' ');
			}
			content += "\n";
		}
		return content + "```";
	}
}