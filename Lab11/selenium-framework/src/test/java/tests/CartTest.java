package tests;

import framework.base.BaseTest;
import framework.pages.CartPage;
import framework.pages.InventoryPage;
import framework.pages.LoginPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CartTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Feature("Giỏ hàng")
    @Story("Thêm sản phẩm vào giỏ")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Kiểm tra việc thêm sản phẩm đầu tiên vào giỏ hàng thành công")
    public void testAddFirstItemToCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        inventoryPage.addFirstItemToCart();
        Assert.assertEquals(inventoryPage.getCartItemCount(), 1, "Giỏ hàng không hiển thị đủ số lượng");

        CartPage cartPage = inventoryPage.goToCart();
        try {
            System.out.println("CART HTML:");
            System.out.println(getDriver().findElement(org.openqa.selenium.By.cssSelector(".cart_list")).getAttribute("innerHTML"));
        } catch(Exception e) {
            e.printStackTrace();
        }
        Assert.assertEquals(cartPage.getItemCount(), 1, "Sản phẩm không có trong giỏ");
    }

    @Test(groups = {"regression"})
    @Feature("Giỏ hàng")
    @Story("Xóa sản phẩm khỏi giỏ")
    @Severity(SeverityLevel.NORMAL)
    @Description("Kiểm tra việc xóa sản phẩm đã thêm khỏi giỏ hàng")
    public void testRemoveItemFromCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        inventoryPage.addFirstItemToCart();
        CartPage cartPage = inventoryPage.goToCart();

        cartPage.removeFirstItem();
        Assert.assertEquals(cartPage.getItemCount(), 0, "Giỏ hàng chưa được làm trống");
    }
}
