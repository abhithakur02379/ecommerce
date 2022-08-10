package Util;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import testcases.Driver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class ExcelUtils {

    private static final Logger logger = getLogger(ExcelUtils.class);

    static XSSFSheet ExcelWSheet;
    private static XSSFRow Row;

    public static int curr_row = 0;
    public static int data_row = 0;
    public static int itr_size = 1;

    public static String iteration_Flag = "";
    public String resultExcelPath = "C:\\result.xlsx";

    public void setExcelFile(String Path, String SheetName) {

        try {
            //Open Excel File
            FileInputStream ExcelFile = new FileInputStream(Path);
            //Access desired test data sheet
            XSSFWorkbook excelWBook = new XSSFWorkbook(ExcelFile);
            ExcelWSheet = excelWBook.getSheet(SheetName);

        } catch (Exception e) {
            logger.error("Unable to find sheet " + e);
        }
    }

    public String getCellData(int rowNum, int colNum) {

        try {
            XSSFCell cell = ExcelWSheet.getRow(rowNum).getCell(colNum);
            return cell.getStringCellValue();
        } catch (Exception e) {
            return "";
        }
    }

    public Map<String, String> getCurrentTestData(String tc_id) {

        try {
            if (data_row == 0) {
                setExcelFile(Constants.Path_TestData + Driver.file_TestData, "TestData");

                int j = 1;
                int i = 0;

                while (j == 1) {
                    if (getCellData(i, 0).equals(tc_id)) {
                        curr_row = i;
                        int s_size = 0;
                        int k = 1;
                        int z = i + 1;

                        while (k == 1) {
                            int x_size = getCellData(z, 0).length();
                            if (x_size != 0) {
                                itr_size = s_size;
                                j = 0;
                                k = 0;
                            }
                            s_size++;
                            z++;
                        }
                    }
                    i++;
                }
                data_row = curr_row + 1;
            }

            Map<String, String> dictionary = new HashMap<>();
            setExcelFile(Constants.Path_TestData + Driver.file_TestData, "TestData");

            int k = 1;
            int i = 1;
            while (k == 1) {
                if (i == 2) {
                    int itr_flag = getCellData(curr_row, i).length();
                    String iterationFlagText = getCellData(curr_row, i);
                    if (itr_flag != 0 && iterationFlagText.equalsIgnoreCase("iterationFlag")) {
                        iteration_Flag = getCellData(data_row, i);
                    } else {
                        iteration_Flag = "Yes";
                    }
                    String key = getCellData(curr_row, i);
                    String value = "" + getCellData(data_row, i);
                    dictionary.put(key, value);
                    i++;
                } else {
                    int x_size = getCellData(curr_row, i).length();
                    if (x_size != 0) {
                        String key = getCellData(curr_row, i);
                        String value = "" + getCellData(data_row, i);
                        dictionary.put(key, value);
                        i++;
                    } else {
                        k = 0;
                    }
                }
            }
            return dictionary;
        } catch (Exception e) {
            logger.error("Error while reading file - " + e);
            return null;
        }
    }


}

