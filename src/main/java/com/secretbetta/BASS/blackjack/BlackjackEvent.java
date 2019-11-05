package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BlackjackEvent extends ListenerAdapter {
	
	private boolean game = false;
	private Blackjack blackjack;
	private List<Member> players;
	private int[] rounds;
	private boolean[] stays;
	
	public void newGame(int players, List<Member> users) {
		this.blackjack = new Blackjack(players);
		this.players = users;
		this.game = true;
		this.blackjack.shuffle();
		this.blackjack.dealAll(2);
		this.rounds = new int[players];
		this.stays = new boolean[players];
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		
		if (content.startsWith("~~blackjack") && !game) {
			if (message.getMentionedMembers().size() > 0) {
				List<Member> players = new ArrayList<>();
				players.addAll(message.getMentionedMembers());
				players.add(message.getMember());
				newGame(players.size(), players);
				channel.sendMessage("Welcome to BlackJack! The players are:\n").queue();
				int p = 0;
				for (Member player : players) {
					channel.sendMessage(String.format("%s\n", player.getNickname())).queue();
					player.getUser()
						.openPrivateChannel()
						.complete()
						.sendMessage(String.format("Your cards are: %s\n"
							+ "Click [1] to hit. Click [2] to stay.", this.blackjack.getHand(p).toString()))
						.queue();
					p++;
				}
			} else {
				channel.sendMessage("Invalid command. Correct usage:\n"
					+ "~~blackjack <@mention>...\n"
					+ "Multiple mentions possible. Up to 8 total players").queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			if (event.getMessage().getContentRaw().startsWith("Your cards are: ")) {
				event.getMessage().addReaction("U+31U+20e3").queue();
				event.getMessage().addReaction("U+32U+20e3").queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		if (game) {
			ReactionEmote choice = event.getReactionEmote();
			System.out.println(choice.getAsCodepoints());
			if (choice.getAsCodepoints().equals("U+31U+20e3")) {
				// Deal more
			} else {
				// stay
				for (int x = 0; x < this.players.size(); x++) {
					
				}
			}
		}
	}
}
