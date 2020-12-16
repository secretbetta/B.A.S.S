package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PokerEvent extends ListenerAdapter {
	
	Poker game;
	
	List<Member> players = new ArrayList<>();
	
	MessageChannel mainchnl = null;
	Message main = null;
	
	int player;
	int button;
	
	private final int blind = 50;
	
	boolean start = false;
	
	public void startGame(int p, MessageChannel chnl, Message main) {
		this.game = new Poker(p);
		this.start = true;
		this.mainchnl = chnl;
		this.main = main;
		this.player = 0;
		this.button = 0;
	}
	
	/**
	 * Gets GUI in EmbedBuilder Format
	 * 
	 * @return
	 */
	public EmbedBuilder getGui() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Poker Game");
		embed.setDescription(String.format("River: %s",
			this.game.getRiver().isEmpty() ? "Empty River" : this.game.getRiver().toString()));
		embed.appendDescription("\n1: Check/Call. 2: Raise. 3: Fold");
		String playerHands = "";
		for (int x = 0; x < this.players.size(); x++) {
			playerHands += String.format("```css\n%s's Info: %s```\n",
				this.players.get(x).getNickname(),
				this.game.getPlayerInfo(x));
		}
		embed.addField("Players", playerHands, false);
		embed.setFooter(String.format("%s's turn", this.players.get(this.player).getNickname()));
		
		return embed;
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		Message msg = event.getMessage();
		Member author = event.getMember();
		MessageChannel chnl = event.getChannel();
		String content = msg.getContentRaw().toLowerCase();
		
		if (content.startsWith("~~poker") && msg.getMentionedMembers().size() >= 1) {
			this.players.addAll(msg.getMentionedMembers());
			this.players.add(0, author);
			
			this.startGame(this.players.size(), chnl, msg);
			
			this.game.raise(this.button, this.blind / 2);
			this.game.raise(this.button + 1, this.blind);
			
			chnl.sendMessage(this.getGui().build()).queue();
		}
	}
	
	@Override
	public void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {
		
		MessageEmbed gui = event.getMessageEmbeds() == null ? null
			: event.getMessageEmbeds().get(0);
		if (gui == null) {
			return;
		}
		
		if (gui.getTitle() != null && gui.getTitle().equals("Poker Game")) {
			Long id = event.getMessageIdLong();
			for (int x = 1; x <= 3; x++) {
				event.getChannel().addReactionById(id, String.format("U+3%dU+20e3", x)).queue();
			}
			
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if (event.getUser().isBot() || !this.start) {
			return;
		}
		
		Member player = event.getMember();
		// MessageChannel chnl = event.getChannel();
		
		System.err.println(event.getMessageId());
		if (event.getReaction().getMessageId().equals(this.main.getId())) {
			event.getReaction().removeReaction(event.getUser()).queue();
		}
		
		if (player.equals(this.players.get(this.player)) && this.start) { // Checks if its player
																			// turn && a game
																			// has started
			switch (event.getReactionEmote().getEmoji()) {
				case "U+31U+20e3": // Call/Check
					
					break;
				case "U+32U+20e3": // Raise
					
					break;
				case "U+33U+20e3": // Fold
					
					break;
			}
		}
	}
}
