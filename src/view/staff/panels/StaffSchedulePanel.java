package view.staff.panels;

import controller.staff.StaffController;
import models.ClassSchedule;
import utils.UIUtils;
import view.staff.dialogs.AttendanceDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

/**
 * StaffSchedulePanel - Quản lý lịch học và điểm danh
 */
public class StaffSchedulePanel extends JPanel {
    private StaffController controller;
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JLabel lblDate;
    private List<ClassSchedule> schedules;

    public StaffSchedulePanel(StaffController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        loadData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);

        JLabel title = new JLabel("Lịch Học & Điểm Danh");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(UIUtils.LIGHT_BG);

        lblDate = new JLabel("Hôm nay: " + LocalDate.now().toString());
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());

        rightPanel.add(lblDate);
        rightPanel.add(Box.createHorizontalStrut(20));
        rightPanel.add(btnRefresh);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        String[] columns = { "ID", "Thời gian", "Lớp", "Giảng viên", "Phòng", "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scheduleTable = new JTable(tableModel);
        scheduleTable.setRowHeight(35);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Hide ID column
        scheduleTable.getColumnModel().getColumn(0).setMinWidth(0);
        scheduleTable.getColumnModel().getColumn(0).setMaxWidth(0);
        scheduleTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(UIUtils.LIGHT_BG);

        JButton btnAttendance = UIUtils.createPrimaryButton("Điểm Danh");
        btnAttendance.setPreferredSize(new Dimension(150, 40));
        btnAttendance.addActionListener(e -> handleAttendance());

        panel.add(btnAttendance);

        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            schedules = controller.getTodaySchedules();
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            if (schedules.isEmpty()) {
                // Nếu không có lịch hôm nay, hiển thị message
                UIUtils.showInfo(this, "Không có lịch học nào hôm nay");
            }

            for (ClassSchedule schedule : schedules) {
                String timeRange = timeFormat.format(schedule.getStartTime()) + " - " +
                        timeFormat.format(schedule.getEndTime());

                tableModel.addRow(new Object[] {
                        schedule.getId(),
                        timeRange,
                        "Lớp #" + schedule.getClassId(),
                        schedule.getTeacherName() != null ? schedule.getTeacherName() : "N/A",
                        schedule.getRoom() != null ? schedule.getRoom() : "N/A",
                        schedule.getStatus()
                });
            }
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAttendance() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarning(this, "Vui lòng chọn lớp để điểm danh!");
            return;
        }

        int scheduleId = (int) tableModel.getValueAt(selectedRow, 0);

        AttendanceDialog dialog = new AttendanceDialog((Frame) SwingUtilities.getWindowAncestor(this), scheduleId);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            loadData(); // Refresh sau khi điểm danh
        }
    }
}
