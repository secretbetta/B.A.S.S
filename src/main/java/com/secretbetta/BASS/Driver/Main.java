package com.secretbetta.BASS.Driver;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.examples.command.PingCommand;
import com.secretbetta.BASS.GoogleSheets.LeaderboardCommand;
import com.secretbetta.BASS.Minecraft.ConsoleEvent;
import com.secretbetta.BASS.Minecraft.ListVersionsCommand;
import com.secretbetta.BASS.Minecraft.ServerInfoCommand;
import com.secretbetta.BASS.Minecraft.StartServerCommand;
import com.secretbetta.BASS.Poker.PokerEvent;
import com.secretbetta.BASS.Tetris.TetrisShowcase;
import com.secretbetta.BASS.Translation.LanguageListCommand;
import com.secretbetta.BASS.Translation.TranslationEvent;
import com.secretbetta.BASS.VC.PrivateVCEvent;
import com.secretbetta.BASS.VC.VCNotificationEvent;
import com.secretbetta.BASS.blackjack.BlackjackEvent;
import com.secretbetta.BASS.debug.AdminTestCommand;
import com.secretbetta.BASS.debug.BugReportCommand;
import com.secretbetta.BASS.debug.FunFactCommand;
import com.secretbetta.BASS.debug.IDCommand;
import com.secretbetta.BASS.debug.SuggestionsCommand;
import com.secretbetta.BASS.debug.TestCommand;
import com.secretbetta.BASS.epicgames.EpicGamesCommand;
import com.secretbetta.BASS.tictactoe.TicTacToeEvent;
import com.secretbetta.BASS.utilities.HelpCommand;
import com.secretbetta.BASS.utilities.PinnerCommand;
import com.secretbetta.BASS.utilities.TimerCommand;
import com.secretbetta.BASS.xkcd.XKCDCommand;
import com.secretbetta.BASS.yahtzeeGame.YahtzeeEvent;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
			JDA api = JDABuilder.createDefault(args[0]).build();
			
			client.setPrefix("~~");
			client.setAlternativePrefix("b/");
			client.setActivity(Activity.playing("I'm secretbetta's bot :woke:"));
			
			client.setEmojis("✔", "⚠", "❌");
			client.setOwnerId("268511458801745921");
			client.useDefaultGame();
			client.useHelpBuilder(false);
			
			client.addCommands(
				new ServerInfoCommand(),
				new PingCommand(), // Jagrosh's Ping Command
				new LeaderboardCommand(),
				new XKCDCommand(),
				new SuggestionsCommand(),
				new IDCommand(),
				new FunFactCommand(),
				new AdminTestCommand(),
				new TestCommand(),
				new BugReportCommand(),
				new PinnerCommand(),
				new StartServerCommand(),
				new ListVersionsCommand(),
				new repeatCommand(),
				new HelpCommand(),
				new TetrisShowcase(),
				new TimerCommand(),
				new trollJustine(),
				new EpicGamesCommand(),
				new LanguageListCommand(),
				new PrivateVCEvent().new PrivateVCAdd(),
				new PrivateVCEvent().new PrivateVCChangeHost(),
				new PrivateVCEvent().new PrivateVCChangeName(),
				new PrivateVCEvent().new PrivateVCHelp(),
				new PrivateVCEvent().new PrivateVCHide(),
				new PrivateVCEvent().new PrivateVCShow(),
				new PrivateVCEvent().new PrivateVCPublic(),
				new PrivateVCEvent().new PrivateVCPrivate(),
				new PrivateVCEvent().new PrivateVCLimit());
			
			// api2.getPresence().setStatus(OnlineStatus.ONLINE);
			// api2.get
			api.addEventListener(new MyEventListener(), // Main Events TODO Remove and deprecate
				new TicTacToeEvent(), // Tic Tac Toe Event
				new RockPaperScissorsEvent(), // Rock Paper Scissors Event
				new YahtzeeEvent(), // Yahtzee Event
				new BlackjackEvent(), // Blackjack Event
				new PokerEvent(), // Poker Event
				new ConsoleEvent(), // Minecraft Console
				new TestEvent(),
				new TranslationEvent(),
				new PrivateVCEvent(),
				new VCNotificationEvent(),
				// new ProfanityFilterEvent(), // Profanity filter
				waiter, client.build()); // Other Commands
			// api2.addEventListener(new TestEvent()); // Testing Events
			// api.addEventListener(new EmotesEvent()); // Emotes Testing Event
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
