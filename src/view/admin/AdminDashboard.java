package view.admin;

import controller.admin.*;
import models.User;
import utils.UIUtils;
import view.LoginFrame;
import view.admin.panels.*;

import javax.swing.*;
import java.awt.*;

/**
 * Admin Dashboard - Giao diện quản trị viên
 * Updated for MVC architecture
 */
public class AdminDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;

    // Specialized Controllers
    private StaffController staffController;
    private TransactionController transactionController;
    private ReportingController reportingController;
    private AccountController accountController;
    private StudentController studentController;

    private HomePanel homePanel;
    private StaffManagementPanel staffPanel;
    private TransactionManagementPanel transactionPanel;
    private ReportsPanel reportsPanel;
    private AccountManagementPanel accountPanel;
    private StudentManagementPanel studentPanel;

    public AdminDashboard(User user) {
        this.currentUser = user;
        initControllers();
        initComponents();
    }

    private void initControllers() {
        this.staffController = new StaffController();
        this.transactionController = new TransactionController();
        this.reportingController = new ReportingController();
        this.accountController = new AccountController();
        this.studentController = new StudentController();
    }

    private void initComponents() {
        setTitle("ODIN - Quản Trị Viên");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Top bar
        add(createTopBar(), BorderLayout.NORTH);

        // Sidebar
        add(createSidebar(), BorderLayout.WEST);

        // Content panel
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(UIUtils.LIGHT_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initPanels();
        showPanel("HOME");

        add(contentPanel, BorderLayout.CENTER);
    }

    private void initPanels() {
        homePanel = new HomePanel();
        staffPanel = new StaffManagementPanel(this, staffController);
        transactionPanel = new TransactionManagementPanel(this, transactionController, currentUser);
        reportsPanel = new ReportsPanel(this, reportingController);
        accountPanel = new AccountManagementPanel(this, accountController);
        studentPanel = new StudentManagementPanel(this, studentController);

        contentPanel.add(homePanel, "HOME");
        contentPanel.add(staffPanel, "STAFF");
        contentPanel.add(transactionPanel, "TRANSACTION");
        contentPanel.add(reportsPanel, "REPORTS");
        contentPanel.add(accountPanel, "ACCOUNTS");
        contentPanel.add(studentPanel, "STUDENT");
    }

    private void showPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
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

        JLabel userLabel = new JLabel(currentUser.getFullName() + " (ADMIN)  ");
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

        // Menu items
        sidebar.add(Box.createVerticalStrut(20));
        addMenuItem(sidebar, "Trang Chủ", e -> showPanel("HOME"));
        addMenuItem(sidebar, "Quản Lí Nhân Viên", e -> showPanel("STAFF"));
        addMenuItem(sidebar, "Quản Lí Giao Dịch", e -> showPanel("TRANSACTION"));
        addMenuItem(sidebar, "Quản Lí Học Viên", e -> showPanel("STUDENT"));
        addMenuItem(sidebar, "Quản Lí Tài Khoản", e -> showPanel("ACCOUNTS"));
        addMenuItem(sidebar, "Báo Cáo - Thống Kê", e -> showPanel("REPORTS"));

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

    private void handleLogout() {
        if (UIUtils.showConfirmation(this, "Đăng xuất?")) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
