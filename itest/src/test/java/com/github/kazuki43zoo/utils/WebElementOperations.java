package com.github.kazuki43zoo.utils;

import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebElementOperations {
	private WebElementOperations() {
		// NOP
	}


	public static void inputValue(WebElement element, String value) {
		element.clear();
		element.sendKeys(value);
	}

	public static void check(List<WebElement> checkboxes, String... selectValues) {
		Set<String> valueSet = new HashSet<>(Arrays.asList(selectValues));
		for (WebElement checkbox : checkboxes) {
			if (valueSet.contains(checkbox.getAttribute("value")) && !checkbox.isSelected()) {
				checkbox.click();
			} else if (checkbox.isSelected()) {
				checkbox.click();
			}
		}

	}

}
