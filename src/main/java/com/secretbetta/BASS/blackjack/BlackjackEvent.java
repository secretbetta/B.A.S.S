package com.secretbetta.BASS.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.User;
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
	private List<User> players;
	private ArrayList<Message> privatemsg;
	// private int[] rounds;
	private boolean[] stays;
	private int[] sums;
	private MessageChannel chnl;
	
	/**
	 * Creates a new game and initializes everything
	 * 
	 * @param players Number of players
	 * @param users   List of plauers
	 */
	public void newGame(int players, List<User> users) {
		this.blackjack = new Blackjack(players);
		this.players = users;
		this.game = true;
		this.blackjack.shuffle();
		this.blackjack.dealAll(2);
		// this.rounds = new int[players];
		this.stays = new boolean[players];
		this.chnl = null;
		this.sums = new int[players];
		this.privatemsg = new ArrayList<>();
	}
	
	public EmbedBuilder getHand(int player) {
		EmbedBuilder emb = new EmbedBuilder();
		emb.setTitle("BlackJack");
		emb.setDescription("**Cards**: ");
		for (String card : this.blackjack.getHand(player)) {
			emb.appendDescription(card + " ");
		}
		
		return emb;
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
			// Checks if multiplayer
			if (message.getMentionedMembers().size() > 0 && message.getMentionedMembers().size() <= 8) {
				List<User> players = new ArrayList<>();
				players.add(message.getAuthor());
				players.addAll(message.getMentionedUsers());
				newGame(players.size(), players);
				
				channel.sendMessage("Welcome to BlackJack! The players are:\n").queue();
				int p = 0;
				
				// Sends msg to each player
				for (User player : players) {
					channel.sendMessage(String.format("%s\n", player.getAsMention())).queue();
					this.sums[p] = blackjack.addCards(p);
					
					EmbedBuilder emb = getHand(p);
					emb.addField("", "Click :one: to hit. Click :two: to stay.", false);
					player.openPrivateChannel().complete().sendMessage(emb.build()).queue();
					p++;
				}
			} else {
				channel.sendMessage("Invalid command. Correct usage:\n"
					+ "~~blackjack <@mention>...\n"
					+ "Multiple mentions possible. Up to 8 total players").queue();
			}
		}
		
		/**
		 * Quit command
		 * Automatically kicks them out of game.
		 */
		if (game) {
			if (content.startsWith("~~quit")) {
				int p = this.players.indexOf(event.getAuthor());
				stays[p] = true;
				this.sums[p] = -1;
			}
		}
		
		if (game) { // Checks to see if everyone is ready
			for (boolean stay : this.stays) {
				if (!stay) {
					return;
				}
			}
			
			/**
			 * Win message
			 */
			String winner = this.blackjack.winner();
			if (winner.contains("Player")) {
				channel.sendMessage(
					String.format("**%s is the winner.**",
						this.players.get(Character.getNumericValue(winner.charAt(19) - 1))))
					.queue();
			} else {
				channel.sendMessage(winner).queue();
			}
			
			/**
			 * Prints each player's hand
			 */
			for (int p = 0; p < this.players.size(); p++) {
				if (this.sums[p] == -1) {
					this.chnl.sendMessage(String.format("%s's Hand: ", this.players.get(p).getAsMention())).queue();
					String cards = "";
					for (String card : this.blackjack.getHand(p)) {
						cards += card + " ";
					}
					this.chnl.sendMessage(cards).queue();
				}
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
		
		if (game && this.players.contains(event.getUser())) {
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
						 * Cards in hand sum check
						 */
						EmbedBuilder emb = getHand(x);
						
						if (this.sums[x] < 21) { // Still playable
							emb.addField("", "Click :one: to hit. Click :two: to stay.", false);
							this.privatemsg.get(x).editMessage(emb.build()).queue();
							this.privatemsg.get(x).addReaction("U+31U+20e3").queue();
							this.privatemsg.get(x).addReaction("U+32U+20e3").queue();
							return;
						} else if (this.sums[x] > 21) { // Busted, automatic ready up
							emb.addField("", "You busted!", false);
						} else { // Automatic stay for hand = 21
							emb.addField("", "You got 21!", false);
						}
						this.privatemsg.get(x).editMessage(emb.build()).queue();
						this.stays[x] = true;
						this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention()))
							.queue();
						return;
					}
				}
			} else if (choice.getAsCodepoints().equals("U+32U+20e3")) { // Stay
				if (this.players.contains(event.getUser())) {
					this.stays[this.players.indexOf(event.getUser())] = true;
					this.chnl.sendMessage(String.format("%s is ready.", event.getUser().getAsMention())).queue();
				}
			}
		}
	}
	
	/**
	 * 5 minutes per game
	 */
	// new java.util.Timer().schedule(
	// new java.util.TimerTask() {
	//
	// @Override
	// public void run() {
	// // chnl.sendMessage(String.format("Game Over. %s took too long to play.",
	// // player == 0 ? player1.getEffectiveName() : player2.getEffectiveName())).queue();
	// // reset();
	//
	// }
	// },
	// 1000 * 5 * 60);
}
