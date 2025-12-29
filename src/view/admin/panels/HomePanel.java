package view.admin.panels;

import utils.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard Home Panel
 */
public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIUtils.LIGHT_BG);

        // Welcome Card
        JPanel welcomeCard = UIUtils.createCardPanel();
        welcomeCard.setLayout(new BorderLayout(UIUtils.SPACING_MD, UIUtils.SPACING_MD));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel welcomeLabel = UIUtils.createHeaderLabel("Bảng Điều Khiển Quản Trị");
        JLabel subtitle = UIUtils.createSecondaryLabel("ODIN Language Center - Dashboard Overview");

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(UIUtils.SPACING_SM));
        textPanel.add(subtitle);

        welcomeCard.add(textPanel, BorderLayout.CENTER);

        add(welcomeCard);
        add(Box.createVerticalStrut(UIUtils.SPACING_LG));

        // System Stats Cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, UIUtils.SPACING_MD, UIUtils.SPACING_MD));
        statsPanel.setOpaque(false);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        statsPanel.add(createStatCard("Học Viên", "150", UIUtils.PRIMARY_COLOR));
        statsPanel.add(createStatCard("Giảng Viên", "25", UIUtils.ACCENT_COLOR));
        statsPanel.add(createStatCard("Nhân Viên", "10", UIUtils.SECONDARY_COLOR));
        statsPanel.add(createStatCard("Khóa Học", "30", UIUtils.WARNING_COLOR));
        statsPanel.add(createStatCard("Giao Dịch", "45", UIUtils.SUCCESS_COLOR));
        statsPanel.add(createStatCard("Hỗ Trợ", "8", UIUtils.DANGER_COLOR));

        add(statsPanel);
        add(Box.createVerticalGlue());
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
}
