
/**
 * A mancala player allowing input from standard in
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */
import java.util.ArrayList;
import java.util.Scanner;

public class HumanMancalaPlayer extends MancalaPlayer {

	Scanner sc;

	public HumanMancalaPlayer() {
		sc = new Scanner(System.in);
	}

	@Override
	public Move getMove(GameState g, long deadline) {
		g.printBoard();
		System.out.println("The legal moves are " + g.getLegalMoves());

		ArrayList<Move> legalMoves = g.getLegalMoves();
		Move m = new Move(-1);
		while (!legalMoves.contains(m)) {
			try {
				m = new Move(sc.nextInt());
			} catch (Exception e) {
				sc.next();
				e.printStackTrace();
			}
		}
		return m;
	}

}
