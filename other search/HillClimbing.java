// code originally copied from
// https://sites.google.com/site/indy256/algo/simulated_annealing
// Then altered by John MacCormick, 10/3/2014.

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HillClimbing extends JFrame implements Runnable {
	Random rnd = new Random();
	int n = rnd.nextInt(300) + 250;
	final int waitTime = 0; // milliseconds between repainting

	int[] bestState;
	double[] x = new double[n];
	double[] y = new double[n];

	{
		for (int i = 0; i < n; i++) {
			x[i] = rnd.nextDouble();
			y[i] = rnd.nextDouble();
		}
	}

	public void climb() {
		int[] curState = new int[n];
		for (int i = 0; i < n; i++)
			curState[i] = i;
		bestState = curState.clone();
		boolean done = false;
		while (!done) {
			double bestDelta = 0.0;
			int bestI = -1;
			int bestJ = -1;
			for (int i = 0; i < n; i++) {
				for (int j = i + 1; j < n; j++) {
					int i1 = (i - 1 + n) % n;
					int j1 = (j + 1) % n;
					double delta = dist(x[curState[i1]], y[curState[i1]],
							x[curState[j]], y[curState[j]])
							+ dist(x[curState[i]], y[curState[i]],
									x[curState[j1]], y[curState[j1]])
							- dist(x[curState[i1]], y[curState[i1]],
									x[curState[i]], y[curState[i]])
							- dist(x[curState[j]], y[curState[j]],
									x[curState[j1]], y[curState[j1]]);
					if (delta < bestDelta) {
						bestDelta = delta;
						bestI = i;
						bestJ = j;
					}
				}
			}
			if (bestDelta < -1e-9) {
				reverse(curState, bestI, bestJ);
				System.arraycopy(curState, 0, bestState, 0, n);
				repaint();
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					// do nothing
				}
			} else {
				done = true;
			}
		}
	}

	// http://en.wikipedia.org/wiki/2-opt
	static void reverse(int[] p, int i, int j) {
		int n = p.length;
		// reverse order from i to j
		while (i != j) {
			int t = p[j];
			p[j] = p[i];
			p[i] = t;
			i = (i + 1) % n;
			if (i == j)
				break;
			j = (j - 1 + n) % n;
		}
	}

	double eval(int[] state) {
		double res = 0;
		for (int i = 0, j = state.length - 1; i < state.length; j = i++)
			res += dist(x[state[i]], y[state[i]], x[state[j]], y[state[j]]);
		return res;
	}

	static double dist(double x1, double y1, double x2, double y2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		return Math.sqrt(dx * dx + dy * dy);
	}

	// visualization code
	public HillClimbing() {
		setContentPane(new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				((Graphics2D) g).setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				((Graphics2D) g).setStroke(new BasicStroke(3));
				g.setColor(Color.BLUE);
				int w = getWidth() - 5;
				int h = getHeight() - 30;
				for (int i = 0, j = n - 1; i < n; j = i++)
					g.drawLine((int) (x[bestState[i]] * w),
							(int) ((1 - y[bestState[i]]) * h),
							(int) (x[bestState[j]] * w),
							(int) ((1 - y[bestState[j]]) * h));
				g.setColor(Color.RED);
				for (int i = 0; i < n; i++)
					g.drawOval((int) (x[i] * w) - 1,
							(int) ((1 - y[i]) * h) - 1, 3, 3);
				g.setColor(Color.BLACK);
				g.drawString(String.format("length: %.3f", eval(bestState)), 5,
						h + 20);
			}
		});
		setSize(new Dimension(600, 600));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		new Thread(this).start();
	}

	public static void main(String[] args) {
		new HillClimbing();
	}

	@Override
	public void run() {
		climb();
	}
}
