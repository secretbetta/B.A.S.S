import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Discord Bot Loader
 * @author Andrew
 *
 */
public class Main extends ListenerAdapter {
	
	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 0) {
				System.err.println("No token given");
				System.exit(1);
			}
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
		
	}
}
