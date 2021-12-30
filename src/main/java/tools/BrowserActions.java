package tools;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	public void waitForPageToLoad(int timeOutInSeconds) {
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

	public void waitForElementToBeVisible(By fieldLocator) {
		waitForElementToBeVisible(fieldLocator, Duration.ofSeconds(10));
	}

	public void waitForElementToBeVisible(By fieldLocator, Duration maxSecondsTimeout) {
		WebDriverWait wait = new WebDriverWait(getDriver(), maxSecondsTimeout);
		wait.until(ExpectedConditions.visibilityOfElementLocated(fieldLocator));
		waitForElementToBeDisplayed(fieldLocator, maxSecondsTimeout);
	}

	private void waitForElementToBeDisplayed(By fieldLocator, Duration maxSecondsTimeout) {
		LocalTime start = LocalTime.now();
		boolean isFound = false;
		do {
			try {
				if (getDriver().findElement(fieldLocator).isDisplayed()) {
					isFound = true;
				} else {
					logger.info("Sleeping while element " + fieldLocator + " is not displayed!");
					waitABit(TimeOuts.TIMEOUT_100);
				}
			} catch (WebDriverException exc) {
				logger.info("WebDriverException found... Sleeping while element " + fieldLocator + " is is not displayed!");
				waitABit(TimeOuts.TIMEOUT_100);
			}
			if (Duration.between(start, LocalTime.now()).getSeconds() >= maxSecondsTimeout.getSeconds()) {
				throw new WebDriverException("Element " + fieldLocator + " was not found after timeout: " + maxSecondsTimeout.getSeconds() + " sec.");
			}
		} while (!isFound);
	}

	public void fluentWaitForElementToBeVisible(By fieldLocator, Duration maxSecondsTimeout) {
		FluentWait fluentWait = new FluentWait(getDriver())
				.withTimeout(maxSecondsTimeout)
				.pollingEvery(Duration.ofSeconds(1))
				.ignoring(NoSuchElementException.class);

		fluentWait.until(ExpectedConditions.visibilityOfElementLocated(fieldLocator));

		// alternative usage
//			WebElement element = (WebElement) fluentWait.until(new Function<WebDriver, WebElement>() {
//				public WebElement apply(WebDriver driver) {
//					//it is possible to put more check logic here
//					return getDriver().findElement(fieldLocator);
//				}
//			});
	}

	public void waitForElementDisappears(By fieldLocator, Duration maxSecondsTimeout) {
		WebDriverWait wait = new WebDriverWait(getDriver(), maxSecondsTimeout);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(fieldLocator));
	}

	public void waitABit(int timeOutInMilliSeconds) {
		try {
			Thread.sleep(timeOutInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
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

	public void clickLinkByPartialText(String partialText) {
		click(By.partialLinkText(partialText));
	}

	public void clickLinkByExactText(String exactText) {
		click(By.linkText(exactText));
	}

	public void rightClick(WebElement elementToClick) {
		LocalTime start = LocalTime.now();
		try {
			Actions actions = new Actions(getDriver());
			actions.moveToElement(elementToClick);
			actions.contextClick(elementToClick);
			actions.perform();
		} catch (StaleElementReferenceException exc) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error("Stale element ref found while right clicking on: " + elementToClick);
			} else {
				waitABit(TimeOuts.TIMEOUT_1000);
				rightClick(elementToClick);
			}
		}
	}

	public void selectValueFromDropDown(By byIdentifier, String value) {
		selectComboBox(byIdentifier, value);
	}

	public void selectComboBox(By byIdentifier, String value) {
		LocalTime start = LocalTime.now();
		try {
			waitForElementToBeVisible(byIdentifier);
			if (isElementVisible(byIdentifier)) {
				Select dropdown = new Select(findElement(byIdentifier));
				dropdown.selectByVisibleText(value);
				logger.info(String.format("Dropdown: '%s' was selected with value: %s ", byIdentifier, value));
			}
		} catch (StaleElementReferenceException exc) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error(String.format("Stale element ref found while selecting dropdown '%s' with value: %s ", byIdentifier, value));
			} else {
				waitABit(TimeOuts.TIMEOUT_1000);
				selectComboBox(byIdentifier, value);
			}
		}
	}

	protected WebElement findElementByExactText(String text) {
		List<WebElement> elements = getDriver().findElements(By.xpath("//*[text()='" + text + "']"));
		if (elements.size() > 1) {
			throw new Error(String.format("More than one elements found by text '%s'! Specify more precise selector.", text));
		}
		return elements.get(0);
	}

	protected WebElement findElementByPartText(String text) {
		List<WebElement> elements = getDriver().findElements(By.xpath("//*[contains(text(), '" + text + "']"));
		if (elements.size() > 1) {
			throw new Error(String.format("More than one elements found by text '%s'! Specify more precise selector.", text));
		}
		return elements.get(0);
	}

	protected WebElement findElementByPartClassName(String partClassName) {
		List<WebElement> elements = getDriver().findElements(By.xpath("//*[contains(@class, '" + partClassName + "']"));
		if (elements.size() > 1) {
			throw new Error(String.format("More than one elements found by partClassName '%s'! Specify more precise selector.", partClassName));
		}
		return elements.get(0);
	}

	public void clear(By byIdentifier) {
		findElement(byIdentifier).clear();
	}

	public List<String> getComboBoxOptions(By byIdentifier) {
		LocalTime start = LocalTime.now();
		logger.info("Getting available options in ComboBox>: " + byIdentifier);
		List<String> comboOptions = new ArrayList<>();
		try {
			List<WebElement> allOptions;
			waitForElementToBeVisible(byIdentifier);
			if (isElementVisible(byIdentifier)) {
				Select dropdown = new Select(findElement(byIdentifier));
				allOptions = dropdown.getOptions();
			} else {
				waitForPageToLoad(TimeOuts.TIMEOUT_10);
				WebElement combo = findElement(byIdentifier);
				Select dropdown = new Select(combo);
				allOptions = dropdown.getOptions();
			}
			allOptions.stream()
					.filter(option -> option.isDisplayed())
					.forEach(option -> comboOptions.add(option.getText()));
		} catch (StaleElementReferenceException exc) {
			if (Duration.between(start, LocalTime.now()).getSeconds() >= TimeOuts.TIMEOUT_5) {
				throw new Error(String.format("Stale element ref found while getting ComboBox '%s' options.", byIdentifier));
			} else {
				waitABit(TimeOuts.TIMEOUT_1000);
				getComboBoxOptions(byIdentifier);
			}
		}
		return comboOptions;
	}

	public void switchToFrame(String frameNameOrID) {
		WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TimeOuts.TIMEOUT_5));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameNameOrID));
		logger.info("Switched to frame: " + frameNameOrID);
	}

	public void switchToWindow(String windowName, int maxTimeOutInSeconds) {
		boolean windowFound = false;
		LocalTime start = LocalTime.now();
		do {
			Set<String> windowsHandles = getDriver().getWindowHandles();
			for (String window : windowsHandles) {
				getDriver().switchTo().window(window);
				String currentTitle = getTitle();
				if (currentTitle.contains(windowName)) {
					logger.info("Found window: '" + windowName + "' and switched to it.");
					windowFound = true;
					break;
				} else {
					if (Duration.between(start, LocalTime.now()).getSeconds() >= maxTimeOutInSeconds) {
						throw new WebDriverException(String.format("Window %s was not found within %s sec", windowName, maxTimeOutInSeconds));
					} else waitABit(TimeOuts.TIMEOUT_500);
				}
			}
		} while (!windowFound);
	}

	public String getTitle() {
		String title = getDriver().getTitle();
		logger.info("Pate title is: " + title);
		return title;
	}

}
