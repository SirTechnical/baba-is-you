// Level objects handle the logic within one Level of the game.

// CLASS IS LEVEL
// LEVEL IS PLAY
// LEVELLOGIC IS PAINFUL :(

package game;

import java.io.*;
import java.util.*;
import javax.swing.*;

public class Level {

	private int number;
	private JPanel levelPanel;
	
	// Logic
	private int rows, cols; 
	private boolean hasYou;
	private boolean isWin;
	
	private LinkedList<Block> blocks;
	private ArrayList<Block>[][] grid;
	private HashMap<String, BlockAttributes> blockAttributes;
	
	// Undo Functionality
	private Level shadow;
	private boolean isShadow;
	private LinkedList<Pair> moveHistory; 	// Implements a queue
	private static final int MAX_UNDO_MOVES = 100;
	
	// For sounds
	private int activeRuleCount;
	
	// A dictionary used to decode Level files
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
		put("bGRAS", "grass");
		put("bGRA2", "grass2");
		put("bTILE", "tile");
		
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
		put("tGRAS", "text_grass");
		put("tTILE", "text_tile");
	}};

	// Constructor: Loads a Level specified by a number.
	//				isShadow represents whether this Level is a shadow copy or not.
	//				Shadow copies exist to support undo functionality: Each active Level has a shadow copy that
	//				"trails" up to MAX_UNDO_MOVES moves behind the active Level.
	//				When the player undoes a move, the shadow copy is restored and all the stored moves are instantly replayed.
	@SuppressWarnings("unchecked")
	public Level(int number, boolean isShadow) {
		
		this.number = number;
		this.isShadow = isShadow;
		
		// Initialize fields
		blockAttributes = new HashMap<String, BlockAttributes>();
		levelPanel = new JPanel(null);
		moveHistory = new LinkedList<Pair>();
		blocks = new LinkedList<Block>();
		
		// Build Level from file
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader("levels/level_" + number + ".txt"));

			rows = Integer.parseInt(fileInput.readLine());
			cols = Integer.parseInt(fileInput.readLine());
			
			grid = new ArrayList[rows][cols];
			
			for (int i = 0; i < rows; i++) {
				StringTokenizer line = new StringTokenizer(fileInput.readLine());
				
				for (int j = 0; j < cols; j++) {
					grid[i][j] = new ArrayList<Block>();
					
					String block = line.nextToken();
					
					// Create new Block objects based on the codes in the Level file
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
			System.out.println("Levels/level_" + number + " not found!");
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
			shadow = new Level(number, true);
		}
		
		// Launch
		updateGrid();
		parseRules();
		updateGraphics(true);
	}
	
	// Constructor overload: By default, new Levels are not shadow copies.
	public Level(int number) {
		this(number, false);
	}

	// Description: Copies all the fields of another Level into this Level.
	// Parameters: The Level to copy from.
	// Return: Void.
	@SuppressWarnings("unchecked")
	public void copy(Level copyLevel) {
		
		number = copyLevel.number;
		rows = copyLevel.rows;
		cols = copyLevel.cols;
		
		// This level is never a shadow
		isShadow = false;
		
		// Create copies of all the Blocks in the copyLevel
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
		blockAttributes = new HashMap<String, BlockAttributes>();
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
	
	// Description: Executes a turn.
	// Parameters: A pair representing the direction to move in (delta-rows, delta-columns), whether this turn is being made by a shadow copy or not.
	// Return: Void.
	public void turn(Pair direction, boolean shadowTurn) {
		
		if (!hasYou) return;	// Cannot make turns if there is no YOU object in the Level.
		
		// Undo functionality: Stores the player move history in a queue.
		if (!shadowTurn) {
			moveHistory.addLast(direction);
			
			if (moveHistory.size() > MAX_UNDO_MOVES) {
				
				// Advance the shadow copy by one turn
				shadow.turn(moveHistory.getFirst());
				moveHistory.removeFirst();
			}
		}
		
		// Execute turn logic
		updateGrid();
		doMovement(direction);
		
		updateGrid();
		parseRules();
		doTransforms();
		// parseRules(); <-- this step is only necessary with WORD and TEXT text objects
		
		updateGrid();
		doProperties();
		
		updateGrid(); 
		parseRules();
		
		if (!shadowTurn) {
			updateGrid();
			updateGraphics(false);
		}
		
		// Reset turn movement flags
		for (Block b : blocks) {
			b.setMoved(false);
		}
		
	}
	
	// Overload: Calls turn depending on whether this Level is a shadow copy or not.
	public void turn(Pair direction) {
		turn(direction, isShadow);
	}
	
	// Description: Syncs the Level grid with the current state of the Level.
	// Parameters: None.
	// Return: Void.
	public void updateGrid() {
		
		// Reset grid
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j].clear();
			}
		}
		
		// Add in each Block in the Level based on its position
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
	
	// Description: Does movement for all moving Blocks. 
	// Parameters: The direction YOU objects move in (delta-rows, delta-columns).
	// Return: Void.
	public void doMovement(int dr, int dc) {
		
		// Move all Blocks that are YOU
		
		// Only if YOU actually move
		if (!(dr == 0 && dc == 0)) {
			ListIterator<Block> it = blocks.listIterator();
			while (it.hasNext()) {
				Block b = it.next();	
	
				// Text can never be YOU
				if (b instanceof Text) continue;
	
				if (b.getAttributes().isYou()) {
					if (b.canMove(dr, dc, grid)) {
						b.move(dr, dc, grid);
					}
				}
			}
		}
		
		// Move all things that are move
		// -- coming soon! -- //
		
	}
	
	// Overload: Does movement with a Pair object representing the movement direction.
	public void doMovement(Pair direction) {
		doMovement(direction.getFirst(), direction.getSecond());
	}
	
	// Description: Transforms all transforming Blocks. 
	//				(Blocks affected by NOUN VERB NOUN rules, e.g. BABA IS WALL)
	// Parameters: None.
	// Return: Void.
	public void doTransforms() {
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			
			// If this block is transforming, transform it.
			if (b.getAttributes().getTransform() != null) {
				it.add(new Block(b.getAttributes().getTransform(), b, this));
				b.destroy();
			}
		}
	}
	
	// Description: Updates logic for each Block in the Level. 
	//				(OPEN + SHUT, SINK, YOU + DEFEAT, YOU + WIN interactions)
	// Parameters: None.
	// Return: Void.
	public void doProperties() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
		
				// Calculate all the attributes present on this grid cell
				BlockAttributes cellAttributes = calculateCellAttributes(grid[i][j]);
				
				for (Block b : grid[i][j]) {
					
					// OPEN + SHUT
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
					
					// ANYTHING + SINK
					if (!b.getAttributes().isSink() && cellAttributes.isSink()) {
						// Destroy all blocks in this tile
						for (Block c : grid[i][j]) {
							c.destroy();
						}
					}
					
					// YOU + DEFEAT
					if (b.getAttributes().isYou() && cellAttributes.isDefeat()) {
						b.destroy();
					}
					
					// YOU + WIN
					if (b.getAttributes().isYou() && cellAttributes.isWin()) {
						isWin = true;
					}
				}
			}
		}
	}
	
	// Description: Calculates the attributes of a specific cell in the Level grid. 
	// Parameters: The cell in the Level grid to calculate for.
	// Return: The BlockAttributes for the desired cell.
	public BlockAttributes calculateCellAttributes(ArrayList<Block> cell) {
		
		BlockAttributes cellAttributes = new BlockAttributes("dummy");
		for (Block b : cell) {
			if (b.getAttributes() == null) {
				blockAttributes.put(b.getType(), new BlockAttributes(b.getType()));
			}
			cellAttributes.or(b.getAttributes());
		}
		return cellAttributes;
	}
	
	// Description: Parses all the rules in this Level based on the position of Text objects. 
	// Parameters: None.
	// Return: Void.
	public void parseRules() {
		
		activeRuleCount = 0;
		
		// Reset all properties 
		Collection<BlockAttributes> c = blockAttributes.values();
		for (BlockAttributes b : c) {
			b.reset();
		}
		
		// Deactivate all Text
		for (Block b : blocks) {
			if (b instanceof Text) {
				Text t = (Text) b;
				t.setActive(false);
			}
		}
		
		// TEXT IS PUSH: Hidden and permanent rule
		addRule("text", "is", "push");
		
		// Detect rules from the middle block (VERB)
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
					
					// First word of rule must be a NOUN
					if (!first.getFunction().equals("noun")) continue;

					// lastBlock = the Block below middleBlock
					for (Block lastBlock : grid[row+1][col]) {
						if (!(lastBlock instanceof Text)) continue;
						Text last = (Text) lastBlock;

						// Last word of rule must be a NOUN or an ADJECTIVE
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

					// First word of rule must be a NOUN
					if (!first.getFunction().equals("noun")) continue;

					// lastBlock = the Block right of middleBlock
					for (Block lastBlock : grid[row][col+1]) {
						if (!(lastBlock instanceof Text)) continue;
						Text last = (Text) lastBlock;

						// Last word of rule must be a NOUN or an ADJECTIVE
						if (last.getFunction().equals("verb")) continue;

						addRule(first, middle, last);
					}
				}
			}
		}
		
		// Check if YOU is present in the level
		hasYou = false;
		for (Block b : blocks) {
			if (b.getAttributes().isYou()) {
				hasYou = true;
				break;
			}
		}
	}
	
	// Description: Adds an active rule to this Level. 
	// Parameters: Strings representing this rule.
	// Return: Void.
	public void addRule(String first, String middle, String last) {	
		if (blockAttributes.get(first) == null) return;
		
		if (middle.equals("is")) {
			blockAttributes.get(first).set(last);
		}
	}
	
	// Description: (Overload) Adds an active rule to this Level. 
	// Parameters: The Texts that compose this rule.
	// Return: Void.
	public void addRule(Text first, Text middle, Text last) {
		activeRuleCount++;
		first.setActive(true);
		middle.setActive(true);
		last.setActive(true);
		addRule(first.getWord(), middle.getWord(), last.getWord());	
	}
	
	// Description: Undoes the last move.
	// Parameters: None.
	// Return: Void.
	public void undo() {
		if (moveHistory.size() == 0) return; // Nothing to undo
		
		// Restore the shadow copy of this Level
		copy(shadow);
		
		// Repeat all the moves except the last one
		moveHistory.removeLast();
		for (Pair move : moveHistory) {
			turn(move, true);
		}
		
		updateGrid();
		updateGraphics(true);
	}
	
	// Description: Updates the graphics of this Level. (called once per turn)
	// Parameters: Whether the graphics should be updated instantly or not.
	// Return: Void.
	public void updateGraphics(boolean skipAnimation) {
			
		// Establish Z-order of Blocks based on their display priority
		Collections.sort(blocks);
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			levelPanel.setComponentZOrder(b.getIcon().getLabel(), it.nextIndex()-1);
		}
		
		// Animate each Blocks as necessary
		for (Block b : blocks) {
			b.startAnimation(skipAnimation);
		}
		levelPanel.updateUI();
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
	
	public int getNumber() {
		return number;
	}
	
	public int getActiveRuleCount() {
		return activeRuleCount;
	}
	
	// Setter Methods
	
}
