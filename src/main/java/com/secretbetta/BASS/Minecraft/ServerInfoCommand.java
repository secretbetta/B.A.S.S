package com.secretbetta.BASS.Minecraft;

import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.secretbetta.BASS.Minecraft.MinecraftServer.Player;
import com.secretbetta.BASS.Minecraft.MinecraftServer.StatusResponse;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Minecraft Server info command
 * <p1>Gets info from server and sends to discord. Info includes: Name of server, MOTD, IP, Online
 * players out of Maximum players, Player list.
 * 
 * @see MinecraftServer
 * @author Andrew Nguyen
 */
public class ServerInfoCommand extends Command {
	
	private String defAddress = "107.3.129.6";
	private int defport = 25565;
	
	public ServerInfoCommand() {
		this.cooldown = 5;
		this.help = "Gets Minecraft server info";
		this.name = "mc";
		this.arguments = "[-a address] [-p port]";
		this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		HashMap<String, String> parsedArgs = new HashMap<>();
		String[] args = event.getArgs().trim().toLowerCase().split(" ");
		for (int i = 0; i < args.length - 1 && i < 3; i++) {
			if (args[i].startsWith("-")) {
				if (args[i].substring(1, 2).equals("a")) {
					parsedArgs.put(args[i], args[i + 1]);
				}
			}
		}
		
		String ip = parsedArgs.containsKey("-a") ? parsedArgs.get("-a")
			: this.defAddress;
		int port = parsedArgs.containsKey("-p") ? Integer.parseInt(parsedArgs.get("-p"))
			: this.defport;
		
		MinecraftServer ass = new MinecraftServer(ip, port);
		try {
			StatusResponse serverInfo = ass.fetchData();
			event.reply("```Server is online```");
			
			EmbedBuilder info = new EmbedBuilder();
			info.setColor(Color.BLUE);
			info.setTitle("Secretbetta's Minecraft Server");
			info.setDescription("Hosted by Secretbetta's PC");
			info.addField("Name", "MC Server", false);
			info.addField("MOTD",
				serverInfo.getDescription().getText().replaceAll(".|[^\\w ']", "").trim(), false);
			info.addField("IP", ip + ((port == 25575) ? "" : port), false);
			
			String playerlist = "";
			if (serverInfo.getPlayers().getOnline() != 0) {
				for (Player player : serverInfo.getPlayers().getSample()) {
					playerlist += player.getName() + "\n";
				}
			}
			info.addField(
				String.format("Online %d/%d", serverInfo.getPlayers().getOnline(),
					serverInfo.getPlayers().getMax()),
				"**Player List**\n" + playerlist, false);
			info.addField("Version", serverInfo.getVersion().getName(), false);
			event.reply(info.build());
		} catch (IOException e) {
			event.reply("Server " + ip + ":" + port + " is offline");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
}
