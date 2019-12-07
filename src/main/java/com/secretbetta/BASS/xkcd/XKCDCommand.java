package com.secretbetta.BASS.xkcd;

import java.awt.Color;
import java.net.MalformedURLException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class XKCDCommand extends Command {
	
	public XKCDCommand() {
		this.name = "xkcd";
		this.help = "xkcd comic getter";
		this.hidden = true;
		// this.cooldown = 5;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		
		if (event.getAuthor().isBot()) {
			return;
		}
		
		try {
			xkcdCrawler xkcd = new xkcdCrawler();
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle(String.format("%s", xkcd.getXKCDtitle()));
			eb.setColor(Color.blue);
			eb.setImage(xkcd.getXKCDimg());
			eb.setDescription(xkcd.getXKCDdesc());
			eb.addField("", String.format("[Permanent link to comic](%s)", xkcd.getXKCDpermLink()), true);
			event.reply(eb.build());
		} catch (MalformedURLException e) {
			event.reply("Could not get comic.");
			System.err.println("URL Exception");
			System.err.println(e.getLocalizedMessage());
		}
	}
	
}
