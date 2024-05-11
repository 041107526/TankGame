package com.tankGame.util;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * class music resource
 */

public class MusicUtil {
    private static Clip start;
    private static Clip bomb;


    static {
        try {

            AudioInputStream startStream = AudioSystem.getAudioInputStream(new File("music/start.wav"));
            start = AudioSystem.getClip();
            start.open(startStream);

            AudioInputStream bombStream = AudioSystem.getAudioInputStream(new File("music/bomb.wav"));
            bomb = AudioSystem.getClip();
            bomb.open(bombStream);


        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void playStart() {
        start.setFramePosition(0);
        start.start();
    }

    public static void playBomb() {
        bomb.setFramePosition(0);
        bomb.start();
    }




}
