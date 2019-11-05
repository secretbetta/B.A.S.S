package com.secretbetta.BASS.blackjack;

import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BlackjackEvent extends ListenerAdapter {
	
	private boolean game = false;
	private Blackjack blackjack;
	private List<Member> players;
	
	public void newGame(int players, List<Member> users) {
		this.blackjack = new Blackjack(players);
		this.players = users;
		this.game = true;
		this.blackjack.shuffle();
		this.blackjack.dealAll(2);
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		
		if (content.startsWith("~~blackjack") && !game) {
			if (message.getMentionedMembers().size() > 0) {
				List<Member> players = message.getMentionedMembers();
				players.add(message.getMember());
				newGame(players.size() + 1, players);
				channel.sendMessage("Welcome to BlackJack! The players are:\n").queue();
				for (Member player : players) {
					channel.sendMessage(String.format("%s\n", player.getNickname())).queue();
				}
			} else {
				channel.sendMessage("Invalid command. Correct usage:\n"
					+ "~~blackjack <@mention>...\n"
					+ "Multiple mentions possible. Up to 8 total players").queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
		if (game) {
			ReactionEmote choice = event.getReactionEmote();
		}
	}
}
