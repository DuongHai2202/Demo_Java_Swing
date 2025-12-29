package repository;

import models.Teacher;
import java.util.List;

/**
 * ITeacherRepository - Interface cho Teacher data access
 */
public interface ITeacherRepository {

    /**
     * Lấy tất cả giảng viên
     */
    List<Teacher> getAllTeachers();

    /**
     * Lấy giảng viên theo phân trang
     */
    List<Teacher> getTeachersPaginated(int page, int size);

    /**
     * Đếm tổng số giảng viên
     */
    int getTotalTeacherCount();

    /**
     * Lấy giảng viên theo ID
     */
    Teacher getTeacherById(int id);

    /**
     * Lấy giảng viên theo User ID
     */
    Teacher getTeacherByUserId(int userId);

    /**
     * Tạo giảng viên mới
     */
    boolean createTeacher(Teacher teacher);

    /**
     * Cập nhật thông tin giảng viên
     */
    boolean updateTeacher(Teacher teacher);

    /**
     * Xóa giảng viên
     */
    boolean deleteTeacher(int id);
}
