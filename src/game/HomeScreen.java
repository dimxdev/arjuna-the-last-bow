package game;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

public class HomeScreen extends JPanel {
    private GameFrame frame;
    private Image background;

    private JLabel titleLabel, subtitleLabel;
    private JButton playButton, exitButton, aboutButton, howToButton, creditsButton;
    private SoundPlayer bgm;

    public HomeScreen(GameFrame frame) {
        this.frame = frame;

        background = new ImageIcon(getClass().getResource("/assets/images/home_bg2.png")).getImage();
        setLayout(null);

        bgm = new SoundPlayer("/assets/sounds/war.wav");
        bgm.playLoop();

        titleLabel = new JLabel("ARJUNA: THE LAST BOW", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Georgia", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel);

        subtitleLabel = new JLabel("\"Seorang pemimpin belajar memanah demi menuntut balas\"", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        add(subtitleLabel);

        playButton = createWoodenButton("Mainkan", e -> {
            bgm.stop();
            frame.changePanel(new CeritaScreen(frame));
        });
        add(playButton);

        ImageIcon icon = new ImageIcon(getClass().getResource("/assets/images/user-icon.jpg"));
        Image scaledIcon = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        creditsButton = createWoodenButton("", e -> showImageDialog("Team", new String[]{
            "/assets/images/dev1.jpg",
            "/assets/images/dev2.jpg",
            "/assets/images/dev3.jpg"
        }));
        creditsButton.setIcon(new ImageIcon(scaledIcon));
        creditsButton.setToolTipText("Lihat Pembuat");
        add(creditsButton);

        aboutButton = createWoodenButton("About", e -> showDialog("Tentang Game", "Arjuna: The Last Bow adalah sebuah game edukatif bergenre aksi-petualangan yang mengangkat kisah inspiratif dari tokoh pewayangan Arjuna.\n\nDalam game ini, kamu akan membimbing Arjuna melalui latihan memanah, melatih fokus dan ketepatan, hingga akhirnya menghadapi tantangan besar untuk membela kerajaannya.\n\nFitur utama:\n- Gameplay latihan panah (archery mini-game)\n- Cerita pendek dan cutscene sinematik\n- Sistem skor & nyawa\n- Visual 2D yang menarik\n- Audio dramatis & efek suara khas perang"));
        add(aboutButton);

        howToButton = createWoodenButton("Cara Bermain", e -> showDialog("Cara Bermain", 
        "Untuk menyelesaikan game ini dan membawa Arjuna menuju kemenangan, kamu harus menguasai teknik memanah di 3 level yang semakin menantang.\n\n" +
        "Instruksi:\n" +
        "- Tekan tombol SPASI (SPACE) untuk menembakkan panah.\n" +
        "- Panah akan melesat secara horizontal dengan efek jejak cahaya.\n" +
        "- Target akan bergerak naik turun secara dinamis.\n" +
        "- Kamu harus mengenai target sebanyak mungkin hingga skor mencapai batas level.\n" +
        "- Setiap kena target akan mendapat +20 poin.\n" +
        "- Jika panah meleset, nyawa berkurang 1. Kamu hanya punya 3 nyawa.\n" +
        "- Jika nyawa habis, skor, nyawa, dan level akan di-reset ulang.\n\n" +
        "Level & Skor:\n" +
        "- Level 1: Maksimal 100 poin\n" +
        "- Level 2: Maksimal 180 poin\n" +
        "- Level 3: Maksimal 220 poin\n\n" +
        "Tips Menang:\n" +
        "- Perhatikan ritme gerakan target.\n" +
        "- Waktu tembakanmu harus presisi.\n" +
        "- Dengarkan suara panah & efek hit untuk meningkatkan fokus dan tempo."));
        add(howToButton);

        exitButton = createWoodenButton("EXIT", e -> showCustomExitDialog());
        add(exitButton);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                resizeComponents();
            }
        });
    }
    

    private JButton createWoodenButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Verdana", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(133, 94, 66));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createLineBorder(new Color(92, 64, 51), 3));
        button.addActionListener(action);
        return button;
    }

    private void resizeComponents() {
        int w = getWidth();
        int h = getHeight();

        titleLabel.setBounds(w / 2 - 300, h / 8, 600, 50);
        subtitleLabel.setBounds(w / 2 - 300, h / 8 + 60, 600, 30);

        int btnWidth = 180;
        int btnHeight = 50;
        int spacing = 20;
        int startY = h / 2 - (btnHeight * 3 + spacing * 2) / 2;
        int x = w / 2 - btnWidth / 2;

        aboutButton.setBounds(x, startY, btnWidth, btnHeight);
        howToButton.setBounds(x, startY + btnHeight + spacing, btnWidth, btnHeight);
        playButton.setBounds(x, startY + 2 * (btnHeight + spacing), btnWidth, btnHeight);

        creditsButton.setBounds(20, 20, 50, 30);
        exitButton.setBounds(w - 100, 20, 70, 30);
    }

    private void showDialog(String title, String message) {
        JTextArea area = new JTextArea(message);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(frame, scroll, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showImageDialog(String title, String[] imagePaths) {
        JPanel panel = new JPanel(new GridLayout(1, imagePaths.length, 10, 10));
        for (String path : imagePaths) {
            URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                ImageIcon originalIcon = new ImageIcon(imgUrl);
                Image img = originalIcon.getImage();

                int maxWidth = 150;
                int maxHeight = 180;

                double ratio = (double) img.getWidth(null) / img.getHeight(null);
                int scaledWidth = maxWidth;
                int scaledHeight = (int) (maxWidth / ratio);

                if (scaledHeight > maxHeight) {
                    scaledHeight = maxHeight;
                    scaledWidth = (int) (maxHeight * ratio);
                }

                Image scaledImage = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                panel.add(imgLabel);
            } else {
                JLabel fallback = new JLabel("Gambar tidak ditemukan");
                panel.add(fallback);
            }
        }

        JOptionPane.showMessageDialog(frame, panel, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCustomExitDialog() {
        JDialog dialog = new JDialog(frame, "Konfirmasi Keluar", true);
        dialog.setUndecorated(true);
        dialog.setSize(400, 380);
        dialog.setLocationRelativeTo(frame);
        dialog.setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new RoundedPanel(30, new Color(255, 255, 255));
        panel.setLayout(null);

        try {
            JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/assets/images/exit_icon.png")));
            iconLabel.setBounds(135, 40, 128, 128);
            panel.add(iconLabel);
        } catch (Exception e) {
            JLabel iconLabel = new JLabel("EXIT");
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iconLabel.setBounds(0, 40, 400, 128);
            panel.add(iconLabel);
        }

        JLabel title = new JLabel("Konfirmasi Keluar", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBounds(0, 180, 400, 30);
        panel.add(title);

        JLabel message = new JLabel("Apakah Anda yakin ingin keluar dari game?", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.PLAIN, 14));
        message.setBounds(0, 210, 400, 30);
        panel.add(message);

        JButton yesButton = new JButton("Keluar");
        yesButton.setBounds(85, 280, 100, 40);
        yesButton.setBackground(new Color(255, 102, 102));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        yesButton.setFocusPainted(false);
        yesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        yesButton.addActionListener(e -> System.exit(0));
        panel.add(yesButton);

        JButton noButton = new JButton("Batal");
        noButton.setBounds(215, 280, 100, 40);
        noButton.setBackground(new Color(200, 230, 255));
        noButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        noButton.setFocusPainted(false);
        noButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        noButton.addActionListener(e -> dialog.dispose());
        panel.add(noButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}