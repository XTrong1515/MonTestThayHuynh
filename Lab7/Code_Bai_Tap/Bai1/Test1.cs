using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;

namespace PowerCalculation.Tests
{
    // Giả sử class chứa hàm Power tên là 'MathUtils'
    // Em cần đảm bảo hàm Power là public để Unit Test gọi được
    public class MathUtils
    {
        // Code đề bài giữ nguyên tuyệt đối, không sửa bug
        public static double Power(double x, int n)
        {
            if (n == 0)
                return 1.0;
            else if (n > 0)
                return n * Power(x, n - 1); // Logic sai ở đây (n thay vì x), nhưng Tester không được sửa source
            else
                return Power(x, n + 1) / x;
        }
    }

    [TestClass]
    public class PowerFunctionTests
    {
        // Định nghĩa sai số cho phép khi so sánh số thực (double)
        private const double Delta = 0.0001;

        // TC_01: Kiểm tra trường hợp n = 0
        [TestMethod]
        public void Test_Power_WhenExponentIsZero_ShouldReturnOne()
        {
            // Arrange
            double x = 999.0;
            int n = 0;
            double expected = 1.0;

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, Delta, "Bất kỳ số nào mũ 0 cũng phải bằng 1");
        }

        // TC_02: Kiểm tra trường hợp n dương (n > 0)
        [TestMethod]
        public void Test_Power_WhenExponentIsPositive_ShouldReturnCorrectPower()
        {
            // Arrange
            double x = 2.0;
            int n = 3;
            double expected = 8.0; // 2^3 = 8

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            // Test này sẽ FAILED vì code đề bài sai: Code tính ra 3*2*1 = 6, Expected là 8
            Assert.AreEqual(expected, actual, Delta, "2 mũ 3 phải bằng 8");
        }

        // TC_03: Kiểm tra trường hợp n âm (n < 0)
        [TestMethod]
        public void Test_Power_WhenExponentIsNegative_ShouldReturnReciprocal()
        {
            // Arrange
            double x = 2.0;
            int n = -2;
            double expected = 0.25; // 2^-2 = 1/4 = 0.25

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, Delta, "2 mũ -2 phải bằng 0.25");
        }

        // TC_04: Kiểm tra cơ số âm với số mũ chẵn
        [TestMethod]
        public void Test_Power_WhenBaseIsNegativeAndExponentEven_ShouldReturnPositive()
        {
            // Arrange
            double x = -2.0;
            int n = 2;
            double expected = 4.0; // (-2)^2 = 4

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, Delta, "(-2) mũ 2 phải bằng 4");
        }

        // TC_05: Kiểm tra cơ số âm với số mũ lẻ
        [TestMethod]
        public void Test_Power_WhenBaseIsNegativeAndExponentOdd_ShouldReturnNegative()
        {
            // Arrange
            double x = -2.0;
            int n = 3;
            double expected = -8.0; // (-2)^3 = -8

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, Delta, "(-2) mũ 3 phải bằng -8");
        }

        // TC_06: Kiểm tra với số thập phân
        [TestMethod]
        public void Test_Power_WhenBaseIsDecimal_ShouldCalculateCorrectly()
        {
            // Arrange
            double x = 0.5;
            int n = 2;
            double expected = 0.25; // 0.5^2 = 0.25

            // Act
            double actual = MathUtils.Power(x, n);

            // Assert
            Assert.AreEqual(expected, actual, Delta, "0.5 mũ 2 phải bằng 0.25");
        }
    }
}