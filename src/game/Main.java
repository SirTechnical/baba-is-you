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

	// JComponents
	private static JFrame frame;
	private static JPanel mainPanel;
	
	private static JButton startButton, exitButton;
	
	private static JLabel label;
	
	
	// Music
	Clip menuMusic, levelMusic, mapMusic, voidMusic;
	Clip winSound, destroySound;
	AudioInputStream audioInputStream;
	
	
	// Game Logic
	public static Level activeLevel;
	
	public static String focus;
	
	
	public Main() {

		loadAssets();
		
		focus = "menu";
		
		frame = new JFrame("Baba is You");

		mainPanel = new JPanel();
		
		
		
		mainPanel.setLayout(null);
		
		
		
		startButton = new JButton("Start");
		startButton.setBounds(Styles.START_BUTTON_LOCATION);
		mainPanel.add(startButton);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(Styles.EXIT_BUTTON_LOCATION);
		mainPanel.add(exitButton);
		
		// Action Listeners
		startButton.addActionListener(this);
		startButton.setActionCommand("Start");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("Exit");
		
		
		
		// Initialize JFrame
		mainPanel.setPreferredSize(Styles.FRAME_DIMENSION);
		frame.add(mainPanel);
		
		// Focus to enable keyboard shortcuts
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
		
		levelMusic.stop();
		menuMusic.stop();
		mapMusic.stop();
		
		int key = event.getKeyCode();
		
		if (focus.equals("level")) {
			levelMusic.start();
			if (key == KeyEvent.VK_UP || key == KeyEvent.VK_KP_UP) {
				performAction("Move Up");
			}
			else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_KP_DOWN) {
				performAction("Move Down");
			}
			else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_KP_LEFT) {
				performAction("Move Left");
			}
			else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_KP_RIGHT) {
				performAction("Move Right");
			}
			else if (key == KeyEvent.VK_Z) {
				performAction("Undo");
			}
			else if (key == KeyEvent.VK_R) {
				performAction("Restart");
			}
		}
		else if (focus.equals("menu")) {
			menuMusic.start();
			
			if (key == KeyEvent.VK_ESCAPE) {
				performAction("Exit");
			}
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
		
		if (action.equals("Exit")) {
			System.exit(0);
		}
		
		levelMusic.stop();
		menuMusic.stop();
		mapMusic.stop();
		
		if (focus.equals("level")) {
			levelMusic.start();
		
			if (action.indexOf(' ') != -1 && action.substring(0, action.indexOf(' ')).equals("Move")) {
				String direction = action.substring(action.indexOf(' ') + 1);
				
				int dr = 0, dc = 0; 
				if (direction.equals("Up")) {
					dr = -1;
				}
				else if (direction.equals("Down")) {
					dr = 1;
				}
				else if (direction.equals("Left")) {
					dc = -1;
				}
				else if (direction.equals("Right")) {
					dc = 1;
				}
				
				// Execute turn
				activeLevel.turn(new Pair(dr, dc));
				
				// Not You: Stop music
				if (!activeLevel.hasYou()) {
					levelMusic.stop();
				}
				
				// Win: end level
				if (activeLevel.isWin()) {
					openMenu();
				}
				
			}
			else if (action.equals("Undo")) {
				activeLevel.undo();
			}
			else if (action.equals("Restart")) {
				openLevel();
			}
		}
		else if (focus.equals("menu")) {
			menuMusic.start();
			
			if (action.equals("Start")) {
				openLevel();
			}
			
		}
		
	}
	
	public void openMenu() {
		focus = "menu";
		
		activeLevel.getPanel().setVisible(false);	
		startButton.setVisible(true);
		exitButton.setVisible(true);
	}
	
	public void openLevel() {
		focus = "level";
		
		// LEVEL NUMBER
		activeLevel = new Level("5");
		mainPanel.add(activeLevel.getPanel());
		mainPanel.updateUI();
		
		
		startButton.setVisible(false);
		exitButton.setVisible(false);
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
        
		
		// Plays music
		levelMusic.setFramePosition(0);
		levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
		menuMusic.setFramePosition(0);
		menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
		mapMusic.setFramePosition(0);
		mapMusic.loop(Clip.LOOP_CONTINUOUSLY);
		
		voidMusic.setFramePosition(0);
		voidMusic.loop(Clip.LOOP_CONTINUOUSLY);
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
