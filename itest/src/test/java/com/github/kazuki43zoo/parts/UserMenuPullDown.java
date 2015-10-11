package com.github.kazuki43zoo.parts;


import com.github.kazuki43zoo.pages.Page;
import com.github.kazuki43zoo.pages.WelcomePage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.github.kazuki43zoo.utils.PageOperations.loadNextPage;

@Getter
public class UserMenuPullDown<P extends Page<P>> {

	final WebDriver driver;

	@CacheLookup
	@FindBy(id = "userMenuLink")
	private WebElement userMenuLink;

	@CacheLookup
	@FindBy(id = "logoutUserMenu")
	private WebElement logoutUserMenu;

	private P page;

	public UserMenuPullDown(WebDriver driver, P page) {
		this.driver = driver;
		this.page = page;
		PageFactory.initElements(driver, this);
	}

	public WelcomePage logout() {
		toggle();
		this.logoutUserMenu.click();
		return loadNextPage(page, WelcomePage.class, driver);
	}

	private UserMenuPullDown toggle() {
		this.userMenuLink.click();
		return this;
	}

}
