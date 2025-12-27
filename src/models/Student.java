package models;

/**
 * Student - Model học viên kế thừa từ User
 */
public class Student {
    private int id;
    private int userId;
    private String studentCode;
    private String currentLevel;

    // Reference to User
    private User user;

    // Constructors
    public Student() {
    }

    public Student(int id, int userId, String studentCode, String currentLevel) {
        this.id = id;
        this.userId = userId;
        this.studentCode = studentCode;
        this.currentLevel = currentLevel;
    }

    // Getters & Setters
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

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(String currentLevel) {
        this.currentLevel = currentLevel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", userId=" + userId +
                ", studentCode='" + studentCode + '\'' +
                ", currentLevel='" + currentLevel + '\'' +
                '}';
    }
}
