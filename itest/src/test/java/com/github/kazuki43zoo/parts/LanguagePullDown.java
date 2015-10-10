package com.github.kazuki43zoo.parts;


import com.github.kazuki43zoo.pages.Page;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Getter
public class LanguagePullDown<P extends Page<P>> {

	private final WebDriver driver;

	@CacheLookup
	@FindBy(id = "languageSwitchLink")
	private WebElement languageSwitchLink;

	@CacheLookup
	@FindBy(id = "englishLink")
	private WebElement englishLink;

	@CacheLookup
	@FindBy(id = "japaneseLink")
	private WebElement japaneseLink;

	private final P page;

	public LanguagePullDown(WebDriver driver, P page) {
		this.driver = driver;
		this.page = page;
		PageFactory.initElements(driver, this);
	}

	public P japanese() {
		toggle();
		this.japaneseLink.click();
		return page.reload();
	}

	public P english() {
		toggle();
		this.englishLink.click();
		return page.reload();
	}

	private LanguagePullDown<P> toggle() {
		this.languageSwitchLink.click();
		return this;
	}


}
