package repository;

import models.User;
import utils.enums.UserRole;
import java.util.List;

/**
 * Interface định nghĩa các khao tác với bảng tbl_users
 */
public interface IUserRepository {

    /**
     * Xác thực người dùng (Đăng nhập)
     * 
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return User nếu thành công, null nếu thất bại
     */
    User authenticate(String username, String password);

    /**
     * Tạo mới người dùng
     * 
     * @param user Đối tượng User
     * @return true nếu tạo thành công
     */
    boolean createUser(User user);

    /**
     * Lấy người dùng theo ID
     * 
     * @param id User ID
     * @return User object
     */
    User getUserById(int id);

    /**
     * Lấy danh sách user theo vai trò (không phân trang)
     * 
     * @param role Vai trò (UserRole)
     * @return List<User>
     */
    List<User> getUsersByRole(UserRole role);

    /**
     * Lấy danh sách user theo vai trò (có phân trang)
     * 
     * @param role Vai trò
     * @param page Trang số (bắt đầu từ 1)
     * @param size Số lượng bản ghi mỗi trang
     * @return List<User>
     */
    List<User> getUsersByRoleAndPage(UserRole role, int page, int size);

    /**
     * Đếm tổng số user theo vai trò
     * 
     * @param role Vai trò
     * @return Số lượng
     */
    int getTotalUsersByRoleCount(UserRole role);

    /**
     * Lấy tất cả user (mặc định trang 1, tất cả)
     * 
     * @return List<User>
     */
    List<User> getAllUsers();

    /**
     * Lấy danh sách user có phân trang
     * 
     * @param page Trang số
     * @param size Số lượng/trang
     * @return List<User>
     */
    List<User> getUsersByPage(int page, int size);

    /**
     * Đếm tổng số user trong hệ thống
     * 
     * @return Số lượng user
     */
    int getTotalUsersCount();

    /**
     * Cập nhật thông tin người dùng
     * 
     * @param user Đối tượng User đã chỉnh sửa
     * @return true nếu cập nhật thành công
     */
    boolean updateUser(User user);

    /**
     * Xóa người dùng
     * 
     * @param id User ID
     * @return true nếu xóa thành công
     */
    boolean deleteUser(int id);

    /**
     * Cập nhật trạng thái người dùng (Ví dụ: Khóa/Mở khóa)
     * 
     * @param id     User ID
     * @param status Trạng thái mới
     * @return true nếu thành công
     */
    boolean updateUserStatus(int id, String status);

    /**
     * Cập nhật mật khẩu
     * 
     * @param id          User ID
     * @param newPassword Mật khẩu mới
     * @return true nếu thành công
     */
    boolean updatePassword(int id, String newPassword);

    /**
     * Kiểm tra tên đăng nhập đã tồn tại chưa
     * 
     * @param username Tên đăng nhập cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean usernameExists(String username);
}
