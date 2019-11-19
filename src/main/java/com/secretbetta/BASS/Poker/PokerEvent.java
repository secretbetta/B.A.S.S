package com.secretbetta.BASS.Poker;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PokerEvent extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
	}
	
	@Override
	public void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {
	}
}
