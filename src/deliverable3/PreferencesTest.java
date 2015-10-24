package deliverable3;



import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class PreferencesTest {
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
	driver = new FirefoxDriver();
	baseUrl = "https://www.reddit.com/";
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /*
     * Given that I am on reddit.com/prefs/friends/ and logged in,
When I type “cs1632friend” and click add,
Then “cs1632friend” should be listed as a friend.
     */
    @Test
    public void testFriend() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.linkText("Log in or sign up")).click();
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("cs1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[4]")).click();
	driver.findElement(By.linkText("preferences")).click();
	driver.findElement(By.xpath("(//a[contains(text(),'friends')])[2]")).click();
	driver.findElement(By.id("name")).click();
	driver.findElement(By.id("name")).clear();
	driver.findElement(By.id("name")).sendKeys("cs1632friend");
	driver.findElement(By.cssSelector("button.btn")).click();
	// Warning: assertTextPresent may require manual changes
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("cs1632friend"));
    }

    /*
     * Given that I am logged in and on reddit.com/prefs/update/ and the upper current password field is filled in,
When I add an email address and click save,
Then my email address should be updated.

     */
    @Test
    public void testChangeEmail() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.name("user")).click();
	driver.findElement(By.name("user")).clear();
	driver.findElement(By.name("user")).sendKeys("cs1632");
	driver.findElement(By.name("passwd")).clear();
	driver.findElement(By.name("passwd")).sendKeys("cs1632");
	driver.findElement(By.cssSelector("button.btn")).click();
	driver.findElement(By.linkText("preferences")).click();
	driver.findElement(By.linkText("password/email")).click();
	driver.findElement(By.name("curpass")).clear();
	driver.findElement(By.name("curpass")).sendKeys("cs1632");
	driver.findElement(By.name("email")).clear();
	driver.findElement(By.name("email")).sendKeys("jda34@pitt.edu");
	driver.findElement(By.cssSelector("button.btn")).click();
	Thread.sleep(1000);
	// Warning: assertTextPresent may require manual changes
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("your email has been updated"));
    }

    /*
     * Given that I am logged in and on reddit.com/prefs/update/ and the lower current password field is filled in,
When I add a new password and verify the password and click save,
Then my password should be updated.

     */
    @Test
    public void testChangePassword() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.name("user")).click();
	driver.findElement(By.name("user")).clear();
	driver.findElement(By.name("user")).sendKeys("cs1632");
	driver.findElement(By.name("passwd")).clear();
	driver.findElement(By.name("passwd")).sendKeys("cs1632");
	driver.findElement(By.cssSelector("button.btn")).click();
	driver.findElement(By.linkText("preferences")).click();
	driver.findElement(By.linkText("password/email")).click();
	driver.findElement(By.xpath("(//input[@name='curpass'])[2]")).clear();
	driver.findElement(By.xpath("(//input[@name='curpass'])[2]")).sendKeys("cs1632");
	driver.findElement(By.name("newpass")).clear();
	driver.findElement(By.name("newpass")).sendKeys("cs1632");
	driver.findElement(By.name("verpass")).clear();
	driver.findElement(By.name("verpass")).sendKeys("cs1632");
	driver.findElement(By.cssSelector("#pref-update-password > button.btn")).click();
	// Warning: assertTextPresent may require manual changes
	Thread.sleep(1000);
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("your password has been updated"));
    }

    /*
     * Given that I am logged in and on reddit.com/prefs,
When I change the language to English and click “save options”,
Then my options should be saved and the page in English.

     */
    @Test
    public void testChangeLanguage() throws Exception {
	driver.get(baseUrl + "/");
	driver.findElement(By.name("user")).click();
	driver.findElement(By.name("user")).clear();
	driver.findElement(By.name("user")).sendKeys("cs1632");
	driver.findElement(By.name("passwd")).clear();
	driver.findElement(By.name("passwd")).sendKeys("cs1632");
	driver.findElement(By.cssSelector("button.btn")).click();
	driver.findElement(By.linkText("preferences")).click();
	new Select(driver.findElement(By.id("lang"))).selectByVisibleText("English [en]");
	driver.findElement(By.cssSelector("input.btn")).click();
	// Warning: assertTextPresent may require manual changes
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().contains("your preferences have been updated"));
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

    private boolean isAlertPresent() {
	try {
	    driver.switchTo().alert();
	    return true;
	} catch (NoAlertPresentException e) {
	    return false;
	}
    }

    private String closeAlertAndGetItsText() {
	try {
	    Alert alert = driver.switchTo().alert();
	    String alertText = alert.getText();
	    if (acceptNextAlert) {
		alert.accept();
	    } else {
		alert.dismiss();
	    }
	    return alertText;
	} finally {
	    acceptNextAlert = true;
	}
    }
}
