/**
 * Contains the logic for how a Mancala game works
 * Do not alter this file
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.ArrayList;

public class GameState {

	

	int[] board;
	
	// index of top score bucket
	int topScore;
	
	//index of bottom score bucket
	int bottomScore;

	//total stones in game
	long totalPieces;
	
	// is it currently the bottom player's turn
	boolean isBottomTurn;
	
	

	// create a default game
	public GameState() {

		board = new int[14];
		for (int i = 0; i < 6; ++i) {
			board[i] = 4;
			board[i + 7] = 4;
		}
		bottomScore = 6;
		topScore = 13;
		totalPieces = 48;
		isBottomTurn = true;
	}
	
	public boolean getTurn() {
		return isBottomTurn;
	}
	
	public int getCount(int loc) {
		return board[loc];
	}

	/**
	 * Creates a starting game 
	 *
	 * @boardSize the number of holes on one side of the board
	 * @startingPieces the number of starting pieces per hole
	 */
	public GameState(int boardSize, int startingPieces) {
		board = new int[boardSize * 2];
		for (int i = 0; i < boardSize - 1; ++i) {
			board[i] = startingPieces;
			board[i + boardSize] = startingPieces;
		}
		bottomScore = boardSize - 1;
		topScore = boardSize * 2 - 1;

		totalPieces = (boardSize - 1) * startingPieces * 2;
		isBottomTurn = true;
	}

	//duplicate a GameState
	private GameState(GameState old) {
		board = new int[old.board.length];
		for (int i = 0; i < board.length; ++i) {
			board[i] = old.board[i];
			isBottomTurn = old.isBottomTurn;
		}
		bottomScore = old.bottomScore;
		topScore = old.topScore;
		totalPieces = old.totalPieces;

	}

	// print a human readable game board
	public void printBoard() {
		long sum = 0;
		System.out.print(" ");
		for (int i = topScore - 1; i >= bottomScore + 1; --i) {
			if (board[i] < 10) {
				System.out.print(" ");
			}
			System.out.print(board[i] + " ");

		}

		System.out.println();
		System.out.println(board[topScore] + "                  " + board[bottomScore]);
		System.out.print(" ");
		for (int i = 0; i <= bottomScore - 1; ++i) {
			if (board[i] < 10) {
				System.out.print(" ");
			}
			System.out.print(board[i] + " ");
		}
		System.out.println();
		if (isBottomTurn) {
			System.out.println("Bottom goes next");
		} else {
			System.out.println("Top goes next");
		}
	}

	// return the indexes of legal moves
	public ArrayList<Move> getLegalMoves() {
		ArrayList<Move> theMoves = new ArrayList<>();

		if (isBottomTurn) {
			for (int i = 0; i <= bottomScore - 1; ++i) {
				if (board[i] != 0) {
					theMoves.add(new Move(i));
				}
			}
		} else {
			for (int i = bottomScore + 1; i <= topScore - 1; ++i) {
				if (board[i] != 0) {
					theMoves.add(new Move(i));
				}
			}
		}
		return theMoves;
	}

	// determine if a game is over
	public boolean isGameOver() {
		long sum = 0;
		for (int i = 0; i <= bottomScore - 1; ++i) {
			sum += board[i];
		}
		if (sum == 0) {
			return true;
		}
		sum = 0;
		for (int i = bottomScore + 1; i <= topScore - 1; ++i) {
			sum += board[i];
		}
		return sum == 0;

	}
	
	
	// determine if a move is legal
	public boolean isLegalMove(Move m) {
		if(m.location < 0 || m.location >= topScore) {
			return false;
		}else if( m.location == bottomScore) {
			return false;
		}else if( board[m.location]==0){
			return false;
		}else {
			return m.location > bottomScore ^ isBottomTurn;
		}
	}

	//return the final score of the game
	// >0 means bottom wins
	// This method is only valid if the game is over
	public long getFinalNetScore() {
		long sum = 0;
		for (int i = 0; i <= bottomScore; ++i) {
			sum += board[i];
		}
		// System.out.println("test " + sum + " " +totalPieces);
		return 2*(sum - totalPieces / 2);
		//return board[bottomScore] - board[topScore];
	}

	// return a new GameState that is the result of performing
	// move m on this GameState
	public GameState makeMove(Move m) {
		GameState next = new GameState(this);
		long totalToMove = next.board[m.location];
		if (!isLegalMove(m)) {
			System.out.println("bad move " + m.location);
			next.printBoard();
			return null;
		}
		next.board[m.location] = 0;
		int curLocation = m.location;
		while (totalToMove > 0) {
			curLocation = (curLocation + 1) % board.length;
			if (curLocation == topScore && next.isBottomTurn) {

			} else if (curLocation == bottomScore && !next.isBottomTurn) {

			} else {
				next.board[curLocation]++;

				totalToMove--;
			}
		}

		if (next.board[curLocation] == 1) {
			if (next.isBottomTurn && curLocation <= bottomScore - 1) {
				next.board[curLocation] = 0;
				next.board[bottomScore]++;
				next.board[bottomScore] += next.board[board.length - 2 - curLocation];
				next.board[board.length - 2 - curLocation] = 0;
			} else if (!next.isBottomTurn && curLocation >= bottomScore + 1 && curLocation <= topScore - 1) {
				next.board[curLocation] = 0;
				next.board[topScore]++;
				next.board[topScore] += next.board[board.length - 2 - curLocation];
				next.board[board.length - 2 - curLocation] = 0;
			}
		}
		if (curLocation == bottomScore) {
			next.isBottomTurn = true;
		} else if (curLocation == topScore) {

			next.isBottomTurn = false;
		} else {

			next.isBottomTurn = !isBottomTurn;

		}
		return next;
	}

	public double currentScore() {

		return board[bottomScore] - board[topScore];

	}
	
	
	// get the current score from the perspective of the current player
	// positive means the current player is winning
	public double getPlayerScore(boolean turn) {
		return board[turn? bottomScore: topScore];
	}
}
