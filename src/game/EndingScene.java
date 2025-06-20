package game;

import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;

public class EndingScene extends JPanel {
    private GameFrame frame;
    private Image background;
    private Clip sound;
    private JButton homeButton, exitButton, mainLagiButton;

    public EndingScene(GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        // Background
        background = new ImageIcon(getClass().getResource("/assets/images/ending-scene2.png")).getImage();

        // Suara
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/assets/sounds/win.wav"));
            sound = AudioSystem.getClip();
            sound.open(audioIn);
            sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WARNA COKLAT
        Color woodBrown = new Color(121, 85, 72);
        Font buttonFont = new Font("SansSerif", Font.BOLD, 16);

        // Tombol HOME
        homeButton = new JButton("🏠 Beranda");
        homeButton.setFont(buttonFont);
        homeButton.setBackground(woodBrown);
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
            if (sound != null) sound.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(homeButton);

        // Tombol MAIN LAGI
        mainLagiButton = new JButton("🎯 Main Lagi");
        mainLagiButton.setFont(buttonFont);
        mainLagiButton.setBackground(woodBrown);
        mainLagiButton.setForeground(Color.WHITE);
        mainLagiButton.setFocusPainted(false);
        mainLagiButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainLagiButton.addActionListener(e -> {
            if (sound != null) sound.stop();
            frame.changePanel(new ArcheryGame(frame));
        });
        add(mainLagiButton);

        // Tombol EXIT
        exitButton = new JButton("Keluar");
        exitButton.setFont(buttonFont);
        exitButton.setBackground(woodBrown);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> showExitDialog());
        add(exitButton);

        // Posisi tombol tengah dan berjajar
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                int btnWidth = 160;
                int btnHeight = 45;
                int spacing = 15;

                int totalHeight = (btnHeight * 3) + (spacing * 2);
                int startY = h / 2 + 100 - totalHeight / 2;

                // Urutan: HOME - MAIN LAGI - EXIT
                homeButton.setBounds(w / 2 - btnWidth / 2, startY, btnWidth, btnHeight);
                mainLagiButton.setBounds(w / 2 - btnWidth / 2, startY + btnHeight + spacing, btnWidth, btnHeight);
                exitButton.setBounds(w / 2 - btnWidth / 2, startY + (btnHeight + spacing) * 2, btnWidth, btnHeight);
            }
        });
    }

    private void showExitDialog() {
        JDialog dialog = new JDialog(frame, "Konfirmasi Keluar", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);

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