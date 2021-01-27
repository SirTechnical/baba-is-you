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
	private boolean isOpen;
	private boolean isShut;
	private boolean isMove;
	
	private String transform;
	
	public int animationCycleLength;
	
	// Blank BlockAttributes will all fields false
	private static BlockAttributes destroyedAttributes = new BlockAttributes("dummy");
	
	// Constructor
	BlockAttributes(String type) {
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
		else if (attribute.equals("open")) {
			isOpen = true;
		}
		else if (attribute.equals("shut")) {
			isShut = true;
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
		isOpen = false;
		isShut = false;
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
		isOpen |= b.isOpen();
		isShut |= b.isShut();
		isMove |= b.isMove();
	}
	
	public int getPriority() {
		int prio = 0;

		if (isSink || isStop || isShut) prio += 1e3;
		if (isOpen) prio += 1e4;
		if (isPush) prio += 1e5;
		if (isWin) prio += 1e6;
		if (isMove) prio += 1e7;
		if (isYou) prio += 1e8;
		if (isDefeat) prio += 1e9;
		
		return prio;
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
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean isShut() {
		return isShut;
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
	
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	
	public void setShut(boolean isShut) {
		this.isShut = isShut;
	}
	
	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}
	
}
