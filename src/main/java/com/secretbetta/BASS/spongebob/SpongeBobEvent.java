package com.secretbetta.BASS.spongebob;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SpongeBobEvent extends ListenerAdapter {
	
	// private Spongebob sb;
	
	public SpongeBobEvent() {
		// try {
		// this.sb = new Spongebob();
		// } catch (IOException e) {
		// System.err.println("Could not load users into sb.");
		// }
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		// String id = event.getAuthor().getId();
		// MessageChannel channel = event.getChannel();
		// Message msg = event.getMessage();
		// String content = msg.getContentRaw();
		// if (this.trollIds.contains(id)) {
		// channel.sendMessage(Spongebob.spongebobUpper(content)).queue();
		// }
	}
}
