package repository;

import models.Course;
import java.util.List;
import java.util.Map;

/**
 * Interface định nghĩa thao tác với bảng tbl_courses (Khóa học)
 */
public interface ICourseRepository {

    /**
     * Lấy tất cả khóa học đang mở (Status = 'Đang mở')
     * 
     * @return List<Course>
     */
    List<Course> getAllActiveCourses();

    /**
     * Lấy tất cả khóa học (bất kể trạng thái)
     * 
     * @return List<Course>
     */
    List<Course> getAllCourses();

    /**
     * Tìm khóa học theo trình độ
     * 
     * @param level Trình độ
     * @return List<Course>
     */
    List<Course> getCoursesByLevel(String level);

    /**
     * Lấy chi tiết khóa học theo ID
     * 
     * @param id Course ID
     * @return Course object
     */
    Course getCourseById(int id);

    /**
     * Tạo mới khóa học
     * 
     * @param course Course object
     * @return true nếu thành công
     */
    boolean createCourse(Course course);

    /**
     * Cập nhật thông tin khóa học
     * 
     * @param course Course object
     * @return true nếu thành công
     */
    boolean updateCourse(Course course);

    /**
     * Xóa khóa học
     * 
     * @param id Course ID
     * @return true nếu xóa thành công
     */
    boolean deleteCourse(int id);

    /**
     * Thống kê số lượng học viên đăng ký theo từng khóa học
     * 
     * @return Map<Tên khóa học, Số lượng học viên>
     */
    Map<String, Integer> getCourseEnrollmentStats();
}
