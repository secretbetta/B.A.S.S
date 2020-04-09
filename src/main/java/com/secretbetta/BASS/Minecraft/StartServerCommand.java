package com.secretbetta.BASS.Minecraft;

import java.io.File;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.secretbetta.BASS.Minecraft.MinecraftServer.StatusResponse;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;


public class StartServerCommand extends Command {
	
	private boolean start;
	private ProcessBuilder file;
	private String version;
	
	public StartServerCommand() {
		this.start = false;
		this.file = new ProcessBuilder("cmd.exe","/c","start","cmd");
		version = "1.15.2 Spigot";
		this.file = this.file.directory(new File("D:\\Games\\Minecraft\\Minecraft Servers\\1.15.2 Spigot"));
//		this.file = this.file.directory(new File("D:\\Games\\Minecraft\\Minecraft Servers\\notadirectory"));
		
		super.name = "run";
//		super.cooldown = 90;
		super.help = "Starts minecraft server";
		super.userPermissions = new Permission[] {Permission.MANAGE_WEBHOOKS};
		super.arguments = "[optional: version]";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		} else if (start) {
			MinecraftServer ass = new MinecraftServer("73.231.149.126", 25565);
			try {
				StatusResponse serverInfo = ass.fetchData();
				event.reply("Server is already running!");
				return;
			} catch (IOException e) {
				start = false;
			}
		}
		
		String jar = "";
		if (event.getArgs() == null) {
			event.reply("Starting default server. Version: " + this.version);
			this.file = this.file.directory(new File("D:\\Games\\Minecraft\\Minecraft Servers\\" + version));
			if (this.version.toLowerCase().contains("spigot")) {
				jar = "spigot.jar";
			} else {
				jar = "server.jar";
			}
		} else {
			event.reply("Starting server version: " + event.getArgs());
			this.file = this.file.directory(new File("D:\\Games\\Minecraft\\Minecraft Servers\\" + event.getArgs()));
			if (event.getArgs().toLowerCase().contains("spigot")) {
				jar = "spigot.jar";
			} else {
				jar = "server.jar";
			}
		}
		
		System.err.println("Server jar: " + jar);
		System.err.println("");
		System.out.println("Running cmd");
		this.file.command("java", "-Xms8G", "-Xmx12G", "-jar", jar);
		try {
			this.file.start();
			start = true;
			event.reply("Please wait at least 1 minute.");
		} catch (IOException e) {
			event.reply("Could not start server.");
			e.printStackTrace();
			return;
		}
		Message message = event.getChannel().sendMessage("Loading").complete();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		int curr = 0;
		MinecraftServer ass = new MinecraftServer("73.231.149.126", 25565);
		
		long t = System.currentTimeMillis();
		long end = t + 60 * 1000;
		
		while (System.currentTimeMillis() < end) {
			try {
				StatusResponse serverInfo = ass.fetchData();
				message.editMessage("```Server is Online```").queue();
				start = true;
				return;
			} catch (IOException e) {
				String msg = "Loading";
				int i = 0;
				curr%=3;
				while (i <= curr) {
					msg += ".";
					i++;
				}
				curr++;
				message.editMessage(msg).queue();
				System.err.println(e.getLocalizedMessage());
			}
		}
		
		event.reply("Server couldn't start. Timeout");
	}
}
