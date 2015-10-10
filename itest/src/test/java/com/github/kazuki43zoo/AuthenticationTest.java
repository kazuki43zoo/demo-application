package com.github.kazuki43zoo;

import com.github.kazuki43zoo.pages.WelcomePage;
import com.github.kazuki43zoo.pages.auth.LoginPage;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/seleniumContext.xml"})
public class AuthenticationTest {

	@Inject
	WebDriver driver;

	@Value("${selenium.applicationContextUrl}")
	String applicationContextUrl;

	@Test
	public void loginAndLogoutSuccess() {

		driver.get(applicationContextUrl);

		// login
		WelcomePage welcomePage = new WelcomePage(driver).username("user01").password("password").login();

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
		assertThat(loginPage.getUsername().getAttribute("value"), is("unknownUser"));
		assertThat(loginPage.getPassword().getAttribute("value"), is(""));

	}


	@Test
	public void loginBadPassword() {

		driver.get(applicationContextUrl);

		// login
		LoginPage loginPage = new WelcomePage(driver)
				.username("user01").password("badPassword").login(LoginPage.class);

		// assert
		assertThat(loginPage.getAlert().getText(), is("Bad credentials"));
		assertThat(loginPage.getUsername().getAttribute("value"), is("user01"));
		assertThat(loginPage.getPassword().getAttribute("value"), is(""));

	}

	@Test
	public void loginEmptyString() {

		driver.get(applicationContextUrl);

		// login
		LoginPage loginPage = new WelcomePage(driver)
				.username("user01").password("").login(LoginPage.class);

		// assert
		assertThat(loginPage.getAlert().getText(), is("\"Password\" may not be null."));
		assertThat(loginPage.getUsername().getAttribute("value"), is("user01"));
		assertThat(loginPage.getPassword().getAttribute("value"), is(""));

		// login
		loginPage.username("").password("password").login();

		assertThat(loginPage.getAlert().getText(), is("\"Account ID\" may not be null."));
		assertThat(loginPage.getUsername().getAttribute("value"), is(""));
		assertThat(loginPage.getPassword().getAttribute("value"), is(""));

	}

	@After
	public void tearDown() {
		driver.quit();
	}

}

