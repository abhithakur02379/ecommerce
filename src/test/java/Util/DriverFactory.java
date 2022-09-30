package Util;

import org.openqa.selenium.WebDriver;

public class DriverFactory {

    private static final DriverFactory instance = new DriverFactory();
    //factory design pattern --> define separate factory methods for creating objects and create objects by calling that methods
    ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

    //Singleton design Pattern
    //private constructor so that no one else can create object of this class
    private DriverFactory() {

    }

    public static DriverFactory getInstance() {
        return instance;
    }

    public WebDriver getDriver() {
        return driver.get();
    }

    public void setDriver(WebDriver driverParm) {
        driver.set(driverParm);
    }


    public void closeBrowser() {
        driver.get().quit();
        driver.remove();
    }
}
