package repository;

import models.Staff;
import java.util.List;

/**
 * Interface định nghĩa thao tác với bảng tbl_staff (Nhân viên)
 */
public interface IStaffRepository {

    /**
     * Tạo mới nhân viên (lưu ý: userId phải tồn tại trước)
     * 
     * @param staff Đối tượng Staff
     * @return true nếu thành công
     */
    boolean createStaff(Staff staff);

    /**
     * Lấy thông tin Staff theo User ID
     * 
     * @param userId ID của tài khoản user
     * @return Staff object
     */
    Staff getStaffByUserId(int userId);

    /**
     * Lấy Staff theo ID của bảng tbl_staff
     * 
     * @param id Staff ID
     * @return Staff object
     */
    Staff getStaffById(int id);

    /**
     * Lấy danh sách nhân viên có phân trang
     * 
     * @param page Trang số
     * @param size Số lượng/trang
     * @return List<Staff>
     */
    List<Staff> getStaffPaginated(int page, int size);

    /**
     * Lấy tất cả nhân viên
     * 
     * @return List<Staff>
     */
    List<Staff> getAllStaff();

    /**
     * Đếm tổng số nhân viên
     * 
     * @return Số lượng
     */
    int getTotalStaffCount();

    /**
     * Cập nhật thông tin chi tiết nhân viên
     * 
     * @param staff Đối tượng Staff
     * @return true nếu thành công
     */
    boolean updateStaff(Staff staff);

    /**
     * Xóa nhân viên theo ID
     * 
     * @param id Staff ID
     * @return true nếu thành công
     */
    boolean deleteStaff(int id);
}
