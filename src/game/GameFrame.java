package game;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Arjuna: The Last Bow");
        setIconImage(new ImageIcon(getClass().getResource("/assets/images/icon_arjuna.png")).getImage());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Menampilkan HomeScreen terlebih dahulu
        setContentPane(new HomeScreen(this));

        setVisible(true);
    }

    // Metode untuk mengganti panel
    public void changePanel(JPanel panel) {
        setContentPane(panel);
        revalidate();
        repaint();
    }
}
