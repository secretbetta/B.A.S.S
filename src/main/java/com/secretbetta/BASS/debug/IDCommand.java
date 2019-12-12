package com.secretbetta.BASS.debug;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Member;

/**
 * ID Command - Gets User(s) ID(s)
 * 
 * @author Andrew
 */
public class IDCommand extends Command {
	
	public IDCommand() {
		this.name = "id";
		this.help = "Gets user id";
		this.arguments = "[Optional @mention]";
		this.guildOnly = false;
		this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getArgs().length() >= 2) {
			List<Member> ids = event.getMessage().getMentionedMembers();
			
			if (ids.size() >= 1) {
				String IDList = "";
				for (Member member : ids) {
					IDList += String.format("%s's ID: %s\n", member.getEffectiveName(), member.getId());
				}
				event.reply(String.format("User IDS\n```\n%s```", IDList));
			} else {
				event.reply("No one was mentioned");
			}
		} else {
			event.reply(String.format("Your ID: %s", event.getAuthor().getId()));
		}
	}
	
}
