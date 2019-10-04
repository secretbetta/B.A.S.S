import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * EventListener for Discord
 * @author Andrew
 *
 */
public class MyEventListener extends ListenerAdapter {
	
	final static MovieQuoter mquotes = new MovieQuoter("marvelquotes.txt");
	
	/**
	 * Gets Leaderboard from Main Leaderboard Sheet
	 * 
	 * @return Formatted String for Leaderboard
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException 
	 * 
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
	 * 
	 * @return Formatted string of scores
	 * 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * 
	 * @see LeaderboardSheet#getPlayer(String, String)
	 */
	public static String getPlayerVS(String name1, String name2) throws GeneralSecurityException, IOException {
		LeaderboardSheet sheets = new LeaderboardSheet();
		return sheets.getPlayer(name1, name2);
	}

	/**
	 * Help command, outputs to channel all possible commands for bot
	 * @param channel Channel to output to
	 */
	public static void help(MessageChannel channel) {
		String[][] cmds = {
				{"~~help", "All possible commands"},
				{"~~leaderboard <Player 1> <Player 2>", "Shows leaderboard. Player flags are optional to print scores between two people"},
				{"~~id", "Gets unique user ID"},
				{"~~hello", "world"},
				{"~~test", "Gets bot's ping"},
				{"~~admin", "Tests admin privileges"},
				{"~~mquote", "Sends random movie quote"},
				{"~~suggestion [Command]", "Command suggestion for Andrew to make"},
				{""},
				{"-debug", "For admins only: Put at end of command for runtime"}
		};
		
		String content = "**Command List**\n```";
		
		for (int i = 0; i < cmds.length; i++) {
			content += cmds[i][0] + ", " + cmds[i][1] + "\n";
		}
		
		content += "```";
		
		channel.sendMessage(content).queue();
	}
	
	/**
	 * Shows main leaderboard of PvP scores
	 * 
	 * @param content Names of players
	 * @return Message to send to discord chat
	 * @throws GeneralSecurityException
	 * @throws IOException
	 * 
	 * @see #getPlayerVS(String, String)
	 * @see #getLeaderboard()
	 */
	public static String leaderboardShow(String content) throws GeneralSecurityException, IOException {
    	if (content.length() > 14) {
    		String[] names = content.split(" ");
    		if (names.length >= 3) {
    			return getPlayerVS(names[1], names[2]);
    		} else {
    			return "Invalid command input.\n"
    					+ "Format: ~~leaderboard <Player1> <Player2>";
    		}
    	} else {
    		return getLeaderboard();
    	}
	}

	/**
	 * ManIpuLAtES STrINg tO RaNdom UPperCasE aND LoWErcAse WordS
	 * 
	 * @param content Content to manipulate
	 * @return Manipulated String
	 */
	private static String spongebobUpper(String content) {
		String newContent = "";
		
		for (int i = 0; i < content.length(); i++) {
			newContent += (int)(Math.random()*2) == 0 ? 
					Character.toLowerCase(content.charAt(i)) : Character.toUpperCase(content.charAt(i));
		}
		
		return newContent;
	}

	/**
	 * Adds suggestions to my list of things to do
	 * @param suggestion
	 * @return
	 * @throws IOException
	 */
	public static void suggestions(String suggestion) throws IOException {
		String path = "suggestions.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
		writer.append(String.format(" * %s\n", suggestion));
		writer.close();
	}
	
    /**
	 * Message Handler
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		Instant start = Instant.now();
		
		/** Commands **/
		
		/**
		 * Administrator test
		 */
		if (content.startsWith("~~admin") && objMember.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("Congrats, you're an administrator").queue();
		} else if (content.startsWith("~~admin") && !objMember.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("You do not have permission for this command").queue();
		}
		
		/**
		 * Manipulating Google Sheets
		 */
		if (content.startsWith("~~leaderboard")) {
			try {
				channel.sendMessage(leaderboardShow(content)).queue();
			} catch (IOException e) {
				System.err.println("Player 1 cannot be found");
				channel.sendMessage("Player 1 not found").queue();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
				System.err.println("Wtf happened? O.o");
			}
		}
		
		/**
		 * Outputs list of possible commands
		 */
		if (content.startsWith("~~help")) {
			help(channel);
		}
		
		/**
		 * Hello World
		 */
		if (content.startsWith("~~hello")) {
			channel.sendMessage("world").queue();
		}
		
		/**
		 * Gets unique user ID
		 */
		if (content.startsWith("~~id")) {
			System.out.println(objMember.getId());
			channel.sendMessage("Your id is: " + objMember.getId()).queue();
		}
		
		/**
		 * Gets random movie quote
		 */
		if (content.startsWith("~~mquote")) {
			channel.sendMessage(mquotes.getRandomQuote()).queue();
		}
		
		/**
		 * Gets bot ping
		 */
		if (content.startsWith("~~ping")) {
			channel.sendMessage("Pong! + " + event.getJDA().getGatewayPing()).queue();
		}
		
		/**
		 * Plays rock paper scissors with opponent
		 */
		if (content.startsWith("~~rps")) {
			// TODO Rock Paper Scissors
		}
		
		/**
		 * Suggestions command, allows users to add suggestions
		 */
		if (content.startsWith("~~suggestion")) {
			try {
				if (content.length() > 13) {
					suggestions(content.substring(13));
				} else {
					channel.sendMessage("Add a command suggestion by using the command"
							+ "\n~~suggestion <suggestion>").queue();
				}
			} catch (IOException e) {
				System.err.println("Cannot write into file");
			}
		}
		
		/**
		 * Test message, changes
		 */
		if (content.startsWith("~~test")) {
			channel.sendMessage("This is a test message. Ping! Pong: "
					+ event.getJDA().getGatewayPing()).queue();
		}
		
		if (event.isFromType(ChannelType.PRIVATE)) {
			System.out.println("Private channel");
		}
		
		/**
		 * Plays tic tac toe with opponent
		 */
		if (content.startsWith("~~tictactoe")) {
			List<User> mentioned = message.getMentionedUsers();
			System.out.println(mentioned);
			if (mentioned.size() > 0) {
//				TicTacToe game = new TicTacToe();
				// TODO TicTacToe game. How to make it so the game 
				// runs on a different thread so other cmds can run 
				// while the game is running
			} else {
				channel.sendMessage("To play tictactoe use this command syntax"
						+ "\n~~tictactoe <@player>").queue();
			}
		}
		
		/**
		 * Debugger
		 */
		if (content.endsWith("-debug") && objMember.hasPermission(Permission.ADMINISTRATOR)) {
    		Duration elapsed = Duration.between(start, Instant.now());
    		double seconds = (double)elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
    		channel.sendMessage("Time Elapsed: " + seconds).queue();
    	}
		
		/** Automated Functions **/
		
		/**
		 * Trolls calculasians and jayheart
		 */
		if (objMember.getId().equals("268480279746838529") 
				|| objMember.getId().equals("400805008276193290")) {
			channel.sendMessage(spongebobUpper(content)).queue();
		}
	}
}
