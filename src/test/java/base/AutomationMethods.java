package base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigManager;
import utils.DriverManager;
import utils.ElementManager;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import static utils.ElementManager.*;

public class AutomationMethods {
    static String browser = System.getProperty("browser", "chrome"); // Default 'chrome' tarayıcısı


//    public static void click(String element) throws Exception {
//        By xpath = ElementManager.getLocatorFromJson(element);
//        DriverManager.getDriver(browser).findElement(xpath).click();
//    }
//
//    public static boolean isElementVisibilityWithSize(String element) throws IOException {
//
//        By xpath = ElementManager.getLocatorFromJson(element);
//        try {
//            WebDriver driver = DriverManager.getDriver(browser);
//            WebElement webElement = driver.findElement(xpath);
//
//            // Eğer element görünürse ve boyutu sıfırdan büyükse true döndür
//            return webElement.isDisplayed() && webElement.getSize().getHeight() > 0 && webElement.getSize().getWidth() > 0;
//        } catch (NoSuchElementException | StaleElementReferenceException e) {
//            return false; // Element yoksa veya erişilemezse false döndür
//        }
//
//    }

    public static boolean waitForElementToBeVisible(String element, int timeoutInSeconds) throws IOException {
        By xpath = ElementManager.getLocatorFromJson(element);
        try {

            WebDriver driver = DriverManager.getDriver(browser);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

            // Element görünene kadar bekle
            WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));

            return webElement.isDisplayed(); // Eğer element görünüyorsa true döndür
        } catch (TimeoutException e) {
            return false;
        }
    }

    public static void hoverOverElement(String element) throws IOException {

        By xpath = ElementManager.getLocatorFromJson(element);
        WebDriver driver = DriverManager.getDriver(browser);

        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(10));
        WebElement webElement = wait.until(ExpectedConditions.visibilityOfElementLocated(xpath));

        Actions actions = new Actions(driver);
        actions.moveToElement(webElement).perform();
    }

    public static void clickMostExpensiveProduct() {
        String priceXPath = "//div[@data-test-id='price-current-price']";

        // İlk olarak fiyat elementinin görünür olmasını bekle
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(priceXPath))); // Fiyat elementinin görünür olmasını bekle

        // Görünür olan tüm fiyat elementlerini al
        List<WebElement> priceElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(priceXPath)));

        double maxPrice = 0.0;
        WebElement mostExpensiveProduct = null;

        // Fiyatları işle
        for (WebElement priceElement : priceElements) {
            String priceText = priceElement.getText().trim();
            System.out.println("Bulunan fiyat: " + priceText);

            if (!priceText.isEmpty()) {
                try {
                    // Fiyatı düzgün bir formata çevir (Örn: "19.999,00 TL" → "19999.00")
                    String cleanedPrice = priceText.replaceAll("[^0-9,]", ""); // "19.999,00" → "19999,00"
                    cleanedPrice = cleanedPrice.replace(".", "").replace(",", "."); // "19999,00" → "19999.00"

                    double price = Double.parseDouble(cleanedPrice);
                    System.out.println("Temizlenmiş fiyat: " + cleanedPrice);
                    if (price > maxPrice) {
                        maxPrice = price;
                        mostExpensiveProduct = priceElement;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Fiyat dönüştürme hatası: " + priceText);
                }
            }
        }

        // En pahalı ürün bulunduysa tıkla
        if (mostExpensiveProduct != null) {
            System.out.println("En pahalı ürün = " + mostExpensiveProduct);
            try {
                wait.until(ExpectedConditions.elementToBeClickable(mostExpensiveProduct)).click();
                System.out.println("En pahalı ürüne tıklandı: " + maxPrice);
            } catch (ElementClickInterceptedException e) {
                System.out.println("Element tıklanamadı, JavaScript ile tıklanıyor...");
                ((JavascriptExecutor) DriverManager.getDriver(browser)).executeScript("arguments[0].click();", mostExpensiveProduct);
            }
        } else {
            System.out.println("Hiçbir fiyat bulunamadı.");
        }
    }


    public static void switchToNewWindow() {
        WebDriver driver = DriverManager.getDriver(browser);
        String currentWindow = driver.getWindowHandle();

        Set<String> allWindows = driver.getWindowHandles();
        for (String window : allWindows) {
            if (!window.equals(currentWindow)) {
                driver.switchTo().window(window); // Yeni pencereye geçiş yap
                System.out.println("Yeni pencereye geçildi: " + driver.getTitle());
                return;
            }
        }
        System.out.println("Yeni pencere bulunamadı!");
    }

//    public static String getText(String element) throws IOException {
//        By xpath = ElementManager.getLocatorFromJson(element);
//
//        try {
//            WebElement webElement = DriverManager.getDriver(browser).findElement(xpath);
//            return webElement.getText().trim(); // Boşlukları temizleyerek döndür
//        } catch (Exception e) {
//            System.out.println("Element bulunamadı: " + xpath.toString());
//            return ""; // Element bulunamazsa boş string döndür
//        }
//    }


    public static void waitAndClick(String element) throws IOException {
        By xpath = ElementManager.getLocatorFromJson(element);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(100));
        WebElement webElement = wait.until(ExpectedConditions.elementToBeClickable(xpath));
        webElement.click();
    }

//    public static void waitForPageLoadOrStop(int timeoutInSeconds) {
//        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(browser), Duration.ofSeconds(timeoutInSeconds));
//        try {
//            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
//                    .executeScript("return document.readyState").equals("complete"));
//        } catch (TimeoutException e) {
//            System.out.println("Sayfa yüklenmedi, yükleme işlemi iptal ediliyor...");
//            ((JavascriptExecutor) DriverManager.getDriver(browser)).executeScript("window.stop();"); // Sayfa yüklemesini iptal et
//        }
//    }
//
//    public static void sleep(int seconds) {
//        try {
//            Thread.sleep(seconds * 1000L); // Milisaniyeye çevir
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // Thread'i tekrar interrupt duruma al
//            System.out.println("Sleep işlemi kesildi: " + e.getMessage());
//        }
//    }

}
