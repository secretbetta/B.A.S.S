package com.secretbetta.BASS.Minecraft;

import java.io.File;
import java.util.ArrayList;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

/**
 * Lists all versions for Minecraft servers that I have installed.
 * 
 * @author Secretbeta
 */
public class ListVersionsCommand extends Command {
	
	private ArrayList<String> versions;
	private final File[] files = new File("D:\\Games\\Minecraft\\Minecraft Servers\\Start Servers")
		.listFiles();
	
	public ListVersionsCommand() {
		this.versions = new ArrayList<String>();
		this.getFiles(files);
		
		super.name = "mcversions";
		super.cooldown = 30;
		super.aliases = new String[] { "mcv" };
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		event.reply(this.getVersions());
	}
	
	/**
	 * List all files in given path. If path is a directory, it recursively calls itself to find
	 * more files. Else, it returns the file
	 * 
	 * @param files Path to get files
	 */
	private void getFiles(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				getFiles(file.listFiles());
			} else {
				if (file.getName().endsWith(".bat")) {
					this.versions.add(file.getName().replace("start", "").replace(".bat", ""));
				}
			}
		}
	}
	
	/**
	 * Compiles arraylist {@link #versions} into a String to print
	 * 
	 * @return List of versions separated by a newline
	 */
	private String getVersions() {
		String list = "```";
		for (String ver : this.versions) {
			list += ver + "\n";
		}
		list += "```";
		
		return list;
	}
	
}
