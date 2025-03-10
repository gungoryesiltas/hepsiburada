package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverManager;
import utils.ElementManager;

import static utils.ElementManager.*;


public class TestBase {
    @BeforeClass
    public void setup() {
        String browser = System.getProperty("browser", "chrome");  // Varsayılan olarak chrome
        DriverManager.getDriver(browser);
    }

    @AfterClass(alwaysRun = true) // Test sınıfı tamamlandığında çalışır
    public void tearDown() {
        String browser = System.getProperty("browser", "chrome");
        WebDriver driver = DriverManager.getDriver(browser);
        if (driver != null) {
            driver.quit(); // Tüm tarayıcı pencerelerini kapat
        }
    }
    }
