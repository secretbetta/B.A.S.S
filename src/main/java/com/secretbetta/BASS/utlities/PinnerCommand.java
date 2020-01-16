package com.secretbetta.BASS.utlities;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * Pinner command for A.S.S Server
 * 
 * @author Andrew
 */
public class PinnerCommand extends Command {
	
	/**
	 * Default channels for A.S.S. Server
	 */
	private String pinnedChannel = "666579340409962496"; // pins channel
	private String privatePinnedChannel = "667501237121450004"; // private pins channel
	private String channelID = "655686387995246623"; // the-only-kids-weve-ever-wanted
	private JDA api;
	
	public PinnerCommand(JDA api) {
		this.name = "pin";
		this.help = "Pins messages to pinned channel";
		this.cooldown = 10;
		this.hidden = true;
		this.api = api;
	}
	
	/**
	 * Pinned Message Builder
	 * 
	 * @param content Content of message
	 * @param url     URL of message
	 * @param time    When was the msg created
	 * @param author  Author of message
	 * @param channel The channel it was sent in
	 * @param image   URL of image if there is an image
	 * @return Embedded Message builder
	 */
	public EmbedBuilder message(String content, String url, OffsetDateTime time, String author, String channel,
		String image) {
		EmbedBuilder msg = new EmbedBuilder();
		msg.setTitle(content, url);
		msg.setTimestamp(time);
		msg.addField("- " + author, "In channel: " + channel, false);
		if (!image.equals("null")) {
			msg.setImage(image);
		}
		return msg;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		System.out.println(event.getMessage().getTimeCreated());
		
		String msg = null;
		List<Long> ids = new ArrayList<>();
		msg = event.getArgs();
		String regex = "/(\\d*)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(msg);
		while (matcher.find()) {
			if (!matcher.group(1).isEmpty()) {
				ids.add(Long.parseLong(matcher.group(1)));
			}
			
		}
		TextChannel pinned = this.api.getTextChannelById(this.pinnedChannel);
		if (event.getChannel().getId().equals(this.channelID)) {
			pinned = this.api.getTextChannelById(this.privatePinnedChannel);
		} else {
			pinned = this.api.getTextChannelById(this.pinnedChannel);
		}
		
		MessageChannel hist = event.getChannel();
		Message message = hist.retrieveMessageById(ids.get(2)).complete();
		List<Attachment> images = message.getAttachments();
		
		if (images.size() >= 1) {
			pinned
				.sendMessage(this
					.message(images.get(0).getFileName(), event.getArgs(),
						hist.getTimeCreated(), message.getAuthor().getName(), message.getChannel().getName(),
						images.get(0).getUrl())
					.build())
				.queue();
		} else {
			pinned
				.sendMessage(this
					.message(message.getContentDisplay(), event.getArgs(),
						hist.getTimeCreated(), message.getAuthor().getName(), message.getChannel().getName(), "null")
					.build())
				.queue();
		}
	}
	
}
