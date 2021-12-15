package BombermanGame.Sound;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/Utils/Sound/music.wav");
        soundURL[1] = getClass().getResource("/Utils/Sound/walk.wav");
        soundURL[2] = getClass().getResource("/Utils/Sound/drop.wav");
        soundURL[3] = getClass().getResource("/Utils/Sound/destroy.wav");
        soundURL[4] = getClass().getResource("/Utils/Sound/die.wav");
        soundURL[5] = getClass().getResource("/Utils/Sound/item.wav");
        soundURL[6] = getClass().getResource("/Utils/Sound/kill.wav");
        soundURL[7] = getClass().getResource("/Utils/Sound/level.wav");
        soundURL[8] = getClass().getResource("/Utils/Sound/win.wav");
        soundURL[9] = getClass().getResource("/Utils/Sound/lose.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}