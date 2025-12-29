package repository.impl;

import database.DatabaseConnection;
import models.ClassSchedule;
import repository.IScheduleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ScheduleRepositoryImpl - Implementation cho Schedule data access
 */
public class ScheduleRepositoryImpl implements IScheduleRepository {

    @Override
    public List<ClassSchedule> getAllSchedules() {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name as teacher_name " +
                "FROM tbl_schedules s " +
                "LEFT JOIN tbl_users u ON s.teacher_id = u.id " +
                "ORDER BY s.schedule_date DESC, s.start_time ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    @Override
    public List<ClassSchedule> getTodaySchedules() {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name as teacher_name " +
                "FROM tbl_schedules s " +
                "LEFT JOIN tbl_users u ON s.teacher_id = u.id " +
                "WHERE s.schedule_date = CURDATE() " +
                "ORDER BY s.start_time ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                schedules.add(mapResultSetToSchedule(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    @Override
    public List<ClassSchedule> getSchedulesByDate(Date date) {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT s.*, u.full_name as teacher_name " +
                "FROM tbl_schedules s " +
                "LEFT JOIN tbl_users u ON s.teacher_id = u.id " +
                "WHERE s.schedule_date = ? " +
                "ORDER BY s.start_time ASC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, date);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToSchedule(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }

    @Override
    public ClassSchedule getScheduleById(int id) {
        String sql = "SELECT s.*, u.full_name as teacher_name " +
                "FROM tbl_schedules s " +
                "LEFT JOIN tbl_users u ON s.teacher_id = u.id " +
                "WHERE s.id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSchedule(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean createSchedule(ClassSchedule schedule) {
        String sql = "INSERT INTO tbl_schedules (class_id, teacher_id, schedule_date, " +
                "start_time, end_time, room, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, schedule.getClassId());
            pstmt.setInt(2, schedule.getTeacherId());
            pstmt.setDate(3, schedule.getScheduleDate());
            pstmt.setTime(4, schedule.getStartTime());
            pstmt.setTime(5, schedule.getEndTime());
            pstmt.setString(6, schedule.getRoom());
            pstmt.setString(7, schedule.getStatus());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        schedule.setId(generatedKeys.getInt(1));
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
    public boolean updateSchedule(ClassSchedule schedule) {
        String sql = "UPDATE tbl_schedules SET class_id = ?, teacher_id = ?, " +
                "schedule_date = ?, start_time = ?, end_time = ?, room = ?, status = ? " +
                "WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, schedule.getClassId());
            pstmt.setInt(2, schedule.getTeacherId());
            pstmt.setDate(3, schedule.getScheduleDate());
            pstmt.setTime(4, schedule.getStartTime());
            pstmt.setTime(5, schedule.getEndTime());
            pstmt.setString(6, schedule.getRoom());
            pstmt.setString(7, schedule.getStatus());
            pstmt.setInt(8, schedule.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteSchedule(int id) {
        String sql = "DELETE FROM tbl_schedules WHERE id = ?";

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
     * Chuyển đổi ResultSet sang đối tượng ClassSchedule
     */
    private ClassSchedule mapResultSetToSchedule(ResultSet rs) throws SQLException {
        ClassSchedule schedule = new ClassSchedule();
        schedule.setId(rs.getInt("id"));
        schedule.setClassId(rs.getInt("class_id"));
        schedule.setTeacherId(rs.getInt("teacher_id"));
        schedule.setScheduleDate(rs.getDate("schedule_date"));
        schedule.setStartTime(rs.getTime("start_time"));
        schedule.setEndTime(rs.getTime("end_time"));
        schedule.setRoom(rs.getString("room"));
        schedule.setStatus(rs.getString("status"));
        schedule.setCreatedAt(rs.getTimestamp("created_at"));
        schedule.setTeacherName(rs.getString("teacher_name"));

        return schedule;
    }
}
