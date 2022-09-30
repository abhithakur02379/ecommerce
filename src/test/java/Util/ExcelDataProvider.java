package Util;

import org.testng.annotations.DataProvider;
import testcases.Driver;

import java.io.File;
import java.lang.reflect.Method;

public class ExcelDataProvider extends InitMethod {

    @DataProvider(name = "multiSheetExcelRead")
    public static Object[][] multiSheetExcelRead(Method method) throws Exception {
        File file = new File(Constants.Path_TestData + Driver.file_TestData, "TestData");
        String SheetName = method.getName();
        System.out.println(SheetName);
        return ExcelUtilsUpdated.getTableArray(file.getAbsolutePath(), SheetName);
    }

    @DataProvider(name = "excelSheetNameAsMethodName")
    public static Object[][] excelSheetNameAsMethodName(Method method) throws Exception {
        File file = new File("src/test/resources/Test Data/Excel Files/" + method.getName() + ".xlsx");
        System.out.println("Opening Excel File:" + file.getAbsolutePath());
        return ExcelUtilsUpdated.getTableArray(file.getAbsolutePath());
    }
}
