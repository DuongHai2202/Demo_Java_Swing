package service;

import models.Attendance;
import repository.IAttendanceRepository;
import repository.impl.AttendanceRepositoryImpl;

import java.util.List;

/**
 * AttendanceService - Business logic cho điểm danh
 */
public class AttendanceService {
    private final IAttendanceRepository attendanceRepository;

    public AttendanceService(IAttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public AttendanceService() {
        this(new AttendanceRepositoryImpl());
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.getAllAttendance();
    }

    public List<Attendance> getAttendanceBySchedule(int scheduleId) {
        return attendanceRepository.getAttendanceBySchedule(scheduleId);
    }

    public List<Attendance> getAttendanceByStudent(int studentId) {
        return attendanceRepository.getAttendanceByStudent(studentId);
    }

    public boolean markAttendance(Attendance attendance) {
        if (attendance == null) {
            return false;
        }

        if (attendance.getScheduleId() <= 0 || attendance.getStudentId() <= 0) {
            return false;
        }

        if (attendance.getStatus() == null || attendance.getStatus().trim().isEmpty()) {
            attendance.setStatus("Có mặt");
        }

        return attendanceRepository.markAttendance(attendance);
    }

    public boolean markAttendanceBatch(List<Attendance> attendanceList) {
        if (attendanceList == null || attendanceList.isEmpty()) {
            return false;
        }

        boolean success = true;
        for (Attendance attendance : attendanceList) {
            if (!markAttendance(attendance)) {
                success = false;
            }
        }

        return success;
    }

    public boolean updateAttendance(Attendance attendance) {
        if (attendance == null || attendance.getId() <= 0) {
            return false;
        }

        return attendanceRepository.updateAttendance(attendance);
    }

    public boolean deleteAttendance(int id) {
        if (id <= 0) {
            return false;
        }

        return attendanceRepository.deleteAttendance(id);
    }

    public boolean hasAttendance(int scheduleId, int studentId) {
        return attendanceRepository.hasAttendance(scheduleId, studentId);
    }
}
