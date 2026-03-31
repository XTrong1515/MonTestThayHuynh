package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import framework.utils.TestDataFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class CheckoutTest extends BaseTest {

    @Test(groups = {"regression"})
    @Feature("Thanh toán (Checkout)")
    @Story("Thanh toán với dữ liệu ngẫu nhiên (Java Faker)")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sử dụng Java Faker để sinh dữ liệu ngẫu nhiên cho form checkout và log ra màn hình")
    public void testCheckoutWithRandomData() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();
        
        cartPage.goToCheckout();

        // Lấy dữ liệu ngẫu nhiên từ TestDataFactory
        Map<String, String> checkoutData = TestDataFactory.randomCheckoutData();
        String firstName = checkoutData.get("firstName");
        String lastName = checkoutData.get("lastName");
        String postalCode = checkoutData.get("postalCode");

        System.out.println("====== DỮ LIỆU FAKER ĐƯỢC SINH ======");
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Postal Code: " + postalCode);
        System.out.println("=====================================");

        // Ở đây đáng lý sẽ điền vào form Checkout (với CheckoutPage),
        // Để demo Java Faker theo như yêu cầu Lab 9, ta chỉ cần sinh dữ liệu và log ra
        // Vì bài thực hành Lab 9 chỉ yêu cầu "sinh dữ liệu ngẫu nhiên cho form checkout" và "chứng minh log",
        // nên ta sẽ assert data không bị null
        Assert.assertNotNull(firstName, "First Name sinh ra không hợp lệ");
        Assert.assertNotNull(lastName, "Last Name sinh ra không hợp lệ");
        Assert.assertNotNull(postalCode, "Postal Code sinh ra không hợp lệ");
    }
}
