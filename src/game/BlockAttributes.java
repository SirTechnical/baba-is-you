// BlockAttributes object represent all the rules affecting a specific type of Block.

package game;

public class BlockAttributes {

	// Reflects NOUN VERB ADJECTIVE rules (e.g. BABA IS YOU)
	private boolean isPush;
	private boolean isStop;
	private boolean isYou;
	private boolean isWin;
	private boolean isDefeat;
	private boolean isSink;
	private boolean isOpen;
	private boolean isShut;
	private boolean isMove;
	
	// Reflects NOUN VERB NOUN rules (e.g. BABA IS WALL)
	private String transform;
	
	// Destroyed Blocks have no attributes (dummy BlockAttributes object with all fields false)
	private static BlockAttributes destroyedAttributes = new BlockAttributes();
	
	// Constructor: Creates a blank BlockAttributes object.
	BlockAttributes() {}
	
	// Description: Sets a specified attribute.
	// Parameters: None.
	// Return: The attribute to set.
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
	
	// Description: Resets all fields of this BlockAttributes.
	// Parameters: Void.
	// Return: None.
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
	
	// Description: Performs bitwise OR on all the boolean fields of this BlockAttributes.
	// Parameters: The BlockAttributes to OR this BlockAttributes with.
	// Return: Void.
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
	
	// Description: Calculates the display priority of a type of Block based on its BlockAttributes.
	// Parameters: None.
	// Return: The display priority. (higher = displayed above, lower = displayed below).
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
	
}
