import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Фабрика WebDriver. Браузер задаётся системным свойством -Dbrowser
 * (по умолчанию chrome, также поддерживается firefox).
 * Режим запуска (headless, kiosk, fullscreen и т.д.) передаётся аргументами.
 */
public final class WebDriverFactory {

    private WebDriverFactory() {
    }

    public static WebDriver createDriver(String... arguments) {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        switch (browser) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                for (String argument : arguments) {
                    options.addArguments(argument);
                }
                return new FirefoxDriver(options);
            }
            case "chrome": {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                for (String argument : arguments) {
                    options.addArguments(argument);
                }
                return new ChromeDriver(options);
            }
            default:
                throw new IllegalArgumentException("Неизвестный браузер: " + browser
                        + ". Допустимые значения: chrome, firefox");
        }
    }
}
