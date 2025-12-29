package repository.impl;

import database.DatabaseConnection;
import models.Attendance;
import repository.IAttendanceRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AttendanceRepositoryImpl - Implementation cho Attendance data access
 */
public class AttendanceRepositoryImpl implements IAttendanceRepository {

    @Override
    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.student_code, u.full_name as student_name " +
                "FROM tbl_attendance a " +
                "INNER JOIN tbl_students s ON a.student_id = s.id " +
                "INNER JOIN tbl_users u ON s.user_id = u.id " +
                "ORDER BY a.recorded_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                attendanceList.add(mapResultSetToAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceBySchedule(int scheduleId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.student_code, u.full_name as student_name " +
                "FROM tbl_attendance a " +
                "INNER JOIN tbl_students s ON a.student_id = s.id " +
                "INNER JOIN tbl_users u ON s.user_id = u.id " +
                "WHERE a.schedule_id = ? " +
                "ORDER BY u.full_name ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendanceList.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

    @Override
    public List<Attendance> getAttendanceByStudent(int studentId) {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT a.*, s.student_code, u.full_name as student_name " +
                "FROM tbl_attendance a " +
                "INNER JOIN tbl_students s ON a.student_id = s.id " +
                "INNER JOIN tbl_users u ON s.user_id = u.id " +
                "WHERE a.student_id = ? " +
                "ORDER BY a.recorded_at DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    attendanceList.add(mapResultSetToAttendance(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attendanceList;
    }

    @Override
    public boolean markAttendance(Attendance attendance) {
        String sql = "INSERT INTO tbl_attendance (schedule_id, student_id, status, note) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE status = ?, note = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, attendance.getScheduleId());
            pstmt.setInt(2, attendance.getStudentId());
            pstmt.setString(3, attendance.getStatus());
            pstmt.setString(4, attendance.getNote());
            pstmt.setString(5, attendance.getStatus());
            pstmt.setString(6, attendance.getNote());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0 && attendance.getId() == 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        attendance.setId(generatedKeys.getInt(1));
                    }
                }
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateAttendance(Attendance attendance) {
        String sql = "UPDATE tbl_attendance SET status = ?, note = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, attendance.getStatus());
            pstmt.setString(2, attendance.getNote());
            pstmt.setInt(3, attendance.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteAttendance(int id) {
        String sql = "DELETE FROM tbl_attendance WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean hasAttendance(int scheduleId, int studentId) {
        String sql = "SELECT COUNT(*) FROM tbl_attendance WHERE schedule_id = ? AND student_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);
            pstmt.setInt(2, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Chuyển đổi ResultSet sang đối tượng Attendance
     */
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setScheduleId(rs.getInt("schedule_id"));
        attendance.setStudentId(rs.getInt("student_id"));
        attendance.setStatus(rs.getString("status"));
        attendance.setNote(rs.getString("note"));
        attendance.setRecordedAt(rs.getTimestamp("recorded_at"));
        attendance.setStudentCode(rs.getString("student_code"));
        attendance.setStudentName(rs.getString("student_name"));

        return attendance;
    }
}
