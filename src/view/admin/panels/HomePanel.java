package view.admin.panels;

import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * HomePanel - Trang chủ dashboard admin
 */
public class HomePanel extends JPanel {

    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(UIUtils.LIGHT_BG);
        initComponents();
    }

    /**
     * Khởi tạo UI components
     */
    private void initComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        JLabel lblWelcome = new JLabel("Chào mừng đến với ODIN Language Center");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(UIUtils.PRIMARY_COLOR);

        JLabel lblSubtitle = new JLabel("Hệ thống quản lý trung tâm ngoại ngữ");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblSubtitle.setForeground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        centerPanel.add(lblWelcome, gbc);

        gbc.gridy = 1;
        centerPanel.add(lblSubtitle, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
}
