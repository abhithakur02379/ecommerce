package testcases;

import Util.Launcher;
import org.slf4j.Logger;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Driver {

    private static final Logger logger = getLogger(Driver.class);

    public static String userDir, file_TestData;
    public static Map<String, String> dictionary = new HashMap<>();
    Launcher objLauncher = new Launcher();

    @Test
    public static void main(String[] args){
        Driver ob = new Driver();
        ob.mainDriver("Chrome");
    }

    @Parameters("Browser")
    public void mainDriver(String testingBrowser){
        try {
            userDir = System.getProperty("user.dir");
            file_TestData = "MasterTestData.xlsx";
            objLauncher.InvokeLauncher(testingBrowser);
        } catch (Exception e) {
            logger.error("Exception in testcases.Driver.mainDriver is " + e);
        }
    }

}
