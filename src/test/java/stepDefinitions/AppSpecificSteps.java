package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.ConfigManager;

import utils.DriverManager;
import utils.ElementManager;

import java.io.IOException;
import java.time.Duration;

import static base.AutomationMethods.*;

public class AppSpecificSteps {

    String browser = System.getProperty("browser", "chrome");
    String productDetailPrice;
    String cartProductPrice;


    @And("Click the most expensive product")
    public void clickTheMostExpensiveProduct() {
        clickMostExpensiveProduct();
    }


    @And("Get product price from product detail page")
    public void getProductPriceFromProductDetailPage() throws IOException {
        By xpath = ElementManager.getLocatorFromJson("PRODUCT_PAGE_PRODUCT_PRICE");
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
        productDetailPrice = webElement.getText();
        System.out.println("productPrice = " + productDetailPrice);
    }

    @And("Assert that product detail price and cart product price are same")
    public void assertThatProductDetailPriceAndCartProductPriceAreSame() throws IOException {

        By xpath = ElementManager.getLocatorFromJson("CART_PAGE_PRODUCT_PRICE");
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));
        cartProductPrice = webElement.getText();
        Assert.assertEquals(productDetailPrice,cartProductPrice,"Product prices are not same");
    }


}
