package com.secretbetta.BASS.Minecraft;

import java.io.IOException;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.kronos.rkon.core.Rcon;
import net.kronos.rkon.core.ex.AuthenticationException;

/**
 * <h1>Minecraft Console Event.</h1>
 * <p1>Listens for minecraft commands in the minecraftconsole channel.
 * Requires webhooks permission to use.</p1>
 * 
 * @author Secretbeta
 *
 */
public class ConsoleEvent extends ListenerAdapter {
	
	/**
	 * Sends all messages by users in minecraftconsole channel. Sends them to console as commands. 
	 * Only usable for those with managing webhooks permission.
	 * 
	 * @param 	type
	 * 			The Guild Message Received Event
	 * 
	 */
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot() || !event.getChannel().getId().equals("698254541774389368")) {
			return;
		} else if (!event.getMember().hasPermission(Permission.MANAGE_WEBHOOKS)) {
			event.getChannel().sendMessage("You do not have permission to use the console.").queue();
			return;
		}
		
		Message msg = event.getMessage();
		MessageChannel chnl = event.getChannel();
		String content = msg.getContentRaw();
		
		Rcon rcon;
		try {
			rcon = new Rcon("127.0.0.1", 25575, "phucnguyen12".getBytes());
			String result = rcon.command(content);
			result = result.replaceAll("[§]+.", "");
			chnl.sendMessage("```" + result + "```").queue();
		} catch (AuthenticationException e1) {
			event.getChannel().sendMessage("Could not connect to server").queue();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
