package game;

import javax.swing.*;
import java.awt.*;

public class BattleScene extends JPanel {
    public BattleScene(GameFrame frame) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("PERTEMPURAN DIMULAI!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 32));
        add(label, BorderLayout.CENTER);
    }
}
