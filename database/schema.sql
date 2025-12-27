-- ================================================
-- SCHEMA CHO HỆ THỐNG QUẢN LÝ TRUNG TÂM NGOẠI NGỮ
-- ================================================

-- Database: english_center
-- Encoding: UTF8

DROP DATABASE IF EXISTS english_center;
CREATE DATABASE english_center CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE english_center;
SET NAMES utf8mb4;

-- ================================================
-- TABLE: tbl_users (Bảng người dùng chính)
-- ================================================
CREATE TABLE tbl_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Quản trị viên', 'Nhân viên', 'Giảng viên', 'Học viên', 'Khách') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20),
    address VARCHAR(255),
    gender ENUM('Nam', 'Nữ', 'Khác'),
    date_of_birth DATE,
    status ENUM('Đang hoạt động', 'Ngừng hoạt động', 'Tạm khóa') DEFAULT 'Đang hoạt động',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_students (Bảng học viên)
-- ================================================
CREATE TABLE tbl_students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    student_code VARCHAR(20) UNIQUE,
    current_level VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES tbl_users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_teachers (Bảng giảng viên)
-- ================================================
CREATE TABLE tbl_teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    teacher_code VARCHAR(20) UNIQUE,
    specialization VARCHAR(100),
    qualification VARCHAR(100),
    years_of_experience INT DEFAULT 0,
    bio TEXT,
    FOREIGN KEY (user_id) REFERENCES tbl_users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_staff (Bảng nhân viên)
-- ================================================
CREATE TABLE tbl_staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    staff_code VARCHAR(20) UNIQUE,
    position VARCHAR(100),
    department VARCHAR(100),
    hire_date DATE,
    FOREIGN KEY (user_id) REFERENCES tbl_users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_courses (Bảng khóa học)
-- ================================================
CREATE TABLE tbl_courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    description TEXT,
    level VARCHAR(20),
    duration_hours INT,
    fee DECIMAL(10, 2),
    status ENUM('Đang mở', 'Đã đóng') DEFAULT 'Đang mở',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_level (level),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_classes (Bảng lớp học)
