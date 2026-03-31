# Test Strategy & Test Plan Document - Dự án ShopEasy
**Người viết:** QA Lead
**Dự án:** Ứng dụng mua sắm online – ShopEasy
**Sprint 5 Goal:** Ra mắt tính năng 'Thanh toán trả góp qua VPBank'

---

## PHẦN A – Test Strategy Document

### 1. Phạm vi kiểm thử (Scope)
Xác định rõ những gì cần và không cần kiểm thử trong Sprint này.

| Loại | Module / Tính năng | Lý do |
|---|---|---|
| **IN SCOPE** | Đăng ký tài khoản | Core feature, ảnh hưởng trực tiếp đến người dùng mới mong muốn trả góp |
| **IN SCOPE** | Đăng nhập / Xác thực | Yếu tố bảo mật quan trọng, có rủi ro cao nếu không chứng thực thông tin chính xác |
| **IN SCOPE** | Tìm kiếm sản phẩm | Tính năng chính, thao tác người dùng trước khi tiếp cận giỏ hàng |
| **IN SCOPE** | Giỏ hàng | Nơi diễn ra bước tiến hành mua sắm, ảnh hưởng trực tiếp hệ thống doanh thu |
| **IN SCOPE** | Thanh toán (VPBank) | Liên quan đến luồng giao dịch tiền mặt/tín dụng, rủi ro cao nhất |
| **OUT SCOPE** | Admin Dashboard | Giai đoạn 2 (Phase 2), không thuộc vi phạm yêu cầu Sprint hiện tại |
| **OUT SCOPE** | Báo cáo thống kê | Giai đoạn 2, không trực tiếp tác động với end-user |

### 2. Phân loại test và tỉ lệ phân bổ
Chiến lược phân bổ tự động hóa để tối ưu hóa thời gian và đảm bảo độ bao phủ rủi ro của tính năng thanh toán.

| Loại test | Tỉ lệ dự kiến | Công cụ áp dụng | Lý do chọn |
|---|---|---|---|
| Unit Test | 20% | JUnit 5 + Mockito | Trách nhiệm của Developer, giúp phát hiện lỗi thuật toán tính luồng giá/kỳ hạn cực nhanh với chi phí thấp nhất. |
| API Test | 45% | RestAssured / Postman | Do nền tảng thương mại điện tử phụ thuộc API rất nhiều đễ đảm bảo tính toàn vẹn (đặc biệt khi kết nối hệ thống VPBank). |
| UI Test (Selenium) | 20% | Selenium + POM + TestNG | Kiểm tra trải nghiệm người dùng, đảm bảo các radio box, nút bấm thanh toán hoạt động ổn định trên nhiều trình duyệt đa nền tảng. |
| Performance Test | 10% | Apache JMeter | Hệ thống ShopEasy luôn chịu tải cao trong ngày event lớn nên thanh toán luồng tải nặng phải chịu được 10,000 requests/giây. |
| Security Test | 5% | OWASP ZAP | Do xử lý thông tin nhạy cảm định danh và thông tin tín dụng nên bắt buộc thực hiện rà soát an ninh. |

### 3. Definition of Done (DoD) - Chuẩn hoàn thành
Để một phiên bản QA được đánh dấu là "đã test xong", tất cả các tiêu chí sau phải được đáp ứng:
- **Smoke test:** 100% test cases đi qua thành công (100% PASS).
- **Regression test:** Phải đạt tỉ lệ ít nhất ≥ 95% PASS vì có tích hợp component mới.
- Không tồn tại bất kỳ Bug cấp độ P1 (Blocker) nào đang trạng thái Mở (Open).
- Không có bất kỳ Bug P2 (Critical) nào không được lên kế hoạch (Scheduled) xử lý.
- Độ bao phủ Code (Code coverage) đạt chuẩn tối thiểu ≥ 80% dựa trên JaCoCo (hệ thống báo cáo tích hợp GitHub Actions).
- Pipeline CI/CD trên Github Actions phải trả về trạng thái Success với màu xanh lá, tự động cung cấp báo cáo.
- **Báo Cáo Allure:** Phải được xuất bản và đội ngũ (Team) bao gồm PM + PO xác nhận duyệt.

### 4. Bảng phân tích rủi ro và quản lý (Risk Management)
Các rủi ro liên quan đến quá trình vận hành và kiểm thử luồng trả góp của bên thứ ba (VPBank).

