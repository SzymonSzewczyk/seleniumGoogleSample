package com.google.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class FirstPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(BasePage.class);

    private By popupBeforeYouGo = By.xpath("//div[text()=\"Zanim przejdziesz do wyszukiwarki Google\"]/../../../../div");
    private By popupBeforeYouGoContainer = By.xpath("//div[@title='Zanim przejdziesz do wyszukiwarki Google']");

    public FirstPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPopupBeforeYouGoVisible() {
        logger.info("Checking if 'popup before you go' is visible");
        return isElementVisible(popupBeforeYouGoContainer);
    }


    //quick code for stack https://stackoverflow.com/questions/75099460/is-there-any-option-handle-popup-using-java-with-selenium-which-has-html-propert
    public void flipCartHelp() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

        getDriver().navigate().to("https://www.flipkart.com/sony-zv-e10l-mirrorless-camera-body-1650-mm-power-zoom-lens-vlog/p/itmed07cbb694444");
        getDriver().findElement(By.xpath("//*[@class='_1KOMV2']")).click();

        By pinCodeButtonLocator = By.xpath("//*[contains(text(),'Enter Delivery Pincode')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(pinCodeButtonLocator));

        getDriver().findElement(By.xpath("//*[contains(text(),'Enter Delivery Pincode')]")).click();

        By enterPincodeFieldLocator = By.xpath("//input[@placeholder='Enter pincode']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(enterPincodeFieldLocator));

        getDriver().findElement(By.xpath("//input[@placeholder='Enter pincode']")).sendKeys("522660");
//        getDriver().findElement(By.xpath("//input[@placeholder='Enter pincode']")).sendKeys(Keys.RETURN); //this also works
        getDriver().findElement(By.xpath("//div[text() = 'Submit']")).click();
        Thread.sleep(10000);

    }
}
