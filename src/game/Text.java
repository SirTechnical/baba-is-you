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
		put("text_skull", "noun");
		put("text_water", "noun");
		put("text_box", "noun");
		put("text_key", "noun");
		put("text_door", "noun");
		put("text_keke", "noun");
		
		put("text_is", "verb");
		
		put("text_you", "adjective");
		put("text_win", "adjective");
		put("text_push", "adjective");
		put("text_stop", "adjective");
		put("text_defeat", "adjective");
		put("text_sink", "adjective");
		put("text_open", "adjective");
		put("text_shut", "adjective");
		put("text_move", "adjective");


	}};
	
	public Text(String type, int row, int col, Level parentLevel) {
		
		super(type, row, col, parentLevel);
		
		textType = type;
		word = textType.substring(textType.indexOf('_') + 1);
		
		
		function = TEXT_FUNCTIONS.get(type);
		
		
		
		active = false;
	}
	
	public Text(String type, Text old, Level parentLevel) {
		super(type, (Block) old, parentLevel);
		textType = type;
		word = textType.substring(textType.indexOf('_') + 1);
		
		
		function = TEXT_FUNCTIONS.get(type);
		
		
		
		active = false;
	}
	
	// Overrides
	public void updateGraphics() {
		this.getIcon().updatePos(this.getRow(), this.getCol());
		if (active)
			this.getIcon().setIcon(textType);
		else
			this.getIcon().setIcon(textType + "_inactive");
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
	
	public String getTextType() {
		return textType;
	}
	
	public boolean getActive() {
		return active;
	}

	// Setter Methods
	public void setActive(boolean active) {
		this.active = active;
	}
		
}
