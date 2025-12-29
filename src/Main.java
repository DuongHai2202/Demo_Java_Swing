import database.DatabaseConnection;
import view.LoginFrame;

import javax.swing.*;

/**
 * Main class - Entry point của ứng dụng
 */
public class Main {
    public static void main(String[] args) {
        // Set Look and Feel to FlatLaf
        try {
            com.formdev.flatlaf.FlatLightLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Test database connection
        System.out.println("ODIN Language Center Management System");

        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        if (dbConnection.testConnection()) {
            System.out.println("Kết nối database thành công!");
            System.out.println("Dự án đang khởi động...\n");

            // Start application
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        } else {
            System.err.println("✗ Database connection failed!");
            System.err.println("✗ Please check:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database 'english_center' exists");
            System.err.println("  3. Username and password are correct");
            System.err.println("  4. MySQL Connector JAR is in classpath");
            System.err.println("===========================================\n");

            JOptionPane.showMessageDialog(null,
                    "Không thể kết nối database!\nVui lòng kiểm tra:\n" +
                            "1. MySQL server đang chạy\n" +
                            "2. Database 'english_center' đã được tạo\n" +
                            "3. Username/password đúng (root/123456)\n" +
                            "4. MySQL Connector JAR trong classpath",
                    "Lỗi Kết Nối Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
