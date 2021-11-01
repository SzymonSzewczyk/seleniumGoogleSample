package com.google.pages;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.BrowserActions;

public class BaseGPage extends BrowserActions {
	private static Logger logger = LoggerFactory.getLogger(BaseGPage.class);

	public BaseGPage(WebDriver driver) {
		super(driver);
	}
}
