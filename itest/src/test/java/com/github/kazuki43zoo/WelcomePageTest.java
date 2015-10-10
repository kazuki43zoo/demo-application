package com.github.kazuki43zoo;

import com.github.kazuki43zoo.pages.WelcomePage;
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
public class WelcomePageTest {

	@Inject
	WebDriver driver;

	@Value("${selenium.applicationContextUrl}")
	String applicationContextUrl;

	@Test
	public void viewWelcomePage() {

		driver.get(applicationContextUrl);

		assertThat(driver.getTitle(),
				is("Demo Application using TERASOLUNA Server Framework for Java (5.x)"));
	}

	@Test
	public void switchLanguage() {

		driver.get(applicationContextUrl);

		WelcomePage welcomePage = new WelcomePage(driver);

		// Switch to Japanese
		{
			welcomePage.getLanguagePullDown().japanese();

			assertThat(welcomePage.getWelcomeMessage().getText(),
					is("ようこそ 「TERASOLUNA Server Framework for Java (5.x)を使用したデモアプリケーション」へ ！！"));
		}
		// Switch to English
		{
			welcomePage.getLanguagePullDown().english();

			assertThat(welcomePage.getWelcomeMessage().getText(),
					is("Welcome Demo Application using TERASOLUNA Server Framework for Java (5.x) !!"));
		}
	}

	@After
	public void tearDown() {
		driver.quit();
	}

}

