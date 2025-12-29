package view;

import controller.AuthController;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame - Màn hình đăng nhập với UI hiện đại
 */
public class LoginFrame extends JFrame {
    // Constants
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 600;
    private static final int SPACING_SMALL = 5;
    private static final int SPACING_MEDIUM = 10;
    private static final int SPACING_LARGE = 15;
    private static final int SPACING_XLARGE = 30;

    private static final Dimension FIELD_SIZE = new Dimension(350, 40);
    private static final Dimension BUTTON_SIZE = new Dimension(350, 45);
    private static final Dimension DEMO_STRIP_SIZE = new Dimension(350, 30);

    private static final Color FIELD_BORDER_COLOR = new Color(220, 220, 220);
    private static final Color BUTTON_HOVER_COLOR = new Color(33, 150, 243);

    // Components
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    // Controller
    private final AuthController authController;

    // Drag & Drop support
    private Point mouseDownCompCoords;

    public LoginFrame() {
        this.authController = new AuthController();
        initComponents();
    }

    /**
     * Khởi tạo UI components
     */
    private void initComponents() {
        setTitle("ODIN - Quản Trị Viên");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        mainPanel.add(createBrandPanel());
        mainPanel.add(createFormPanel());

        add(mainPanel);
        enableDragAndDrop();
    }

