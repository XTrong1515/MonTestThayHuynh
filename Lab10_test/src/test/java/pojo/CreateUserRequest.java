// File: src/test/java/pojo/CreateUserRequest.java
package pojo;

public class CreateUserRequest {
    // Khai báo các field private tương ứng với JSON body cần gửi [cite: 496]
    private String name;
    private String job;

    // Constructor mặc định cần thiết cho Jackson deserialize
    public CreateUserRequest() {}

    // Constructor để khởi tạo đối tượng nhanh [cite: 498]
    public CreateUserRequest(String name, String job) {
        this.name = name;
        this.job = job;
    }

    // Các Getter và Setter (bắt buộc phải có để Jackson đọc/ghi dữ liệu)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
}