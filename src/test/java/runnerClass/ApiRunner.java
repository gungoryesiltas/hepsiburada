package runnerClass;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.*;
import utils.DriverManager;

@CucumberOptions(
        features = "src/test/resources",
        glue = "stepDefinitions",
        tags = "@API1",
        plugin = {"pretty", "html:target/cucumber-reports.html", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",},
        monochrome = true
)
public class ApiRunner extends AbstractTestNGCucumberTests {

}
