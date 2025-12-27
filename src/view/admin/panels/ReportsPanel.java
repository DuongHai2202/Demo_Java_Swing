package view.admin.panels;

import controller.admin.ReportingController;
import view.components.LineChartPanel;
import models.Transaction;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import java.awt.*;
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
                container.setBorder(new EmptyBorder(25, 25, 25, 25));

                // 1. Metric Cards Header (Refined & Smaller)
                JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
                metricsPanel.setOpaque(false);
                metricsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                lblTotalStudents = new JLabel("--");
                lblTotalTeachers = new JLabel("--");
                lblTotalStaff = new JLabel("--");
                lblTotalRevenue = new JLabel("--");

                metricsPanel.add(createMetricCard("HỌC VIÊN", lblTotalStudents, new Color(37, 99, 235)));
                metricsPanel.add(createMetricCard("GIÁO VIÊN", lblTotalTeachers, new Color(14, 165, 233)));
                metricsPanel.add(createMetricCard("NHÂN VIÊN", lblTotalStaff, new Color(99, 102, 241)));
                metricsPanel.add(createMetricCard("DOANH THU", lblTotalRevenue, new Color(2, 132, 199)));

                container.add(metricsPanel);
                container.add(Box.createVerticalStrut(25));

                // 2. Middle Tier: Line Chart (Trends)
                trendChart = new LineChartPanel("Xu Hướng Doanh Thu", new java.util.TreeMap<>());
                JPanel trendWrapper = wrapInCard(trendChart);
                trendWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));
                container.add(trendWrapper);
                container.add(Box.createVerticalStrut(25));

                // 3. Bottom Tier: Recent Activities
                JPanel bottomPanel = new JPanel(new BorderLayout());
                bottomPanel.setBackground(Color.WHITE);
                bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                                new EmptyBorder(15, 15, 15, 15)));
                bottomPanel.putClientProperty("FlatLaf.style", "arc: 12");

                JLabel lblActTitle = new JLabel("GIAO DỊCH GẦN ĐÂY");
                lblActTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
                lblActTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
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
                tableActivities.setIntercellSpacing(new Dimension(0, 0));
                tableActivities.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
                tableActivities.getTableHeader().setBackground(new Color(248, 250, 252));

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
                refreshTimer = new Timer(5000, e -> refreshData());
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
}
