import java.awt.Color;
import java.util.ArrayList;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Test Event for random shenanigans
 * 
 * @author Andrew
 */
public class TestEvent extends ListenerAdapter {
	
	User player1 = null;
	User player2 = null;
	
	@SuppressWarnings("unused")
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
			eb.setAuthor("Test png", null,
				"https://img.pngio.com/ceshi-test-testing-icon-with-png-and-vector-format-for-free-testing-png-512_512.png");
			eb.setFooter("Footer",
				"https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
			eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			channel.sendMessage(eb.build()).queue();
		}
		
		/**
		 * Test message, changes for testing purposes :p
		 */
		if (content.startsWith("~~scores")) {
			ArrayList<Integer> scoresheet = new ArrayList<>();
			Yahtzee game = new Yahtzee();
			game.roll();
			String dice = "";
			game.changeDie();
			int[] dices = game.die();
			for (int p : dices) {
				dice += String.format("[%d]", p);
			}
			System.out.println("Dice: " + dice);
			System.out.println(game.move(Integer.parseInt(content.split(" ")[1])));
			channel.sendMessage(game.getScoresheet().build()).queue();
		}
		
		/**
		 * PM/DM
		 */
		if (content.startsWith("~~dog")) {
			EmbedBuilder emb = new EmbedBuilder();
			emb.setImage("https://www.randomdoggiegenerator.com/randomdoggie.php");
			channel.sendMessage(emb.build()).queue();
		}
	}
	
	public static void main(String[] args) {
		// Test stuff here
	}
	
}
