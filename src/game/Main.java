// CLASS IS MAIN

// Name: Kevin Guo
// Date: 
// Program: Baba is You
// Description: TODO


/* Questions:
- public/private/protected?


*/

package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Main implements ActionListener, KeyListener {

	private static JFrame frame;
	private static JPanel mainPanel;
	private static JLabel label;
	private static ImageIcon img;
	private static BufferedImage image;
	
	private static BlockIcon blocks;
	
	
	// Music
	Clip menuMusic, levelMusic, mapMusic, voidMusic;
	Clip winSound, destroySound;
	AudioInputStream audioInputStream;
	
	
	// Game Logic
	public static Level activeLevel;
	
	private static int dr, dc;
	
	
	public Main() {

		frame = new JFrame("Baba is You");

		mainPanel = new JPanel();
		
		activeLevel = new Level("2");

		
		mainPanel.add(activeLevel.levelPanel);


		loadAssets();
		
		// Initialize JFrame
		//frame.add(mainPanel);
		
		activeLevel.levelPanel.setPreferredSize(Styles.FRAME_DIMENSION);
		frame.add(activeLevel.levelPanel);
		frame.setFocusable(true);
		frame.requestFocus();
		frame.addKeyListener(this);
		
		frame.pack();
		frame.setVisible(true);	
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	// Description: Detects when a keyboard shortcut for one of the buttons is pressed. (implements KeyListener interface)
	// Parameters: The KeyEvent being generated. (the key being pressed) 
	// Return: Void.
	public void keyPressed(KeyEvent event) { 
		
		menuMusic.stop();
		mapMusic.stop();
		
		int key = event.getKeyCode();
		if (key == KeyEvent.VK_A) {
			
		}
		else if (key == KeyEvent.VK_B) {
			
		}
		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_KP_UP ||
				key == KeyEvent.VK_DOWN || key == KeyEvent.VK_KP_DOWN ||
				key == KeyEvent.VK_LEFT || key == KeyEvent.VK_KP_LEFT ||
				key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_KP_RIGHT) {
			
			int dr = 0, dc = 0; 
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_KP_UP) {
				//performAction("Prev");
				dr = -1;
			}
			else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_KP_DOWN) {
				dr = 1;
			}
			else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_KP_LEFT) {
				dc = -1;
			}
			else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_KP_RIGHT) {
				dc = 1;
			}
			
			activeLevel.turn(dr, dc);
			if (!activeLevel.hasYou) {
				levelMusic.stop();
			}
			
		}
		else if (key == KeyEvent.VK_ESCAPE) {
			performAction("Exit");
		}
	}

	// Description: Called when a button is clicked or the animation timer ticks. (implements ActionListener interface)
	// Parameters: The ActionEvent being called. 
	// Return: Void.
	public void actionPerformed(ActionEvent e) {
		performAction(e.getActionCommand());
	}

	// Description: Executes an action. (essentially, it is an actionPerformed() that can be called elsewhere in my program)
	// Parameters: The name of the action command to perform.
	// Return: Void.
	public void performAction(String action) {

		if (action.equals("Animate")) {	

		}
		else if (action.equals("Exit")) {
			System.exit(0);
		}

	}
	
	
	
	
	
	
	public void loadAssets() {
		
		BlockIcon.loadAssets();
		
		
		// Load sounds
		
		try {
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/baba.wav"));
            levelMusic = AudioSystem.getClip();
            levelMusic.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/menu.wav"));
            menuMusic = AudioSystem.getClip();
            menuMusic.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/map.wav"));
            mapMusic = AudioSystem.getClip();
            mapMusic.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/void.wav"));
            voidMusic = AudioSystem.getClip();
            voidMusic.open(audioInputStream);


        }
        catch (UnsupportedAudioFileException e) {
            System.out.println("File not supported");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        
		
		//  Plays music
		levelMusic.setFramePosition(0);
		levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
		levelMusic.start();
		
		menuMusic.setFramePosition(0);
		menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
		menuMusic.start();
		
		mapMusic.setFramePosition(0);
		mapMusic.loop(Clip.LOOP_CONTINUOUSLY);
		mapMusic.start();
		
		voidMusic.setFramePosition(0);
		voidMusic.loop(Clip.LOOP_CONTINUOUSLY);
		voidMusic.start();
	}
	
	
	
	public static boolean sayHi() {
		return true;
	}
	
	public static void main(String[] args) {
		new Main();		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
