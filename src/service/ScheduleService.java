package service;

import models.ClassSchedule;
import repository.IScheduleRepository;
import repository.impl.ScheduleRepositoryImpl;

import java.sql.Date;
import java.util.List;

/**
 * ScheduleService - Business logic cho lịch học
 */
public class ScheduleService {
    private final IScheduleRepository scheduleRepository;

    public ScheduleService(IScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public ScheduleService() {
        this(new ScheduleRepositoryImpl());
    }

    public List<ClassSchedule> getAllSchedules() {
        return scheduleRepository.getAllSchedules();
    }

    public List<ClassSchedule> getTodaySchedules() {
        return scheduleRepository.getTodaySchedules();
    }

    public List<ClassSchedule> getSchedulesByDate(Date date) {
        return scheduleRepository.getSchedulesByDate(date);
    }

    public ClassSchedule getScheduleById(int id) {
        return scheduleRepository.getScheduleById(id);
    }

    public boolean createSchedule(ClassSchedule schedule) {
        if (schedule == null) {
            return false;
        }

        if (schedule.getScheduleDate() == null || schedule.getStartTime() == null) {
            return false;
        }

        return scheduleRepository.createSchedule(schedule);
    }

    public boolean updateSchedule(ClassSchedule schedule) {
        if (schedule == null || schedule.getId() == 0) {
            return false;
        }

        return scheduleRepository.updateSchedule(schedule);
    }

    public boolean deleteSchedule(int id) {
        if (id <= 0) {
            return false;
        }

        return scheduleRepository.deleteSchedule(id);
    }

    /**
     * Lấy lịch dạy của giảng viên
     */
    public List<ClassSchedule> getSchedulesByTeacher(int teacherId) {
        return getAllSchedules().stream()
                .filter(s -> s.getTeacherId() == teacherId)
                .toList();
    }

    /**
     * Lấy lịch dạy hôm nay của giảng viên
     */
    public List<ClassSchedule> getTodaySchedulesByTeacher(int teacherId) {
        return getTodaySchedules().stream()
                .filter(s -> s.getTeacherId() == teacherId)
                .toList();
    }
}
