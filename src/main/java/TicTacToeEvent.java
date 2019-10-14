import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicTacToeEvent extends ListenerAdapter {
	public TicTacToe tttGame;
	public int player;
	public Member player1;
	public Member player2;
	public boolean gameStart;
	
	public TicTacToeEvent() {
		this.tttGame = new TicTacToe();
		player = 0;
		this.gameStart = false;
	}
	
	public void reset() {
		this.tttGame.newGame();
		this.player = 0;
		this.gameStart = false;
		this.player1 = null;
		this.player2 = null;
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
			channel.sendMessage(String.format("Player 1: %s\nPlayer 2: %s", 
					event.getAuthor().getAsMention(), players.get(0))).queue();
			channel.sendMessage(tttGame.toString()).queue();
			channel.sendMessage(String.format("%s's turn. Type ~~move <num> ", 
					event.getMember().getNickname())).queue();
			
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
		if (content.startsWith("~~move") && this.gameStart && (this.player == 0 ? 
				this.player1.getId() : 
				this.player2.getId()).equals(objMember.getId())) {
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
					
					if (this.tttGame.winner().length() == 1) {
						channel.sendMessage(String.format("Winner is %s!",
								this.tttGame.winner().equals("x") ? 
									this.player1.getNickname() : 
									this.player2.getNickname())).queue();
						reset();
					} else {
						channel.sendMessage(String.format("%s's turn. Type ~~move <num> ", 
								(this.player == 0 ? 
									this.player1.getNickname() : 
									this.player2.getNickname()))).queue();
					}
				} else {
					channel.sendMessage("Invalid input. Enter a number from 1-9 inclusive").queue();
				}
			} catch (NumberFormatException e) {
				channel.sendMessage("Invalid input. Enter a number from 1-9 inclusive").queue();
			}
		} else if (content.startsWith("~~move") && this.gameStart && !(this.player == 0 ? 
				this.player1.getId() : 
				this.player2.getId()).equals(objMember.getId())) {
			channel.sendMessage("It's not your turn!").queue();
		}
	}
	
	
}
