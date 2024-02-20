package codingChallenge;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class worldometers {

	static WebDriver driver = new ChromeDriver();

	public static void main(String[] args) {
		try {
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\sprKumar1\\FreshWorkSpace\\Tmobile\\drivers\\chromedriver.exe");
			driver.get("https://www.worldometers.info/");
			driver.manage().window().maximize();
			long i = 0, j = 1;
			while (j < 8 && i < 20) {				
				Thread.sleep(500);
				System.out.println(driver.findElement(By.xpath("(//span[@class=\"counter-item\"])[" + j + "]"))
						.getText() + ": "
						+ driver.findElement(By.xpath("(//span[@class=\"counter-number\"])[" + j + "]")).getText());
				if (j == 7)
					j = 1;
				j++;
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
