package com.secretbetta.BASS.epicgames;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.secretbetta.BASS.utilities.WebCrawler;

/**
 * <h1>HTML crawler to find latest game</h1>
 * Crawls html at <a href="https://www.pcgamer.com/epic-games-store-free-games-list/">PCGamer</a>
 * site and fetches latest game from the list.
 * 
 * @author Secretbeta
 */
public class EpicGamesCrawler {
	
	private String html;
	
	/**
	 * Fetches website that has list of free Epic Games
	 * 
	 * @throws MalformedURLException
	 */
	public EpicGamesCrawler() throws MalformedURLException {
		this.html = WebCrawler
			.fetchHTML(new URL("https://www.pcgamer.com/epic-games-store-free-games-list/"));
	}
	
	/**
	 * Gets the latest game using a specific regex. Might break;
	 * 
	 * @return null if could not find any matching regex, returns the latest game and date if it
	 *         exists
	 */
	public String latestGame() {
		String regex = "(?is)<strong>(.*?)</strong>(.*?)</a>";
		Pattern pattern = Pattern.compile(regex);
		
		if (this.html != null) {
			Matcher matcher = pattern.matcher(this.html);
			if (matcher.find()) {
				return matcher.group(0).replaceAll("<.*?>", "");
			}
		}
		return null;
	}
}
