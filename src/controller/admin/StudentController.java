package controller.admin;

import models.Student;
import service.StudentService;
import java.util.List;

/**
 * StudentController - Quản lý học viên
 */
public class StudentController {
    private final StudentService studentService;

    public StudentController() {
        this.studentService = new StudentService();
    }

    /**
     * Lấy danh sách học viên theo phân trang
     */
    public List<Student> getStudentListPaginated(int page, int size) {
        return studentService.getStudentsPaginated(page, size);
    }

    public int getTotalStudentCount() {
        return studentService.getTotalStudentCount();
    }

    public boolean addStudent(Student student) {
        return studentService.createStudent(student);
    }

    public boolean updateStudent(Student student) {
        return studentService.updateStudent(student);
    }

    public boolean deleteStudent(int userId) {
        return studentService.deleteStudent(userId);
    }

    public Student getStudentByUserId(int userId) {
        return studentService.getStudentByUserId(userId);
    }
}
