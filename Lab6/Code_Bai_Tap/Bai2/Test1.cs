using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq;

namespace PolynomialExercise
{
    // ==========================================
    // PHẦN CODE CỦA BÀI TẬP (GIỮ NGUYÊN)
    // ==========================================
    class Polynomial
    {
        private int n;
        private List<int> a;

        public Polynomial(int n, List<int> a)
        {
            // Code gốc của bài tập (đang thiếu check n < 0)
            if (a.Count() != n + 1)
                throw new ArgumentException("Invalid Data");

            this.n = n;
            this.a = a;
        }

        public int Cal(double x)
        {
            int result = 0;
            for (int i = 0; i <= this.n; i++)
            {
                result += (int)(a[i] * Math.Pow(x, i));
            }
            return result;
        }
    }

    // ==========================================
    // PHẦN UNIT TEST (ĐÃ SỬA ĐỂ TƯƠNG THÍCH MỌI PHIÊN BẢN)
    // ==========================================
    [TestClass]
    public class PolynomialTests
    {
        // TC_01: Kiểm tra tính toán đúng
        [TestMethod]
        public void Test_Cal_ValidPolynomial_ReturnsCorrectValue()
        {
            int n = 2;
            List<int> coeffs = new List<int> { 1, 2, 3 };
            double x = 2.0;
            int expected = 17;

            Polynomial poly = new Polynomial(n, coeffs);
            int actual = poly.Cal(x);

            Assert.AreEqual(expected, actual, "Tính toán sai giá trị đa thức");
        }

        // TC_02: Kiểm tra khi x = 0 
        [TestMethod]
        public void Test_Cal_WithXIsZero_ReturnsFreeCoefficient()
        {
            int n = 1;
            List<int> coeffs = new List<int> { 10, 5 };
            double x = 0;
            int expected = 10;

            Polynomial poly = new Polynomial(n, coeffs);
            int actual = poly.Cal(x);

            Assert.AreEqual(expected, actual);
        }

        // TC_03: Kiểm tra hệ số âm
        [TestMethod]
        public void Test_Cal_WithNegativeCoefficients()
        {
            int n = 1;
            List<int> coeffs = new List<int> { -5, 2 };
            double x = 3;
            int expected = 1;

            Polynomial poly = new Polynomial(n, coeffs);
            int actual = poly.Cal(x);

            Assert.AreEqual(expected, actual);
        }

        // --- CÁC TEST CASE CHECK NGOẠI LỆ (Đã sửa lại dùng Try-Catch) ---

        // TC_04: Kiểm tra thiếu hệ số
        [TestMethod]
        public void Test_Constructor_NotEnoughCoefficients_ThrowsException()
        {
            try
            {
                // Hành động gây lỗi: Cần 3 số nhưng chỉ đưa 2
                new Polynomial(2, new List<int> { 1, 2 });

                // Nếu chạy đến dòng này mà không lỗi -> Test Fail
                Assert.Fail("Lẽ ra phải báo lỗi 'Invalid Data' nhưng không thấy báo.");
            }
            catch (ArgumentException ex)
            {
                // Bắt được lỗi -> Kiểm tra nội dung lỗi có đúng không
                Assert.AreEqual("Invalid Data", ex.Message);
            }
            catch (Exception)
            {
                Assert.Fail("Báo lỗi sai loại ngoại lệ (Không phải ArgumentException).");
            }
        }

        // TC_05: Kiểm tra thừa hệ số
        [TestMethod]
        public void Test_Constructor_TooManyCoefficients_ThrowsException()
        {
            try
            {
                // Hành động gây lỗi: Cần 2 số nhưng đưa 3
                new Polynomial(1, new List<int> { 1, 2, 3 });

                Assert.Fail("Lẽ ra phải báo lỗi 'Invalid Data' nhưng không thấy báo.");
            }
            catch (ArgumentException ex)
            {
                Assert.AreEqual("Invalid Data", ex.Message);
            }
        }

        // TC_06: Kiểm tra n âm (ĐÂY LÀ CASE SẼ GIÚP EM TÌM BUG)
        [TestMethod]
        public void Test_Constructor_NegativeDegree_ThrowsException()
        {
            try
            {
                // Hành động gây lỗi: n = -1
                // List rỗng {} có count = 0. n+1 = 0. => 0 == 0 => Code Dev sẽ lọt qua bước check!
                new Polynomial(-1, new List<int> { });

                // Nếu code chạy xuống đây tức là Dev KHÔNG bắt lỗi n âm
                Assert.Fail("BUG FOUND: Chương trình cho phép n âm mà không báo lỗi!");
            }
            catch (ArgumentException ex)
            {
                Assert.AreEqual("Invalid Data", ex.Message);
            }
        }
    }
}