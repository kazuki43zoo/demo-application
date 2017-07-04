package com.kazuki43zoo.parts;


import com.kazuki43zoo.pages.Page;
import com.kazuki43zoo.pages.WelcomePage;
import com.kazuki43zoo.utils.PageOperations;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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
		return PageOperations.loadNextPage(page, WelcomePage.class, driver);
	}

	private UserMenuPullDown toggle() {
		this.userMenuLink.click();
		return this;
	}

}
