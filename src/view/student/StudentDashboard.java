package view.student;

import controller.student.StudentController;
import service.CourseService;
import models.Course;
import models.User;
import view.LoginFrame;
import view.student.panels.MyCoursesPanel;
import view.student.panels.PaymentHistoryPanel;
import view.student.panels.ProfilePanel;
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
    private StudentController controller;

    // Panels
    private MyCoursesPanel myCoursesPanel;
    private PaymentHistoryPanel paymentHistoryPanel;
    private ProfilePanel profilePanel;

    public StudentDashboard(User user) {
        this.currentUser = user;
        this.courseService = new CourseService();
        this.controller = new StudentController(user);
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

        // Welcome Card
        JPanel welcomeCard = UIUtils.createCardPanel();
        welcomeCard.setLayout(new BorderLayout(UIUtils.SPACING_MD, UIUtils.SPACING_MD));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel welcomeLabel = UIUtils.createHeaderLabel("Chào mừng, " + currentUser.getFullName() + "!");
        JLabel subtitle = UIUtils.createSecondaryLabel("Hệ thống học viên ODIN Language Center");

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(UIUtils.SPACING_SM));
        textPanel.add(subtitle);

        welcomeCard.add(textPanel, BorderLayout.CENTER);

        homePanel.add(welcomeCard);
        homePanel.add(Box.createVerticalStrut(UIUtils.SPACING_LG));

        // Quick Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, UIUtils.SPACING_MD, 0));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        statsPanel.add(createStatCard("Khóa Học", "0", UIUtils.PRIMARY_COLOR));
        statsPanel.add(createStatCard("Điểm Danh", "0%", UIUtils.ACCENT_COLOR));
        statsPanel.add(createStatCard("Thanh Toán", "0đ", UIUtils.SECONDARY_COLOR));

        homePanel.add(statsPanel);
        homePanel.add(Box.createVerticalGlue());

        contentPanel.add(homePanel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout());

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(color);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(UIUtils.NORMAL_FONT);
        lblTitle.setForeground(UIUtils.TEXT_SECONDARY);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblValue, BorderLayout.CENTER);
        card.add(lblTitle, BorderLayout.SOUTH);

        return card;
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
                UIUtils.showInfo(this, "Để đăng ký khóa " + courseName + ", vui lòng liên hệ:\n" +
                        "☎ Hotline: 1900-xxxx\n" +
                        "📧 Email: contact@odin.edu.vn");
            } else {
                UIUtils.showError(this, "Vui lòng chọn khóa học!");
            }
        });

        viewDetailButton.addActionListener(e -> {
            int selectedRow = courseTable.getSelectedRow();
            if (selectedRow >= 0) {
                String courseCode = (String) courseTable.getValueAt(selectedRow, 0);
                String courseName = (String) courseTable.getValueAt(selectedRow, 1);
                String level = (String) courseTable.getValueAt(selectedRow, 2);
                String duration = courseTable.getValueAt(selectedRow, 3).toString();
                String fee = (String) courseTable.getValueAt(selectedRow, 4);

                String details = String.format(
                        "Thông tin khóa học:\n\n" +
                                "Mã: %s\n" +
                                "Tên: %s\n" +
                                "Trình độ: %s\n" +
                                "Thời lượng: %s giờ\n" +
                                "Học phí: %s",
                        courseCode, courseName, level, duration, fee);
                UIUtils.showInfo(this, details);
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

        switch (title) {
            case "Khóa Học Của Tôi":
                if (myCoursesPanel == null) {
                    myCoursesPanel = new MyCoursesPanel(controller);
                }
                contentPanel.add(myCoursesPanel, BorderLayout.CENTER);
                break;

            case "Thanh Toán":
                if (paymentHistoryPanel == null) {
                    paymentHistoryPanel = new PaymentHistoryPanel(controller);
                }
                contentPanel.add(paymentHistoryPanel, BorderLayout.CENTER);
                break;

            case "Tài Khoản Cá Nhân":
                if (profilePanel == null) {
                    profilePanel = new ProfilePanel(controller);
                }
                contentPanel.add(profilePanel, BorderLayout.CENTER);
                break;
        }

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
