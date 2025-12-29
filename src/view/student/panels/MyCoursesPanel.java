package view.student.panels;

import controller.student.StudentController;
// import models.Enrollment;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * MyCoursesPanel - Khóa học của tôi
 */
public class MyCoursesPanel extends JPanel {
    private StudentController controller;
    private JTable courseTable;
    private DefaultTableModel tableModel;

    public MyCoursesPanel(StudentController controller) {
        this.controller = controller;
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.LIGHT_BG);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.LIGHT_BG);

        JLabel title = new JLabel("Khóa Học Của Tôi");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UIUtils.PRIMARY_COLOR);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());

        header.add(title, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        String[] columns = { "Mã KH", "Khóa học", "Ngày đăng ký", "Trạng thái", "Học phí" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(35);
        courseTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        courseTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(null);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadData() {
        tableModel.setRowCount(0);

        try {
            // Enrollment feature currently disabled
            UIUtils.showInfo(this,
                    "Chức năng đăng ký khóa học đang phát triển.\nVui lòng xem tab 'Khóa Học' để duyệt danh sách khóa.");
        } catch (Exception e) {
            UIUtils.showError(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
