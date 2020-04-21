package com.secretbetta.BASS.xkcd;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * <h1>XKCD command</h1>
 * Command that gets a random or specific XKCD comic from <a href="xkcd.com">XKCD</a>
 * 
 * @author Andrew
 */
public class XKCDCommand extends Command {
	
	public XKCDCommand() {
		super.name = "xkcd";
		super.help = "xkcd comic getter";
		super.arguments = "[Optional link/number to specific comic]";
		super.cooldown = 10;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		xkcdCrawler xkcd = null;
		if (event.getArgs().length() == 0) { // Random comic
			try {
				xkcd = new xkcdCrawler();
			} catch (MalformedURLException e) {
				event.reply("Could not get comic.");
				System.err.println("URL Exception");
				System.err.println(e.getLocalizedMessage());
				return;
			}
		} else { // Specific comic getter
			try {
				int comic = -1;
				if (event.getArgs().trim().length() < 5) {
					comic = Integer.parseInt(event.getArgs().trim());
				}
				
				if (comic >= 1 && comic <= 9999) { // Comic number
					xkcd = new xkcdCrawler(new URL(String.format("https://xkcd.com/%d/", comic)));
				} else { // Direct link to comic
					xkcd = new xkcdCrawler(new URL(event.getArgs().trim()));
					event.getMessage().delete().queue();
				}
			} catch (MalformedURLException e) {
				event.reply("Could not get comic.");
				System.err.println("URL Exception");
				System.err.println(e.getLocalizedMessage());
				return;
			}
		}
		
		// The actual message
		EmbedBuilder eb = new EmbedBuilder();
		try {
			eb.setTitle(String.format("%s", xkcd.getXKCDtitle()));
			eb.setImage(xkcd.getXKCDimg());
			eb.setDescription(xkcd.getXKCDdesc());
			eb.addField("", String.format("[Permanent link to comic](%s)", xkcd.getXKCDpermLink()),
				true);
		} catch (IndexOutOfBoundsException e) {
			event.reply("No comic found");
			return;
		} catch (MalformedURLException e) {
			event.reply("Could not get image");
			System.err.println(e.getLocalizedMessage());
		}
		eb.setColor(Color.blue);
		event.reply(eb.build());
	}
	
}
