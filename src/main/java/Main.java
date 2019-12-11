import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.secretbetta.BASS.GoogleSheets.LeaderboardCommand;
import com.secretbetta.BASS.Minecraft.ServerInfo;
import com.secretbetta.BASS.Poker.PokerEvent;
import com.secretbetta.BASS.blackjack.BlackjackEvent;
import com.secretbetta.BASS.xkcd.XKCDCommand;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

/**
 * Discord Bot Loader
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
			
			CommandClientBuilder client = new CommandClientBuilder();
			EventWaiter waiter = new EventWaiter();
			
			client.setPrefix("~~");
			client.setAlternativePrefix("b/");
			
			client.setEmojis("✔", "⚠", "❌");
			client.setOwnerId("268511458801745921");
			client.useDefaultGame();
			
			client.addCommand(new ServerInfo());
			client.addCommand(new LeaderboardCommand());
			client.addCommand(new XKCDCommand());
			// client.addCommand(new PokerCommand(waiter));
			
			System.out.println("Running B.A.S.S Bot");
			JDA api = new JDABuilder(AccountType.BOT)
				.setToken(args[0])
				.setActivity(Activity.playing("try ~~help"))
				.build();
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
			System.out.println("Finished running");
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
}
