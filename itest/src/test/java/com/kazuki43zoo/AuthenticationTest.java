package com.kazuki43zoo;

import com.kazuki43zoo.pages.WelcomePage;
import com.kazuki43zoo.pages.auth.LoginPage;
import com.kazuki43zoo.utils.WebElementOperations;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/seleniumContext.xml"})
public class AuthenticationTest {

	private static WebDriver driver;

	@Value("${selenium.applicationContextUrl}")
	String applicationContextUrl;

	@BeforeClass
	public static void setUp() {
		FirefoxDriverManager.getInstance().setup();
		driver = new FirefoxDriver();
	}

	@Test
	public void loginAndLogoutSuccess() {

		driver.get(applicationContextUrl);

		// login
		WelcomePage welcomePage = new WelcomePage(driver)
				.username("user01").password("password").login();

		// logout
		welcomePage.getUserMenuPullDown().logout();

		// assert
		assertThat(welcomePage.getAlert().getText(), is("Logout was completed."));

	}

	@Test
	public void loginUnknownUser() {

		driver.get(applicationContextUrl);

		// login
		LoginPage loginPage = new WelcomePage(driver)
				.username("unknownUser").password("password").login(LoginPage.class);

		// assert
		assertThat(loginPage.getAlert().getText(), is("Bad credentials"));
		assertThat(WebElementOperations.getValue(loginPage.getUsername()), is("unknownUser"));
		assertThat(WebElementOperations.getValue(loginPage.getPassword()), is(""));

	}


	@Test
	public void loginBadPassword() {

		driver.get(applicationContextUrl);

		// login
		LoginPage loginPage = new WelcomePage(driver)
				.username("user01").password("badPassword").login(LoginPage.class);

		// assert
		assertThat(loginPage.getAlert().getText(), is("Bad credentials"));
		assertThat(WebElementOperations.getValue(loginPage.getUsername()), is("user01"));
		assertThat(WebElementOperations.getValue(loginPage.getPassword()), is(""));

	}

	@Test
	public void loginEmptyString() {

		driver.get(applicationContextUrl);

		// login
		LoginPage loginPage = new WelcomePage(driver)
				.username("user01").password("").login(LoginPage.class);

		// assert
		assertThat(loginPage.getAlert().getText(), is("\"Password\" may not be null."));
		assertThat(WebElementOperations.getValue(loginPage.getUsername()), is("user01"));
		assertThat(WebElementOperations.getValue(loginPage.getPassword()), is(""));

		// login
		loginPage.username("").password("password").login();

		assertThat(loginPage.getAlert().getText(), is("\"Account ID\" may not be null."));
		assertThat(WebElementOperations.getValue(loginPage.getUsername()), is(""));
		assertThat(WebElementOperations.getValue(loginPage.getPassword()), is(""));

	}

	@AfterClass
	public static void tearDown() {
		driver.quit();
	}

}

