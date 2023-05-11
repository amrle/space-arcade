/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class easily allows us to play sounds.
*/


import javax.sound.sampled.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    // sound stuff
    private AudioInputStream in;
    private Clip clip;

    // Constructor. Needs to be provided with relative or absolute path to audio file. Audio // file must be a .wav file
    public Sound(String filepath) {
        try {
            in = AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(in);
        } catch (Exception e) {
            System.out.println("Audio file not recognized. Check the path to the audio file.");
            e.printStackTrace();
        }
    }


    // play, stop, loop the sound clip
    public void play(float volume){
        // Set volume.
        FloatControl vol = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        vol.setValue(volume);
        // Rewind to start.
        clip.setFramePosition(0);  
        // Play.
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}