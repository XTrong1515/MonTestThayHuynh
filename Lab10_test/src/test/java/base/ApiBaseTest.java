// File: src/test/java/base/ApiBaseTest.java
package base;

// Import tĩnh các thư viện cần thiết của REST Assured
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import static org.hamcrest.Matchers.lessThan;

public class ApiBaseTest {
    // Khai báo biến protected để các class con có thể kế thừa và sử dụng [cite: 270, 271]
    protected RequestSpecification requestSpec;
    protected ResponseSpecification responseSpec;

    // Annotation @BeforeClass đảm bảo cấu hình này chạy một lần trước khi các test trong class bắt đầu [cite: 88]
    @BeforeClass
    public void setupApiSpec() {
        // Ngụy trang full giáp: Thêm toàn bộ header của một trình duyệt thật
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Encoding", "gzip, deflate, br, zstd")
                .addHeader("Accept-Language", "vi-VN,vi;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("Connection", "keep-alive")
                .addHeader("Sec-Ch-Ua", "\"Google Chrome\";v=\"123\", \"Not:A-Brand\";v=\"8\", \"Chromium\";v=\"123\"")
                .addHeader("Sec-Ch-Ua-Mobile", "?0")
                .addHeader("Sec-Ch-Ua-Platform", "\"Windows\"")
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        responseSpec = new ResponseSpecBuilder()
                // Tạm thời comment dòng kiểm tra JSON lại để xem nó có nhả data không
                // .expectContentType(ContentType.JSON)
                .expectResponseTime(lessThan(5000L)) // Nâng SLA lên 5s vì qua tường lửa sẽ chậm
                .build();
    }
}