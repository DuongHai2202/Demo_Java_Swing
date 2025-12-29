package view.staff.dialogs;

import models.Attendance;
import models.Student;
import controller.staff.StaffController;
import service.StudentService;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AttendanceDialog - Dialog điểm danh học viên
 */
public class AttendanceDialog extends JDialog {
    private int scheduleId;
    private StaffController controller;
    private StudentService studentService;

    private JPanel studentsPanel;
    private Map<Integer, JComboBox<String>> statusMap;
    private boolean confirmed = false;

    public AttendanceDialog(Frame owner, int scheduleId) {
        super(owner, "Điểm Danh", true);
        this.scheduleId = scheduleId;
        this.studentService = new StudentService();
        this.statusMap = new HashMap<>();

        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createStudentsPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("Điểm Danh Học Viên");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        panel.add(title, BorderLayout.WEST);

        return panel;
    }

    private JScrollPane createStudentsPanel() {
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new BoxLayout(studentsPanel, BoxLayout.Y_AXIS));
        studentsPanel.setBackground(Color.WHITE);
        studentsPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(studentsPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        JButton btnCancel = new JButton("Hủy");
        btnCancel.setPreferredSize(new Dimension(100, 38));
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = UIUtils.createPrimaryButton("Lưu");
        btnSave.setPreferredSize(new Dimension(100, 38));
        btnSave.addActionListener(e -> handleSave());

        panel.add(btnCancel);
        panel.add(btnSave);

        return panel;
    }

    private void loadStudents() {
        studentsPanel.removeAll();

        // Lấy danh sách học viên (trang 1, 20 học viên)
        List<Student> students = studentService.getStudentsPaginated(1, 20);

        String[] statuses = { "Có mặt", "Vắng", "Muộn", "Vắng có phép" };

        for (Student student : students) {
            JPanel studentRow = new JPanel(new BorderLayout(10, 0));
            studentRow.setBackground(Color.WHITE);
            studentRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            studentRow.setBorder(new EmptyBorder(5, 0, 5, 0));

            String studentName = student.getUser() != null ? student.getUser().getFullName() : "Unknown";
            JLabel lblName = new JLabel(studentName + " (" + student.getStudentCode() + ")");
            lblName.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            JComboBox<String> comboStatus = new JComboBox<>(statuses);
            comboStatus.setPreferredSize(new Dimension(150, 32));
            comboStatus.putClientProperty("FlatLaf.style", "arc: 6");

            statusMap.put(student.getId(), comboStatus);

            studentRow.add(lblName, BorderLayout.CENTER);
            studentRow.add(comboStatus, BorderLayout.EAST);

            studentsPanel.add(studentRow);
        }

        studentsPanel.revalidate();
        studentsPanel.repaint();
    }

    private void handleSave() {
        List<Attendance> attendanceList = new ArrayList<>();

        for (Map.Entry<Integer, JComboBox<String>> entry : statusMap.entrySet()) {
            int studentId = entry.getKey();
            String status = (String) entry.getValue().getSelectedItem();

            Attendance attendance = new Attendance();
            attendance.setScheduleId(scheduleId);
            attendance.setStudentId(studentId);
            attendance.setStatus(status);

            attendanceList.add(attendance);
        }

        // Save via service directly since controller might not be set
        service.AttendanceService attendanceService = new service.AttendanceService();
        if (attendanceService.markAttendanceBatch(attendanceList)) {
            confirmed = true;
            UIUtils.showSuccess(this, "Đã lưu điểm danh thành công!");
            dispose();
        } else {
            UIUtils.showError(this, "Lỗi khi lưu điểm danh!");
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
