# 🎓 ODIN Language Center - Hệ Thống Quản Lý Trung Tâm Anh Ngữ

Ứng dụng Java Swing quản lý trung tâm tiếng Anh với 4 roles: Admin, Staff, Teacher, Student.

## ✨ Tính Năng Chính

### 👨‍💼 Admin Dashboard
- Quản lý người dùng (Staff, Teacher, Student)
- Quản lý khóa học & lớp học
- Quản lý giao dịch thanh toán
- Báo cáo & thống kê chi tiết
- Xuất báo cáo CSV

### 👥 Staff Dashboard
- Xử lý yêu cầu hỗ trợ
- Quản lý bài viết/thông báo
- Quản lý lịch học
- Điểm danh học viên

### 👨‍🏫 Teacher Dashboard
- Xem lớp được phân công
- Lịch giảng dạy
- **Điểm danh học viên** ✅
- Thống kê giảng dạy

### 🎓 Student Dashboard  
- Xem khóa học đã đăng ký
- Lịch sử thanh toán
- Quản lý hồ sơ cá nhân

## 🛠️ Tech Stack

- **Language:** Java 17+
- **UI Framework:** Swing với FlatLaf
- **Database:** MySQL 8.0
- **Architecture:** MVC Pattern
- **JDBC:** MySQL Connector 8.0.33

## 📁 Cấu Trúc Dự Án

```
src/
├── controller/          # Business logic controllers
│   ├── admin/          # Admin controllers
│   ├── student/        # Student controller
│   ├── teacher/        # Teacher controller
│   └── staff/          # Staff controller
├── models/             # Data models (User, Student, Course...)
├── repository/         # Data access layer
│   ├── impl/          # Repository implementations
│   └── I*Repository   # Repository interfaces
├── service/            # Business services
├── utils/              # Utilities (UIUtils, DatabaseConnection...)
├── view/               # UI components
│   ├── admin/         # Admin dashboard
│   ├── student/       # Student dashboard
│   ├── teacher/       # Teacher dashboard
│   └── staff/         # Staff dashboard
└── Main.java          # Application entry point

database/
└── schema.sql         # Database schema & sample data
```

## 🚀 Cài Đặt & Chạy

### 1. Yêu Cầu Hệ Thống
- JDK 17 trở lên
- MySQL 8.0+
- IDE: IntelliJ IDEA / Eclipse / VS Code

### 2. Cài Đặt Database

```bash
# Import schema vào MySQL
mysql -u root -p < database/schema.sql
```

### 3. Cấu Hình Kết Nối

Chỉnh `src/utils/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/english_center";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

### 4. Biên Dịch & Chạy

**Windows:**
```bash
# Compile
.\compile.bat

# Run
.\run.bat
```

**Manual:**
```bash
# Compile
javac -d bin -cp "lib/*" src/**/*.java

# Run  
java -cp "bin;lib/*" Main
```

## 👤 Tài Khoản Demo

| Role | Username | Password |
|------|----------|----------|
| Admin | admin1 | 123456 |
| Staff | staff1 | 123456 |
| Teacher | teacher1 | 123456 |
| Student | student1 | 123456 |

## 📊 Database Schema

**14 Tables:**
- `tbl_users` - Người dùng chính
- `tbl_students` - Thông tin học viên
- `tbl_teachers` - Thông tin giảng viên
- `tbl_staff` - Thông tin nhân viên
- `tbl_courses` - Khóa học
- `tbl_classes` - Lớp học
- `tbl_enrollments` - Đăng ký học
- `tbl_schedules` - Lịch học
- `tbl_attendance` - Điểm danh
- `tbl_transactions` - Giao dịch
- `tbl_posts` - Bài viết
- `tbl_support_requests` - Yêu cầu hỗ trợ
- `tbl_documents` - Tài liệu
- `tbl_ratings` - Đánh giá

## 🎨 UI Features

- Modern flat design với FlatLaf  
- Responsive layouts
- Vietnamese language support
- Consistent color scheme
- Professional dashboards
- Interactive tables & forms

## 🔐 Bảo Mật

- Password hashing (sẽ implement)
- Role-based access control
- Session management
- SQL injection prevention (PreparedStatement)

## 📝 Sample Data

Schema bao gồm:
- 35 users (admin, staff, teachers, students)
- 7 courses (IELTS, TOEIC, Communication)
- 4 active classes
- 39 enrollments
- 22 schedules
- 20+ transactions

## 🐛 Troubleshooting

**Lỗi kết nối database:**
```
Kiểm tra MySQL đang chạy
Kiểm tra username/password
Kiểm tra database đã import
```

**Compilation error:**
```
Kiểm tra JDK version >= 17
Kiểm tra lib/ có đủ jar files
```

## 📚 Development Notes

**Key Design Patterns:**
- MVC Architecture
- Repository Pattern
- Dependency Injection
- Observer Pattern (UI updates)

**Code Standards:**
- Vietnamese comments
- Consistent naming
- Clean code principles
- Proper error handling

## 🚧 Roadmap

- [ ] Implement password hashing
- [ ] Add email notifications
- [ ] Export reports to PDF
- [ ] Advanced analytics dashboard
- [ ] Mobile responsive design
- [ ] REST API layer

## 👨‍💻 Author

**ODIN Language Center Development Team**

## 📄 License

Educational Project - Free to use and modify

---

**Version:** 2.0  
**Last Updated:** 2025-12-29  
**Status:** Active Development ✅
