package Util;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import testcases.Driver;

import java.util.concurrent.TimeUnit;

public class TestBase extends WebDr {

    BrowserFactory bf = new BrowserFactory();

    @BeforeTest
    public void LaunchApplication() throws Exception {

        setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
        String browser = getCellData(1, 2);
        DriverFactory.getInstance().setDriver(bf.createBrowserInstance(browser));

        DriverFactory.getInstance().getDriver().manage().window().maximize();
        DriverFactory.getInstance().getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
        String url = getCellData(6, 2);
        DriverFactory.getInstance().getDriver().navigate().to(url);

    }

    @AfterTest
    public void tearDown() {
        DriverFactory.getInstance().closeBrowser();
    }
}
