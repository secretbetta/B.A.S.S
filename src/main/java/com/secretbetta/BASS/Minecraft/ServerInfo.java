package com.secretbetta.BASS.Minecraft;

import java.awt.Color;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.secretbetta.BASS.Minecraft.MinecraftServer.Player;
import com.secretbetta.BASS.Minecraft.MinecraftServer.StatusResponse;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Minecraft Server info command
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
		
		// int timeout = 3000;
		MinecraftServer ass = new MinecraftServer("73.162.89.39", 25565);
		try {
			StatusResponse serverInfo = ass.fetchData();
			event.reply("```Server is online```");
			
			EmbedBuilder info = new EmbedBuilder();
			info.setColor(Color.BLUE);
			info.setTitle("A.S.S Minecraft Server");
			info.setDescription("Dedicated Minecraft Server for A.S.S");
			info.addField("Name", "A.S.S Server", false);
			info.addField("MOTD", serverInfo.getDescription().getText().replaceAll("§.|[^\\w ']", "").trim(), false);
			info.addField("IP", "73.162.89.39:25565", false);
			
			String playerlist = "";
			for (Player player : serverInfo.getPlayers().getSample()) {
				playerlist += player.getName() + "\n";
			}
			info.addField(
				String.format("Online %d/%d", serverInfo.getPlayers().getOnline(), serverInfo.getPlayers().getMax()),
				"**Player List**\n```" + playerlist + "```", false);
			info.addField("Version", serverInfo.getVersion().getName(), false);
			event.reply(info.build());
		} catch (IOException e) {
			event.reply("Server is offline");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
}
