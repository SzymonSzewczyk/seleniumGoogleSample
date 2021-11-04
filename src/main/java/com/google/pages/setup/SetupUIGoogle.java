package com.google.pages.setup;


import com.google.pages.BaseGPage;
import com.google.pages.FirstPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class SetupUIGoogle extends GenericUI {
	protected final Logger logger = LoggerFactory.getLogger(SetupUIGoogle.class);

	protected BaseGPage basePage;
	protected FirstPage firstPage;

	@BeforeClass(alwaysRun = true)
	public void setupBefore() {
		initDriver();
		driver.navigate().to("https://todomvc.com/examples/typescript-angular/#/");
		initPages();
	}

	private void initPages() {
		basePage = new BaseGPage(driver);
		firstPage = new FirstPage(driver);
	}

	@AfterClass(alwaysRun = true)
	public void cleanup() {
		quitDriver();
	}


}
