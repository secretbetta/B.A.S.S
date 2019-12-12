package com.secretbetta.BASS.spongebob;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpongeBobEvent extends ListenerAdapter {
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		String id = event.getAuthor().getId();
		MessageChannel channel = event.getChannel();
		Message msg = event.getMessage();
		String content = msg.getContentRaw();
		// TODO Spongebob command + event
		// if (this.trollIds.contains(id)) {
		// channel.sendMessage(Spongebob.spongebobUpper(content)).queue();
		// }
	}
}
