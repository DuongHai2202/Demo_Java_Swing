package view.teacher.panels;

import controller.teacher.TeacherController;
import models.Attendance;
import models.ClassSchedule;
import models.Student;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * AttendancePanel - Điểm danh học viên
 */
public class AttendancePanel extends JPanel {
    private TeacherController controller;
    private JComboBox<String> cbClass;
    private JComboBox<String> cbSchedule;
    private JTable tableStudents;
    private DefaultTableModel tableModel;

    private Map<Integer, List<ClassSchedule>> schedulesByClass;
    private List<ClassSchedule> currentClassSchedules;
    private ClassSchedule selectedSchedule;
    private List<Student> currentStudents;

    public AttendancePanel(TeacherController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(UIUtils.LIGHT_BG);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(UIUtils.LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(UIUtils.SPACING_LG, UIUtils.SPACING_LG, UIUtils.SPACING_LG,
                UIUtils.SPACING_LG));

        // Header
        JPanel headerPanel = createHeaderPanel();
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(UIUtils.SPACING_LG));

        // Selection panel
        JPanel selectionPanel = createSelectionPanel();
        selectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(selectionPanel);
        mainPanel.add(Box.createVerticalStrut(UIUtils.SPACING_MD));

        // Student table
        JPanel tablePanel = createStudentTable();
        mainPanel.add(tablePanel);
        mainPanel.add(Box.createVerticalStrut(UIUtils.SPACING_MD));

        // Action buttons
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        mainPanel.add(buttonPanel);

        add(mainPanel, BorderLayout.CENTER);

        loadClasses();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel titleLabel = UIUtils.createHeaderLabel("📋 Điểm Danh Học Viên");
        panel.add(titleLabel, BorderLayout.WEST);

        return panel;
    }

    private JPanel createSelectionPanel() {
        JPanel panel = UIUtils.createCardPanel();
        panel.setLayout(new GridLayout(2, 2, UIUtils.SPACING_MD, UIUtils.SPACING_SM));

        // Class selection
        JLabel lblClass = new JLabel("Chọn lớp:");
        lblClass.setFont(UIUtils.NORMAL_FONT);
        cbClass = new JComboBox<>();
        cbClass.setFont(UIUtils.NORMAL_FONT);
        cbClass.addActionListener(e -> onClassSelected());

        // Schedule selection
        JLabel lblSchedule = new JLabel("Chọn buổi học:");
        lblSchedule.setFont(UIUtils.NORMAL_FONT);
        cbSchedule = new JComboBox<>();
        cbSchedule.setFont(UIUtils.NORMAL_FONT);
        cbSchedule.addActionListener(e -> onScheduleSelected());

        panel.add(lblClass);
        panel.add(cbClass);
        panel.add(lblSchedule);
        panel.add(cbSchedule);

        return panel;
    }

    private JPanel createStudentTable() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        String[] columns = { "STT", "Mã SV", "Họ Tên", "Trạng Thái" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only status column is editable
            }
        };

        tableStudents = new JTable(tableModel);
        tableStudents.setRowHeight(40);
        tableStudents.setFont(UIUtils.NORMAL_FONT);
        tableStudents.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableStudents.getTableHeader().setBackground(UIUtils.PRIMARY_COLOR);
        tableStudents.getTableHeader().setForeground(Color.WHITE);

        // Status column with dropdown
        JComboBox<String> statusCombo = new JComboBox<>(new String[] {
                "Có mặt", "Vắng", "Muộn", "Có phép"
        });
        tableStudents.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(statusCombo));

        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tableStudents.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tableStudents.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(tableStudents);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtils.BORDER_COLOR));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, UIUtils.SPACING_MD, 0));
        panel.setOpaque(false);

        JButton btnSave = UIUtils.createPrimaryButton("💾 Lưu Điểm Danh");
        btnSave.addActionListener(e -> saveAttendance());

        JButton btnClear = UIUtils.createDangerButton("🔄 Làm mới");
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnSave);
        panel.add(btnClear);

        return panel;
    }

    private void loadClasses() {
        cbClass.removeAllItems();
        schedulesByClass = controller.getSchedulesGroupedByClass();

        if (schedulesByClass.isEmpty()) {
            cbClass.addItem("Không có lớp nào");
            return;
        }

        for (Integer classId : schedulesByClass.keySet()) {
            cbClass.addItem("Lớp " + classId);
        }
    }

    private void onClassSelected() {
        if (cbClass.getSelectedIndex() < 0)
            return;

        cbSchedule.removeAllItems();
        tableModel.setRowCount(0);

        String selected = (String) cbClass.getSelectedItem();
        if (selected == null || selected.equals("Không có lớp nào"))
            return;

        int classId = Integer.parseInt(selected.replace("Lớp ", ""));
        currentClassSchedules = schedulesByClass.get(classId);

        if (currentClassSchedules != null) {
            for (ClassSchedule schedule : currentClassSchedules) {
                String display = schedule.getScheduleDate() + " " + schedule.getStartTime();
                cbSchedule.addItem(display);
            }
        }
    }

    private void onScheduleSelected() {
        if (cbSchedule.getSelectedIndex() < 0)
            return;
        if (currentClassSchedules == null)
            return;

        int index = cbSchedule.getSelectedIndex();
        selectedSchedule = currentClassSchedules.get(index);

        loadStudents();
    }

    private void loadStudents() {
        tableModel.setRowCount(0);

        if (selectedSchedule == null)
            return;

        currentStudents = controller.getStudentsByClass(selectedSchedule.getClassId());
        Map<Integer, Attendance> existingAttendance = new HashMap<>();

        // Load existing attendance
        List<Attendance> existing = controller.getAttendanceForSchedule(selectedSchedule.getId());
        for (Attendance att : existing) {
            existingAttendance.put(att.getStudentId(), att);
        }

        int stt = 1;
        for (Student student : currentStudents) {
            String status = "Có mặt";
            if (existingAttendance.containsKey(student.getId())) {
                status = existingAttendance.get(student.getId()).getStatus();
            }

            tableModel.addRow(new Object[] {
                    stt++,
                    student.getStudentCode(),
                    student.getUser() != null ? student.getUser().getFullName() : "N/A",
                    status
            });
        }
    }

    private void saveAttendance() {
        if (selectedSchedule == null) {
            UIUtils.showWarning(this, "Vui lòng chọn buổi học");
            return;
        }

        if (currentStudents == null || currentStudents.isEmpty()) {
            UIUtils.showWarning(this, "Không có học viên nào");
            return;
        }

        List<Attendance> attendanceList = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            Student student = currentStudents.get(i);
            String status = (String) tableModel.getValueAt(i, 3);

            Attendance attendance = new Attendance();
            attendance.setScheduleId(selectedSchedule.getId());
            attendance.setStudentId(student.getId());
            attendance.setStatus(status);

            attendanceList.add(attendance);
        }

        if (controller.saveAttendanceBatch(attendanceList)) {
            UIUtils.showSuccess(this, "Đã lưu điểm danh thành công!");
        } else {
            UIUtils.showError(this, "Lỗi khi lưu điểm danh");
        }
    }

    private void clearForm() {
        tableModel.setRowCount(0);
        cbClass.setSelectedIndex(-1);
        cbSchedule.removeAllItems();
        selectedSchedule = null;
        currentStudents = null;
    }
}