-- ================================================
CREATE TABLE tbl_classes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    teacher_id INT,
    class_code VARCHAR(20) UNIQUE,
    class_name VARCHAR(100) NOT NULL,
    schedule VARCHAR(100),
    start_date DATE,
    end_date DATE,
    max_students INT DEFAULT 30,
    current_students INT DEFAULT 0,
    status ENUM('Sắp mở', 'Đang mở lớp', 'Đã kết thúc', 'Đã hủy') DEFAULT 'Sắp mở',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES tbl_courses(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES tbl_teachers(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_course_id (course_id),
    INDEX idx_teacher_id (teacher_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_enrollments (Bảng đăng ký học)
-- ================================================
CREATE TABLE tbl_enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    enroll_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('Đang chờ', 'Đã xác nhận', 'Đã hủy', 'Đã hoàn thành') DEFAULT 'Đang chờ',
    payment_status ENUM('Chưa thanh toán', 'Thanh toán một phần', 'Đã thanh toán') DEFAULT 'Chưa thanh toán',
    grade DECIMAL(5, 2),
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES tbl_students(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id) REFERENCES tbl_classes(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, class_id),
    INDEX idx_student_id (student_id),
    INDEX idx_class_id (class_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_transactions (Bảng giao dịch thanh toán)
-- ================================================
CREATE TABLE tbl_transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    enrollment_id INT,
    transaction_code VARCHAR(50) UNIQUE,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_type ENUM('Học phí', 'Phí ghi danh', 'Giáo trình', 'Hoàn tiền', 'Khác') DEFAULT 'Học phí',
    payment_method ENUM('Tiền mặt', 'Chuyển khoản', 'Thẻ tín dụng', 'Ví điện tử') DEFAULT 'Tiền mặt',
    status ENUM('Đang chờ', 'Thành công', 'Thất bại', 'Đã hoàn tiền') DEFAULT 'Đang chờ',
    description TEXT,
    processed_by INT,
    FOREIGN KEY (student_id) REFERENCES tbl_students(id) ON DELETE CASCADE,
    FOREIGN KEY (enrollment_id) REFERENCES tbl_enrollments(id) ON DELETE SET NULL,
    FOREIGN KEY (processed_by) REFERENCES tbl_users(id) ON DELETE SET NULL,
    INDEX idx_student_id (student_id),
    INDEX idx_status (status),
    INDEX idx_transaction_date (transaction_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_posts (Bảng bài viết/tin tức)
-- ================================================
CREATE TABLE tbl_posts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    author_id INT NOT NULL,
    category VARCHAR(50),
    status ENUM('Bản nháp', 'Đã đăng', 'Lưu trữ') DEFAULT 'Bản nháp',
    published_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES tbl_users(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_published_at (published_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_support_requests (Bảng yêu cầu hỗ trợ)
-- ================================================
CREATE TABLE tbl_support_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    requester_id INT,
    requester_name VARCHAR(100),
    requester_email VARCHAR(100),
    requester_phone VARCHAR(20),
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    status ENUM('Mới', 'Đang xử lý', 'Đã giải quyết', 'Đã đóng') DEFAULT 'Mới',
    assigned_to INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES tbl_users(id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_to) REFERENCES tbl_users(id) ON DELETE SET NULL,
    INDEX idx_status (status),
    INDEX idx_assigned_to (assigned_to)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLE: tbl_ratings (Bảng đánh giá khóa học)
-- ================================================
CREATE TABLE tbl_ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    review TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES tbl_students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES tbl_courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_rating (student_id, course_id),
    INDEX idx_course_id (course_id),
    INDEX idx_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- INSERT DỮ LIỆU MẪU (BẢN CHUẨN 35 TÀI KHOẢN)
-- ================================================

-- 1. ADMIN & STAFF (5 users: IDs 1-5)
INSERT INTO tbl_users (username, password, role, full_name, email, phone, address, gender, date_of_birth, status) VALUES
('admin', '123456', 'Quản trị viên', 'Quản Trị Viên', 'admin@odin.edu.vn', '0901234567', 'Hà Nội', 'Nam', '1990-01-01', 'Đang hoạt động'),
('staff1', '123456', 'Nhân viên', 'Nguyễn Văn An', 'an.nv@odin.edu.vn', '0902234567', 'Hải Phòng', 'Nam', '1995-05-10', 'Đang hoạt động'),
('staff2', '123456', 'Nhân viên', 'Trần Thị Bình', 'binh.tt@odin.edu.vn', '0903234567', 'Đà Nẵng', 'Nữ', '1996-08-15', 'Đang hoạt động'),
('staff3', '123456', 'Nhân viên', 'Lê Hoàng Long', 'long.lh@odin.edu.vn', '0904234568', 'Cần Thơ', 'Nam', '1994-11-20', 'Đang hoạt động'),
('staff4', '123456', 'Nhân viên', 'Vũ Thùy Linh', 'linh.vt@odin.edu.vn', '0905234569', 'Quảng Ninh', 'Nữ', '1997-03-30', 'Đang hoạt động');

-- 2. TEACHERS (8 users: IDs 6-13)
INSERT INTO tbl_users (username, password, role, full_name, email, phone, address, gender, date_of_birth, status) VALUES
('teacher1', '123456', 'Giảng viên', 'Phạm Minh Cường', 'cuong.pm@odin.edu.vn', '0904234567', 'Hà Nội', 'Nam', '1985-06-25', 'Đang hoạt động'),
('teacher2', '123456', 'Giảng viên', 'Trần Thu Hương', 'huong.tt@odin.edu.vn', '0905234567', 'TP.HCM', 'Nữ', '1988-12-12', 'Đang hoạt động'),
('teacher3', '123456', 'Giảng viên', 'Nguyễn Văn Hùng', 'hung.nv@odin.edu.vn', '0906234567', 'Hải Phòng', 'Nam', '1982-04-10', 'Đang hoạt động'),
('teacher4', '123456', 'Giảng viên', 'Lê Thị Lan', 'lan.lt@odin.edu.vn', '0907234568', 'Đà Nẵng', 'Nữ', '1990-09-18', 'Đang hoạt động'),
('teacher5', '123456', 'Giảng viên', 'Hoàng Đức Minh', 'minh.hd@odin.edu.vn', '0908234569', 'Cần Thơ', 'Nam', '1986-01-30', 'Đang hoạt động'),
('teacher6', '123456', 'Giảng viên', 'Hoàng Xuân Vinh', 'vinh.hx@odin.edu.vn', '0909234570', 'Hà Nội', 'Nam', '1984-07-22', 'Đang hoạt động'),
('teacher7', '123456', 'Giảng viên', 'Nguyễn Mai Anh', 'anh.nm@odin.edu.vn', '0910234571', 'Huế', 'Nữ', '1991-02-14', 'Đang hoạt động'),
('teacher8', '123456', 'Giảng viên', 'Vũ Thị Ngọc', 'ngoc.vt@odin.edu.vn', '0911234572', 'Quảng Ninh', 'Nữ', '1987-11-05', 'Đang hoạt động');

INSERT INTO tbl_teachers (user_id, teacher_code, specialization, qualification, years_of_experience, bio) VALUES
(6, 'T001', 'IELTS', 'TESOL, CELTA', 8, 'Chuyên về Writing và Speaking, giàu kinh nghiệm.'),
(7, 'T002', 'TOEIC', 'TESOL', 5, 'Chuyên gia chiến lược đạt điểm cao.'),
(8, 'T003', 'Communication', 'BA English', 3, 'Nhiệt tình, phương pháp giảng dạy tương tác.'),
(9, 'T004', 'Grammar', 'MA Education', 10, 'Giảng viên dày dặn kinh nghiệm ngữ pháp.'),
(10, 'T005', 'Business English', 'MBA', 7, 'Chuyên đào tạo doanh nghiệp.'),
(11, 'T006', 'Vocabulary', 'BA English', 4, 'Tỉ mỉ trong việc mở rộng vốn từ.'),
(12, 'T007', 'IELTS Speaking', 'TESOL', 6, 'Đam mê nâng cao khả năng giao tiếp.'),
(13, 'T008', 'TOEFL', 'MA TESOL', 9, 'Chuyên gia luyện thi TOEFL toàn diện.');

-- Staff records
INSERT INTO tbl_staff (user_id, staff_code, position, department, hire_date) VALUES
(2, 'ST001', 'Nhân viên lễ tân', 'Bộ phận lễ tân', '2020-03-01'),
(3, 'ST002', 'Phối hợp học vụ', 'Bộ phận học vụ', '2019-06-15'),
(4, 'ST003', 'Kế toán viên', 'Bộ phận tài chính', '2021-01-10'),
(5, 'ST004', 'Nhân viên IT', 'Bộ phận công nghệ', '2022-02-20');

-- 3. STUDENTS (22 users: IDs 14-35)
INSERT INTO tbl_users (username, password, role, full_name, email, phone, address, gender, date_of_birth, status) VALUES
('student1', '123456', 'Học viên', 'Lê Văn Dũng', 'dung.lv@gmail.com', '0907234567', 'Hà Nội', 'Nam', '2000-05-15', 'Đang hoạt động'),
('student2', '123456', 'Học viên', 'Hoàng Thị Hoa', 'hoa.ht@gmail.com', '0908234567', 'Hải Phòng', 'Nữ', '1999-08-20', 'Đang hoạt động'),
('student3', '123456', 'Học viên', 'Vũ Minh Tuấn', 'tuan.vm@gmail.com', '0909234567', 'Đà Nẵng', 'Nam', '2001-03-10', 'Đang hoạt động'),
('student4', '123456', 'Học viên', 'Phạm Bảo Ngọc', 'ngoc.pb@gmail.com', '0910234568', 'Cần Thơ', 'Nữ', '2002-11-25', 'Đang hoạt động'),
('student5', '123456', 'Học viên', 'Đỗ Thành Nam', 'nam.dt@gmail.com', '0911234569', 'Quảng Ninh', 'Nam', '2003-01-30', 'Đang hoạt động'),
('student6', '123456', 'Học viên', 'Trịnh Thu Trang', 'trang.tt@gmail.com', '0912234570', 'Nam Định', 'Nữ', '2000-12-12', 'Đang hoạt động'),
('student7', '123456', 'Học viên', 'Ngô Quang Huy', 'huy.nq@gmail.com', '0913234571', 'Bắc Ninh', 'Nam', '1998-04-05', 'Đang hoạt động'),
('student8', '123456', 'Học viên', 'Lý Mỹ Linh', 'linh.lm@gmail.com', '0914234572', 'Lạng Sơn', 'Nữ', '2001-07-22', 'Đang hoạt động'),
('student9', '123456', 'Học viên', 'Bùi Xuân Khánh', 'khanh.bx@gmail.com', '0915234573', 'Thái Bình', 'Nam', '2002-09-18', 'Đang hoạt động'),
('student10', '123456', 'Học viên', 'Tô Lan Hương', 'huong.tl@gmail.com', '0916234574', 'Hòa Bình', 'Nữ', '1997-02-14', 'Đang hoạt động'),
('student11', '123456', 'Học viên', 'Nguyễn Thế Vinh', 'vinh.nt@gmail.com', '0917234575', 'Thanh Hóa', 'Nam', '2000-06-25', 'Đang hoạt động'),
('student12', '123456', 'Học viên', 'Đặng Phương Thảo', 'thao.dp@gmail.com', '0918234576', 'Nghệ An', 'Nữ', '2003-10-05', 'Đang hoạt động'),
('student13', '123456', 'Học viên', 'Hồ Sỹ Hùng', 'hung.hs@gmail.com', '0919234577', 'Hà Tĩnh', 'Nam', '2001-12-25', 'Đang hoạt động'),
('student14', '123456', 'Học viên', 'Cao Thanh Tùng', 'tung.ct@gmail.com', '0920234578', 'Quảng Bình', 'Nam', '1999-03-30', 'Đang hoạt động'),
('student15', '123456', 'Học viên', 'Mai Tuyết Nhi', 'nhi.mt@gmail.com', '0921234579', 'Long An', 'Nữ', '2004-05-20', 'Đang hoạt động'),
('student16', '123456', 'Học viên', 'Dương Quốc Anh', 'anh.dq@gmail.com', '0922234580', 'Tiền Giang', 'Nam', '2002-08-15', 'Đang hoạt động'),
('student17', '123456', 'Học viên', 'Phan Gia Hân', 'han.pg@gmail.com', '0923234581', 'Bình Dương', 'Nữ', '2001-01-10', 'Đang hoạt động'),
('student18', '123456', 'Học viên', 'Trần Hữu Phước', 'phuoc.th@gmail.com', '0924234582', 'Đồng Nai', 'Nam', '2000-04-22', 'Đang hoạt động'),
('student19', '123456', 'Học viên', 'Lê Kim Liên', 'lien.lk@gmail.com', '0925234583', 'Tây Ninh', 'Nữ', '2003-09-12', 'Đang hoạt động'),
('student20', '123456', 'Học viên', 'Vương Đình Huệ', 'hue.vd@gmail.com', '0926234584', 'Cà Mau', 'Nữ', '1998-11-05', 'Đang hoạt động'),
('student21', '123456', 'Học viên', 'Nguyễn Tấn Dũng', 'dung.nt@gmail.com', '0927234585', 'Kiên Giang', 'Nam', '2000-05-15', 'Đang hoạt động'),
('student22', '123456', 'Học viên', 'Trương Tấn Sang', 'sang.tt@gmail.com', '0928234586', 'Bến Tre', 'Nam', '1999-08-20', 'Đang hoạt động');

INSERT INTO tbl_students (user_id, student_code, current_level) VALUES
(14, 'S001', 'Trung cấp'), (15, 'S002', 'Sơ cấp'), (16, 'S003', 'Nâng cao'), 
(17, 'S004', 'Sơ cấp'), (18, 'S005', 'Trung cấp'), (19, 'S006', 'Sơ cấp'),
(20, 'S007', 'Trung cấp'), (21, 'S008', 'Nâng cao'), (22, 'S009', 'Trung cấp'),
(23, 'S010', 'Sơ cấp'), (24, 'S011', 'Trung cấp'), (25, 'S012', 'Sơ cấp'),
(26, 'S013', 'Nâng cao'), (27, 'S014', 'Trung cấp'), (28, 'S015', 'Sơ cấp'),
(29, 'S016', 'Trung cấp'), (30, 'S017', 'Sơ cấp'), (31, 'S018', 'Nâng cao'),
(32, 'S019', 'Trung cấp'), (33, 'S020', 'Sơ cấp'), (34, 'S021', 'Trung cấp'),
(35, 'S022', 'Nâng cao');

-- 4. COURSES (7 Entries)
INSERT INTO tbl_courses (course_code, course_name, description, level, duration_hours, fee, status) VALUES
('IELTS-B', 'IELTS Basic', 'Nền tảng IELTS cho người mới', 'Sơ cấp', 60, 5000000, 'Đang mở'),
('IELTS-I', 'IELTS Intermediate', 'Xây dựng kỹ năng 5.0-6.0', 'Trung cấp', 80, 7000000, 'Đang mở'),
('IELTS-A', 'IELTS Advanced', 'Phá đảo 6.5-7.5+', 'Nâng cao', 100, 10000000, 'Đang mở'),
('TOEIC-450', 'TOEIC Target 450', 'Luyện thi TOEIC cơ bản', 'Sơ cấp', 48, 3500000, 'Đang mở'),
('TOEIC-650', 'TOEIC Target 650', 'Luyện thi TOEIC trung cấp', 'Trung cấp', 56, 4500000, 'Đang mở'),
('COMM-BEG', 'English Communication Beg', 'Giao tiếp cho người mất gốc', 'Sơ cấp', 40, 3000000, 'Đang mở'),
('COMM-ADV', 'Business English', 'Tiếng Anh thương mại chuyên sâu', 'Nâng cao', 40, 6000000, 'Đang mở');

-- 5. CLASSES (4 Entries)
INSERT INTO tbl_classes (course_id, teacher_id, class_code, class_name, schedule, start_date, end_date, max_students, current_students, status) VALUES
(1, 1, 'C1-ILB', 'IELTS Basic Morning', 'T2-4-6 (08:00-10:00)', '2024-10-01', '2024-12-30', 20, 5, 'Đã kết thúc'),
(2, 2, 'C2-ILI', 'IELTS Intermediate Night', 'T3-5-7 (18:00-20:00)', '2024-11-15', '2025-02-15', 25, 10, 'Đang mở lớp'),
(3, 3, 'C3-ILA', 'IELTS Advanced intensive', 'T2-4-6 (19:00-21:00)', '2025-01-05', '2025-04-05', 15, 8, 'Đang mở lớp'),
(6, 5, 'C4-COM', 'Comm Basic Weekends', 'T7-CN (09:00-11:00)', '2024-12-01', '2025-02-01', 20, 7, 'Đang mở lớp');

-- 6. ENROLLMENTS (6 Entries)
INSERT INTO tbl_enrollments (student_id, class_id, enroll_date, status, payment_status) VALUES
(1, 1, '2024-09-20', 'Đã hoàn thành', 'Đã thanh toán'),
(2, 1, '2024-09-22', 'Đã hoàn thành', 'Đã thanh toán'),
(3, 2, '2024-11-01', 'Đã xác nhận', 'Đã thanh toán'),
(4, 2, '2024-11-05', 'Đã xác nhận', 'Thanh toán một phần'),
(10, 3, '2024-12-20', 'Đã xác nhận', 'Đã thanh toán'),
(15, 3, '2024-12-22', 'Đã xác nhận', 'Chưa thanh toán');

-- 7. TRANSACTIONS (20 records cho lịch sử giao dịch)
INSERT INTO tbl_transactions (student_id, enrollment_id, transaction_code, amount, transaction_date, transaction_type, payment_method, status, description, processed_by) VALUES
(1, 1, 'TXN-0901', 5000000, '2024-09-20 10:00:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí IELTS Basic', 1),
(2, 1, 'TXN-0902', 5000000, '2024-09-25 14:30:00', 'Học phí', 'Tiền mặt', 'Thành công', 'Học phí IELTS Basic', 1),
(5, NULL, 'TXN-0903', 1200000, '2024-09-28 09:15:00', 'Giáo trình', 'Ví điện tử', 'Thành công', 'Mua giáo trình', 1),
(3, 2, 'TXN-1001', 7000000, '2024-10-10 16:00:00', 'Học phí', 'Thẻ tín dụng', 'Thành công', 'Học phí IELTS Intermediate', 1),
(7, NULL, 'TXN-1002', 3000000, '2024-10-15 11:00:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí Communication Basic', 1),
(8, NULL, 'TXN-1101', 4500000, '2024-11-05 09:00:00', 'Học phí', 'Ví điện tử', 'Thành công', 'Học phí TOEIC 650', 1),
(4, 2, 'TXN-1102', 3500000, '2024-11-12 15:20:00', 'Học phí', 'Tiền mặt', 'Thành công', 'Học phí TOEIC 450 (trả góp)', 1),
(12, NULL, 'TXN-1103', 6000000, '2024-11-20 10:45:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí Business English', 1),
(10, 3, 'TXN-1201', 10000000, '2024-12-01 08:30:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí IELTS Advanced', 1),
(2, 1, 'TXN-1202', 7000000, '2024-12-10 14:00:00', 'Học phí', 'Thẻ tín dụng', 'Thành công', 'Học phí IELTS Intermediate', 1),
(15, 3, 'TXN-1203', 500000, '2024-12-15 09:10:00', 'Phí ghi danh', 'Tiền mặt', 'Thành công', 'Phí ghi danh', 1),
(18, NULL, 'TXN-0101', 10000000, '2025-01-05 10:00:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí IELTS Advanced', 1),
(22, NULL, 'TXN-0102', 4500000, '2025-01-12 16:45:00', 'Học phí', 'Ví điện tử', 'Thành công', 'Học phí TOEIC 650', 1),
(5, NULL, 'TXN-0103', 3000000, '2025-01-20 11:30:00', 'Học phí', 'Tiền mặt', 'Thành công', 'Học phí Communication Basic', 1),
(11, NULL, 'TXN-0201', 7000000, '2025-02-02 10:00:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí IELTS Intermediate', 1),
(14, NULL, 'TXN-0202', 5000000, '2025-02-10 15:30:00', 'Học phí', 'Thẻ tín dụng', 'Thành công', 'Học phí IELTS Basic', 1),
(21, NULL, 'TXN-0203', 10000000, '2025-02-15 09:45:00', 'Học phí', 'Chuyển khoản', 'Thành công', 'Học phí IELTS Advanced', 1),
(6, NULL, 'TXN-0204', 3500000, '2025-02-18 11:20:00', 'Học phí', 'Ví điện tử', 'Thành công', 'Học phí TOEIC 450', 1),
(9, NULL, 'TXN-0205', 4500000, '2025-02-21 16:10:00', 'Học phí', 'Tiền mặt', 'Thành công', 'Học phí TOEIC 650', 1);

-- 8. BÀI VIẾT & HỖ TRỢ
INSERT INTO tbl_posts (title, content, author_id, category, status, published_at) VALUES
('Chào mừng đến với ODIN', 'Hệ thống quản lý trung tâm Anh ngữ chuyên nghiệp.', 1, 'Tin tức', 'Đã đăng', NOW()),
('Tips học Reading', 'Chia sẻ bí kíp đạt điểm tối đa phần thi Reading.', 6, 'Bí quyết', 'Đã đăng', NOW());

INSERT INTO tbl_support_requests (requester_name, requester_email, requester_phone, subject, message, status) VALUES
('Khách hàng A', 'guest.a@hmail.com', '0988777666', 'Tư vấn khóa SAT', 'Tôi muốn biết lộ trình học SAT.', 'Mới');

COMMIT;
