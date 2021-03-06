package com.secretbetta.BASS.Driver;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Rock Paper Scissors game for discord bot
 * 
 * @author Andrew
 */
public class RockPaperScissorsEvent extends ListenerAdapter {
	
	private User player1;
	private User player2;
	
	/**
	 * -1 = default
	 * 0 = scissors
	 * 1 = paper
	 * 2 = rock
	 */
	private int choice1;
	private int choice2;
	
	private MessageChannel channelID;
	
	private boolean game;
	
	/**
	 * Ends game and gets default start
	 */
	public RockPaperScissorsEvent() {
		restart();
	}
	
	/**
	 * Makes choice for user(s)
	 * 
	 * @param author  User that made a choice
	 * @param content The choice
	 */
	public void choiceMaker(User author, String content) {
		if (this.choice1 == -1 || this.choice2 == -1) {
			if (this.player1.getId().equals(author.getId())) {
				switch (content) {
					case "rock":
						this.choice1 = 2;
						break;
					case "paper":
						this.choice1 = 1;
						break;
					case "scissors":
						this.choice1 = 0;
						break;
				}
			} else if (this.player2.getId().equals(author.getId())) {
				switch (content) {
					case "rock":
						this.choice2 = 2;
						break;
					case "paper":
						this.choice2 = 1;
						break;
					case "scissors":
						this.choice2 = 0;
						break;
				}
			}
		}
	}
	
	/**
	 * Sets everything to default
	 */
	public void restart() {
		this.game = false;
		this.choice1 = -1;
		this.choice2 = -1;
		this.player1 = null;
		this.player2 = null;
		this.channelID = null;
	}
	
	/**
	 * Initializes Rock Paper Scissors game. Returns input validation messages
	 * 
	 * @param message   Message
	 * @param objMember Member of message
	 * @param channel   Channel that the game was initialized
	 * @param content   Raw text of user input
	 * @return Input validation
	 */
	public String rpsInit(Message message, Member objMember, MessageChannel channel, String content) {
		if (!this.game) {
			List<Member> players;
			if ((players = message.getMentionedMembers()).size() == 1) {
				if (!players.get(0).getId().equals(objMember.getId())) {
					this.startGame(objMember, players, channel);
					this.timer(5);
				} else {
					return "You can't play with yourself... Well you can, but you shouldn't...";
				}
			} else {
				if (players.size() == 0) {
					return "Use ~~rps <@user> to play";
				} else {
					return "Too many mentions, cannot start game.\n"
						+ "Use ~~rps <@user> to play";
				}
			}
		} else if (content.split(" ")[0].equals("~~rps")) {
			return "Game is already playing";
		}
		return "Unknown";
	}
	
	/**
	 * Initializes game
	 * 
	 * @param objMember Player 1
	 * @param players   Player 2
	 * @param channel   Main channel
	 */
	public void startGame(Member objMember, List<Member> players, MessageChannel channel) {
		this.player1 = objMember.getUser();
		this.player2 = players.get(0).getUser();
		this.channelID = channel;
		this.player1
			.openPrivateChannel()
			.complete()
			.sendMessage(String.format("Hello %s, "
				+ "type \"rock\", \"paper\", or \"scissors\" to make a move.",
				this.player1.getName()))
			.queue();
		if (!this.player2.isBot()) {
			this.player2
				.openPrivateChannel()
				.complete()
				.sendMessage(String.format("Hello %s, "
					+ "type \"rock\", \"paper\", or \"scissors\" to make a move.",
					this.player2.getName()))
				.queue();
		} else {
			this.choice2 = (int) (Math.random() * 3);
		}
		this.game = true;
	}
	
	/**
	 * Timer for game auto end
	 * 
	 * @param minutes Int minutes until game ends
	 */
	public void timer(int minutes) {
		new java.util.Timer().schedule(
			new java.util.TimerTask() {
				
				@Override
				public void run() {
					if (game) {
						channelID.sendMessage(String.format("Game Over. %s took too long to play.",
							choice1 == -1 ? player1.getAsMention() : player2.getAsMention())).queue();
						restart();
					}
				}
			},
			1000 * minutes * 60);
	}
	
	/**
	 * The end game message
	 */
	public void winMessage() {
		this.channelID.sendMessage(String.format("%s's choice: %s\n"
			+ "%s's choice: %s",
			this.player1.getName(),
			this.choice1 == 0 ? "scissors" : this.choice1 == 1 ? "paper" : "rock",
			this.player2.getName(),
			this.choice2 == 0 ? "scissors" : this.choice2 == 1 ? "paper" : "rock")).queue();
		switch (this.winner(this.choice1, this.choice2)) {
			case 1:
				this.channelID.sendMessage(String.format("%s is the winner!",
					this.player1.getAsMention())).queue();
				this.restart();
				break;
			case 0:
				this.channelID.sendMessage("It's a draw!").queue();
				this.restart();
				break;
			case -1:
				this.channelID.sendMessage(String.format("%s is the winner!",
					this.player2.getAsMention())).queue();
				this.restart();
				break;
		}
	}
	
	/**
	 * Returns winner
	 * 
	 * @param p1 player 1 choice
	 * @param p2 player 2 choice
	 * @return 0 = draw, 1 = c1, -1 = c2
	 */
	public int winner(int p1, int p2) {
		// if (p1 == p2)
		// return 0;
		
		if ((p1 + 1) % 3 == p2) {
			return 1;
		} else if (((p1 - 1) % 3 < 0 ? ((p1 - 1) % 3) + 3 : (p1 - 1) % 3) == p2) {
			// Technically this isn't needed, could be replaced by testing if p1==p2 at the beginning
			return -1;
		}
		return 0;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		User author = event.getAuthor();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		
		if (author.isBot()) {
			return;
		}
		
		/**
		 * Gets user(s) choice for game
		 */
		if (this.game && (content.equals("rock") || content.equals("paper") || content.equals("scissors"))) {
			this.choiceMaker(author, content);
			
			if (this.choice1 != -1 && this.choice2 != -1) {
				this.winMessage();
			}
		} else if (this.game && !(content.equals("rock") || content.equals("paper") || content.equals("scissors"))
			&& !event.isFromGuild()) {
			channel.sendMessage("Invalid input. Please put \"rock\", \"paper\", or \"scissors\"").queue();
		}
		
		/**
		 * ~~rps command
		 */
		if (content.startsWith("~~rps")) {
			channel.sendMessage(this.rpsInit(message, objMember, channel, content)).queue();
		}
		
		/**
		 * Quit command
		 */
		if (content.equals("~~rpsquit")
			&& (objMember.getId().equals(this.player1.getId())
				|| objMember.getId().equals(this.player2.getId()))) {
			
			channel.sendMessage(String.format("%s forfeited. %s won!",
				objMember.getNickname(),
				objMember.getId().equals(this.player1.getId()) ? this.player2.getAsMention()
					: this.player1.getAsMention()))
				.queue();
			this.restart();
		}
	}
}