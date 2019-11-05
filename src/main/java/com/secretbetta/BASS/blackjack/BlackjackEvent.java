package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Blackjack event
 * 
 * @author Andrew
 */
public class BlackjackEvent extends ListenerAdapter {
	
	private boolean game = false;
	private Blackjack blackjack;
	private List<Member> players;
	private int[] rounds; // TODO Max number of cards in hand = 5
	private boolean[] stays;
	private int[] sums;
	private MessageChannel chnl;
	
	/**
	 * Creates a new game and initializes everything
	 * 
	 * @param players Number of players
	 * @param users   List of plauers
	 */
	public void newGame(int players, List<Member> users) {
		this.blackjack = new Blackjack(players);
		this.players = users;
		this.game = true;
		this.blackjack.shuffle();
		this.blackjack.dealAll(2);
		this.rounds = new int[players];
		this.stays = new boolean[players];
		this.chnl = null;
		this.sums = new int[players];
	}
	
	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Message message = event.getMessage();
		String content = message.getContentRaw();
		MessageChannel channel = event.getChannel();
		this.chnl = channel;
		
		/**
		 * Main game command
		 */
		if (content.startsWith("~~blackjack") && !game) {
			if (message.getMentionedMembers().size() > 0) {
				List<Member> players = new ArrayList<>();
				players.add(message.getMember());
				players.addAll(message.getMentionedMembers());
				newGame(players.size(), players);
				channel.sendMessage("Welcome to BlackJack! The players are:\n").queue();
				int p = 0;
				
				// Sends msg to each player
				for (Member player : players) {
					channel.sendMessage(String.format("%s\n", player.getNickname())).queue();
					this.sums[p] = blackjack.addCards(p);
					player.getUser()
						.openPrivateChannel()
						.complete()
						.sendMessage(String.format("Your cards are: %s\n"
							+ "Click [1] to hit. Click [2] to stay.", this.blackjack.getHand(p).toString()))
						.queue();
					p++;
				}
			} else {
				channel.sendMessage("Invalid command. Correct usage:\n"
					+ "~~blackjack <@mention>...\n"
					+ "Multiple mentions possible. Up to 8 total players").queue();
			}
		}
		
		if (game) { // Checks to see if everyone is ready
			for (boolean stay : this.stays) {
				if (!stay) {
					return;
				}
			}
			
			channel.sendMessage(this.blackjack.winner()).queue();
			game = false;
		}
	}
	
	@Override
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			if (event.getMessage().getContentRaw().startsWith("Your cards are: ")
				&& event.getMessage().getContentRaw().contains("Click")) {
				event.getMessage().addReaction("U+31U+20e3").queue();
				event.getMessage().addReaction("U+32U+20e3").queue();
			}
		}
	}
	
	@Override
	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		
		boolean flag = false;
		
		for (Member mmb : this.players) {
			if (mmb.getId().equals(event.getUser().getId())) {
				flag = true;
			}
		}
		
		if (game && flag) {
			ReactionEmote choice = event.getReactionEmote();
			
			/**
			 * Reactions of 1 or 2
			 */
			if (choice.getAsCodepoints().equals("U+31U+20e3")) { // Deal more
				for (int x = 0; x < this.players.size(); x++) {
					if (this.players.get(x).getId().equals(event.getUser().getId())) {
						this.blackjack.deal(1, x);
						this.sums[x] = this.blackjack.addCards(x);
						
						/**
						 * Does the hand check
						 */
						if (this.sums[x] < 21) { // Still playable
							event.getChannel().sendMessage(String.format("Your cards are: %s"
								+ "Click [1] to hit. Click [2] to stay.", this.blackjack.getHand(x).toString()))
								.queue();
						} else if (this.sums[x] > 21) { // Busted, readies up
							event.getChannel()
								.sendMessage(String.format("Your cards are: %s", this.blackjack.getHand(x).toString()))
								.queue();
							event.getChannel().sendMessage("You busted!").queue();
							this.stays[x] = true;
							this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention()))
								.queue();
						} else { // Automatic stay for hand = 21
							event.getChannel().sendMessage("Good job! You got 21. Now to wait for the others").queue();
							this.stays[x] = true;
							this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention()))
								.queue();
						}
						return;
					}
				}
			} else if (choice.getAsCodepoints().equals("U+32U+20e3")) { // Stay
				for (int x = 0; x < this.players.size(); x++) {
					if (this.players.get(x).getId().equals(event.getUser().getId())) {
						this.stays[x] = true;
						this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention())).queue();
						break;
					}
				}
			}
		}
	}
}
