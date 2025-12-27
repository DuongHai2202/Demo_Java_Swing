package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

/**
 * Custom modern Bar Chart using Java2D
 */
public class BarChartPanel extends JPanel {
    private Map<String, Number> data;
    private String title;
    private Color barColor = new Color(37, 99, 235); // Modern Blue (#2563EB)

    public BarChartPanel(String title, Map<String, Number> data) {
        this.title = title;
        this.data = data;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
    }

    public void setBarColor(Color color) {
        this.barColor = color;
    }

    public void setData(Map<String, Number> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty())
            return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int labelPadding = 30;
        int width = getWidth() - (2 * padding);
        int height = getHeight() - (2 * padding) - labelPadding;

        // Find max value
        double max = 0;
        for (Number value : data.values()) {
            if (value.doubleValue() > max)
                max = value.doubleValue();
        }
        if (max == 0)
            max = 1;

        // Draw Axes
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(padding, padding + height, padding + width, padding + height); // X axis
        g2d.drawLine(padding, padding, padding, padding + height); // Y axis

        // Draw Bars
        int count = data.size();
        int maxBarWidth = 60;
        int barWidth = Math.min(maxBarWidth, (width / count) - 20);
        int totalGraphWidth = (barWidth + 20) * count;
        int x = padding + (width - totalGraphWidth) / 2 + 10;

        for (Map.Entry<String, Number> entry : data.entrySet()) {
            double value = entry.getValue().doubleValue();
            int barHeight = (int) ((value / max) * height);

            // Gradient
            GradientPaint gp = new GradientPaint(
                    x, padding + height - barHeight, barColor,
                    x, padding + height, barColor.brighter());
            g2d.setPaint(gp);

            RoundRectangle2D bar = new RoundRectangle2D.Float(
                    x, padding + height - barHeight,
                    barWidth, barHeight, 10, 10);
            g2d.fill(bar);

            // Labels
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));

            // X label (Key) - Rotated to avoid overlap
            String key = entry.getKey();
            FontMetrics fm = g2d.getFontMetrics();

            // Limit label length
            if (key.length() > 15)
                key = key.substring(0, 12) + "...";

            int labelX = x + (barWidth / 2);
            int labelY = padding + height + 15;

            // Rotation for X labels
            java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
            g2d.translate(labelX, labelY);
            g2d.rotate(Math.toRadians(35)); // 35 degree tilt
            g2d.drawString(key, 0, 0);
            g2d.setTransform(oldTransform);

            // Value label
            String valStr = String.valueOf(entry.getValue());
            if (value >= 1000)
                valStr = String.format("%.1fk", value / 1000.0);
            int valX = x + (barWidth - fm.stringWidth(valStr)) / 2;
            g2d.drawString(valStr, valX, padding + height - barHeight - 8);

            x += barWidth + 20;
        }

        // Title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString(title, padding, padding - 15);
    }
}
