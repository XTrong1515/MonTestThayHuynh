# Selenium Test Framework (Lab 9 + Lab 11)

[![Test Status](https://github.com/TENTAIKHOAN/selenium-framework/actions/workflows/selenium-full.yml/badge.svg)](https://github.com/TENTAIKHOAN/selenium-framework/actions)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://TENTAIKHOAN.github.io/selenium-framework/)

*(Lưu ý: Thay `TENTAIKHOAN` bằng username của bạn thật trên GitHub để badge hiển thị xanh/đỏ chính xác nha)*

Framework chuẩn POM & DDT ứng dụng Java TestNG, Allure Report, chạy CI/CD trên Github Actions chia luồng (Matrix Matrix Strategy).

## Cách chạy Local
- Cài sẵn Java 17 và Maven cơ bản trên thiết bị Windows/Mac.
- Rename file `.env.example` thành `.env`, điền thông tin sauce/user password.
- Run smoke test mặc định:
```bash
mvn clean test -Denv=dev -Dbrowser=chrome -DsuiteXmlFile=testng-smoke.xml
```

## Chạy Allure báo cáo
Kiểm thử framework xuất báo cáo (report) trực quan dạng HTML với Allure, chỉ với 1 câu lệnh sau khi chạy `mvn clean test`:
```bash
mvn allure:serve
```

## Selenium Grid với Docker
Bật cluster Hub & Nodes phục vụ chạy Grid siêu tốc bằng lệnh:
```bash
docker-compose up -d
```
Xem log giao diện Grid trên http://localhost:4444. 
Muốn tắt Grid thì chạy:
```bash
docker-compose down
```
Chạy TestNG tương tác cùng GRID HUB:
```bash
mvn test -Dgrid.url=http://localhost:4444 -DsuiteXmlFile=testng-grid.xml
```
