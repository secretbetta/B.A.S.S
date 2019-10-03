import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Jokes class that reads jokes from a file and puts it into an ArrayList
 * @author Andrew
 *
 */
public class Jokes {
	private ArrayList<String> jokes;
	
	/**
	 * Initializes {@link #jokes}
	 */
	public Jokes() {
		jokes = new ArrayList<>();
	}
	
	/**
	 * Reads a list of jokes from a file
	 * @param path Path to read from
	 */
	public void readJokes(Path path) {
		try {
			BufferedReader myReader = Files.newBufferedReader(path);
			String line = "";
			while ((line = myReader.readLine()) != null) {
				jokes.add(line);
			}
		} catch (IOException e) {
			System.err.println("Cannot read file");
		}
	}
	
	/**
	 * Gets a random joke
	 * @return a joke
	 */
	public String randomJoke() {
		return jokes.get((int)Math.random()*jokes.size());
	}

	/**
	 * @return ArrayList of the jokes
	 */
	public ArrayList<String> getJokes() {
		return jokes;
	}
}
