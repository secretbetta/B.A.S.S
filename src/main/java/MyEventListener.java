import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * EventListener for Discord
 * 
 * @author Andrew
 */
public class MyEventListener extends ListenerAdapter {
	
	final static MovieQuoter mquotes = new MovieQuoter("marvelquotes.txt");
	
	private List<String> trollIds = new ArrayList<>();
	
	public MyEventListener() {
		this.trollIds.add("268480279746838529"); // Sun's id
		// trollIds.add("400805008276193290"); // Justine's id
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
	public static String leaderboardShow(String content) throws GeneralSecurityException, IOException {
		if (content.split("\\s+").length >= 2 && !content.split("\\s+")[1].equals("-debug")) {
			String[] names = content.split("\\s+");
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
			newContent += (int) (Math.random() * 2) == 0 ? Character.toLowerCase(content.charAt(i))
				: Character.toUpperCase(content.charAt(i));
		}
		
		return newContent;
	}
	
	/**
	 * Adds suggestions to my list of things to do
	 * 
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
	@Override
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
		 * Funfacts about discord bot as of 11/6/2019 2:20pm
		 */
		if (content.equals("~~funfacts")) {
			channel.sendMessage("Fun facts!").addFile(new File("pictures/funfacts.jpg")).queue();
		}
		
		/**
		 * Outputs list of possible commands
		 */
		if (content.startsWith("~~help")) {
			for (EmbedBuilder eb : HelpCmds.helpCmds()) {
				channel.sendMessage(eb.build()).queue();
			}
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
			channel.sendMessage("Your id is: " + objMember.getId()).queue();
		}
		
		if (content.equals("~~img")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Image");
			try {
				List<String> imgs = WebCrawler.cleanImgs(WebCrawler.getImgs(new URL("https://www.reddit.com/")));
				eb.setImage(imgs.get(0));
				System.err.println(imgs.size());
			} catch (MalformedURLException e) {
				System.err.println("No image found");
			} catch (IndexOutOfBoundsException e) {
				System.err.println("Index out of bounds");
			}
			channel.sendMessage(eb.build()).queue();
		}
		
		/**
		 * Shows Leaderboard
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
		 * Gets puppies from reddit.com/r/puppies
		 */
		if (content.equals("~~puppies")) {
			// TODO use webcrawler class for this
		}
		
		/**
		 * Spongebob command
		 */
		if (content.startsWith("~~spongebob") && objMember.hasPermission(Permission.ADMINISTRATOR)) {
			List<Member> members = message.getMentionedMembers();
			for (Member member : members) {
				if (!this.trollIds.contains(member.getId())) {
					this.trollIds.add(member.getId());
					channel.sendMessage(String.format("%s has been added",
						member.getEffectiveName())).queue();
				} else {
					this.trollIds.remove(member.getId());
					channel.sendMessage(String.format("%s has been removed",
						member.getEffectiveName())).queue();
				}
			}
		} else if (content.startsWith("~~spongebob") && !objMember.hasPermission(Permission.ADMINISTRATOR)) {
			channel.sendMessage("You do not have permission to run this command.").queue();
		}
		
		/**
		 * Suggestions command, allows users to add suggestions
		 */
		if (content.startsWith("~~suggestion")) {
			try {
				if (content.length() > 13) {
					suggestions(content.substring(13));
					channel.sendMessage(String.format("Suggestion \"%s\" was added",
						content.substring(13))).queue();
				} else {
					channel.sendMessage("Add a command suggestion by using the command"
						+ "\n~~suggestion <suggestion>").queue();
				}
			} catch (IOException e) {
				System.err.println("Cannot write into file");
			}
		}
		
		if (content.startsWith("~~pm")) {
			List<Member> members = message.getMentionedMembers();
			User user = members.get(0).getUser();
			user.openPrivateChannel().queue((ch) -> {
				channel.sendMessage("test").queue();
			});
		}
		
		/**
		 * Gets random xkcd comic
		 */
		if (content.equals("~~xkcd")) {
			try {
				xkcdCrawler xkcd = new xkcdCrawler();
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle(String.format("%s", xkcd.getXKCDtitle()));
				eb.setColor(Color.blue);
				eb.setImage(xkcd.getXKCDimg());
				eb.setDescription(xkcd.getXKCDdesc());
				eb.addField("", String.format("[Permanent link to comic](%s)", xkcd.getXKCDpermLink()), true);
				channel.sendMessage(eb.build()).queue();
			} catch (MalformedURLException e) {
				System.err.println("URL Exception");
			}
		}
		
		/**
		 * Debugger
		 */
		if (content.endsWith("-debug") && objMember.hasPermission(Permission.ADMINISTRATOR)) {
			Duration elapsed = Duration.between(start, Instant.now());
			double seconds = (double) elapsed.toMillis() / Duration.ofSeconds(1).toMillis();
			channel.sendMessage("Time Elapsed: " + seconds).queue();
		}
		
		if (content.equals("~~probability of people that stand above andrew")) {
			channel.sendMessage("Calculating...").queue();
			channel.sendTyping().queue();
			new java.util.Timer().schedule(
				new java.util.TimerTask() {
					
					@Override
					public void run() {
						channel.sendMessage("0%").queue();
					}
				},
				1000 * 10);
		}
		
		/** Automated Functions **/
		
		/**
		 * Trolls people using spongebob meme
		 */
		if (objMember != null && this.trollIds.contains(objMember.getId())) {
			channel.sendMessage(spongebobUpper(content)).queue();
		}
		
		/**
		 * If user @<mentions> people, gets told to shut up
		 */
		if (message.mentionsEveryone()) {
			channel.sendMessage(String.format("Shut up, %s I'm sleeping.", objMember.getAsMention())).queue();
		}
	}
}
