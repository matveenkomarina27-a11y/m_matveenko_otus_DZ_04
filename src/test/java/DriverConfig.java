import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

public class DriverConfig {

    private static final String BASE_URL = System.getProperty("base.url",
            "https://the-internet.herokuapp.com");

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.createDriver("--headless", "--start-maximized");
    }

    @AfterEach
    public void close() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void cookieTest() {
        driver.get(BASE_URL);
        //Добавить Cookie#1 с параметром Otus1 и значением Value1
        driver.manage().addCookie(new Cookie("Otus1", "Value1"));
        //Добавить Cookie#2 с параметром Otus2 и значением Value2
        driver.manage().addCookie(new Cookie("Otus2", "Value2"));
        //Добавить Cookie#3 с параметром Otus3 и значением Value3
        // (добавлять через переменную, переменная должна быть сохранена)
        var cookie = new Cookie("Otus3", "Value3");
        driver.manage().addCookie(cookie);
        // Добавить Cookie#4 с параметром Otus4 и значением Value4
        driver.manage().addCookie(new Cookie("Otus4", "Value4"));
        //Вывести на экран все Cookies
        System.out.println(driver.manage().getCookies());
        //Вывести на экран Cookie1
        System.out.println(driver.manage().getCookieNamed("Otus1"));
        //Удалить Cookie#2 по имени куки
        driver.manage().deleteCookieNamed("Otus2");
        //Удалить Cookie#3 по переменной Cookie
        driver.manage().deleteCookie(cookie);
        //Удалить все куки, убедиться что их нет
        driver.manage().deleteAllCookies();
        Assertions.assertEquals(0, driver.manage().getCookies().size());
    }

    @Test
    public void windowTest() throws InterruptedException {
        System.out.println(driver.manage().window().getSize());
        driver.manage().window().setSize(new Dimension(800, 600));
        System.out.println(driver.manage().window().getPosition());
        Point point = driver.manage().window().getPosition();
        driver.manage().window().setPosition(new Point(point.x + 100, point.y));
        Thread.sleep(2000);
        driver.manage().window().setPosition(new Point(point.x + 100, point.y + 100));
        Thread.sleep(2000);
        driver.manage().window().setPosition(new Point(point.x, point.y + 100));
        Thread.sleep(2000);
        driver.manage().window().setPosition(new Point(point.x, point.y));
        Thread.sleep(2000);
    }
}
