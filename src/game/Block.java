// CLASS IS BLOCK

package game;

import java.io.*;
import java.util.*;
import java.util.HashMap.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Block implements Comparable<Block> {

	
	private int row;
	private int col;
	
	private String type;
	private int animationCycle;
	private int facing;
	
	private boolean hasMoved;
	private boolean isDestroyed;
	
	private BlockIcon icon;
	
	private Level parentLevel;
	
	public Block(String type, int row, int col, Level parentLevel) {
		
		this.type = type;
		this.row = row;
		this.col = col;
		this.parentLevel = parentLevel;
		
		if (this instanceof Text) 
			this.type = "text";
		
		animationCycle = 1;
		
		hasMoved = false;
		
		icon = new BlockIcon(type);
		
		parentLevel.getPanel().add(icon.getLabel());
		
	}
	
	public Block(String type, Block old, Level parentLevel) {
		this.type = type;
		this.row = old.row;
		this.col = old.col;
		this.facing = old.facing;
		this.animationCycle = old.animationCycle;
		this.parentLevel = parentLevel;
		
		if (this instanceof Text) 
			this.type = "text";
		
		hasMoved = false;
		
		icon = new BlockIcon(type);
		
		parentLevel.getPanel().add(icon.getLabel());
	}
	
	public boolean canMove(int dr, int dc, ArrayList<Block>[][] grid) {
		
		int r2 = row + dr;
		int c2 = col + dc;
		
		// DOn't move if out of bounds
		if (r2 < 0 || c2 < 0 || r2 >= grid.length || c2 >= grid[0].length) return false;

		// Don't move if STOP
		ListIterator<Block> it2 = grid[r2][c2].listIterator();
		while (it2.hasNext()) {
			Block b = it2.next();	

			if (b.getAttributes().isStop()) {
				
				// ...unless it is STOP and SHUT and this is OPEN
				if (b.getAttributes().isShut() && getAttributes().isOpen() ||
						getAttributes().isShut() && b.getAttributes().isOpen()) {
					continue;
				}
				
				return false;
			}

		}

		// Don't move if PUSH and not pushable
		it2 = grid[r2][c2].listIterator();
		while (it2.hasNext()) {
			Block b = it2.next();	

			if (b.getAttributes().isPush()) {
				if (!b.canMove(dr, dc, grid)) return false;

			}

		}
		
		return true;				
	}
	
	public void move(int dr, int dc, ArrayList<Block>[][] grid) {
		
		if (hasMoved) return;
		
		row += dr;
		col += dc;
		
		ListIterator<Block> it = grid[row][col].listIterator();
		while (it.hasNext()) {
			Block b = it.next();	

			if (b.getAttributes().isPush()) {
				b.move(dr, dc, grid);
			}

		}
		
		hasMoved = true;
		
	}
	
	public void destroy() {
		icon.getLabel().setVisible(false);
		isDestroyed = true;
	}
	
	public void updateGraphics() {
		icon.updatePos(row, col);
		icon.setIcon(type);
	}
	
	public BlockAttributes getAttributes() {
		if (isDestroyed) 
			return BlockAttributes.getDestroyedAttributes();
		return parentLevel.getBlockAttributes().get(type);
	}
	
	public static void loadIcons() {
		
	}
	
	public int compareTo(Block b) {
		return b.getAttributes().getPriority() - getAttributes().getPriority();
	}
	
	// Getter Methods
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
	
	public String getType() {
		return type;
	}
	
	public BlockIcon getIcon() {
		return icon;
	}
	
	// Setter Methods
	public void setMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
	
}
