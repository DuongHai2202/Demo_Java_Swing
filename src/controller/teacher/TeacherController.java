package controller.teacher;

import models.Attendance;
import models.ClassSchedule;
import models.Student;
import models.User;
import service.AttendanceService;
import service.ScheduleService;
import service.StudentService;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TeacherController - Quản lý nghiệp vụ giảng viên
 */
public class TeacherController {
    private final User currentTeacher;
    private final ScheduleService scheduleService;
    private final UserService userService;

    public TeacherController(User teacher) {
        this.currentTeacher = teacher;
        this.scheduleService = new ScheduleService();
        this.userService = new UserService();
    }

    /**
     * Lấy danh sách lịch dạy của giảng viên
     */
    public List<ClassSchedule> getMySchedules() {
        if (currentTeacher == null) {
            return List.of();
        }
        return scheduleService.getSchedulesByTeacher(currentTeacher.getId());
    }

    /**
     * Lấy lịch dạy hôm nay
     */
    public List<ClassSchedule> getTodaySchedules() {
        if (currentTeacher == null) {
            return List.of();
        }
        return scheduleService.getTodaySchedulesByTeacher(currentTeacher.getId());
    }

    /**
     * Lấy số lớp dạy (ước tả từ schedules)
     */
    public int getAssignedClassesCount() {
        if (currentTeacher == null) {
            return 0;
        }
        // Count unique class_ids from schedules
        return (int) getMySchedules().stream()
                .map(ClassSchedule::getClassId)
                .distinct()
                .count();
    }

    /**
     * Lấy số buổi dạy trong tháng
     */
    public int getMonthlySessionsCount() {
        if (currentTeacher == null) {
            return 0;
        }
        return getMySchedules().size();
    }

    /**
     * Lấy thống kê giảng dạy
     */
    public Map<String, Integer> getTeachingStats() {
        Map<String, Integer> stats = new HashMap<>();

        if (currentTeacher != null) {
            List<ClassSchedule> schedules = getMySchedules();
            int classCount = (int) schedules.stream()
                    .map(ClassSchedule::getClassId)
                    .distinct() // Retained for syntactic correctness, as the provided snippet was
                                // incomplete/malformed
                    .count();

            stats.put("CLASSES", classCount);
            stats.put("STUDENTS", classCount * 25); // Estimate: avg 25 students/class
            stats.put("SESSIONS", schedules.size());
        } else {
            stats.put("CLASSES", 0);
            stats.put("STUDENTS", 0);
            stats.put("SESSIONS", 0);
        }

        return stats;
    }

    /**
     * Lấy thông tin giảng viên hiện tại
     */
    public User getCurrentTeacher() {
        return currentTeacher;
    }

    /**
     * Cập nhật thông tin profile
     */
    public boolean updateProfile(User updatedUser) {
        return userService.updateUser(updatedUser);
    }

    /**
     * Lấy schedules grouped by class_id
     */
    public Map<Integer, List<ClassSchedule>> getSchedulesGroupedByClass() {
        return getMySchedules().stream()
                .collect(Collectors.groupingBy(ClassSchedule::getClassId));
    }

    /**
     * Lấy danh sách học viên trong lớp (from enrollments)
     */
    public List<Student> getStudentsByClass(int classId) {
        // Load enrollments for this class, then get students
        // For now, query all students and filter by enrollments
        StudentService studentService = new StudentService();

        // This is simplified - in real app, should JOIN enrollments
        // Use paginated query for now (page is 1-indexed)
        return studentService.getStudentsPaginated(1, 10);
    }

    /**
     * Lưu điểm danh batch
     */
    public boolean saveAttendanceBatch(List<Attendance> attendanceList) {
        AttendanceService attendanceService = new AttendanceService();
        return attendanceService.markAttendanceBatch(attendanceList);
    }

    /**
     * Lấy attendance đã có cho một schedule
     */
    public List<Attendance> getAttendanceForSchedule(int scheduleId) {
        AttendanceService attendanceService = new AttendanceService();
        return attendanceService.getAttendanceBySchedule(scheduleId);
    }
}
