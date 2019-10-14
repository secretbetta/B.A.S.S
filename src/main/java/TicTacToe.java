/**
 * TicTacToe Game Class
 * @author Andrew
 *
 */
public class TicTacToe {
	/* Game board */
	private char[][] board;
	
	/**
	 * Initializes TicTacToe board
	 */
	public TicTacToe() {
		board = new char[3][3];
	}
	
	/**
	 * The move maker method
	 * 
	 * @param x coord x
	 * @param y coord y
	 * @param player x, o
	 * @return
	 */
	public boolean move(int x, int y, char player) {
		if (this.board[x][y] == '\0') {
			this.board[x][y] = player;
			return true;
		}
		return false;
	}
	
	/**
	 * Creates a new board
	 */
	public void newGame() {
		this.board = new char[3][3];
	}
	
	/**
	 * Checks for winner, draw, or game is still on going
	 * @return "x" or "o" for winner, none for on going game, draw for a tie
	 */
	public String winner() {
		for (int x = 0; x < 8; x++) {
			String line = null;
			switch (x) {
				case 0: 
					line = "" + this.board[0][0] + this.board[0][1] + this.board[0][2];
					break;
				case 1:
					line = "" + this.board[1][0] + this.board[1][1] + this.board[1][2];
					break;
				case 2:
					line = "" + this.board[2][0] + this.board[2][1] + this.board[2][2];
					break;
				case 3:
					line = "" + this.board[0][0] + this.board[1][0] + this.board[2][0];
					break;
				case 4:
					line = "" + this.board[0][1] + this.board[1][1] + this.board[2][1];
					break;
				case 5:
					line = "" + this.board[0][2] + this.board[1][2] + this.board[2][2];
					break;
				case 6:
					line = "" + this.board[0][0] + this.board[1][1] + this.board[2][2];
					break;
				case 7:
					line = "" + this.board[0][2] + this.board[1][1] + this.board[2][0];
					break;
			}
			
			if (line.equals("xxx")) {
				return "x";
			} else if (line.equals("ooo")) {
				return "o";
			}
		}
		
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (this.board[r][c] == '\0') {
					return "none";
				}
			}
		}
		
		return "draw";
	}
	
	/**
	 * To String of tic tac toe board
	 */
	public String toString() {
		String content = "";
		for (int x = 0; x < board.length; x++) {
			for (int y = 0; y < board.length; y++) {
				content += String.format("[%c]", board[x][y]);
			}
			content += "\n";
		}
		return content;
	}
}
