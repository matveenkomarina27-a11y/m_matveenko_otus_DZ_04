import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomeWorkTest {

    private static final Logger log = LogManager.getLogger(HomeWorkTest.class);

    private static final String BASE_URL = "https://wishlist.otus.kartushin.su";

    WebDriver driver;

    @BeforeAll
    public static void install() {
        WebDriverManager.chromedriver().setup();
        log.info("ChromeDriver установлен через WebDriverManager");
    }

    @BeforeEach
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        log.info("Браузер Chrome запущен в режиме headless");
    }

    @AfterEach
    public void close() {
        if (driver != null)
            driver.close();
        log.info("Браузер закрыт");
    }


    private void openInMode(String modeArgument) {
        if (driver != null)
            driver.quit();

        ChromeOptions options = new ChromeOptions();
        options.addArguments(modeArgument);
        driver = new ChromeDriver(options);
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        log.info("Браузер Chrome перезапущен в режиме: " + modeArgument);
    }

    /**
     * Задание №1.
     * Открыть Chrome в headless-режиме, ввести «ОТУС» в поле ввода
     * и проверить, что текст соответствует введённому.
     */
    @Test
    public void headlessInputTest() {
        log.info("=== Задание №1: headless ===");
        driver.get(BASE_URL);
        log.info("Открыта страница: " + BASE_URL);

        var input = driver.findElement(org.openqa.selenium.By.cssSelector("input[type='text'].form-control"));

        input.sendKeys("ОТУС");
        log.info("В поле введён текст: ОТУС");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String actualValue = (String) js.executeScript("return arguments[0].value;", input);

        Assertions.assertEquals("ОТУС", actualValue);
        log.info("Проверка пройдена: в поле действительно «ОТУС»");
    }

    /**
     * Задание №2.
     * Открыть Chrome в режиме киоска, нажать «Открыть модальное окно»
     * и проверить, что модальное окно открылось.
     */
    @Test
    public void kioskModalTest() {
        log.info("=== Задание №2: kiosk ===");
        openInMode("--kiosk");
        driver.get(BASE_URL);
        log.info("Открыта страница: " + BASE_URL);

        // Нажимаем кнопку «Открыть модальное окно»
        // Селектор уточни по своему стенду
        var openButton = driver.findElement(org.openqa.selenium.By.xpath("//button[contains(text(),'Открыть модальное окно')]"));
        openButton.click();
        log.info("Нажата кнопка «Открыть модальное окно»");

        // Ждём появления модального окна и проверяем, что оно видно
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        var modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                org.openqa.selenium.By.cssSelector(".modal.show, .modal[style*='display: block']")));

        Assertions.assertTrue(modal.isDisplayed(), "Модальное окно должно открыться");
        log.info("Модальное окно открылось — проверка пройдена");
    }

    /**
     * Задание №3.
     * Открыть Chrome в полноэкранном режиме, ввести имя и ПАРОЛЬ,
     * нажать «ВОЙТИ» и проверить переход на страницу Списка желаний.
     */
    @Test
    public void fullscreenFormTest() {
        log.info("=== Задание №3: fullscreen ===");
        // Пересоздаём браузер в полноэкранном режиме
        openInMode("--start-fullscreen");
        driver.get(BASE_URL);
        log.info("Открыта страница: " + BASE_URL);

        String name = "Марина";
        String password = "12345678";

        driver.findElement(org.openqa.selenium.By.cssSelector("input[type='text'].form-control")).sendKeys(name);
        driver.findElement(org.openqa.selenium.By.cssSelector("input[type='password'].form-control")).sendKeys(password);
        log.info("Введено имя: " + name + ", пароль: " + password);

        // Нажимаем «Войти»
        driver.findElement(org.openqa.selenium.By.xpath("//button[contains(text(),'Войти')]")).click();
        log.info("Нажата кнопка «Войти»");

        // Ждём появления страницы списка желаний
        var wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/wishlists"));
        log.info("Переход на /wishlists произошёл");

        Assertions.assertEquals(
                "https://wishlist.otus.kartushin.su/wishlists",
                driver.getCurrentUrl(),
                "Должен быть переход на страницу списков желаний"
        );

        var heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[contains(text(),'Мои списки желаний')]")));

        Assertions.assertEquals("Мои списки желаний", heading.getText().trim(),
                "Заголовок на странице должен быть «Мои списки желаний»");
        log.info("Заголовок «Мои списки желаний» найден — проверка пройдена");
    }
}

