package com.secretbetta.BASS.utilities;

import java.awt.Color;
import java.time.Instant;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

public class HelpCommand extends Command {
	
	public HelpCommand() {
		super.name = "help";
		super.arguments = "[admin/games]";
		super.aliases = new String[] { "?" };
		super.cooldown = 15;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getArgs().isEmpty()) {
			event.reply(this.MainCmds().build());
			return;
		}
		
		if (event.getArgs().toLowerCase().contains("games")) {
			event.reply(this.GameCmds().build());
		}
		
		if (event.getArgs().toLowerCase().contains("admin")
			&& event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			event.reply(this.AdminCmds().build());
		}
	}
	
	private EmbedBuilder MainCmds() {
		EmbedBuilder eb = new EmbedBuilder()
			.setTitle("Main Commands")
			.setColor(new Color(2058913))
			.setThumbnail(
				"http://www.handandbeak.com/wp-content/uploads/ju/jumping-bass-fish-icon-vector.jpg") // Use
																										// BASS
																										// Icon
			.setDescription("Usage: ~~help [Optional arguments: admin/games]")
			.addField("~~help", "Lists all possible commands", false)
			.addField("~~bug", "reports a bug", false)
			.addField("~~leaderboard [<Player1> <Player2>]",
				"Shows leaderboard. Player flags are optional to print scores between two people",
				false)
			.addField("~~id", "Gets unique user ID", false)
			.addField("~~hello", "world", false)
			.addField("~~epgames", "Gets the most recent free game from Epic Games", false)
			.addField("~~mquote", "Sends random movie quote", false)
			.addField("~~test", "Test command, differs from time to time", false)
			.addField("~~suggestion <feature>", "Feature suggestion for Andrew to make", false)
			.addField("~~bug", "Reports a bug to Andrew", false)
			.addField("~~ping", "Pong!", false)
			.addField("~~timer",
				new TimerCommand().getHelp() + "\nUsage:" + new TimerCommand().getArguments(),
				false)
			.addField("~~xkcd", "Gets a random comic from XKCD", false)
			.addField("~~run [Minecraft Version]", "Starts Minecraft Server", true)
			.addField("~~mcversions", "Gets all possible minecraft versions", true)
			.addField("~~mc", "Gets Minecraft Server Info if online", false);
		return eb;
	}
	
	/**
	 * @return
	 */
	private EmbedBuilder GameCmds() {
		EmbedBuilder eb = new EmbedBuilder()
			.setTitle("Game Commands")
			.setDescription("Current Games:\n"
				+ "1. Rock Paper Scissors\n"
				+ "2. Tic Tac Toe\n"
				+ "3. Black Jack")
			.setColor(new Color(2058913))
			.addField("~~rps <@mention>",
				"Plays rock paper scissors against opponent. AI Compatible", false)
			.addField("~~rpsquit", "Quits current game", false)
			.addBlankField(true)
			.addField("~~ttt <@mention>", "Tic Tac Toe against opponent", false)
			.addField("~~tttquit", "Quits current game of tic tac toe", false)
			.addBlankField(true)
			.addField("~~blackjack <@mention> <@mention>...",
				"Plays blackjack with n opponents. Max 8 players", false)
			.addField("~~bjquit", "Quits current game", false);
		return eb;
	}
	
	private EmbedBuilder AdminCmds() {
		EmbedBuilder eb = new EmbedBuilder()
			.setTitle("Admin Commands")
			.setDescription("Commands for Admins")
			.setColor(new Color(2058913))
			.setTimestamp(Instant.now())
			.setFooter("Last Updated", null)
			.addField("~~admin", "Tests admin privileges", false)
			.addField("~~~~spongebob <@mention>...~~",
				"~~Adds one or more users to spongebob troll function~~ _Not Working_", false);
		return eb;
	}
}
