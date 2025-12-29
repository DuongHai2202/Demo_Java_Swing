package view.staff;

import controller.staff.StaffController;
import models.User;
import view.LoginFrame;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Staff Dashboard - Giao diện nhân viên
 * Updated for View package
 */
public class StaffDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private StaffController staffController;

    // Panels
    private StaffSupportPanel supportPanel;
    private StaffPostPanel postPanel;
    private view.staff.panels.StaffSchedulePanel schedulePanel;
    private view.staff.panels.StaffDocumentPanel documentPanel;

    public StaffDashboard(User user) {
        this.currentUser = user;
        initComponents(); // Init View first
        this.staffController = new StaffController(this, currentUser); // Then Controller
    }

    private void initComponents() {
        setTitle("ODIN - Nhân Viên");
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

        JLabel userLabel = new JLabel(currentUser.getFullName() + " (Nhân viên)  ");
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

        sidebar.add(Box.createVerticalStrut(20));
        addMenuItem(sidebar, "Trang Chủ", e -> showDashboardHome());
        addMenuItem(sidebar, "Lịch & Điểm Danh", e -> switchContent("Lịch & Điểm Danh"));
        addMenuItem(sidebar, "Tài Liệu", e -> switchContent("Tài Liệu"));
        addMenuItem(sidebar, "Quản Lí Bài Viết", e -> switchContent("Quản Lí Bài Viết"));
        addMenuItem(sidebar, "CSKH", e -> switchContent("CSKH"));

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
        JLabel subtitle = UIUtils.createSecondaryLabel("Hệ thống nhân viên ODIN Language Center");

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

        statsPanel.add(createStatCard("Hỗ Trợ", "12", UIUtils.WARNING_COLOR));
        statsPanel.add(createStatCard("Lịch Học", "8", UIUtils.PRIMARY_COLOR));
        statsPanel.add(createStatCard("Bài Đăng", "5", UIUtils.ACCENT_COLOR));

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

    private void switchContent(String title) {
        contentPanel.removeAll();

        switch (title) {
            case "Lịch & Điểm Danh":
                if (schedulePanel == null) {
                    schedulePanel = new view.staff.panels.StaffSchedulePanel(staffController);
                }
                contentPanel.add(schedulePanel, BorderLayout.CENTER);
                break;

            case "Tài Liệu":
                if (documentPanel == null) {
                    documentPanel = new view.staff.panels.StaffDocumentPanel(staffController);
                }
                contentPanel.add(documentPanel, BorderLayout.CENTER);
                break;

            case "CSKH":
                if (supportPanel == null) {
                    supportPanel = new StaffSupportPanel(staffController);
                }
                contentPanel.add(supportPanel, BorderLayout.CENTER);
                break;

            case "Quản Lí Bài Viết":
                if (postPanel == null) {
                    postPanel = new StaffPostPanel(staffController);
                }
                contentPanel.add(postPanel, BorderLayout.CENTER);
                break;

            default:
                contentPanel.add(new JLabel("Đang phát triển: " + title));
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
