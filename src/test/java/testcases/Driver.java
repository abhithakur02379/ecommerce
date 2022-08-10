package testcases;

import Util.Launcher;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class Driver {

    private static final Logger logger = getLogger(Driver.class);

    public static String userDir, file_TestData;
    public static Map<String, String> dictionary = new HashMap<>();
    Launcher objLauncher = new Launcher();

    public static void main(String[] args){
        Driver ob = new Driver();
        ob.mainDriver("Chrome");
    }

    public void mainDriver(String testingBrowser){
        try {
            userDir = System.getProperty("user.dir");
            file_TestData = "MasterTestData.xlsx";
            objLauncher.InvokeLauncher(testingBrowser);
        } catch (Exception e) {
            logger.info("Exception in testcases.Driver.mainDriver is " + e);
        }
    }

}
