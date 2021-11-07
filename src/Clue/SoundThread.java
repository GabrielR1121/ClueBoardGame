package Clue;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundThread extends Thread {

    private Clip clip;

    // Receives the file path for the sound and tries to play it.
    // The audio NEEDS to be a .wav for this to work. Code Example below taken from
    // GalaxyWars.
    // player = new SoundThread("File path");
    // player.start();
    // player.playSound();
    public SoundThread(String string) { // constructor

        try {
            File file = new File(string).getAbsoluteFile();

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

            this.clip = AudioSystem.getClip();

            this.clip.open(audioInputStream);

        } catch (Exception ex) {
            System.err.println("Error with playing sound.");
            ex.printStackTrace();
            System.exit(0);
        } // catch

    } // constructor

    @Override
    public void run() {

        while (true) {

        } // while

    } // run

    public void pauseSound() {
        this.clip.stop();
    } // pauseSound

    public void playSound() {
        this.clip.start();

    } // playsound

    public void playSoundEffect() {
        this.clip.start();
    }

} // class
