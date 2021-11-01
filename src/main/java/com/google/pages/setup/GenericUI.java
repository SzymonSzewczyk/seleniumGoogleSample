package com.google.pages.setup;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class GenericUI {
	protected final Logger logger = LoggerFactory.getLogger(GenericUI.class);
	protected WebDriver driver;

	protected void initDriver() {
		logger.info("Initializing chromeDriver");
		driver = WebDriverManager.chromedriver().create();
	}

	protected void quitDriver() {
		logger.info("Quit webDriver");
		driver.quit();
	}

	public WebDriver getDriver() {
		return driver;
	}
}
