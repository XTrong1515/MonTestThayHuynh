package tests;

import framework.base.BaseTest;
import framework.pages.LoginPage;
import framework.pages.InventoryPage;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * [DEMO] Test này cố ý dùng assertion SAI để minh chứng:
 *  - Log màu đỏ trên GitHub Actions
 *  - Screenshot tự động chụp khi fail (Allure artifact)
 */
public class DemoFailTest extends BaseTest {

    @Test(groups = {"demo-fail"})
    @Feature("Demo CI/CD")
    @Story("Test cố ý thất bại để kiểm tra pipeline")
    @Severity(SeverityLevel.MINOR)
    @Description("Test này dùng assertion SAI CỐ Ý: expect inventory URL nhưng sẽ luôn fail. " +
                 "Mục đích là chứng minh pipeline báo đỏ và tự động chụp screenshot.")
    public void testIntentionalFailForCIDemo() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = loginPage.login("standard_user", "secret_sauce");

        // ASSERTION SAI CỐ Ý: URL thật là /inventory.html nhưng ta assert là /wrong-page
        // => Test LUÔN FAIL => GitHub Actions báo đỏ => screenshot được đính kèm vào artifact
        Assert.assertTrue(
            getDriver().getCurrentUrl().contains("/wrong-page"),
            "[DEMO FAIL] Assertion này SAI CỐ Ý! URL thật: " + getDriver().getCurrentUrl()
        );
    }
}
