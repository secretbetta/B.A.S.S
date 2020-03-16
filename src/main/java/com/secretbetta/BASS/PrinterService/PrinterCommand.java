package com.secretbetta.BASS.PrinterService;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class PrinterCommand extends Command {
	
	public PrinterCommand() {
		super.help = "print";
		super.arguments = "(Include a PDF file)";
		super.cooldown = 60;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		event.getMessage().getAttachments().get(0).downloadToFile()
			.thenAccept(file -> System.out.println("Saved file: " + file.getName()))
			.exceptionally(t -> { // handle failure
				t.printStackTrace();
				return null;
			});
	}
	
}
