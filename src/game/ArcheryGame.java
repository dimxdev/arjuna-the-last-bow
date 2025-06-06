package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ArcheryGame extends JPanel implements ActionListener, KeyListener {
    private GameFrame frame;
    private Timer timer;
    private SoundPlayer bgm;
    private Image backgroundImage, arjunaImage, arrowImage, targetImage;
    private int arjunaX, arjunaY, arjunaWidth, arjunaHeight;
    private int arrowX, arrowY;
    private int targetX, targetY, targetWidth = 100, targetHeight = 100;
    private int targetSpeed = 2;
    private boolean arrowFlying = false;
    private boolean movingDown = true;
    private int score = 0;
    private int nyawa = 3;

    private RoundedButton exitButton, menuButton;

    public ArcheryGame(GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        // Load assets
        backgroundImage = new ImageIcon(getClass().getResource("/assets/images/archery-bg.png")).getImage();
        arjunaImage = new ImageIcon(getClass().getResource("/assets/images/arjuna_memanah.png")).getImage();
        arrowImage = new ImageIcon(getClass().getResource("/assets/images/anak-panah-melesat.png")).getImage();
        targetImage = new ImageIcon(getClass().getResource("/assets/images/target.png")).getImage();

        // Musik latar
        bgm = new SoundPlayer("/assets/sounds/archery-game.wav");
        bgm.playLoop();

        // Tombol EXIT
        exitButton = new RoundedButton("EXIT");
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        // Tombol Home
        menuButton = new RoundedButton("🏠");
        menuButton.setBackground(new Color(0, 0, 0, 150));
        menuButton.setForeground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.addActionListener(e -> {
            bgm.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(menuButton);

        // Keyboard dan Timer
        setFocusable(true);
        addKeyListener(this);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());

        timer = new Timer(10, this);
        timer.start();

        // Posisi tombol responsif
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                exitButton.setBounds(w - 90, 10, 70, 30);
                menuButton.setBounds(w - 180, 10, 80, 30);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int w = getWidth();
        int h = getHeight();

        // Setup Arjuna
        arjunaHeight = (int) (h * 0.65);
        arjunaWidth = (int) (arjunaHeight * 0.66);
        arjunaX = w - arjunaWidth;
        arjunaY = h - arjunaHeight;
        targetX = 30;

        // Gambar semua elemen
        g.drawImage(backgroundImage, 0, 0, w, h, this);
        g.drawImage(arjunaImage, arjunaX, arjunaY, arjunaWidth, arjunaHeight, this);
        g.drawImage(targetImage, targetX, targetY, targetWidth, targetHeight, this);

        if (arrowFlying) {
            int arrowWidth = 110;
            int arrowHeight = 70;
            g.drawImage(arrowImage, arrowX, arrowY, arrowWidth, arrowHeight, this);
        }

        // Score dan Nyawa
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Score: " + score + "/100", 30, 40);
        g.drawString("Nyawa: " + nyawa + "/3", 30, 70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (arrowFlying) {
            arrowX -= 6;

            Rectangle arrowRect = new Rectangle(arrowX, arrowY, 80, 30);
            Rectangle targetRect = new Rectangle(targetX, targetY, targetWidth, targetHeight);

            if (arrowRect.intersects(targetRect)) {
                arrowFlying = false;
                SoundPlayer.playOnce("/assets/sounds/hit.wav");
                score += 20;

                if (score < 100) targetSpeed += 1;

                if (score >= 100) {
                    bgm.stop();
                    frame.changePanel(new FinalCeritaScene(frame));
                }
            }

            if (arrowX + 80 < 0) {
                arrowFlying = false;
                nyawa--;

                if (nyawa <= 0) {
                    score = 0;
                    nyawa = 3;
                    targetSpeed = 2;
                }
            }
        }

        // Gerak target
        if (movingDown) {
            targetY += targetSpeed;
            if (targetY + targetHeight >= getHeight()) movingDown = false;
        } else {
            targetY -= targetSpeed;
            if (targetY <= 0) movingDown = true;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !arrowFlying) {
            SoundPlayer.playOnce("/assets/sounds/fire.wav");
            arrowX = arjunaX - 40;
            arrowY = arjunaY + (int) (arjunaHeight * 0.20);
            arrowFlying = true;
        }

        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    private void showCustomExitDialog() {
        JDialog dialog = new JDialog(frame, "Keluar dari Game", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);

        JPanel panel = new RoundedPanel(30, Color.WHITE);
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
