import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EmotesTestEvent extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		System.out.println(event.getReaction());
		System.out.println(event.getUser().getName());
	}
}
