package models;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Model class cho Course
 */
public class Course {
    private int id;
    private String courseCode;
    private String courseName;
    private String description;
    private String level;
    private int durationHours;
    private BigDecimal fee;
    private CourseStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Course() {
        this.status = CourseStatus.DANG_MO;
    }

    public Course(String courseCode, String courseName, String level, int durationHours, BigDecimal fee) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.level = level;
        this.durationHours = durationHours;
        this.fee = fee;
        this.status = CourseStatus.DANG_MO;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", level='" + level + '\'' +
                ", fee=" + fee +
                ", status=" + status +
                '}';
    }
}
