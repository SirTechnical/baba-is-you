// This class handles the display graphics for one Block. 
// by storing a JLabel to represent each Block
// as well as storing a collection of all the recoloured and resized ImageIcon sprites.

// CLASS IS BLOCKICON

package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class BlockIcon {
	
	// The JLabel for this Block's icon
	private JLabel iconLabel;	
	
	// Stores all the recoloured ImageIcons that were loaded before (to prevent reloading already-loaded images
	private static HashMap<String, ImageIcon> images = new HashMap<String, ImageIcon>();
	
	// Constructor: Creates a BlockIcon for a specific type of Block.
	public BlockIcon(String type) {

		// Load a new BlockIcon only if this type of Block was not loaded before
		if (!images.containsKey(type)) {

			// To show that each unique icon is only loaded once:
			// System.out.println("load " + type);

			images.put(type, getImage(type));

			// If this is a TEXT, add an inactive version of the image
			if (type.indexOf('_') != -1) {
				images.put(type + "_inactive", getImage(type, false));
			}
		}

		// Initialize JLabel
		iconLabel = new JLabel();
		if (type.indexOf('_') == -1) {
			iconLabel.setIcon(images.get(type));
		}
		iconLabel.setSize(Styles.BLOCK_DIM);
	}
	
	// Description: Creates a new, recoloured, and resized ImageIcon of a specific type .
	// Parameters: The type of the ImageIcon to create, and whether it is full-colour (active) or not (dim).
	// Return: The new, recoloured, and resized ImageIcon.
	public static ImageIcon getImage(String type, boolean active) {
		
		// Read image from file
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("sprites/" + type + ".png"));
			
			// Default colour filter: White (full brightness)
			Color filter = Color.WHITE;
			
			// Text colour filter: Light red (for all TEXT that is not IS)
			if (type.indexOf('_') != -1 && !type.substring(type.indexOf('_') + 1).equals("is")) {
				filter = Styles.COLOUR_TEXT;
			}
			
			// Inactive text colour filter: Dull colour (dimmed brightness)
			if (type.indexOf('_') != -1 && !active) {
				filter = Styles.COLOUR_INACTIVE_TEXT;
			}
			
			// Recolour image
			image = updateColour(image, filter);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
		}
		
		ImageIcon recolouredImage = new ImageIcon(image);
		ImageIcon resizedImage = new ImageIcon(recolouredImage.getImage().getScaledInstance(Styles.BLOCK_SIZE, Styles.BLOCK_SIZE, Image.SCALE_SMOOTH));
		
		return resizedImage;
	}
	
	// getImage() overload: New ImageIcons are full-colour by default.
	public static ImageIcon getImage(String type) {
		return getImage(type, true);
	}
	
	// Description: Recolours a BufferedImage with a specified RGBA colour filter.
	// Parameters: The image to recolour, a Color object representing an RGBA colour filter to colour it with.
	// Return: The recoloured image.
	public static BufferedImage updateColour(BufferedImage image, Color filter) {
		
		// Individually recolours each pixel of the image
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = image.getRGB(x, y);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;

				pixel = (filter.getAlpha()*alpha/255<<24) | (filter.getRed()*red/255<<16) | (filter.getGreen()*green/255<<8) | (filter.getBlue()*blue/255);
				
				image.setRGB(x, y, pixel);
			}
		}
		return image;
	}
	
	// Description: Moves the position of the JLabel of this Block to specified coordinates.
	// Parameters: The coordinates to move this BlockIcon to.
	// Return: Void.
	public void updatePos(double posX, double posY) {
		iconLabel.setLocation((int) posX, (int) posY);
	}
	
	// Getter Methods
	public JLabel getLabel() {
		return iconLabel;
	}
	
	// Setter Methods
	public void setIcon(String type) {
		iconLabel.setIcon(images.get(type));
	}
	
}
