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

import static com.github.kazuki43zoo.utils.PageOperations.loadNextPage;
import static com.github.kazuki43zoo.utils.WebElementOperations.*;

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

	@Override
	public AccountCreateFormPage reload() {
		PageFactory.initElements(driver, this);
		this.userMenuPullDown = new UserMenuPullDown<>(driver, this);
		this.languagePullDown = new LanguagePullDown<>(driver, this);
		this.leftMenu = new LeftMenu<>(driver, this);
		return this;
	}

	public AccountCreateFormPage accountId(String value) {
		return input(this, this.accountId, value);
	}

	public AccountCreateFormPage firstName(String value) {
		return input(this, this.firstName, value);
	}

	public AccountCreateFormPage lastName(String value) {
		return input(this, this.lastName, value);
	}

	public AccountCreateFormPage password(String value) {
		return input(this, this.password, value);
	}

	public AccountCreateFormPage confirmPassword(String value) {
		return input(this, this.confirmPassword, value);
	}

	public AccountCreateFormPage enabled(boolean value) {
		return select(this, enabled, value);
	}

	public AccountCreateFormPage enabledAutoLogin(boolean value) {
		return select(this, enabledAutoLogin, value);
	}

	public AccountCreateFormPage authorities(String... values) {
		return check(this, authorities, values);
	}

	public AccountDetailPage save() {
		return save(AccountDetailPage.class);
	}

	public <P extends Page<P>> P save(Class<P> nextPage) {
		this.saveBtn.click();
		return loadNextPage(this, nextPage, driver);
	}


}
