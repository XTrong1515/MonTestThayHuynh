using Microsoft.VisualStudio.TestTools.UnitTesting;
using System;
using System.Collections.Generic;
using System.Linq; // Cần thư viện này cho List và Reverse

namespace RadixConversionTests
{
    // ==========================================
    // PHẦN CODE CỦA BÀI TẬP (GIỮ NGUYÊN)
    // ==========================================
    public class Radix
    {
        private int number;

        public Radix(int number)
        {
            if (number < 0)
                throw new ArgumentException("Incorrect Value");
            this.number = number;
        }

        public string ConvertDecimalToAnother(int radix = 2)
        {
            int n = this.number;

            if (radix < 2 || radix > 16)
                throw new ArgumentException("Invalid Radix");

            List<string> result = new List<string>();
            while (n > 0)
            {
                int value = n % radix;
                if (value < 10)
                    result.Add(value.ToString());
                else
                {
                    switch (value)
                    {
                        case 10: result.Add("A"); break;
                        case 11: result.Add("B"); break;
                        case 12: result.Add("C"); break;
                        case 13: result.Add("D"); break;
                        case 14: result.Add("E"); break;
                        case 15: result.Add("F"); break;
                    }
                }
                n /= radix;
            }

            result.Reverse();
            return String.Join("", result.ToArray());
        }
    }

    // ==========================================
    // PHẦN UNIT TEST CỦA TESTER
    // ==========================================
 // Nhớ using namespace chứa class Radix

namespace RadixTests
    {
        [TestClass]
        public class RadixTests
        {
            // TC_RAD_001: Chuyển đổi sang Nhị phân (Binary)
            [TestMethod]
            public void Test_Convert_ToBinary_ReturnsCorrectString()
            {
                Radix r = new Radix(10);
                string result = r.ConvertDecimalToAnother(2);
                Assert.AreEqual("1010", result);
            }

            // TC_RAD_002: Chuyển đổi sang Bát phân (Octal)
            [TestMethod]
            public void Test_Convert_ToOctal_ReturnsCorrectString()
            {
                Radix r = new Radix(8);
                string result = r.ConvertDecimalToAnother(8);
                Assert.AreEqual("10", result);
            }

            // TC_RAD_003: Chuyển đổi sang Hex (Base 16) - Số lớn
            [TestMethod]
            public void Test_Convert_ToHex_LargeNumber_ReturnsFF()
            {
                Radix r = new Radix(255);
                string result = r.ConvertDecimalToAnother(16);
                Assert.AreEqual("FF", result);
            }

            // TC_RAD_004: Chuyển đổi sang Hex - Số 10 thành A
            [TestMethod]
            public void Test_Convert_ToHex_ValueTen_ReturnsA()
            {
                Radix r = new Radix(10);
                string result = r.ConvertDecimalToAnother(16);
                Assert.AreEqual("A", result);
            }

            // TC_RAD_005: Ngoại lệ khi nhập số âm vào Constructor
            [TestMethod]
            public void Test_Constructor_NegativeNumber_ThrowsException()
            {
                try
                {
                    new Radix(-5);
                    Assert.Fail("Test Fail: Không bắt được lỗi số âm.");
                }
                catch (ArgumentException ex)
                {
                    Assert.AreEqual("Incorrect Value", ex.Message);
                }
            }

            // TC_RAD_006: Ngoại lệ khi Radix < 2
            [TestMethod]
            public void Test_Convert_RadixTooSmall_ThrowsException()
            {
                try
                {
                    Radix r = new Radix(10);
                    r.ConvertDecimalToAnother(1);
                    Assert.Fail("Test Fail: Không bắt được lỗi Radix < 2.");
                }
                catch (ArgumentException ex)
                {
                    Assert.AreEqual("Invalid Radix", ex.Message);
                }
            }

            // TC_RAD_007: Ngoại lệ khi Radix > 16
            [TestMethod]
            public void Test_Convert_RadixTooLarge_ThrowsException()
            {
                try
                {
                    Radix r = new Radix(10);
                    r.ConvertDecimalToAnother(17);
                    Assert.Fail("Test Fail: Không bắt được lỗi Radix > 16.");
                }
                catch (ArgumentException ex)
                {
                    Assert.AreEqual("Invalid Radix", ex.Message);
                }
            }

            // TC_RAD_008: Kiểm tra số 0 
            [TestMethod]
            public void Test_Convert_ZeroInput_ReturnsZeroString()
            {
                // Arrange
                Radix r = new Radix(0);

                // Act
                string result = r.ConvertDecimalToAnother(2);

                // Assert
                // Code cũ trả về "" (Fail). Code mới trả về "0" (Pass).
                Assert.AreEqual("0", result, "Số 0 phải chuyển đổi thành chuỗi '0'");
            }
        }
    }
}