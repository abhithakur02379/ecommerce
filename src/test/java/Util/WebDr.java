package Util;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import testcases.Driver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class WebDr extends HtmlReporter {

    private static final Logger logger = getLogger(WebDr.class);

    public static Map<String, String> page_Objects = new HashMap<>();

    public WebDriver webDriver;
    public String URL;

    public static String genPassportID(int count) {
        String passportID = RandomStringUtils.randomAlphanumeric(count);
        return passportID;
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(String preferBrowser) {
        try {
            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "Configuration");
            URL = getCellData(5, 1);
            launchApplication(URL, preferBrowser);
        } catch (Exception e) {
            logger.error("Error in setup is " + e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        webDriver.manage().deleteAllCookies();
        webDriver.close();
        webDriver.quit();
    }

    public void launchApplication(String URL, String preferBrowser) {
        try {
            DesiredCapabilities capabilities;
            ChromeOptions options = new ChromeOptions();
            switch (preferBrowser) {
                case "Chrome":
                    HashMap<String, Object> chromePrefs = new HashMap<>();
                    chromePrefs.put("profile.default_content_settings.popups", 0);
                    chromePrefs.put("download.default_directory", Constants.Download);
                    options.setExperimentalOption("prefs", chromePrefs);
                    options.addArguments("start-maximized");
                    DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
                    desiredCapabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
                    desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
                    webDriver = WebDriverManager.chromedriver().capabilities(desiredCapabilities).create();
                    webDriver.manage().deleteAllCookies();
                    break;
                case "HeadLess":
                    System.setProperty("webdriver.chrome.driver", Constants.Chrome_Driver);
                    options = new ChromeOptions();
                    options.addArguments("--headless");
                    options.addArguments("window-size=1600,900");
                    break;
                case "Firefox":
                    System.setProperty("webdriver.geko.driver", Constants.Firefox_Driver);
                    desiredCapabilities = DesiredCapabilities.firefox();
                    desiredCapabilities.setBrowserName("firefox");
                    webDriver = new FirefoxDriver(desiredCapabilities);
                    break;
                case "Firefox_Headless":
                    System.setProperty("webdriver.geko.driver", Constants.Firefox_Driver);
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.setHeadless(true);
                    webDriver = new FirefoxDriver(firefoxOptions);
                    break;
                default:
                    break;
            }

            if (preferBrowser.equals("Mobile")) {
//                code for mobile
            } else {
                webDriver.get(URL);
                webDriver.manage().window().maximize();
                webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                webDriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.error("Failed to create Webdriver object for " + preferBrowser);
            throw e;
        }
    }

    public String getValue(String fieldName) {
        try {
            return Driver.dictionary.get(fieldName).trim();
        } catch (Exception e) {
            logger.error("Error in WebDr.getValue is : " + e);
            throw e;
        }
    }

    public WebElement getElement(String str) {
        try {
            String desc = page_Objects.get(str);
            String[] a = desc.split("\\|", 2);

            switch (a[0]) {
                case "ID":
                    return this.webDriver.findElement(By.id(a[1]));
                case "CLASSNAME":
                    return this.webDriver.findElement(By.className(a[1]));
                case "LINKTEXT":
                    return this.webDriver.findElement(By.linkText(a[1]));
                case "PARTIALLINKTEXT":
                    return this.webDriver.findElement(By.partialLinkText(a[1]));
                case "NAME":
                    return this.webDriver.findElement(By.name(a[1]));
                case "TAGNAME":
                    return this.webDriver.findElement(By.tagName(a[1]));
                case "XPATH":
                    return this.webDriver.findElement(By.xpath(a[1]));
                default:
                    logger.info("Method getElement cannot return object for " + str);
                    break;
            }
        } catch (Exception e) {
            logger.error("Method getElement cannot return object for " + str);
        }
        return null;
    }

    public List<WebElement> getElements(String str) {
        try {
            String desc = page_Objects.get(str);
            String[] a = desc.split("\\|", 2);
            switch (a[0]) {
                case "ID":
                    return this.webDriver.findElements(By.id(a[1]));
                case "CLASSNAME":
                    return this.webDriver.findElements(By.className(a[1]));
                case "LINKTEXT":
                    return this.webDriver.findElements(By.linkText(a[1]));
                case "PARTIALLINKTEXT":
                    return this.webDriver.findElements(By.partialLinkText(a[1]));
                case "NAME":
                    return this.webDriver.findElements(By.name(a[1]));
                case "TAGNAME":
                    return this.webDriver.findElements(By.tagName(a[1]));
                case "XPATH":
                    return this.webDriver.findElements(By.xpath(a[1]));
                default:
                    logger.info("Method getElements cannot return object for " + str);
                    break;
            }
        } catch (Exception e) {
            logger.error("Method getElements cannot return object for " + str);
        }
        return null;
    }

    public boolean exists(String elementName, boolean expected, String description) {
        boolean state = false;
        try {
            WebElement element = getElement(elementName);
            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            wait.until(ExpectedConditions.visibilityOf(element));

            if (element.isDisplayed() && expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Exists", "PASS");
                state = true;
            } else if (element.isDisplayed() && !expected) {
                WriteStep(webDriver, description, elementName + ": Should NOT Exists", elementName + ": Exists", "FAIL");
            } else if (!element.isDisplayed() && expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Does NOT Exists", "FAIL");
            } else if (!element.isDisplayed() && !expected) {
                WriteStep(webDriver, description, elementName + ": Does NOT Exists", elementName + ": Does NOT Exists", "PASS");
                state = true;
            }
        } catch (Exception e) {
            if (expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Does NOT Exists", "FAIL");
            } else {
                WriteStep(webDriver, description, elementName + ": Does NOT Exists", elementName + ": Does NOT Exists", "PASS");
                state = true;
            }
        }
        return state;
    }

    public boolean existsNoReport(String elementName, boolean expected, String description) {
        boolean state = false;
        try {
            WebElement element = getElement(elementName);
            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            wait.until(ExpectedConditions.visibilityOf(element));

            if (element.isDisplayed() && expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Exists", "PASS");
                state = true;
            } else if (element.isDisplayed() && !expected) {
                WriteStep(webDriver, description, elementName + ": Should NOT Exists", elementName + ": Exists", "FAIL");
            } else if (!element.isDisplayed() && expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Does NOT Exists", "FAIL");
            } else if (!element.isDisplayed() && !expected) {
                WriteStep(webDriver, description, elementName + ": Does NOT Exists", elementName + ": Does NOT Exists", "PASS");
                state = true;
            }
        } catch (Exception e) {
            if (expected) {
                WriteStep(webDriver, description, elementName + ": Exists", elementName + ": Does NOT Exists", "FAIL");
            } else {
                WriteStep(webDriver, description, elementName + ": Does NOT Exists", elementName + ": Does NOT Exists", "PASS");
            }
        }
        return state;
    }

    public boolean enabled(String elementName, boolean expected, String description) {
        WebElement element = getElement(elementName);
        boolean state = false;
        try {
            if (element.isEnabled() && expected) {
                WriteStep(webDriver, description, elementName + ": Should be Enabled", elementName + ": is Enabled", "PASS");
                state = true;
            } else if (element.isEnabled() && !expected) {
                WriteStep(webDriver, description, elementName + ": Should NOT be Enabled", elementName + ": is Enabled", "FAIL");
            } else if (!element.isEnabled() && expected) {
                WriteStep(webDriver, description, elementName + ": Should be Enabled", elementName + ": is NOT Enabled", "FAIL");
            } else if (!element.isEnabled() && !expected) {
                WriteStep(webDriver, description, elementName + ": Should NOT be Enabled", elementName + ": is NOT Enabled", "PASS");
                state = true;
            }
        } catch (Exception e) {
            if (expected) {
                WriteStep(webDriver, description, elementName + ": Should be Enabled", elementName + ": is NOT Enabled", "FAIL");
            } else {
                WriteStep(webDriver, description, elementName + ": Should NOT be Enabled", elementName + ": is NOT Enabled", "PASS");
                state = true;
            }
        }
        return state;
    }

    public void setText(String elementName, String textToSet, String description) {
        try {
            if (textToSet != null) {
                WebElement element = getElement(elementName);
                WebDriverWait wait = new WebDriverWait(webDriver, 60);
                wait.until(ExpectedConditions.visibilityOf(element));
                if (textToSet.equals("BLANK")) {
                    textToSet = "";
                }
                element.clear();
                element.sendKeys(textToSet);
                WriteStep(webDriver, description, elementName + ": Enter Text", "Entered text : " + textToSet, "PASS");
            }
        } catch (Exception e) {
            logger.info("Exception in WebDr.setText is : " + e);
            WriteStep(webDriver, "Object NOT Visible - " + description, "Enter Text", "Not Entered : " + textToSet, "FAIL");
        }
    }

    public void click(String elementName, String description) {
        try {
            WebElement element = getElement(elementName);
            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            wait.until(ExpectedConditions.visibilityOf(element));
            if (element.isDisplayed()) {
                Actions actions = new Actions(webDriver);
                actions.moveToElement(element).click().build().perform();
                Thread.sleep(1000);
                WriteStep(webDriver, description, "Click " + elementName, elementName + "Clicked", "PASS");
            } else {
                WriteStep(webDriver, "Object NOT Visible - " + elementName, "Click " + elementName, elementName + " : Not Clicked", "FAIL");
            }
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + elementName, "Click " + elementName, elementName + " : Not Clicked", "FAIL");
            logger.error("Exception is WebDr.click is : " + e);
        }
    }

    public void selectValueFromDropdown(String elementName, String selectBy, String value, String description) {
        try {
            String desc = page_Objects.get(elementName);
            String xPath;
            if (elementName.contains("drpDwnList")) {
                String[] a = desc.split("\\|", 2);
                xPath = a[1] + "";
            } else {
                String[] a = desc.split("\\|");
                xPath = a[1] + "";
            }
            webDriver.findElement(By.xpath(xPath)).click();
            Thread.sleep(1000);
            WebElement element = getElement(elementName);
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
            wait.until(ExpectedConditions.visibilityOf(element));
            if (element.isDisplayed() && element.isEnabled()) {
                Select select = new Select(element);
                switch (selectBy) {
                    case "value":
                        select.selectByValue(value);
                        break;
                    case "index":
                        select.selectByIndex(Integer.parseInt(value));
                        break;
                    case "text":
                        select.selectByVisibleText(value);
                        break;
                }
                WriteStep(webDriver, description, "Select " + value, "Selected" + value, "PASS");
            } else {
                WriteStep(webDriver, "Object NOT Visible - " + description, "Select " + value, "Unable to Select " + value, "FAIL");
            }
        } catch (InterruptedException e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Select " + value, "Unable to Select " + value, "FAIL");
            logger.error("Exception is WebDr.selectValueFromDropdown is : " + e);
        }
    }

    public void selectValueFromList(String elementName, String value, String description) {
        try {
            String desc = page_Objects.get(elementName);
            String xPath;
            String[] a = desc.split("\\|", 2);
            xPath = a[1];
            WebElement element = getElement(elementName);
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
            wait.until(ExpectedConditions.visibilityOf(element));
            if (element.isDisplayed() && element.isEnabled()) {
                Select select = new Select(element);
                List<WebElement> webElements = webDriver.findElements(By.xpath(xPath));
                for (WebElement webElement : webElements) {
                    String sValue = webElement.getText();
                    if (sValue.equalsIgnoreCase(value)) {
                        webElement.click();
                        Thread.sleep(1000);
                        WriteStep(webDriver, description, "Select : " + value, "Selected : " + value, "PASS");
                    }
                }
            } else {
                WriteStep(webDriver, "Object NOT Visible - " + description, "Select " + value, "Unable to Select " + value, "FAIL");
            }
        } catch (InterruptedException e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Select " + value, "Unable to Select " + value, "FAIL");
            logger.error("Exception is WebDr.selectValueFromDropdown is : " + e);
        }
    }

    public String selectRadioGroup(String elementName, String textToSelect, String description) {
        String sAllList = "", sActualData = "";

        try {
            boolean boolSelected = false;
            List<WebElement> webElements = getElements(elementName);
            List<WebElement> radioGroup = webElements;
            for (int irgNum = 0; irgNum < radioGroup.size(); irgNum++) {
                sActualData = radioGroup.get(irgNum).getText().trim();
                if (sActualData.equals("")) {
                    sActualData = radioGroup.get(irgNum).getAttribute("value");
                }
                //            if (sActualData.contains("-")){
                //                sActualData = sActualData.split("-")[0];
                //            }
                if (sActualData.contains(textToSelect.trim())) {
                    radioGroup.get(irgNum).click();
                    boolSelected = true;
                    sAllList = sAllList + " , " + sActualData;
                    WriteStep(webDriver, description, "Select Radio Button : " + textToSelect, "Selected " + textToSelect, "PASS");
                    break;
                }
            }
            if (!boolSelected) {
                WriteStep(webDriver, description, textToSelect + " should be in List", textToSelect + " is not in List : " + sAllList, "FAIL");
            }
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Select Radio Button : " + textToSelect, "Unable to Select Radio button : " + textToSelect, "FAIL");
            logger.error("Exception is WebDr.selectRadioGroup is : " + e);
        }
        return sActualData;
    }

    public void dblClick(String elementName, String description) {
        try {
            Actions actions = new Actions(webDriver);
            WebElement element = getElement(elementName);
            actions.doubleClick(element).build().perform();
            WriteStep(webDriver, description, "Double Click : " + elementName, "Double Clicked :  " + elementName, "PASS");
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Double Click : " + elementName, elementName + " NOT Clicked", "FAIL");
            logger.error("Exception is WebDr.dblClick is : " + e);
        }
    }

    public void rightClick(String elementName, String description) {
        try {
            Actions actions = new Actions(webDriver);
            WebElement element = getElement(elementName);
            actions.contextClick(element).build().perform();
            WriteStep(webDriver, description, "Right Click : " + elementName, "Right Clicked :  " + elementName, "PASS");
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Right Click : " + elementName, elementName + " NOT Right Clicked", "FAIL");
            logger.error("Exception is WebDr.rightClick is : " + e);
        }
    }

    public void mouseOver(String elementName, String description) {
        try {
            Actions actions = new Actions(webDriver);
            WebElement element = getElement(elementName);
            actions.moveToElement(element).build().perform();
            WriteStep(webDriver, description, "Mouse Over on " + elementName, "Mouse Overed on " + elementName, "PASS");
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Mouse Over on " + elementName, "Mouse NOT Overed on " + elementName, "FAIL");
            logger.error("Exception is WebDr.mouseOver is : " + e);
        }
    }

    public void ControlKeyStroke(String elementName, String keyToEnter, String description) {
        try {
            Actions actions = new Actions(webDriver);
            WebElement element = getElement(elementName);
            actions.sendKeys(Keys.chord(Keys.CONTROL, keyToEnter)).build().perform();
            WriteStep(webDriver, description, "Key Stroke", "Key Stroke Entered", "PASS");
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Key Stroke", "Key Stroke NOT Entered", "FAIL");
            logger.error("Exception is WebDr.ControlKeyStroke is : " + e);
        }
    }

    public void setPwd(String elementName, String sPassword, String description) {
        try {
            WebElement element = getElement(elementName);
            element.clear();
            String encryptedPwd = Base64.getEncoder().encodeToString(sPassword.getBytes());
            element.sendKeys(encryptedPwd);
            WriteStep(webDriver, description, "Enter Password", "Password Entered : " + encryptedPwd, "PASS");
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT Visible - " + description, "Enter Text", "NOT Entered : " + sPassword, "FAIL");
            logger.error("Exception is WebDr.setPwd is : " + e);
        }
    }

    public void verifyText(String elementName, boolean matchSubString, String expectedText, String description) {
        try {
            WebElement element = getElement(elementName);
            if (element.isDisplayed()) {
                String actualText = element.getText();
                if (matchSubString) {
                    if (actualText.contains(expectedText)) {
                        WriteStep(webDriver, description, "Verify Text - " + expectedText, "Verified - " + actualText, "PASS");
                    } else {
                        WriteStep(webDriver, description, "Verify Text - " + expectedText, "Verification Failed - " + actualText, "FAIL");
                    }
                } else {
                    if (actualText.equals(expectedText)) {
                        WriteStep(webDriver, description, "Verify Text - " + expectedText, "Verified - " + actualText, "PASS");
                    } else {
                        WriteStep(webDriver, description, "Verify Text - " + expectedText, "Verification Failed - " + actualText, "FAIL");
                    }
                }
            } else {
                WriteStep(webDriver, "Object NOT visible " + description, "Verify Text - " + expectedText, "NOT Verified", "FAIL");
            }
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT visible " + description, "Verify Text - " + expectedText, "NOT Verified", "FAIL");
            logger.error("Exception is WebDr.verifyText is : " + e);
        }
    }

    public String getText(String elementName, String description) {
        String textValue = "";
        try {
            if (textValue != null) {
                WebElement element = getElement(elementName);
                if (element.isDisplayed()) {
                    textValue = element.getText();
                } else {
                    WriteStep(webDriver, "Object NOT visible " + description, "Get value for - " + description, "Object NOT visible", "FAIL");
                }
            }
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT visible " + description, "Get value for - " + description, "Object NOT visible", "FAIL");
            logger.error("Exception is WebDr.getText is : " + e);
        }
        return textValue;
    }

    public void verifyAttribute(String elementName, boolean matchSubString, String attribute, String expectedText, String description) {
        try {
            WebElement element = getElement(elementName);
            if (element.isDisplayed()) {
                String actualText = element.getAttribute(attribute);
                if (matchSubString) {
                    if (actualText.contains(expectedText)) {
                        WriteStep(webDriver, description, "Verify Attribute - " + expectedText, "Verified - " + actualText, "PASS");
                    } else {
                        WriteStep(webDriver, description, "Verify Attribute - " + expectedText, "Verification Failed - " + actualText, "FAIL");
                    }
                } else {
                    if (actualText.equals(expectedText)) {
                        WriteStep(webDriver, description, "Verify Attribute - " + expectedText, "Verified - " + actualText, "PASS");
                    } else {
                        WriteStep(webDriver, description, "Verify Attribute - " + expectedText, "Verification Failed - " + actualText, "FAIL");
                    }
                }
            } else {
                WriteStep(webDriver, "Object NOT visible " + description, "Verify Attribute - " + expectedText, "NOT Verified", "FAIL");
            }
        } catch (Exception e) {
            WriteStep(webDriver, "Object NOT visible " + description, "Verify Attribute - " + expectedText, "NOT Verified", "FAIL");
            logger.error("Exception is WebDr.verifyAttribute is : " + e);
        }
    }

    public String GenRSAID(int iage, String sGender) {
        //Get DOB from age
        String DOB = GETDOB(iage);

        //Get random number based on gender
        int genNum;
        if (sGender.equals("Female")) {
            genNum = ThreadLocalRandom.current().nextInt(1000, 4999);
        } else {
            genNum = ThreadLocalRandom.current().nextInt(5000, 9999);
        }

        int iCitizenship = 0;
        int iA = 8;

        String temp = DOB + genNum + iCitizenship + iA;
        String RSAID = temp + generateLuhnDigit(temp);
        return RSAID;
    }

    public int generateLuhnDigit(String input) {
        int total = 0;
        int count = 0;
        int multiple = 0;

        for (int i = 0; i < input.length(); i++) {
            multiple = (count % 2) + 1;
            count++;
            int temp = multiple * Integer.parseInt(String.valueOf(input.charAt(i)));
            temp = (int) Math.floor(temp / 10) + (temp % 10);
            total += temp;
        }
        total = (total * 9) % 10;
        return total;
    }

    public String GETDOB(int number) {
        LocalDate now = LocalDate.now();
        LocalDate dob = now.minusYears(number);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        String date = dob.format(formatter);
        return date;
    }


}












