package com.secretbetta.BASS.debug;

import java.io.File;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * Fun Facts command! Last Updated facts Nov. 8th
 * 
 * @author Andrew
 */
public class FunFactCommand extends Command {
	
	public FunFactCommand() {
		this.name = "funfacts";
		this.help = "gets fun facts about B.A.S.S.!";
		this.cooldown = 15;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		event.getMessage().getChannel().sendMessage("Fun Facts!").addFile(new File("pictures/funfacts.jpg")).queue();
	}
	
}
