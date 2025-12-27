package service;

import models.Staff;
import models.User;
import models.UserRole;
import repository.IStaffRepository;
import repository.IUserRepository;
import repository.impl.StaffRepositoryImpl;
import repository.impl.UserRepositoryImpl;

import java.util.List;

/**
 * StaffService - Xử lý business logic cho Staff
 */
public class StaffService {
    private final IStaffRepository staffRepository;
    private final IUserRepository userRepository;

    public StaffService() {
        this.staffRepository = new StaffRepositoryImpl();
        this.userRepository = new UserRepositoryImpl();
    }

    public List<Staff> getStaffPaginated(int page, int size) {
        return staffRepository.getStaffPaginated(page, size);
    }

    public int getTotalStaffCount() {
        return staffRepository.getTotalStaffCount();
    }

    public List<Staff> getAllStaff() {
        return staffRepository.getAllStaff();
    }

    /**
     * Tạo nhân viên mới (tạo User trước, sau đó Staff)
     */
    public boolean createStaff(Staff staff) {
        User user = staff.getUser();
        user.setRole(UserRole.NHAN_VIEN);

        if (userRepository.createUser(user)) {
            staff.setUserId(user.getId());
            return staffRepository.createStaff(staff);
        }
        return false;
    }

    /**
     * Cập nhật thông tin nhân viên (update cả User và Staff)
     */
    public boolean updateStaff(Staff staff) {
        if (userRepository.updateUser(staff.getUser())) {
            return staffRepository.updateStaff(staff);
        }
        return false;
    }

    /**
     * Xóa nhân viên (cascade tự động xóa Staff entry)
     */
    public boolean deleteStaff(int userId) {
        return userRepository.deleteUser(userId);
    }

    public Staff getStaffByUserId(int userId) {
        return staffRepository.getStaffByUserId(userId);
    }

    public Staff getStaffById(int id) {
        return staffRepository.getStaffById(id);
    }
}
