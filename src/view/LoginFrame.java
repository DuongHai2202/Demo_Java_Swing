package view;

import controller.AuthController;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * LoginFrame - Màn hình đăng nhập hệ thống ODIN Language Center
 */
public class LoginFrame extends JFrame {
    // Constants
    private static final int WINDOW_WIDTH = 960;
    private static final int WINDOW_HEIGHT = 560;
    private static final int BORDER_RADIUS = 16;
    private static final int SPACING_SMALL = 5;
    private static final int SPACING_MEDIUM = 15;
    private static final int SPACING_LARGE = 25;
    private static final int SPACING_XLARGE = 30;
    private static final Dimension FIELD_SIZE = new Dimension(400, 40);
    private static final Dimension BUTTON_SIZE = new Dimension(400, 45);
    private static final Dimension DEMO_STRIP_SIZE = new Dimension(400, 30);
    private static final Color BRAND_COLOR_TOP = new Color(25, 118, 210);
    private static final Color BRAND_COLOR_BOTTOM = new Color(13, 71, 161);
    private static final Color BUTTON_HOVER_COLOR = new Color(21, 101, 192);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color FIELD_BORDER_COLOR = new Color(200, 200, 200);
    
    // Fields
    private final AuthController authController;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private int posX = 0, posY = 0;
    
    public LoginFrame() {
        this.authController = new AuthController();
        initComponents();
    }
    
    /**
     * Khởi tạo và cấu hình các components chính
     */
    private void initComponents() {
        setTitle("ODIN Language Center");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        
        try {
            setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS));
        } catch (Exception ignored) {}
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        setContentPane(mainPanel);
        
        JPanel brandPanel = createBrandPanel();
        JPanel formPanel = createFormPanel();
        
        mainPanel.add(brandPanel);
        mainPanel.add(formPanel);
        
        addDragSupport(brandPanel);
        addDragSupport(formPanel);
    }
    
    /**
     * Thêm chức năng kéo thả cửa sổ
     */
    private void addDragSupport(Component comp) {
        comp.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                posX = e.getX();
                posY = e.getY();
            }
        });
        
        comp.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
            }
        });
    }
    
    /**
     * Tạo panel branding với gradient
     */
    private JPanel createBrandPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(0, 0, BRAND_COLOR_TOP, 0, getHeight(), BRAND_COLOR_BOTTOM);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(new Color(255, 255, 255, 10));
                g2.drawOval(-100, getHeight() - 200, 400, 400);
                
                drawBranding(g2);
            }
            
            private void drawBranding(Graphics2D g2) {
                int centerY = getHeight() / 2;
                
                drawCenteredText(g2, "ODIN", new Font("Segoe UI", Font.BOLD, 48), Color.WHITE, centerY - 20);
                drawCenteredText(g2, "LANGUAGE CENTER", new Font("Segoe UI", Font.PLAIN, 16), Color.WHITE, centerY + 10);
                drawCenteredText(g2, "Professional Education System", new Font("Segoe UI", Font.ITALIC, 13), 
                    new Color(255, 255, 255, 180), getHeight() - 60);
            }
            
            private void drawCenteredText(Graphics2D g2, String text, Font font, Color color, int y) {
                g2.setFont(font);
                g2.setColor(color);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                g2.drawString(text, x, y);
            }
        };
    }
    
    /**
     * Tạo panel form đăng nhập
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        panel.add(createTopBar(), BorderLayout.NORTH);
        panel.add(createFormCenter(), BorderLayout.CENTER);
        panel.add(createFooter(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topBar.setBackground(Color.WHITE);
        
        topBar.add(createSystemButton("—", e -> setState(Frame.ICONIFIED)));
        topBar.add(createSystemButton("✕", e -> System.exit(0)));
        
        return topBar;
    }
    
    /**
     * Tạo phần trung tâm chứa form
     */
    private JPanel createFormCenter() {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(new EmptyBorder(10, 50, 10, 50));
        
        center.add(Box.createVerticalGlue());
        addFormHeader(center);
        addLoginFields(center);
        addLoginButton(center);
        addDemoLinks(center);
        center.add(Box.createVerticalGlue());
        
        return center;
    }
    
    private void addFormHeader(JPanel container) {
        JLabel lblHeader = new JLabel("Chào mừng");
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblHeader.setForeground(UIUtils.PRIMARY_COLOR);
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
        demoStrip.add(createDemoLink("Staff", "staff1"));
        demoStrip.add(createDemoLink("Teacher", "teacher1"));
        demoStrip.add(createDemoLink("Student", "student1"));
        
        container.add(demoStrip);
    }
    
    private JLabel createFooter() {
        JLabel lblCopyright = new JLabel("© 2024 Odin Language Center", SwingConstants.CENTER);
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
        javax.swing.border.Border iconBorder = new javax.swing.border.AbstractBorder() {
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
    
    private JLabel createDemoLink(String text, String username) {
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
                authController.fillDemoAccount(username.replace("1", ""), txtUsername, txtPassword);
            }
        });
        
        return link;
    }
    
    /**
     * Tạo hover effect cho button/label
     */
    private MouseAdapter createHoverEffect(JComponent component, Color normalColor, Color hoverColor) {
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
}
