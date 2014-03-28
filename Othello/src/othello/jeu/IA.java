package othello.jeu;

import java.awt.Point;
import java.util.ArrayList;

public class IA {

	public static int PETIT = -3000;
	public static int GRAND = 3000;
	private int[][] sample;
	private double [] taux;
	private int tour;

	class Evaluation {
		public final int [][] forceTab = {
			{500, -150, 30, 10, 10, 30, -150, 500},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{10, 0, 2, 16, 15, 2, 0, 10},
			{10, 0, 2, 16, 15, 2, 0, 10},
			{30, 0, 1, 2, 2, 1, 0, 30},
			{-150, -250, 0, 0, 0, 0, -250, -150},
			{500, -150, 30, 10, 10, 30, -150, 500},
		};
		
		
		public void changeTab() {
			for (int y = 0; y < sample.length; y = sample.length - 1) {
				for (int x = 0; x < sample.length; x = sample.length - 1) {
					if (sample[y][x] == 1) {
						for (int i = -1; y + i <= y + 1; i++) {
							for (int o = -1; x + o <= x + 1; o++) {
								if (isBorned(y + i) && isBorned(x + o)) {
									forceTab[x][y] = Math.abs(forceTab[x][y]);
								}
							}
						}				
					}
				}
			}
		}
		
		public Evaluation() {
			changeTab();
			if (tour < 20) {
				taux = new double [] {10., 45., 45.};
			} else if (tour >= 20 && tour < 50) {
				taux = new double [] {20., 30., 50.};
			} else {
				taux = new double [] {45., 10., 45.};
			}
		}
		
		public int evaluation() {
			return ((int)(taux[0] * countEvaluation()
					+ taux[1] * mobilityEvaluation()
					+ taux[2] * forceEvaluation()));
		}
		
		public int countEvaluation() {
			int count = 0;
			for (int y = 0; y < sample.length; ++y) {
				for (int x = 0; x < sample.length; ++x) {
					count = (sample[y][x] == 1) ? 1 : 0;
				}
			}
			return (count);
		}
		
		public int mobilityEvaluation() {
			int count = 0;
			for (int y = 0; y < sample.length; ++y) {
				for (int x = 0; x < sample.length; ++x) {
					if (sample[y][x] == 1 && lookAround(y, x, -1 * 1))
						count = (sample[y][x] == 1) ? 1 : 0;
				}
			}
			return (count);
		}
		
		public int forceEvaluation() {
			int count = 0;
			for (int y = 0; y < sample.length; ++y) {
				for (int x = 0; x < sample.length; ++x) {
					if (sample[y][x] == 1 && lookAround(y, x, -1 * 1)) {
						count = forceTab[y][x] = forceTab[y][x];
					}
				}
			}
			return (count);
		}
	}
	
	private boolean isBorned(int t) {
		return (t >= 0 && t < sample.length);
	}

	private boolean lookAround(int y, int x, int noc) {
		int g;
		for (int i = -1; i <= 1; i++) {
			for (int o = -1; o <= 1; o++) {
				
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == noc) {
					g = 1;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + i][x + o] == noc) {
						++g;
						if (y == 2 && x == 2) {System.out.println("la " + y +" "+ x +" "+ (y + (g * i)) +" "+ (x + (g * o)));}
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == -1 * noc){
//							System.out.println("jen ai trouve un" + y +" "+ x +" "+ (y + (g * i)) +" "+ (x + (g * o)));
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
					System.out.println("jen mets un");
					list.add(new State(new Point(x, y), sample, c));
				}
			}
		}
		return (list);
	}

	public int abMax(int profondeur, int alpha, int beta) {
		int val;
		if (profondeur == 0)
			return (new Evaluation().evaluation());
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

	public int abMin(int profondeur, int alpha, int beta) {
		int val;
		if (profondeur == 0)
			return (new Evaluation().evaluation());
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
		System.out.println("step " + fils.size());
		for (int i = 0, l = fils.size(); i < l; i++) {
			test = helper;
			if (test != (helper = Math.max(helper, abMin(4, PETIT, GRAND)))) {
				indice = i;
			}
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
		sample[y][x] = c;
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
					System.out.println(test);
					for (int f = 1; test && sample[y + f * i][x + f * o] != c; ++f) {
						addSecond(new Point(x + f * o, y + f * i));
						sample[y + f * i][x + f * o] = c;
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