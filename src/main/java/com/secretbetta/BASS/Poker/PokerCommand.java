package com.secretbetta.BASS.Poker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PokerCommand extends Command {
	
	private final EventWaiter waiter;
	
	private Poker game;
	
	public PokerCommand(EventWaiter waiter) {
		this.waiter = waiter;
		this.name = "Poker";
		this.guildOnly = true;
		this.arguments = "<@mention> <@mention> ...";
		this.help = "Initiate a game of poker";
		this.cooldown = 15;
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if (event.getArgs().isEmpty()) {
			event.replyWarning("You must mention at least one other player to play Poker!");
		} else if (event.getArgs().split(" ").length >= 11) {
			event.replyWarning("Too many players!");
		} else {
			List<Member> players = new ArrayList<>();
			players.add(event.getMember());
			players.addAll(event.getEvent().getMessage().getMentionedMembers());
			this.game = new Poker(players.size());
			this.game.dealAll(2);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setTitle("Poker Game");
			embed.setDescription(String.format("River: %s",
				this.game.getRiver().isEmpty() ? "Empty River" : this.game.getRiver().toString()));
			String playerHands = "";
			for (int x = 0; x < players.size(); x++) {
				playerHands += String.format("%s's Info: %s\n", players.get(x).getNickname(),
					this.game.getPlayerInfo(x));
			}
			
			embed.addField("Player List", playerHands, false);
			
			event.reply(embed.build());
			
			this.waiter.waitForEvent(GuildMessageReceivedEvent.class,
				// e -> e.getMessage().getEmbeds().size() > 1,
				e -> e.getAuthor().equals(event.getAuthor())
					&& e.getChannel().equals(event.getChannel())
					&& !e.getMessage().equals(event.getMessage())
					&& e.getMessage().getContentRaw().equals("test"),
				e -> {
					for (int x = 0; x < 3; x++) {
						event.getMessage().addReaction("U+3" + (x + 1) + "U+20e3");
					}
				});
			
			// this.waiter.waitForEvent(GuildMessageReactionAddEvent.class,
			// e -> e.getAuthor().equals(event.getAuthor())
			// && e.getChannel().equals(event.getChannel())
			// && !e.getMessage().equals(event.getMessage())
			// && e.getMessage().getContentRaw().equals("start"),
			// action);
			
			this.waiter.waitForEvent(GuildMessageReceivedEvent.class,
				e -> e.getAuthor().equals(event.getAuthor())
					&& e.getChannel().equals(event.getChannel())
					&& !e.getMessage().equals(event.getMessage())
					&& e.getMessage().getContentRaw().equals("start"),
				e -> event.reply(embed.build()),
				10, TimeUnit.SECONDS, () -> event.reply("Sorry, you took too long."));
		}
	}
	
}
