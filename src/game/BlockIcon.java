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
	private boolean isRed = false;
	
	
	private static HashMap<String, ImageIcon> images;
	
	
	// loads all images
	public static void loadAssets() {
		images = new HashMap<String, ImageIcon>();
		
		images.put("wall", getImage("wall"));
		
		
	}
	
	public static ImageIcon getImage(String type) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("assets/" + type + ".png"));
			
			image = updateColour(image);

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
			images.put(type, getImage(type));
		}

		iconLabel = new JLabel(images.get(type));
		iconLabel.setSize(Styles.BLOCK_DIM);
	}
	
	public static BufferedImage updateColour(BufferedImage image) {
		
		// image recolouring
		int redPercent = 50;
		int greenPercent = 70;
		int bluePercent = 70;

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = image.getRGB(x, y);

				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = pixel & 0xff;

				pixel = (alpha<<24) | (redPercent*red/100<<16) | (greenPercent*green/100<<8) | (bluePercent*blue/100);

				image.setRGB(x, y, pixel);
			}
		}
		
		return image;
	}
	
	public void updatePos(int row, int col) {
		iconLabel.setLocation(col * Styles.BLOCK_SIZE, row * Styles.BLOCK_SIZE);
	}
	
	public JLabel getLabel() {
		return iconLabel;
	}
	
	
}
