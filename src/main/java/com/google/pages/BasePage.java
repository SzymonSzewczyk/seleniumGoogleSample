package com.google.pages;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.BrowserActions;

public class BasePage extends BrowserActions {
	private static Logger logger = LoggerFactory.getLogger(BasePage.class);

	public BasePage(WebDriver driver) {
		super(driver);
	}
}
