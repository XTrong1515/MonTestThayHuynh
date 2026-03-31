package framework.utils;

import framework.config.ConfigReader;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    public static String capture(WebDriver driver, String testName) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        
        String destination = ConfigReader.getInstance().getScreenshotPath() + testName + "_" + dateName + ".png";
        
        try {
            Path destPath = Paths.get(destination);
            Files.createDirectories(destPath.getParent());
            Files.copy(source.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            return destination;
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
            return null;
        }
    }

    public static byte[] captureAsBytes(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
