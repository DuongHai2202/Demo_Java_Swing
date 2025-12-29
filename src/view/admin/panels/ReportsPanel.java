package view.admin.panels;

import controller.admin.ReportingController;
import view.components.LineChartPanel;
import models.Transaction;
import utils.UIUtils;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import javax.swing.border.EmptyBorder;

public class ReportsPanel extends JPanel {
        private ReportingController reportingController;
        private JLabel lblTotalStudents, lblTotalTeachers, lblTotalStaff, lblTotalRevenue;
        private LineChartPanel trendChart;
        private Timer refreshTimer;
        private JTable tableActivities;
        private DefaultTableModel tableModel;

        public ReportsPanel(JFrame parentFrame, ReportingController reportingController) {
                this.reportingController = reportingController;
                setLayout(new BorderLayout());
                setOpaque(false);
                initComponents();
        }

        private void initComponents() {
                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
                container.setOpaque(false);
                container.setBorder(new EmptyBorder(UIUtils.SPACING_XL, UIUtils.SPACING_XL, UIUtils.SPACING_XL,
                                UIUtils.SPACING_XL));

                // Header with Title and Controls
                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setOpaque(false);
                headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JLabel titleLabel = UIUtils.createHeaderLabel("Báo Cáo & Thống Kê");
                headerPanel.add(titleLabel, BorderLayout.WEST);

                // Filter and Export Controls
                JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, UIUtils.SPACING_SM, 0));
                controlsPanel.setOpaque(false);

                JComboBox<String> dateFilter = new JComboBox<>(new String[] {
                                "7 ngày qua", "30 ngày qua", "3 tháng qua", "Năm nay"
                });
                dateFilter.putClientProperty("JComponent.roundRect", true);

                JComboBox<String> typeFilter = new JComboBox<>(new String[] {
                                "Tất cả", "Học phí", "Đăng ký", "Hoàn tiền"
                });
                typeFilter.putClientProperty("JComponent.roundRect", true);

                JButton btnRefresh = UIUtils.createPrimaryButton("🔄 Làm mới");
                JButton btnExport = UIUtils.createSuccessButton("📊 Xuất CSV");
                btnExport.addActionListener(e -> exportToCSV());

                controlsPanel.add(new JLabel("Thời gian:"));
                controlsPanel.add(dateFilter);
                controlsPanel.add(Box.createHorizontalStrut(UIUtils.SPACING_SM));
                controlsPanel.add(new JLabel("Loại:"));
                controlsPanel.add(typeFilter);
                controlsPanel.add(Box.createHorizontalStrut(UIUtils.SPACING_SM));
                controlsPanel.add(btnRefresh);
                controlsPanel.add(btnExport);

                headerPanel.add(controlsPanel, BorderLayout.EAST);
                container.add(headerPanel);
                container.add(Box.createVerticalStrut(UIUtils.SPACING_LG));

                // Metric Cards
                JPanel metricsPanel = new JPanel(new GridLayout(1, 4, UIUtils.SPACING_MD, 0));
                metricsPanel.setOpaque(false);
                metricsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                lblTotalStudents = new JLabel("--");
                lblTotalTeachers = new JLabel("--");
                lblTotalStaff = new JLabel("--");
                lblTotalRevenue = new JLabel("--");

                metricsPanel.add(createMetricCard("HỌC VIÊN", lblTotalStudents, UIUtils.PRIMARY_COLOR));
                metricsPanel.add(createMetricCard("GIÁO VIÊN", lblTotalTeachers, UIUtils.ACCENT_COLOR));
                metricsPanel.add(createMetricCard("NHÂN VIÊN", lblTotalStaff, UIUtils.SECONDARY_COLOR));
                metricsPanel.add(createMetricCard("DOANH THU", lblTotalRevenue, UIUtils.WARNING_COLOR));

                container.add(metricsPanel);
                container.add(Box.createVerticalStrut(UIUtils.SPACING_LG));

                // Line Chart
                trendChart = new LineChartPanel("Xu Hướng Doanh Thu", new java.util.TreeMap<>());
                JPanel trendWrapper = wrapInCard(trendChart);
                trendWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
                container.add(trendWrapper);
                container.add(Box.createVerticalStrut(UIUtils.SPACING_LG));

                // Recent Transactions Table
                JPanel bottomPanel = new JPanel(new BorderLayout());
                bottomPanel.setBackground(Color.WHITE);
                bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(UIUtils.BORDER_COLOR, 1),
                                new EmptyBorder(UIUtils.SPACING_MD, UIUtils.SPACING_MD, UIUtils.SPACING_MD,
                                                UIUtils.SPACING_MD)));
                bottomPanel.putClientProperty("FlatLaf.style", "arc: 8");

                JLabel lblActTitle = UIUtils.createSubheaderLabel("GIAO DỊCH GẦN ĐÂY");
                lblActTitle.setBorder(new EmptyBorder(0, 0, UIUtils.SPACING_SM, 0));
                bottomPanel.add(lblActTitle, BorderLayout.NORTH);

                String[] cols = { "Mã GD", "Học viên", "Số tiền", "Loại", "Ngày" };
                tableModel = new DefaultTableModel(cols, 0) {
                        @Override
                        public boolean isCellEditable(int r, int c) {
                                return false;
                        }
                };
                tableActivities = new JTable(tableModel);
                tableActivities.setRowHeight(35);
                tableActivities.setShowGrid(false);
                tableActivities.setFont(UIUtils.NORMAL_FONT);
                tableActivities.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tableActivities.getTableHeader().setBackground(UIUtils.LIGHT_BG);

                JScrollPane tableScroll = new JScrollPane(tableActivities);
                tableScroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 300));
                tableScroll.setBorder(null);
                bottomPanel.add(tableScroll, BorderLayout.CENTER);

                container.add(bottomPanel);

                JScrollPane mainScroll = new JScrollPane(container);
                mainScroll.setBorder(null);
                mainScroll.setOpaque(false);
                mainScroll.getViewport().setOpaque(false);
                mainScroll.getVerticalScrollBar().setUnitIncrement(16);

                add(mainScroll, BorderLayout.CENTER);

                refreshData();
                refreshTimer = new Timer(10000, e -> refreshData()); // 10 seconds
                refreshTimer.start();
        }

        private void refreshData() {
                Map<String, Integer> stats = reportingController.getSummaryStats();
                Map<String, java.math.BigDecimal> revenueData = reportingController.getMonthlyRevenue();
                java.math.BigDecimal totalRev = revenueData.values().stream().reduce(java.math.BigDecimal.ZERO,
                                java.math.BigDecimal::add);
                NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

                lblTotalStudents.setText(stats.get("STUDENTS").toString());
                lblTotalTeachers.setText(stats.get("TEACHERS").toString());
                lblTotalStaff.setText(stats.get("STAFF").toString());
                lblTotalRevenue.setText(nf.format(totalRev).replace("₫", "").trim()); // Cleaner for metric

                // Update Trend Chart
                Map<String, Number> trendMap = new java.util.TreeMap<>(); // TreeMap to keep keys sorted
                revenueData.forEach((k, v) -> trendMap.put(k, v));
                trendChart.setData(trendMap);

                // Update Activities
                List<Transaction> activities = reportingController.getRecentActivities();
                tableModel.setRowCount(0);
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM HH:mm");
                for (Transaction t : activities) {
                        tableModel.addRow(new Object[] {
                                        t.getTransactionCode(),
                                        t.getStudentName(),
                                        nf.format(t.getAmount()),
                                        t.getTransactionType(),
                                        sdf.format(t.getTransactionDate())
                        });
                }
        }

        @Override
        public void removeNotify() {
                super.removeNotify();
                if (refreshTimer != null)
                        refreshTimer.stop();
        }

        private JPanel createMetricCard(String title, JLabel valLabel, Color accent) {
                JPanel card = new JPanel(new BorderLayout(5, 0));
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(235, 235, 235), 1),
                                new EmptyBorder(12, 15, 12, 15)));
                card.putClientProperty("FlatLaf.style", "arc: 12");

                JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 2));
                textPanel.setOpaque(false);

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
                titleLabel.setForeground(new Color(100, 116, 139));

                valLabel.setFont(new Font("Segoe UI Black", Font.BOLD, 22));
                valLabel.setForeground(Color.BLACK);

                textPanel.add(titleLabel);
                textPanel.add(valLabel);

                JPanel accentStrip = new JPanel();
                accentStrip.setBackground(accent);
                accentStrip.setPreferredSize(new Dimension(4, 0));

                card.add(accentStrip, BorderLayout.WEST);
                card.add(textPanel, BorderLayout.CENTER);
                return card;
        }

        private JPanel wrapInCard(JPanel panel) {
                JPanel card = new JPanel(new BorderLayout());
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
                card.add(panel, BorderLayout.CENTER);
                card.putClientProperty("FlatLaf.style", "arc: 12");
                return card;
        }

        private void exportToCSV() {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Xuất báo cáo CSV");
                fileChooser.setSelectedFile(new File("bao_cao_" + System.currentTimeMillis() + ".csv"));

                int userSelection = fileChooser.showSaveDialog(this);
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();

                        try (FileWriter writer = new FileWriter(fileToSave)) {
                                // Write header
                                writer.write("Mã GD,Học viên,Số tiền,Loại,Ngày\n");

                                // Write data
                                for (int i = 0; i < tableModel.getRowCount(); i++) {
                                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                                                writer.write(String.valueOf(tableModel.getValueAt(i, j)));
                                                if (j < tableModel.getColumnCount() - 1) {
                                                        writer.write(",");
                                                }
                                        }
                                        writer.write("\n");
                                }

                                UIUtils.showSuccess(this, "Đã xuất báo cáo thành công!");
                        } catch (IOException ex) {
                                UIUtils.showError(this, "Lỗi khi xuất file: " + ex.getMessage());
                        }
                }
        }
}
