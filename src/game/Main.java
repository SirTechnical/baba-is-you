// Name: Kevin Guo
// Date: Jan. 27, 2021
// Program: Baba is You
// Description: See README.pdf (or press Help in the game)

// CLASS IS MAIN

package game;

import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;

public class Main implements ActionListener, KeyListener {

	// JComponents
	private static JFrame frame;
	private static JPanel mainPanel;
	private static JPanel menuPanel, pausePanel;
	
	private static JButton startButton, menuHelpButton, exitButton;
	private static JButton resumeButton, restartButton, pauseHelpButton, returnButton;
	
	private static JComboBox<String> levelSelectionBox;
	
	private static JLabel mainBackgroundLabel;
	private static ImageIcon mainBackgroundImage;
	
	private static JLabel congratulationsLabel;
	private static ImageIcon congratulationsImage;
	
	private static Fader fader;
	
	private static Timer animationTimer;
	
	// Music and Sounds
	Clip menuMusic, levelMusic, voidMusic;
	Clip startSound, winSound, destroySound, ruleSound, moveSound, undoSound;
	AudioInputStream audioInputStream;
	
	private static ImageIcon musicOnIcon, musicOffIcon, soundOnIcon, soundOffIcon;
	private static JButton menuMusicButton, pauseMusicButton, menuSoundButton, pauseSoundButton;
	
	private static boolean music, sound;
	
	// Game Logic
	private static Level activeLevel;
	private static String currentScreen;
	
	// Level Unlocks
	private static int unlockedLevels = 1;
	private static final int TOTAL_LEVELS = 20;
	private static final int LEVELS_TO_UNLOCK = 2;
	private static boolean[] solved = new boolean[TOTAL_LEVELS + 1];
	
