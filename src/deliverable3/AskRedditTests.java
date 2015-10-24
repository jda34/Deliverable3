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

    /*
     * Given I am on /r/AskReddit,
When I click “new”,
Then the individual threads should be in chronological order.

     */
    @Test
    public void testNew() throws Exception {
	driver.get(baseUrl + "/r/AskReddit");



    }

    /*
     * Given I am on /r/AskReddit,
When I click “top”,
Then the threads with the most upvotes should be listed first.

     */
    @Test
    public void testTop() throws Exception {

	//div[contains(@class, 'unvoted')]/text()
	driver.get(baseUrl + "/r/askreddit/top");
	String s1 = driver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div[1]/div[1]/div[3]")).getText();
	String s2 = driver.findElement(By.xpath("/html/body/div[3]/div[3]/div/div[3]/div[1]/div[3]")).getText();
	int n1 = Integer.parseInt(s1);
	int n2 = Integer.parseInt(s2);
	assertTrue(n1>=n2);
    }

    /*
     * Given I am on the front page,
When I click “AskReddit”,
Then I am taken to /r/AskReddit.

     */
    @Test
    public void testClickAskReddit() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.xpath("(//a[contains(text(),'AskReddit')])[2]")).click();
	assertEquals("https://www.reddit.com/r/AskReddit/",
		driver.getCurrentUrl());
    }

    /*
     * Given I am on /r/AskReddit and logged in,
When I click “Ask a New Question”,
Then I should be taken to a page to create a new thread.

     */
    @Test
    public void testAskQuestion() throws Exception {
	driver.get(baseUrl + "/r/AskReddit/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
	driver.findElement(By.linkText("Ask A New Question")).click();
	assertTrue(isElementPresent(By.name("submit")));
    }

    /*
     * Given I am on /r/AskReddit,
When I click “Filter posts by ‘Serious Posts’”,
Then only posts tagged serious will be there.

     */
    @Test
    public void testFilterBySerious() throws Exception {
	driver.get(baseUrl + "/r/AskReddit/");
	driver.findElement(By.linkText("Serious posts")).click();
	assertEquals("https://dg.reddit.com/r/AskReddit/#dg",
		driver.getCurrentUrl());
    }

    /*
     * Given I am on /r/AskReddit,
When I click the first thread,
Then it should take me to the thread to let me comment.

     */
    @Test
    public void testCommentOnPost() throws Exception {

	// Login
	driver.get(baseUrl + "/r/AskReddit/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();

	// Click first post
	driver.findElement(By.xpath("(//a[contains(@href,'comments')])[1]"))
		.click();

	assertTrue(isElementPresent(By.cssSelector("button.save")));
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
