package com.secretbetta.BASS.Poker;

import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PokerEvent extends ListenerAdapter {
	
	Poker game;
	List<User> players;
	
	public PokerEvent() {
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message msg = event.getMessage();
		User author = event.getAuthor();
		String content = msg.getContentRaw().toLowerCase();
		
		if (content.startsWith("~~poker") && (this.players = msg.getMentionedUsers()).size() >= 1) {
			this.players.add(0, author);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Poker Game");
			embed.setDescription(String.format("River: %s",
				this.game.getRiver().isEmpty() ? "Empty River" : this.game.getRiver().toString()));
			String playerHands = "";
			for (int x = 0; x < this.players.size(); x++) {
				playerHands += String.format("%s's Info: %s\n", this.players.get(x).getName(),
					this.game.getPlayerInfo(x));
			}
		}
	}
	
	@Override
	public void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {
	}
}
