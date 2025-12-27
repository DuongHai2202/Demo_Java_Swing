package repository.impl;

import database.DatabaseConnection;
import models.Course;
import models.CourseStatus;
import repository.ICourseRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Lớp triển khai cho ICourseRepository
 */
public class CourseRepositoryImpl implements ICourseRepository {
    public CourseRepositoryImpl() {
    }

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public List<Course> getAllActiveCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM tbl_courses WHERE status = 'Đang mở' ORDER BY course_name";

        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM tbl_courses ORDER BY created_at DESC";

        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> getCoursesByLevel(String level) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM tbl_courses WHERE level = ? AND status = 'Đang mở' ORDER BY course_name";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, level);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                courses.add(extractCourseFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course getCourseById(int id) {
        String sql = "SELECT * FROM tbl_courses WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCourseFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createCourse(Course course) {
        String sql = "INSERT INTO tbl_courses (course_code, course_name, description, level, duration_hours, fee, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getCourseCode());
            pstmt.setString(2, course.getCourseName());
            pstmt.setString(3, course.getDescription());
            pstmt.setString(4, course.getLevel());
            pstmt.setInt(5, course.getDurationHours());
            pstmt.setBigDecimal(6, course.getFee());
            pstmt.setString(7, course.getStatus().toString());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateCourse(Course course) {
        String sql = "UPDATE tbl_courses SET course_name = ?, description = ?, level = ?, duration_hours = ?, fee = ?, status = ? WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getDescription());
            pstmt.setString(3, course.getLevel());
            pstmt.setInt(4, course.getDurationHours());
            pstmt.setBigDecimal(5, course.getFee());
            pstmt.setString(6, course.getStatus().toString());
            pstmt.setInt(7, course.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCourse(int id) {
        String sql = "DELETE FROM tbl_courses WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Map<String, Integer> getCourseEnrollmentStats() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT c.course_name, COUNT(e.id) as student_count " +
                "FROM tbl_courses c " +
                "LEFT JOIN tbl_classes cl ON c.id = cl.course_id " +
                "LEFT JOIN tbl_enrollments e ON cl.id = e.class_id " +
                "GROUP BY c.id, c.course_name";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                stats.put(rs.getString("course_name"), rs.getInt("student_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setCourseCode(rs.getString("course_code"));
        course.setCourseName(rs.getString("course_name"));
        course.setDescription(rs.getString("description"));
        course.setLevel(rs.getString("level"));
        course.setDurationHours(rs.getInt("duration_hours"));
        course.setFee(rs.getBigDecimal("fee"));
        course.setStatus(CourseStatus.fromDisplayName(rs.getString("status")));
        course.setCreatedAt(rs.getTimestamp("created_at"));
        course.setUpdatedAt(rs.getTimestamp("updated_at"));
        return course;
    }
}
