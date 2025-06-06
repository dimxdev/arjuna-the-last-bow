package game;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int radius;
    private Color backgroundColor;

    public RoundedPanel(int radius, Color bgColor) {
        this.radius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
    }
}
