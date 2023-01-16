package com.google.pages.setup;


import com.google.pages.BasePage;
import com.google.pages.FirstPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class SetupUI extends GenericUI {
	protected final Logger logger = LoggerFactory.getLogger(SetupUI.class);

	protected BasePage basePage;
	protected FirstPage firstPage;

	@BeforeClass(alwaysRun = true)
	public void setupBefore() {
		initDriver();
		driver.navigate().to("https://flipkart.com");
		initPages();
	}

	private void initPages() {
		basePage = new BasePage(driver);
		firstPage = new FirstPage(driver);
	}

	@AfterClass(alwaysRun = true)
	public void cleanup() {
		quitDriver();
	}


}
