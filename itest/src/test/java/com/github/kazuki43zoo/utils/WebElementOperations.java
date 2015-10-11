package com.github.kazuki43zoo.utils;

import com.github.kazuki43zoo.pages.Page;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebElementOperations {
	private WebElementOperations() {
		// NOP
	}

	public static <P extends Page<P>> P input(P page, WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
		return page;
	}

	public static <P extends Page<P>> P select(P page, List<WebElement> radios, boolean value) {
		radios.get(value ? 0 : 1).click();
		return page;
	}


	public static <P extends Page<P>> P check(P page, List<WebElement> checkboxes, String... selectValues) {
		Set<String> valueSet = new HashSet<>(Arrays.asList(selectValues));
		for (WebElement checkbox : checkboxes) {
			if (valueSet.contains(getValue(checkbox)) && !checkbox.isSelected()) {
				checkbox.click();
			} else if (checkbox.isSelected()) {
				checkbox.click();
			}
		}
		return page;
	}

	public static String getValue(WebElement item) {
		return item.getAttribute("value");
	}

}