	public Main() {
		
		// Initialization
		loadAssets();
		currentScreen = "menu";
		frame = new JFrame("Baba is You");
		mainPanel = new JPanel(null);
		
		// ------------ Main Menu Screen ------------ //
		menuPanel = new JPanel(null);
		menuPanel.setBounds(Styles.ENTIRE_FRAME);
		
		startButton = new JButton("Play");
		startButton.setBounds(Styles.START_BUTTON_LOCATION);
		startButton.setFont(Styles.BUTTON_FONT);
		startButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		startButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		menuPanel.add(startButton);
		
		menuHelpButton = new JButton("Help");
		menuHelpButton.setBounds(Styles.MENU_HELP_BUTTON_LOCATION);
		menuHelpButton.setFont(Styles.BUTTON_FONT);
		menuHelpButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		menuHelpButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		menuPanel.add(menuHelpButton);
		
		exitButton = new JButton("Exit");
		exitButton.setBounds(Styles.EXIT_BUTTON_LOCATION);
		exitButton.setFont(Styles.BUTTON_FONT);
		exitButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		exitButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		menuPanel.add(exitButton);
		
		// Level Selector
		levelSelectionBox = new JComboBox<String>();
		levelSelectionBox.setBounds(Styles.LEVEL_SELECTOR_LOCATION);
		levelSelectionBox.setFont(Styles.BUTTON_FONT);
		levelSelectionBox.setBackground(Styles.BUTTON_BACK_COLOUR);
		levelSelectionBox.setForeground(Styles.BUTTON_TEXT_COLOUR);
		levelSelectionBox.setMaximumRowCount(7);
		((JLabel) levelSelectionBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		menuPanel.add(levelSelectionBox);
		
		// Menu Music Buttons
		musicOnIcon = new ImageIcon("sprites/icon_music_on.png");
		soundOnIcon = new ImageIcon("sprites/icon_sound_on.png");
		musicOffIcon = new ImageIcon("sprites/icon_music_off.png");
		soundOffIcon = new ImageIcon("sprites/icon_sound_off.png");
		
		menuMusicButton = new JButton("", musicOnIcon);
		menuMusicButton.setBounds(Styles.MENU_MUSIC_BUTTON_LOCATION);
		menuPanel.add(menuMusicButton);
		
		menuSoundButton = new JButton("", soundOnIcon);
		menuSoundButton.setBounds(Styles.MENU_SOUND_BUTTON_LOCATION);
		menuPanel.add(menuSoundButton);
		
		music = true;
		sound = true;
		
		// Main Background
		mainBackgroundImage = new ImageIcon("sprites/screen_main.png");
		mainBackgroundLabel = new JLabel(mainBackgroundImage);
		mainBackgroundLabel.setBounds(Styles.ENTIRE_FRAME);
		menuPanel.add(mainBackgroundLabel);
		menuPanel.setComponentZOrder(mainBackgroundLabel, menuPanel.getComponentCount()-1);
		
		mainPanel.add(menuPanel);
		
		menuMusic.start();
		
		// Congratulations Banner
		congratulationsImage = new ImageIcon("sprites/screen_congratulations.png");
		congratulationsLabel = new JLabel(congratulationsImage);
		congratulationsLabel.setBounds(Styles.CONGRATULATIONS_BOUNDS);
		congratulationsLabel.setVisible(false);
		
		// Fader
		fader = new Fader();
		mainPanel.add(fader.getLabel());
		mainPanel.setComponentZOrder(fader.getLabel(), 0);
		
		// ------------ Pause Panel ------------ //
		pausePanel = new JPanel(null);
		pausePanel.setBounds(Styles.PAUSE_PANEL_LOCATION);
		pausePanel.setBackground(Styles.PAUSE_BG_COLOUR);
		pausePanel.setBorder(Styles.PAUSE_PANEL_BORDER);
		
		resumeButton = new JButton("Resume");
		resumeButton.setBounds(Styles.RESUME_BUTTON_LOCATION);
		resumeButton.setFont(Styles.BUTTON_FONT);
		resumeButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		resumeButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		pausePanel.add(resumeButton);
		
		restartButton = new JButton("Restart");
		restartButton.setBounds(Styles.RESTART_BUTTON_LOCATION);
		restartButton.setFont(Styles.BUTTON_FONT);
		restartButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		restartButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		pausePanel.add(restartButton);
		
		pauseHelpButton = new JButton("Help");
		pauseHelpButton.setBounds(Styles.PAUSE_HELP_BUTTON_LOCATION);
		pauseHelpButton.setFont(Styles.BUTTON_FONT);
		pauseHelpButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		pauseHelpButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		pausePanel.add(pauseHelpButton);
		
		returnButton = new JButton("Return to Menu");
		returnButton.setBounds(Styles.RETURN_BUTTON_LOCATION);
		returnButton.setFont(Styles.BUTTON_FONT);
		returnButton.setBackground(Styles.BUTTON_BACK_COLOUR);
		returnButton.setForeground(Styles.BUTTON_TEXT_COLOUR);
		pausePanel.add(returnButton);
		
		// Music Buttons
		pauseMusicButton = new JButton("", musicOnIcon);
		pauseMusicButton.setBounds(Styles.PAUSE_MUSIC_BUTTON_LOCATION);
		pausePanel.add(pauseMusicButton);
		
		pauseSoundButton = new JButton("", soundOnIcon);
		pauseSoundButton.setBounds(Styles.PAUSE_SOUND_BUTTON_LOCATION);
		pausePanel.add(pauseSoundButton);

		// ------------ Load File Save ------------ //
		try {
			BufferedReader saveReader = new BufferedReader(new FileReader("save.txt"));
			String line = saveReader.readLine();
			if (line != null) {
				if (line.equals("unlockAll")) {
					unlockedLevels = TOTAL_LEVELS;
					for (int i = 1; i <= unlockedLevels; i++) {
						solved[i] = true;
					}
				}
				else {
					unlockedLevels = Integer.parseInt(line);
				}
				while ((line = saveReader.readLine()) != null) {
					int unlockedLevel = Integer.parseInt(line);
					solved[unlockedLevel] = true;
				}
			}
			saveReader.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("save.txt not found!");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		updateLevelSelector();

		// ------------ Action Listeners ------------ //
		startButton.addActionListener(this);
		startButton.setActionCommand("Start");
		menuHelpButton.addActionListener(this);
		menuHelpButton.setActionCommand("Help");
		exitButton.addActionListener(this);
		exitButton.setActionCommand("Exit");
		
		resumeButton.addActionListener(this);
		resumeButton.setActionCommand("Resume");
		restartButton.addActionListener(this);
		restartButton.setActionCommand("Restart");
		pauseHelpButton.addActionListener(this);
		pauseHelpButton.setActionCommand("Help");
		returnButton.addActionListener(this);
		returnButton.setActionCommand("Return");
		
		levelSelectionBox.addActionListener(this);
		levelSelectionBox.setActionCommand("LevelSelected");
		
		menuSoundButton.addActionListener(this);
		menuSoundButton.setActionCommand("Toggle Sound");
		menuMusicButton.addActionListener(this);
		menuMusicButton.setActionCommand("Toggle Music");
		pauseSoundButton.addActionListener(this);
		pauseSoundButton.setActionCommand("Toggle Sound");
		pauseMusicButton.addActionListener(this);
		pauseMusicButton.setActionCommand("Toggle Music");
		
		// Animation timer
		animationTimer = new Timer(Styles.FRAME_DURATION, this);
		animationTimer.setActionCommand("Animate");
		animationTimer.start();
		
		// Initialize JFrame
		mainPanel.setPreferredSize(Styles.FRAME_DIMENSION);
		frame.add(mainPanel);
		
		// Retain focus to enable keyboard shortcuts
		frame.setFocusable(true);
		frame.requestFocus();
		frame.addKeyListener(this);
		
		frame.setResizable(false);
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
		
		// Press any key to put away CONGRATULATIONS screen
		if (currentScreen.equals("level") && activeLevel != null && activeLevel.isWin()) {
			changeScreen("menu");
		}
		
		if (currentScreen.equals("level")) {
			// Arrow Keys --> Movement
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
			// Z --> Undo
			else if (key == KeyEvent.VK_Z) {
				performAction("Undo");
			}
			// R --> Restart
			else if (key == KeyEvent.VK_R) {
				performAction("Restart");
			}
			
			// ESC --> Pause
			if (key == KeyEvent.VK_ESCAPE) {
				levelMusic.stop();
				voidMusic.stop();
	
				pausePanel.setVisible(true);
				activeLevel.getPanel().setComponentZOrder(pausePanel, 0);
				activeLevel.getPanel().updateUI();
			}
			// Any Key --> Unpause
			else {
				performAction("Resume");
			}
		}
		// Menu keyboard shortcuts
		else if (currentScreen.equals("menu")) {
			if (key == KeyEvent.VK_ENTER) {
				// performAction("Start");
			}
			// ESC --> Exit
			else if (key == KeyEvent.VK_ESCAPE) {
				// performAction("Exit");
			}
		}
	}

	// Description: Called when a button is clicked or the animation timer ticks. (implements ActionListener interface)
	// Parameters: The ActionEvent being called. 
	// Return: Void.
	public void actionPerformed(ActionEvent e) {
		performAction(e.getActionCommand());
	}

	// Description: Executes a specific action.
	// Parameters: The name of the action command to perform.
	// Return: Void.
	public void performAction(String action) {
		
		// Animations
		if (action.equals("Animate")) {
			if (currentScreen.equals("level")) {
				for (Block b : activeLevel.getBlocks()) {
					b.animate();
				}
			}
			fader.animate();
			if (fader.isAnimating() && fader.getFrame() == Styles.FADE_CRITICAL_FRAME) {
				showScreen();
			}
			return;
		}
		
		// Sound Buttons
		if (action.equals("Toggle Sound")) {
			sound = !sound;
			if (sound) {
				menuSoundButton.setIcon(soundOnIcon);
				pauseSoundButton.setIcon(soundOnIcon);
			}
			else {
				menuSoundButton.setIcon(soundOffIcon);
				pauseSoundButton.setIcon(soundOffIcon);
			}
			return;
		}
		else if (action.equals("Toggle Music")) {
			music = !music;
			if (music) {
				menuMusicButton.setIcon(musicOnIcon);
				pauseMusicButton.setIcon(musicOnIcon);
				if (currentScreen.equals("menu")) {
					menuMusic.start();
					menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
				}
			}
			else {
				menuMusicButton.setIcon(musicOffIcon);
				pauseMusicButton.setIcon(musicOffIcon);
				levelMusic.stop();
				menuMusic.stop();
				voidMusic.stop();
			}
			return;
		}

		// Main Menu
		if (action.equals("Start")) {
			changeScreen("level");
		}
		else if (action.equals("Help")) {
			try {
				Desktop.getDesktop().open(new File("README.pdf"));
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (action.equals("Exit")) {
			System.exit(0);
		}
		else if (action.equals("LevelSelected")) {
			// System.out.println(levelSelectionBox.getSelectedIndex());
		}
		
		// Screen-Specific Actions
		if (currentScreen.equals("level")) {
		
			if (action.indexOf(' ') != -1 && action.substring(0, action.indexOf(' ')).equals("Move")) {
				
				// For sound effects
				int blockCount = activeLevel.getBlocks().size();
				int ruleCount = activeLevel.getActiveRuleCount();
				
				// Get movement direction
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
					congratulationsLabel.setVisible(true);
					activeLevel.getPanel().add(congratulationsLabel);
					activeLevel.getPanel().setComponentZOrder(congratulationsLabel, 0);
					activeLevel.getPanel().updateUI();
					mainPanel.updateUI();
					
					// Win sound effect
					if (sound) {
						winSound.setFramePosition(0);
						winSound.start();
					}
					
					// Unlock two new levels
					if (!solved[activeLevel.getNumber()]) {
						int unlockUpTo = Math.min(unlockedLevels + LEVELS_TO_UNLOCK, TOTAL_LEVELS);
						while (unlockedLevels < unlockUpTo) {
							unlockedLevels++;
						}
						solved[activeLevel.getNumber()] = true;
					}
					
					updateLevelSelector();
					
					// Update File Save
					try {
						BufferedWriter saveWriter = new BufferedWriter(new FileWriter("save.txt"));
						saveWriter.write(unlockedLevels + "\n");
						for (int i = 1; i < TOTAL_LEVELS; i++) {
							if (solved[i]) {
								saveWriter.write(i + "\n");
							}
						}
						saveWriter.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				// Sound effects
				if (sound) {
					if (activeLevel.getBlocks().size() < blockCount) {
						destroySound.setFramePosition(0);
						destroySound.start();
					}
					if (activeLevel.getActiveRuleCount() > ruleCount) {
						ruleSound.setFramePosition(0);
						ruleSound.start();
					}
					moveSound.setFramePosition(0);
					moveSound.start();
				}
				updateLevelMusic();
			}
			else if (action.equals("Undo")) {
				activeLevel.undo();
				activeLevel.getPanel().add(pausePanel);
				
				if (sound) {
					undoSound.setFramePosition(0);
					undoSound.start();
				}
				
				updateLevelMusic();
			}
			else if (action.equals("Restart")) {
				mainPanel.remove(activeLevel.getPanel());
				changeScreen("level");
			}
			else if (action.equals("Return")) {
				changeScreen("menu");
			}
			else if (action.equals("Resume")) {
				pausePanel.setVisible(false);
				updateLevelMusic();
			}
		}
	}
	
	
	// Description: Updates the level selection JComboBox to update the unlocked and solved levels.
	// Parameters: None.
	// Return: Void. 
	public void updateLevelSelector() {
		levelSelectionBox.removeAllItems();
		
		int earliestUnsolved = TOTAL_LEVELS;
		
		for (int i = 1; i <= unlockedLevels; i++) {
			if (solved[i]) {
				levelSelectionBox.addItem("Level " + i + " (solved)");
			}
			else {
				levelSelectionBox.addItem("Level " + i);
				earliestUnsolved = Math.min(earliestUnsolved, i);
			}
		}
		
		// Automatically select the latest unsolved level
		levelSelectionBox.setSelectedIndex(earliestUnsolved-1);
	}
	
	// Description: Updates the music when a Level is in focus.
	// Parameters: None.
	// Return: Void. 
	public void updateLevelMusic() {
		if (!music) return;
		
		if (activeLevel.hasYou()) {
			voidMusic.stop();
			levelMusic.start();
			levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
		// Not You: Stop music
		else {
			levelMusic.stop();
			voidMusic.start();
			voidMusic.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	// Description: Begins animations to change to a new screen. (called once at the beginning of a screen change)
	// Parameters: The screen to change to.
	// Return: Void. 
	public void changeScreen(String screen) {
		// Start fading transition
		fader.fade();
		
		// Pause all music
		levelMusic.stop();
		menuMusic.stop();
		voidMusic.stop();
		
		// Change the current screen
		if (screen.equals("menu")) {
			currentScreen = "menu";
		}
		else if (screen.equals("level")) {
			currentScreen = "level";
			openLevel();
			
			if (sound) {
				startSound.setFramePosition(0);
				startSound.start();
			}
		}
		mainPanel.updateUI();
	}
	
	// Description: Show the current screen and start the corresponding music. (called midway through the animation of a screen change)
	// Parameters: None.
	// Return: Void. 
	public void showScreen() {
		
		// Hide all screens by default
		menuPanel.setVisible(false);
		if (activeLevel != null)
			activeLevel.getPanel().setVisible(false);
		congratulationsLabel.setVisible(false);
		pausePanel.setVisible(false);

		if (currentScreen.equals("menu")) {
			menuPanel.setVisible(true);			
			if (activeLevel != null) 
				mainPanel.remove(activeLevel.getPanel());
			
			// Start menu music
			if (music) {
				menuMusic.start();
				menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		else if (currentScreen.equals("level")) {	
			openLevel();
			activeLevel.getPanel().setVisible(true);
			
			// Start level music
			if (music) {
				levelMusic.start();
				levelMusic.loop(Clip.LOOP_CONTINUOUSLY);
			}
		}
		mainPanel.updateUI();
	}
	
	// Description: Opens the level currently selected by the ComboBox.
	// Parameters: None.
	// Return: Void. 
	public void openLevel() {
		
		// Open Selected Level
		int selectedLevel = levelSelectionBox.getSelectedIndex()+1;

		activeLevel = new Level(selectedLevel);
		mainPanel.add(activeLevel.getPanel());
		activeLevel.getPanel().add(pausePanel);
		pausePanel.setVisible(false);
	}
	
	// Description: Loads all the sprites and sound assets in the game. (called once upon program initialization)
	// Parameters: None.
	// Return: Void. 
	public void loadAssets() {
		
		// Load all sprites
		BlockIcon.loadAssets();
		
		// Load all sounds 
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
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/start.wav"));
            startSound = AudioSystem.getClip();
            startSound.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/win.wav"));
            winSound = AudioSystem.getClip();
            winSound.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/rule.wav"));
            ruleSound = AudioSystem.getClip();
            ruleSound.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/move.wav"));
            moveSound = AudioSystem.getClip();
            moveSound.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/undo.wav"));
            undoSound = AudioSystem.getClip();
            undoSound.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(new File("sounds/destroy.wav"));
            destroySound = AudioSystem.getClip();
            destroySound.open(audioInputStream);
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
	
	public static void main(String[] args) {
		new Main();		
	}

	// Unused Methods (implements KeyListener)
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

}
