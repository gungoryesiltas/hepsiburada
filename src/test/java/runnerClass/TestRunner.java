package runnerClass;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;
import utils.DriverManager;

@CucumberOptions(
        features = "src/test/resources",
        glue = "stepDefinitions",
        tags = "@ADD_PRODUCT",
        plugin = {"pretty", "html:target/cucumber-reports.html", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",},
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeTest
    @Parameters("browser")
    public void setup(String browser) {
        DriverManager.getDriver(browser);  // Test başlarken doğru browser'ı alacak
    }

    @AfterMethod
    public void teardown() {
        DriverManager.quitDriver();
    }
//    @Test
//    public void runCucumber() {
//        // Bu metod Cucumber TestRunner'ı başlatacak
//        super.runCucumber();
//    }
}
