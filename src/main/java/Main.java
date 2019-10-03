import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * Discord Bot Loader
 * @author Andrew
 *
 */
public class Main {
	
	public static void main(String[] args) throws Exception {
		try {
			System.out.println(args[0]);
			System.out.println("Running B.A.S.S Bot");
			JDA api = new JDABuilder(AccountType.BOT).setToken(args[0]).build();
			api.addEventListener(new MyEventListener());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
