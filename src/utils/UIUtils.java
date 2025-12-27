package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Utility class cho UI components và styling
 * Updated for Vibrant/Modern UI
 */
public class UIUtils {

    // Brand Colors
    public static final Color PRIMARY_COLOR = new Color(13, 71, 161); // Deep Blue (#0D47A1)
    public static final Color SECONDARY_COLOR = new Color(217, 119, 6); // Sunset Gold (#D97706)
    public static final Color ACCENT_COLOR = new Color(46, 204, 113); // Emerald (Green)
    public static final Color WARNING_COLOR = new Color(241, 196, 15); // Sun Flower (Yellow)
    public static final Color DANGER_COLOR = new Color(231, 76, 60); // Alizarin (Red)
    public static final Color DARK_COLOR = new Color(44, 62, 80); // Midnight Blue
    public static final Color SUCCESS_COLOR = ACCENT_COLOR;
    public static final Color INFO_COLOR = new Color(149, 165, 166); // Concrete (Gray)

    // Backgrounds
    public static final Color LIGHT_BG = new Color(236, 240, 241); // Clouds
    public static final Color WHITE = Color.WHITE;

    public static final Color TEXT_PRIMARY = new Color(44, 62, 80);
    public static final Color TEXT_SECONDARY = new Color(127, 140, 141); // Concrete

    // Fonts
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    /**
     * Creates a JPanel with a gradient background
     */
    public static JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
    }

    /**
     * Create styled button using FlatLaf client properties
     */
    public static JButton createStyledButton(String text, String type, Color bg) {
        JButton button = new JButton(text);
        if (type != null) {
            button.putClientProperty("JButton.buttonType", type);
        }
        if (bg != null) {
            button.setBackground(bg);
            button.setForeground(Color.WHITE);
        }
        button.setFont(BUTTON_FONT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createPrimaryButton(String text) {
        return createStyledButton(text, "roundRect", PRIMARY_COLOR);
    }

    public static JButton createSuccessButton(String text) {
        return createStyledButton(text, "roundRect", ACCENT_COLOR);
    }

    public static JButton createDangerButton(String text) {
        return createStyledButton(text, "roundRect", DANGER_COLOR);
    }

    public static JTextField createStyledTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.putClientProperty("JTextField.placeholderText", "Nhập thông tin...");
        field.putClientProperty("JComponent.roundRect", false);
        field.putClientProperty("FlatLaf.style", "arc: 6");
        field.putClientProperty("JComponent.outline", "gray");
        return field;
    }

    public static JPasswordField createStyledPasswordField(int columns) {
        JPasswordField field = new JPasswordField(columns);
        field.putClientProperty("JTextField.placeholderText", "Nhập mật khẩu...");
        field.putClientProperty("JComponent.roundRect", false);
        field.putClientProperty("FlatLaf.style", "arc: 6");
        field.putClientProperty("JPasswordField.showRevealButton", true);
        return field;
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HEADER_FONT);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.putClientProperty("FlatLaf.style", "arc: 8; [light]background: #FFFFFF; [dark]background: #303030");
        return panel;
    }

    // Helper to keep compatibility if other code calls this
    public static JButton createStyledButton(String text, Color bgColor) {
        return createStyledButton(text, "roundRect", bgColor);
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean showConfirmation(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Xác nhận",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
