package com.secretbetta.BASS.Driver;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class repeatCommand extends Command {
	
	public repeatCommand() {
		super.name = "repeat";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String msg = event.getArgs();
		event.getMessage().delete().queue();
		event.reply(msg);
	}
	
}
