import java.util.List;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Discord Bot Loader
 * @author Andrew
 *
 */
public class Main extends ListenerAdapter {
	public TicTacToe tttGame;
	public int player;
	public boolean gameStart;
	
	public Main() {
//		this.tttGame = new TicTacToe();
		this.player = 0;
		this.gameStart = true;
	}
	
	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 0) {
				System.err.println("No token given");
				System.exit(1);
			}
			
			System.out.println("Initiated Tic Tac Toe");
			
			System.out.println("Running B.A.S.S Bot");
			JDA api = new JDABuilder(AccountType.BOT)
					.setToken(args[0])
					.setActivity(Activity.playing("try ~~help"))
					.build();
			api.getPresence().setStatus(OnlineStatus.ONLINE);
//			api.getPresence().setGame(Game);
			api.addEventListener(new MyEventListener());
			api.addEventListener(new Main());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Event Handler
	 */
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		Member objMember = event.getMember();
		String content = message.getContentRaw().toLowerCase();
		MessageChannel channel = event.getChannel();
		
		if (content.startsWith("~~ttt")) {
			List<Member> players = message.getMentionedMembers();
			if (players.size() > 1) {
				channel.sendMessage("Too many players, only mention 1 player").queue();
				return;
			} else if (players.size() == 0) {
				channel.sendMessage("type ~~ttt <@mention> play against them").queue();
			} else if (players.get(0).getUser().isBot()) {
				channel.sendMessage("Cannot play against a bot").queue();
				return;
			} else if (players.get(0).getId().equals(objMember.getId())) {
				channel.sendMessage("I know you're lonely and all... but you can't play against yourself...").queue();
				return;
			}
			channel.sendMessage(String.format("Player 1: %s\nPlayer 2: %s", event.getAuthor().getAsMention(), players.get(0))).queue();
			channel.sendMessage(tttGame.toString()).queue();
		}
	}
}
