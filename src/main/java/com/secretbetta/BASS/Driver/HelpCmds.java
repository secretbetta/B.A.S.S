package com.secretbetta.BASS.Driver;
import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;

/**
 * Commands Class
 * 
 * @author Andrew
 */
public class HelpCmds {
	
	/**
	 * Gets all commands
	 * 
	 * @return List of Embedded messages that contains commands
	 */
	public static List<EmbedBuilder> helpCmds() {
		List<EmbedBuilder> cmds = new ArrayList<>();
		EmbedBuilder eb;
		
		eb = new EmbedBuilder()
			.setTitle("Main Commands")
			.setColor(new Color(2058913))
			.setThumbnail("http://www.handandbeak.com/wp-content/uploads/ju/jumping-bass-fish-icon-vector.jpg") // Use
																												// BASS
																												// Icon
			.addField("~~help", "Lists all possible commands", false)
			.addField("~~bug", "reports a bug", false)
			.addField("~~leaderboard <Player1> <Player2>",
				"Shows leaderboard. Player flags are optional to print scores between two people", false)
			.addField("~~id", "Gets unique user ID", false)
			.addField("~~hello", "world", false)
			.addField("~~mquote", "Sends random movie quote", false)
			.addField("~~test", "Test command, differs from time to time", false)
			.addField("~~suggestion [Command]", "Command suggestion for Andrew to make", false)
			.addField("~~ping", "Pong!", false)
			.addField("~~xkcd", "Gets a random comic from XKCD", false);
		cmds.add(eb);
		eb = new EmbedBuilder()
			.setTitle("Games")
			.setDescription("Current Games:\n"
				+ "1. Rock Paper Scissors\n"
				+ "2. Tic Tac Toe\n"
				+ "3. Black Jack")
			.setColor(new Color(2058913))
			.addField("~~rps <@mention>", "Plays rock paper scissors against opponent. AI Compatible", false)
			.addField("~~rpsquit", "Quits current game", false)
			.addBlankField(true)
			.addField("~~ttt <@mention>", "Tic Tac Toe against opponent", false)
			.addField("~~tttquit", "Quits current game of tic tac toe", false)
			.addBlankField(true)
			.addField("~~blackjack <@mention> <@mention>...", "Plays blackjack with n opponents. Max 8 players", false)
			.addField("~~bjquit", "Quits current game", false);
		cmds.add(eb);
		eb = new EmbedBuilder()
			.setTitle("Admin Commands")
			.setDescription("Commands for Admins")
			.setColor(new Color(2058913))
			.setTimestamp(Instant.now())
			.setFooter("Last Updated", null)
			.addField("~~admin", "Tests admin privileges", false)
			.addField("~~spongebob <@mention>...", "Adds one or more users to spongebob troll function", false)
			.addField("-debug", "Put at end of command for runtime", false);
		cmds.add(eb);
		return cmds;
	}
}
