package sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gfx.AssetStorage;

public class SoundPlayer {
    private static ArrayList<Clip> audioClips = new ArrayList<Clip>();
    public static float musicVolume = 0.2f;
    public static Clip loopSound(String name) {
        File audioFile = AssetStorage.sounds.get(name);
        if (audioFile == null) {
            System.err.println("Cannot play sound: " + name);
            return null;
        }
        try {
            // Create an AudioInputStream from the file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource
            Clip audioClip = AudioSystem.getClip();

            // Open audio clip and load samples from the audio input stream
            audioClip.open(audioStream);
            // Start the clip
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            audioClips.add(audioClip);
            return audioClip;
        } catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error playing the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line for playing back is unavailable.");
            e.printStackTrace();
        }
        return null;
    }

    public static void playSound(String name, boolean stoppable, boolean music) {
        File audioFile = AssetStorage.sounds.get(name);
        if (audioFile == null) {
            System.err.println("Cannot play sound: " + name);
            return;
        }
        try {
            // Create an AudioInputStream from the file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            // Get a sound clip resource
            Clip audioClip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream
            audioClip.open(audioStream);
            if(music){
                setVolume(audioClip, musicVolume);
            }

            // Start the clip
            audioClip.start();
            if(stoppable){
                audioClips.add(audioClip);
            }
        } catch (UnsupportedAudioFileException e) {
            System.out.println("The specified audio file is not supported.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error playing the audio file.");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Audio line for playing back is unavailable.");
            e.printStackTrace();
        }
    }

    public static void stopAllSounds() {
        System.out.println("Stopping all sounds");
        for (Clip c : audioClips) {
            c.stop();
        }
        audioClips.clear();
    }

    private static float getVolume(Clip clip) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    private static void setVolume(Clip clip, float volume) {
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }
}
