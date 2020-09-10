/**
 * Parent class of all Mancala players.
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

public abstract class MancalaPlayer {

	boolean verbose;
	String name;
	/**
	 * 
	 * @g the GameState to make a move
	 * @deadline the system time when it must return by
	 * @return the move the player wants to make
	 */
	public abstract Move getMove(GameState g, long deadline);
	
	public void setVerbosity(boolean verbose) {
        this.verbose = verbose;
    }

 
    /**
     * Returns the name of this player.
     */
    public String getName() {
        return name;
    }


  


    /**
     * Returns a string representation of this player.
     */
    public String toString() {
        return name;
    }
	
}
