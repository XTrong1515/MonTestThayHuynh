using System;
using System.Collections.Generic;
using System.Linq;

namespace StudentManagement
{
    public class HocVien
    {
        public string MaSo { get; set; }
        public string HoTen { get; set; }
        public double DiemMon1 { get; set; }
        public double DiemMon2 { get; set; }
        public double DiemMon3 { get; set; }

        public HocVien(string maSo, string hoTen, double d1, double d2, double d3)
        {
            // [TC_HV_008, 009] Kiểm tra tính hợp lệ của điểm
            if (d1 < 0 || d1 > 10 || d2 < 0 || d2 > 10 || d3 < 0 || d3 > 10)
                throw new ArgumentException("Điểm số phải từ 0 đến 10");

            MaSo = maSo;
            HoTen = hoTen;
            DiemMon1 = d1;
            DiemMon2 = d2;
            DiemMon3 = d3;
        }

        public double TinhDiemTrungBinh()
        {
            return (DiemMon1 + DiemMon2 + DiemMon3) / 3.0;
        }

        public bool KiemTraHocBong()
        {
            double dtb = TinhDiemTrungBinh();

            // Điều kiện 1: ĐTB >= 8.0
            if (dtb < 8.0) return false;

            // Điều kiện 2: Không môn nào < 5.0
            // [TC_HV_003] Đây là chỗ Developer hay quên nhất!
            if (DiemMon1 < 5.0 || DiemMon2 < 5.0 || DiemMon3 < 5.0)
                return false;

            return true;
        }
    }
}