package deliverable3;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DiscoverSubredditsTests {
    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
	driver = new FirefoxDriver();
	baseUrl = "https://www.reddit.com";
	driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }


    /*
     * Given I am on /r/pitt and I am not subscribed and I am not logged in,
     * When I click subscribe,
     * It should tell me to login.
     */
    @Test
    public void testSubscribeNotLoggedIn() throws Exception {
	driver.get(baseUrl + "/r/pitt");
	driver.findElement(By.linkText("subscribe")).click();
	for (int second = 0;; second++) {
	    if (second >= 60)
		fail("timeout");
	    try {
		if (isElementPresent(By.id("cover-msg")))
		    break;
	    } catch (Exception e) {
	    }
	    Thread.sleep(1000);
	}

	assertTrue(driver
		.findElement(By.cssSelector("BODY"))
		.getText()
		.contains(
			"You need to be logged in to subscribe to subreddits."));
    }

    /*
     * Given I am on /r/pitt and I am not subscribed and I am logged in,
     * When I click subscribe,
     * I should now be subscribed.
     */
    @Test
    public void testSubscribe() throws Exception {
	driver.get(baseUrl + "/r/pitt/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

	// Unsubscribe if already subscribed
	if (isElementPresent(By.linkText("unsubscribe"))) {
	    driver.findElement(By.linkText("unsubscribe")).click();
	}

	driver.findElement(By.linkText("subscribe")).click();
	assertFalse(isElementPresent(By.linkText("subscribe")));
	assertTrue(isElementPresent(By.linkText("unsubscribe")));
    }

    /*
     * Given I am on /r/pitt and I am subscribed and I am logged in,
     * When I click Unsubscribe,
     * I should now not be subscribed.
    */
    @Test
    public void testUnsubscribe() throws Exception {
	driver.get(baseUrl + "/r/pitt/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

	// Subscribe if not already subscribed
	if (isElementPresent(By.linkText("subscribe"))) {
	    driver.findElement(By.linkText("subscribe")).click();
	}

	driver.findElement(By.linkText("unsubscribe")).click();
	assertFalse(isElementPresent(By.linkText("unsubscribe")));
	assertTrue(isElementPresent(By.linkText("subscribe")));
    }

    /*
     * Given I am on the front page and logged on,
     * When I click “create” under multireddits and type “Pitt” and “Pittsburgh” under “add subreddit”,
     * The page should now contain post from /r/Pitt and /r/Pittsburgh.
     */
    @Test
    public void testCreateMultiReddit() throws Exception {
	// Login
	driver.get(baseUrl + "/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

	// Create multi-reddit
	driver.findElement(By.xpath("//button[contains(text(), 'create')]"))
		.click();
	driver.findElement(By.cssSelector("input.multi-name")).clear();
	driver.findElement(By.cssSelector("input.multi-name")).sendKeys(
		"pittsburghstuff");
	driver.findElement(By.xpath("//button[contains(text(), 'create')]"))
		.click();

	// Add subreddits
	driver.findElement(By.id("sr-autocomplete")).clear();
	driver.findElement(By.id("sr-autocomplete")).sendKeys("pitt");
	driver.findElement(By.cssSelector("button.add")).click();
	driver.findElement(By.id("sr-autocomplete")).clear();
	driver.findElement(By.id("sr-autocomplete")).sendKeys("pittsburgh");
	driver.findElement(By.cssSelector("button.add")).click();

	// Refresh page, ensure that multireddit was created
	driver.get("https://www.reddit.com/me/m/pittsburghstuff");
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText()
		.matches("^[\\s\\S]*pittsburghstuff subreddits[\\s\\S]*$"));
	driver.findElement(

	// Delete multireddit
		By.cssSelector("span.delete.confirm-button > button")).click();
	driver.findElement(By.cssSelector("button.yes")).click();
    }

    /*
     * Given I am on the front page
     * When I click on “ALL”,
     * I should go to a page with content from all of reddit, not just my subscriptions.
     */
    @Test
    public void testViewAll() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.xpath("//a[contains(text(), 'all')]")).click();
	assertEquals("https://www.reddit.com/r/all", driver.getCurrentUrl());
    }

    /*
     * Given I am on reddit.com/subreddits and logged on,
     * When I click on “What are you interested in?” and type in “retail” and click “/r/TalesFromRetail”,
     * A new page should take me to /r/TalesFromRetail.
     */
    @Test
    public void testFindNewSubreddit() throws Exception {
	driver.get(baseUrl + "/subreddits");
	driver.findElement(By.cssSelector("input.query")).clear();
	driver.findElement(By.cssSelector("input.query")).sendKeys("retail");
	assertTrue(isElementPresent(By.xpath("//a[@href='/r/TalesFromRetail']")));
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
