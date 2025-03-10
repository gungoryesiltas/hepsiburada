package stepDefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import utils.ConfigManager;

import utils.DriverManager;

import java.io.IOException;

import static base.AutomationMethods.*;

public class GeneralSteps {


    @Given("Go to url")
    public void go_to_url() throws Exception {
        String browser = System.getProperty("browser", "chrome"); // Default 'chrome' tarayıcısı

        // Tarayıcıyı parametre olarak DriverManager'a iletmek
        DriverManager.getDriver(browser).get(ConfigManager.getProperty("url"));
        waitForElementToBeVisible("CEREZLERI_KABUL_ET_BUTON",5);
//        if(isElementVisibilityWithSize("CEREZLERI_KABUL_ET_BUTON")){
//            click("CEREZLERI_KABUL_ET_BUTON");
//        }
    }

    @And("Click {string}")
    public void clickElement(String element) throws Exception {

        waitAndClick(element);
    }

    @And("Hover {string}")
    public void hover(String element) throws IOException {
        hoverOverElement(element);
    }

    @And("Switch new window")
    public void switchNewWindow() {
        switchToNewWindow();
    }

//    @And("Wait in seconds {string}")
//    public void waitInSeconds(int second) {
//        sleep(second);
//    }
}
