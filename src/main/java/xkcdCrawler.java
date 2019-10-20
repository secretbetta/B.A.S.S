import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XKCD comic crawler
 * @author Andrew
 *
 */
public class xkcdCrawler {
	
	private String img;
	private String title;
	private String desc;
	
	private String html;
	
	private List<String> links;
	
	/**
	 * Gets random XKCD post and crawls it
	 * @throws MalformedURLException
	 */
	public xkcdCrawler() throws MalformedURLException {
		this.img = null;
		this.title = null;
		this.desc = null;
		this.html = WebCrawler.fetchHTML(new URL("https://c.xkcd.com/random/comic/"));
		
		links = new ArrayList<String>();
		
		this.getPost();
	}
	
	/**
	 * Crawls XKCD post
	 * @param url URL of post
	 */
	public xkcdCrawler(URL url) {
		this.img = null;
		this.title = null;
		this.desc = null;
		this.html = WebCrawler.fetchHTML(url);
		
		links = new ArrayList<String>();
	}
	
	/**
	 * Compiles post
	 * @return List of urls
	 */
	public List<String> getPost() {
		String regex = "(?is)(<img src=\")(.*?)\" title=\"(.*?)\" alt=\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		
		if (this.html != null) {
			Matcher matcher = pattern.matcher(this.html);
			while (matcher.find()) {
				this.links.add(matcher.group());
			}
		}
		return this.links;
	}
	
	/**
	 * Cleans image links
	 * @return List of image urls
	 */
	public List<String> cleanImgs() {
		String regex = "(?is)(src=)\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		
		List<String> imgs = new ArrayList<>();
		
		
		Matcher matcher;
		for (String link : this.links) {
			matcher = pattern.matcher(link);
			while (matcher.find()) {
				imgs.add(matcher.group(2));
			}
		}
		
		return imgs;
	}
	
	/**
	 * Gets XKCD img url
	 * @return image URL in string
	 * @throws MalformedURLException
	 */
	public String getXKCDimg() throws MalformedURLException {
		List<String> imgs = cleanImgs();
		this.img = "https:" + imgs.get(2);
		return this.img;
	}
	
	/**
	 * Gets XKCD post tite
	 * @return post title
	 */
	public String getXKCDtitle() {
		String regex = "(?is)(alt=)\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		
		List<String> imgs = new ArrayList<>();
		
		
		Matcher matcher;
		for (String link : this.links) {
			matcher = pattern.matcher(link);
			while (matcher.find()) {
				imgs.add(matcher.group(2));
			}
		}
		
		this.desc = imgs.get(1);
		return this.desc;
	}
	
	/**
	 * Gets XKCD post description
	 * @return post description
	 */
	public String getXKCDdesc() {
		String regex = "(?is)(title=)\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		
		List<String> imgs = new ArrayList<>();
		
		
		Matcher matcher;
		for (String link : links) {
			matcher = pattern.matcher(link);
			while (matcher.find()) {
				imgs.add(matcher.group(2));
			}
		}
		
		this.title = imgs.get(0).replace("&#39;", "'");
		return this.title;
	}
}
