package com.secretbetta.BASS.Cute;

import java.net.MalformedURLException;
import java.net.URL;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.secretbetta.BASS.utlities.WebCrawler;

import net.dv8tion.jda.api.EmbedBuilder;

public class PuppyCommand extends Command {
	
	private String html;
	private String img = "https://www.randomdoggiegenerator.com/randomdoggie.php";
	
	public PuppyCommand() {
		this.name = "puppy";
		this.help = "Gets random cute puppy images";
		this.cooldown = 0;
		
	}
	
	public void PuppyCrawler() throws MalformedURLException {
		String url = "https://www.randomdoggiegenerator.com/";
		this.html = WebCrawler.fetchHTML(new URL(url));
		
	}
	
	// public void getImg() {
	// String regex = "(?is)";
	// Pattern pattern = Pattern.compile(regex);
	//
	//
	//
	// this.img = "";
	// }
	
	public EmbedBuilder puppyShower() {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setTitle("Cute Puppy!");
		embed.setImage(this.img);
		embed.setFooter("Puppy Crawler by Secretbeta");
		
		return embed;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		event.reply(this.puppyShower().build());
	}
}
