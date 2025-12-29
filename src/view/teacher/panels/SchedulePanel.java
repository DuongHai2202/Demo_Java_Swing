package view.teacher.panels;

import controller.teacher.TeacherController;
import models.ClassSchedule;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * SchedulePanel - Hiển thị lịch dạy
 */
public class SchedulePanel extends JPanel {
    private TeacherController controller;
    private JTable tableSchedule;
    private DefaultTableModel tableModel;
    private JLabel lblTodayInfo;

    public SchedulePanel(TeacherController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIUtils.LIGHT_BG);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIUtils.LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIUtils.SPACING_LG, 0));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel titleLabel = UIUtils.createHeaderLabel("Lịch Giảng Dạy");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnRefresh = UIUtils.createPrimaryButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(UIUtils.SPACING_MD));

        // Today's schedule card
        JPanel todayCard = createTodayScheduleCard();
        todayCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(todayCard);
        mainPanel.add(Box.createVerticalStrut(UIUtils.SPACING_MD));

        // Full schedule table
        JPanel tablePanel = createScheduleTable();
        mainPanel.add(tablePanel);

        add(mainPanel, BorderLayout.CENTER);

        loadData();
    }

    private JPanel createTodayScheduleCard() {
        JPanel card = UIUtils.createCardPanel();
        card.setLayout(new BorderLayout(UIUtils.SPACING_MD, UIUtils.SPACING_MD));

        JLabel lblTitle = UIUtils.createSubheaderLabel("📅 Lịch Hôm Nay");
        lblTitle.setForeground(UIUtils.PRIMARY_COLOR);

        lblTodayInfo = new JLabel("Đang tải...");
        lblTodayInfo.setFont(UIUtils.NORMAL_FONT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblTodayInfo, BorderLayout.CENTER);

        return card;
    }

    private JPanel createScheduleTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = { "Ngày", "Thời Gian", "Lớp Học", "Phòng", "Trạng Thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSchedule = new JTable(tableModel);
        tableSchedule.setRowHeight(40);
        tableSchedule.setFont(UIUtils.NORMAL_FONT);
        tableSchedule.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableSchedule.getTableHeader().setBackground(UIUtils.PRIMARY_COLOR);
        tableSchedule.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tableSchedule);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        try {
            // Load today's schedules
            List<ClassSchedule> todaySchedules = controller.getTodaySchedules();
            if (todaySchedules.isEmpty()) {
                lblTodayInfo.setText("Không có lịch dạy hôm nay");
            } else {
                lblTodayInfo.setText(String.format("Có %d buổi dạy hôm nay", todaySchedules.size()));
            }

            // Load all schedules
            tableModel.setRowCount(0);
            List<ClassSchedule> allSchedules = controller.getMySchedules();

            if (allSchedules.isEmpty()) {
                lblTodayInfo.setText("Chưa có lịch giảng dạy");
                return;
            }

            for (ClassSchedule schedule : allSchedules) {
                String date = schedule.getScheduleDate() != null ? dateFormat.format(schedule.getScheduleDate())
                        : "N/A";
                String time = schedule.getStartTime() + " - " + schedule.getEndTime();
                String className = schedule.getClassName() != null ? schedule.getClassName()
                        : "Lớp " + schedule.getClassId();
                String room = schedule.getRoom() != null ? schedule.getRoom() : "TBA";
                String status = schedule.getStatus() != null ? schedule.getStatus() : "Đã lên lịch";

                tableModel.addRow(new Object[] {
                        date,
                        time,
                        className,
                        room,
                        status
                });
            }

        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi khi tải lịch: " + e.getMessage());
            lblTodayInfo.setText("Lỗi khi tải dữ liệu");
            e.printStackTrace();
        }
    }
}
