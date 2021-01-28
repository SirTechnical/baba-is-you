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
	
	private static JPanel menuPanel, pausePanel;
	
	
	private static JLabel mainBackgroundLabel;
	private static ImageIcon mainBackgroundImage;
	
	private static Fader fader;
	
	private static JButton startButton, helpButton, exitButton;
	
	private static JComboBox<String> levelSelectionBox;

	
	// Music
	Clip menuMusic, levelMusic, voidMusic;
	Clip winSound, destroySound;
	AudioInputStream audioInputStream;
	
	
	// Game Logic
	public static Level activeLevel;
	
	public static String currentScreen;
	
	
	public Main() {

		loadAssets();
		
		currentScreen = "menu";
		
		frame = new JFrame("Baba is You");

		mainPanel = new JPanel(null);
		
		
		// Main Menu Screen
		menuPanel = new JPanel(null);
		
		startButton = new JButton("Start");
		startButton.setBounds(Styles.START_BUTTON_LOCATION);
		menuPanel.add(startButton);
		
		helpButton = new JButton("Help");
		helpButton.setBounds(Styles.HELP_BUTTON_LOCATION);
		menuPanel.add(helpButton);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(Styles.EXIT_BUTTON_LOCATION);
		menuPanel.add(exitButton);
		
		menuPanel.setBounds(Styles.ENTIRE_FRAME);
		
		
		// Level Selector
		levelSelectionBox = new JComboBox<String>();
		levelSelectionBox.setBounds(Styles.LEVEL_SELECTOR_LOCATION);
		menuPanel.add(levelSelectionBox);
		
		
		
		// Main Background
		mainBackgroundImage = new ImageIcon("sprites/screen_main.png");
		mainBackgroundLabel = new JLabel(mainBackgroundImage);
		mainBackgroundLabel.setBounds(Styles.ENTIRE_FRAME);
		menuPanel.add(mainBackgroundLabel);
		menuPanel.setComponentZOrder(mainBackgroundLabel, menuPanel.getComponentCount()-1);
		
		mainPanel.add(menuPanel);
		
		// temp
		levelSelectionBox.addItem("1");
		levelSelectionBox.addItem("2");
		levelSelectionBox.addItem("3");
		
		// Fader
		fader = new Fader();
		mainPanel.add(fader.getLabel());
		mainPanel.setComponentZOrder(fader.getLabel(), 0);
		
		System.out.println(Styles.FRAME_HEIGHT + " " + Styles.FRAME_WIDTH);
		
		// Action Listeners
		startButton.addActionListener(this);
		startButton.setActionCommand("Start");
		helpButton.addActionListener(this);
		helpButton.setActionCommand("Help");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("Exit");
		
		levelSelectionBox.addActionListener(this);
		levelSelectionBox.setActionCommand("LevelSelected");
		
		
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
		
		int key = event.getKeyCode();
		
		if (currentScreen.equals("level")) {
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
		else if (currentScreen.equals("menu")) {
			
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
		
		// Main Menu
		if (action.equals("Start")) {
			changeScreen("level");
		}
		else if (action.equals("Help")) {
			System.out.println("Help!");
		}
		else if (action.equals("Exit")) {
			System.exit(0);
		}
		else if (action.equals("LevelSelected")) {
			System.out.println(levelSelectionBox.getSelectedIndex());
		}
		
		// Screen-Specific Actions
		if (currentScreen.equals("level")) {
		
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
				
				// Win: end level
				if (activeLevel.isWin()) {
					changeScreen("menu");
				}
				
			}
			else if (action.equals("Undo")) {
				activeLevel.undo();
			}
			else if (action.equals("Restart")) {
				mainPanel.remove(activeLevel.getPanel());
				//openLevel();
				changeScreen("level");
			}
		}
		else if (currentScreen.equals("menu")) {
			
			
			
		}
		
	}
	
	public void changeScreen(String screen) {
		
		// Hide All Screens
		menuPanel.setVisible(false);
		if (activeLevel != null)
			activeLevel.getPanel().setVisible(false);
		
		// Stop Music
		levelMusic.stop();
		menuMusic.stop();
		voidMusic.stop();
		
		if (screen.equals("menu")) {
			currentScreen = "menu";
			menuPanel.setVisible(true);
			
			menuMusic.start();
			menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
		else if (screen.equals("level")) {
			openLevel();

			activeLevel.getPanel().setVisible(true);
			
			if (activeLevel.hasYou()) {
				levelMusic.start();
				levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
				
			}
			// Not You: Stop music
			else {
				voidMusic.start();
				voidMusic.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		
		mainPanel.updateUI();
	}
	
	
	public void openLevel() {
		currentScreen = "level";
		
		// LEVEL NUMBER
		
		int selectedLevel = levelSelectionBox.getSelectedIndex()+1;
		
		activeLevel = new Level(Integer.toString(selectedLevel));
		mainPanel.add(activeLevel.getPanel());
		
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

		
		menuMusic.setFramePosition(0);
		
		
		voidMusic.setFramePosition(0);
		
		
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
	
	// Getter Methods
	/*
	public static Level getActiveLevel() {
		return activeLevel;
	}*/

}
