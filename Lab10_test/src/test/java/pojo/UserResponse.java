// File: src/test/java/pojo/UserResponse.java
package pojo;

public class UserResponse {
    // Các field phản hồi từ server sau khi tạo user thành công [cite: 496]
    private String name;
    private String job;
    private String id;
    private String createdAt;
    private String updatedAt; // Dùng cho trường hợp PUT/PATCH

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getJob() { return job; }
    public void setJob(String job) { this.job = job; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}