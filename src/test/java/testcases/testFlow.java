package testcases;

import Util.WebDr;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import ecommerce.HomePage;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class testFlow extends WebDr {

    private static final Logger logger = getLogger(testFlow.class);

    public testFlow(ExtentTest extentTest, ExtentReports extentReports) {
        this.extentTest = extentTest;
        this.extentReports = extentReports;
    }

    public void executeTC(String flowName, String preferBrowser) {

        try {
            if ("flow_Login".equals(flowName)) {
                logger.info("Current Scenario is : " + flowName);
                login(preferBrowser);
            }
        } catch (Exception e) {
            logger.error("Error in testFlow is " + e);
        }
    }


    public void login(String preferBrowser) {
        setUp(preferBrowser);
//        new HomePage(webDriver, extentTest).signIn();
        new HomePage(webDriver, extentTest).signIn();

    }

}
