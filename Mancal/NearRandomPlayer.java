/**
 * A MancalaPlayer that plays random games to determine the quality of a move
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.ArrayList;
import java.util.Random;

public class NearRandomPlayer extends MancalaPlayer implements MiniMax{


	Random myRandom = new Random();
	
	@Override
	public Move getMove(GameState g, long deadline) {
		ArrayList<Move> validMoves = g.getLegalMoves();
		Move bestMove = null;
		double bestScore=-100000000;
		ArrayList<Double> scoreForValidMoves = new ArrayList<>();
		for(int i = 0; i < validMoves.size(); ++i) {
			scoreForValidMoves.add(0.0);
		}
		while( deadline-System.currentTimeMillis() > 500) {
			for(int i = 0; i < validMoves.size(); ++i) {
				Move m = validMoves.get(i);
				GameState next = g.makeMove(m);
				double score= scoreForValidMoves.get(i) +staticEvaluator(next);
				scoreForValidMoves.set(i, score);
				if(!g.isBottomTurn) {
					score=-score;
				}
				if(score > bestScore) {
					bestScore = score;
					bestMove = m;
				}
				//System.out.println("score for " +m + " " + score);
			}
		}
		
		
		return bestMove;
	}

	@Override
	public double staticEvaluator(GameState state) {
		double totalScore = 0;
		for(int i = 0; i < 100; ++i) {

			//System.out.println("random game " + i);
			GameState cur = state;
			while(!cur.isGameOver()) {
				ArrayList<Move> legalMoves =cur.getLegalMoves();
				Move m = legalMoves.
						get(myRandom.nextInt(legalMoves.size()));
				cur =cur.makeMove(m);
			}
			totalScore += cur.getFinalNetScore();
		}
		return totalScore;
	}

	@Override
	public int getNodesGenerated() {
		return 0;
	}

	@Override
	public int getStaticEvaluations() {
		return 0;
	}

	@Override
	public double getAveBranchingFactor() {
		return 0;
	}

	@Override
	public double getEffectiveBranchingFactor() {
		return 0;
	}
	
	public String toString() {
		return "NearRandom";
	}

}
