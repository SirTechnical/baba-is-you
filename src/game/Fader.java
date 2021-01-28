// Fader objects create the fade effect shown when screens are changed.

package game;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.image.*;

public class Fader {

	// Fader Image
	private JLabel label;
	private ImageIcon icon;
	private BufferedImage image;
	
	// Fader Position
	private int posX;
	private static final int posY = 0;
	
	// Fader Animation
	private boolean animating;
	private int framesLeft;
	
	// Animation Constants
	private static final int START_X = -2*Styles.FRAME_WIDTH;
	private static final int DEST_X = 2*Styles.FRAME_WIDTH;
	private static final double DX = (DEST_X - START_X) / (double) Styles.FADE_FRAMES;
	
	// Constructor: Creates a new Fader object (no parameters)
	public Fader() {

		// Initialize image
		try {
			image = ImageIO.read(new File("sprites/screen_fader.png"));	
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
		}
		icon = new ImageIcon(image);
		label = new JLabel(icon);
		
		// Initialize position
		posX = START_X; 
		label.setBounds(Styles.FADER_BOUNDS);
		label.setLocation(posX, posY);
	}
	
	// Description: Starts animating this Fader object.
	// Parameters: None.
	// Return: Void.
	public void fade() {
		framesLeft = Styles.FADE_FRAMES;
		animating = true;
	}
	
	// Description: Smoothly animates this Fader object by changing the location of its JLabel.
	//				This method is called on a timer in the main class.
	//				The Fader advances by one animation frame each time this method is called.
	// Parameters: None.
	// Return: Void.
	public void animate() {
		if (!animating) return;
		
		// Advance position by one frame
		framesLeft--;
		posX += DX;
		
		// Reset and stop animating if no more frames left
		if (framesLeft == 0) {
			posX = START_X;
			animating = false;
		}
		label.setLocation(posX, posY);
	}
	
	// Getter Methods
	public JLabel getLabel() {
		return label;
	}
	
	public boolean isAnimating() {
		return animating;
	}
	
	public int getFrame() {
		return framesLeft;
	}
	
	// Setter Methods
	
}
