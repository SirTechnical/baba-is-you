package game;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Styles {

	// Layout Constants
	public static final int BLOCK_SIZE = 48;
	public static final Dimension BLOCK_DIM = new Dimension(BLOCK_SIZE, BLOCK_SIZE);
	
	public static final Color LEVEL_BG_COLOUR = new Color(8, 8, 8);
	
	
	public static final int MAX_GRID_HEIGHT = 18;
	public static final int MAX_GRID_WIDTH = 24;
	

	// Graphics Constants
	public static final int FRAME_HEIGHT = MAX_GRID_HEIGHT * BLOCK_SIZE;	// 864
	public static final int FRAME_WIDTH = MAX_GRID_WIDTH * BLOCK_SIZE;		// 1152
	public static final Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
	
	public static final int BUTTON_HEIGHT = 50;
	public static final int BUTTON_WIDTH = 150;
	
	public static final int MAIN_TOP_BUTTON_HEIGHT = 500;
	public static final int BUTTON_VERTICAL_PADDING = 75 + BUTTON_HEIGHT;
	
	public static final Rectangle START_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle HELP_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT + BUTTON_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle EXIT_BUTTON_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT + 2*BUTTON_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

	public static final Rectangle LEVEL_SELECTOR_LOCATION = new Rectangle(FRAME_WIDTH/2 - BUTTON_WIDTH/2, MAIN_TOP_BUTTON_HEIGHT - BUTTON_VERTICAL_PADDING, BUTTON_WIDTH, BUTTON_HEIGHT);

	
	public static final Rectangle ENTIRE_FRAME = new Rectangle(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
	
	// Colour Filter
	public static final Color COLOUR_TEXT = new Color(217, 56, 107);
	public static final Color COLOUR_INACTIVE_TEXT = new Color(100, 100, 100);
	
	
	public static final Font TITLE_FONT = new Font("Amatic SC", Font.PLAIN, 12);
	
	
	// Animation Constants
	public static final int FPS = 30;
	public static final int ANIMATION_FRAMES = 10;
	
	public static final int FADE_FRAMES = 20;
	
	
	
	
	
	
	
	
	

}