| Rủi ro (Risk) | Xác suất (Prob) | Tác động (Impact) | Kế hoạch giảm thiểu (Mitigation Strategy) |
|---|---|---|---|
| **Sandbox VPBank không ổn định** | Cao | Block toàn bộ phase test E2E | Liên hệ VPBank lấy sandbox sớm; thiết kế mock server/Mock API để thay thế dự phòng khẩn cấp. |
| **Staging DB reset/dữ liệu test bị xoá đột ngột** | Trung bình | Phải cào lại Test data hoặc tạo lại tốn nhiều giờ | Chạy script seed data tự động, build CI jobs tạo Mock users/ Mock orders trước mỗi chu trình chạy test. |
| **Dịch vụ 3rd Party (SMS, Email OTP, Bank) bị treo** | Trung bình | Test E2E/API bị block | Sử dụng Offline mock services giả lập phản hồi (response format). |
| **CI server hết disk space sinh lỗi pipeline do file artifacts lớn** | Thấp | Test tự động ngừng ở Github Actions | Cài cron job tự động `rm` (xóa) artifact cũ hoặc cấu hình vòng đời tồn tại (retention-days) trên github actions chỉ giữ lại artifact trong vòng tối đa 7 ngày để giải phóng log. |

### 5. Lịch trình kiểm thử
| Loại test | Trigger Trigger / Tần Suất | Thời gian chạy ước tính | Phương thức |
|---|---|---|---|
| Smoke Test | Sau mỗi lần commit Push code lên Repository | ~5 phút | Tự động hóa qua GitHub Actions CI |
| Regression Test | Hàng đêm vào lúc 2:00 AM sáng thứ 2 đến thứ 6 | ~45 phút | Lên lịch tự động - Cron schedule qua Allure |
| Performance Test | Cuối tuần (Chủ nhật) khi traffic thấp | ~2 giờ | Chạy tay hoặc Cronjob |
| Security Scan | Trước phiên bản Release lên Production vài ngày | ~3 giờ | Công cụ scan rà soát thủ công / Sonarqube |
| UAT | Cuối mỗi mốc Sprint 2 tuần | 2–3 ngày | Phối hợp thử nghiệm môi trường thật có sự chứng kiến và tương tác từ Product Owner (PO) |


---

## PHẦN B – Test Plan cho Sprint 5
**Chủ đề Tính năng:** Thanh toán trả góp VPBank (3/6/12 tháng, miễn phí 0% lãi suất với đơn hàng ≥ 3 triệu VNĐ).

### Danh Sách 5 Kịch Bản Gây Rủi Ro Mất Tiền Người Dùng
1. _Đơn hàng giá trị < 3.000.000 VNĐ nhưng hệ thống UI/API lỗi vẫn bypass xác thực cho phép bấm trả góp VPBank_ → Hậu quả: khách hàng bị trừ tiền thu phí ngoài ý muốn trái định nghĩa hợp đồng trả góp.
2._Công thức tính toán chia số tiền mặt trả theo kỳ báo sai (không miễn phí 0%)_ → Hậu quả: người dùng bị ngân hàng hoặc cổng dịch vụ trừ tiền sai lệch.
3. _Thanh toán API từ VPBank báo về trạng thái Success (trừ tiền) nhưng đơn hàng ShopEasy lại không bắt được ACK (acknowledge) và ghi nhận là Failed_ → Hậu quả: Dẫn tới mất tiền nhưng khách không thấy đơn đặt thành công.
4. _Hệ thống crash/disconnect timeout giữa luồng redirect chuyển hướng payment_ → Hậu quả: Trạng thái không rõ ràng, chưa confirm được tiền đã trừ hay chưa, dễ sinh tranh chấp giao dịch.
5. _Phiên giao dịch (Session) trên web bị hết hạn (timeout session) khi vẫn đang rề rà trong trang Checkout_ → Hệ thống rollback giao dịch hoặc ngưng xác nhận nhưng chưa báo người lại bắt họ nhập mới từ đầu, gây khó hiểu hoặc spam nhấn trừ tiền 2 lần.

