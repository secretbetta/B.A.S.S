package com.secretbetta.BASS.epicgames;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.secretbetta.BASS.utilities.WebCrawler;

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
	 * Fetches
	 * 
	 * @param url URL of Website
	 */
	public EpicGamesCrawler(URL url) {
		this.html = WebCrawler.fetchHTML(url);
	}
	
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
