package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton class để quản lý kết nối MySQL
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // MySQL Configuration
    private static final String URL = "jdbc:mysql://localhost:3306/english_center";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Private constructor để implement Singleton pattern
     */
    private DatabaseConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName(DRIVER);

            // Establish connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Ket noi database thanh cong!");

        } catch (ClassNotFoundException e) {
            System.err.println("Khong tim thay MySQL JDBC Driver!");
            System.err.println("Vui long them mysql-connector-java vào classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Loi ket noi database!");
            System.err.println("Kiem tra: MySQL server, database 'english_center', username/password");
            e.printStackTrace();
        }
    }

    /**
     * Lấy instance duy nhất của DatabaseConnection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            synchronized (DatabaseConnection.class) {
                if (instance == null || !isConnectionValid()) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Kiểm tra connection còn hợp lệ không
     */
    private static boolean isConnectionValid() {
        try {
            return instance != null
                    && instance.connection != null
                    && !instance.connection.isClosed()
                    && instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Lấy connection để thực hiện query
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Đóng connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Da dong ket noi database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test connection
     */
    public boolean testConnection() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery("SELECT 1");
            stmt.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
