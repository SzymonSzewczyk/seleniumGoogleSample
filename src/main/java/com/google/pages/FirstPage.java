package com.google.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstPage extends BasePage {
	private static final Logger logger = LoggerFactory.getLogger(FirstPage.class);

	private final By popupBeforeYouGoContainer = By.xpath("//div[@title='Zanim przejdziesz do wyszukiwarki Google']");

	public FirstPage(WebDriver driver) {
		super(driver);
	}

	public boolean isPopupBeforeYouGoVisible() {
		logger.info("Checking if 'popup before you go' is visible");
		return isElementVisible(popupBeforeYouGoContainer);
	}



}
