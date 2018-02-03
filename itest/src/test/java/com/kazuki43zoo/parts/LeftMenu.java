package com.kazuki43zoo.parts;


import com.kazuki43zoo.pages.Page;
import com.kazuki43zoo.pages.account.AccountListPage;
import com.kazuki43zoo.utils.PageOperations;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LeftMenu<P extends Page<P>> {

	private final WebDriver driver;

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
		return PageOperations.loadNextPage(page, AccountListPage.class, driver);
	}

}
