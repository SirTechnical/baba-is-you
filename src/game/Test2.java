// Name: Kevin Guo
// Date: 
// Program: Baba is You
// Description: TODO

package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Test2 {

	private static JFrame frame;
	private static JPanel mainPanel;
	private static JLabel label1, label2;
	private static ImageIcon img;
	private static BufferedImage image;
	
	private static BlockIcon2 test;
	
	
	
	
	public Test2() {
		
		
		try {
			image = ImageIO.read(new File("assets/all_sprites.png"));
			
			
			//test = new BlockIcon2("all_sprites");
			
			
			// image recolouring
			
			
			int redPercent = 50;
			int greenPercent = 0;
			int bluePercent = 50;
			
			for(int y = 0; y < image.getHeight(); y++){
	            for(int x = 0; x < image.getWidth(); x++){
	                int pixel = image.getRGB(x,y);

	                int alpha = (pixel>>24)&0xff;
	                int red = (pixel>>16)&0xff;
	                int green = (pixel>>8)&0xff;
	                int blue = pixel&0xff;

	                pixel = (alpha<<24) | (redPercent*red/100<<16) | (greenPercent*green/100<<8) | (bluePercent*blue/100);

	                image.setRGB(x, y, pixel);
	            }
	        }
			
			
			//image = BlockIcon2.recolor(image);
			
			
			img = new ImageIcon(image);
			label1 = new JLabel(img);
			label2 = new JLabel(img);
			mainPanel = new JPanel();
			mainPanel.add(label1);
			mainPanel.add(label2);
			
			
			
			redPercent = 50;
			greenPercent = 0;
			bluePercent = 100;
			
			for(int y = 0; y < image.getHeight(); y++){
	            for(int x = 0; x < image.getWidth(); x++){
	                int pixel = image.getRGB(x,y);

	                int alpha = (pixel>>24)&0xff;
	                int red = (pixel>>16)&0xff;
	                int green = (pixel>>8)&0xff;
	                int blue = pixel&0xff;

	                pixel = (alpha<<24) | (redPercent*red/100<<16) | (greenPercent*green/100<<8) | (bluePercent*blue/100);

	                image.setRGB(x, y, pixel);
	            }
	        }
			img = new ImageIcon(image);
			

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not found!");
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException!");
		}
		

		// Initialize JFrame
		frame = new JFrame("Baba is You"); 
		frame.add(mainPanel);
		frame.pack();
		frame.setVisible(true);	
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	public static void main(String[] args) {
		new Test2();
		
		
		
	}

}
