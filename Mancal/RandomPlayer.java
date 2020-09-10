/**
 * A MancalaPlayer that plays randomly
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.ArrayList;
import java.util.Random;


public class RandomPlayer extends MancalaPlayer {

	Random myRandom = new Random();
	@Override
	public Move getMove(GameState g, long deadline) {

		
		ArrayList<Move> legalMoves =g.getLegalMoves();
		Move m = legalMoves.
				get(myRandom.nextInt(legalMoves.size()));
		return m;
	}
	
	public String toString() {
		return "Random";
	}

}
