using System;

namespace GeometryExercise
{
    public class Diem
    {
        public double X { get; set; }
        public double Y { get; set; }

        public Diem(double x, double y)
        {
            X = x;
            Y = y;
        }
    }

    public class HinhChuNhat
    {
        // Dùng Properties (Read-only) để lưu tọa độ đã chuẩn hóa
        public double XMin { get; private set; }
        public double YMin { get; private set; }
        public double XMax { get; private set; }
        public double YMax { get; private set; }

        public HinhChuNhat(Diem p1, Diem p2)
        {
            // [TC_HCN_003] Logic chuẩn hóa tọa độ:
            // Không quan trọng p1 hay p2 là góc nào, ta luôn tìm Min/Max
            XMin = Math.Min(p1.X, p2.X);
            XMax = Math.Max(p1.X, p2.X);
            YMin = Math.Min(p1.Y, p2.Y);
            YMax = Math.Max(p1.Y, p2.Y);
        }

        public double TinhDienTich()
        {
            // Diện tích = Chiều rộng * Chiều cao
            return (XMax - XMin) * (YMax - YMin);
        }

        public bool KiemTraGiaoNhau(HinhChuNhat other)
        {
            // [TC_HCN_008] Defensive Programming: Chặn lỗi Null
            if (other == null)
                throw new ArgumentNullException(nameof(other), "Hình chữ nhật so sánh không được null");

            // Logic kiểm tra giao nhau:
            // Hai hình KHÔNG giao nhau nếu chúng nằm tách biệt hoàn toàn về 1 phía
            bool isDisjoint = (this.XMax < other.XMin) || // Hình này nằm bên TRÁI hình kia
                              (this.XMin > other.XMax) || // Hình này nằm bên PHẢI hình kia
                              (this.YMax < other.YMin) || // Hình này nằm bên DƯỚI hình kia
                              (this.YMin > other.YMax);   // Hình này nằm bên TRÊN hình kia

            // Nếu không tách biệt (isDisjoint = false) thì tức là có giao nhau
            return !isDisjoint;
        }
    }
}