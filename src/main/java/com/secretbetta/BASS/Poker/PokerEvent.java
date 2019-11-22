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
	
	boolean start = false;
	Poker game;
	List<Member> players = new ArrayList<>();
	MessageChannel mainchnl = null;
	
	public void startGame(int p, MessageChannel chnl) {
		this.game = new Poker(p);
		this.start = true;
		this.mainchnl = chnl;
	}
	
	public EmbedBuilder getGui() {
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("Poker Game");
		embed.setDescription(String.format("River: %s",
			this.game.getRiver().isEmpty() ? "Empty River" : this.game.getRiver().toString()));
		embed.appendDescription("\n1: Check/Call. 2: Raise. 3: Fold");
		String playerHands = "";
		for (int x = 0; x < this.players.size(); x++) {
			playerHands += String.format("```css\n%s's Info: %s```\n", this.players.get(x).getNickname(),
				this.game.getPlayerInfo(x));
		}
		embed.addField("Players", playerHands, false);
		embed.setFooter(String.format("%s's turn", this.players.get(this.game.player).getNickname()));
		
		return embed;
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message msg = event.getMessage();
		Member author = event.getMember();
		MessageChannel chnl = event.getChannel();
		String content = msg.getContentRaw().toLowerCase();
		
		this.players.addAll(msg.getMentionedMembers());
		if (content.startsWith("~~poker") && (this.players.size() >= 1)) {
			this.players.add(0, author);
			
			this.startGame(this.players.size(), chnl);
			chnl.sendMessage(this.getGui().build()).queue();
		}
	}
	
	@Override
	public void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {
		MessageEmbed gui = event.getMessageEmbeds().get(0);
		
		if (gui.getTitle().equals("Poker Game")) {
			Long id = event.getMessageIdLong();
			for (int x = 1; x <= 3; x++) {
				event.getChannel().addReactionById(id, String.format("U+3%dU+20e3", x)).queue();
			}
			
		}
	}
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		Member player = event.getMember();
		MessageChannel chnl = event.getChannel();
		
		if (player.equals(this.players.get(this.game.player))) {
			switch (event.getReactionEmote().getEmoji()) {
				case "U+31U+20e3": //
					break;
				case "U+32U+20e3":
					break;
				case "U+33U+20e3":
					break;
			}
		}
	}
}
