package com.secretbetta.BASS.Driver;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * This is so broken, but its format works:
 * ~~troll <num> "<string>"
 * Output:
 * Prints <string> <num> times
 * 
 * @author Secretbeta
 */
public class trollJustine extends Command {
	
	public trollJustine() {
		this.name = "troll";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		String[] args = event.getArgs().split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)");
		args[1] = args[1].replace("\"", "");
		int times = Integer.parseInt(args[0]);
		for (int x = 0; x < times; x++) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			event.reply(args[1]);
		}
	}
	
}
