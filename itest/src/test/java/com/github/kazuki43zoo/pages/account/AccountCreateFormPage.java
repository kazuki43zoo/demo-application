package com.github.kazuki43zoo.pages.account;


import com.github.kazuki43zoo.pages.Page;
import com.github.kazuki43zoo.parts.LanguagePullDown;
import com.github.kazuki43zoo.parts.LeftMenu;
import com.github.kazuki43zoo.parts.UserMenuPullDown;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static com.github.kazuki43zoo.utils.WebElementOperations.check;
import static com.github.kazuki43zoo.utils.WebElementOperations.inputValue;

@Getter
public class AccountCreateFormPage implements Page<AccountCreateFormPage> {

	private final WebDriver driver;

	@CacheLookup
	@FindBy(id = "accountId")
	private WebElement accountId;

	@CacheLookup
	@FindBy(id = "firstName")
	private WebElement firstName;

	@CacheLookup
	@FindBy(id = "lastName")
	private WebElement lastName;

	@CacheLookup
	@FindBy(name = "enabled")
	private List<WebElement> enabled;

	@CacheLookup
	@FindBy(name = "enabledAutoLogin")
	private List<WebElement> enabledAutoLogin;

	@CacheLookup
	@FindBy(id = "password")
	private WebElement password;

	@CacheLookup
	@FindBy(id = "confirmPassword")
	private WebElement confirmPassword;

	@CacheLookup
	@FindBy(name = "authorities")
	private List<WebElement> authorities;

	@CacheLookup
	@FindBy(id = "saveBtn")
	private WebElement saveBtn;


	private UserMenuPullDown<AccountCreateFormPage> userMenuPullDown;
	private LanguagePullDown<AccountCreateFormPage> languagePullDown;
	private LeftMenu<AccountCreateFormPage> leftMenu;

	public AccountCreateFormPage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	public AccountCreateFormPage reload() {
		PageFactory.initElements(driver, this);
		this.userMenuPullDown = new UserMenuPullDown<>(driver, this);
		this.languagePullDown = new LanguagePullDown<>(driver, this);
		this.leftMenu = new LeftMenu<>(driver, this);
		return this;
	}

	public AccountCreateFormPage accountId(String value) {
		return input(this.accountId, value);
	}

	public AccountCreateFormPage firstName(String value) {
		return input(this.firstName, value);
	}

	public AccountCreateFormPage lastName(String value) {
		return input(this.lastName, value);
	}

	public AccountCreateFormPage password(String value) {
		return input(this.password, value);
	}

	public AccountCreateFormPage confirmPassword(String value) {
		return input(this.confirmPassword, value);
	}

	public AccountCreateFormPage enabled(boolean value) {
		enabled.get(value ? 0 : 1).click();
		return this;
	}

	public AccountCreateFormPage enabledAutoLogin(boolean value) {
		enabledAutoLogin.get(value ? 0 : 1).click();
		return this;
	}

	public AccountCreateFormPage authorities(String... values) {
		check(authorities, values);
		return this;
	}

	public AccountDetailPage save() {
		this.saveBtn.click();
		return new AccountDetailPage(driver);
	}

	private AccountCreateFormPage input(WebElement element, String value) {
		inputValue(element, value);
		return this;
	}

}
