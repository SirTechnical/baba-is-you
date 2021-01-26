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
	JPanel levelPanel;
	
	// Logic
	int rows, cols; 
	
	boolean hasYou;
	
	LinkedList<Block> blocks;
	
	ArrayList<Block>[][] grid;
	
	
	
	
	
	protected static HashMap<String, BlockAttributes> blockAttributes;
	
	// Logic Constants
	
	@SuppressWarnings("serial")
	static HashMap<String, String> BLOCK_CODES = new HashMap<String, String>() {{
		
		put("bBABA", "baba");
		put("bFLAG", "flag");
		put("bROCK", "rock");
		put("bWALL", "wall");
		
		put("tBABA", "text_baba");
		put("tFLAG", "text_flag");
		put("tROCK", "text_rock");
		put("tWALL", "text_wall");
		
		put("tIS__", "text_is");
		put("tYOU_", "text_you");
		put("tWIN_", "text_win");
		put("tPUSH", "text_push");
		put("tSTOP", "text_stop");


	}};
	
	// Constructor: Initializes a level from a file
	@SuppressWarnings("unchecked")
	public Level(String levelName) {
		
		// init
		BlockIcon.loadAssets();
		
		
		blockAttributes = new HashMap<String, BlockAttributes>();
		
		// temp: testing only
		blockAttributes.put("baba", new BlockAttributes("baba"));
		blockAttributes.put("flag", new BlockAttributes("flag"));
		blockAttributes.put("rock", new BlockAttributes("rock"));
		blockAttributes.put("wall", new BlockAttributes("wall"));
		blockAttributes.put("text", new BlockAttributes("text"));
		
		blockAttributes.get("baba").isYou = true;
		blockAttributes.get("flag").isWin = true;
		blockAttributes.get("rock").isPush = true;
		blockAttributes.get("wall").isStop = true;
		blockAttributes.get("text").isPush = true;
		
		
		// Graphics
		levelPanel = new JPanel(null); 
		levelPanel.setPreferredSize(new Dimension(400, 400));
		
		
		
		
		// Build level from file
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader("levels/level_" + levelName + ".txt"));

			rows = Integer.parseInt(fileInput.readLine());
			cols = Integer.parseInt(fileInput.readLine());
			
			grid = new ArrayList[rows][cols];
			blocks = new LinkedList<Block>();
			
			for (int i = 0; i < rows; i++) {
				StringTokenizer line = new StringTokenizer(fileInput.readLine());
				
				for (int j = 0; j < cols; j++) {
					grid[i][j] = new ArrayList<Block>();
					
					String block = line.nextToken();
					
					if (!block.equals(".....")) {
						String type = BLOCK_CODES.get(block);
						
						
						if (block.charAt(0) == 't') {
							
							blocks.add(new Text(type, i, j, levelPanel));
							
							System.out.println(i + " " + j);
							
						}
						else {
							blocks.add(new Block(type, i, j, levelPanel));

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
		
		// Launch
		updateGrid();
		parseRules();
		updateGraphics();
	}
	
	public void turn(int dr, int dc) {
		
		updateGrid();
		
		doMovement(dr, dc);
		
		updateGrid();
		
		parseRules();
		doTransforms();
		// parseRules(); <-- only necessary with WORD and TEXT texts
		doProperties();
		updateGrid(); 
		parseRules();
		
		updateGraphics();
		
		
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
			grid[b.row][b.col].add(b);
		}
		
	}
	
	public void doMovement(int dr, int dc) {
		
		// Move all things that are you
		
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();	

			if (b instanceof Text) 
				continue;

			if (b.getAttributes().isYou) {
				if (b.canMove(dr, dc, grid)) {
					b.move(dr, dc, grid);
				}
			}

		}
		
		// Move all things that are move
		
	}
	
	public void doTransforms() {
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			
			if (b.getAttributes().transform != null) {
				it.remove();
				it.add(new Block(b.getAttributes().transform, b, levelPanel));
				b.destroy();
			}
			
		}
	}
	
	public void doProperties() {
		
	}
	
	public void parseRules() {
		
		// Reset all properties 
		Collection<BlockAttributes> c = blockAttributes.values();
		for (BlockAttributes b : c) {
			b.reset();
		}
		
		// Reset all rules
		
		hasYou = false;
		
		// TEXT IS PUSH: hidden, permanent rule
		addRule("text", "is", "push");
	
		
		// Find new rules
		for (Block middleBlock : blocks) {
			if (!(middleBlock instanceof Text)) continue;
			
			Text middle = (Text) middleBlock;
			
			if (!middle.getFunction().equals("verb")) continue;
				
				
			int row = middle.row;
			int col = middle.col;

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
			if (col != 0 && col != rows-1) {

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
		
	}
	
	public void addRule(String first, String middle, String last) {
		
		if (middle.equals("is")) {
			blockAttributes.get(first).set(last);
		}	
		if (last.equals("you")) {
			hasYou = true;
		}
		
	}
	
	public void addRule(Text first, Text middle, Text last) {
		addRule(first.getWord(), middle.getWord(), last.getWord());	
	}
	
	
	// does graphics
	public void updateGraphics() {
		
		ListIterator<Block> it = blocks.listIterator();
		while (it.hasNext()) {
			Block b = it.next();
			b.updateGraphics();
		}
		
	}
	
	
	public JPanel getPanel() {
		return levelPanel;
	}
	
	
}
