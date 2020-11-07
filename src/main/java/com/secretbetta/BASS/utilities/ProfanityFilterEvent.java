package com.secretbetta.BASS.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Profanity filter. Filters all profanity listed in PronfanityList.txt. Uses a hashmap to replace
 * key (curse) with value (replacement)
 * 
 * @author Secretbeta
 */
public class ProfanityFilterEvent extends ListenerAdapter {
	
	private HashMap<String, String> wordlist;
	
	public ProfanityFilterEvent() {
		this.wordlist = new HashMap<>();
		
		try {
			BufferedReader myReader = Files.newBufferedReader(Paths.get("ProfanityList.txt"));
			String line;
			while ((line = myReader.readLine()) != null) {
				// TODO add each line, separated by a space, into wordlist hashmap
				this.wordlist.put(line.split(" ")[0], line.split(" ")[1]);
			}
			
			System.out.println(wordlist);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		Member member = event.getMember();
		String content = message.getContentRaw();
		
		String newmsg = content;
		boolean prof = false;
		
		// Searches for profanity in content
		for (String word : content.split(" ")) {
			for (String profword : wordlist.keySet()) {
				if (word.toLowerCase().contains(profword)) {
					prof = true;
					newmsg = newmsg.replaceAll("(?i)" + profword,
						wordlist.get(profword));
				}
			}
		}
		
		// Replaces message if profanity is spotted
		if (prof) {
			message.delete().queue();
			channel.sendMessage(member.getEffectiveName() + " said : \"" + newmsg + "\"")
				.queue();
		}
	}
}
