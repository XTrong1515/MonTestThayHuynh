// File: src/test/java/tests/PerformanceSlaTest.java
package tests;

import base.ApiBaseTest;
import io.qameta.allure.Step;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PerformanceSlaTest extends ApiBaseTest {

    // Bảng ma trận quy định SLA cho từng endpoint khác nhau [cite: 569, 571]
    @DataProvider(name = "slaEndpoints")
    public Object[][] slaEndpoints() {
        return new Object[][] {
                // Method | Endpoint | SLA (ms) | Status Code
                {"GET", "/api/users", 2000L, 200},
                {"GET", "/api/users/2", 1500L, 200},
                {"POST", "/api/users", 3000L, 201},
                {"POST", "/api/login", 2000L, 200},
                {"DELETE", "/api/users/2", 1000L, 204}
        };
    }

    // Tích hợp Allure Report. Tham số được nội suy vào chuỗi Step để báo cáo hiển thị sinh động [cite: 572]
    @Step("Gọi {method} {endpoint} - SLA: {maxMs}ms")
    @Test(dataProvider = "slaEndpoints", description = "Kiểm tra SLA Performance đa Endpoint")
    public void testSlaMonitor(String method, String endpoint, long maxMs, int expectedStatus) {
        // Lấy mốc thời gian trước khi gọi API [cite: 427]
        long startTime = System.currentTimeMillis();

        // Khởi tạo request
        var request = given().spec(requestSpec);

        // Setup payload riêng cho những method cần body (như POST)
        if (method.equals("POST") && endpoint.contains("login")) {
            request.body("{\"email\":\"eve.holt@reqres.in\",\"password\":\"cityslicka\"}");
        } else if (method.equals("POST") && endpoint.contains("users")) {
            request.body("{\"name\":\"Trong\",\"job\":\"QA\"}");
        }

        // Thực thi gọi API động tùy theo Method đang chạy
        var response = switch (method) {
            case "GET" -> request.when().get(endpoint);
            case "POST" -> request.when().post(endpoint);
            case "DELETE" -> request.when().delete(endpoint);
            default -> throw new IllegalArgumentException("Method không hỗ trợ");
        };

        // Kiểm tra cơ bản: Mã HTTP và thời gian phản hồi phải dưới ngưỡng SLA [cite: 468]
        response.then()
                .statusCode(expectedStatus)
                .time(lessThan(maxMs));

        // Lấy thời gian sau khi kết thúc và tính số mili-giây đã trôi qua [cite: 432]
        long elapsed = System.currentTimeMillis() - startTime;

        // In console log để sinh viên chụp hình nộp báo cáo [cite: 573]
        System.out.println("[Perf Monitor] " + method + " " + endpoint + " mất: " + elapsed + "ms (Max SLA: " + maxMs + "ms)");

        // Thực thi các Assertion bổ sung dựa theo yêu cầu của từng endpoint [cite: 569]
        if (endpoint.equals("/api/users") && method.equals("GET")) {
            response.then().body("data.size()", greaterThanOrEqualTo(1));
        } else if (endpoint.equals("/api/users/2") && method.equals("GET")) {
            response.then().body("data.id", equalTo(2));
        } else if (endpoint.equals("/api/users") && method.equals("POST")) {
            response.then().body("id", notNullValue());
        } else if (endpoint.equals("/api/login") && method.equals("POST")) {
            response.then().body("token", notNullValue());
        }
    }

    // Mô phỏng tool Monitor: Chạy 1 API 10 lần liên tục để lấy thống kê Min/Max/Average [cite: 574, 575]
    @Test(description = "Chạy API 10 lần tính toán cấu hình Min/Max/Avg")
    public void testAverageResponseTime() {
        List<Long> times = new ArrayList<>();

        // Vòng lặp bắn 10 request liên tiếp
        for (int i = 0; i < 10; i++) {
            long timeTaken = given()
                    .spec(requestSpec) // Load cấu hình base
                    .when()
                    .get("/api/users?delay=1") // API có tính năng delay để test time
                    .time(); // Method của RestAssured trả về trực tiếp số ms tiêu tốn

            times.add(timeTaken); // Thêm kết quả vào danh sách
        }

        // Xử lý dữ liệu thống kê bằng hàm Collection chuẩn của Java
        long min = Collections.min(times);
        long max = Collections.max(times);
        long sum = times.stream().mapToLong(Long::longValue).sum(); // Tính tổng toàn bộ mảng
        long avg = sum / times.size(); // Tính trung bình

        // In báo cáo thống kê ra màn hình
        System.out.println("=== THỐNG KÊ PERFORMANCE SAU 10 LẦN CHẠY ===");
        System.out.println("Thời gian trung bình (Average): " + avg + "ms");
        System.out.println("Thời gian chậm nhất (Max): " + max + "ms");
        System.out.println("Thời gian nhanh nhất (Min): " + min + "ms");
    }
}
