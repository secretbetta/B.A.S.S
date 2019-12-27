package com.secretbetta.BASS.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class BugReportCommand extends Command {
	
	public BugReportCommand() {
		this.name = "bug";
		this.help = "Reports bugs to Secretbetta (Andrew)";
		this.arguments = "<bug report>";
		this.cooldown = 30;
		this.hidden = true;
		this.guildOnly = false;
	}
	
	/**
	 * Reports bugs
	 * 
	 * @param bug
	 * @throws IOException
	 */
	public static void bugReport(String bug) throws IOException {
		String path = "bugs.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
		writer.append(String.format(" * %s\n", bug));
		writer.close();
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getArgs().length() != 0) {
			try {
				BugReportCommand.bugReport(event.getArgs());
				event.reply(String.format("Added \"%s\" to bug reports. Thank you.", event.getArgs()));
			} catch (IOException e) {
				event.reply("Could not add bug report to file.");
			}
		} else {
			event.reply("No bug reported.");
			event.reply("Usage:\n~~bug <bug report>");
		}
	}
	
}
