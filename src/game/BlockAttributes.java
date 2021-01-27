package game;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class BlockAttributes {

	private boolean isPush;
	private boolean isStop;
	private boolean isYou;
	private boolean isWin;
	private boolean isDefeat;
	private boolean isSink;
	private boolean isMove;
	
	private String transform;
	
	public int animationCycleLength;
	
	//static ArrayList<BlockIcon> blockIcons;
	
	private static BlockAttributes destroyedAttributes = new BlockAttributes("dummy");
	
	BlockAttributes(String type) {
		
		if (type.equals("text")) return;
		
		
		
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
	
	public void or(BlockAttributes b) {
		isPush |= b.isPush();
		isStop |= b.isStop();
		isYou |= b.isYou();
		isWin |= b.isWin();
		isDefeat |= b.isDefeat();
		isSink |= b.isSink();
		isMove |= b.isMove();
	}
	
	// Getter Methods
	public boolean isPush() {
		return isPush;
	}
	
	public boolean isStop() {
		return isStop;
	}
	
	public boolean isYou() {
		return isYou;
	}
	
	public boolean isWin() {
		return isWin;
	}
	
	public boolean isDefeat() {
		return isDefeat;
	}
	
	public boolean isSink() {
		return isSink;
	}
	
	public boolean isMove() {
		return isMove;
	}
	
	public String getTransform() {
		return transform;
	}
	
	public static BlockAttributes getDestroyedAttributes() {
		return destroyedAttributes;
	}
	
	// Setter Methods
	public void setPush(boolean isPush) {
		this.isPush = isPush;
	}
	
	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
	
	public void setYou(boolean isYou) {
		this.isYou = isYou;
	}
	
	public void setWin(boolean isWin) {
		this.isWin = isWin;
	}
	
	public void setDefeat(boolean isDefeat) {
		this.isDefeat = isDefeat;
	}
	
	public void setSink(boolean isSink) {
		this.isSink = isSink;
	}
	
	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}
	
}
