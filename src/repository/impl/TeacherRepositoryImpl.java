package repository.impl;

import database.DatabaseConnection;
import models.Teacher;
import models.User;
import repository.ITeacherRepository;
import utils.enums.Gender;
import utils.enums.UserRole;
import utils.enums.UserStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TeacherRepositoryImpl - Implementation cho Teacher data access
 */
public class TeacherRepositoryImpl implements ITeacherRepository {

    @Override
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        String sql = "SELECT t.*, u.* FROM tbl_teachers t " +
                "INNER JOIN tbl_users u ON t.user_id = u.id " +
                "ORDER BY t.id DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                teachers.add(mapResultSetToTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    @Override
    public List<Teacher> getTeachersPaginated(int page, int size) {
        List<Teacher> teachers = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = "SELECT t.*, u.* FROM tbl_teachers t " +
                "INNER JOIN tbl_users u ON t.user_id = u.id " +
                "ORDER BY t.id DESC LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    teachers.add(mapResultSetToTeacher(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teachers;
    }

    @Override
    public int getTotalTeacherCount() {
        String sql = "SELECT COUNT(*) FROM tbl_teachers";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Teacher getTeacherById(int id) {
        String sql = "SELECT t.*, u.* FROM tbl_teachers t " +
                "INNER JOIN tbl_users u ON t.user_id = u.id " +
                "WHERE t.id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Teacher getTeacherByUserId(int userId) {
        String sql = "SELECT t.*, u.* FROM tbl_teachers t " +
                "INNER JOIN tbl_users u ON t.user_id = u.id " +
                "WHERE t.user_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTeacher(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createTeacher(Teacher teacher) {
        String sql = "INSERT INTO tbl_teachers (user_id, teacher_code, specialization, qualification, years_of_experience, bio) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, teacher.getUserId());
            pstmt.setString(2, teacher.getTeacherCode());
            pstmt.setString(3, teacher.getSpecialization());
            pstmt.setString(4, teacher.getQualification());
            pstmt.setInt(5, teacher.getYearsOfExperience());
            pstmt.setString(6, teacher.getBio());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        teacher.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        String sql = "UPDATE tbl_teachers SET teacher_code = ?, specialization = ?, " +
                "qualification = ?, years_of_experience = ?, bio = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, teacher.getTeacherCode());
            pstmt.setString(2, teacher.getSpecialization());
            pstmt.setString(3, teacher.getQualification());
            pstmt.setInt(4, teacher.getYearsOfExperience());
            pstmt.setString(5, teacher.getBio());
            pstmt.setInt(6, teacher.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteTeacher(int id) {
        String sql = "DELETE FROM tbl_teachers WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Map ResultSet sang Teacher object (bao gồm cả User info)
     */
    private Teacher mapResultSetToTeacher(ResultSet rs) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("t.id"));
        teacher.setUserId(rs.getInt("t.user_id"));
        teacher.setTeacherCode(rs.getString("t.teacher_code"));
        teacher.setSpecialization(rs.getString("t.specialization"));
        teacher.setQualification(rs.getString("t.qualification"));
        teacher.setYearsOfExperience(rs.getInt("t.years_of_experience"));
        teacher.setBio(rs.getString("t.bio"));

        // Map User info
        User user = new User();
        user.setId(rs.getInt("u.id"));
        user.setUsername(rs.getString("u.username"));
        user.setFullName(rs.getString("u.full_name"));
        user.setEmail(rs.getString("u.email"));
        user.setPhone(rs.getString("u.phone"));
        user.setRole(UserRole.fromDisplayName(rs.getString("u.role")));
        user.setGender(Gender.fromDisplayName(rs.getString("u.gender")));
        user.setDateOfBirth(rs.getDate("u.date_of_birth"));
        user.setAddress(rs.getString("u.address"));
        user.setStatus(UserStatus.fromDisplayName(rs.getString("u.status")));
        user.setCreatedAt(rs.getTimestamp("u.created_at"));
        user.setUpdatedAt(rs.getTimestamp("u.updated_at"));

        teacher.setUser(user);

        return teacher;
    }
}
