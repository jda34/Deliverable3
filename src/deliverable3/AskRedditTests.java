package deliverable3;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AskRedditTests {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
	driver = new FirefoxDriver();
	baseUrl = "https://www.reddit.com";
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testBrowse() throws Exception {
	driver.get(baseUrl + "/r/AskReddit");
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
		.matches("^[\\s\\S]*self\\.AskReddit[\\s\\S]*$"));
    }

    @Test
    public void testClickAskReddit() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.cssSelector("span.selected.title")).click();
	driver.findElement(By.linkText("AskReddit")).click();
	assertEquals("https://www.reddit.com/r/AskReddit/",
		driver.getCurrentUrl());
    }

    @After
    public void tearDown() throws Exception {
	driver.quit();
	String verificationErrorString = verificationErrors.toString();
	if (!"".equals(verificationErrorString)) {
	    fail(verificationErrorString);
	}
    }

    private boolean isElementPresent(By by) {
	try {
	    driver.findElement(by);
	    return true;
	} catch (NoSuchElementException e) {
	    return false;
	}
    }
}
