package database;

import java.sql.*;

public class DbTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/english_center";
        String user = "root";
        String pass = "123456";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("Connection successful!");

            // Check if tbl_users exists and has correct columns
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getColumns(null, null, "tbl_users", null);

            System.out.println("Columns in tbl_users:");
            boolean hasAddress = false;
            boolean hasGender = false;
            boolean hasDob = false;

            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                System.out.println("- " + colName);
                if (colName.equalsIgnoreCase("address"))
                    hasAddress = true;
                if (colName.equalsIgnoreCase("gender"))
                    hasGender = true;
                if (colName.equalsIgnoreCase("date_of_birth"))
                    hasDob = true;
            }

            if (hasAddress && hasGender && hasDob) {
                System.out.println("SUCCESS: All new columns found.");
            } else {
                System.out.println("FAILURE: Some columns are missing!");
            }

            // Check for users table (old name)
            rs = meta.getTables(null, null, "users", null);
            if (rs.next()) {
                System.out.println("WARNING: Old 'users' table still exists.");
            }

        } catch (SQLException e) {
            System.err.println("Database test failed!");
            e.printStackTrace();
        }
    }
}
