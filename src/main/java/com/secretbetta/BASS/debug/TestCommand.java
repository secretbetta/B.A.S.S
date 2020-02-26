package com.secretbetta.BASS.debug;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;

public class TestCommand extends Command {
	
	public TestCommand() {
		Permission[] perms = new Permission[1];
		perms[0] = Permission.ADMINISTRATOR;
		
		this.name = "test";
		this.userPermissions = perms;
		this.hidden = true;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String[] args = event.getArgs().trim().split(" ");
		event.reply(String.format("Arg length: %d", event.getArgs().length()));
		event.reply(String.format("Args: %s", event.getArgs()));
		
		ArrayList<String> pArgs = new ArrayList<>();
		for (String word : args) {
			pArgs.add(word);
		}
		event.reply(String.format("Split Args Length: %d", pArgs.size()));
		event.reply(args[0]);
		event.reply(String.format("Split Args: %s", pArgs));
	}
	
}
