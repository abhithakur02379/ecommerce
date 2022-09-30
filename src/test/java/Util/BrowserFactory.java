package Util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserFactory {

    //create webdriver object for given browser
    public WebDriver createBrowserInstance(String browser) throws MalformedURLException {

        //	WebDriver driver = null;
        RemoteWebDriver driver = null;

        if (browser.equalsIgnoreCase("Chrome")) {

            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.chrome.silentOutput", "true");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            driver = new ChromeDriver(options);

        } else if (browser.equalsIgnoreCase("firefox")) {

            WebDriverManager.firefoxdriver().setup();
            //	FirefoxOptions foptions = new FirefoxOptions();
            //	foptions.addArguments("-private");

            driver = new RemoteWebDriver(new URL("http:192.168.225.219:4444/wd/hub"), DesiredCapabilities.firefox());


            //driver = new FirefoxDriver(foptions);

        }
        return driver;
    }
}
