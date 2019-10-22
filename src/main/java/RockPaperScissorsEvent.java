import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RockPaperScissorsEvent extends ListenerAdapter {
	
	private User player1 = null;
	private User player2 = null;
	
	/**
	 * 0 = scissors
	 * 1 = paper
	 * 2 = rock
	 */
	private int choice1 = -1;
	private int choice2 = -1;
	
	private MessageChannel channelID = null;
	
	private boolean game = false;
	
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
	 * Returns winner
	 * @param p1 player 1 choice
	 * @param p2 player 2 choice
	 * @return 0 = draw, 1 = c1, -1 = c2
	 */
	public int winner(int p1, int p2) {
		if ((p1+1)%3 == p2) {
			return 1;
		} else if (((p1-1)%3 < 0 ? ((p1-1)%3)+3 : (p1-1)%3) == p2) {
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

		if  (!author.isBot()) {
			if (this.game && (content.equals("rock") || content.equals("paper") || content.equals("scissors"))) {
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
					
					if (this.game && this.choice1 != -1 && this.choice2 != -1) {
						switch(this.winner(this.choice1, this.choice2)) {
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
				}
			} else if (this.game && !(content.equals("rock") || content.equals("paper") || content.equals("scissors")) && !event.isFromGuild()) {
				channel.sendMessage("Invalid input. Please put \"rock\", \"paper\", or \"scissors\"").queue();
			}
			
			if (content.startsWith("~~rps")) {
				if (!this.game) {
					List<Member> players;
					if ((players = message.getMentionedMembers()).size() == 1) {
						this.player1 = objMember.getUser();
						this.player2 = players.get(0).getUser();
						System.err.println(this.player1.getId() + " " + this.player2.getId());
						this.channelID = channel;
						this.player1
							.openPrivateChannel()
							.complete()
							.sendMessage(String.format("Hello %s, "
								+ "type \"rock\", \"paper\", or \"scissors\" to make a move.",
								this.player1.getName()))
							.queue();
						this.player2
							.openPrivateChannel()
							.complete()
							.sendMessage(String.format("Hello %s, "
								+ "type \"rock\", \"paper\", or \"scissors\" to make a move.",
								this.player2.getName()))
							.queue();
						this.game = true;
					} else {
						if (players.size() == 0) {
							channel.sendMessage("Use ~~rps <@mention> to play").queue();
						} else {
							channel.sendMessage("Too many mentions, cannot start game.\n"
								+ "Use ~~rps <@mention> to play").queue();
						}
					}
				} else {
					channel.sendMessage("Game is already playing").queue();
				}
			}
		}
	}
}
