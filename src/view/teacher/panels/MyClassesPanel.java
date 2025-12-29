package view.teacher.panels;

import controller.teacher.TeacherController;
import models.ClassSchedule;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MyClassesPanel - Hiển thị danh sách lớp được phân công
 */
public class MyClassesPanel extends JPanel {
    private TeacherController controller;
    private JTable tableClasses;
    private DefaultTableModel tableModel;

    public MyClassesPanel(TeacherController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIUtils.LIGHT_BG);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, UIUtils.SPACING_LG, 0));

        JLabel titleLabel = UIUtils.createHeaderLabel("Lớp Học Được Phân Công");
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JButton btnRefresh = UIUtils.createPrimaryButton("🔄 Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = { "Mã Lớp", "Tên Lớp", "Số Buổi", "Phòng Học", "Lịch Gần Nhất" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableClasses = new JTable(tableModel);
        tableClasses.setRowHeight(40);
        tableClasses.setFont(UIUtils.NORMAL_FONT);
        tableClasses.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableClasses.getTableHeader().setBackground(UIUtils.PRIMARY_COLOR);
        tableClasses.getTableHeader().setForeground(Color.WHITE);
        tableClasses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableClasses);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(UIUtils.SPACING_MD, 0, 0, 0));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            List<ClassSchedule> allSchedules = controller.getMySchedules();

            if (allSchedules.isEmpty()) {
                JLabel emptyLabel = new JLabel("Chưa được phân công lớp học nào", SwingConstants.CENTER);
                emptyLabel.setFont(UIUtils.NORMAL_FONT);
                emptyLabel.setForeground(Color.GRAY);

                JPanel emptyPanel = new JPanel(new GridBagLayout());
                emptyPanel.setBackground(Color.WHITE);
                emptyPanel.add(emptyLabel);

                add(emptyPanel, BorderLayout.CENTER);
                revalidate();
                return;
            }

            // Group by class_id
            Map<Integer, List<ClassSchedule>> byClass = allSchedules.stream()
                    .collect(Collectors.groupingBy(ClassSchedule::getClassId));

            for (Map.Entry<Integer, List<ClassSchedule>> entry : byClass.entrySet()) {
                List<ClassSchedule> classSchedules = entry.getValue();
                int sessionCount = classSchedules.size();

                // Get latest schedule for display
                ClassSchedule latest = classSchedules.stream()
                        .max(Comparator.comparing(ClassSchedule::getScheduleDate))
                        .orElse(classSchedules.get(0));

                String classCode = "LH-" + latest.getClassId();
                String className = latest.getClassName() != null ? latest.getClassName() : "Lớp " + latest.getClassId();
                String room = latest.getRoom() != null ? latest.getRoom() : "TBA";
                String lastSchedule = latest.getScheduleDate() + " " + latest.getStartTime();

                tableModel.addRow(new Object[] {
                        classCode,
                        className,
                        sessionCount + " buổi",
                        room,
                        lastSchedule
                });
            }

        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
