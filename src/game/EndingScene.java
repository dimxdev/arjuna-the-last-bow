package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class EndingScene extends JPanel {
    private GameFrame frame;
    private Image background;
    private Clip sound;
    private RoundedButton homeButton, exitButton;

    public EndingScene(GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        // Load background image
        background = new ImageIcon(getClass().getResource("/assets/images/ending-scene.png")).getImage();

        // Play win sound loop
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/assets/sounds/win.wav"));
            sound = AudioSystem.getClip();
            sound.open(audioIn);
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Tombol Home 🏠
        homeButton = new RoundedButton("🏠");
        homeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeButton.setForeground(Color.BLACK);
        homeButton.setBackground(new Color(255, 255, 255, 180));
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
            if (sound != null) sound.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(homeButton);

        // Tombol Exit
        exitButton = new RoundedButton("EXIT");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(255, 255, 255, 180));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> showExitDialog());
        add(exitButton);

        // Responsif posisi tombol
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                homeButton.setBounds(w - 170, 20, 60, 30);
                exitButton.setBounds(w - 100, 20, 70, 30);
            }
        });
    }

    private void showExitDialog() {
        JDialog dialog = new JDialog(frame, "Konfirmasi Keluar", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new RoundedPanel(30, new Color(255, 255, 255));
        panel.setLayout(null);

        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/images/exit_icon.png")));
        iconLabel.setBounds(135, 50, 128, 128);
        panel.add(iconLabel);

        JLabel title = new JLabel("Keluar dari Game", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBounds(0, 180, 400, 30);
        panel.add(title);

        JLabel message = new JLabel("Yakin ingin keluar sekarang?", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setBounds(0, 210, 400, 30);
        panel.add(message);

        JButton yesButton = new JButton("Yakin");
        yesButton.setBounds(90, 280, 90, 40);
        yesButton.setBackground(new Color(200, 230, 255));
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yesButton.addActionListener(e -> System.exit(0));
        panel.add(yesButton);

        JButton noButton = new JButton("Batal");
        noButton.setBounds(220, 280, 90, 40);
        noButton.setBackground(new Color(220, 220, 220));
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noButton.addActionListener(e -> dialog.dispose());
        panel.add(noButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
