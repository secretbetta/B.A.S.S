package com.secretbetta.BASS.utilities;

import java.math.BigInteger;
import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.MessageChannel;

/**
 * Timer command. Reminds user in hours, minutes, and seconds with their own message. Pings them
 * once timer is completed.
 * 
 * @author Secretbeta
 */
public class TimerCommand extends Command {
	
	/* User IDs */
	ArrayList<String> users = new ArrayList<String>();
	
	public TimerCommand() {
		super.name = "timer";
		super.help = "Makes a timer in seconds, minutes, or hours. Message must be surrounded by Quotes";
		super.arguments = "-h <hours> -m <minutes> -s <seconds> -msg \"<Message>\"";
		super.cooldown = 5;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (this.users.contains(event.getAuthor().getAsMention())) {
			event.reply("You have already made a reminder.");
			return;
		}
		
		if (event.getArgs().isEmpty()) {
			event.reply("No args given.");
			event.reply("Usage: ~~timer " + super.arguments);
			return;
		}
		
		ArgumentMap args = new ArgumentMap(event.getArgs().toLowerCase()
			.split("\\s(?=(?:[^'\"`]*(['\"`])[^'\"`]*\\1)*[^'\"`]*$)"));
		
		BigInteger seconds = args.getBigInt("-s", new BigInteger("0"));
		BigInteger minutes = args.getBigInt("-m", new BigInteger("0"));
		BigInteger hours = args.getBigInt("-h", new BigInteger("0"));
		String msg = args.getString("-msg", "");
		this.users.add(event.getAuthor().getAsMention());
		
		event.reply("You will be reminded of \"" + msg + "\" in " + hours + " hours, " + minutes
			+ " minutes, and " + seconds + " seconds.");
		
		this.timer(event.getChannel(), event.getAuthor().getAsMention(), msg, hours, minutes,
			seconds);
	}
	
	/**
	 * Creates a timer in hours, minutes, and seconds
	 * 
	 * @param mention The user to ping
	 * @param msg     Message to tell user
	 * @param hours   Number of hours
	 * @param minutes Number of minutes
	 * @param seconds Number of seconds
	 */
	public void timer(MessageChannel chnl, String mention, String msg, BigInteger hours,
		BigInteger minutes,
		BigInteger seconds) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					chnl.sendMessage(mention + ": " + msg).queue();
					users.remove(mention);
				}
			},
			hours.longValue() * 60 * 60 * 1000 + minutes.longValue() * 60 * 1000
				+ seconds.longValue() * 1000);
	}
}
