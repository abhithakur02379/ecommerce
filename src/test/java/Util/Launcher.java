package Util;

import org.slf4j.Logger;
import org.testng.Reporter;
import testcases.Driver;
import testcases.testFlow;

import static org.slf4j.LoggerFactory.getLogger;

public class Launcher extends HtmlReporter {

    private static final Logger logger = getLogger(Launcher.class);

    public void InvokeLauncher(String preferBrowser) {

        try {
            setUpReportFolder(preferBrowser);
            startResult(preferBrowser);
            int z1 = 1;
            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Driver");
            int tc_row = 1;
            String tc_id = "";
            String tc_name = "";
            String fn_name = "";
            while (z1 == 1) {
                setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Driver");
                String s_Flag = getCellData(tc_row, 3);
                if (s_Flag.equals("Yes")) {
                    tc_id = getCellData(tc_row, 0);
                    tc_name = getCellData(tc_row, 1);
                    fn_name = getCellData(tc_row, 2);

                    int k;
                    for (k = 1; k <= itr_size; k++) {
                        testCase_Itr = "" + k;
                        testCase_ID = tc_id;
                        testCase_Name = tc_name;

                        Driver.dictionary = getCurrentTestData(tc_id);
                        //********  Driver Setup **************
                        if (iteration_Flag.equals("Yes")) {
                            step_no = 0;
                            tc_no++;
                            //**********************************
                            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Driver");
                            String sTestIterationName = Driver.dictionary.get("TestCase_Name");
                            String sTestCaseName = tc_name + " :- " + Driver.dictionary.get("TestCase_Name");
                            startTestCase(sTestCaseName, "TC_Description: " + tc_name + " executed in " + preferBrowser, preferBrowser);
                            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
                            new testFlow(extentTest, extentReports).executeTC(fn_name, preferBrowser);
                            endTestCase();
                            Reporter.log("<h2>TC - " + tc_name + " (ID - " + tc_id + ") - Ends( Iteration - " + k + ")</h2>");
                        }
                        if (k == itr_size) {
                            data_row = 0;
                        } else {
                            data_row++;
                        }
                    }
                    tc_row++;
                } else if (getCellData(tc_row, 3).length() < 1) {
                    z1 = 0;
                } else {
                    tc_no++;
                }
            }
            endTestReport();
        } catch (Exception e) {
            logger.error("Error in InvokeLauncher is " + e);
        }
    }

}
