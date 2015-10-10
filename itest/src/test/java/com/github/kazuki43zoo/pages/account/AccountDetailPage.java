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

import static com.github.kazuki43zoo.utils.WebElementOperations.inputValue;

@Getter
public class AccountDetailPage implements Page<AccountDetailPage> {

	private final WebDriver driver;

	@CacheLookup
	@FindBy(id = "saveBtn")
	private WebElement saveBtn;

	private UserMenuPullDown<AccountDetailPage> userMenuPullDown;
	private LanguagePullDown<AccountDetailPage> languagePullDown;
	private LeftMenu<AccountDetailPage> leftMenu;

	public AccountDetailPage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	public AccountDetailPage reload() {
		PageFactory.initElements(driver, this);
		this.userMenuPullDown = new UserMenuPullDown<>(driver, this);
		this.languagePullDown = new LanguagePullDown<>(driver, this);
		this.leftMenu = new LeftMenu<>(driver, this);
		return this;
	}

	public void save() {
		this.saveBtn.click();
	}


	private AccountDetailPage input(WebElement element, String value) {
		inputValue(element, value);
		return this;
	}

}
