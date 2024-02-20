package codingChallenge;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TmobileChallenge {
	static ChromeDriver driver = new ChromeDriver();
	static WebElement filterSelection;
	static String FilterCategoty;
	static WebDriverWait wait;

	public static void main(String[] args) {
		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(25));
			System.setProperty("webdriver.chrome.driver",
					"C:\\Users\\sprKumar1\\FreshWorkSpace\\Tmobile\\drivers\\chromedriver.exe");
			driver.manage().window().maximize();
			driver.get("https://www.t-mobile.com/tablets");
//			((JavascriptExecutor) driver).executeScript("document.body.style.zoom = '1'");
//			FilterCategoty = "Operating System";
//			clickOnFilters(FilterCategoty, Arrays.asList("Other"));
//			clickOnFilters(FilterCategoty, Arrays.asList("All"));
//			FilterCategoty = "Deals";
//			clickOnFilters(FilterCategoty, Arrays.asList("New"));
//			clickOnFilters(FilterCategoty, Arrays.asList("All"));
			FilterCategoty = "Brands";
//			clickOnFilters(FilterCategoty, Arrays.asList("Alcatel", "Samsung"));
			clickOnFilters(FilterCategoty, Arrays.asList("All"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				FileUtils.copyFile(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),
						new File("C:\\Users\\sprKumar1\\Desktop\\"
								+ new SimpleDateFormat("mmDDyyyyhhmmss").format(new Date()) + ".jpeg"));

				driver.quit();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private static void clickOnFilters(String filterName, List<String> filterVales) {
		try {
			boolean bool = false;
			while (!bool) {
				((JavascriptExecutor) driver).executeScript("window.scrollBy(0,100)");
				Thread.sleep(1000);
				filterSelection = driver
						.findElement(By.xpath("//legend[text()=' " + FilterCategoty + " ']/parent::mat-panel-title"));
				bool = filterSelection.isDisplayed();
			}

			wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(filterSelection)));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filterSelection);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterSelection);

			if (filterVales.contains("All")) {
				List<WebElement> filterElements = driver.findElements(
						By.xpath("//div[@aria-label='" + filterName + "']//span[@class='filter-display-name']"));
				for (WebElement fileterWebElement : filterElements) {
					wait.until(ExpectedConditions.visibilityOf(fileterWebElement));
					System.out.print(fileterWebElement.getText() + ", ");
					filterSelection = driver.findElement(By.xpath(
							"//div[@aria-label='" + FilterCategoty + "']//span[text()=' " + fileterWebElement.getText()
									+ " ']/ancestor::mat-checkbox//span[@class='mat-checkbox-inner-container']"));
					((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filterSelection);
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", fileterWebElement);
				}
			} else
				for (String filterValue : filterVales) {
					filterSelection = driver.findElement(
							By.xpath("//div[@aria-label='" + FilterCategoty + "']//span[text()=' " + filterValue
									+ " ']/ancestor::mat-checkbox//span[@class='mat-checkbox-inner-container']"));
					wait.until(ExpectedConditions.visibilityOf(filterSelection));
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterSelection);
				}

			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", filterSelection);
			filterSelection = driver
					.findElement(By.xpath("//legend[text()=' " + FilterCategoty + " ']/parent::mat-panel-title"));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", filterSelection);
			((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
