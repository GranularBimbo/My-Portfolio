package Managers;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;

public class Audio {
	public Clip Klip;
	
	public Audio() {
		
	}
	
	public void playSound(String fileLocation,boolean loop) {
		try {
			File musicPath = new File(fileLocation);
			
			if(musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				
				if(loop) {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					Klip = clip;
				}
			}
			else {
				System.out.println("can't find the file");
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//only stops the last sound played --- !!!BE CAREFUL TO StOP SOUNDS BEFORE PLAYING NEW ONES!!!
	public void stopSound() {
		Klip.stop();
	}
}
