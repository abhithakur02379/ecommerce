package ecommerce;

import Util.TestBase;
import com.relevantcodes.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import static org.slf4j.LoggerFactory.getLogger;

public class HomePage extends TestBase {

    private static final Logger logger = getLogger(HomePage.class);

    public HomePage(WebDriver webDriver, ExtentTest extentTest) {
        this.webDriver = webDriver;
        this.extentTest = extentTest;
    }

    @Test
    public void signIn() {

        HomePage_Mappings.HomePage_Factory();


        if (exists("linkSignIn", true, "Home Page Logo")) {
//            logger.info("PASS");
        }
    }

}
