import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * MovieQuoter class to gets movie quotes and do fun stuff with the list :3
 * @author Andrew
 *
 */
public class MovieQuoter {
	/** List of quotes */
	private ArrayList<String> quotes;
	
	/**
	 * Initializes {@link #quotes}
	 */
	public MovieQuoter() {
		this.quotes = new ArrayList<String>();
	}
	
	/**
	 * Initializes {@link #quotes} and gets all movie quotes from path
	 * @param path Path to read movie quotes from
	 */
	public MovieQuoter(String path) {
		this.quotes = new ArrayList<String>();
		try {
			this.getQuotes(path);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not acess path");
		}
	}
	
	/**
	 * Adds list of quotes from file to {@link #quotes}
	 * 
	 * Uses ISO-885901 charset because UTF-8 doesn't work for some reason
	 * 
	 * @return List of quotes
	 * @throws IOException
	 */
	public ArrayList<String> getQuotes(String path) throws IOException {
		BufferedReader reader = Files.newBufferedReader(Paths.get(path), Charset.forName("ISO-8859-1"));
		
		String line = "";
		while ((line = reader.readLine()) != null) {
			this.quotes.add(line);
		}
		return this.quotes;
	}
	
	/**
	 * Gets random quote from list
	 * 
	 * @return Movie Quote
	 */
	public String getRandomQuote() {
		return this.quotes.get((int)(Math.random()*this.quotes.size()));
	}
	
	/**
	 * Finds quote(s) from {@link #quotes} relating to input "content".
	 * 
	 * @param content String of words to read in
	 * @return random quote that relates to the content. Returns null if little to no relations (score < 0.5)
	 */
	public String quoteFinder(String content) {
		// TODO gets random quote from list that relates to content with a score of at least 0.5 (50%)
//		double score = 0.0;
		return null;
	}
	
	public static void main(String[] args) {
		// Edit this to test your code if necessary
		String path = "marvelquotes.txt";
		MovieQuoter mquotes = new MovieQuoter();
		try {
			mquotes.getQuotes(path); //This has some exceptions so you need this in the try catch block
			System.out.println(mquotes.getRandomQuote());
		} catch (IOException e) {
			System.err.printf("Cannot find file %s\n", path);
			System.err.println(e);
		}
	}
}
