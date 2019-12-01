package com.secretbetta.BASS.Minecraft;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class ServerInfo extends Command {
	
	public ServerInfo() {
		this.cooldown = 5;
		this.help = "Gets Minecraft server info";
		this.name = "mc";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		/**
		 * 
		 */
		EmbedBuilder info = new EmbedBuilder();
		info.setTitle("A.S.S Minecraft Server");
		info.setDescription("Dedicated Minecraft Server for A.S.S");
		info.addField("", "", false);
		info.addField("", "", false);
		info.addField("", "", false);
		info.addField("", "", false);
		
	}
	
}
