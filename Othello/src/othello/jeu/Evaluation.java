package othello.jeu;


public class Evaluation {
	private int [][]sample;
	private int tour;
	private double [] taux;
	private IA ia;
	
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
	
	public Evaluation(int [][] sample, int tour, IA ia) {
		this.sample = sample;
		this.tour = tour;
		this.ia = ia;
		changeTab();
		if (this.tour < 20) {
			taux = new double [] {10., 45., 45.};
		} else if (this.tour >= 20 && this.tour < 50) {
			taux = new double [] {20., 30., 50.};
		} else {
			taux = new double [] {45., 10., 45.};
		}
	}
	
	public void changeTab() {
		for (int y = 0; y < sample.length; y = sample.length - 1) {
			for (int x = 0; x < sample.length; x = sample.length - 1) {
				if (sample[y][x] == 1) {
					for (int i = -1; y + i <= y + 1; i++) {
						for (int o = -1; x + o <= x + 1; o++) {
							if (ia.isBorned(y + i) && ia.isBorned(x + o)) {
								forceTab[x][y] = Math.abs(forceTab[x][y]);
							}
						}
					}				
				}
			}
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
				if (sample[y][x] == 1 && ia.lookAround(y, x, -1 * 1))
					count = (sample[y][x] == 1) ? 1 : 0;
			}
		}
		return (count);
	}
	
	public int forceEvaluation() {
		int count = 0;
		for (int y = 0; y < sample.length; ++y) {
			for (int x = 0; x < sample.length; ++x) {
				if (sample[y][x] == 1 && ia.lookAround(y, x, -1 * 1)) {
					count = forceTab[y][x] = forceTab[y][x];
				}
			}
		}
		return (count);
	}
}