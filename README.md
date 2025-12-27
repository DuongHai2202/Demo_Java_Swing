# ODIN Language Center - Hệ Thống Quản Lý Trung Tâm Ngoại Ngữ

![Java](https://img.shields.io/badge/Java-17-orange)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![MySQL](https://img.shields.io/badge/Database-MySQL-blue)
![License](https://img.shields.io/badge/License-MIT-green)

Hệ thống quản lý toàn diện cho trung tâm ngoại ngữ, được xây dựng bằng Java Swing với kiến trúc MVC, hỗ trợ đầy đủ các chức năng quản lý học viên, giảng viên, nhân viên, khóa học, giao dịch và báo cáo thống kê.

## 📋 Mục Lục

- [Tính Năng](#-tính-năng)
- [Tech Stack](#-tech-stack)
- [Cài Đặt](#-cài-đặt)
- [Sử Dụng](#-sử-dụng)
- [Cấu Trúc Project](#-cấu-trúc-project)
- [Database Schema](#-database-schema)
- [Screenshots](#-screenshots)
- [Đóng Góp](#-đóng-góp)
- [License](#-license)

## ✨ Tính Năng

### 🔐 Xác Thực & Phân Quyền
- **Multi-role authentication**: Quản trị viên, Nhân viên, Giảng viên, Học viên
- **Role-based access control** (RBAC)
- **Demo accounts** để test nhanh
- **Password encryption** (có thể mở rộng)

### 👥 Quản Lý Người Dùng
- **Quản lý tài khoản**: CRUD operations cho tất cả users
- **Phân trang**: Hiển thị danh sách với pagination
- **Tìm kiếm & lọc**: Theo role, status, từ khóa
- **Quản lý trạng thái**: Đang hoạt động, Ngừng hoạt động, Tạm khóa

### 📚 Quản Lý Học Viên
- Thông tin cá nhân đầy đủ
- Mã học viên, trình độ hiện tại
- Lịch sử đăng ký khóa học
- Theo dõi tiến trình học tập

### 👨‍🏫 Quản Lý Nhân Viên & Giảng Viên
- **Staff**: Mã nhân viên, chức vụ, phòng ban, ngày vào làm
- **Teachers**: Chuyên môn, bằng cấp, kinh nghiệm, tiểu sử
- Quản lý thông tin chi tiết từng loại user

### 💰 Quản Lý Giao Dịch
- **Transaction types**: Học phí, Đăng ký, Hoàn tiền, Phí khác
- **Payment methods**: Tiền mặt, Chuyển khoản, Thẻ, Ví điện tử
- **Status tracking**: Đang chờ, Đã thanh toán, Thất bại, Hoàn tiền
- Tìm kiếm giao dịch theo từ khóa
- Báo cáo doanh thu

### 📊 Báo Cáo & Thống Kê
- **Thống kê tổng quan**: Số lượng học viên, giảng viên, khóa học
- **Doanh thu theo tháng**: Biểu đồ line chart
- **Thống kê theo loại giao dịch**: Pie chart
- **Hoạt động gần đây**: 10 transactions mới nhất

### 📝 Quản Lý Bài Viết & Hỗ Trợ
- **Posts**: Tạo, chỉnh sửa bài viết/thông báo
- **Support Requests**: Tiếp nhận và xử lý yêu cầu hỗ trợ
- Status tracking cho cả 2 modules

### 🎨 Giao Diện Người Dùng
- **Modern UI**: Sử dụng FlatLaf Look & Feel
- **Responsive design**: Split layout, GridLayout, BoxLayout
- **Clean aesthetics**: Gradient backgrounds, rounded corners, icons
- **Intuitive navigation**: Dashboard với sidebar menu
- **Drag-to-move**: Login window có thể kéo thả

## 🛠 Tech Stack

### Core Technologies
- **Java 17+**: Programming language
- **Swing**: GUI framework
- **JDBC**: Database connectivity
- **MySQL 8.0+**: Relational database

### Libraries & Frameworks
- **FlatLaf 3.4.1**: Modern Look & Feel
- **MySQL Connector/J 8.0.33**: JDBC driver

### Architecture
- **MVC Pattern**: Model-View-Controller
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic separation
- **DAO Pattern**: Database operations

### Code Quality
- **Vietnamese documentation**: Comprehensive Javadoc
- **Enum-based configuration**: Type-safe constants
- **Standardized code style**: Consistent across 73 files
- **Clean code principles**: SOLID, DRY, separation of concerns

## 📥 Cài Đặt

### Yêu Cầu Hệ Thống
- Java JDK 17 trở lên
- MySQL Server 8.0+
- IDE: IntelliJ IDEA, Eclipse, NetBeans (optional)

### Bước 1: Clone Repository
```bash
git clone https://github.com/yourusername/odin-language-center.git
cd odin-language-center
```

### Bước 2: Cấu Hình Database
1. Tạo database MySQL:
```sql
CREATE DATABASE odin_language_center CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Import schema và data:
```bash
mysql -u root -p odin_language_center < database/schema.sql
mysql -u root -p odin_language_center < database/sample_data.sql
```

3. Cập nhật thông tin kết nối trong `src/database/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/odin_language_center?useUnicode=true&characterEncoding=UTF-8";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

### Bước 3: Build & Run

#### Sử dụng Scripts (Recommended)
```bash
# Compile
.\compile.bat   # Windows
./compile.sh    # Linux/Mac

# Run
.\run.bat       # Windows
./run.sh        # Linux/Mac
```

#### Sử dụng IDE
1. Import project vào IDE
2. Add libraries: `lib/flatlaf-3.4.1.jar`, `lib/mysql-connector-j-8.0.33.jar`
3. Run `src/Main.java`

## 🚀 Sử Dụng

### Demo Accounts

| Role | Username | Password | Mô Tả |
|------|----------|----------|-------|
| Admin | `admin` | `123` | Quản trị viên - Full access |
| Staff | `staff1` | `123` | Nhân viên - Quản lý posts & support |
| Teacher | `teacher1` | `123` | Giảng viên - Xem thông tin |
| Student | `student1` | `123` | Học viên - Xem thông tin cá nhân |

### Quick Start
1. Chạy ứng dụng
2. Click vào demo link (Admin/Staff/Teacher/Student) để tự động điền
3. Click "ĐĂNG NHẬP"
4. Explore các tính năng theo role

### Luồng Sử Dụng Chính

#### Quản Trị Viên
1. **Dashboard** → Xem tổng quan hệ thống
2. **Quản Lí Học Viên** → Thêm/Sửa/Xóa học viên
3. **Quản Lí Nhân Viên** → Quản lý staff và teachers
4. **Quản Lí Giao Dịch** → Theo dõi payment
5. **Quản Lí Tài Khoản** → Quản lý users và permissions
6. **Báo Cáo - Thống Kê** → Xem reports và charts

#### Nhân Viên
1. **Bài Viết** → Tạo/chỉnh sửa thông báo
2. **Hỗ Trợ** → Xử lý support requests từ học viên

## 📁 Cấu Trúc Project

```
odin-language-center/
├── src/
│   ├── Main.java                    # Entry point
│   ├── controller/                  # Controllers (MVC)
│   │   ├── AuthController.java
│   │   └── admin/
│   │       ├── AccountController.java
│   │       ├── StaffController.java
│   │       ├── StudentController.java
│   │       ├── TransactionController.java
│   │       └── ReportingController.java
│   ├── models/                      # Domain models
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Staff.java
│   │   ├── Teacher.java
│   │   ├── Transaction.java
│   │   ├── Course.java
│   │   ├── Post.java
│   │   ├── SupportRequest.java
│   │   └── [12 enum types]
│   ├── repository/                  # Data access layer
│   │   ├── *.java                   # Interfaces (7 files)
│   │   └── impl/                    # Implementations (7 files)
│   ├── service/                     # Business logic layer
│   │   ├── UserService.java
│   │   ├── StaffService.java
│   │   ├── StudentService.java
│   │   ├── TransactionService.java
│   │   └── [3 more services]
│   ├── view/                        # UI layer (Swing)
│   │   ├── LoginFrame.java
│   │   ├── admin/
│   │   │   ├── AdminDashboard.java
│   │   │   ├── dialogs/             # 5 dialog files
│   │   │   └── panels/              # 6 panel files
│   │   ├── staff/                   # Staff UI
│   │   └── components/              # Reusable components
│   ├── database/                    # Database utilities
│   │   └── DatabaseConnection.java
│   └── utils/                       # Utility classes
│       └── UIUtils.java
├── lib/                             # External libraries
│   ├── flatlaf-3.4.1.jar
│   └── mysql-connector-j-8.0.33.jar
├── database/                        # SQL scripts
│   ├── schema.sql
│   └── sample_data.sql
├── compile.bat / compile.sh         # Build scripts
├── run.bat / run.sh                 # Run scripts
└── README.md
```

## 🗄 Database Schema

### Core Tables

#### `tbl_users`
- `id`, `username`, `password`, `role`, `full_name`, `email`, `phone`
- `gender`, `date_of_birth`, `address`, `status`
- `created_at`, `updated_at`

#### `tbl_students`
- `id`, `user_id` (FK), `student_code`, `current_level`

#### `tbl_staff`
- `id`, `user_id` (FK), `staff_code`, `position`, `department`, `hire_date`

#### `tbl_teachers`
- `id`, `user_id` (FK), `teacher_code`, `specialization`, `qualification`
- `years_of_experience`, `bio`

#### `tbl_transactions`
- `id`, `student_id` (FK), `enrollment_id`, `transaction_code`
- `amount`, `transaction_date`, `transaction_type`, `payment_method`
- `status`, `description`, `processed_by`

#### `tbl_posts`
- `id`, `title`, `content`, `author_id` (FK), `category`
- `status`, `published_at`, `created_at`, `updated_at`

#### `tbl_support_requests`
- `id`, `requester_id`, `requester_name`, `requester_email`
- `subject`, `message`, `status`, `assigned_to`, `created_at`, `updated_at`

### Relationships
- **1:1**: User ↔ Student/Staff/Teacher
- **1:N**: Student → Transactions
- **1:N**: User → Posts
- **1:N**: User → Support Requests

### Enum Values (Vietnamese)
- **Roles**: Quản trị viên, Nhân viên, Giảng viên, Học viên
- **User Status**: Đang hoạt động, Ngừng hoạt động, Tạm khóa
- **Transaction Types**: Học phí, Đăng ký, Hoàn tiền, Phí khác
- **Payment Methods**: Tiền mặt, Chuyển khoản, Thẻ, Ví điện tử
- **Transaction Status**: Đang chờ, Đã thanh toán, Thất bại, Hoàn tiền

## 📸 Screenshots

### Login Screen
- Modern split-screen design
- Gradient background
- Demo account quick access

### Admin Dashboard
- Statistical overview cards
- Clean navigation sidebar
- Multi-panel content area

### Management Panels
- Paginated tables
- Search & filter functionality
- CRUD dialogs

### Reports & Charts
- Line charts for revenue
- Pie charts for statistics
- Recent activity feed

## 🤝 Đóng Góp

Contributions are welcome! Để đóng góp:

1. Fork project
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

### Coding Standards
- Vietnamese Javadoc cho classes và methods quan trọng
- Simple comment style (không dùng section separators)
- Tuân thủ MVC architecture
- Follow existing code conventions

## 📝 License

Distributed under the MIT License. See `LICENSE` for more information.

## 👥 Authors

- **ODIN Development Team** - *Initial work*

## 🙏 Acknowledgments

- [FlatLaf](https://www.formdev.com/flatlaf/) - Modern Look & Feel
- [MySQL](https://www.mysql.com/) - Database
- Swing community for UI patterns

## 📞 Contact

Project Link: [https://github.com/yourusername/odin-language-center](https://github.com/yourusername/odin-language-center)

---

**⭐ Nếu project hữu ích, đừng quên star repo!**
