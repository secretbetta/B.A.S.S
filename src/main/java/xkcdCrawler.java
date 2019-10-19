import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class xkcdCrawler {
	
	private String img;
	private String title;
	private String desc;
	
	public xkcdCrawler() {
		this.img = null;
		this.title = null;
		this.desc = null;
	}
	
	public static List<String> getImgs(URL url) {
		String regex = "(?is)(<img src=\")(.*?)\" title";
		Pattern pattern = Pattern.compile(regex);
		String html = WebCrawler.fetchHTML(url);
		
		ArrayList<String> imgs = new ArrayList<String>();
		int limit = 0;
		if (html != null) {
			Matcher matcher = pattern.matcher(html);
			
			while (matcher.find() && limit != 20) {
				limit++;
				imgs.add(matcher.group());
			}
		}
		return imgs;
	}
	
	public static List<String> cleanImgs(List<String> links) {
		String regex = "(?is)(src=)\"(.*?)\"";
		Pattern pattern = Pattern.compile(regex);
		
		List<String> imgs = new ArrayList<>();
		
		
		Matcher matcher;
		for (String link : links) {
			matcher = pattern.matcher(link);
			while (matcher.find()) {
				imgs.add(matcher.group(2));
			}
		}
		
		return imgs;
	}
	
	public String getXKCDimg() throws MalformedURLException {
		List<String> imgs = cleanImgs(getImgs(new URL("https://c.xkcd.com/random/comic/")));
		this.img = "https:" + imgs.get(1);
		return this.img;
	}
	
	public String getXKCDtitle() {
		
		return this.title;
	}
	
	public String getXKCDdesc() {
		
		return this.desc;
	}
}
