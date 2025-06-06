package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MonologScene extends JPanel {
    private GameFrame frame;
    private RoundedButton lanjutButton, exitButton;
    private Image backgroundImage, arjunaImage, monologBubble;

    public MonologScene(GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        // Load gambar
        backgroundImage = new ImageIcon(getClass().getResource("/assets/images/monolog_bg2.jpg")).getImage();
        arjunaImage = new ImageIcon(getClass().getResource("/assets/images/arjuna-removebg-preview.png")).getImage();
        monologBubble = new ImageIcon(getClass().getResource("/assets/images/teks-dialog.png")).getImage();

        // Tombol Lanjut
        lanjutButton = new RoundedButton("Lanjut ▶");
        lanjutButton.setBackground(new Color(0, 0, 0, 150));
        lanjutButton.setForeground(Color.BLACK);
        lanjutButton.setFocusPainted(false);
        lanjutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lanjutButton.addActionListener(e -> {
            ArcheryGame game = new ArcheryGame(frame);
            frame.changePanel(game);
            SwingUtilities.invokeLater(() -> game.requestFocusInWindow());
        });

        add(lanjutButton);

        // Tombol Exit
        exitButton = new RoundedButton("EXIT");
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        // Responsif
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                lanjutButton.setBounds(w - 160, h - 70, 120, 40);
                exitButton.setBounds(w - 90, 10, 70, 30);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        // Background
        g.drawImage(backgroundImage, 0, 0, w, h, this);

        // Arjuna (pojok kiri bawah)
        int arjunaWidth = 200, arjunaHeight = 190;
        int arjunaX = 10;
        int arjunaY = h - arjunaHeight - 30;
        g.drawImage(arjunaImage, arjunaX, arjunaY, arjunaWidth, arjunaHeight, this);

        // Bubble monolog (di atas Arjuna, agak ke kanan)
        int bubbleWidth = 260, bubbleHeight = 180;
        int bubbleX = arjunaX + arjunaWidth - 20;
        int bubbleY = arjunaY - bubbleHeight + 30;
        g.drawImage(monologBubble, bubbleX, bubbleY, bubbleWidth, bubbleHeight, this);
    }

    private void showCustomExitDialog() {
        JDialog dialog = new JDialog(frame, "Konfirmasi Keluar", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new RoundedPanel(30, Color.WHITE);
        panel.setLayout(null);

        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/images/exit_icon.png")));
        iconLabel.setBounds(135, 50, 128, 128);
        panel.add(iconLabel);

        JLabel title = new JLabel("Konfirmasi Keluar", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBounds(0, 180, 400, 30);
        panel.add(title);

        JLabel message = new JLabel("Apakah Anda yakin ingin keluar dari game?", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setBounds(0, 210, 400, 30);
        panel.add(message);

        JButton yesButton = new JButton("Yakin");
        yesButton.setBounds(90, 280, 90, 40);
        yesButton.setBackground(new Color(200, 230, 255));
        yesButton.setFocusPainted(false);
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yesButton.addActionListener(e -> System.exit(0));
        panel.add(yesButton);

        JButton noButton = new JButton("Gajadi ah");
        noButton.setBounds(220, 280, 90, 40);
        noButton.setBackground(new Color(220, 220, 220));
        noButton.setFocusPainted(false);
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noButton.addActionListener(e -> dialog.dispose());
        panel.add(noButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}
