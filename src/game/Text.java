// Text objects represent a special type of Block: a rule Block (BABA, IS, YOU, etc.) 
// Inherits methods and variables from Block class

// CLASS IS TEXT
// TEXT IS BLOCK

package game;

import java.util.*;

public class Text extends Block {

	private String word;		// "baba" (the word being represented)
	private String textType;	// "text_baba" (the full type of this Text object)
	private boolean active;		// Whether this Text is part of an active rule or not
	private String function;	// The function of this Text (noun, verb, or adjective)
	
	// A dictionary to convert from textType to function
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
		put("text_grass", "noun");
		
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
	
	// Constructor: Creates a specific type of Text at a specified position (row, col) in the active Level.
	public Text(String type, int row, int col, Level parentLevel) {
		super(type, row, col, parentLevel);
		
		textType = type;
		word = textType.substring(textType.indexOf('_') + 1);
		function = TEXT_FUNCTIONS.get(type);
		active = false;
	}
	
	// Constructor: Creates a specific type of Text as a copy of another Text object in the active Level.
	public Text(String type, Text old, Level parentLevel) {
		super(type, (Block) old, parentLevel);
		
		textType = type;
		word = textType.substring(textType.indexOf('_') + 1);
		function = TEXT_FUNCTIONS.get(type);
		active = false;
	}
	
	// Overrides animate() inherited from Block
	// Before animating this Text, makes a check for whether this Text is active or not.
	public void animate() {
		
		// Update the BlockIcon of the Text object to reflect if it is active or not.
		if (active) this.getIcon().setIcon(textType);
		else this.getIcon().setIcon(textType + "_inactive");
		
		// Do animation
		super.animate();
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
