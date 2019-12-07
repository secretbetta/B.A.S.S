package com.secretbetta.BASS.GoogleSheets;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class LeaderboardCommand extends Command {
	
	public LeaderboardCommand() {
		this.hidden = true;
		this.name = "leaderboard";
		this.arguments = "No args/<Player1> <Player2>";
		this.help = "Leaderboard scores between two players";
		// this.cooldown = 20;
	}
	
	/**
	 * Gets Leaderboard from Main Leaderboard Sheet
	 * 
	 * @return Formatted String for Leaderboard
	 * @throws IOException
	 * @throws GeneralSecurityException
	 * @see LeaderboardSheet#getLeaderboard()
	 */
	public static String getLeaderboard() throws IOException, GeneralSecurityException {
		LeaderboardSheet sheets = new LeaderboardSheet();
		return sheets.getLeaderboard();
	}
	
	/**
	 * Gets Player VS Player List of scores
	 * 
	 * @param name1 Player 1
	 * @param name2 Player 2
	 * @return Formatted string of scores
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @see LeaderboardSheet#getPlayer(String, String)
	 */
	public static String getPlayerVS(String name1, String name2) throws GeneralSecurityException, IOException {
		LeaderboardSheet sheets = new LeaderboardSheet();
		return sheets.getPlayer(name1, name2);
	}
	
	/**
	 * Shows main leaderboard of PvP scores
	 * 
	 * @param content Names of players
	 * @return Message to send to discord chat
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * @see #getPlayerVS(String, String)
	 * @see #getLeaderboard()
	 */
	public static String leaderboardShow(String player1, String player2) throws GeneralSecurityException, IOException {
		return getPlayerVS(player1, player2);
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		if (event.getArgs().length() == 0) {
			try {
				event.reply(LeaderboardCommand.getLeaderboard());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		
		String[] players = event.getArgs().split(" ");
		
		switch (players.length) {
			case 1: // Not enough names
				event.reply("Not enough names provided");
				break;
			case 2: // Do VS command
				try {
					event.reply(LeaderboardCommand.getPlayerVS(players[0], players[1]));
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					event.reply("Couldn't get leaderboard");
				}
				break;
		}
	}
}
