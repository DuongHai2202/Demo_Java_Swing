package models;

import java.sql.Date;

/**
 * Model class cho Staff (Nhân viên)
 */
public class Staff {
    private int id;
    private int userId;
    private String staffCode;
    private String position; // Chức vụ
    private String department; // Phòng ban
    private Date hireDate; // Ngày vào làm

    // User information (joined from tbl_users)
    private User user;

    // Constructors
    public Staff() {
    }

    public Staff(int userId, String staffCode) {
        this.userId = userId;
        this.staffCode = staffCode;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", staffCode='" + staffCode + '\'' +
                ", position='" + position + '\'' +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
