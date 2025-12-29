package repository;

import models.ClassSchedule;
import java.sql.Date;
import java.util.List;

/**
 * IScheduleRepository - Interface cho Schedule data access
 */
public interface IScheduleRepository {

    /**
     * Lấy tất cả lịch học
     */
    List<ClassSchedule> getAllSchedules();

    /**
     * Lấy lịch học hôm nay
     */
    List<ClassSchedule> getTodaySchedules();

    /**
     * Lấy lịch học theo ngày
     */
    List<ClassSchedule> getSchedulesByDate(Date date);

    /**
     * Lấy lịch theo ID
     */
    ClassSchedule getScheduleById(int id);

    /**
     * Tạo lịch học mới
     */
    boolean createSchedule(ClassSchedule schedule);

    /**
     * Cập nhật lịch học
     */
    boolean updateSchedule(ClassSchedule schedule);

    /**
     * Xóa lịch học
     */
    boolean deleteSchedule(int id);
}
