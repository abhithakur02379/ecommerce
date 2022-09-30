package Util;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import testcases.Driver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static Util.Constants.AlertCheck;
import static org.slf4j.LoggerFactory.getLogger;


public class HtmlReporter extends ExcelUtils{

    private static final Logger logger = getLogger(HtmlReporter.class);

    public static String htmlReport, captureScreen, appEnv, appCycle, appUrl, executionType, ScreenFolder;
    public String testCase_Itr, testCase_ID, testCase_Name, dirPath, screenDirPath;
    public int IterationCount, step_no, tc_no;
    public ExtentReports extentReports;
    public ExtentTest extentTest;

    public ExtentTest node;

    public void setUpReportFolder(String preferBrowser){

        try {
            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
            htmlReport = getCellData(1, 1);
            captureScreen = getCellData(2, 1);
            appEnv = getCellData(3, 1);
            appCycle = getCellData(4, 1);
            appUrl = getCellData(5, 1);
            executionType = getCellData(6, 1);
            tc_no = 0;

            DateFormat dataFormat = new SimpleDateFormat("dd-MM-yyy HH-mm-ss");
            Date date = new Date();
            String NowDateTime = dataFormat.format(date);
            dirPath = Driver.userDir + "\\TestResults\\" + "Result_" + preferBrowser + "_" + NowDateTime;
            File file = new File(dirPath);
        } catch (Exception e) {
            logger.error("Error in creating report folder " + e);
        }
    }

    public void startResult(String browser){

        try {
            extentReports = new ExtentReports(dirPath + "\\Result_" + browser + ".html", false);
            extentReports.addSystemInfo("Browser", browser);

            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
            String sURL = getCellData(5,1);
            if (!(sURL.equalsIgnoreCase(""))) {
                extentReports.addSystemInfo("URL", sURL);
                extentReports.addSystemInfo("Env", appEnv);
//                extentReports.addSystemInfo("Author", "Abhishek Singh");
//                extentReports.addSystemInfo("Browser", Browser);
//                extentReports.addSystemInfo("OS", OSName);
//                extentReports.addSystemInfo("OS Version", OSVersion);
//                extentReports.addSystemInfo("OS Architecture", OSArchitecture);
//                extentReports.addSystemInfo("OS Bit", OSBit);
//                extentReports.addSystemInfo("JAVA Version", System.getProperty("java.version"));
            }
            extentReports.loadConfig(new File(Constants.Path_Plugin + "\\extent-config.xml"));
        } catch (Exception e) {
            logger.error("Error in HtmlReporter.startResult is " + e);
        }
    }


    public void startTestCase(String testCaseName, String testDescription, String browser){
        ScreenFolder = testCase_ID + "-" + testCase_Name + "-" + testCase_Itr;
        screenDirPath = dirPath + "\\Screenshots\\" + ScreenFolder;
        System.setProperty("ScreenshotPath_" + browser, screenDirPath);

        try {
            Files.createDirectories(Paths.get(screenDirPath));
            String sScenario = testCaseName.split(":-")[0];
            extentTest = extentReports.startTest(testCaseName, testDescription);
            extentTest.assignCategory(sScenario);
        } catch (IOException e) {
            logger.error("Error in HtmlReporter.startTestCase is " + e);
        }
    }


    public HtmlReporter WriteStep(WebDriver driver, String description, String expected, String actual, String status){

        String snapPath = null;

        try {
            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
            String screenShotCapture = getCellData(2,1);

            switch (screenShotCapture){
                case "On Every Step" :
                    Thread.sleep(500);
                    snapPath = takeSnap(driver, description);
                    break;
                case "On Error" :
                    if (status.equalsIgnoreCase("FAIL")) {
                        snapPath = takeSnap(driver, description);
                    }
                    break;
                case "Never" :
                    snapPath = "";
                    break;
            }

            String ScreenShotPath = "./Screenshots/" + ScreenFolder + "\\" + snapPath + ".png";
            assert snapPath != null;
            if (snapPath.equals("")){
                if (status.equalsIgnoreCase("PASS")){
                    extentTest.log(LogStatus.PASS, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual);
                } else if (status.equalsIgnoreCase("FAIL")){
                    extentTest.log(LogStatus.FAIL, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual);
                } else if (status.equalsIgnoreCase("FATAL")){
                    extentTest.log(LogStatus.FATAL, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual);
                }
            } else {
                if (status.equalsIgnoreCase("PASS")){
                    extentTest.log(LogStatus.PASS, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual + extentTest.addScreenCapture(ScreenShotPath));
                } else if (status.equalsIgnoreCase("FAIL")){
                    extentTest.log(LogStatus.FAIL, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual + extentTest.addScreenCapture(ScreenShotPath));
                } else if (status.equalsIgnoreCase("FATAL")){
                    extentTest.log(LogStatus.FATAL, "Desc: " + description + " -||- Expected: " + expected + " -||- Actual: " + actual + extentTest.addScreenCapture(ScreenShotPath));
                }
            }
            System.out.printf("Desc : " + description + " || Expected: " + expected + " || Actual: " + actual + " || Status: " + status);
        } catch (Exception e) {
            logger.error("Error in HtmlReporter.WriteStep is " + e);
        }
        return this;
    }


    public String takeSnap(WebDriver driver, String description){
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        String nowDateTime = dateFormat.format(date);

        String strGetScreenShotPath = "";
        String imageFilePath = "";

        try {
            String browserName = driver.toString();
            if (browserName.contains("Chrome")){
                strGetScreenShotPath = System.getProperty("ScreenshotPath_Chrome");
            } else if (browserName.contains("Firefox")) {
                strGetScreenShotPath = System.getProperty("ScreenShotPath_Firefox");
            } else if (browserName.contains("Edge")) {
                strGetScreenShotPath = System.getProperty("ScreenShotPath_Edge");
            }

            imageFilePath = strGetScreenShotPath + "\\" + description.trim() + "_" + nowDateTime + ".png";
            AlertCheck = isAlertPresent(driver);
            if (AlertCheck){
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                ImageIO.write(image, "png", new File(imageFilePath));
            } else {
                File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(imageFilePath));
            }
        } catch (AWTException | IOException e) {
            logger.error("Error in HtmlReporter.takeSnap is " + e);
        }
        return description.trim() + "_" + nowDateTime;
    }


    public boolean isAlertPresent(WebDriver driver){
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void endTestCase(){
        extentReports.endTest(extentTest);
    }

    public void endTestReport(){
        extentReports.flush();
    }






}
