package game;

import java.util.ArrayList;

public class BlockAttributes {

	public boolean isPush;
	public boolean isStop;
	public boolean isYou;
	public boolean isWin;
	public boolean isDefeat;
	public boolean isSink;
	public boolean isMove;
	
	public String transform;
	
	public int animationCycleLength;
	
	//static ArrayList<BlockIcon> blockIcons;
	
	public BlockIcon icon;
	
	
	BlockAttributes(String type) {
		
		if (type.equals("text")) return;
		
		icon = new BlockIcon(type);
		
		
		reset();
		
	}
	
	public void set(String attribute) {
		
		// Adding a rule of the format NOUN VERB ADJECTIVE
		if (attribute.equals("push")) {
			isPush = true;
		}
		else if (attribute.equals("stop")) {
			isStop = true;
		}
		else if (attribute.equals("you")) {
			isYou = true;
		}
		else if (attribute.equals("win")) {
			isWin = true;
		}
		else if (attribute.equals("defeat")) {
			isDefeat = true;
		}
		else if (attribute.equals("sink")) {
			isSink = true;
		}
		else if (attribute.equals("move")) {
			isMove = true;
		}
		// Adding a rule of the format NOUN VERB NOUN
		else {
			transform = attribute;
		}
	}
	
	public void reset() {
		isPush = false;
		isStop = false;
		isYou = false;
		isWin = false;
		isDefeat = false;
		isSink = false;
		isMove = false;
		
		transform = null;
	}
	
	
}
