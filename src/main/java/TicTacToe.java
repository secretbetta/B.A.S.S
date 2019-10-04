/**
 * TicTacToe Game Class
 * @author Andrew
 *
 */
public class TicTacToe {
	
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
		if (board[x][y] == 0) {
			board[x][y] = player;
			return true;
		}
		return false;
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

	public static void main(String[] args) {
		
	}

}
