package view.admin.panels;

import controller.admin.AccountController;
import models.User;
import utils.UIUtils;
import view.admin.dialogs.AccountDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class AccountManagementPanel extends JPanel {
    private AccountController accountController;
    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel model;
    private List<User> allUsers;

    // Pagination fields
    private int currentPage = 1;
    private int pageSize = 10;
    private int totalPages = 1;
    private JLabel lblPageInfo;
    private JButton btnPrev;
    private JButton btnNext;
    private JComboBox<String> roleFilter;

    public AccountManagementPanel(JFrame parentFrame, AccountController accountController) {
        this.parentFrame = parentFrame;
        this.accountController = accountController;
        setLayout(new BorderLayout(15, 15));
        setOpaque(false);
        initComponents();
    }

    private void initComponents() {
        // Header & Filters
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(UIUtils.createHeaderLabel("Quản Lí Tài Khoản"), BorderLayout.WEST);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filters.setOpaque(false);

        JTextField searchField = new JTextField(15);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm tên, username...");

        roleFilter = new JComboBox<>(
                new String[] { "Tất cả chức vụ", "Quản trị viên", "Nhân viên", "Giảng viên", "Học viên" });

        JButton refreshBtn = new JButton("Làm mới");

        filters.add(new JLabel("Tìm kiếm: "));
        filters.add(searchField);
        filters.add(roleFilter);
        filters.add(refreshBtn);
        topPanel.add(filters, BorderLayout.EAST);

        // Table
        String[] cols = { "ID", "Username", "Họ Tên", "Chức Vụ", "Giới tính", "Ngày sinh", "Email", "SĐT", "Địa chỉ",
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

        loadData();

        // Action Buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionPanel.setOpaque(false);

        JButton addBtn = UIUtils.createPrimaryButton("Thêm tài khoản");
        JButton editBtn = UIUtils.createStyledButton("Sửa tài khoản", "roundRect", UIUtils.PRIMARY_COLOR);
        JButton deleteBtn = UIUtils.createDangerButton("Xóa tài khoản");
        JButton lockBtn = UIUtils.createStyledButton("Khóa/Mở Khóa", "roundRect", UIUtils.WARNING_COLOR);

        actionPanel.add(addBtn);
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(lockBtn);

        // Center Panel (Table + Pagination)
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(sp, BorderLayout.CENTER);
        centerPanel.add(paginationPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        // Listeners
        refreshBtn.addActionListener(e -> loadData());

        searchField.addActionListener(e -> filterData(searchField.getText(), (String) roleFilter.getSelectedItem()));
        roleFilter.addActionListener(e -> filterData(searchField.getText(), (String) roleFilter.getSelectedItem()));

        addBtn.addActionListener(e -> {
            AccountDialog dialog = new AccountDialog(parentFrame, null);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                if (accountController.addAccount(dialog.getUser())) {
                    UIUtils.showSuccess(parentFrame, "Thêm tài khoản thành công!");
                    loadData();
                } else {
                    UIUtils.showError(parentFrame, "Tên đăng nhập đã tồn tại!");
                }
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                User user = allUsers.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
                if (user != null) {
                    AccountDialog dialog = new AccountDialog(parentFrame, user);
                    dialog.setVisible(true);
                    if (dialog.isConfirmed()) {
                        if (accountController.updateAccount(dialog.getUser())) {
                            UIUtils.showSuccess(parentFrame, "Cập nhật tài khoản thành công!");
                            loadData();
                        } else {
                            UIUtils.showError(parentFrame, "Cập nhật thất bại!");
                        }
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một tài khoản!");
            }
        });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                String username = (String) model.getValueAt(row, 1);
                if (UIUtils.showConfirmation(parentFrame, "Bạn có chắc chắn muốn xóa tài khoản '" + username + "'?")) {
                    if (accountController.deleteAccount(id)) {
                        UIUtils.showSuccess(parentFrame, "Đã xóa tài khoản!");
                        loadData();
                    } else {
                        UIUtils.showError(parentFrame,
                                "Không thể xóa tài khoản này (có thể liên quan đến dữ liệu khác)!");
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một tài khoản!");
            }
        });

        lockBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) model.getValueAt(row, 0);
                User user = allUsers.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
                if (user != null) {
                    models.UserStatus currentStatus = user.getStatus();
                    String currentStatusDisplay = currentStatus != null ? currentStatus.getDisplayName()
                            : "Đang hoạt động";
                    String newStatusDisplay = "Đang hoạt động".equals(currentStatusDisplay) ? "Ngừng hoạt động"
                            : "Đang hoạt động";
                    String msg = "Đang hoạt động".equals(newStatusDisplay) ? "Mở khóa tài khoản này?"
                            : "Khóa tài khoản này?";
                    if (UIUtils.showConfirmation(parentFrame, msg)) {
                        models.UserStatus newStatus = models.UserStatus.fromDisplayName(newStatusDisplay);
                        user.setStatus(newStatus);
                        if (accountController.updateAccount(user)) {
                            UIUtils.showSuccess(parentFrame, "Cập nhật trạng thái thành công");
                            loadData();
                        }
                    }
                }
            } else {
                UIUtils.showError(parentFrame, "Vui lòng chọn một tài khoản!");
            }
        });

        // Pagination Listeners
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
    }

    private void loadData() {
        String role = (String) roleFilter.getSelectedItem();
        if (role == null || role.equals("Tất cả chức vụ")) {
            int total = accountController.getTotalUsersCount();
            totalPages = (int) Math.ceil((double) total / pageSize);
            if (totalPages == 0)
                totalPages = 1;
            allUsers = accountController.getUsersByPage(currentPage, pageSize);
        } else {
            models.UserRole userRole = models.UserRole.fromDisplayName(role);
            int total = accountController.getTotalUsersByRoleCount(userRole);
            totalPages = (int) Math.ceil((double) total / pageSize);
            if (totalPages == 0)
                totalPages = 1;
            allUsers = accountController.getUsersByRoleAndPage(userRole, currentPage, pageSize);
        }

        lblPageInfo.setText(String.format("Trang %d / %d", currentPage, totalPages));
        btnPrev.setEnabled(currentPage > 1);
        btnNext.setEnabled(currentPage < totalPages);

        renderTable(allUsers);
    }

    private void filterData(String keyword, String role) {
        String kw = keyword.toLowerCase();
        List<User> filtered = allUsers.stream()
                .filter(u -> (kw.isEmpty() || u.getFullName().toLowerCase().contains(kw)
                        || u.getUsername().toLowerCase().contains(kw)))
                .filter(u -> (role.equals("Tất cả chức vụ") || u.getRole().getDisplayName().equals(role)))
                .collect(Collectors.toList());
        renderTable(filtered);
    }

    private void renderTable(List<User> users) {
        model.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (User u : users) {
            model.addRow(new Object[] {
                    u.getId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getRole().getDisplayName(),
                    u.getGender() != null ? u.getGender() : "",
                    u.getDateOfBirth() != null ? sdf.format(u.getDateOfBirth()) : "",
                    u.getEmail(),
                    u.getPhone() != null ? u.getPhone() : "",
                    u.getAddress() != null ? u.getAddress() : "",
                    u.getStatus()
            });
        }
    }
}
