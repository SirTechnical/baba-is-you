package game;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Styles {

	// Layout Constants
	
	public static final int FRAME_HEIGHT = 750;
	public static final int FRAME_WIDTH = 1000;
	public static final Dimension FRAME_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
	
	public static final int BUTTON_HEIGHT = 50;
	public static final int BUTTON_WIDTH = 100;
	
	public static final Rectangle START_BUTTON_LOCATION = new Rectangle(200, 100, BUTTON_WIDTH, BUTTON_HEIGHT);
	public static final Rectangle EXIT_BUTTON_LOCATION = new Rectangle(300, 200, BUTTON_WIDTH, BUTTON_HEIGHT);

	
	
	
	public static final Font TITLE_FONT = new Font("Amatic SC", Font.PLAIN, 12);
	
	
	// Animation Constants
	public static final int FPS = 30;
	public static final int ANIMATION_DURATION = 100;
	
	
	
	// Graphics Constants
	public static final int BLOCK_SIZE = 48;
	public static final Dimension BLOCK_DIM = new Dimension(BLOCK_SIZE, BLOCK_SIZE);
	
	public static final Color LEVEL_BG_COLOUR = new Color(8, 8, 8);
	
	
	
	
	
	
	

}
