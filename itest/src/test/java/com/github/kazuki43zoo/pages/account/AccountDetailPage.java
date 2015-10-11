package com.github.kazuki43zoo.pages.account;


import com.github.kazuki43zoo.pages.Page;
import com.github.kazuki43zoo.parts.LanguagePullDown;
import com.github.kazuki43zoo.parts.LeftMenu;
import com.github.kazuki43zoo.parts.UserMenuPullDown;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

@Getter
public class AccountDetailPage implements Page<AccountDetailPage> {

	private final WebDriver driver;

	private UserMenuPullDown<AccountDetailPage> userMenuPullDown;
	private LanguagePullDown<AccountDetailPage> languagePullDown;
	private LeftMenu<AccountDetailPage> leftMenu;

	public AccountDetailPage(WebDriver driver) {
		this.driver = driver;
		reload();
	}

	@Override
	public AccountDetailPage reload() {
		PageFactory.initElements(driver, this);
		this.userMenuPullDown = new UserMenuPullDown<>(driver, this);
		this.languagePullDown = new LanguagePullDown<>(driver, this);
		this.leftMenu = new LeftMenu<>(driver, this);
		return this;
	}

}
