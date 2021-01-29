// Block objects represent one item in a Level. 
// (e.g. BABA, FLAG, WALL)

// CLASS IS BLOCK

package game;

import java.util.*;

public class Block implements Comparable<Block> {

	// Logic Variables
	private int row;
	private int col;
	private String type;
	
	private boolean hasMoved;
	private boolean isDestroyed;
	
	// Graphics Variables
	private BlockIcon icon;
	private Level parentLevel;
	
	// Animation Variables
	// private int animationCycle;  not enough time to implement this feature :(
	
	private double posX;
	private double posY;
	private double dx;
	private double dy;
	
	private int framesLeft;
	
	// Constructor: Creates a specific type of Block object at a specified position (row, col) in the active Level.
	public Block(String type, int row, int col, Level parentLevel) {
		
		// Initialize instance variables
		this.type = type;
		this.row = row;
		this.col = col;
		this.parentLevel = parentLevel;
		posX = col * Styles.BLOCK_SIZE;
		posY = row * Styles.BLOCK_SIZE;
		hasMoved = false;
		
		// All Text objects have type "text"
		if (this instanceof Text) this.type = "text";
		
		// Initialize graphics
		icon = new BlockIcon(type);
		icon.updatePos(posX, posY);
		parentLevel.getPanel().add(icon.getLabel());
	}
	
	// Constructor: Creates a specific type of Block object as a copy of another Block object in the active Level.
	public Block(String type, Block old, Level parentLevel) {
		
		// Initialize instance variables
		this.type = type;
		this.row = old.row;
		this.col = old.col;
		this.parentLevel = parentLevel;
		posX = col * Styles.BLOCK_SIZE;
		posY = row * Styles.BLOCK_SIZE;
		hasMoved = false;
		
		// All Text objects have type "text"
		if (this instanceof Text) this.type = "text";
		
		// Initialize graphics
		icon = new BlockIcon(type);
		icon.updatePos(posX, posY);
		parentLevel.getPanel().add(icon.getLabel());
	}
	
	// Description: Checks if this Block can move in a specified direction, given the current state of the Level grid.
	// Parameters: The direction this Block is moving (delta-rows, delta-columns) and the current state of the Level grid.
	// Return: Whether this Block can move in the specified direction.
	public boolean canMove(int dr, int dc, ArrayList<Block>[][] grid) {
		
		if (dr == 0 && dc == 0) return true;	// The Block can always stay still
		
		int r2 = row + dr;
		int c2 = col + dc;
		
		// Cannot move if out of bounds
		if (r2 < 0 || c2 < 0 || r2 >= grid.length || c2 >= grid[0].length) return false;

		// Cannot move if colliding with a STOP Block
		ListIterator<Block> it2 = grid[r2][c2].listIterator();
		while (it2.hasNext()) {
			Block b = it2.next();	
			if (b.getAttributes().isStop()) {
				// ...unless the Block is STOP and SHUT and this Block is OPEN
				if (b.getAttributes().isShut() && getAttributes().isOpen() ||
						getAttributes().isShut() && b.getAttributes().isOpen()) {
					continue;
				}
				return false;
			}
		}

		// Cannot move if colliding with an immovable PUSH Block
		it2 = grid[r2][c2].listIterator();
		while (it2.hasNext()) {
			Block b = it2.next();	
			// Recursively check if the PUSH Block can move
			if (b.getAttributes().isPush()) {
				if (!b.canMove(dr, dc, grid)) return false;
			}
		}
		
		// If all the above checks are passed, then this Block can move in this direction.
		return true;				
	}
	
	// Description: Logically (not graphically) moves this Block.
	// Parameters: The direction to move this Block in (delta-rows, delta-columns) and the current state of the Level grid.
	// Return: Void.
	public void move(int dr, int dc, ArrayList<Block>[][] grid) {
		
		if (dr == 0 && dc == 0) return;	// Do not move if this Block is not moving
		if (hasMoved) return;			// Do not move if this Block already moved this turn
		
		row += dr;
		col += dc;
		
		ListIterator<Block> it = grid[row][col].listIterator();
		while (it.hasNext()) {
			Block b = it.next();	
			
			// Recursively push all Block objects in the cell in front of this Block
			if (b.getAttributes().isPush()) {
				b.move(dr, dc, grid);
			}
		}
		hasMoved = true;
		
		if (type.equals("baba")) {
			if (dr == -1 && dc == 0) {
				icon.setIcon("babaUp");
			}
			else if (dr == 1 && dc == 0) {
				icon.setIcon("babaDown");
			}
			else if (dr == 0 && dc == -1) {
				icon.setIcon("babaLeft");
			}
			else if (dr == 0 && dc == 1) {
				icon.setIcon("babaRight");
			}
		}
		else if (type.equals("keke")) {
			if (dr == -1 && dc == 0) {
				icon.setIcon("kekeUp");
			}
			else if (dr == 1 && dc == 0) {
				icon.setIcon("kekeDown");
			}
			else if (dr == 0 && dc == -1) {
				icon.setIcon("kekeLeft");
			}
			else if (dr == 0 && dc == 1) {
				icon.setIcon("kekeRight");
			}
		}
	}
	
	// Description: Visually moves this Block by beginning its animation cycle.
	// Parameters: Whether the animation should be skipped (completed instantly).
	// Return: Void.
	public void startAnimation(boolean skipAnimation) {
		if (skipAnimation) {
			// Instantly set current position to reflect destination position
			posX = col * Styles.BLOCK_SIZE;
			posY = row * Styles.BLOCK_SIZE;
			icon.updatePos(posX, posY);
		}
		else {
			// Calculate animation movement
			int destX = col * Styles.BLOCK_SIZE;
			int destY = row * Styles.BLOCK_SIZE;
	
			dx = (destX - posX) / Styles.ANIMATION_FRAMES;
			dy = (destY - posY) / Styles.ANIMATION_FRAMES;
	
			framesLeft = Styles.ANIMATION_FRAMES;
		}
	}
	
	// Description: Smoothly animates this Block by changing the location of its JLabel.
	//				This method is called on a timer in the Main class.
	//				The Block advances by one animation frame each time this method is called.
	// Parameters: None.
	// Return: Void.
	public void animate() {
		if (framesLeft == 0) return;
		
		// Update position by one frame
		posX += dx;
		posY += dy;
		framesLeft--;
		
		icon.updatePos(posX, posY);
	}
	
	// Description: Destroys this Block by hiding its JLabel and flagging it as destroyed.
	// Parameters: None.
	// Return: Void.
	public void destroy() {
		icon.getLabel().setVisible(false);
		isDestroyed = true;
	}

	// Description: Gets the BlockAttributes of this Block's type.
	// Parameters: None.
	// Return: The BlockAttributes of this Block's type. (reflects the rules currently affecting this Block)
	public BlockAttributes getAttributes() {
		if (isDestroyed) 
			return BlockAttributes.getDestroyedAttributes();
		return parentLevel.getBlockAttributes().get(type);
	}

	// Description: Compares two Blocks by their display priority. (implements Comparable interface)
	// Parameters: The Block to be compared against.
	// Return: A positive integer if this Block has a higher priority than the compared Block. (this Block should be displayed on top)
	//		   A negative if this Block has a lower priority than the compared Block. (the compared Block should be displayed on top)
	//		   0 if the two Block have the same priority.
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
