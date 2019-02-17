import java.io.IOException;
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
	public static void main(String[] args) throws IOException, InterruptedException {
		System.setProperty("webdriver.chrome.driver", "/Users/digantupadhyaya/Downloads/chromedriver");
		String url = "https://scholar.google.co.uk/citations?user=VWCHlwkAAAAJ";
		
		WebDriver driver = new ChromeDriver();
		driver.get(url);
		WebElement button = driver.findElement(By.cssSelector("button[id='gsc_bpf_more']"));
		while(button.isEnabled()) {
			button.click();
			TimeUnit.SECONDS.sleep(2);
		}
		
		Document doc = Jsoup.parse(driver.getPageSource());
		Elements elements = doc.select("td.gsc_a_t");
		for(int i = 0; i < elements.size(); i++) {
			Element element = elements.get(i);
			System.out.println(i + ": " + element.text());
			
		}
	}
}
