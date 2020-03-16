package com.secretbetta.BASS.Driver;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.secretbetta.BASS.Cute.PuppyCommand;
import com.secretbetta.BASS.GoogleSheets.LeaderboardCommand;
import com.secretbetta.BASS.Minecraft.ServerInfo;
import com.secretbetta.BASS.Poker.PokerEvent;
import com.secretbetta.BASS.blackjack.BlackjackEvent;
import com.secretbetta.BASS.debug.AdminTestCommand;
import com.secretbetta.BASS.debug.BugReportCommand;
import com.secretbetta.BASS.debug.FunFactCommand;
import com.secretbetta.BASS.debug.IDCommand;
import com.secretbetta.BASS.debug.SuggestionsCommand;
import com.secretbetta.BASS.debug.TestCommand;
import com.secretbetta.BASS.utlities.PinnerCommand;
import com.secretbetta.BASS.xkcd.XKCDCommand;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

/**
 * Discord Bot Driver
 * 
 * @author Andrew
 */
public class Main {
	
	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 0) {
				System.err.println("No token given");
				System.exit(1);
			}
			
			EventWaiter waiter = new EventWaiter();
			
			CommandClientBuilder client = new CommandClientBuilder();
			JDA api = new JDABuilder(AccountType.BOT)
				.setToken(args[0])
				.setActivity(Activity.playing("try ~~help"))
				.build();
			
			client.setPrefix("~~");
			client.setAlternativePrefix("b/");
			
			client.setEmojis("✔", "⚠", "❌");
			client.setOwnerId("268511458801745921");
			client.useDefaultGame();
			client.useHelpBuilder(false);
			
			client.addCommands(
				new ServerInfo(),
				new PingCommand(), // Jagrosh's Ping Command
				new LeaderboardCommand(),
				new XKCDCommand(),
				new SuggestionsCommand(),
				new IDCommand(),
				new FunFactCommand(),
				new AdminTestCommand(),
				new TestCommand(),
				new BugReportCommand(),
				new PuppyCommand(),
				new PinnerCommand(api));
			
			api.getPresence().setStatus(OnlineStatus.ONLINE);
			api.addEventListener(new MyEventListener()); // Main Events
			api.addEventListener(new TicTacToeEvent()); // Tic Tac Toe
			api.addEventListener(new RockPaperScissorsEvent()); // Rock Paper Scissors
			api.addEventListener(new YahtzeeEvent()); // Yahtzee
			api.addEventListener(new BlackjackEvent());
			api.addEventListener(new PokerEvent());
			api.addEventListener(waiter, client.build());
			// api.addEventListener(new TestEvent()); // Testing Events
			// api.addEventListener(new EmotesTestEvent()); // Emotes Testing Event
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
