package game;

import javax.sound.sampled.*;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String path) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(getClass().getResource(path));
            clip = AudioSystem.getClip();
            clip.open(audio);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        }
    }
    
    public static void playOnce(String path) {
    try {
        AudioInputStream audio = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
