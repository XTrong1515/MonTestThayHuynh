package tests;

import framework.base.BaseTest;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.JsonReader;
import framework.utils.UserData;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class UserLoginTest extends BaseTest {

    @DataProvider(name = "jsonUsers")
    public Object[][] getUsersFromJson() throws IOException {
        List<UserData> users = JsonReader.readUsers("src/test/resources/testdata/users.json");
        return users.stream()
                .map(u -> new Object[]{u.username, u.password, u.expectSuccess, u.description})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "jsonUsers", groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Test Login cho JSON Users")
    public void testLoginFromJson(String username, String password, boolean expectSuccess, String description) {
        System.out.println("Running Test: " + description);
        LoginPage loginPage = new LoginPage(getDriver());
        if (expectSuccess) {
            InventoryPage inventory = loginPage.login(username, password);
            Assert.assertTrue(inventory.isLoaded(), "Đăng nhập hợp lệ nhưng không chuyển được trang Inventory");
        } else {
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.isErrorDisplayed(), "Đăng nhập sai thông tin không có lỗi trả về");
        }
    }
}
