package com.google.pages.setup;


import com.google.pages.BasePage;
import com.google.pages.FirstPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class SetupUI extends GenericUI {

	protected BasePage basePage;
	protected FirstPage firstPage;

	@BeforeClass(alwaysRun = true)
	public void setupBefore() {
		initDriver();
		driver.navigate().to("https://www.google.com/");
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
