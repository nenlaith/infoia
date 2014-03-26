package othello.jeu;

import java.awt.Point;
import java.util.ArrayList;

public class IA {

	public static int PETIT = -3000;
	public static int GRAND = 3000;
	private int[][] sample;

	private boolean isBorned(int t) {
		return (t >= 0 && t < sample.length);
	}

	private boolean lookAround(int y, int x, int noc) {
		int g;
		for (int i = -1; y + i <= y + 1; i++) {
			for (int o = -1; x + o <= x + 1; o++) {
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == noc) {
					g = 2;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + i][x + o] == noc) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == -1 * noc)
							return (true);
					}
				}
			}
		}
		return (false);
	}

	private ArrayList<State> getPossibleMouv(int c) {
		ArrayList<State> list = new ArrayList <State> ();
		for (int y = 0; y < sample.length; y++) {
			for (int x = 0; x < sample[0].length; x++) {
				if (sample[y][x] == c && lookAround(y, x, -1 * c))
					list.add(new State(new Point(x, y), sample, c));
			}
		}
		return (list);
	}

	private int evaluation() {
		return (1);
	}

	public int abMax(int profondeur, int alpha, int beta) {
		int val;
		if (profondeur == 0)
			return evaluation();
		ArrayList<State> fils = getPossibleMouv(1);
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			val = abMin(alpha, beta, profondeur - 1);
			fils.get(i).retrieve();
			if (val >= beta)
				return beta;
			if (val > alpha)
				alpha = val;
		}
		return alpha;
	}

	public int abMin(int profondeur, int alpha, int beta) {
		int val;
		if (profondeur == 0)
			return -1 * evaluation();
		ArrayList<State> fils = getPossibleMouv(1);
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			val = abMax(alpha, beta, profondeur - 1);
			fils.get(i).retrieve();
			if (val <= alpha)
				return alpha;
			if (val < beta)
				beta = val;
		}
		return beta;
	}

	public Point tour(int[][] sample, int nombreTour) {
		this.sample = sample;
		int helper = GRAND;
		int indice = 0;

		ArrayList<State> fils = getPossibleMouv(1);
		for (int i = 0, l = fils.size(); i < l; i++) {
			if (helper == (helper = Math.max(helper, abMin(4, PETIT, GRAND)))) {
				indice = i;
			}
		}
		return (fils.get(indice).getPrimary());
	}
}

class State {
	private Point primary;
	private ArrayList<Point> seconds;
	private int [][] sample;
	private int c;
	
	public State(Point primary, int [][] sample, int c) {
		this.primary = primary;
		this.sample = sample;
		this.c = c;
	}

	private boolean isBorned(int t) {
		return ((t >= 0 && t < sample.length) ? true : false);
	}
	
	public void addSecond(Point point) {
		seconds.add(point);
	}

	public Point getPrimary() {
		return (primary);
	}

	public void fill() {
		int g;
		boolean test;
		int x = primary.x;
		int y = primary.y;
		sample[y][x] = c;
		for (int i = -1; y + i <= y + 1; i++) {
			for (int o = -1; x + o <= x + 1; o++) {
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == -1 * c) {
					g = 2;
					test = false;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + g * i][x + g * o] == -1 * c) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == c)
							test = true;
					}
					for (int f = 1; test && sample[y + f * i][x + f * o] != c; ++f) {
						sample[y + f * i][x + f * o] = c;
					}
				}
			}
		}
	}

	public void retrieve() {
		Point helper;
		sample[primary.y][primary.x] = 0;
		for (int i = 0, length = seconds.size(); i < length; i++) {
			helper = seconds.get(i);
			sample[helper.y][helper.x] *= -1;
		}
	}
}
