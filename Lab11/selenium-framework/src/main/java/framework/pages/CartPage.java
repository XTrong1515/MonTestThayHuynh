package framework.pages;

import framework.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    @FindBy(css = ".cart_item")
    private List<WebElement> cartItems;

    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;

    @FindBy(css = ".cart_button")
    private List<WebElement> removeButtons;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getItemCount() {
        return isElementVisible(By.cssSelector(".cart_item")) ? cartItems.size() : 0;
    }

    public CartPage removeFirstItem() {
        if (!removeButtons.isEmpty()) {
            waitAndClick(removeButtons.get(0));
        }
        return this;
    }

    public List<String> getItemNames() {
        List<String> names = new ArrayList<>();
        if (isElementVisible(By.cssSelector(".inventory_item_name"))) {
            for (WebElement e : itemNames) {
                names.add(e.getText().trim());
            }
        }
        return names;
    }

    public void goToCheckout() {
        waitAndClick(checkoutButton);
    }
}
