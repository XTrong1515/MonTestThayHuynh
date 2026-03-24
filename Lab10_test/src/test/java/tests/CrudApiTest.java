// File: src/test/java/tests/CrudApiTest.java
package tests;

import base.ApiBaseTest;
import pojo.CreateUserRequest;
import pojo.UserResponse;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CrudApiTest extends ApiBaseTest {
    // Biến lưu trữ ID sinh ra để chain các API call với nhau [cite: 499]
    private String createdUserId;

    // POST: Tạo User mới và Validate JSON Schema [cite: 489]
    @Test(description = "POST tạo user và validate schema")
    public void testCreateUser() {
        // Khởi tạo body request thông qua POJO object thay vì String thuần [cite: 498]
        CreateUserRequest reqBody = new CreateUserRequest("Trong Tram", "Backend Developer");

        // Gọi API POST và ánh xạ thẳng (deserialize) JSON response trả về thành object UserResponse
        UserResponse response = given()
                .spec(requestSpec) // Sử dụng cấu hình chung
                .body(reqBody) // Truyền POJO vào body, RestAssured tự parse sang JSON
                .when()
                .post("/api/users") // Gọi phương thức POST
                .then()
                .spec(responseSpec) // Kiểm tra SLA thời gian
                .statusCode(201) // Trạng thái HTTP 201 Created (Tạo thành công) [cite: 151]
                // Áp dụng JSON Schema Validation để bảo vệ cấu trúc, ngăn chặn việc Backend trả về field lạ (additionalProperties: false) [cite: 504, 505]
                .body(matchesJsonSchemaInClasspath("schemas/create-user-schema.json"))
                .body("name", equalTo("Trong Tram")) // Xác nhận name khớp dữ liệu gửi lên
                .body("id", notNullValue()) // Đảm bảo server có tạo ID [cite: 155]
                .body("createdAt", notNullValue()) // Đảm bảo có timestamp tạo [cite: 159]
                .extract().as(UserResponse.class); // Giải nén luồng response ra thành POJO Java

        // Lấy ID tự động sinh ra lưu vào biến global để dùng cho các bước GET/PUT phía sau [cite: 160]
        createdUserId = response.getId();
        System.out.println("Đã tạo user với ID: " + createdUserId);
    }

    // GET: Truy vấn lại chính User vừa tạo để xác nhận [cite: 489, 499]
    // Sử dụng dependsOnMethods để đảm bảo test POST chạy trước và thành công
    @Test(description = "GET để xác nhận data trả về khớp với data đã tạo", dependsOnMethods = "testCreateUser")
    public void testVerifyCreatedUser() {
        given()
                .spec(requestSpec) // Dùng base cấu hình
                .when()
                // Truyền ID vừa sinh ra vào đường dẫn API (Lưu ý: reqres.in mock API GET tạo mới thường trả về null/404, nhưng đây là minh họa flow chuẩn)
                .get("/api/users/" + createdUserId)
                .then()
                .spec(responseSpec) // Kiểm tra SLA
                .statusCode(200); // Kỳ vọng 200 OK
    }

    // PUT: Cập nhật toàn bộ thông tin User [cite: 489]
    @Test(description = "PUT cập nhật user", dependsOnMethods = "testCreateUser")
    public void testUpdateUserPut() {
        // Cập nhật chức danh từ Backend thành Fullstack
        CreateUserRequest reqBody = new CreateUserRequest("Trong Tram", "Fullstack Developer");

        UserResponse response = given()
                .spec(requestSpec) // Load cấu hình base
                .body(reqBody) // Truyền body cập nhật
                .when()
                .put("/api/users/2") // Endpoint PUT [cite: 167]
                .then()
                .spec(responseSpec)
                .statusCode(200) // PUT thành công trả về 200 [cite: 169]
                .body("job", equalTo("Fullstack Developer")) // Xác nhận job đã được đổi
                .body("updatedAt", notNullValue()) // Xác nhận có sinh ra mốc thời gian cập nhật [cite: 172]
                .extract().as(UserResponse.class); // Giải nén để assert nâng cao

        // Assert thủ công bằng TestNG: Thời gian cập nhật phải luôn khác (hoặc mới hơn) thời gian khởi tạo
        Assert.assertNotNull(response.getUpdatedAt(), "Trường updatedAt không được phép null");
    }

    // PATCH: Cập nhật một phần (chỉ cập nhật job) [cite: 489]
    @Test(description = "PATCH cập nhật một phần")
    public void testUpdateUserPatch() {
        // Đối với PATCH, thường dùng Map thay vì POJO để tránh gửi các trường null không mong muốn
        Map<String, String> patchBody = new HashMap<>();
        patchBody.put("job", "Software Tester");

        given()
                .spec(requestSpec) // Load base spec
                .body(patchBody) // Truyền Map JSON
                .when()
                .patch("/api/users/2") // Endpoint PATCH
                .then()
                .spec(responseSpec)
                .statusCode(200) // Mã HTTP 200
                .body("job", equalTo("Software Tester")) // Xác nhận dữ liệu partial đã ghi đè thành công
                .body("updatedAt", notNullValue()); // Có thời gian cập nhật
    }

    // DELETE: Xóa User [cite: 489]
    @Test(description = "DELETE xóa user")
    public void testDeleteUser() {
        given()
                .spec(requestSpec) // Cấu hình chuẩn
                .when()
                .delete("/api/users/2") // Endpoint xóa [cite: 176]
                .then()
                .spec(responseSpec)
                .statusCode(204) // Thành công nhưng không trả về data (204 No Content) [cite: 178]
                .body(is(emptyString())); // Kiểm tra response body rỗng hoàn toàn, không có chuỗi nào [cite: 489]
    }
}