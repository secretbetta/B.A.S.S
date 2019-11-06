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
	
	private boolean game = false; // Game is running or not
	private Blackjack blackjack; // Actual game class
	private List<User> players; // List of players
	private ArrayList<Message> privatemsg; // List of private msgs to edit
	// private int[] rounds;
	private boolean[] stays; // Who chooses to stay
	private int[] sums; // Sum of cards (auto checked)
	private MessageChannel chnl; // Main channel of the game
	
	/**
	 * Creates a new game and initializes everything
	 * 
	 * @param players Number of players
	 * @param users   List of players
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
	
	/**
	 * Gets player's hand in embed format
	 * 
	 * @param player Player to get hand from
	 * @return Embedded message
	 */
	public EmbedBuilder getHand(int player) {
		EmbedBuilder emb = new EmbedBuilder();
		emb.setTitle("BlackJack");
		emb.setDescription("**Cards**: ");
		for (String card : this.blackjack.getHand(player)) {
			emb.appendDescription(card + " ");
		}
		
		return emb;
	}
	
	/**
	 * Timer method
	 * 
	 * @param minutes time before game ends
	 */
	public void timer(int minutes) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					if (BlackjackEvent.this.game) {
						for (int x = 0; x < BlackjackEvent.this.stays.length; x++) {
							BlackjackEvent.this.stays[x] = true;
						}
						BlackjackEvent.this.chnl.sendMessage("Time is up!").queue();
					}
				}
			},
			1000 * minutes * 60);
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
		if (content.startsWith("~~blackjack") && !this.game) {
			// Checks if multiplayer
			if (message.getMentionedMembers().size() > 0 && message.getMentionedMembers().size() <= 8) {
				List<User> players = new ArrayList<>();
				players.add(message.getAuthor());
				players.addAll(message.getMentionedUsers());
				this.newGame(players.size(), players);
				
				channel.sendMessage("Welcome to BlackJack! You have 5 minutes to finish the game.\n"
					+ "The players are:\n")
					.queue();
				int p = 0;
				
				// Sends msg to each player
				for (User player : players) {
					channel.sendMessage(String.format("%s\n", player.getAsMention())).queue();
					this.sums[p] = this.blackjack.addCards(p);
					this.timer(5);
					EmbedBuilder emb = this.getHand(p);
					emb.addField("", "Click :one: to hit. Click :two: to stay.", false);
					player.openPrivateChannel().complete().sendMessage(emb.build()).queue();
					p++;
				}
			} else { // Invalid command usage
				channel.sendMessage("Invalid command. Correct usage:\n"
					+ "~~blackjack <@mention>...\n"
					+ "Multiple mentions possible. Up to 8 total players").queue();
			}
		}
		
		/**
		 * Quit command
		 * Automatically kicks them out of game.
		 */
		if (this.game) {
			if (content.startsWith("~~quit")) {
				int p = this.players.indexOf(event.getAuthor());
				this.stays[p] = true;
				this.sums[p] = -1;
			}
		}
		
		if (this.game) { // Checks to see if everyone is ready
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
				if (this.sums[p] != -1) {
					this.chnl.sendMessage(String.format("%s's Hand: ", this.players.get(p).getAsMention())).queue();
					String cards = "";
					for (String card : this.blackjack.getHand(p)) {
						cards += card + " ";
					}
					this.chnl.sendMessage(cards).queue();
				}
			}
			this.game = false;
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
		
		if (this.game && this.players.contains(event.getUser())) {
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
						EmbedBuilder emb = this.getHand(x);
						
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
}
