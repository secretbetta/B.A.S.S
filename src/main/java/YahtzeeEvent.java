import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class YahtzeeEvent extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		System.out.println("Yahtzee Event");
		System.out.println(event.getAuthor().getName());
		System.out.println(event.getMessage().getContentRaw());
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
