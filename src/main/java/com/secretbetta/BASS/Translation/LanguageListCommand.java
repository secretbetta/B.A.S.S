package com.secretbetta.BASS.Translation;

import java.util.List;

import com.google.cloud.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * Gets list of support languages from Google Translate API
 * 
 * @author Secretbeta
 */
public class LanguageListCommand extends Command {
	
	Translate translate = TranslateOptions.getDefaultInstance().getService();
	private String list = "";
	
	public LanguageListCommand() {
		super.name = "languages";
		super.help = "List of all available languages";
		super.cooldown = 20;
		super.cooldownScope = CooldownScope.GUILD;
		super.aliases = new String[] { "lang", "langs" };
		
		langInit();
	}
	
	/**
	 * Initiates list of languages in {@link #list}
	 */
	public void langInit() {
		List<Language> languages = translate.listSupportedLanguages();
		for (Language language : languages) {
			this.list += "Name: " + language.getName() + ", Code: " + language.getCode() + "\n";
		}
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.reply(this.list);
	}
	
}
