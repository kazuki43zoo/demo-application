package com.github.kazuki43zoo;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:META-INF/spring/seleniumContext.xml"})
public class HelloTest {

	@Inject
	WebDriver webDriver;

	@Value("${selenium.applicationContextUrl}")
	String applicationContextUrl;

	@Test
	public void testHelloWorld() throws IOException {

		webDriver.get(applicationContextUrl);

		assertThat(webDriver.getTitle(), is("Demo Application using TERASOLUNA Server Framework for Java (5.x)"));
	}

	@After
	public void tearDown() {
		webDriver.quit();
	}

}

