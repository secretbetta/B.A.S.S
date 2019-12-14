package com.secretbetta.BASS.spongebob;

import java.io.IOException;
import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class AddPlayerCommand extends Command {
	
	Spongebob troller;
	
	public AddPlayerCommand() {
		Permission[] perms = new Permission[1];
		perms[0] = Permission.ADMINISTRATOR;
		
		this.name = "spongebob";
		this.help = "Adds player to list of people to troll on spongebob meme";
		this.userPermissions = perms;
		this.hidden = true;
		try {
			this.troller = new Spongebob();
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		Message message = event.getMessage();
		List<Member> members = message.getMentionedMembers();
		for (Member member : members) {
			try {
				switch (this.troller.changeList(member.getId())) {
					case 0:
						event.reply(member.getEffectiveName() + " has been removed.");
						break;
					case 1:
						event.reply(member.getEffectiveName() + " has been added.");
						break;
				}
			} catch (IOException e) {
				System.err.println(e.getLocalizedMessage());
			}
		}
	}
	
}
