package com.secretbetta.BASS.Minecraft;

import java.io.File;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

/**
 * A command class to start my personal Minecraft Server. Uses {@link ProcessBuilder} to run the jar
 * file in my Minecraft Server directory.
 * 
 * @author Secretbeta
 */
public class StartServerCommand extends Command {
	
	/* Directory of Servers and default version */
	private final String mainDir = "D:\\Games\\Minecraft\\Minecraft Servers\\";
	private final String version = "1.15.2 Spigot";
	
	private boolean start;
	private ProcessBuilder process;
	
	private Message message;
	
	/**
	 * Initializes necessary variables to start a server.
	 * <p1>Creates command prompt for Process Builder and initializes Command variables</p1>
	 * 
	 * @see Command
	 */
	public StartServerCommand() {
		this.start = false;
		this.process = new ProcessBuilder("cmd.exe", "/c", "start", "cmd");
		this.process = this.process
			.directory(new File(this.mainDir));
		
		super.name = "run";
		super.cooldown = 90;
		super.help = "Starts minecraft server. Defaults to " + this.version
			+ " if no argument is given.";
		super.userPermissions = new Permission[] { Permission.MANAGE_WEBHOOKS };
		super.arguments = "[optional: version]";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		} else if (start) {
			MinecraftServer ass = new MinecraftServer("73.231.149.126", 25565);
			try {
				ass.fetchData();
				event.reply("Server is already running!");
				return;
			} catch (IOException e) {
				start = false;
			}
		}
		
		this.process.command("java", "-Xms8G", "-Xmx12G", "-jar", this.getJar(event));
		try {
			this.process.start();
			start = true;
			event.reply("Please wait about 1 minute.");
		} catch (IOException e) {
			event.reply(
				"Could not start server. Maybe invalid version? Try *~~mcversions* to see all versions available");
			e.printStackTrace();
			return;
		}
		
		this.message = event.getChannel().sendMessage("Loading").complete();
		CheckServer check = new CheckServer();
		check.start();
	}
	
	/**
	 * Figures out which jar file to return. Returns either spigot.jar or server.jar
	 * 
	 * @param event The command event received by the user.
	 * @return "spigot.jar" or "server.jar"
	 */
	private String getJar(CommandEvent event) {
		String jar = "";
		if (event.getArgs().isEmpty()) {
			event.reply("Starting default server. Version: " + this.version);
			this.process = this.process
				.directory(new File(this.mainDir + version));
			if (this.version.toLowerCase().contains("spigot")) {
				jar = "spigot.jar";
			} else {
				jar = "server.jar";
			}
		} else {
			event.reply("Starting server version: " + event.getArgs());
			this.process = this.process
				.directory(new File(this.mainDir + event.getArgs()));
			if (event.getArgs().toLowerCase().contains("spigot")) {
				jar = "spigot.jar";
			} else {
				jar = "server.jar";
			}
		}
		System.err.println(jar);
		return jar;
	}
	
	/**
	 * Checks Minecraft server's status.
	 * <p1>Runs a thread that loops for 60 seconds to check the status of the server. If server is
	 * online, it stops, else, it sends a timout message</p1>
	 * 
	 * @author Secretbeta
	 */
	private class CheckServer extends Thread {
		
		@Override
		public void run() {
			long t = System.currentTimeMillis();
			long end = t + 60 * 1000;
			
			MinecraftServer ass = new MinecraftServer("73.231.149.126", 25565);
			while (System.currentTimeMillis() < end) {
				message.editMessage(message.getContentRaw() + "..").queue();
				try {
					ass.fetchData();
					message.editMessage("```Server is Online```").queue();
					start = true;
					return;
				} catch (IOException e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
			
			message.getChannel().sendMessage("Server couldn't start. Timeout").queue();
		}
	}
}
