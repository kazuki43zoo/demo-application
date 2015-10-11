package com.github.kazuki43zoo.parts;


import com.github.kazuki43zoo.pages.Page;
import com.github.kazuki43zoo.pages.account.AccountListPage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static com.github.kazuki43zoo.utils.PageOperations.loadNextPage;

@Getter
public class LeftMenu<P extends Page<P>> {

	final WebDriver driver;

	@CacheLookup
	@FindBy(id = "accountsMenuLink")
	private WebElement accountsMenuLink;

	private P page;

	public LeftMenu(WebDriver driver, P page) {
		this.driver = driver;
		this.page = page;
		PageFactory.initElements(driver, this);
	}

	public AccountListPage accountManagement() {
		this.accountsMenuLink.click();
		return loadNextPage(page, AccountListPage.class, driver);
	}

}
