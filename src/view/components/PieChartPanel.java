package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Custom modern Pie Chart using Java2D
 */
public class PieChartPanel extends JPanel {
    private Map<String, Integer> data;
    private String title;
    private Color[] colors = {
            new Color(37, 99, 235), // Blue 600
            new Color(14, 165, 233), // Sky 500
            new Color(99, 102, 241), // Indigo 500
            new Color(71, 85, 105), // Slate 600
            new Color(203, 213, 225) // Slate 300
    };

    public PieChartPanel(String title, Map<String, Integer> data) {
        this.title = title;
        this.data = data;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
    }

    public void setData(Map<String, Integer> data) {
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

        int total = 0;
        for (int value : data.values())
            total += value;
        if (total == 0)
            total = 1;

        int padding = 40;
        int size = Math.min(getWidth(), getHeight()) - (padding * 2) - 40;
        int x = (getWidth() - size) / 2 - 40;
        int y = (getHeight() - size) / 2 + 10;

        int startAngle = 0;
        int i = 0;

        // Legend start pos
        int legendX = x + size + 20;
        int legendY = y;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int angle = (int) Math.round((double) entry.getValue() / total * 360);

            g2d.setColor(colors[i % colors.length]);
            g2d.fillArc(x, y, size, size, startAngle, angle);

            // Draw Legend
            g2d.fillRect(legendX, legendY + (i * 25), 15, 15);
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2d.drawString(entry.getKey() + " (" + entry.getValue() + ")", legendX + 25, legendY + (i * 25) + 12);

            startAngle += angle;
            i++;
        }

        // Title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString(title, 20, 30);
    }
}
