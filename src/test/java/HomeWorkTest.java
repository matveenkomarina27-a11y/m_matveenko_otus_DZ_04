import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomeWorkTest {

    private static final Logger log = LogManager.getLogger(HomeWorkTest.class);

    private static final String BASE_URL = System.getProperty("base.url",
            "https://the-internet.herokuapp.com");

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createDriver("--headless=new");
        log.info("Браузер запущен в headless-режиме");
    }

    @AfterEach
    public void close() {
        if (driver != null) {
            driver.quit();
        }
        log.info("Браузер закрыт");
    }

    private void openInMode(String modeArgument) {
        driver.quit();
        driver = WebDriverFactory.createDriver(modeArgument);
        log.info("Браузер перезапущен в режиме: " + modeArgument);
    }

    /**
     * Задание №1.
     * Открыть Chrome в headless-режиме, перейти на /inputs,
     * ввести 42 в поле ввода и проверить, что значение соответствует введённому.
     */
    @Test
    public void headlessInputTest() {
        log.info("=== Задание №1: headless ===");
        driver.get(BASE_URL + "/inputs");
        log.info("Открыта страница: " + BASE_URL + "/inputs");

        var input = driver.findElement(By.cssSelector("input[type='number']"));

        input.sendKeys("42");
        log.info("В поле введено значение: 42");

        String actualValue = input.getAttribute("value");

        Assertions.assertEquals("42", actualValue);
        log.info("Проверка пройдена: в поле действительно «42»");
    }

    /**
     * Задание №2.
     * Открыть Chrome в режиме киоска, перейти на /dialog,
     * нажать «Open modal dialog» и проверить, что модальное окно открылось.
     */
    @Test
    public void kioskModalTest() {
        log.info("=== Задание №2: kiosk ===");
        openInMode("--kiosk");
        driver.get(BASE_URL + "/dialog");
        log.info("Открыта страница: " + BASE_URL + "/dialog");

        driver.findElement(By.id("open-modal")).click();
        log.info("Нажата кнопка «Open modal dialog»");

        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        var modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("modal-dialog")));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean isModalOpen = (Boolean) js.executeScript(
                "return arguments[0].open && arguments[0].matches(':modal');", modal);

        Assertions.assertEquals(Boolean.TRUE, isModalOpen, "Модальное окно должно открыться");
        log.info("Модальное окно открылось — проверка пройдена");
    }

    /**
     * Задание №3.
     * Открыть Chrome в полноэкранном режиме, перейти на /login,
     * ввести tomsmith / SuperSecretPassword!, нажать «Login»
     * и проверить поле «Logged in as tomsmith».
     */
    @Test
    public void fullscreenFormTest() {
        log.info("=== Задание №3: fullscreen ===");
        openInMode("--start-fullscreen");
        driver.get(BASE_URL + "/login");
        log.info("Открыта страница: " + BASE_URL + "/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        log.info("Введены имя пользователя и пароль");

        driver.findElement(By.cssSelector("#login button[type='submit']")).click();
        log.info("Нажата кнопка «Login»");

        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        var loggedIn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(text(),'Logged in as')]")));
        Assertions.assertEquals("Logged in as tomsmith", loggedIn.getText().trim(),
                "После входа должно отображаться «Logged in as tomsmith»");

        var username = loggedIn.findElement(By.id("username"));
        Assertions.assertEquals("tomsmith", username.getText());
        log.info("Поле «Logged in as tomsmith» найдено — проверка пройдена");
    }
}
