using Microsoft.VisualStudio.TestTools.UnitTesting;
using StudentManagement;
using System;

namespace StudentTests
{
    [TestClass]
    public class HocVienTests
    {
        // ==========================================
        // NHÓM 1: PHÂN VÙNG TƯƠNG ĐƯƠNG (EP)
        // ==========================================

        // TC_HV_001: Học viên đạt chuẩn (Valid True)
        // Input: 9, 9, 9 -> ĐTB = 9.0 (>= 8), Min = 9 (>= 5)
        [TestMethod]
        public void Test_HocBong_TC_HV_001_Qualified_ReturnsTrue()
        {
            var hv = new HocVien("HV01", "Nguyen Van A", 9.0, 9.0, 9.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsTrue(result, "Học viên điểm cao đều phải nhận được học bổng.");
        }

        // TC_HV_002: Học viên rớt do ĐTB thấp (Valid False)
        // Input: 7, 7, 7 -> ĐTB = 7.0 (< 8)
        [TestMethod]
        public void Test_HocBong_TC_HV_002_LowAvg_ReturnsFalse()
        {
            var hv = new HocVien("HV02", "Le Thi B", 7.0, 7.0, 7.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsFalse(result, "ĐTB dưới 8.0 phải rớt học bổng.");
        }

        // TC_HV_003: ĐTB cao nhưng bị điểm liệt (Compound Condition)
        // Input: 10, 10, 4 -> ĐTB = 8.0 (Đạt), Min = 4 (< 5 Rớt)
        [TestMethod]
        public void Test_HocBong_TC_HV_003_HighAvg_FailSubject_ReturnsFalse()
        {
            var hv = new HocVien("HV03", "Pham Van C", 10.0, 10.0, 4.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsFalse(result, "Dù ĐTB >= 8.0 nhưng có môn < 5.0 thì phải rớt.");
        }

        // ==========================================
        // NHÓM 2: PHÂN TÍCH GIÁ TRỊ BIÊN (BVA)
        // ==========================================

        // TC_HV_004: Biên ĐTB vừa đủ 8.0
        // Input: 8, 8, 8 -> ĐTB = 8.0
        [TestMethod]
        public void Test_HocBong_TC_HV_004_BoundaryAvg8_ReturnsTrue()
        {
            var hv = new HocVien("HV04", "Hoang Van D", 8.0, 8.0, 8.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsTrue(result, "ĐTB đúng bằng 8.0 vẫn được nhận học bổng.");
        }

        // TC_HV_005: Biên ĐTB thiếu chút xíu (7.9...)
        // Input: 7.9, 8, 8 -> ĐTB = 7.96... (< 8.0)
        [TestMethod]
        public void Test_HocBong_TC_HV_005_BoundaryAvgBelow8_ReturnsFalse()
        {
            var hv = new HocVien("HV05", "Tran Thi E", 7.9, 8.0, 8.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsFalse(result, "ĐTB nhỏ hơn 8.0 (dù rất sát) vẫn phải rớt.");
        }

        // TC_HV_006: Biên môn học vừa đủ thoát liệt (5.0)
        // Input: 5, 9.5, 9.5 -> ĐTB = 8.0, Min = 5.0
        [TestMethod]
        public void Test_HocBong_TC_HV_006_BoundarySubject5_ReturnsTrue()
        {
            var hv = new HocVien("HV06", "Ngo Van F", 5.0, 9.5, 9.5);
            bool result = hv.KiemTraHocBong();
            Assert.IsTrue(result, "Môn thấp nhất là 5.0 (không dưới 5) nên vẫn đậu.");
        }

        // TC_HV_007: Biên môn học bị liệt sát nút (4.9)
        // Input: 4.9, 10, 10 -> ĐTB = 8.3, Min = 4.9
        [TestMethod]
        public void Test_HocBong_TC_HV_007_BoundarySubject49_ReturnsFalse()
        {
            var hv = new HocVien("HV07", "Vu Thi G", 4.9, 10.0, 10.0);
            bool result = hv.KiemTraHocBong();
            Assert.IsFalse(result, "Môn thấp nhất là 4.9 (< 5.0) nên phải rớt.");
        }

        // ==========================================
        // NHÓM 3: XỬ LÝ NGOẠI LỆ (INVALID INPUT)
        // ==========================================

        // TC_HV_008: Kiểm tra nhập điểm âm
        // Input: -1
        [TestMethod]
        public void Test_Constructor_TC_HV_008_NegativeScore_ThrowsException()
        {
            // Sử dụng cú pháp Assert.Throws để bắt lỗi chính xác
            Assert.Throws<ArgumentException>(() =>
                new HocVien("ERR1", "Error User", -1.0, 8.0, 8.0),
                "Phải ném ngoại lệ khi nhập điểm âm."
            );
        }

        // TC_HV_009: Kiểm tra nhập điểm lớn hơn 10
        // Input: 11
        [TestMethod]
        public void Test_Constructor_TC_HV_009_ScoreOver10_ThrowsException()
        {
            Assert.Throws<ArgumentException>(() =>
                new HocVien("ERR2", "Error User", 11.0, 8.0, 8.0),
                "Phải ném ngoại lệ khi nhập điểm > 10."
            );
        }
    }
}