package com.secretbetta.BASS.Translation;

import java.util.List;

import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Receives Message Events and Translates them if
 * Google Translate API Detects that it is not English
 * 
 * @author Secretbeta
 */
public class TranslationEvent extends ListenerAdapter {
	
	Translate translate = TranslateOptions.getDefaultInstance().getService();
	private String channelID;
	private TranslateOption option = Translate.TranslateOption.targetLanguage("en");
	
	/**
	 * Creates a Translation Event Listener. Extends {@link ListenerAdapter}.
	 */
	public TranslationEvent() {
		this.channelID = "774465708230574120";
	}
	
	public TranslationEvent(String chnlID, String lang) {
		this.channelID = chnlID;
		if (isLanguage(lang)) {
			this.option = Translate.TranslateOption.targetLanguage(lang);
		}
	}
	
	/**
	 * Checks if lang is in list of supported languages
	 * 
	 * @param lang Language to check
	 * @return True if supported, false if not
	 */
	public boolean isLanguage(String lang) {
		List<Language> languages = translate.listSupportedLanguages();
		for (Language language : languages) {
			if (language.getCode().equals(lang)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		MessageChannel channel = event.getChannel();
		String content = event.getMessage().getContentRaw();
		
		if (!channel.getId().equals(this.channelID)) {
			return;
		}
		
		Translation translation = translate.translate(content, this.option);
		String src = translation.getSourceLanguage();
		
		if (src.equals("en")) {
			return;
		}
		
		String translatedText = translation.getTranslatedText();
		if (translatedText.contains("&#39;")) {
			translatedText.replace("(&#39;)", "'");
		}
		translatedText.replaceAll("&quot;", "\"");
		channel.sendMessage("Translated Text from " + src + " to English: " + translatedText)
			.queue();
	}
	
}
