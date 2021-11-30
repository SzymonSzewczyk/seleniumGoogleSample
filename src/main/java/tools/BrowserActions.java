package tools;

import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

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
				logger.info("Sleeping while page status is: " + ((JavascriptExecutor) getDriver()).executeScript("return document.readyState", new Object[0]));
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

	public void openURL(String url) {
		try {
			logger.info("Opening page URL: " + url);
			getDriver().navigate().to(url);
			getDriver().manage().window().maximize();
			logger.info("Page was opened and window maximized");
		} catch (WebDriverException webDriverException) {
			logger.error(String.format("Could not navigate to URL: %s ", url));
		}
	}

	public WebElement findElement(By by) {
		boolean isFound = false;
		WebElement element = null;
		LocalTime start = LocalTime.now();

		while (true) {
			try {
				List<WebElement> elements = getDriver().findElements(by);
				if (elements.size() <= 0) {
					throw new NoSuchElementException(String.format("Element matching criteria: %s was not found!", by));
				}

				element = elements.get(0);
				isFound = true;
			} catch (StaleElementReferenceException e) {
				if (Duration.between(start, LocalTime.now()).getSeconds() >= 5L) {
					throw new Error(String.format("Stale element ref found! after %s sec", 5));
				}

				waitABit(500);
				if (isFound) {
					break;
				}
			}
		}
		return element;
	}

	private void waitABit(int timeOutInMilliSeconds) {
		try {
			Thread.sleep(timeOutInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
