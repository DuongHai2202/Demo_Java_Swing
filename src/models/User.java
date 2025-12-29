package models;

import utils.enums.Gender;
import utils.enums.UserRole;
import utils.enums.UserStatus;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * User - Model người dùng trong hệ thống
 */
public class User {
    // ID và thông tin tài khoản
    private int id;
    private String username;
    private String password;

    // Vai trò và trạng thái
    private UserRole role;
    private UserStatus status;

    // Thông tin cá nhân
    private String fullName;
    private String email;
    private String phone;
    private Gender gender;
    private Date dateOfBirth;
    private String address;

    // Timestamps
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public User() {
    }

    public User(int id, String username, String fullName, UserRole role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Role
    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    // Status
    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    // Full name
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Gender
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // Date of birth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Created at
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Updated at
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", status=" + status +
                '}';
    }
}
