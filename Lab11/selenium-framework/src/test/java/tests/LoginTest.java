package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.ExcelReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Allure;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "excelSmokeData")
    public Object[][] getSmokeData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "SmokeCases");
    }

    @DataProvider(name = "excelNegativeData")
    public Object[][] getNegativeData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "NegativeCases");
    }

    @DataProvider(name = "excelBoundaryData")
    public Object[][] getBoundaryData() {
        String path = "src/test/resources/testdata/login_data.xlsx";
        return ExcelReader.getData(path, "BoundaryCases");
    }

    @Test(dataProvider = "excelSmokeData", groups = {"smoke", "regression"})
    @Feature("Đăng nhập")
    @Story("Đăng nhập hợp lệ")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Kiểm tra chức năng đăng nhập thành công với tài khoản hợp lệ")
    public void testLoginSuccess(String username, String password, String expectedUrl, String description) {
        Allure.step("Truy cập trang đăng nhập", () -> {
            System.out.println("Running Test: " + description);
        });
        
        LoginPage loginPage = new LoginPage(getDriver());
        Allure.step("Nhập tài khoản: " + username + " và mật khẩu", () -> {
            loginPage.enterUsername(username);
            loginPage.enterPassword(password);
        });
        
        Allure.step("Nhấn nút Login", () -> loginPage.clickLoginButton());
        
        InventoryPage inventoryPage = new InventoryPage(getDriver());
        Allure.step("Xác nhận đã vào trang Inventory", () -> {
            Assert.assertTrue(inventoryPage.isLoaded(), "Không thể chuyển qua trang Inventory");
            Assert.assertTrue(getDriver().getCurrentUrl().contains(expectedUrl), "URL chưa chính xác");
        });
    }

    @Test(dataProvider = "excelNegativeData", groups = {"regression"})
    @Feature("Đăng nhập")
    @Story("Đăng nhập thất bại (Negative)")
    @Severity(SeverityLevel.NORMAL)
    @Description("Kiểm tra thông báo lỗi khi đăng nhập không thành công")
    public void testLoginFailure(String username, String password, String expectedError, String description) {
        Allure.step("Truy cập và đăng nhập với tài khoản: " + username, () -> {
            System.out.println("Running Test: " + description);
        });

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(username, password);
        
        Allure.step("Xác nhận thông báo lỗi hiển thị: " + expectedError, () -> {
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Lỗi không được hiển thị");
            Assert.assertEquals(loginPage.getErrorMessage(), expectedError, "Nội dung lỗi không đúng");
        });
    }

    @Test(dataProvider = "excelBoundaryData", groups = {"regression"})
    @Feature("Đăng nhập")
    @Story("Trường hợp biên (Boundary)")
    @Severity(SeverityLevel.MINOR)
    public void testLoginBoundary(String username, String password, String expectedError, String description) {
        System.out.println("Running Test: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(username, password);
        Assert.assertTrue(loginPage.isErrorDisplayed());
    }
}
