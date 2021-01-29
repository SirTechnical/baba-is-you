// This class defines most graphics constants for the entire game.

package game;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Styles {

	// ---------- Graphics Constants ---------- //
	
	// Blocks and Grid
	public static final int BLOCK_SIZE = 48;
	public static final Dimension BLOCK_DIM = new Dimension(BLOCK_SIZE, BLOCK_SIZE);
	
	public static final int MAX_GRID_HEIGHT = 18;
	public static final int MAX_GRID_WIDTH = 24;
	
	// Button Styles
	public static final int BUTTON_HEIGHT = 45;
	public static final int BUTTON_WIDTH = 403;
	
	public static final Color BUTTON_BACK_COLOUR = new Color(35, 41, 67);
	public static final Color BUTTON_TEXT_COLOUR = new Color(255, 255, 255);
	public static final Color BUTTON_BORDER_COLOUR = new Color(78, 90, 148);
	
	public static final int VOLUME_BUTTON_SIZE = 48;
	public static final int VOLUME_BUTTON_PADDING = 24 + VOLUME_BUTTON_SIZE;
	
	// Main Frame
	public static final int FRAME_HEIGHT = MAX_GRID_HEIGHT * BLOCK_SIZE;	// 864
	public static final int FRAME_WIDTH = MAX_GRID_WIDTH * BLOCK_SIZE;		// 1152
	public static final Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
	public static final Rectangle ENTIRE_FRAME = new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
	
	// Menu Panel
	public static final int MAIN_TOP_BUTTON_HEIGHT = 550;
	public static final int BUTTON_VERTICAL_PADDING = 50 + BUTTON_HEIGHT;
	
	public static final Rectangle START_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle MENU_HELP_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT + BUTTON_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle EXIT_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT + 2*BUTTON_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

	public static final int LEVEL_SELECTOR_HEIGHT = BUTTON_HEIGHT;
	public static final int LEVEL_SELECTOR_WIDTH = BUTTON_WIDTH;	
	public static final Rectangle LEVEL_SELECTOR_LOCATION = new Rectangle(FRAME_WIDTH/2 - LEVEL_SELECTOR_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT - BUTTON_VERTICAL_PADDING, LEVEL_SELECTOR_WIDTH, LEVEL_SELECTOR_HEIGHT);

	public static final Rectangle MENU_SOUND_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH - 2*VOLUME_BUTTON_PADDING, FRAME_HEIGHT - VOLUME_BUTTON_PADDING, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE);
	public static final Rectangle MENU_MUSIC_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH - VOLUME_BUTTON_PADDING, FRAME_HEIGHT - VOLUME_BUTTON_PADDING, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE);
	
	// Level Panel
	public static final Color LEVEL_BG_COLOUR = new Color(8, 8, 8);
	
	// Pause Panel
	public static final int PAUSE_PANEL_PADDING = 50;
	public static final int PAUSE_VERTICAL_PADDING = BUTTON_VERTICAL_PADDING - 25;
	public static final int PAUSE_PANEL_HEIGHT = 3*PAUSE_VERTICAL_PADDING + 2*PAUSE_PANEL_PADDING + VOLUME_BUTTON_PADDING;
	public static final int PAUSE_PANEL_WIDTH = BUTTON_WIDTH + 2*PAUSE_PANEL_PADDING;
	public static final Rectangle PAUSE_PANEL_LOCATION = new Rectangle(FRAME_WIDTH/2 - PAUSE_PANEL_WIDTH/2, FRAME_HEIGHT/2 - PAUSE_PANEL_HEIGHT/2, PAUSE_PANEL_WIDTH, PAUSE_PANEL_HEIGHT);
 	
	public static final int PAUSE_TOP_BUTTON_HEIGHT = PAUSE_PANEL_HEIGHT/2 - BUTTON_HEIGHT/2 - BUTTON_VERTICAL_PADDING - VOLUME_BUTTON_PADDING/2 - 12;
	public static final Rectangle RESUME_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 - BUTTON_WIDTH/2, PAUSE_TOP_BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle RESTART_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 - BUTTON_WIDTH/2, PAUSE_TOP_BUTTON_HEIGHT + PAUSE_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle PAUSE_HELP_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 - BUTTON_WIDTH/2, PAUSE_TOP_BUTTON_HEIGHT + 2*PAUSE_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle RETURN_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 - BUTTON_WIDTH/2, PAUSE_TOP_BUTTON_HEIGHT + 3*PAUSE_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
	
	public static final Rectangle PAUSE_SOUND_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 - VOLUME_BUTTON_PADDING, PAUSE_PANEL_HEIGHT - VOLUME_BUTTON_PADDING, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE);
	public static final Rectangle PAUSE_MUSIC_BUTTON_LOCATION = new Rectangle(PAUSE_PANEL_WIDTH/2 + VOLUME_BUTTON_PADDING - VOLUME_BUTTON_SIZE, PAUSE_PANEL_HEIGHT - VOLUME_BUTTON_PADDING, VOLUME_BUTTON_SIZE, VOLUME_BUTTON_SIZE);
	
	public static final Color PAUSE_BG_COLOUR = new Color(94, 108, 188, 150);
	
	public static final Border PAUSE_PANEL_BORDER = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK);
	
	// Congratulations Banner
	public static final int CONGRATULATIONS_HEIGHT = 180;
	public static final int CONGRATULATIONS_WIDTH = 1144;
	public static final Rectangle CONGRATULATIONS_BOUNDS = new Rectangle(FRAME_WIDTH/2 - CONGRATULATIONS_WIDTH/2, FRAME_HEIGHT/2 - CONGRATULATIONS_HEIGHT/2, CONGRATULATIONS_WIDTH, CONGRATULATIONS_HEIGHT);
	
	// Fader
	public static final Rectangle FADER_BOUNDS = new Rectangle(0, 0, 2*FRAME_WIDTH, FRAME_HEIGHT);
	
	// Colour Filters
	public static final Color COLOUR_TEXT = new Color(255, 255, 255);
	// public static final Color COLOUR_TEXT = new Color(217, 56, 107);
	public static final Color COLOUR_INACTIVE_TEXT = new Color(100, 100, 100);	
	
	// Fonts
	public static final Font TITLE_FONT = new Font("Amatic SC", Font.PLAIN, 12);
	public static final Font BUTTON_FONT = new Font("Courier", Font.BOLD, 30);
	
	// Animation Constants
	public static final int FRAME_DURATION = 10;
	public static final int ANIMATION_FRAMES = 7;
	
	public static final int FADE_FRAMES = 100;
	public static final int FADE_CRITICAL_FRAME = 50;

}
