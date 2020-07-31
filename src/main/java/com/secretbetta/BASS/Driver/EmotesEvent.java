package com.secretbetta.BASS.Driver;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EmotesEvent extends ListenerAdapter {
	
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		// TODO Auto-generated method stub
		// super.onGuildMessageReactionAdd(event);
		System.out.println(event.getReactionEmote().getName());
		System.out.println(event.getReactionEmote().getId());
		// System.out.println(event.getReactionEmote().getEmoji());
		System.out.println(event.getReactionEmote().getAsCodepoints());
	}
}
