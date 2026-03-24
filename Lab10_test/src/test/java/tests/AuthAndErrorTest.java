// File: src/test/java/tests/AuthAndErrorTest.java
package tests;

import base.ApiBaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AuthAndErrorTest extends ApiBaseTest {

    // Phần A: Test Đăng ký (Register) - Authorization cơ bản [cite: 509, 517, 518]
    @Test(description = "Test register thành công trả về id và token")
    public void testRegisterSuccess() {
        // Tạo payload JSON chứa đủ email và password hợp lệ
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");

        given()
                .spec(requestSpec) // Gọi Base Spec
                .body(body) // Đính kèm payload
                .when()
                .post("/api/register") // Endpoint đăng ký
                .then()
                .spec(responseSpec)
                .statusCode(200) // Đăng ký thành công trả mã 200
                .body("id", notNullValue()) // Bắt buộc phải sinh ra ID user
                .body("token", not(emptyString())); // Bắt buộc sinh ra chuỗi Token xác thực
    }

    // Phần B: Sử dụng DataProvider thiết lập kịch bản bắt lỗi (Error Handling) [cite: 523]
    @DataProvider(name = "loginScenarios")
    public Object[][] loginScenarios() {
        return new Object[][] {
                // Cấu trúc mảng: { Email, Password, Mã HTTP kỳ vọng, Câu báo lỗi kỳ vọng }
                {"eve.holt@reqres.in", "cityslicka", 200, null}, // Case 1: Đúng tất cả [cite: 528]
                {"eve.holt@reqres.in", "", 400, "Missing password"}, // Case 2: Để trống password [cite: 531]
                {"", "cityslicka", 400, "Missing email or username"}, // Case 3: Để trống email [cite: 535]
                {"notexist@reqres.in", "wrongpass", 400, "user not found"}, // Case 4: Sai thông tin hoàn toàn [cite: 538]
                {"invalid-email", "pass123", 400, "user not found"} // Case 5: Sai định dạng email [cite: 542]
        };
    }

    // Truyền DataProvider vào test method [cite: 548]
    @Test(dataProvider = "loginScenarios", description = "Test Data-Driven nhiều kịch bản login")
    public void testLoginScenarios(String email, String password, int expectedStatus, String expectedError) {
        // Khởi tạo Map động để chứa body
        Map<String, String> body = new HashMap<>();
        // Đẩy email vào body (dù trống vẫn đẩy để test validation) [cite: 554]
        body.put("email", email);
        // Chỉ đẩy trường password vào nếu nó không bị trống (mô phỏng người dùng quên nhập field) [cite: 555]
        if (!password.isEmpty()) {
            body.put("password", password);
        }

        // Thực thi gọi API
        var response = given()
                .spec(requestSpec) // Áp dụng cấu hình mặc định
                .body(body) // Gửi data từ DataProvider
                .when()
                .post("/api/login") // Endpoint đăng nhập
                .then()
                .spec(responseSpec)
                .statusCode(expectedStatus); // Kiểm tra mã HTTP trả về xem có khớp với bảng dự kiến không [cite: 560]

        // Logic kiểm tra báo lỗi: Nếu expectedError != null (tức là case thất bại), thì kiểm tra dòng báo lỗi [cite: 564]
        if (expectedError != null) {
            response.body("error", containsString(expectedError)); // Xác nhận mã lỗi trả ra đúng từng chữ
        } else {
            // Nếu expectedError là null (case thành công), thì kiểm tra xem server có nhả token không
            response.body("token", notNullValue());
        }
    }
}