// File: src/test/java/tests/GetApiTest.java
package tests;

import base.ApiBaseTest;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

// Kế thừa ApiBaseTest để sử dụng lại requestSpec và responseSpec
public class GetApiTest extends ApiBaseTest {

    // Kịch bản 1: Lấy danh sách users trang 1 [cite: 481]
    @Test(description = "Test 1: GET /api/users?page=1")
    public void testGetUsersPage1() {
        // Bắt đầu chuỗi thiết lập request
        given()
                .spec(requestSpec) // Áp dụng cấu hình chung từ ApiBaseTest [cite: 304, 305]
                .queryParam("page", 1) // Truyền tham số page=1 vào URL
                .when()
                .get("/api/users") // Thực hiện phương thức GET tới endpoint /api/users [cite: 106]
                .then()
                .spec(responseSpec) // Áp dụng kiểm tra cấu hình chung (thời gian < 3s, kiểu JSON) [cite: 309, 310]
                .statusCode(200) // Kỳ vọng mã phản hồi là 200 OK [cite: 108]
                .body("page", equalTo(1)) // Kiểm tra field 'page' trong JSON trả về bằng 1
                .body("total_pages", greaterThan(0)) // Kiểm tra tổng số trang phải lớn hơn 0
                .body("data.size()", greaterThanOrEqualTo(1)); // Kiểm tra mảng 'data' chứa ít nhất 1 phần tử
    }

    // Kịch bản 2: Lấy danh sách users trang 2 và kiểm tra chi tiết cấu trúc dữ liệu [cite: 482, 483]
    @Test(description = "Test 2: GET /api/users?page=2")
    public void testGetUsersPage2() {
        given()
                .spec(requestSpec) // Sử dụng spec chung
                .queryParam("page", 2) // Gán tham số page=2
                .when()
                .get("/api/users") // Gọi API GET danh sách user
                .then()
                .spec(responseSpec) // Kiểm tra SLA chung
                .statusCode(200) // Kỳ vọng mã HTTP 200
                .body("page", equalTo(2)) // Xác nhận dữ liệu trả về thuộc trang 2
                .body("data.id", everyItem(notNullValue())) // Kiểm tra MỌI phần tử trong mảng data đều phải có id khác null
                .body("data.email", everyItem(notNullValue())) // Kiểm tra MỌI phần tử đều phải có email
                .body("data.first_name", everyItem(notNullValue())) // Kiểm tra MỌI phần tử đều có first_name
                .body("data.last_name", everyItem(notNullValue())) // Kiểm tra MỌI phần tử đều có last_name
                .body("data.avatar", everyItem(notNullValue())); // Kiểm tra MỌI phần tử đều có avatar URL
    }

    // Kịch bản 3: Lấy thông tin chi tiết của 1 user cụ thể [cite: 484]
    @Test(description = "Test 3: GET /api/users/3")
    public void testGetSingleUser() {
        given()
                .spec(requestSpec) // Dùng cấu hình base
                .when()
                .get("/api/users/3") // Lấy data của user có ID là 3
                .then()
                .spec(responseSpec) // Kiểm tra thời gian phản hồi
                .statusCode(200) // Trả về thành công 200
                .body("data.id", equalTo(3)) // Xác nhận ID trả về đúng là 3
                .body("data.email", containsString("@reqres.in")) // Xác nhận email thuộc domain @reqres.in
                .body("data.first_name", not(emptyString())); // Xác nhận tên first_name không bị bỏ trống
    }

    // Kịch bản 4: Gọi API lấy user không tồn tại để kiểm thử mã lỗi [cite: 485]
    @Test(description = "Test 4: GET /api/users/9999")
    public void testUserNotFound() {
        given()
                .spec(requestSpec) // Khởi tạo request chuẩn
                .when()
                .get("/api/users/9999") // Truy vấn ID 9999 (chắc chắn không tồn tại)
                .then()
                .spec(responseSpec) // Phản hồi vẫn phải đạt chuẩn SLA
                .statusCode(404) // Kỳ vọng server ném ra lỗi 404 Not Found [cite: 132]
                .body("$", anEmptyMap()); // Kỳ vọng JSON body trả về là một object rỗng {} [cite: 133]
    }
}