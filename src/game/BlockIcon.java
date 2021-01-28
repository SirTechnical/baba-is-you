// CLASS IS BLOCKICON
// BLOCK HAS ICON

package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class BlockIcon {
	
	private JLabel iconLabel;

	// temp
	private boolean isColouredText = false;
	
	
	private static HashMap<String, ImageIcon> images;
	
	
	// loads all images
	public static void loadAssets() {
		images = new HashMap<String, ImageIcon>();
		
		//images.put("wall", getImage("wall"));
		//images.put("rock", getImage("rock"));
		
		
	}
	
	public static ImageIcon getImage(String type) {
		return getImage(type, true);
	}
	
	public static ImageIcon getImage(String type, boolean active) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("sprites/" + type + ".png"));
			
			
			// filter
			
			Color filter = Color.WHITE;
			
			if (type.indexOf('_') != -1 && !type.substring(type.indexOf('_') + 1).equals("is")) {
				filter = Styles.COLOUR_TEXT;
			}
			
			if (type.indexOf('_') != -1 && !active) {
				filter = Styles.COLOUR_INACTIVE_TEXT;
			}
			
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
	
	public BlockIcon(String type) {
		
		if (!images.containsKey(type)) {
			System.out.println("load " + type);
			
			images.put(type, getImage(type));
			
			// If TEXT, add inactive version
			if (type.indexOf('_') != -1) {
				images.put(type + "_inactive", getImage(type, false));
			}
		}

		iconLabel = new JLabel();
		
		iconLabel.setIcon(images.get(type));
		
		iconLabel.setSize(Styles.BLOCK_DIM);

	}
	
	public void setIcon(String type) {
		iconLabel.setIcon(images.get(type));
	}
	
	
	// barely used
	
	public static BufferedImage updateColour(BufferedImage image, Color filter) {
		
		// image recolouring
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = image.getRGB(x, y);

				int alpha = (pixel >> 24) & 0xff;	// disregarding alpha
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;

				pixel = (0x0f<<24) | (filter.getRed()*red/255<<16) | (filter.getGreen()*green/255<<8) | (filter.getBlue()*blue/255);

				image.setRGB(x, y, pixel);
			}
		}
		
		return image;
	}
	
	public void updatePos(int row, int col) {
		iconLabel.setLocation(col * Styles.BLOCK_SIZE, row * Styles.BLOCK_SIZE);
	}
	
	// Getter Methods
	public JLabel getLabel() {
		return iconLabel;
	}
	
	// Setter Methods
	
}
