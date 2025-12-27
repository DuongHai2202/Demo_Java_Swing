package view.admin.panels;

import controller.admin.StudentController;
import models.Student;
import models.User;
import utils.UIUtils;
import view.admin.dialogs.StudentDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class StudentManagementPanel extends JPanel {
    private StudentController studentController;
    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private List<Student> studentList;

    // Pagination fields
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;
    private JLabel lblPageInfo;
    private JButton btnPrev;
    private JButton btnNext;

    public StudentManagementPanel(JFrame parentFrame, StudentController studentController) {
        this.parentFrame = parentFrame;
        this.studentController = studentController;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(UIUtils.createHeaderLabel("Quản Lí Học Viên"), BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);
        JButton btnAdd = UIUtils.createSuccessButton("+ Thêm học viên");
        JButton btnRefresh = new JButton("Làm mới");
        controls.add(btnAdd);
        controls.add(btnRefresh);
        topPanel.add(controls, BorderLayout.EAST);

        // Table
        String[] cols = { "ID", "Mã HV", "Username", "Họ Tên", "Trình Độ", "Giới tính", "Ngày sinh", "Email", "SĐT",
                "Trạng Thái" };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());

        // Pagination Panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        paginationPanel.setOpaque(false);
        btnPrev = new JButton("<<");
        btnNext = new JButton(">>");
        lblPageInfo = new JLabel("Trang 1 / 1");
        paginationPanel.add(btnPrev);
        paginationPanel.add(lblPageInfo);
        paginationPanel.add(btnNext);

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);
        JButton btnEdit = UIUtils.createPrimaryButton("Sửa học viên");
        JButton btnDelete = UIUtils.createDangerButton("Xóa học viên");
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        // Layout Assembly
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(sp, BorderLayout.CENTER);
        centerPanel.add(paginationPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Listeners
        btnRefresh.addActionListener(e -> loadData());

        btnAdd.addActionListener(e -> {
            StudentDialog dialog = new StudentDialog(parentFrame, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                if (studentController.addStudent(dialog.getStudent())) {
                    UIUtils.showSuccess(parentFrame, "Thêm học viên thành công!");
                    loadData();
                } else {
                    UIUtils.showError(parentFrame,
                            "Không thể thêm học viên (có thể trùng tên đăng nhập hoặc mã học viên)!");
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int userId = (int) model.getValueAt(row, 0);
                Student s = studentList.stream().filter(std -> std.getUserId() == userId).findFirst().orElse(null);
                if (s != null) {
                    StudentDialog dialog = new StudentDialog(parentFrame, s);
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
                        if (studentController.updateStudent(dialog.getStudent())) {
                            UIUtils.showSuccess(parentFrame, "Cập nhật thành công!");
                            loadData();
                        } else {
                            UIUtils.showError(parentFrame, "Cập nhật thất bại!");
                        }
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một học viên!");
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int userId = (int) model.getValueAt(row, 0);
                String name = (String) model.getValueAt(row, 3);
                if (UIUtils.showConfirmation(parentFrame,
                        "Xóa học viên '" + name + "'? Dữ liệu người dùng cũng sẽ bị xóa.")) {
                    if (studentController.deleteStudent(userId)) {
                        UIUtils.showSuccess(parentFrame, "Đã xóa học viên!");
                        loadData();
                    } else {
                        UIUtils.showError(parentFrame, "Không thể xóa học viên!");
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một học viên!");
            }
        });

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadData();
            }
        });

        loadData();
    }

    private void loadData() {
        int total = studentController.getTotalStudentCount();
        totalPages = (int) Math.ceil((double) total / pageSize);
        if (totalPages == 0)
            totalPages = 1;

        lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages));
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);

        studentList = studentController.getStudentListPaginated(currentPage, pageSize);
        renderTable();
    }

    private void renderTable() {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Student s : studentList) {
            User u = s.getUser();
            model.addRow(new Object[] {
                    u.getId(),
                    s.getStudentCode(),
                    u.getUsername(),
                    u.getFullName(),
                    s.getCurrentLevel(),
                    u.getGender() != null ? u.getGender() : "",
                    u.getDateOfBirth() != null ? sdf.format(u.getDateOfBirth()) : "",
                    u.getEmail(),
                    u.getPhone(),
                    u.getStatus()
            });
        }
    }
}
