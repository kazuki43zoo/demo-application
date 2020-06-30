package com.kazuki43zoo.pages.account;


import com.kazuki43zoo.pages.Page;
import com.kazuki43zoo.parts.LanguagePullDown;
import com.kazuki43zoo.parts.LeftMenu;
import com.kazuki43zoo.parts.UserMenuPullDown;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class AccountListPage implements Page<AccountListPage> {

	private final WebDriver driver;

	@CacheLookup
	@FindBy(id = "createBtn")
	private WebElement createBtn;

	private UserMenuPullDown<AccountListPage> userMenuPullDown;
	private LanguagePullDown<AccountListPage> languagePullDown;
	private LeftMenu<AccountListPage> leftMenu;

	public AccountListPage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	@Override
	public AccountListPage reload() {
		PageFactory.initElements(driver, this);
		this.userMenuPullDown = new UserMenuPullDown<>(driver, this);
		this.languagePullDown = new LanguagePullDown<>(driver, this);
		this.leftMenu = new LeftMenu<>(driver, this);
		return this;
	}

	public AccountCreateFormPage create() {
		this.createBtn.click();
		return new AccountCreateFormPage(driver);
	}

}