### 15 Test Cases Chính Yếu dành Cho Sprint 5
| TC-ID | Tiêu đề Test Case (Phạm vi Trả góp VPBank) | Loại | Ưu tiên | Kết quả mong đợi (Expected Result) |
|---|---|---|---|---|
| TC-001 | Thanh toán trả góp kỳ hạn 3 tháng với đơn hàng ≥ 3 triệu VND quy định | API | P1 | Status code 201 trả về, `status=APPROVED`. Đơn hàng ghi nhận |
| TC-002 | Khách chọn thanh toán trả góp nhưng tổng giỏ hàng < 3 triệu VNĐ | API | P1 | Sẽ báo lỗi HTTP 400 Bad Request, Exception `error=ORDER_TOO_SMALL`. Thanh toán bị reject |
| TC-003 | Giao diện thanh toán hiện đầy đủ 3 option trả góp thời gian (3, 6, 12 tháng) | UI | P1 | Element UI hiển thị 3 radio buttons clickable hoạt động chuẩn |
| TC-004 | Xác thực công thức chia giá gốc khoản vay mỗi tháng phải khớp 100% | Unit | P1 | Số Tền Trả Tháng = (Số Tiền Gốc Mua) / (Số Cột Kỳ Hạn tháng), lãi suất phí gia tăng = 0 |
| TC-005 | Cập nhật tổng số tiền hiển thị thời gian thực theo đúng tùy chọn được chọn | UI | P2 | Giao diện phải thông báo chính xác số tiền gốc + tổng phí (bằng 0) |
| TC-006 | Thanh toán kỳ hạn 6 tháng hoạt động trơn tru từ A đến Z | API | P1 | Request thanh toán thành công, response 201 Created `status=APPROVED` |
| TC-007 | Thanh toán kỳ hạn mức tối đa 12 tháng không xảy ra độn lãi lên giá | API | P1 | Hệ thống chấp thuận, không sai lệch số tổng sau 12 tháng, kết quả 201 `status=APPROVED` |
| TC-008 | User thực thi trả góp thông qua tài khoản VPBank giả lập môi trường Test hợp lệ | E2E | P1 | Đơn hàng trạng thái "Chờ giao", giao dịch trả góp APPROVED và lưu chi tiết thanh toán thành công. |
| TC-009 | Checkout bằng thẻ tín dụng VPBank đã hết hạn / Cũ | API | P1 | Giao dịch chặn lại: Status 400, thông điệp `error=CARD_EXPIRED` |
| TC-010 | Submit dữ liệu khi hệ thống VPBank (sandbox dummy server) đang down/lỗi | API | P2 | Server return lỗi 503 (Dịch vụ không sẵn sàng), UI render câu báo lỗi thân thiện thay vì crash 500 |
| TC-011 | Giai đoạn ấn Cancel giữa chừng khi Webpage chuyển tiếp sang payment gateway | E2E | P1 | Ứng dụng rollback hoặc quay về giỏ hàng. Số tiền tuyệt đối không bị trừ |
| TC-012 | Session hết hạn lúc đang gõ thông tin thanh toán do để máy ngâm lâu quá 15 phút | E2E | P2 | UI yêu cầu đăng nhập lại, thông tin giỏ hàng vẫn còn lưu chứ không mất hay reset trống rỗng. |
| TC-013 | Lọc ẩn tuỳ chọn thẻ trả góp cho giá trị đơn hàng siêu bé (< 3 triệu) trên góc độ UI | UI | P2 | Radio section trả góp hoàn toàn không hiển thị trong màn hình DOM element |
| TC-014 | Gửi Email Receipt Billing có xác nhận thông tin trả góp | API | P2 | Email chi tiết kì hạn phải bay vào tài khoản User qua SMTP trong vòng muộn nhất 5 phút sau checkout |
| TC-015 | Layout lịch sử Order (Order History) hiển thị field 'Pay by VPBank Trả Góp' | UI | P3 | Phải in danh sách từng chu kỳ trả rực quan cho người dùng theo dõi và số tiền tương ứng từng tháng |

### Xác Định Blockers Cản Trở Tiến Độ Testing
1. **Môi trường Sandbox / Gateway VPBank chưa sẵn sàng ở tuẩn đầu tiên.** (Tác động mạnh - Prob High)
   **Xử lý:** Ép hoặc nhắc nhở bên phía cung cấp đưa thông tin, đồng thời DevOps hoặc BE phải tự tạo "Mock API Gateway proxy" chạy local để Automation Test E2E vượt qua được step đó đến chừng nào Vendor giao Sandbox thực tế.

2. **Test Data (Credential thẻ VISA/ATM VPBank) cấp sẵn không tồn tại hoặc ít.**
   **Xử lý:** Liên hệ BA (Business Analyst) hoặc PM phối hợp với Bank ngay ngày đầu tiên của sprint lập danh sách card thẻ Sandbox Test tương ứng với test Pass (có đủ hạn mức) và Failed (thẻ hết hiệu lực, hạn mức bằng 0).

3. **Chưa có chuẩn API Specification hoặc API doc trên Swagger bị lỗi thời.**
   **Xử lý:** Nhấn mạnh bắt buộc (Mandate) BE Devs hoàn thiện API Request/Response schema hợp đồng (contract) trước khi bắt tay viết core API Test (giúp làm TDD và API mock).

4. **Môi trường Server Staging chậm triển khai tích hợp mã nguồn branch VPBank. **
   **Xử lý:** Tuần thứ nhất tự tin deploy test API mock ở localhost kết hợp Dev Environment. Chỉ deploy lên Staging khi BE tự tin Test Unit hoàn chỉnh, tránh Staging die mất debug track của QA.
