import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * TicTacToe event listener
 * @author Andrew
 *
 */
public class TicTacToeEvent extends ListenerAdapter {
	private TicTacToe tttGame;
	private int player;
	private Member player1;
	private Member player2;
	private boolean gameStart;
	
	/**
	 * Initializes event
	 */
	public TicTacToeEvent() {
		this.tttGame = new TicTacToe();
		player = 0;
		this.gameStart = false;
	}
	
	/**
	 * Resets the game
	 */
	public void reset() {
		this.tttGame.newGame();
		this.player = 0;
		this.gameStart = false;
		this.player1 = null;
		this.player2 = null;
	}
	
	/**
	 * Checks winner, else returns player's turn
	 * @return turn of player if no winner
	 */
	public String winCheck() {
		if (this.tttGame.winner().length() == 1) {
			String line = String.format("Winner is %s!",
					this.tttGame.winner().equals("x") ? 
							this.player1.getEffectiveName() : 
							this.player2.getEffectiveName());
			this.reset();
			return line;
		} else if (this.tttGame.winner().equals("draw")) {
			this.reset();
			return "It's a tie!";
		} else {
			return (String.format("%s's turn. Type ~~move <num> ", 
					(this.player == 0 ? 
						this.player1.getEffectiveName() : 
						this.player2.getEffectiveName())));
		}
	}
	
	/**
	 * Event Handler
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		List<Member> players = message.getMentionedMembers();
		
		/**
		 * Game starter
		 */
		if (content.startsWith("~~ttt") && !this.gameStart) {
			if (players.size() > 1) {
				channel.sendMessage("Too many players, only mention 1 player").queue();
				return;
			} else if (players.size() == 0) {
				channel.sendMessage("type ~~ttt <@mention> play against them").queue();
			} else if (players.get(0).getUser().isBot()) {
				channel.sendMessage("Cannot play against a bot").queue();
				return;
			} else if (players.get(0).getId().equals(objMember.getId())) {
				channel.sendMessage("I know you're lonely and all... "
						+ "but you can't play against yourself...").queue();
				return;
			}
			channel.sendMessage("Game will end in 5 minutes").queue();
			channel.sendMessage(String.format("Player 1: %s\nPlayer 2: %s", 
					event.getAuthor().getAsMention(), players.get(0))).queue();
			channel.sendMessage(tttGame.toString()).queue();
			channel.sendMessage(String.format("%s's turn. Type ~~move <num> ", 
					event.getMember().getEffectiveName())).queue();
			
			this.player1 = objMember;
			this.player2 = players.get(0);
			this.gameStart = true;
		} else if (content.startsWith("~~ttt") && this.gameStart) {
			channel.sendMessage("Game has already started").queue();
			return;
		}
		
		/**
		 * Move input
		 */
		if (content.startsWith("~~move") && this.gameStart && 
				(this.player == 0 ? 
				this.player1.getId() : this.player2.getId())
				.equals(objMember.getId())) {
			try {
				if (content.split(" ").length > 1 && 
						Integer.parseInt(content.split(" ")[1], 10)-1 < 9 &&
						Integer.parseInt(content.split(" ")[1], 10)-1 >= 0) {
					if (tttGame.move(Integer.parseInt(content.split(" ")[1])-1, 
							(this.player == 0 ? 'x' : 'o'))) {
						this.player = (this.player + 1) % 2;
						channel.sendMessage(tttGame.toString()).queue();
					} else if (!tttGame.move(Integer.parseInt(content.split(" ")[1])-1, 
							(this.player == 0 ? 'x' : 'o'))) {
						channel.sendMessage("Cannot place in this square! Try again.").queue();
					}
					
					/**
					 * Win check and resets game
					 */
					channel.sendMessage(winCheck()).queue();
				} else {
					channel.sendMessage("Invalid input. Enter a number from 1-9 inclusive").queue();
					channel.sendMessage(tttGame.toString()).queue();
				}
			} catch (NumberFormatException e) {
				channel.sendMessage("Invalid input. Enter a number from 1-9 inclusive").queue();
				channel.sendMessage(tttGame.toString()).queue();
			}
		} else if (content.startsWith("~~move") && this.gameStart && !(this.player == 0 ? 
				this.player1.getId() : 
				this.player2.getId()).equals(objMember.getId())) {
			channel.sendMessage("It's not your turn!").queue();
		} else if (content.startsWith("~~move") && !this.gameStart) {
			channel.sendMessage("No game playing!").queue();
		}
		
		/**
		 * Quit cmd
		 */
		if (content.equals("~~quit") && this.gameStart && 
				(this.player1.getId().equals(objMember.getId()) || 
				this.player2.getId().equals(objMember.getId()))) {
			channel.sendMessage(String.format("Player %s has forfeited.", objMember.getEffectiveName())).queue();
			reset();
		} else if (content.equals("~~quit") && !this.gameStart) {
			channel.sendMessage("No game playing!").queue();
		}
		
		/**
		 * 5 minutes per game
		 */
		if (this.gameStart) {
			new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						channel.sendMessage(String.format("Game Over. %s took too long to play.", 
								player == 0 ? 
								player1.getEffectiveName() : 
								player2.getEffectiveName())).queue();
						reset();
					}
				},
				1000 * 5 * 60
			);
		}
	}
}