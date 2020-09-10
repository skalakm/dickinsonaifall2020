/**
 * A MancalaPlayer that looks one move ahead
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.ArrayList;

public class OneMovePlayer extends MancalaPlayer{

	@Override
	public Move getMove(GameState g, long deadline) {
		ArrayList<Move> validMoves = g.getLegalMoves();
		Move bestMove = null;
		double bestScore=-100000000;
		for(Move m :validMoves) {
			GameState next = g.makeMove(m);
			double score= staticEvaluator(next);
			if(!g.isBottomTurn) {
				score=-score;
			}
			if(score > bestScore) {
				bestScore = score;
				bestMove = m;
			}
			//System.out.println("score for " +m + " " + score);
		}
		
		return bestMove;
	}

	public double staticEvaluator(GameState state) {
		return state.currentScore();
	}

	public String toString() {
		return "OneMove";
	}
}
