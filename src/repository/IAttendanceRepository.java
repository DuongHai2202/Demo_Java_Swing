package repository;

import models.Attendance;
import java.util.List;

/**
 * IAttendanceRepository - Interface cho Attendance data access
 */
public interface IAttendanceRepository {

    /**
     * Lấy tất cả điểm danh
     */
    List<Attendance> getAllAttendance();

    /**
     * Lấy điểm danh theo schedule ID
     */
    List<Attendance> getAttendanceBySchedule(int scheduleId);

    /**
     * Lấy điểm danh theo student ID
     */
    List<Attendance> getAttendanceByStudent(int studentId);

    /**
     * Đánh dấu điểm danh
     */
    boolean markAttendance(Attendance attendance);

    /**
     * Cập nhật điểm danh
     */
    boolean updateAttendance(Attendance attendance);

    /**
     * Xóa điểm danh
     */
    boolean deleteAttendance(int id);

    /**
     * Kiểm tra đã điểm danh chưa
     */
    boolean hasAttendance(int scheduleId, int studentId);
}
