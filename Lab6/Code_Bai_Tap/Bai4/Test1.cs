using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using GeometryExercise;

namespace GeometryTests
{
    [TestClass]
    public class HinhChuNhatTests
    {
        // TC_HCN_001: Tính diện tích hình vuông
        [TestMethod]
        public void Test_TinhDienTich_Square_ReturnsCorrectArea()
        {
            var hcn = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
            Assert.AreEqual(16, hcn.TinhDienTich());
        }

        // TC_HCN_002: Tính diện tích hình chữ nhật
        [TestMethod]
        public void Test_TinhDienTich_Rectangle_ReturnsCorrectArea()
        {
            var hcn = new HinhChuNhat(new Diem(0, 5), new Diem(10, 0));
            Assert.AreEqual(50, hcn.TinhDienTich());
        }

        // TC_HCN_003: Tính diện tích khi nhập tọa độ ngược (Cần logic chuẩn hóa)
        [TestMethod]
        public void Test_TinhDienTich_SwappedCoordinates_ReturnsCorrectArea()
        {
            // Nhập P1 là góc dưới phải, P2 là góc trên trái
            var hcn = new HinhChuNhat(new Diem(4, 0), new Diem(0, 4));
            Assert.AreEqual(16, hcn.TinhDienTich(), "Code phải tự chuẩn hóa tọa độ để tính đúng.");
        }

        // TC_HCN_004: Hai hình cắt nhau
        [TestMethod]
        public void Test_GiaoNhau_Intersecting_ReturnsTrue()
        {
            var h1 = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
            var h2 = new HinhChuNhat(new Diem(2, 6), new Diem(6, 2));
            Assert.IsTrue(h1.KiemTraGiaoNhau(h2));
        }

        // TC_HCN_005: Hình lồng nhau
        [TestMethod]
        public void Test_GiaoNhau_OneInsideAnother_ReturnsTrue()
        {
            var big = new HinhChuNhat(new Diem(0, 10), new Diem(10, 0));
            var small = new HinhChuNhat(new Diem(2, 8), new Diem(8, 2));
            Assert.IsTrue(big.KiemTraGiaoNhau(small));
        }

        // TC_HCN_006: Hai hình chạm cạnh nhau (Biên)
        [TestMethod]
        public void Test_GiaoNhau_TouchingEdges_ReturnsTrue()
        {
            var h1 = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0)); // Xmax = 4
            var h2 = new HinhChuNhat(new Diem(4, 4), new Diem(8, 0)); // Xmin = 4
            Assert.IsTrue(h1.KiemTraGiaoNhau(h2), "Chạm biên vẫn tính là giao nhau.");
        }

        // TC_HCN_007: Hai hình rời nhau hoàn toàn
        [TestMethod]
        public void Test_GiaoNhau_Disjoint_ReturnsFalse()
        {
            var h1 = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));
            var h2 = new HinhChuNhat(new Diem(5, 4), new Diem(9, 0)); // Xmin = 5 > Xmax 4
            Assert.IsFalse(h1.KiemTraGiaoNhau(h2));
        }

        // TC_HCN_008: Kiểm tra truyền Null
        [TestMethod]
        public void Test_GiaoNhau_NullInput_ThrowsException()
        {
            var h1 = new HinhChuNhat(new Diem(0, 4), new Diem(4, 0));

            // Sử dụng Assert.Throws thay vì ThrowsException
            Assert.Throws<ArgumentNullException>(() => h1.KiemTraGiaoNhau((HinhChuNhat?)null));
        }
    }
}