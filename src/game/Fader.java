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
		
		//icon = new ImageIcon(image);
		label = new JLabel(icon);
		label.setBounds(Styles.ENTIRE_FRAME);
		label.setLocation(-100, -100);
	}
	
	
	// Getter Methods
	public JLabel getLabel() {
		return label;
	}
	
	// Setter Methods
	
}
