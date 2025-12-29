package repository.impl;

import database.DatabaseConnection;
import models.Student;
import models.User;
import utils.enums.UserRole;
import repository.IStudentRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai cho IStudentRepository
 */
public class StudentRepositoryImpl implements IStudentRepository {
    public StudentRepositoryImpl() {
    }

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean createStudent(Student student) {
        String sql = "INSERT INTO tbl_students (user_id, student_code, current_level) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, student.getUserId());
            pstmt.setString(2, student.getStudentCode());
            pstmt.setString(3, student.getCurrentLevel());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT s.*, u.* FROM tbl_students s JOIN tbl_users u ON s.user_id = u.id WHERE s.user_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> getStudentsPaginated(int page, int size) {
        List<Student> students = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT s.*, u.* FROM tbl_students s JOIN tbl_users u ON s.user_id = u.id ORDER BY u.full_name LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public int getTotalStudentCount() {
        String sql = "SELECT COUNT(*) FROM tbl_students";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean updateStudent(Student student) {
        String sql = "UPDATE tbl_students SET student_code = ?, current_level = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getCurrentLevel());
            pstmt.setInt(3, student.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getInt("s.id"));
        s.setUserId(rs.getInt("s.user_id"));
        s.setStudentCode(rs.getString("s.student_code"));
        s.setCurrentLevel(rs.getString("s.current_level"));

        User u = new User();
        u.setId(rs.getInt("u.id"));
        u.setUsername(rs.getString("u.username"));
        u.setRole(UserRole.HOC_VIEN);
        u.setFullName(rs.getString("u.full_name"));
        u.setEmail(rs.getString("u.email"));
        u.setPhone(rs.getString("u.phone"));
        u.setAddress(rs.getString("u.address"));
        u.setGender(utils.enums.Gender.fromDisplayName(rs.getString("u.gender")));
        u.setDateOfBirth(rs.getDate("u.date_of_birth"));
        u.setStatus(utils.enums.UserStatus.fromDisplayName(rs.getString("u.status")));

        s.setUser(u);
        return s;
    }
}
