package com.NebulaDemo.validations;

import com.NebulaDemo.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;


//Soft assertions allow tests to continue running even if an assertion fails, collecting all assertion failures at the end of the test.
public class Validation extends BaseAssertion {
    private static final SoftAssert softAssert = new SoftAssert();
    private static boolean used = false;
    public Validation() {
        super();
    }
    public Validation(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void assertTrue(boolean condition, String message) {
        used = true; // Mark that an assertion has been made
        softAssert.assertTrue(condition, message);
    }

    @Override
    protected void assertFalse(boolean condition, String message) {
        used = true; // Mark that an assertion has been made
        softAssert.assertFalse(condition, message);
    }

    @Override
    protected void assertEquals(Object actual, Object expected, String message) {
        used = true; // Mark that an assertion has been made
        softAssert.assertEquals(actual, expected, message);
    }
public static void assertAll(ITestResult result) {
       if (!used)return;//if noassertions were made, no need to call assertAll
        try {
softAssert.assertAll();

       }catch (AssertionError e)
       {
           LogsManager.error("Assertion failed: " + e.getMessage());
           result.setStatus(ITestResult.FAILURE);
           result.setThrowable(e);
       }
finally {
              softAssert.assertAll();
       }
    }

}
