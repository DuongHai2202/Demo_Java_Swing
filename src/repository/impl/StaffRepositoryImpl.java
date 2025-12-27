package repository.impl;

import database.DatabaseConnection;
import models.Staff;
import models.User;
import models.UserRole;
import repository.IStaffRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai cho IStaffRepository
 */
public class StaffRepositoryImpl implements IStaffRepository {
    public StaffRepositoryImpl() {
    }

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public boolean createStaff(Staff staff) {
        String sql = "INSERT INTO tbl_staff (user_id, staff_code, position, department, hire_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, staff.getUserId());
            pstmt.setString(2, staff.getStaffCode());
            pstmt.setString(3, staff.getPosition());
            pstmt.setString(4, staff.getDepartment());
            pstmt.setDate(5, staff.getHireDate());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    staff.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Staff getStaffByUserId(int userId) {
        // Join với tbl_users để lấy đầy đủ thông tin
        String sql = "SELECT s.*, u.* FROM tbl_staff s JOIN tbl_users u ON s.user_id = u.id WHERE s.user_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStaffFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Staff getStaffById(int id) {
        String sql = "SELECT s.*, u.* FROM tbl_staff s JOIN tbl_users u ON s.user_id = u.id WHERE s.id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractStaffFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Staff> getStaffPaginated(int page, int size) {
        List<Staff> staffList = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT s.*, u.* FROM tbl_staff s JOIN tbl_users u ON s.user_id = u.id ORDER BY u.full_name LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT s.*, u.* FROM tbl_staff s JOIN tbl_users u ON s.user_id = u.id ORDER BY u.full_name";

        try (Statement stmt = getConnection().createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                staffList.add(extractStaffFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public int getTotalStaffCount() {
        String sql = "SELECT COUNT(*) FROM tbl_staff";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE tbl_staff SET staff_code = ?, position = ?, department = ?, hire_date = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, staff.getStaffCode());
            pstmt.setString(2, staff.getPosition());
            pstmt.setString(3, staff.getDepartment());
            pstmt.setDate(4, staff.getHireDate());
            pstmt.setInt(5, staff.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteStaff(int id) {
        String sql = "DELETE FROM tbl_staff WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Staff extractStaffFromResultSet(ResultSet rs) throws SQLException {
        Staff s = new Staff();
        s.setId(rs.getInt("s.id"));
        s.setUserId(rs.getInt("s.user_id"));
        s.setStaffCode(rs.getString("s.staff_code"));
        s.setPosition(rs.getString("s.position"));
        s.setDepartment(rs.getString("s.department"));
        s.setHireDate(rs.getDate("s.hire_date"));

        User u = new User();
        u.setId(rs.getInt("u.id"));
        u.setUsername(rs.getString("u.username"));
        u.setRole(UserRole.NHAN_VIEN);
        u.setFullName(rs.getString("u.full_name"));
        u.setEmail(rs.getString("u.email"));
        u.setPhone(rs.getString("u.phone"));
        u.setAddress(rs.getString("u.address"));
        u.setGender(models.Gender.fromDisplayName(rs.getString("u.gender")));
        u.setDateOfBirth(rs.getDate("u.date_of_birth"));
        u.setStatus(models.UserStatus.fromDisplayName(rs.getString("u.status")));

        s.setUser(u);
        return s;
    }
}
