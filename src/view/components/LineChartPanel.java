package view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

/**
 * Custom modern Line Chart for trend visualization
 */
public class LineChartPanel extends JPanel {
    private Map<String, Number> data;
    private String title;
    private Color lineColor = new Color(37, 99, 235); // Blue 600

    public LineChartPanel(String title, Map<String, Number> data) {
        this.title = title;
        this.data = data;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 300));
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

        int padding = 60;
        int width = getWidth() - (2 * padding);
        int height = getHeight() - (2 * padding);

        // Sort data by key (usually months)
        List<String> keys = new ArrayList<>(data.keySet());

        // Find max value
        double max = 0;
        for (Number value : data.values()) {
            if (value.doubleValue() > max)
                max = value.doubleValue();
        }
        if (max == 0)
            max = 1;

        // Draw Axes
        g2d.setColor(new Color(226, 232, 240)); // Slate 200
        g2d.drawLine(padding, padding + height, padding + width, padding + height);
        g2d.drawLine(padding, padding, padding, padding + height);

        // Plot points
        int xStep = width / (keys.size() > 1 ? keys.size() - 1 : 1);
        List<Point2D> points = new ArrayList<>();

        for (int i = 0; i < keys.size(); i++) {
            int px = padding + (i * xStep);
            double val = data.get(keys.get(i)).doubleValue();
            int py = padding + height - (int) ((val / max) * height);
            points.add(new Point2D.Double(px, py));
        }

        // Draw Area Gradient (Fill under line)
        Path2D area = new Path2D.Double();
        if (!points.isEmpty()) {
            area.moveTo(points.get(0).getX(), padding + height);
            for (Point2D p : points)
                area.lineTo(p.getX(), p.getY());
            area.lineTo(points.get(points.size() - 1).getX(), padding + height);
            area.closePath();

            GradientPaint areaGp = new GradientPaint(
                    0, padding, new Color(37, 99, 235, 40),
                    0, padding + height, new Color(37, 99, 235, 0));
            g2d.setPaint(areaGp);
            g2d.fill(area);
        }

        // Draw Line
        g2d.setColor(lineColor);
        g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < points.size() - 1; i++) {
            g2d.draw(new Line2D.Double(points.get(i), points.get(i + 1)));
        }

        // Draw Points & Labels
        for (int i = 0; i < points.size(); i++) {
            Point2D p = points.get(i);
            String key = keys.get(i);
            double val = data.get(key).doubleValue();

            // Marker
            g2d.setColor(Color.WHITE);
            g2d.fill(new Ellipse2D.Double(p.getX() - 5, p.getY() - 5, 10, 10));
            g2d.setColor(lineColor);
            g2d.setStroke(new BasicStroke(2f));
            g2d.draw(new Ellipse2D.Double(p.getX() - 5, p.getY() - 5, 10, 10));

            // Labels
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));

            // X label
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(key, (int) p.getX() - fm.stringWidth(key) / 2, padding + height + 20);

            // Y value label
            String valStr = String.valueOf(val);
            if (val >= 1000)
                valStr = String.format("%.1fk", val / 1000.0);
            g2d.drawString(valStr, (int) p.getX() - fm.stringWidth(valStr) / 2, (int) p.getY() - 15);
        }

        // Title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2d.drawString(title, padding, padding - 20);
    }
}
