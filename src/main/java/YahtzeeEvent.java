import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class YahtzeeEvent extends ListenerAdapter {
	
	private Yahtzee game1;
	private Yahtzee game2;
	
	private User player1;
	private User player2;
	
	private int player;
	
	private boolean game;
	
	public YahtzeeEvent() {
		this.newGame();
	}
	
	public void newGame() {
		this.game1 = new Yahtzee();
		this.game2 = new Yahtzee();
		this.player1 = null;
		this.player2 = null;
		this.player = 0;
		this.game = false;
	}
	
	public void nextRound() {
		this.player = (this.player + 1) % 2;
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		MessageChannel channel = event.getChannel();
		User user = event.getAuthor();
		Message msg = event.getMessage();
		String content = msg.getContentRaw();
		
		if (content.startsWith("~~yahtzee") && !this.game) {
			List<Member> members;
			if ((members = msg.getMentionedMembers()).size() > 0) {
				this.player1 = user;
				this.player2 = members.get(0).getUser();
				
				this.game = true;
			} else {
				channel.sendMessage("No player mentioned.\nCommand usage: ~~yahtzee <@mention>").queue();
			}
		}
		
		if (content.startsWith("~~keep")) {
			String[] keep = content.replace("[\\D\\s]", "").split(" ");
			if (keep[1].matches("[1-6]{1,6}")) {
				this.game1.chooseDie(keep[1]);
			} else {
				channel.sendMessage("Invalid input. Usage:\n"
					+ "~~keep [1-6]").queue();
			}
			
		}
		
		if (content.startsWith("~~scores")) {
			channel.sendMessage(this.game1.getScoresheet().build()).queue();
		}
		
		// if (content.startsWith("~~scores") && this.game) {
		// if (user.getId().equals(this.player1.getId())) {
		// channel.sendMessage(game1.getScoresheet().build()).queue();
		// } else if (user.getId().equals(this.player2.getId())) {
		// channel.sendMessage(game2.getScoresheet().build()).queue();
		// } else {
		// channel.sendMessage("You're not even playing...").queue();
		// }
		// }
		
		if (msg.getEmbeds().size() > 0 && msg.getEmbeds().get(0).getTitle().equals("Scoresheet")) {
			// String msgId = msg.getId();
			for (int x = 0; x < 13; x++) {
				msg.addReaction(String.format("U+1f1%02x", x + 6 + 16 * 14)).queue();
			}
			List<MessageReaction> react = msg.getReactions();
			System.out.println(react.get(0).getReactionEmote().getEmoji());
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
		// System.out.println("Reactions");
		// System.out.println(event.getReaction());
		// System.out.println(event.getUser().getName());
	}
	
	public static void main(String[] args) {
		
	}
	
}
