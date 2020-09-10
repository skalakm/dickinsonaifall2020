// A simple class to hold a move
public class Move {

	
	int location;
	
	public Move(int loc) {
		location = loc;
	}
	
	public String toString() {
		return ""+location;
	}
	
	public boolean equals(Object other) {
		return location == ((Move)other).location;
	}
}
