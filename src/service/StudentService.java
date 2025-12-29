package service;

import models.Student;
import models.User;
import utils.enums.UserRole;
import repository.IStudentRepository;
import repository.IUserRepository;
import repository.impl.StudentRepositoryImpl;
import repository.impl.UserRepositoryImpl;

import java.util.List;

/**
 * StudentService - Xử lý business logic cho Student
 */
public class StudentService {
    private final IStudentRepository studentRepository;
    private final IUserRepository userRepository;

    public StudentService(IStudentRepository studentRepository, IUserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    public StudentService() {
        this(new StudentRepositoryImpl(), new UserRepositoryImpl());
    }

    public List<Student> getStudentsPaginated(int page, int size) {
        return studentRepository.getStudentsPaginated(page, size);
    }

    public int getTotalStudentCount() {
        return studentRepository.getTotalStudentCount();
    }

    /**
     * Tạo học viên mới (tạo User trước, sau đó Student)
     */
    public boolean createStudent(Student student) {
        User user = student.getUser();
        user.setRole(UserRole.HOC_VIEN);

        if (userRepository.createUser(user)) {
            student.setUserId(user.getId());
            return studentRepository.createStudent(student);
        }
        return false;
    }

    /**
     * Cập nhật thông tin học viên (update cả User và Student)
     */
    public boolean updateStudent(Student student) {
        if (userRepository.updateUser(student.getUser())) {
            return studentRepository.updateStudent(student);
        }
        return false;
    }

    /**
     * Xóa học viên (cascade tự động xóa Student entry)
     */
    public boolean deleteStudent(int userId) {
        return userRepository.deleteUser(userId);
    }

    public Student getStudentByUserId(int userId) {
        return studentRepository.getStudentByUserId(userId);
    }
}
