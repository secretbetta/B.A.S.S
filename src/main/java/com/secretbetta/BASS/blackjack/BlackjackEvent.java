package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
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
	
	/**
	 * TODO Timer for each player. If timer is up, that player automatically forfeits
	 */
	
	private boolean game = false;
	private Blackjack blackjack;
	private List<Member> players;
	private ArrayList<Message> privatemsg;
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
		this.privatemsg = new ArrayList<>();
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
			if (message.getMentionedMembers().size() > 0) { // Checks if multiplayer
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
					
					EmbedBuilder emb = new EmbedBuilder();
					emb.setTitle("BlackJack");
					emb.setDescription("**Cards** " + this.blackjack.getHand(p).toString());
					emb.addField("", "Click :one: to hit. Click :two: to stay.", false);
					player.getUser().openPrivateChannel().complete().sendMessage(emb.build()).queue();
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
			
			channel.sendMessage(this.blackjack.winner()).queue(); // TODO Might need a new method in blackjack as a win
																	// method
			for (int p = 0; p < this.players.size(); p++) {
				this.chnl.sendMessage(String.format("%s's Hand: %s", this.players.get(p).getNickname(),
					this.blackjack.getHand(p).toString())).queue(); // TODO Embed!!!
			}
			game = false;
		}
	}
	
	@Override
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		this.privatemsg.add(event.getMessage());
		if (event.getAuthor().isBot()) {
			if (event.getMessage().getEmbeds().size() > 0) {
				if (event.getMessage().getEmbeds().get(0).getTitle().equals("BlackJack")) {
					event.getMessage().addReaction("U+31U+20e3").queue();
					event.getMessage().addReaction("U+32U+20e3").queue();
				}
			}
		}
	}
	
	@Override
	public void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {
		if (event.getUser().isBot()) {
			return;
		}
		
		boolean flag = false;
		
		for (Member mmb : this.players) { // TODO Change this.players to list of Users and not Members
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
					if (!this.stays[x] && this.players.get(x).getId().equals(event.getUser().getId())) {
						this.blackjack.deal(1, x);
						this.sums[x] = this.blackjack.addCards(x);
						
						/**
						 * Does the hand check
						 * TODO Clean this shit up lmao
						 */
						EmbedBuilder emb = new EmbedBuilder();
						emb.setTitle("BlackJack");
						emb.setDescription("**Cards** " + this.blackjack.getHand(x).toString());
						
						if (this.sums[x] < 21) { // Still playable
							emb.addField("", "Click :one: to hit. Click :two: to stay.", false);
							this.privatemsg.get(x).editMessage(emb.build()).queue();
							// this.privatemsg.get(x).clearReactions().queue();
							this.privatemsg.get(x).addReaction("U+31U+20e3").queue();
							this.privatemsg.get(x).addReaction("U+32U+20e3").queue();
						} else if (this.sums[x] > 21) { // Busted, automatic ready up
							emb.addField("", "You busted!", false);
							this.privatemsg.get(x).editMessage(emb.build()).queue();
							this.stays[x] = true;
							this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention()))
								.queue();
						} else { // Automatic stay for hand = 21
							emb.addField("", "You got 21!", false);
							this.privatemsg.get(x).editMessage(emb.build()).queue();
							this.stays[x] = true;
							this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention()))
								.queue();
						}
						return;
					}
				}
			} else if (choice.getAsCodepoints().equals("U+32U+20e3")) { // Stay
				for (int x = 0; x < this.players.size(); x++) { // TODO This could probably be in a method or something
					// Also I could probably embed this...
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
