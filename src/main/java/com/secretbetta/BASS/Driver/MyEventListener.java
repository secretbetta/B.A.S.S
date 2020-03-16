package com.secretbetta.BASS.Driver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import com.secretbetta.BASS.utlities.WebCrawler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * EventListener for Discord
 * Soon to be a test event listener for all things miscellaneous
 * 
 * @author Andrew
 */
public class MyEventListener extends ListenerAdapter {
	
	final static MovieQuoter mquotes = new MovieQuoter("marvelquotes.txt");
	
	// private List<String> trollIds = new ArrayList<>();
	//
	// public MyEventListener() {
	// this.trollIds.add("268480279746838529"); // Sun's id
	// // trollIds.add("400805008276193290"); // Justine's id
	// }
	//
	// /**
	// * ManIpuLAtES STrINg tO RaNdom UPperCasE aND LoWErcAse WordS
	// *
	// * @param content Content to manipulate
	// * @return Manipulated String
	// */
	// private static String spongebobUpper(String content) {
	// String newContent = "";
	//
	// for (int i = 0; i < content.length(); i++) {
	// newContent += (int) (Math.random() * 2) == 0 ? Character.toLowerCase(content.charAt(i))
	// : Character.toUpperCase(content.charAt(i));
	// }
	//
	// return newContent;
	// }
	
	HashSet<String> lines = new HashSet<>();
	
	public MyEventListener() {
		try {
			BufferedReader myReader = Files.newBufferedReader(Paths.get("justine.txt"));
			String line;
			while ((line = myReader.readLine()) != null) {
				this.lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean addVerb(String content) throws IOException {
		if (this.lines.add(content)) {
			String path = "justine.txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
			writer.append(String.format("%s\n", content));
			writer.close();
			return true;
		}
		return false;
	}
	
	/**
	 * Message Handler
	 */
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) { // or if andrew types anything
			return;
		}
		
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		Instant start = Instant.now();
		
		/** Commands **/
		
		/**
		 * Outputs list of possible commands
		 */
		if (content.startsWith("~~help")) {
			for (EmbedBuilder eb : HelpCmds.helpCmds()) {
				channel.sendMessage(eb.build()).queue();
			}
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
		
		/*
		 * Gets random movie quote
		 */
		if (content.startsWith("~~mquote")) {
			channel.sendMessage(MyEventListener.mquotes.getRandomQuote()).queue();
		}
		
		/**
		 * Gets puppies from reddit.com/r/puppies
		 */
		if (content.equals("~~puppies")) {
			// use webcrawler class for this
		}
		
		if (objMember.getId().equals("169281100856819712")) {
			// channel.sendMessage("fk you Peter").queue();
			return;
		}
		
		if (content.startsWith("~~addjustine ") && !content.contains("andrew")) {
			try {
				if (this.addVerb(content.replace("~~addjustine ", ""))
					&& !content.replaceAll("[\\W ]*?", "").equals("")) {
					channel
						.sendMessage(String.format("\"*%s*\" has been added to Justine's list of verbs.",
							content.replace("~~addjustine ", "").replaceAll("[\\W ]*?", "")))
						.queue();
					return;
				} else {
					channel.sendMessage("Error, did not add.").queue();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (content.contains("justine")) {
			channel
				.sendMessage(
					String.format("Justine %s.", this.lines.toArray()[(int) (Math.random() * this.lines.size())]))
				.queue();
		}
		
		/**
		 * Spongebob command
		 */
		// if (content.startsWith("~~spongebob") && objMember.hasPermission(Permission.ADMINISTRATOR)) {
		// List<Member> members = message.getMentionedMembers();
		// for (Member member : members) {
		// if (!this.trollIds.contains(member.getId())) {
		// this.trollIds.add(member.getId());
		// channel.sendMessage(String.format("%s has been added",
		// member.getEffectiveName())).queue();
		// } else {
		// this.trollIds.remove(member.getId());
		// channel.sendMessage(String.format("%s has been removed",
		// member.getEffectiveName())).queue();
		// }
		// }
		// } else if (content.startsWith("~~spongebob") && !objMember.hasPermission(Permission.ADMINISTRATOR)) {
		// channel.sendMessage("You do not have permission to run this command.").queue();
		// }
		
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
		// if (objMember != null && this.trollIds.contains(objMember.getId())) {
		// channel.sendMessage(spongebobUpper(content)).queue();
		// }
		
		/**
		 * If user @<mentions> people, gets told to shut up
		 */
		if (message.mentionsEveryone()) {
			channel.sendMessage(String.format("Shut up, %s I'm sleeping.", objMember.getAsMention())).queue();
		}
	}
}
