package game;

import java.io.*;
import java.util.*;
import java.util.HashMap.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Level {
	
	// Graphics
	private JPanel levelPanel;
	
	private String levelName;
	
	// Logic
	private int rows, cols; 
	
	private boolean hasYou;
	private boolean isWin;
	
	private LinkedList<Block> blocks;
	
	private ArrayList<Block>[][] grid;
	
	// Undo Functionality
	
	private Level shadow;
	private boolean isShadow;
	
	private LinkedList<Pair> moveHistory; // Queue
	
	private static final int MAX_UNDO_MOVES = 100;
	
	
	
	private HashMap<String, BlockAttributes> blockAttributes;
	
	// Logic Constants
	
	@SuppressWarnings("serial")
	static HashMap<String, String> BLOCK_CODES = new HashMap<String, String>() {{
		
		put("bBABA", "baba");
		put("bFLAG", "flag");
		put("bROCK", "rock");
		put("bWALL", "wall");
		put("bSKUL", "skull");
		put("bWATR", "water");
		put("bBOX_", "box");
		put("bKEY_", "key");
		put("bDOOR", "door");
		put("bKEKE", "keke");
		
		put("tBABA", "text_baba");
		put("tFLAG", "text_flag");
		put("tROCK", "text_rock");
		put("tWALL", "text_wall");
		put("tSKUL", "text_skull");
		put("tWATR", "text_water");
		put("tBOX_", "text_box");
		put("tKEY_", "text_key");
		put("tDOOR", "text_door");
		put("tKEKE", "text_keke");
		
		put("tIS__", "text_is");
		
		put("tYOU_", "text_you");
		put("tWIN_", "text_win");
		put("tPUSH", "text_push");
		put("tSTOP", "text_stop");
		put("tDEFT", "text_defeat");
		put("tSINK", "text_sink");
		put("tOPEN", "text_open");
		put("tSHUT", "text_shut");
		put("tMOVE", "text_move");

	}};
	
	// Constructor: Initializes a level from a file
	public Level(String levelName) {
		this(levelName, false);
	}

	// Overload
	@SuppressWarnings("unchecked")
	public Level(String levelName, boolean isShadow) {
		
		this.levelName = levelName;
		this.isShadow = isShadow;
		
		// Initializing Fields
		blockAttributes = new HashMap<String, BlockAttributes>();
		levelPanel = new JPanel(null);
		moveHistory = new LinkedList<Pair>();
		blocks = new LinkedList<Block>();
		
		// Build level from file
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader("levels/level_" + levelName + ".txt"));

			rows = Integer.parseInt(fileInput.readLine());
			cols = Integer.parseInt(fileInput.readLine());
			
			grid = new ArrayList[rows][cols];
			
			for (int i = 0; i < rows; i++) {
				StringTokenizer line = new StringTokenizer(fileInput.readLine());
				
				for (int j = 0; j < cols; j++) {
					grid[i][j] = new ArrayList<Block>();
					
					String block = line.nextToken();
					
					if (!block.equals(".....")) {
						String type = BLOCK_CODES.get(block);
						
						if (block.charAt(0) == 't') {
							blocks.add(new Text(type, i, j, this));
						}
						else {
							blocks.add(new Block(type, i, j, this));
						}
						
					}
		
				}
			}
			
			fileInput.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Levels/level_" + levelName + " not found!");
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// Initialize blockAttributes
		for (Block b : blocks) {
			if (!blockAttributes.containsKey(b.getType())) {
				blockAttributes.put(b.getType(), new BlockAttributes(b.getType()));
			}
		}

		// Graphics
		levelPanel.setBackground(Styles.LEVEL_BG_COLOUR);
		levelPanel.setBounds(0, 0, cols * Styles.BLOCK_SIZE, rows * Styles.BLOCK_SIZE);
		
		// Undo Functionality
		if (!isShadow) {
			shadow = new Level(levelName, true);
		}
		
		// Launch
		updateGrid();
		parseRules();
		updateGraphics(true);
	}

	// Copies another level into this level
		
	@SuppressWarnings("unchecked")
	public void copy(Level copyLevel) {
		
		levelName = copyLevel.levelName;
		// this level is never a shadow
		isShadow = false;
		
		rows = copyLevel.rows;
		cols = copyLevel.cols;
		
		blockAttributes = new HashMap<String, BlockAttributes>();
		levelPanel.removeAll();
		
		blocks.clear();
		
		for (Block b : copyLevel.blocks) {
			if (b instanceof Text) {
				Text t = (Text) b;
				blocks.add(new Text(t.getTextType(), t, this));
			}
			else {
				blocks.add(new Block(b.getType(), b, this));
			}
		}
		
		// Initialize grid
		grid = new ArrayList[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new ArrayList<Block>();
			}
		}

		// Initialize blockAttributes
		for (Block b : blocks) {
			if (!blockAttributes.containsKey(b.getType())) {
				blockAttributes.put(b.getType(), new BlockAttributes(b.getType()));
			}
		}

		// Graphics
		levelPanel.setBackground(Styles.LEVEL_BG_COLOUR);
		levelPanel.setBounds(0, 0, cols * Styles.BLOCK_SIZE, rows * Styles.BLOCK_SIZE);
		
		updateGrid();
		parseRules();
	}
	
	
	
	// Executes a turn
	public void turn(Pair direction) {
		turn(direction, isShadow);
	}
	
	public void turn(Pair direction, boolean shadowTurn) {
		
		if (!hasYou) return;
		
		// Undo Functionality
		if (!shadowTurn) {
			moveHistory.addLast(direction);
			
			if (moveHistory.size() > MAX_UNDO_MOVES) {
				// Advance the shadow copy by one turn
				shadow.turn(moveHistory.getFirst());
				moveHistory.removeFirst();
			}
		}
		
		// Execute Logic
		updateGrid();
		doMovement(direction);
		
		updateGrid();
		parseRules();
		doTransforms();
		
		// parseRules(); <-- only necessary with WORD and TEXT texts
		
		updateGrid();
		doProperties();
		
		updateGrid(); 
		parseRules();
		
		if (!shadowTurn) {
			updateGrid();
			updateGraphics(false);
		}
		
		for (Block b : blocks) {
			b.setMoved(false);
		}
	}
	
	
	
	public void updateGrid() {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j].clear();
			}
		}
		
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();	
			
			if (b.isDestroyed()) {
				it.remove();
				continue;
			}
			
			grid[b.getRow()][b.getCol()].add(b);
		}
		
	}
	
	public void doMovement(int dr, int dc) {
		
		// Move all blocks that are YOU
		
		// only if YOU move
		if (!(dr == 0 && dc == 0)) {
			ListIterator<Block> it = blocks.listIterator();
			while (it.hasNext()) {
				Block b = it.next();	
	
				if (b instanceof Text) 
					continue;
	
				if (b.getAttributes().isYou()) {
					if (b.canMove(dr, dc, grid)) {
						b.move(dr, dc, grid);
					}
				}
			}
		}
		
		// Move all things that are move
	}
	
	public void doMovement(Pair direction) {
		doMovement(direction.getFirst(), direction.getSecond());
	}
	
	public void doTransforms() {
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			
			if (b.getAttributes().getTransform() != null) {
				it.add(new Block(b.getAttributes().getTransform(), b, this));
				b.destroy();
			}
		}
	}
	
	public void doProperties() {
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				BlockAttributes cellAttributes = calculateCellAttributes(grid[i][j]);
				
				for (Block b : grid[i][j]) {
					
					if (b.getAttributes().isOpen() && cellAttributes.isShut()) {
						b.destroy();
						// Destroy one OPEN and one SHUT object
						for (Block c : grid[i][j]) {
							if (c.getAttributes().isShut()) {
								c.destroy();
								break;
							}
						}
						cellAttributes = calculateCellAttributes(grid[i][j]);
					}
					
					if (!b.getAttributes().isSink() && cellAttributes.isSink()) {
						// Destroy all blocks in this tile
						for (Block c : grid[i][j]) {
							c.destroy();
						}
					}
					
					if (b.getAttributes().isYou() && cellAttributes.isDefeat()) {
						b.destroy();
					}
					
					if (b.getAttributes().isYou() && cellAttributes.isWin()) {
						// win
						isWin = true;
					}
					
				}
			}
		}
		
	}
	
	public BlockAttributes calculateCellAttributes(ArrayList<Block> cell) {
		BlockAttributes cellAttributes = new BlockAttributes("dummy");
		for (Block b : cell) {
			cellAttributes.or(b.getAttributes());
		}
		return cellAttributes;
	}
	
	public void parseRules() {
		
		// Reset all properties 
		Collection<BlockAttributes> c = blockAttributes.values();
		for (BlockAttributes b : c) {
			b.reset();
		}
		
		// Deactivate all text
		for (Block b : blocks) {
			if (b instanceof Text) {
				Text t = (Text) b;
				t.setActive(false);
			}
		}
		
		
		// TEXT IS PUSH: hidden, permanent rule
		addRule("text", "is", "push");
	
		
		// Find new rules
		for (Block middleBlock : blocks) {
			if (!(middleBlock instanceof Text)) continue;
			
			Text middle = (Text) middleBlock;
			
			if (!middle.getFunction().equals("verb")) continue;
				
			int row = middle.getRow();
			int col = middle.getCol();

			// Check vertically
			if (row != 0 && row != rows-1) {
				
				// firstBlock = the Block above middleBlock
				for (Block firstBlock : grid[row-1][col]) {
					if (!(firstBlock instanceof Text)) continue;
					Text first = (Text) firstBlock;
					
					// First word of Rule must be a noun
					if (!first.getFunction().equals("noun")) continue;

					// lastBlock = the Block below middleBlock
					for (Block lastBlock : grid[row+1][col]) {
						if (!(lastBlock instanceof Text)) continue;
						Text last = (Text) lastBlock;

						// Last word of Rule must be a noun or an adjective
						if (last.getFunction().equals("verb")) continue;
						
						addRule(first, middle, last);
					}
				}
			}

			// Check horizontally
			if (col != 0 && col != cols-1) {

				// firstBlock = the Block left of middleBlock
				for (Block firstBlock : grid[row][col-1]) {
					if (!(firstBlock instanceof Text)) continue;
					Text first = (Text) firstBlock;

					// First word of Rule must be a noun
					if (!first.getFunction().equals("noun")) continue;

					// lastBlock = the Block right of middleBlock
					for (Block lastBlock : grid[row][col+1]) {
						if (!(lastBlock instanceof Text)) continue;
						Text last = (Text) lastBlock;

						// Last word of Rule must be a noun or an adjective
						if (last.getFunction().equals("verb")) continue;

						addRule(first, middle, last);
					}
				}
			}
		}
		
		// Done parsing rules.
		
		
		// Check for YOU
		hasYou = false;
		for (Block b : blocks) {
			if (b.getAttributes().isYou()) {
				hasYou = true;
				break;
			}
		}
	}
	
	public void addRule(String first, String middle, String last) {
		
		if (blockAttributes.get(first) == null) return;
		
		if (middle.equals("is")) {
			blockAttributes.get(first).set(last);
		}
	}
	
	public void addRule(Text first, Text middle, Text last) {
		first.setActive(true);
		middle.setActive(true);
		last.setActive(true);
		addRule(first.getWord(), middle.getWord(), last.getWord());	
	}
	
	
	// does graphics - sorts Z order
	public void updateGraphics(boolean skipAnimation) {
		
		Collections.sort(blocks);
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			levelPanel.setComponentZOrder(b.getIcon().getLabel(), it.nextIndex()-1);
		}
		
		for (Block b : blocks) {
			b.startAnimation(skipAnimation);
		}
		
		levelPanel.updateUI();
		
	}
	
	// DEBUG ONLY
	/*
	public Pair getBabaLocation() {
		for (Block b : blocks) {
			if (b.getType().equals("baba")) {
				return new Pair(b.getRow(), b.getCol());
			}
		}
		return new Pair(-1, -1);
	}*/
	
	// Undoes one move
	public void undo() {
		if (moveHistory.size() == 0) return; // Nothing to undo
		
		// clone
		copy(shadow);
		
		moveHistory.removeLast();
		
		// Repeat moves
		for (Pair move : moveHistory) {
			turn(move, true);
		}
		
		// turn(Pair.origin, true);
		
		updateGrid();
		updateGraphics(true);
	}
	
	// Getter Methods
	public JPanel getPanel() {
		return levelPanel;
	}
	
	public boolean hasYou() {
		return hasYou;
	}
	
	public boolean isWin() {
		return isWin;
	}
	
	public HashMap<String, BlockAttributes> getBlockAttributes() {
		return blockAttributes;
	}
	
	public LinkedList<Block> getBlocks() {
		return blocks;
	}
	
	// Setter Methods
	
}
