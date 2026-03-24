// File: src/test/java/tests/ApiUiIntegrationTest.java
package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class ApiUiIntegrationTest {

    private WebDriver driver;
    private String apiToken;
    private boolean isApiAlive;

    // PHẦN A & B: API Precondition Setup - Chạy trước mỗi test method [cite: 578]
    @BeforeMethod
    public void setupPrecondition() {
        // BƯỚC 1 (API Check): Gọi GET để xác nhận server backend của hệ thống không bị "sập" [cite: 590, 593]
        Response pingResponse = given().get("https://reqres.in/api/users");
        // Lưu cờ boolean xem server có trả về 200 OK không
        isApiAlive = (pingResponse.statusCode() == 200);

        // BƯỚC 2 (API Action): Lấy Token đăng nhập cực nhanh qua Backend API thay vì gõ UI [cite: 579, 593]
        Response loginResponse = given()
                .contentType("application/json")
                .body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}")
                .when()
                .post("https://reqres.in/api/login");

        // Nếu API lỗi (khác 200), chúng ta ném ra ngoại lệ SkipException để BỎ QUA test UI (Skip), không lãng phí tài nguyên chạy Selenium [cite: 580, 581]
        if (loginResponse.statusCode() != 200) {
            throw new SkipException("API Login gặp lỗi, không thể lấy Token. Bỏ qua chạy giao diện UI!");
        }

        // Bóc tách token từ JSON và lưu lại
        apiToken = loginResponse.jsonPath().getString("token");
        System.out.println("Đã chuẩn bị Token API: " + apiToken); // Log xác nhận token [cite: 579]

        // BƯỚC 3 (UI Setup): Khởi tạo Trình duyệt web tự động hóa (Selenium) [cite: 386]
        // (Lưu ý: Bạn cần có cấu hình ChromeDriver trong pom.xml hoặc cài WebDriverManager trên máy để chạy)
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    // Luồng test chính [cite: 588]
    @Test(description = "Test luồng UI: Đăng nhập và Thêm Giỏ Hàng bằng API Precondition")
    public void testCompleteUiFlowWithApi() {
        // KIỂM TRA ĐIỀU KIỆN: Nếu biến isApiAlive = false thì Stop ngay lập tức bằng lệnh skip [cite: 591]
        if (!isApiAlive) {
            throw new SkipException("Backend đang chết (isApiAlive = false). Hủy chạy bài test UI!");
        }

        // BƯỚC 4 (UI Action): Điều hướng ban đầu tới trang chủ để set local storage
        driver.get("https://www.saucedemo.com/");

        // --- ĐÂY LÀ KỸ THUẬT QUAN TRỌNG NHẤT --- 
        // Thay vì dùng driver.findElement().sendKeys() để gõ user/pass, ta dùng Javascript tiêm thẳng session vào máy chủ! [cite: 375, 388, 389, 390]
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.setItem('session-username', 'standard_user')");

        // BƯỚC 5 (UI Action): Bỏ qua trang login, bắt browser chạy thẳng vào trang kiểm kho (Inventory) [cite: 391, 392]
        driver.get("https://www.saucedemo.com/inventory.html");

        // BƯỚC 6 (Assertion): Xác minh (Verify) cấu trúc URL có chứa cụm 'inventory' và title đúng 'Swag Labs' [cite: 584, 593]
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Lỗi: Không bypass được qua màn hình Login");
        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Sai tiêu đề trang web");

        // BƯỚC 7 (UI Action): Thực hiện nghiệp vụ - Nhấn nút "Add to cart" cho 2 sản phẩm đầu tiên [cite: 592]
        // Dùng CSS selector gốc của trang sauce demo
        driver.findElement(org.openqa.selenium.By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']")).click();
        driver.findElement(org.openqa.selenium.By.cssSelector("[data-test='add-to-cart-sauce-labs-bike-light']")).click();

        // BƯỚC 8 (Assertion): Kiểm tra badge giỏ hàng góc trên bên phải xem có hiển thị số "2" không [cite: 592, 593]
        String badgeText = driver.findElement(org.openqa.selenium.By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(badgeText, "2", "Lỗi: Giỏ hàng không hiển thị đúng 2 sản phẩm");
    }

    // Đóng dọn dẹp browser sau khi test xong
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}