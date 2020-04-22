package com.secretbetta.BASS.utlities;

import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TimerCommand extends Command {
	
	ArrayList<String> userIDs = new ArrayList<String>();
	
	public TimerCommand() {
		super.name = "timer";
		super.help = "Makes a timer in seconds, minutes, or hours";
		super.arguments = "-h <hours> -m <minutes> -s <seconds>";
		super.cooldown = 30;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		// Parse -h, -m, -s
		// Record UserIDS
	}
	
	public void timer(String mention, int hours, int minutes, int seconds) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					
				}
			},
			hours * 3600 + minutes * 60 + seconds);
	}
	
	private class Reminder extends Thread {
		
		@Override
		public void run() {
			// long t = System.currentTimeMillis();
			// long end = t + 60 * 1000;
			//
			// MinecraftServer ass = new MinecraftServer("73.231.149.126", 25565);
			// while (System.currentTimeMillis() < end) {
			// message.editMessage(message.getContentRaw() + "..").queue();
			// try {
			// ass.fetchData();
			// message.editMessage("```Server is Online```").queue();
			// start = true;
			// return;
			// } catch (IOException e) {
			// System.err.println(e.getLocalizedMessage());
			// }
			// }
			//
			// message.getChannel().sendMessage("Server couldn't start. Timeout").queue();
		}
	}
	
}
