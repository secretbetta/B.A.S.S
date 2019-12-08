package com.secretbetta.BASS.spongebob;

import java.util.List;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class AddPlayerCommand extends Command {
	
	Spongebob troller;
	
	public AddPlayerCommand() {
		this.name = "spongebob";
		this.help = "Adds player to list of peope to troll on spongebob meme";
		this.hidden = true;
		this.troller = new Spongebob();
		// this.userPermissions = ;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		
		Message message = event.getMessage();
		List<Member> members = message.getMentionedMembers();
		for (Member member : members) {
			switch (this.troller.changeList(member.getId())) {
				case 0:
					event.reply(member.getEffectiveName() + " has been removed.");
					break;
				case 1:
					event.reply(member.getEffectiveName() + " has been added.");
					break;
			}
		}
	}
	
}
