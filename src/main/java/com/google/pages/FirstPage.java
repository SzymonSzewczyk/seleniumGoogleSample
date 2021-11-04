package com.google.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class FirstPage extends BaseGPage {
	private static final Logger logger = LoggerFactory.getLogger(BaseGPage.class);
	private final By inputToDo = By.xpath("//form[@ng-submit='vm.addTodo()']/input");
	private final By label = By.xpath("//div[@class='view']/..//label");


	public FirstPage(WebDriver driver) {
		super(driver);
	}


	public FirstPage createTodo(String valueToType) {
		logger.info("Create todo field with value:" + valueToType);
		getDriver().findElement(inputToDo).sendKeys(valueToType);
		getDriver().findElement(inputToDo).sendKeys(Keys.RETURN);
		return new FirstPage(getDriver());
	}

	public List<String> getTodosTexts() {
		logger.info("Getting Todos texts");
		List<String> labelsText = new ArrayList<>();
		List<WebElement> labelsList = getDriver().findElements(label);
		for (WebElement e : labelsList) {
			logger.info(e.getText());
			labelsText.add(e.getText());
		}
		return labelsText;
	}

	public int getNumberOfTodos() {
		logger.info("Getting number of ToDos");
		List<WebElement> labelsList = getDriver().findElements(label);
		return labelsList.size();
	}
}
