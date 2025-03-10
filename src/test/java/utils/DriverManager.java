package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverManager {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver(String browserType) {
        if (driver.get() == null) {  // Eğer thread'deki driver null ise
            switch (browserType.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-notifications");
                    chromeOptions.addArguments("--disable-popup-blocking");
                    WebDriverManager.chromedriver().setup();
                    driver.set(new ChromeDriver(chromeOptions));  // Chrome driver'ı oluştur
                    break;

                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                    WebDriverManager.firefoxdriver().setup();
                    driver.set(new FirefoxDriver(firefoxOptions));  // Firefox driver'ı oluştur
                    break;

                default:
                    throw new IllegalArgumentException("Geçersiz tarayıcı türü: " + browserType);
            }

            driver.get().manage().window().maximize();
        }
        return driver.get();  // Her test için thread'e özel driver'ı döndür
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();  // ThreadLocal'dan driver'ı temizle
        }
    }
}
