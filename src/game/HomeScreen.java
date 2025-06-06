package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen extends JPanel {

    private GameFrame frame;
    private Image background;

    private JLabel titleLabel, subtitleLabel;
    private RoundedButton playButton, exitButton;

    private SoundPlayer bgm; // Musik latar belakang

    public HomeScreen(GameFrame frame) {
        this.frame = frame;

        // Load background image
        background = new ImageIcon(getClass().getResource("/assets/images/home_bg.jpg")).getImage();
        setLayout(null); // manual layout

        // Inisialisasi dan putar musik
        bgm = new SoundPlayer("/assets/sounds/war.wav");
        bgm.playLoop();

        // Judul
        titleLabel = new JLabel("ARJUNA: THE LAST BOW", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        // Subjudul
        subtitleLabel = new JLabel("\"Seorang pemimpin belajar memanah demi menuntut balas\"", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        add(subtitleLabel);

        // Tombol Play
        playButton = new RoundedButton("Play");
        playButton.addActionListener(e -> {
            bgm.stop(); // stop musik saat ganti panel
            frame.changePanel(new CeritaScreen(frame));
        });
        add(playButton);

        // Tombol Exit
        exitButton = new RoundedButton("EXIT");
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        // Responsif saat resize
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    private void resizeComponents() {
        int w = getWidth();
        int h = getHeight();

        titleLabel.setBounds(w / 2 - 250, h / 8, 500, 50);
        subtitleLabel.setBounds(w / 2 - 300, h / 8 + 50, 600, 30);
        playButton.setBounds(w / 2 - 60, h - 150, 120, 50);
        exitButton.setBounds(w - 90, 10, 70, 30);
    }

    private void showCustomExitDialog() {
        JDialog dialog = new JDialog(frame, "Konfirmasi Keluar", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new RoundedPanel(30, new Color(255, 255, 255));
        panel.setLayout(null);
        panel.setBounds(0, 0, 400, 380);

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
        panel.add(yesButton);

        JButton noButton = new JButton("Gajadi ah");
        noButton.setBounds(220, 280, 90, 40);
        noButton.setBackground(new Color(220, 220, 220));
        noButton.setFocusPainted(false);
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(noButton);

        yesButton.addActionListener(e -> System.exit(0));
        noButton.addActionListener(e -> dialog.dispose());

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}
