package othello.jeu;

import java.awt.Point;
import java.util.ArrayList;

public class IA {

	public static int PETIT = -10000;
	public static int GRAND = 10000;
	private int[][] sample;
	private int tour;

	
	public boolean isBorned(int t) {
		return (t >= 0 && t < sample.length);
	}

	public boolean lookAround(int y, int x, int noc) {
		int g;
		for (int i = -1; i <= 1; i++) {
			for (int o = -1; o <= 1; o++) {
				
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == noc) {
					g = 1;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + i][x + o] == noc) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == -1 * noc){
							return (true);
						}
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
				if (sample[y][x] == 0 && lookAround(y, x, -1 * c)) {
					list.add(new State(new Point(x, y), sample, c));
				}
			}
		}
		return (list);
	}

	public int abMax(int alpha, int beta, int profondeur) {
		int val;
		if (profondeur == 0)
			return (new Evaluation(sample, tour, this).evaluation());
		ArrayList<State> fils = getPossibleMouv(1);
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			this.tour++;
			val = abMin(alpha, beta, profondeur - 1);
			this.tour--;
			fils.get(i).retrieve();
			if (val >= beta)
				return beta;
			if (val > alpha)
				alpha = val;
		}
		return alpha;
	}

	public int abMin(int alpha, int beta, int profondeur) {
		int val;
		if (profondeur == 0)
			return (new Evaluation(sample, tour, null).evaluation());
		ArrayList<State> fils = getPossibleMouv(1);
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			this.tour++;
			val = abMax(alpha, beta, profondeur - 1);
			this.tour--;
			fils.get(i).retrieve();
			if (val <= alpha)
				return alpha;
			if (val < beta)
				beta = val;
		}
		return beta;
	}

	public State tour(int[][] sample, int nombreTour) {
		this.sample = sample;
		int helper = GRAND, test = 0;
		int indice = 0;
		this.tour = nombreTour;

		ArrayList<State> fils = getPossibleMouv(1);
		System.out.println("nombre possible de coup " + fils.size());
		printSampleCases();
		if (fils.size() == 0)
			return (null);
		for (int i = 0, l = fils.size(); i < l; i++) {
			test = helper;
			fils.get(i).fill();
			printSampleCases();
			if (test != (helper = Math.max(helper, abMin(4, PETIT, GRAND)))) {
				indice = i;
			}
			fils.get(i).retrieve();
			printSampleCases();
		}
		return (fils.get(indice));
	}
	
	public void printSampleCases() {
		System.out.print("  ");
		for (int y = 0; y < sample.length; ++y) {
			System.out.print(" " + y);
		}
		System.out.println();
		for (int y = 0; y < sample.length; ++y) {
			System.out.print(y + "[");
			for (int x = 0; x < sample.length; ++x) {
				if (sample[y][x] == -1) {System.out.print(sample[y][x]);}
				else {System.out.print(" " + sample[y][x]);}
			}
			System.out.println(']');
		}
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
		this.seconds = new ArrayList <Point> ();
		this.c = c;
		put();
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

	public ArrayList <Point> getSeconds() {
		return (seconds);
	}
	
	public void put() {
		int g;
		boolean test;
		int x = primary.x;
		int y = primary.y;
		for (int i = -1; y + i <= y + 1; i++) {
			for (int o = -1; x + o <= x + 1; o++) {
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == -1 * c) {
					g = 1;
					test = false;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + g * i][x + g * o] == -1 * c) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == c)
							test = true;
					}
					for (int f = 1; test && sample[y + f * i][x + f * o] != c; ++f) {
						addSecond(new Point(x + f * o, y + f * i));
					}
				}
			}
		}		
	}
	
	public void fill() {
		Point helper;
		sample[primary.y][primary.x] = 1;
		for (int i = 0, length = seconds.size(); i < length; i++) {
			helper = seconds.get(i);
			sample[helper.y][helper.x] = 1;
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