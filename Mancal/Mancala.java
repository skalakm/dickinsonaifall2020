
/**
 * Plays a match between two players
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */
public class Mancala {

	MancalaPlayer p1, p2, randomPlayer;

	int size, startingStones,time;
	
	public static void main(String[] args) {

		RandomPlayer rp = new RandomPlayer();
		HumanMancalaPlayer hu = new HumanMancalaPlayer();
		MiniMaxPlayer mm = new MiniMaxPlayer();
		AlphaBetaPlayer ab = new AlphaBetaPlayer();
		NearRandomPlayer nrp = new NearRandomPlayer();
		OneMovePlayer omp = new OneMovePlayer();
		Mancala m = new Mancala(hu, hu, 7,4,100000);
		m.playGame();
		
	}

	public Mancala(MancalaPlayer p1, MancalaPlayer p2, int size, int startingStones, int time) {
		this.p1 = p1;
		this.p2 = p2;
		this.size = size;
		this.startingStones = startingStones;
		this.time = time;
		randomPlayer = new RandomPlayer();
	}

	public long playGame() {
		return playGame(true);
	}
	
	public long playGame(boolean p1IsBottom) {
		int moveCount = 0;
		MancalaPlayer bottom,top;
		if(p1IsBottom) {
			bottom=p1;
			top = p2;
		}else {
			bottom = p2;
			top = p1;
		}
		GameState g = new GameState(size, startingStones);
		while (!g.isGameOver()) {

			moveCount++;
			GameThread t;
			if (g.isBottomTurn) {
				t = new GameThread(bottom,g,time);
				
			} else {

				t = new GameThread(top,g,time);
				
			}
		//	System.out.println("trying ");
			t.run();
			
			int segments =0;
			while(t.m==null && segments <8) {
				try {
					Thread.sleep(time/8);
					segments++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(t.m == null){
				System.out.println("Player took too long! " + t.p);
				System.out.println("Making random move...");
				t.m=randomPlayer.getMove(g,time);
			}
			g = g.makeMove(t.m);
			g.printBoard();
			System.out.println("Top is " + top);
			System.out.println("Bottom is " + bottom);
			
		}
		long finalScore = g.getFinalNetScore();
		System.out.println( "Game over!");
		System.out.println("Final net score is " + finalScore );
		
		if(finalScore > 0) {
			System.out.println(bottom + " wins!");
		}else if(finalScore <0) {
			System.out.println(top + " wins!");
		}else {
			System.out.println("It's a tie!");
		}
		return g.getFinalNetScore();
	}

	private class GameThread implements Runnable {

		MancalaPlayer p;
		GameState g;
		int time;
		Move m=null;

		public GameThread(MancalaPlayer p, GameState g, int time) {
			this.p = p;
			this.g = g;
			this.time = time;
		}

		@Override
		public void run() {
			System.out.println("getting move");
			m = p.getMove(g, System.currentTimeMillis() + time);

		}

	}
}
