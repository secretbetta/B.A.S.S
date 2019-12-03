package com.secretbetta.BASS.Minecraft;

import java.awt.Color;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Minecraft Server information command
 * 
 * @author Andrew
 *
 */
public class ServerInfo extends Command {
	
	public ServerInfo() {
		this.cooldown = 5;
		this.help = "Gets Minecraft server info";
		this.name = "mc";
		this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		int timeout = 3000;
		MinecraftServer ass = new MinecraftServer("73.162.89.39", 25565, timeout);
		if (ass.fetchData()) {
			event.reply("```Server is online```");
			EmbedBuilder info = new EmbedBuilder();
			info.setColor(Color.BLUE);
			info.setTitle("A.S.S Minecraft Server");
			info.setDescription("Dedicated Minecraft Server for A.S.S");
			info.addField("Name", "A.S.S Server", false);
			info.addField("MOTD", ass.getMotd(), false);
			info.addField("IP", "73.162.89.39:25565", false);
			info.addField("Current World", "World", false);
			info.addField("Difficulty", "Hard", false);
			info.addField("Admins", "unknown", false);
			info.addField(String.format("Online %d/%d", ass.getPlayersOnline(), ass.getMaxPlayers()), "(list)", false);
			info.addField("Version", ass.getGameVersion(), false);
			event.reply(info.build());
		} else {
			event.reply("```Server is offline```");
		}
	}
	
}
