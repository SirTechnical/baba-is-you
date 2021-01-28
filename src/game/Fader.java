package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Fader {

	private JLabel label;
	private ImageIcon icon;
	private BufferedImage image;
	
	private int posX;
	private static final int posY = 0;
	
	private boolean animating;
	private int framesLeft;
	
	private static final int START_X = -2*Styles.FRAME_WIDTH;
	private static final int DEST_X = 2*Styles.FRAME_WIDTH;
	private static final double DX = (DEST_X - START_X) / (double) Styles.FADE_FRAMES;
	
	public Fader() {
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
		
		posX = START_X; 
		icon = new ImageIcon(image);
		label = new JLabel(icon);
		label.setBounds(Styles.FADER_BOUNDS);
		label.setLocation(posX, posY);
	}
	
	public void fade() {
		framesLeft = Styles.FADE_FRAMES;
		animating = true;
	}
	
	public void animate() {
		if (!animating) return;
		
		framesLeft--;
		posX += DX;
		
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
