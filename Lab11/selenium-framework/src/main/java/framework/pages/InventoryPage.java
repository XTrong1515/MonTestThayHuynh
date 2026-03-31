package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class InventoryPage extends BasePage {

    @FindBy(css = ".inventory_list")
    private WebElement inventoryList;

    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(css = ".inventory_item button")
    private List<WebElement> addToCartButtons;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isElementVisible(By.cssSelector(".inventory_list"));
    }

    public InventoryPage addFirstItemToCart() {
        waitAndClick(addToCartButtons.get(0));
        return this;
    }

    public InventoryPage addItemByName(String name) {
        for (int i = 0; i < itemNames.size(); i++) {
            if (itemNames.get(i).getText().trim().equalsIgnoreCase(name)) {
                waitAndClick(addToCartButtons.get(i));
                break;
            }
        }
        return this;
    }

    public int getCartItemCount() {
        try {
            if (isElementVisible(By.cssSelector(".shopping_cart_badge"))) {
                return Integer.parseInt(cartBadge.getText().trim());
            }
        } catch (Exception e) {
            // Do nothing
        }
        return 0; // Badge không hiển thị = giỏ hàng rỗng
    }

    @FindBy(className = "shopping_cart_link")
    private WebElement shoppingCartLink;

    public CartPage goToCart() {
        waitAndClick(shoppingCartLink);
        return new CartPage(driver);
    }
}
