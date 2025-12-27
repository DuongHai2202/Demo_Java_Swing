package view.admin.panels;

import controller.admin.StaffController;
import models.Staff;
import models.User;
import utils.UIUtils;
import view.admin.dialogs.AddStaffDialog;
import view.admin.dialogs.EditStaffDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StaffManagementPanel extends JPanel {
    private StaffController staffController;
    private JTable table;
    private DefaultTableModel model;
    private JFrame parentFrame;

    // Pagination fields
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;
    private JLabel lblPageInfo;
    private JButton btnPrev;
    private JButton btnNext;

    public StaffManagementPanel(JFrame parentFrame, StaffController staffController) {
        this.parentFrame = parentFrame;
        this.staffController = staffController;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        initComponents();
    }

    private void initComponents() {
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(UIUtils.createHeaderLabel("Quản Lí Nhân Viên"), BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controls.setOpaque(false);

        JButton addButton = UIUtils.createSuccessButton("+ Thêm mới");
        JButton refreshButton = new JButton("Làm mới");

        controls.add(addButton);
        controls.add(refreshButton);
        headerPanel.add(controls, BorderLayout.EAST);

        // Table
        String[] columns = { "ID", "Mã NV", "Họ tên", "Chức vụ", "Phòng ban", "SĐT", "Email", "Trạng thái" };
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Pagination Panel
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        paginationPanel.setOpaque(false);
        btnPrev = new JButton("<<");
        btnNext = new JButton(">>");
        lblPageInfo = new JLabel("Trang 1 / 1");

        paginationPanel.add(btnPrev);
        paginationPanel.add(lblPageInfo);
        paginationPanel.add(btnNext);

        // Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        JButton editButton = UIUtils.createPrimaryButton("Sửa");
        JButton deleteButton = UIUtils.createDangerButton("Xóa");
        actions.add(editButton);
        actions.add(deleteButton);

        // Center Panel (Table + Pagination)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(paginationPanel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        // Listeners
        addButton.addActionListener(e -> {
            AddStaffDialog dialog = new AddStaffDialog(parentFrame);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                if (staffController.addStaff(dialog.getNewStaff())) {
                    UIUtils.showSuccess(parentFrame, "Thêm thành công");
                    fillStaffTable();
                } else {
                    UIUtils.showError(parentFrame, "Lỗi khi thêm nhân viên!");
                }
            }
        });

        refreshButton.addActionListener(e -> fillStaffTable());

        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                if (UIUtils.showConfirmation(parentFrame, "Xóa nhân viên này?")) {
                    int id = (int) model.getValueAt(row, 0);
                    // Lấy userId thực tế từ staff object nếu cần, ở đây deleteStaff nhận userId
                    Staff s = staffController.getStaffById(id);
                    if (s != null) {
                        staffController.deleteStaff(s.getUserId());
                        fillStaffTable();
                    }
                }
            }
        });

        editButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                Staff staff = staffController.getStaffById(id);
                EditStaffDialog dialog = new EditStaffDialog(parentFrame, staff);
                dialog.setVisible(true);
                if (dialog.isConfirmed()) {
                    if (staffController.updateStaff(staff)) {
                        UIUtils.showSuccess(parentFrame, "Cập nhật thành công");
                        fillStaffTable();
                    } else {
                        UIUtils.showError(parentFrame, "Lỗi khi cập nhật thông tin nhân viên!");
                    }
                }
            }
        });

        // Pagination Listeners
        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                fillStaffTable();
            }
        });

        btnNext.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                fillStaffTable();
            }
        });

        fillStaffTable();
    }

    private void fillStaffTable() {
        try {
            int total = staffController.getTotalStaffCount();
            totalPages = (int) Math.ceil((double) total / pageSize);
            if (totalPages == 0)
                totalPages = 1;

            lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages));
            btnPrev.setEnabled(currentPage > 1);
            btnNext.setEnabled(currentPage < totalPages);

            model.setRowCount(0);
            List<Staff> list = staffController.getStaffListPaginated(currentPage, pageSize);
            for (Staff s : list) {
                User u = s.getUser();
                model.addRow(new Object[] {
                        s.getId(),
                        s.getStaffCode(),
                        u.getFullName(),
                        s.getPosition() != null ? s.getPosition() : "",
                        s.getDepartment() != null ? s.getDepartment() : "",
                        u.getPhone(),
                        u.getEmail(),
                        u.getStatus()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showError(parentFrame, "Lỗi khi tải danh sách nhân viên: " + e.getMessage());
        }
    }
}
