package com.secretbetta.BASS.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class SuggestionsCommand extends Command {

	/**
	 * Adds suggestions to my list of things to do
	 * 
	 * @param suggestion
	 * @return
	 * @throws IOException
	 */
	public static void suggestions(String suggestion) throws IOException {
		String path = "suggestions.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
		writer.append(String.format(" * %s\n", suggestion));
		writer.close();
	}
	
	public SuggestionsCommand() {
		this.name = "suggestion";
		this.help = "makes a suggestion for Andrew to add/fix! Please try to do one suggestion per command use";
		this.cooldown = 30;
		this.hidden = true;
		this.arguments = "<suggestion>";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getArgs().length() != 0) {
			try {
				SuggestionsCommand.suggestions(event.getArgs());
				event.reply(String.format("Added \"%s\" to suggestions. Thank you.", event.getArgs()));
			} catch (IOException e) {
				event.reply("Could not add suggestion to file.");
			}
		}
	}
	
}
