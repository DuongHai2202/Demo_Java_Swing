package repository.impl;

import database.DatabaseConnection;
import models.User;
import utils.enums.UserRole;
import repository.IUserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp triển khai (Implementation) cho IUserRepository
 * Thực thi các thao tác database liên quan đến User
 */
public class UserRepositoryImpl implements IUserRepository {
    public UserRepositoryImpl() {
    }

    private Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM tbl_users WHERE username = ? AND password = ? AND status = 'Đang hoạt động'";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createUser(User user) {
        String sql = "INSERT INTO tbl_users (username, password, `role`, full_name, email, phone, address, gender, date_of_birth, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole().getDisplayName());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getPhone());
            pstmt.setString(7, user.getAddress());
            pstmt.setString(8, user.getGender() != null ? user.getGender().getDisplayName() : null);
            pstmt.setDate(9, user.getDateOfBirth());
            pstmt.setString(10, user.getStatus() != null ? user.getStatus().getDisplayName() : "Đang hoạt động");

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User getUserById(int id) {
        String sql = "SELECT * FROM tbl_users WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUsersByRole(UserRole role) {
        return getUsersByRoleAndPage(role, 1, Integer.MAX_VALUE);
    }

    @Override
    public List<User> getUsersByRoleAndPage(UserRole role, int page, int size) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM tbl_users WHERE `role` = ? ORDER BY full_name LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, role.getDisplayName());
            pstmt.setInt(2, size);
            pstmt.setInt(3, offset);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int getTotalUsersByRoleCount(UserRole role) {
        String sql = "SELECT COUNT(*) FROM tbl_users WHERE `role` = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, role.getDisplayName());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<User> getAllUsers() {
        return getUsersByPage(1, Integer.MAX_VALUE);
    }

    @Override
    public List<User> getUsersByPage(int page, int size) {
        List<User> users = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT * FROM tbl_users ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public int getTotalUsersCount() {
        String sql = "SELECT COUNT(*) FROM tbl_users";
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
    public boolean updateUser(User user) {
        String sql = "UPDATE tbl_users SET full_name = ?, email = ?, phone = ?, address = ?, gender = ?, date_of_birth = ?, status = ?, password = ? WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getGender() != null ? user.getGender().getDisplayName() : null);
            pstmt.setDate(6, user.getDateOfBirth());
            pstmt.setString(7, user.getStatus() != null ? user.getStatus().getDisplayName() : "Đang hoạt động");
            pstmt.setString(8, user.getPassword());
            pstmt.setInt(9, user.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        String sql = "DELETE FROM tbl_users WHERE id = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUserStatus(int id, String status) {
        String sql = "UPDATE tbl_users SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePassword(int id, String newPassword) {
        String sql = "UPDATE tbl_users SET password = ? WHERE id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM tbl_users WHERE username = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Map dữ liệu từ ResultSet sang đối tượng User
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(UserRole.fromDisplayName(rs.getString("role")));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setGender(utils.enums.Gender.fromDisplayName(rs.getString("gender")));
        user.setDateOfBirth(rs.getDate("date_of_birth"));
        user.setStatus(utils.enums.UserStatus.fromDisplayName(rs.getString("status")));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}
