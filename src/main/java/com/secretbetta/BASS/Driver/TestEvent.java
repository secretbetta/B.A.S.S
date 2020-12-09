package com.secretbetta.BASS.Driver;

import java.awt.Color;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Test Event for random shenanigans
 * 
 * @author Andrew
 */
public class TestEvent extends ListenerAdapter {
	
	User player1 = null;
	User player2 = null;
	
	@Override
	public void onUserActivityStart(@Nonnull UserActivityStartEvent event) {
		if (event.getNewActivity().getName().toLowerCase().contains("coding")) {
			System.out.println("God, you're coding again?");
		}
	}
	
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
			// Emote emote;
			// U+1f1e4
			String.format("U+1f1%02x", (0 + 6 + 16 * 14));
			eb.addField("Field test", ":large_blue_circle::large_blue_circle::large_blue_circle:"
				+ ":large_blue_circle::large_blue_circle::large_blue_circle:", false); // Can use
																						// discord's
																						// emojis
																						// command
																						// directly
																						// in embed
			eb.addBlankField(true);
			eb.setAuthor("Test png", null,
				"https://img.pngio.com/ceshi-test-testing-icon-with-png-and-vector-format-for-free-testing-png-512_512.png");
			eb.setFooter("Footer",
				"https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
			eb.setImage(
				"https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			eb.setThumbnail(
				"https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
			channel.sendMessage(eb.build()).queue();
		}
		
		/**
		 * Test message, changes for testing purposes :p
		 */
		// if (content.startsWith("~~scores")) {
		// ArrayList<Integer> scoresheet = new ArrayList<>();
		// Yahtzee game = new Yahtzee();
		// game.roll();
		// System.out.println(game.getDice());
		// game.move(Integer.parseInt(content.split(" ")[1]));
		// channel.sendMessage(game.getScoresheet().build()).queue();
		// }
		
		if (message.getEmbeds().size() > 0 && message.getEmbeds().get(0).getTitle() != null
			&& message.getEmbeds().get(0).getTitle().toString().equals("Scoresheet")) {
			String msgId = message.getId();
			for (int x = 0; x < 13; x++) {
				message.addReaction(String.format("U+1f1%02x", (x + 6 + 16 * 14))).queue();
			}
			List<MessageReaction> react = message.getReactions();
			System.out.println(react.get(0).getReactionEmote().getEmoji());
		}
		
		/**
		 * PM/DM
		 */
		if (content.startsWith("~~dog")) {
			EmbedBuilder emb = new EmbedBuilder();
			emb.setImage("https://www.randomdoggiegenerator.com/randomdoggie.php");
			channel.sendMessage(emb.build()).queue();
		}
		
		if (content.startsWith("emote")) {
			Emote ahhh = event.getJDA().getEmoteById("675136315485847613");
			channel.sendMessage(ahhh.getAsMention()).queue();
		}
	}
	
	public static void main(String[] args) {
	}
	
}
