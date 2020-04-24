package com.secretbetta.BASS.Tetris;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

public class TetrisShowcase extends Command {
	
	public TetrisShowcase() {
		super.name = "tetris";
		super.cooldown = 30;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**Tetris**");
		eb.setDescription(""
			+ "```"
			+ "[    ]\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\t[ | | ]\n"
			+ "[    ]\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\t[ |Z|Z]\n"
			+ "[    ]\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\t[Z|Z| ]\n"
			+ " Hold \t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][O][O][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][O][O][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][ ][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][ ][S][ ][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[ ][T][S][S][ ][ ][ ][ ][ ][ ]\n"
			+ "  \t\t[T][T][T][S][🟦][🟦][🟦][🟦][ ][ ]\n"
			+ "```")
			.addBlankField(false)
			.addField("Score", "50", false)
			.addField("Lines Cleared", "4", false)
			.addField("Level", "2", false);
		Message msg = event.getChannel().sendMessage(eb.build()).complete();
		msg.addReaction("⬅️").queue();
		msg.addReaction("➡️").queue();
		msg.addReaction("⬆️").queue();
		msg.addReaction("⬇️").queue();
		msg.addReaction("⏬").queue();
		msg.addReaction("↩️").queue();
		msg.addReaction("↪️").queue();
		msg.addReaction("U+1f7ea").queue();
	}
	/*
	 * XXXX
	 * XXTX U+1f7ea
	 * XTTT
	 * 
	 * XSXX
	 * XSSX U+1f7e9
	 * XXSX
	 * 
	 * XXXX
	 * LLLL U+1f7e6
	 * XXXX
	 * 
	 * XXZX
	 * XZZX U+1f7e5
	 * XZXX
	 * 
	 * XXXX
	 * XOOX U+1f7e8
	 * XOOX
	 * 
	 * XLXX
	 * XLXX U+1f7eb
	 * XLLX
	 * 
	 * XXJX
	 * XXJX U+1f7e7
	 * XJJX
	 */
	
}
