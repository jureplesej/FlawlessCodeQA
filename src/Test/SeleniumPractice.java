package Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SeleniumPractice {

	public WebDriver driver = null;

	public void Browser() throws IOException {

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				"C:\\Users\\pingv\\eclipse-workspace\\Practice_v1\\src\\Test\\dataDriver.properties");

		prop.load(fis);

		System.setProperty("webdriver.chrome.driver", prop.getProperty("chromePath"));
		System.setProperty("webdriver.gecko.driver", prop.getProperty("firefoxPath"));

		if (prop.getProperty("browser").contains("firefox")) {
			driver = new FirefoxDriver();
		} else if (prop.getProperty("browser").contains("chrome")) {
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		driver.get(prop.getProperty("url"));

	}

	// Select 2nd radio button, 2nd dropdown option and 2nd checkbox
	@Test
	public void TestCase1() throws IOException {
		Browser();
		driver.findElement(By.xpath("//div[@id=\"checkbox-example\"]/fieldset/label[2]/input")).click();

		String text = driver.findElement(By.xpath("//div[@id=\"checkbox-example\"]/fieldset/label[2]")).getText();

		Select s = new Select(driver.findElement(By.id("dropdown-class-example")));
		s.selectByVisibleText(text);

		driver.findElement(By.xpath("//div[@id='radio-btn-example']/fieldset/label[2]/input")).click();

		Assert.assertTrue(driver.findElement(By.cssSelector("[value='radio2']")).isSelected());
		Assert.assertTrue(driver.findElement(By.cssSelector("[value='option2']")).isSelected());
		Assert.assertTrue(driver.findElement(By.id("checkBoxOption2")).isSelected());

	}

	// Write "Slo" in the suggestion box and select 2nd option and check if Slovenia
	// is selected
	@Test
	public void Suggestion() throws IOException {
		Browser();
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		WebElement suggestionBox = driver.findElement(By.id("autocomplete"));

		suggestionBox.sendKeys("Slo");

		WebDriverWait w = new WebDriverWait(driver, 5);
		w.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//*[text()='Slovenia']"))));

		suggestionBox.sendKeys(Keys.ARROW_DOWN);
		suggestionBox.sendKeys(Keys.ARROW_DOWN);
		suggestionBox.sendKeys(Keys.ENTER);

		String text = driver.findElement(By.id("autocomplete")).getAttribute("value");

		if (text.equals("Slovenia")) {
			Assert.assertTrue(true);
		} else {
			Assert.assertTrue(false);
		}

	}

	// Press on Open Window and get the title of the new page, then switch back and
	// get the title of the original page
	@Test
	public void Windows() throws IOException {
		Browser();

		driver.findElement(By.id("openwindow")).click();

		Set<String> windows = driver.getWindowHandles();
		Iterator<String> it = windows.iterator();

		String parentId = it.next();
		String childId = it.next();

		driver.switchTo().window(childId);
		System.out.println(driver.getTitle());
		driver.switchTo().window(parentId);
		System.out.print(driver.getTitle());

	}

}
