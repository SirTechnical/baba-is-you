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

public class Block {

	
	int row;
	int col;
	
	
	protected String type;
	private int animationCycle;
	private int facing;
	
	BlockIcon icon;
	
	public Block(String type, int row, int col, JPanel levelPanel) {
		
		this.type = type;
		this.row = row;
		this.col = col;
		
		if (this instanceof Text) 
			this.type = "text";
		
		animationCycle = 1;
		
		
		icon = new BlockIcon(type);
		
		levelPanel.add(icon.getLabel());
		
	}
	
	public Block(String type, Block old, JPanel levelPanel) {
		this.type = type;
		this.row = old.row;
		this.col = old.col;
		this.facing = old.facing;
		this.animationCycle = old.animationCycle;
		
		icon = new BlockIcon(type);
		
		levelPanel.add(icon.getLabel());
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

			if (b.getAttributes().isStop) {
				return false;
			}

		}

		// Don't move if PUSH and not pushable
		it2 = grid[r2][c2].listIterator();
		while (it2.hasNext()) {
			Block b = it2.next();	

			if (b.getAttributes().isPush) {
				if (!b.canMove(dr, dc, grid)) return false;

			}

		}
		
		return true;				
	}
	
	public void move(int dr, int dc, ArrayList<Block>[][] grid) {
		row += dr;
		col += dc;
		
		ListIterator<Block> it = grid[row][col].listIterator();
		while (it.hasNext()) {
			Block b = it.next();	

			if (b.getAttributes().isPush) {
				b.move(dr, dc, grid);
			}

		}
		
	}
	
	public void destroy() {
		icon.getLabel().setVisible(false);
	}
	
	public void updateGraphics() {
		icon.updatePos(row, col);
	}
	
	public BlockAttributes getAttributes() {
		return Level.blockAttributes.get(type);
	}
	
	public static void loadIcons() {
		
	}
	
	
	
	
}
