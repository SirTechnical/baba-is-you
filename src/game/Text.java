package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Text extends Block {

	private String word;
	private String textType;
	private boolean active;
	private String function;
	
	@SuppressWarnings("serial")
	static HashMap<String, String> TEXT_FUNCTIONS = new HashMap<String, String>() {{
		
		put("text_baba", "noun");
		put("text_flag", "noun");
		put("text_rock", "noun");
		put("text_wall", "noun");
		
		put("text_is", "verb");
		
		put("text_you", "adjective");
		put("text_win", "adjective");
		put("text_push", "adjective");
		put("text_stop", "adjective");

	}};
	
	public Text(String type, int row, int col, JPanel levelPanel) {
		
		super(type, row, col, levelPanel);
		
		textType = type;
		word = textType.substring(textType.indexOf('_') + 1);
		
		
		function = TEXT_FUNCTIONS.get(type);
		
		
		
		active = false;
	}
	
	// Getter Methods
	public boolean isActive() {
		return active;
	}
	
	public String getWord() {
		return word;
	}
	
	public String getFunction() {
		return function;
	}

	// Setter Methods
		
}
