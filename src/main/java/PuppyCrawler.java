import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.secretbetta.BASS.utlities.WebCrawler;

/**
 * Crawls for puppies
 * 
 * @author Andrew
 */
public class PuppyCrawler {
	
	private final URL url;
	
	private String html;
	
	/**
	 * Finalizes URL to get puppies from
	 * 
	 * @throws MalformedURLException
	 */
	public PuppyCrawler() throws MalformedURLException {
		// this.url = new URL("https://www.randomdoggiegenerator.com/");
		this.url = new URL("https://www.randomdoggiegenerator.com/randomdoggie.php");
	}
	
	public void getImage() {
		this.html = WebCrawler.fetchHTML(this.url);
		System.out.println(html);
		String[] vals = this.html.split(":");
		List<String> content = new ArrayList<>();
		for (String keys : vals) {
			content.add(keys.replaceAll("(?is)[{}\"]+?", ""));
			
			System.out.println(keys);
		}
	}
	
	public static void main(String[] args) throws MalformedURLException {
		PuppyCrawler puppies = new PuppyCrawler();
		puppies.getImage();
	}
}