    /**
     * Tạo panel bên trái với gradient background
     */
    private JPanel createBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, UIUtils.PRIMARY_COLOR,
                        0, getHeight(), new Color(30, 136, 229));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridBagLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);

        JLabel lblLogo = new JLabel("ODIN");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 56));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTagline = new JLabel("Language Center");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        lblTagline.setForeground(new Color(255, 255, 255, 200));
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lblLogo);
        content.add(Box.createVerticalStrut(10));
        content.add(lblTagline);

        panel.add(content);
        return panel;
    }

    /**
     * Tạo panel bên phải với form đăng nhập
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        // Top buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(createSystemButton("─", e -> setState(JFrame.ICONIFIED)));
        topPanel.add(createSystemButton("×", e -> System.exit(0)));

        // Form center
        JPanel formCenter = createFormCenter();

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(formCenter, BorderLayout.CENTER);
        panel.add(createFooter(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFormCenter() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 0, 20, 0));

        addFormHeader(container);
        addLoginFields(container);
        addLoginButton(container);
        addDemoLinks(container);

        panel.add(container);
        return panel;
    }

    private void addFormHeader(JPanel container) {
        JLabel lblHeader = new JLabel("Đăng Nhập Hệ Thống");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSubtitle = new JLabel("Đăng nhập để vào hệ thống.");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitle.setForeground(Color.GRAY);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(lblHeader);
        container.add(Box.createVerticalStrut(SPACING_SMALL));
        container.add(lblSubtitle);
        container.add(Box.createVerticalStrut(SPACING_XLARGE));
    }

    private void addLoginFields(JPanel container) {
        JLabel lblUsername = new JLabel("Tài khoản");
        lblUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(lblUsername);
        container.add(Box.createVerticalStrut(SPACING_SMALL));

        txtUsername = createIconField("user");
        container.add(txtUsername);
        container.add(Box.createVerticalStrut(SPACING_MEDIUM));

        JLabel lblPassword = new JLabel("Mật khẩu");
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(lblPassword);
        container.add(Box.createVerticalStrut(SPACING_SMALL));

        txtPassword = createIconPasswordField("lock");
        container.add(txtPassword);
        container.add(Box.createVerticalStrut(SPACING_LARGE));
    }

    private void addLoginButton(JPanel container) {
        btnLogin = createStyledButton("ĐĂNG NHẬP",
                e -> authController.login(txtUsername.getText().trim(), new String(txtPassword.getPassword()), this));

        container.add(btnLogin);
        container.add(Box.createVerticalStrut(SPACING_LARGE));
    }

    private void addDemoLinks(JPanel container) {
        JLabel lblDemo = new JLabel("Truy cập nhanh (Demo):");
        lblDemo.setAlignmentX(Component.LEFT_ALIGNMENT);
        container.add(lblDemo);
        container.add(Box.createVerticalStrut(SPACING_SMALL));

        JPanel demoStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        demoStrip.setBackground(Color.WHITE);
        demoStrip.setAlignmentX(Component.LEFT_ALIGNMENT);
        demoStrip.setMaximumSize(DEMO_STRIP_SIZE);

        demoStrip.add(createDemoLink("Admin", "admin"));
        demoStrip.add(createDemoLink("Staff", "staff"));
        demoStrip.add(createDemoLink("Teacher", "teacher"));
        demoStrip.add(createDemoLink("Student", "student"));

        container.add(demoStrip);
    }

    private JLabel createFooter() {
        JLabel lblCopyright = new JLabel("© 2025 Odin Language Center", SwingConstants.CENTER);
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCopyright.setForeground(new Color(180, 180, 180));
        lblCopyright.setBorder(new EmptyBorder(5, 0, 10, 0));
        return lblCopyright;
    }

    private JTextField createIconField(String iconType) {
        JTextField field = new JTextField();
        styleIconField(field, iconType);
        return field;
    }

    private JPasswordField createIconPasswordField(String iconType) {
        JPasswordField field = new JPasswordField();
        styleIconField(field, iconType);
        return field;
    }

    /**
     * Style text field với icon bên trong
     */
    private void styleIconField(JTextField field, String iconType) {
        field.setMaximumSize(FIELD_SIZE);
        field.setPreferredSize(FIELD_SIZE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        javax.swing.border.Border lineBorder = BorderFactory.createLineBorder(FIELD_BORDER_COLOR);
        javax.swing.border.Border iconBorder = new AbstractBorder() {
            private final SimpleIcon icon = new SimpleIcon(iconType);

            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                int iconY = y + (height - icon.getIconHeight()) / 2;
                int iconX = x + 10;
                icon.paintIcon(c, g, iconX, iconY);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(5, 35, 5, 10);
            }

            @Override
            public Insets getBorderInsets(Component c, Insets insets) {
                insets.set(5, 35, 5, 10);
                return insets;
            }
        };

        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, iconBorder));
    }

    /**
     * Tạo button chính với rounded corners
     */
    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(UIUtils.PRIMARY_COLOR);
        button.setMaximumSize(BUTTON_SIZE);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(listener);
        button.addMouseListener(createHoverEffect(button, UIUtils.PRIMARY_COLOR, BUTTON_HOVER_COLOR));

        return button;
    }

    private JButton createSystemButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.GRAY);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        button.addMouseListener(createHoverEffect(button, Color.GRAY, UIUtils.DANGER_COLOR));

        return button;
    }

    private JLabel createDemoLink(String text, String role) {
        JLabel link = new JLabel(text);
        link.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        link.setForeground(Color.GRAY);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));

        link.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                link.setForeground(UIUtils.PRIMARY_COLOR);
                link.setText("<html><u>" + text + "</u></html>");
            }

            public void mouseExited(MouseEvent e) {
                link.setForeground(Color.GRAY);
                link.setText(text);
            }

            public void mouseClicked(MouseEvent e) {
                authController.fillDemoAccount(role, txtUsername, txtPassword);
            }
        });

        return link;
    }

    private MouseAdapter createHoverEffect(Component component, Color normalColor, Color hoverColor) {
        return new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (component instanceof JButton) {
                    ((JButton) component).setBackground(hoverColor);
                } else {
                    component.setForeground(hoverColor);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (component instanceof JButton) {
                    ((JButton) component).setBackground(normalColor);
                } else {
                    component.setForeground(normalColor);
                }
            }
        };
    }

    /**
     * SimpleIcon - Vẽ icon đơn giản cho login form
     */
    private static class SimpleIcon implements Icon {
        private final String type;
        private final int size = 16;

        public SimpleIcon(String type) {
            this.type = type;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            g2.setColor(Color.LIGHT_GRAY);

            if ("user".equals(type)) {
                g2.fillOval(4, 0, 8, 8);
                g2.fillArc(0, 9, 16, 12, 0, 180);
            } else {
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawArc(4, 0, 8, 10, 0, 180);
                g2.fillRect(2, 6, 12, 9);
                g2.setColor(Color.WHITE);
                g2.fillOval(7, 9, 2, 2);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }

    /**
     * Enable drag and drop cho window
     */
    private void enableDragAndDrop() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
            }
        });
    }
}
