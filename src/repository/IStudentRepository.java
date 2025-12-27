package repository;

import models.Student;
import java.util.List;

/**
 * Interface định nghĩa thao tác với bảng tbl_students (Học viên)
 */
public interface IStudentRepository {

    /**
     * Tạo mới hồ sơ học viên
     * 
     * @param student Đối tượng Student
     * @return true nếu thành công
     */
    boolean createStudent(Student student);

    /**
     * Lấy thông tin học viên theo User ID
     * 
     * @param userId ID tài khoản user
     * @return Student object
     */
    Student getStudentByUserId(int userId);

    /**
     * Lấy danh sách học viên có phân trang
     * 
     * @param page Trang số
     * @param size Số lượng/trang
     * @return List<Student>
     */
    List<Student> getStudentsPaginated(int page, int size);

    /**
     * Đếm tổng số học viên
     * 
     * @return Số lượng
     */
    int getTotalStudentCount();

    /**
     * Cập nhật thông tin học viên
     * 
     * @param student Đối tượng Student
     * @return true nếu thành công
     */
    boolean updateStudent(Student student);
}
