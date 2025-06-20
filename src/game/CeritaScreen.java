package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CeritaScreen extends JPanel {
    private GameFrame frame;
    private JTextArea textArea;
    private RoundedButton lanjutButton, exitButton, backButton;
    private Timer typingTimer;
    private String fullText;
    private int charIndex = 0;

    private SoundPlayer typingSound;
    private Image backgroundImage;

    public CeritaScreen(GameFrame frame) {
        this.frame = frame;
        setLayout(null);
        setBackground(Color.BLACK);

        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource("/assets/images/bg_kalah_perang.jpg")).getImage();

        // Area teks cerita
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(0, 0, 0, 100)); // transparan gelap
        textArea.setFont(new Font("Serif", Font.PLAIN, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);
        add(textArea);

        fullText = """
        Setelah kekalahan besar menimpa kerajaannya,
        Arjuna menjadi satu-satunya harapan rakyatnya.

        Dalam keheningan, ia bersumpah akan berlatih memanah,
        hingga panah terakhirnya menjadi penentu takdir.

        Perjalanan ini bukan sekadar balas dendam,
        tetapi pengabdian untuk tanah air dan kehormatan.
        """;

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

        lanjutButton = new RoundedButton("Lanjut ▶");
        lanjutButton.addActionListener(e -> frame.changePanel(new Cutscene(frame)));
        add(lanjutButton);

        exitButton = new RoundedButton("EXIT");
        exitButton.setForeground(Color.BLACK);
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        backButton = new RoundedButton("🏠");
        backButton.setForeground(Color.BLACK);
        backButton.setBackground(new Color(0, 0, 0, 150));
        backButton.addActionListener(e -> {
            typingSound.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(backButton);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void resizeComponents() {
        int w = getWidth();
        int h = getHeight();
        textArea.setBounds(w / 2 - 300, h / 4, 600, 200);
        lanjutButton.setBounds(w - 150, h - 80, 120, 45);
        exitButton.setBounds(w - 100, 20, 70, 30);
        backButton.setBounds(w - 190, 20, 70, 30);
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
}