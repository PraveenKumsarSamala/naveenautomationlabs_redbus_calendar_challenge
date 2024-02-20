package codingChallenge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.tools.config.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class PassportSeva {

	static WebDriver driver;

	public static void main(String[] args) {
		try {

			for (String fileName : new File(System.getProperty("user.home") + File.separator + "Desktop\\Passport\\")
					.list()) {
				if (fileName.contains("Passport") | fileName.contains("passport"))
					new File(System.getProperty("user.home") + File.separator + "Desktop\\Passport\\" + fileName)
							.delete();
			}
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + File.separator + "drivers\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			getStatausByPostCall();
			getStatusByUI();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			driver.quit();
		}
	}

	static void getStatausByPostCall() {

		try {
			HashMap<String, String> parameters = new HashMap<>();
			parameters.put("optStatus", "Application_Status");
			parameters.put("fileNo", "HY2076262354924");
			parameters.put("applDob", "01/06/1990");
			HttpRequest httpRequest = HttpRequest.newBuilder().uri(new URI(
					"https://portal1.passportindia.gov.in/AppOnlineProject/statusTracker/trackStatusForFileNoNew"))
					.header("Content-Type", "application/x-www-form-urlencoded")
					.POST(BodyPublishers.ofString(parameters.entrySet().stream()
							.map(entry -> String.join("=",
									URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8),
									URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8)))
							.collect(Collectors.joining("&"))))
					.build();

			HttpClient client = HttpClient.newHttpClient();
			HttpResponse<String> response = null;
			try {
				response = client.send(httpRequest, BodyHandlers.ofString());
				String responseBody = response.body().toString();

				String fileName = System.getProperty("user.home") + File.separator
						+ "Desktop\\Passport\\PassportStatus-"
						+ new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date()).toString() + ".html";

				File f = new File(fileName);
				FileWriter writer = new FileWriter(f);
				writer.write(responseBody);
				writer.close();

				driver.get(fileName);

				System.out
						.println(driver.findElement(By.xpath("(//td[text()='Status ']/parent::tr//td)[2]")).getText());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	static void getStatusByUI() {
		try {
			driver.get("https://portal1.passportindia.gov.in/AppOnlineProject/statusTracker/trackStatusForFileNoNew");

			WebElement filetype = driver.findElement(By.id("optStatus"));
			Select select = new Select(filetype);
			select.selectByValue("Application_Status");
			WebElement fileNo = driver.findElement(By.name("fileNo"));
			fileNo.sendKeys("HY2076262354924");
			WebElement dob = driver.findElement(By.name("applDob"));
			dob.sendKeys("01/06/1990");
			WebElement trackFile = driver.findElement(By.id("trackFile"));
			trackFile.click();
			File passportStatus = new File(
					System.getProperty("user.home") + File.separator + "Desktop\\Passport\\passportStatus.jpeg");

			if (passportStatus.exists())
				passportStatus.delete();
			File passportStatusScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(passportStatusScreenShot, passportStatus);
		} catch (WebDriverException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
