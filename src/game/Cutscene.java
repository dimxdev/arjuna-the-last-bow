package game;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;

public class Cutscene extends JPanel {
    private GameFrame frame;
    private JFXPanel jfxPanel;

    public Cutscene(GameFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        jfxPanel = new JFXPanel();
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(this::initFX);
    }

    private void initFX() {
        String videoPath = getClass().getResource("/assets/videos/defeated1.mp4").toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // ⚙️ Responsive: Bind ke scene size
        Group root = new Group(mediaView);
        Scene scene = new Scene(root);

        mediaView.fitWidthProperty().bind(scene.widthProperty());
        mediaView.fitHeightProperty().bind(scene.heightProperty());
        mediaView.setPreserveRatio(true);

        jfxPanel.setScene(scene);
        mediaPlayer.play();

        mediaPlayer.setOnEndOfMedia(() -> {
            SwingUtilities.invokeLater(() -> frame.changePanel(new MonologScene(frame)));
        });
    }
}
