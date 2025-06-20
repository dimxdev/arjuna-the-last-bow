package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ArcheryGame extends JPanel implements ActionListener, KeyListener {
    private GameFrame frame;
    private Timer timer;
    private SoundPlayer bgm;
    private Image[] timeCycleBackgrounds;
    private int bgIndex = 0;
    private Timer bgSwitchTimer;
    private Image arjunaImage, arrowImage, targetImage, scoreIcon, heartIcon, explosionImage, levelIcon, arrowTrailImage, birdImage;
    private int arjunaX, arjunaY, arjunaWidth, arjunaHeight;
    private int arjunaBaseY = 0;
    private boolean arjunaMovingUp = true;
    private int arrowX, arrowY;
    private int targetX, targetY, targetWidth = 100, targetHeight = 100;
    private int targetSpeed = 2;
    private boolean arrowFlying = false;
    private boolean movingDown = true;
    private int score = 0;
    private int nyawa = 3;
    private int level = 1;
    private boolean showExplosion = false;
    private long explosionStart;
    private int birdX, birdY, birdWidth, birdHeight;

    private RoundedButton exitButton, menuButton;

    public ArcheryGame(GameFrame frame) {
        this.frame = frame;
        setLayout(null);

        timeCycleBackgrounds = new Image[] {
            new ImageIcon(getClass().getResource("/assets/images/bg_pagi.png")).getImage(),
            new ImageIcon(getClass().getResource("/assets/images/bg_siang.png")).getImage(),
            new ImageIcon(getClass().getResource("/assets/images/bg_malam.png")).getImage()
        };

        bgSwitchTimer = new Timer(10000, e -> {
            bgIndex = (bgIndex + 1) % timeCycleBackgrounds.length;
        });
        bgSwitchTimer.start();

        arjunaImage = new ImageIcon(getClass().getResource("/assets/images/arjuna_memanah.png")).getImage();
        arrowImage = new ImageIcon(getClass().getResource("/assets/images/anak-panah-melesat.png")).getImage();
        arrowTrailImage = new ImageIcon(getClass().getResource("/assets/images/arrow_trail.png")).getImage();
        targetImage = new ImageIcon(getClass().getResource("/assets/images/target.png")).getImage();
        scoreIcon = new ImageIcon(getClass().getResource("/assets/images/icon_arrow.png")).getImage();
        heartIcon = new ImageIcon(getClass().getResource("/assets/images/icon_heart.png")).getImage();
        explosionImage = new ImageIcon(getClass().getResource("/assets/images/explosion.png")).getImage();
        levelIcon = new ImageIcon(getClass().getResource("/assets/images/icon_levelup.png")).getImage();

        birdImage = new ImageIcon(getClass().getResource("/assets/images/burung3.gif")).getImage(); // Load bird image

        bgm = new SoundPlayer("/assets/sounds/archery-game.wav");
        bgm.playLoop();

        exitButton = new RoundedButton("EXIT");
        exitButton.setBackground(new Color(0, 0, 0, 150));
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> showCustomExitDialog());
        add(exitButton);

        menuButton = new RoundedButton("\uD83C\uDFE0");
        menuButton.setBackground(new Color(0, 0, 0, 150));
        menuButton.setForeground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuButton.addActionListener(e -> {
            bgm.stop();
            frame.changePanel(new HomeScreen(frame));
        });
        add(menuButton);

        setFocusable(true);
        addKeyListener(this);
        SwingUtilities.invokeLater(() -> requestFocusInWindow());

        timer = new Timer(10, this);
        timer.start();

        // Initialize bird position
        birdWidth = 60;
        birdHeight = 40;
        birdX = 0; // Starting position at the left edge
        birdY = 100; // Set bird's vertical position

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

        arjunaHeight = (int) (h * 0.65);
        arjunaWidth = (int) (arjunaHeight * 0.66);
        arjunaX = w - arjunaWidth;
        arjunaY = h - arjunaHeight;
        if (arjunaBaseY == 0) arjunaBaseY = arjunaY;
        targetX = 30;

        g.drawImage(timeCycleBackgrounds[bgIndex], 0, 0, w, h, this);
        g.drawImage(arjunaImage, arjunaX, arjunaY, arjunaWidth, arjunaHeight, this);
        g.drawImage(targetImage, targetX, targetY, targetWidth, targetHeight, this);

        if (arrowFlying) {
            g.drawImage(arrowTrailImage, arrowX + 40, arrowY + 25, 80, 20, this);
            g.drawImage(arrowImage, arrowX, arrowY, 110, 70, this);
        }

        if (showExplosion && System.currentTimeMillis() - explosionStart < 500) {
            g.drawImage(explosionImage, targetX, targetY, targetWidth, targetHeight, this);
        } else {
            showExplosion = false;
        }

        g.drawImage(scoreIcon, 20, 20, 30, 30, this);
        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        int maxScore = getMaxScore();
        g.drawString(score + "/" + maxScore, 60, 45);

        for (int i = 0; i < nyawa; i++) {
            g.drawImage(heartIcon, 20 + (i * 35), 60, 30, 30, this);
        }

        g.drawImage(levelIcon, (w / 2) - 45, 10, 30, 30, this);
        g.drawString("Lv " + level, (w / 2) - 5, 33);

        // Draw the bird
        g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, this);
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
                showExplosion = true;
                explosionStart = System.currentTimeMillis();
                score += 20;
                if (score < getMaxScore()) targetSpeed += 1;
                if (score >= getMaxScore()) {
                    if (level < 3) level++;
                    else {
                        bgm.stop();
                        frame.changePanel(new FinalCeritaScene(frame));
                    }
                }
            }

            if (arrowX + 80 < 0) {
                arrowFlying = false;
                nyawa--;
                if (nyawa <= 0) {
                    score = 0;
                    nyawa = 3;
                    targetSpeed = 2;
                    level = 1;
                }
            }
        }

        if (movingDown) {
            targetY += targetSpeed;
            if (targetY + targetHeight >= getHeight()) movingDown = false;
        } else {
            targetY -= targetSpeed;
            if (targetY <= 0) movingDown = true;
        }

        // Arjuna naik turun otomatis
        if (arjunaMovingUp) {
            arjunaY -= 1;
            if (arjunaY <= arjunaBaseY - 40) arjunaMovingUp = false;
        } else {
            arjunaY += 1;
            if (arjunaY >= arjunaBaseY + 40) arjunaMovingUp = true;
        }

        // Move the bird from left to right
        birdX += 2; // Adjust the speed of the bird
        if (birdX > getWidth()) {
            birdX = -birdWidth; // Reset to the left side if it reaches the right edge
        }

        repaint();
    }

    private int getMaxScore() {
        return (level == 1) ? 100 : (level == 2) ? 180 : 220;
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