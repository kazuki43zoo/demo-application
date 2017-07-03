package com.kazuki43zoo.pages.auth;


import com.kazuki43zoo.pages.Page;
import com.kazuki43zoo.utils.PageOperations;
import com.kazuki43zoo.utils.WebElementOperations;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LoginPage implements Page<LoginPage> {

	private final WebDriver driver;

	@CacheLookup
	@FindBy(id = "username")
	private WebElement username;

	@CacheLookup
	@FindBy(id = "password")
	private WebElement password;

	@CacheLookup
	@FindBy(id = "loginBtn")
	private WebElement loginBtn;

	@CacheLookup
	@FindBy(className = "alert")
	private WebElement alert;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	public LoginPage reload() {
		PageFactory.initElements(driver, this);
		return this;
	}

	public LoginPage username(String value) {
		return WebElementOperations.input(this, this.username, value);
	}

	public LoginPage password(String value) {
		return WebElementOperations.input(this, this.password, value);
	}

	public LoginPage login() {
		return login(LoginPage.class);
	}

	public <T extends Page<T>> T login(Class<T> nextPage) {
		this.loginBtn.click();
		return PageOperations.loadNextPage(this, nextPage, driver);
	}

}
