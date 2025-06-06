package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FinalCeritaScene extends JPanel {
    private GameFrame frame;
    private JTextArea textArea;
    private RoundedButton lanjutButton, exitButton, homeButton;
    private Timer typingTimer;
    private String fullText;
    private int charIndex = 0;

    private SoundPlayer typingSound;

    public FinalCeritaScene(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.BLACK);

        // Area teks
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.BLACK);
        textArea.setFont(new Font("Serif", Font.PLAIN, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        add(textArea);

        // Narasi
        fullText = """
        Terima kasih telah membimbing Arjuna hingga tahap ini.

        Latihanmu membuatnya lebih kuat, lebih sigap, dan lebih berani.

        Kini saatnya Arjuna melangkah ke medan perang yang sesungguhnya...
        Dan meraih kemenangan untuk negerinya!
        """;

        // Suara ketik
        typingSound = new SoundPlayer("/assets/sounds/computer-keyboard-typing.wav");
        typingSound.playLoop();

        typingTimer = new Timer(40, e -> {
            if (charIndex < fullText.length()) {
                textArea.append(String.valueOf(fullText.charAt(charIndex++)));
            } else {
                typingTimer.stop();
                typingSound.stop();
            }
        });
        typingTimer.start();

        // Tombol Lanjut → ke EndingScene
        lanjutButton = new RoundedButton("Lanjut ▶");
        lanjutButton.addActionListener(e -> frame.changePanel(new EndingScene(frame)));
        add(lanjutButton);

        // Tombol Exit
        exitButton = new RoundedButton("EXIT");
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        // Tombol 🏠 Home
        homeButton = new RoundedButton("🏠");
        homeButton.setForeground(Color.BLACK);
        homeButton.setBackground(new Color(0, 0, 0, 150));
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> {
            typingSound.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(homeButton);

        // Responsif posisi
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();
                textArea.setBounds(w / 2 - 300, h / 4, 600, 200);
                lanjutButton.setBounds(w - 150, h - 80, 120, 45);
                exitButton.setBounds(w - 100, 20, 70, 30);
                homeButton.setBounds(w - 180, 20, 60, 30);
            }
        });
    }

    private void showCustomExitDialog() {
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
}
