package tools;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserActions {
	private static final Logger logger = LoggerFactory.getLogger(BrowserActions.class);
	private WebDriver driver;

	public BrowserActions(WebDriver webDriver) {
		this.driver = webDriver;
	}

	public WebDriver getDriver() {
		return this.driver;
	}

	public void refresh() {
		logger.info("Refreshing page..");
		getDriver().navigate().refresh();
		waitForPageToLoad(5);
	}

	private void waitForPageToLoad(int timeOutInSeconds) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis() + 1000L * (long) timeOutInSeconds;

		for (long currentTime = startTime;
			 !((JavascriptExecutor) this.driver).executeScript("return document.readyState", new Object[0]).equals("complete") && currentTime <= endTime;
			 currentTime = System.currentTimeMillis()) {
			try {
				Thread.sleep(500L);
				this.logger.info("Sleeping while page status is: " + ((JavascriptExecutor) this.driver).executeScript("return document.readyState", new Object[0]));
			} catch (InterruptedException e) {
				logger.error("Interrupted while waiting");
			}
		}
	}

	public boolean isElementVisible(By elementLocator) {
		boolean result = false;
		try {
			result = getDriver().findElement(elementLocator).isDisplayed();
			logger.info("Element found");
		} catch (NoSuchElementException e) {
			logger.info("Element not found");
		}
		return result;
	}
}
