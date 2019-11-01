import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
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
		newGame();
	}
	
	public void newGame() {
		this.game1 = new Yahtzee();
		this.game2 = new Yahtzee();
		this.player1 = null;
		this.player2 = null;
		this.player = 0;
		game = false;
	}
	
	public void nextRound() {
		this.player = (this.player + 1) % 2;
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Member objMember = event.getMember();
		MessageChannel channel = event.getChannel();
		User user = event.getAuthor();
		Message msg = event.getMessage();
		String content = msg.getContentRaw();
		System.out.println("Yahtzee Event");
		System.out.println(event.getAuthor().getName());
		System.out.println(event.getMessage().getContentRaw());
		
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
			String[] keep = content.split(" ");
			if (keep[1].matches("[0-9]{1,5}")) {
				this.game1.chooseDie(keep[1]);
			}
			
		}
		
		if (content.startsWith("~~scores") && this.game) {
			if (user.getId().equals(this.player1.getId())) {
				channel.sendMessage(game1.getScoresheet().build()).queue();
			} else if (user.getId().equals(this.player2.getId())) {
				channel.sendMessage(game2.getScoresheet().build()).queue();
			} else {
				channel.sendMessage("You're not even playing...").queue();
			}
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {
		System.out.println("Reactions");
		System.out.println(event.getReaction());
		System.out.println(event.getUser().getName());
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
}
