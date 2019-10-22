import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RockPaperScissorsEvent extends ListenerAdapter {
	
	private User player1;
	private User player2;
	
	private int choice1;
	private int choice2;
	
	private boolean game;
	
	/**
	 * Initializes class with newgame default
	 */
	public RockPaperScissorsEvent() {
		this.restart();
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
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
	}
}
