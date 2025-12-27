package view.student;

import service.CourseService;
import models.Course;
import models.User;
import view.LoginFrame;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Student Dashboard - Giao diện học viên
 * Updated for View package and CourseService
 */
public class StudentDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CourseService courseService;

    public StudentDashboard(User user) {
        this.currentUser = user;
        this.courseService = new CourseService();
        initComponents();
    }

    private void initComponents() {
        setTitle("ODIN - Học Viên");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);
        add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIUtils.LIGHT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        showDashboardHome();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JToolBar topBar = new JToolBar();
        topBar.setFloatable(false);
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        topBar.setPreferredSize(new Dimension(0, 60));

        JLabel titleLabel = new JLabel("  ODIN Language Center");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UIUtils.PRIMARY_COLOR);

        topBar.add(titleLabel);
        topBar.add(Box.createHorizontalGlue());

        JLabel userLabel = new JLabel(currentUser.getFullName() + " (Học viên)  ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.putClientProperty("JButton.buttonType", "borderless");
        logoutButton.setForeground(UIUtils.DANGER_COLOR);
        logoutButton.addActionListener(e -> handleLogout());

        topBar.add(userLabel);
        topBar.add(logoutButton);
        topBar.add(Box.createHorizontalStrut(10));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topBar, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtils.DARK_COLOR);
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        sidebar.add(Box.createVerticalStrut(10));
        addMenuItem(sidebar, "Trang Chủ", e -> showDashboardHome());
        addMenuItem(sidebar, "Khóa Học", e -> showCourses());
        addMenuItem(sidebar, "Khóa Học Của Tôi", e -> switchContent("Khóa Học Của Tôi"));
        addMenuItem(sidebar, "Thanh Toán", e -> switchContent("Thanh Toán"));
        addMenuItem(sidebar, "Đánh Giá", e -> switchContent("Đánh Giá Khóa Học"));
        addMenuItem(sidebar, "Tài Khoản", e -> switchContent("Tài Khoản Cá Nhân"));

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private void addMenuItem(JPanel sidebar, String text, java.awt.event.ActionListener action) {
        JButton menuItem = new JButton(text);
        menuItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuItem.setForeground(Color.WHITE);
        menuItem.setHorizontalAlignment(SwingConstants.LEFT);
        menuItem.setMaximumSize(new Dimension(240, 45));
        menuItem.putClientProperty("JButton.buttonType", "square");
        menuItem.setFocusPainted(false);
        menuItem.setBorderPainted(false);
        menuItem.setBackground(UIUtils.DARK_COLOR);

        menuItem.addActionListener(action);

        menuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(UIUtils.PRIMARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuItem.setBackground(UIUtils.DARK_COLOR);
            }
        });

        sidebar.add(menuItem);
    }

    private void showDashboardHome() {
        contentPanel.removeAll();

        JPanel homePanel = new JPanel();
        homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));
        homePanel.setBackground(UIUtils.LIGHT_BG);

        JLabel welcomeLabel = UIUtils.createHeaderLabel("Chào mừng, " + currentUser.getFullName() + "!");
        homePanel.add(welcomeLabel);

        contentPanel.add(homePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCourses() {
        contentPanel.removeAll();

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setOpaque(false);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(UIUtils.createHeaderLabel("Danh Sách Khóa Học"), BorderLayout.WEST);

        // Course table
        String[] columnNames = { "Mã KH", "Tên Khóa Học", "Trình Độ", "Thời Lượng (giờ)", "Học Phí (VNĐ)",
                "Trạng Thái" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int cell, int col) {
                return false;
            }
        };

        // Load courses from database via service
        List<Course> courses = courseService.getAllActiveCourses();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Course course : courses) {
            Object[] row = {
                    course.getCourseCode(),
                    course.getCourseName(),
                    course.getLevel(),
                    course.getDurationHours(),
                    currencyFormat.format(course.getFee()),
                    course.getStatus()
            };
            model.addRow(row);
        }

        JTable courseTable = new JTable(model);
        courseTable.setRowHeight(35);
        courseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setOpaque(false);

        JButton enrollButton = UIUtils.createSuccessButton("Đăng Ký Học");
        JButton viewDetailButton = UIUtils.createPrimaryButton("Xem Chi Tiết");

        enrollButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                String courseName = (String) courseTable.getValueAt(selectedRow, 1);
                UIUtils.showSuccess(this, "Đã gửi yêu cầu đăng ký khóa: " + courseName);
            } else {
                UIUtils.showError(this, "Vui lòng chọn khóa học!");
            }
        });

        viewDetailButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                String courseName = (String) courseTable.getValueAt(selectedRow, 1);
                UIUtils.showSuccess(this, "Xem chi tiết khóa: " + courseName);
            } else {
                UIUtils.showError(this, "Vui lòng chọn khóa học!");
            }
        });

        buttonPanel.add(enrollButton);
        buttonPanel.add(viewDetailButton);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void switchContent(String title) {
        contentPanel.removeAll();
        contentPanel.add(new JLabel("Đang phát triển: " + title));
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleLogout() {
        if (UIUtils.showConfirmation(this, "Bạn có chắc muốn đăng xuất?")) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
