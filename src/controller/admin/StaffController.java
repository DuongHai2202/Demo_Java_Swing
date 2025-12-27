package controller.admin;

import models.Staff;
import service.StaffService;
import java.util.List;

/**
 * StaffController - Quản lý nhân viên
 */
public class StaffController {
    private final StaffService staffService;

    public StaffController() {
        this.staffService = new StaffService();
    }

    public List<Staff> getStaffList() {
        return staffService.getAllStaff();
    }

    /**
     * Lấy danh sách nhân viên theo phân trang
     */
    public List<Staff> getStaffListPaginated(int page, int size) {
        return staffService.getStaffPaginated(page, size);
    }

    public int getTotalStaffCount() {
        return staffService.getTotalStaffCount();
    }

    public boolean addStaff(Staff staff) {
        return staffService.createStaff(staff);
    }

    public boolean updateStaff(Staff staff) {
        return staffService.updateStaff(staff);
    }

    public boolean deleteStaff(int userId) {
        return staffService.deleteStaff(userId);
    }

    public Staff getStaffById(int id) {
        return staffService.getStaffById(id);
    }

    public Staff getStaffByUserId(int userId) {
        return staffService.getStaffByUserId(userId);
    }
}
