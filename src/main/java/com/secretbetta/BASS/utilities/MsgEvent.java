package com.secretbetta.BASS.Driver;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MsgEvent extends ListenerAdapter {
	
	JDA jda;
	
	public MsgEvent(JDA jda) {
		// TODO Auto-generated constructor stub
		this.jda = jda;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		super.onMessageReceived(event);
		PrivateChannel chnl = event.getAuthor().openPrivateChannel().complete();
		
	}
}
