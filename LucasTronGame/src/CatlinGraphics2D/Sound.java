package CatlinGraphics2D;

import javax.sound.sampled.*;
import java.io.*;

/**
 * The Sound class is used to play simple sound clips.
 * The sound clips should be stored in a file in .wav, .aiff, or .au format.
 * All sound is played in the "background" - in other words, your program
 * continues to run while the sound is playing.
 * <p>
 * Example usage:
 * <p>
 * <pre>
 Sound beep = new Sound("beep.wav");
 beep.play();

 Sound backgroundbeat = new Sound("drumloop.wav");
 backgroundbeat.setVolume(-10);
 backgroundbeat.loop();
 </pre>
 *
 * @author Andrew Merrill, Spring 2006
 * @version 1.0
 */

public class Sound
{

    private Clip clip = null;
    private FloatControl gainControl = null;
    private BooleanControl muteControl = null;

    /**************************************************************************************
     * Creates a new Sound from an audio file.
     * The file should be in the same location (folder) as this program.
     * The audio formats .wav, .aiff, and .au should be supported.
     *
     * @param filename The name of the disk file with the audio clip.
     */

    public Sound(String filename) {
        this("", filename);
    }

    /**************************************************************************************
     * Creates a new Sound from an audio file.
     * The audio formats .wav, .aiff, and .au should be supported.
     *
     * @param folder   The folder containing the audio file.
     * @param filename The name of the disk file with the audio file.
     */

    public Sound(String folder, String filename)
    {
        try {
            if (folder.length() > 0)
                folder = folder + "/";
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource("/" + folder + filename));
            clip = AudioSystem.getClip();
            clip.open(stream);

            try {
                gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } catch (IllegalArgumentException e) {
                gainControl = null;
            }
            try {
                muteControl = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);
            } catch (IllegalArgumentException e) {
                muteControl = null;
            }
        }
        catch (UnsupportedAudioFileException e)
        {
            throw new RuntimeException("SimpleSound Error: " + filename + " is not a supported audio file: " + e.getMessage());
        }
        catch (IOException e)
        {
            throw new RuntimeException("SimpleSound Error: failed while reading file " + filename + " : " + e.getMessage());
        }
        catch (LineUnavailableException e)
        {
            throw new RuntimeException("SimpleSound error: no line for audio clip " + filename + " : " + e.getMessage());
        }
    }

    /**
     * Plays the sound once.
     */

    public void play()
    {
        if (clip != null) {
            if (!clip.isRunning()) {
                clip.setFramePosition(0);
            }
            clip.start();
        }
    }

    /**
     * Stops the sound from playing.
     */

    public void stop()
    {
        if (clip != null)
            clip.stop();
    }

    /**
     * Plays the sound in a repeating loop, over and over.
     */

    public void loop()
    {
        if (clip != null)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Plays the sound in a repeating loop, with a given number of repetitions.
     *
     * @param numRepeats The number of times to repeat the sound clip.
     */

    public void loop(int numRepeats)
    {
        if (clip != null)
            clip.loop(numRepeats);
    }

    /**
     * Sets the volume to the given number of decibels.  Negative numbers will make the sound quieter than
     * the original recording, and positive numbers will make it louder.
     *
     * @param level The number of decibels to add to the clip's intrinsic sound level.
     */

    public void setVolume(float level)
    {
        if (gainControl != null)
            gainControl.setValue(level);
        else
            throw new UnsupportedOperationException("error: no control for setting volume!");
    }

    /**
     * Returns the lowest possible volume level.
     */

    public float getMinimumVolume()
    {
        if (gainControl != null)
            return gainControl.getMinimum();
        else
            throw new UnsupportedOperationException("error: no control for setting volume!");
    }

    /**
     * Returns the highest possible volume level.
     */

    public float getMaximumVolume()
    {
        if (gainControl != null)
            return gainControl.getMaximum();
        else
            throw new UnsupportedOperationException("error: no control for setting volume!");
    }

    /**
     * Returns the current volume level.
     */

    public float getVolume()
    {
        if (gainControl != null)
            return gainControl.getValue();
        else
            throw new UnsupportedOperationException("error: no control for setting volume!");
    }

    /**
     * Mutes the volume (makes it silent).
     */

    public void mute()
    {
        if (muteControl != null)
            muteControl.setValue(true);
        else
            throw new UnsupportedOperationException("error: no control for muting volume!");
    }

    /**
     *  Restores the volume to its unmuted level.
     */

    public void unMute()
    {
        if (muteControl != null)
            muteControl.setValue(false);
        else
            throw new UnsupportedOperationException("error: no control for muting volume!");
    }


}