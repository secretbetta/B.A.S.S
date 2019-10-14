import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicTacToeEvent extends ListenerAdapter {
	public TicTacToe tttGame;
	public int player;
	public boolean gameStart;
	
	public TicTacToeEvent() {
		this.tttGame = new TicTacToe();
		this.player = 0;
		this.gameStart = true;
	}
	
	/**
	 * Event Handler
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		
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
			} 
//			else if (players.get(0).getId().equals(objMember.getId())) {
//				channel.sendMessage("I know you're lonely and all... but you can't play against yourself...").queue();
//				return;
//			}
			channel.sendMessage(String.format("Player 1: %s\nPlayer 2: %s", event.getAuthor().getAsMention(), players.get(0))).queue();
			channel.sendMessage(tttGame.toString()).queue();
		} else if (this.gameStart) {
			channel.sendMessage("Game has already started").queue();
			return;
		}
		
		if (content.startsWith("~~move")) {
			channel.sendMessage(String.format("Player #%d's turn", this.player)).queue();
			this.player = (this.player + 1) % 2;
		}
	}
}
