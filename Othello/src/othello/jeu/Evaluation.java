package othello.jeu;


public class Evaluation {
	private int [][] sample;
	private int tour;
	private double [] taux;
	private IA ia;
	
	public static final int CORNER = 250;
	public static final int STRAIGHT = 70;
	public static final int DIAGONAL = 100;
	public static final int [][] FORCETAB = {
		{    0,    0,   30,   10,   10,   30,    0,    0},
		{    0,    0,    0,    0,    0,    0,    0,    0},
		{   30,    0,    1,    2,    2,    1,    0,   30},
		{   10,    0,    2,   16,   15,    2,    0,   10},
		{   10,    0,    2,   16,   15,    2,    0,   10},
		{   30,    0,    1,    2,    2,    1,    0,   30},
		{    0,	   0,    0,    0,    0,    0,    0,    0},
		{    0,    0,   30,   10,   10,   30,    0,    0},
	};

	
	/*
	 * 3 cas possible :
	 * IA a le coin :			- toutes les pieces a cote rapporte des points a l'IA
	 * adversaire a le coin :	- si l'IA a des pieces a cote il perd des points 
	 * personne na le coin :	- si l'IA a des pieces a cote il perd des points.
	 * 							- si l'adversaire a des pieces a cote, l'IA gagne des points 
	 */
	public Evaluation(int [][] sample, int tour, IA ia) {
		this.sample = sample;
		this.tour = tour;
		this.ia = ia;
		if (this.tour < 20) {
			taux = new double [] {10., 45., 45.};
		} else if (this.tour >= 20 && this.tour < 50) {
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
				count += (sample[y][x] == 1) ? 15 : 0;
			}
		}
		return (count);
	}
	
	public int mobilityEvaluation() {
		int count = 0;
		for (int y = 0; y < sample.length; ++y) {
			for (int x = 0; x < sample.length; ++x) {
				if (sample[y][x] == 1 && ia.lookAround(y, x, -1 * 1))
					count += (sample[y][x] == 1) ? 20 : 0;
			}
		}
		return (count);
	}
	
	public int forceEvaluation() {
		int count = 0;
		for (int y = 0; y < sample.length; ++y) {
			for (int x = 0; x < sample.length; ++x) {
				if (sample[y][x] != 0) {
					count += sample[y][x] * FORCETAB[y][x];
				}
			}
		}
		int i, o;
		for (int y = 0; y < sample.length; y += sample.length - 1) {
			for (int x = 0; x < sample.length; x += sample.length - 1) {
				i = (y == 0) ? 1 : -1;
				o = (x == 0) ? 1 : -1;
				if (sample[y][x] == 0) {
					count += -1 * sample[y+i][x]	* STRAIGHT 
						   + -1 * sample[y][x+o]	* STRAIGHT
						   + -1 * sample[y+i][x+o]	* DIAGONAL;
				} else if (sample[y][x] == 1) {
					count += Math.abs(sample[y+i][x])	* STRAIGHT
						   + Math.abs(sample[y][x+o])	* STRAIGHT
						   + Math.abs(sample[y+i][x+o])	* DIAGONAL
						   +							  CORNER;
				} else {
					count += -1 * Math.abs(sample[y+i][x]) * STRAIGHT
						     -1 * Math.abs(sample[y][x+o]) * STRAIGHT
					         -1*Math.abs(sample[y+i][x+o]) * DIAGONAL
					         -								 CORNER;					
				}
			}
		}
		return (count);
	}
}