import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * TicTacToe event listener
 * 
 * @author Andrew
 *
 */
public class TicTacToeEvent extends ListenerAdapter {
	
	private TicTacToe tttGame;
	private int player;
	private Member player1;
	private Member player2;
	private boolean gameStart;
	private Message msg;
	
	/**
	 * Initializes event
	 */
	public TicTacToeEvent() {
		this.tttGame = new TicTacToe();
		this.player = 0;
		this.gameStart = false;
	}
	
	/**
	 * Resets the game
	 */
	public void reset() {
		this.msg.clearReactions();
		this.tttGame.newGame();
		this.msg = null;
		this.player = 0;
		this.gameStart = false;
		this.player1 = null;
		this.player2 = null;
	}
	
	/**
	 * Checks winner, else returns player's turn
	 * 
	 * @return turn of player if no winner
	 */
	public String winCheck() {
		if (this.tttGame.winner().length() == 1) {
			String line = String.format("Winner is %s!",
				this.tttGame.winner().equals("x") ? this.player1.getEffectiveName() : this.player2.getEffectiveName());
			this.reset();
			return line;
		} else if (this.tttGame.winner().equals("draw")) {
			this.reset();
			return "It's a tie!";
		} else {
			return (String.format("%s's turn.",
				(this.player == 1 ? this.player1.getEffectiveName() : this.player2.getEffectiveName())));
		}
	}
	
	/**
	 * Timer for Tic Tac Toe Game
	 * 
	 * @param channel Which channel game is being played on
	 * @param minutes Minutes per game
	 */
	public void timer(MessageChannel channel, int minutes) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					channel.sendMessage(String.format("Game Over. %s took too long to play.",
						TicTacToeEvent.this.player == 0 ? TicTacToeEvent.this.player1.getEffectiveName()
							: TicTacToeEvent.this.player2.getEffectiveName()))
						.queue();
					TicTacToeEvent.this.reset();
				}
			},
			1000 * minutes * 60);
	}
	
	/**
	 * Event Handler
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		
		/**
		 * Gets the game
		 */
		if (content.startsWith("```\ntic tac toe") && event.getAuthor().isBot()) {
			this.msg = message;
			for (int x = 1; x <= 9; x++) {
				this.msg.addReaction(String.format("U+3%dU+fe0fU+20e3", x)).queue();
			}
		}
		
		/**
		 * Game starter
		 */
		if (content.startsWith("~~ttt") && !this.gameStart) {
			List<Member> players = message.getMentionedMembers();
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
			channel.sendMessage(this.tttGame.toString()).queue();
			
			this.player1 = objMember;
			this.player2 = players.get(0);
			this.gameStart = true;
			this.timer(channel, 5);
		} else if (content.split(" ")[0].equals("~~ttt") && this.gameStart) {
			channel.sendMessage("Game has already started").queue();
			return;
		}
		
		/**
		 * Quit cmd
		 */
		if (content.equals("~~tttquit") && this.gameStart &&
			(this.player1.getId().equals(objMember.getId()) ||
				this.player2.getId().equals(objMember.getId()))) {
			channel.sendMessage(String.format("Player %s has forfeited.", objMember.getEffectiveName())).queue();
			this.reset();
		} else if (content.equals("~~quit") && !this.gameStart) {
			channel.sendMessage("No game playing!").queue();
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
		if (this.gameStart && !event.getUser().isBot() && event.getChannel().equals(this.msg.getChannel())) {
			if ((this.player == 0 ? this.player1.getId() : this.player2.getId()).equals(event.getUser().getId())) {
				this.msg.editMessage(this.tttGame.toString() + "\n" + this.winCheck()).queue();
				this.player = (this.player + 1) % 2;
				event.getReaction().removeReaction().queue();
				event.getReaction().removeReaction(event.getUser()).queue();
			} else {
				event.getReaction().removeReaction(event.getUser()).queue();
			}
		}
	}
}