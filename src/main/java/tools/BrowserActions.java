package tools;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
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

	public void waitForElementToBeVisible(By fieldLocator) {
		waitForElementToBeVisible(fieldLocator, Duration.ofSeconds(10));
	}

	public void waitForElementToBeVisible(By fieldLocator, Duration maxSecondsTimeout) {
		WebDriverWait wait = new WebDriverWait(getDriver(), maxSecondsTimeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(fieldLocator));

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

	public List<WebElement> findAllElements(By by) {
		List<WebElement> elements = new ArrayList<>();
		LocalTime start = LocalTime.now();

		while (true) {
			try {
				elements = getDriver().findElements(by);
				break;
			} catch (StaleElementReferenceException exc) {
				boolean staleExc = true;
				if (Duration.between(start, LocalTime.now()).getSeconds() >= 5L) {
					throw new Error(String.format("Stale element ref found! after %s sec", 5));
				}

				waitABit(500);
				if (!staleExc) {
					break;
				}
			}
		}
		return elements;
	}

	public void sendKeys(By fieldLocator, String value) {
		LocalTime start = LocalTime.now();

		try {
			waitForElementToBeVisible(fieldLocator);
			if (isElementVisible(fieldLocator)) {
				findElement(fieldLocator).clear();
				findElement(fieldLocator).sendKeys(new CharSequence[]{value});
			} else {
				setEditJavaScript(fieldLocator, value);
			}

			logger.info(String.format("EditBox : %s was filled with value %s", fieldLocator, value));
		} catch (StaleElementReferenceException exc) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= 5L) {
				throw new Error(String.format("Stale element ref found while setting EditBox: %s with value: %s", fieldLocator, value));
			}
			waitABit(1000);
			sendKeys(fieldLocator, value);
		}
	}

	public void setEditJavaScript(By fieldLocator, String value) {
		LocalTime start = LocalTime.now();

		try {
			JavascriptExecutor executor = (JavascriptExecutor) getDriver();
			executor.executeScript("arguments[0].setAttribute('value', '" + value + "')", findElement(fieldLocator));
		} catch (StaleElementReferenceException exc) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error(String.format("Stale element ref found while setting (JavaScript) EditBox: %s with value: %s", fieldLocator, value));
			} else {
				waitABit(500);
				setEditJavaScript(fieldLocator, value);
			}
		}
	}

	public void click(By byLocator) {
		LocalTime start = LocalTime.now();
		waitForElementToBeVisible(byLocator);
		try {
			if (isElementVisible(byLocator)) {
				findElement(byLocator).click();
				logger.info("Element : '" + byLocator + "' was clicked..");
			} else {
				waitForPageToLoad(TimeOuts.TIMEOUT_10);
				clickJavaScript(byLocator);
				logger.info("Element: '" + byLocator + "' was clicked by JavaScript..");
			}
		} catch (StaleElementReferenceException e) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error(String.format("Stale element ref found while clicking on: %s ", byLocator));
			} else {
				waitABit(TimeOuts.TIMEOUT_500);
				click(byLocator);
			}
		} catch (WebDriverException e) {
			if (e.getMessage().contains("is not clickable at point")) {
				clickJavaScript(byLocator);
			} else {
				throw new WebDriverException(String.format("Could not click on element %s , Error: ", byLocator, e.getMessage()));
			}
		}
	}

	public void clickJavaScript(By byLocator) {
		LocalTime start = LocalTime.now();
		try {
			WebElement element = findElement(byLocator);
			JavascriptExecutor executor = (JavascriptExecutor) getDriver();
			executor.executeScript("argument[0].click();", element);
		} catch (StaleElementReferenceException e) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error(String.format("Stale element ref found while clicking (JavaScript) on: %s ", byLocator));
			} else {
				waitABit(TimeOuts.TIMEOUT_500);
				clickJavaScript(byLocator);
			}
		}
	}

	public void clear(By byIdentifier) {
		TODO
	}


	public void waitABit(int timeOutInMilliSeconds) {
		try {
			Thread.sleep(timeOutInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
