/**
 * Plays a series of matches among several players
 *
 * @author Michael Skalak
 * @author Dickinson College
 * @version Sept 9, 2020
 */

import java.util.*;

public class MancalaCageMatch {

	Random myRandom = new Random();

	public static void main(String[] args) {
		RandomPlayer rp = new RandomPlayer();
		HumanMancalaPlayer hu = new HumanMancalaPlayer();
		MiniMaxPlayer mm = new MiniMaxPlayer();
		AlphaBetaPlayer ab = new AlphaBetaPlayer();
		NearRandomPlayer nrp = new NearRandomPlayer();
		OneMovePlayer omp = new OneMovePlayer();
		MancalaCageMatch mcm = new MancalaCageMatch();
		mcm.addPlayer(rp);
		// mcm.addPlayer(mm);
		mcm.addPlayer(ab);
		mcm.addPlayer(nrp);
		mcm.addPlayer(omp);
		mcm.runGames();

	}

	ArrayList<SortablePlayer> thePlayers;

	public MancalaCageMatch() {
		thePlayers = new ArrayList<>();
	}

	public void runGames() {
		while (true) {
			for (int i = 0; i < thePlayers.size(); ++i) {
				for (int j = i + 1; j < thePlayers.size(); ++j) {

					SortablePlayer sp1 = thePlayers.get(i);
					SortablePlayer sp2 = thePlayers.get(j);
					System.out.println(sp1.myPlayer + " versus " + sp2.myPlayer);
					Mancala m = new Mancala(sp1.myPlayer, sp2.myPlayer, 7, 4, 1000);
					long p1FirstScore = m.playGame();
					m = new Mancala(sp2.myPlayer, sp1.myPlayer, 7, 4, 1000);
					long p2FirstScore = m.playGame();
					long totalScore = p1FirstScore - p2FirstScore;
					sp1.gamesPlayed++;
					sp2.gamesPlayed++;
					if (totalScore > 0) {
						System.out.println(
								sp1.myPlayer + " beats " + sp2.myPlayer + " " + p1FirstScore + " to " + p2FirstScore);
						sp1.wins++;
					} else if (totalScore == 0) {
						System.out.println(sp1.myPlayer + " and " + sp2.myPlayer + " tie");

						sp1.wins += .5;
						sp2.wins += .5;
					} else {
						System.out.println(
								sp2.myPlayer + " beats " + sp1.myPlayer + " " + p2FirstScore + " to " + p1FirstScore);

						sp2.wins++;
					}
					Collections.sort(thePlayers);
					System.out.println("w%\twins\tgames\tname");
					for (int k = 0; k < thePlayers.size(); ++k) {
						System.out.println(thePlayers.get(k));
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void addPlayer(MancalaPlayer p) {
		thePlayers.add(new SortablePlayer(p));
	}

	private class SortablePlayer implements Comparable<SortablePlayer> {
		double gamesPlayed;
		double wins;
		MancalaPlayer myPlayer;

		public SortablePlayer(MancalaPlayer p) {
			myPlayer = p;
			gamesPlayed = 0;
			wins = 0;
		}

		double getWinningPct() {
			if (gamesPlayed == 0) {
				return 0;

			} else {
				return wins / gamesPlayed;
			}
		}

		public String toString() {
			return String.format("%.2f", getWinningPct()) + "\t" + wins + "\t" + gamesPlayed + "\t" + myPlayer;
		}

		@Override
		public int compareTo(SortablePlayer o) {
			double winPct = getWinningPct();
			double oWinPct = o.getWinningPct();
			if (winPct > oWinPct) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
