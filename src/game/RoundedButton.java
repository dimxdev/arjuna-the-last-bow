package game;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.BLACK);
        setFont(new Font("SansSerif", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isRollover()) {
            g2.setColor(new Color(255, 255, 255, 220)); // Hover effect
        } else {
            g2.setColor(new Color(255, 255, 255, 180));
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Rounded corner
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }
}
