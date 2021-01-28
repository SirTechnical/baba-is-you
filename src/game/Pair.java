// Pair objects represent a pair of integers.

package game;

public class Pair {

	private int first;
	private int second;
	
	// Constructor
	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}
	
	// Getter Methods
	public int getFirst() {
		return first;
	}
	
	public int getSecond() {
		return second;
	}
	
	// Setter Methods
	public void setFirst(int first) {
		this.first = first;
	}
	
	public void setSecond(int second) {
		this.second = second;
	}
	
}
