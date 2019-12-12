package com.secretbetta.BASS.debug;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;

/**
 * Admin Test Command. Checks if user is an admin or not
 * 
 * @author Andrew
 */
public class AdminTestCommand extends Command {
	
	public AdminTestCommand() {
		this.name = "admin";
		this.help = "Tells user if they are an admin or not";
		this.cooldown = 15;
		this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.reply("You are an administrator!");
		} else {
			event.reply("You are not an administrator!");
		}
	}
	
}
