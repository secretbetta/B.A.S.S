package com.secretbetta.BASS.epicgames;

import java.net.MalformedURLException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * <h1>Epic Games latest free game command</h1>
 * Command to get the latest free game that Epic Games is currenttly offering
 * 
 * @author Secretbeta
 */
public class EpicGamesCommand extends Command {
	
	public EpicGamesCommand() {
		super.name = "epgames";
		super.help = "Lists the current free game provided by Epic Games";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		try {
			EpicGamesCrawler cr = new EpicGamesCrawler();
			event.reply("Epic Games current free game:\n" + cr.latestGame());
		} catch (MalformedURLException e) {
			System.err.println("Website could not be crawled");
		}
	}
	
}
