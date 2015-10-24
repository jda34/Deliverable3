package deliverable3;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class LoginTests {
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
     * Given a correct user name and password,
When I login,
Then the login should be successful.

     */
    @Test
    public void testLogin() throws Exception {
	driver.get(baseUrl + "/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
	assertTrue(isElementPresent(By.linkText("logout")));
    }

    /*
     * Given a user name that does exist and an incorrect password,
When I login,
Then the login should be unsuccessful.


     */
    @Test
    public void testIncorrectPassword() throws Exception {
	driver.get(baseUrl + "/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("laboonify");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
	assertTrue(isElementPresent(By
		.xpath("//form[@id='login-form']/div[2]/div/span[2]")));
    }

    /*
     * Given a user name that does not exist and a password,
When I login,
Then the login should be unsuccessful.

     */
    @Test
    public void testIncorrectUsername() throws Exception {
	driver.get(baseUrl + "/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS163212345");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
	assertTrue(isElementPresent(By
		.xpath("//form[@id='login-form']/div[2]/div/span[2]")));
    }

    /*
     * Given I am logged in,
When I click logout,
Then I should be logged out.

     */
    @Test
    public void testLogout() throws Exception{
	driver.get(baseUrl + "/login");
	driver.findElement(By.id("user_login")).clear();
	driver.findElement(By.id("user_login")).sendKeys("CS1632");
	driver.findElement(By.id("passwd_login")).clear();
	driver.findElement(By.id("passwd_login")).sendKeys("cs1632");
	driver.findElement(By.xpath("(//button[@type='submit'])[2]")).click();
	driver.findElement(By.linkText("logout")).click();
	assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*Want to join[\\s\\S] [\\s\\S]*$"));	
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
