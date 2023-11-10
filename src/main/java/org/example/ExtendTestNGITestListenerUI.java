package org.example;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtendTestNGITestListenerUI implements ITestListener {

    private static ExtentReports extent = new ExtentReports();
    private static ExtentTest test;

    public void onStart(ITestContext context) {
//        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("Report generated successfully");
        String reportName = "ExtentReportUI.html";
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/test-output/" + reportName);
        extent.attachReporter(htmlReporter);
    }

    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
    }

    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Case Passed: " + result.getMethod().getMethodName());
    }

    public void onTestFailure(ITestResult result) {
        test.log(Status.FAIL, "Test Case Failed: " + result.getMethod().getMethodName());
        test.log(Status.FAIL, "Error Message: " + result.getThrowable().getMessage());
    }

    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
