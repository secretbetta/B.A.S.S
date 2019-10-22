import java.awt.Color;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TestEvent extends ListenerAdapter {
	
	User player1 = null;
	User player2 = null;
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		
		/**
		 * Test message, changes for testing purposes :p
		 */
		if (content.startsWith("~~embed")) {
			channel.sendMessage("This is a test embedded message").queue();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Test Embed");
			eb.setColor(Color.blue);
			eb.setDescription("This is just a test format");
			eb.addField("Field test", "Testing field", false);
			eb.addBlankField(true);
			eb.setAuthor("Test png", null, "https://img.pngio.com/ceshi-test-testing-icon-with-png-and-vector-format-for-free-testing-png-512_512.png");
			eb.setFooter("Footer", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
			eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			channel.sendMessage(eb.build()).queue();
		}
		
		/**
		 * PM/DM
		 */
		if (content.startsWith("~~pm")) {
			channel.sendTyping().queue();
//			if (!objMember.getUser().hasPrivateChannel()) {
			objMember.getUser().openPrivateChannel().complete().sendMessage("???").queue();
			System.out.println(objMember.getUser().openPrivateChannel().complete().getLatestMessageId());
//			} else {
//			}
		}
		
		/**
		 * RPS test
		 */
		if (content.startsWith("~~rps")) {
			List<Member> players = message.getMentionedMembers();
			this.player1 = objMember.getUser();
			this.player2 = players.get(0).getUser();
			
			this.player1.openPrivateChannel().complete().sendMessage(String.format("This is a test message for %s! Ignore Please", player1.getName())).queue();
			this.player2.openPrivateChannel().complete().sendMessage(String.format("This is a test message for %s! Ignore Please", player2.getName())).queue();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
