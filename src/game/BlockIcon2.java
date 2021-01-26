// COLOUR IS CHANGE

package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;


// Extends ImageIcon?

public class BlockIcon2 extends BufferedImage {

	public BlockIcon2(String fileName) {
		super(1, 1, 1);
		
		try {
			image = ImageIO.read(new File("assets/" + fileName + ".png"));
		}	
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
		}
	}
	
	
	// Just to make Java happy
	public BlockIcon2(int width, int height, int imageType) {
		
		super(width, height, imageType);
		
		
		// TODO Auto-generated constructor stub
	}

	private static Color a = new Color(155, 0, 155, 1);
	private static BufferedImage image;
	
	
	public static void recolor() {
		
		// image recolouring
		
		for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);

                int alpha = (pixel >> 24) & 0xff;
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;

                pixel = (a.getAlpha() * alpha << 24) 
                	  | (a.getRed() * red / 255 << 16) 
                	  | (a.getGreen() * green / 255 << 8) 
                	  | (a.getBlue() * blue / 255);

                image.setRGB(x, y, pixel);
            }
        }

	}
	
	public static BufferedImage getImage() {
		return image;
	}
	
	
}
