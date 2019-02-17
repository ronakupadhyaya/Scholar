import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("webdriver.chrome.driver", "/Users/digantupadhyaya/Downloads/chromedriver");
		String url = "https://scholar.google.com/citations?user=GjhiSf8AAAAJ";
		run(url);
	}
	
	public static void run(String url) throws InterruptedException, IOException {
//		WebDriver driver = new ChromeDriver();
//		driver.get(url);
//		WebElement button = driver.findElement(By.cssSelector("button[id='gsc_bpf_more']"));
//		while(button.isEnabled()) {
//			button.click();
//			TimeUnit.SECONDS.sleep(2);
//		}
//		
//		Document doc = Jsoup.parse(driver.getPageSource());
		Document doc = Jsoup.connect(url).get();
		Elements elements = doc.select("tr.gsc_a_tr");
		for(int i = 0; i < elements.size(); i++) {
			Element tr = elements.get(i);
			Element title = tr.selectFirst("td.gsc_a_t");
			String dataHref = "https://scholar.google.com" + title.selectFirst("a").attr("data-href");
			
//			Element a = tr.selectFirst("td.gsc_a_c").selectFirst("a");
//			String href = a.attr("href");
//			int count = 0;
//			if(!a.text().equals("")) {
//				count = Integer.parseInt(a.text());
//				ArrayList<String> citers = getCiters(href, count);
//			}
			getCitees(dataHref);
		}
	}
	
	public static ArrayList<String> getCitees(String url) throws IOException {
		HashSet<String> citees = new HashSet<String>();
		String href = scrapeForm(url);
		System.out.println("here1: " + href);
		Document doc = Jsoup.connect(href).get();
		Elements divs = doc.select("div.gs_ri");
		for(int i = 0; i < divs.size(); i++) {
			Element div = divs.get(i);
			Element span = div.selectFirst("span.gs_ctc");
			if(span != null && span.text().contains("PDF")) {
				Element a = div.selectFirst("a");
				String pdfLink = a.attr("href");
				System.out.println(pdfLink);
			}
		}
		return new ArrayList<String>(citees);
	}
	
	public static String scrapeForm(String url) throws IOException {
		Document form = Jsoup.connect(url).get();
		Elements links = form.select("a.gsc_oms_link");
		String href = links.get(links.size() - 1).attr("href");
		return href;
	}
	
	public static ArrayList<String> getCiters(String url, int count) throws IOException {
		if(url == null) {
			return new ArrayList<String>();
		}
		HashSet<String> citers = new HashSet<String>();
		Document doc;
		int pages = count/10;
		for(int i = 0; i < pages; i++) {
			System.out.println(i);
			doc = Jsoup.connect(url + "&start=" + i*10).get();
			Elements divs = doc.select("div.gs_a");
			for(int j = 0; j < divs.size(); j++) {
				Element div = divs.get(j);
				Elements authors = div.select("a");
				for(int k = 0; k < authors.size(); k++) {
					Element author = authors.get(k);
					String href = "https://scholar.google.com" + author.attr("href");
					Document page = Jsoup.connect(href).get();
					String name = page.selectFirst("div#gsc_prf_in").text();
					citers.add(name);
				}
			}
		}
		return new ArrayList<String>(citers);
	}
}
