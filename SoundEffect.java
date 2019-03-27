package sample;

import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;

public enum SoundEffect {
    SHOOT("Laser.wav"),
    FLY("rocket1.wav"),
    EXPLODE("Bomb.wav"),
    THEME("flower.wav"),
    GRUNT("Grunt.wav"),
    GROWL("roar.wav"),
    DUN("Dun.wav");
    public static enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;

    private Clip clip;

    SoundEffect(String soundFileName) {
        try {
            URL url;
            url = this.getClass().getResource(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
    public void stop()
    {
        if (clip.isRunning())
            clip.stop();
    }

    static void init() {
        values();
    }
}
