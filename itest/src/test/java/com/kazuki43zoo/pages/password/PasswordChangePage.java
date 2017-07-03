package com.kazuki43zoo.pages.password;


import com.kazuki43zoo.pages.Page;
import com.kazuki43zoo.utils.WebElementOperations;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class PasswordChangePage implements Page<PasswordChangePage> {

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

	public PasswordChangePage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	public PasswordChangePage reload() {
		PageFactory.initElements(driver, this);
		return this;
	}

	public PasswordChangePage username(String value) {
		return WebElementOperations.input(this, this.username, value);
	}

	public PasswordChangePage password(String value) {
		return WebElementOperations.input(this, this.password, value);
	}

	public void login() {
		this.loginBtn.click();
	}

	public PasswordChangePage failureOnLogin() {
		this.loginBtn.click();
		return reload();
	}

}
