package othello.jeu;

import java.awt.Point;
import java.util.ArrayList;

import othello.ui.Couleur;
import othello.ui.DiffDialog;
import othello.ui.Niveau;

public class IA {

	public static int PETIT = -10000;
	public static int GRAND = 10000;
	private int[][] sample;
	private int tour;
	private ArrayList<Carnet> listCarnet;
	private Niveau niveau;

	public IA(Niveau niveau) {
		this.niveau = niveau;
		Evaluation.changeLevel(niveau);
	}
	
	public boolean isBorned(int t) {
		return (t >= 0 && t < sample.length);
	}

	public boolean lookAround(int y, int x, int noc) {
		int g;
		for (int i = -1; i <= 1; i++) {
			for (int o = -1; o <= 1; o++) {

				if (isBorned(y + i) && isBorned(x + o)
						&& sample[y + i][x + o] == noc) {
					g = 1;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + g * i][x + g * o] == noc) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == -1 * noc) {
							return (true);
						}
					}
				}
			}
		}
		return (false);
	}

	private ArrayList<State> getPossibleMouv(int c) {
		ArrayList<State> list = new ArrayList<State>();
		for (int y = 0; y < sample.length; y++) {
			for (int x = 0; x < sample[0].length; x++) {
				if (sample[y][x] == 0 && lookAround(y, x, -1 * c)) {
					list.add(new State(new Point(x, y), sample, c));
				}
			}
		}
		return (list);
	}

	public int abMax(int alpha, int beta, int profondeur, Carnet fatherCarnet) {
		int val;
		int result;
		Carnet firsts;

		if (profondeur == 0) {
			result = new Evaluation(sample, tour, this).evaluation();
			listCarnet.add(fatherCarnet.addResult(result));
			return (result);
		}
		ArrayList<State> fils = getPossibleMouv(1);
		
		fils = triCoups(fils);//ORDONNANCEMENT DES FILS
		
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			this.tour++;
			firsts = new Carnet(sample, fatherCarnet);
			fatherCarnet.addSon(firsts);
			val = abMin(alpha, beta, profondeur - 1, firsts);
			this.tour--;
			fils.get(i).retrieve();
			if (val >= beta)
				return val;
			alpha = (val >= alpha) ? val : alpha;
		}
		return alpha;
	}

	public int abMin(int alpha, int beta, int profondeur, Carnet fatherCarnet) {
		int val;
		int result;
		Carnet firsts;

		if (profondeur == 0) {
			result = new Evaluation(sample, tour, this).evaluation();
			listCarnet.add(fatherCarnet.addResult(result));
			return (result);
		}
		ArrayList<State> fils = getPossibleMouv(-1);
		
		fils = triCoups(fils);//ORDONNANCEMENT DES FILS
		
		for (int i = 0, l = fils.size(); i < l; i++) {
			fils.get(i).fill();
			this.tour++;
			firsts = new Carnet(sample, fatherCarnet);
			fatherCarnet.addSon(firsts);
			val = abMax(alpha, beta, profondeur - 1, firsts);
			this.tour--;
			fils.get(i).retrieve();
			if (val <= alpha)
				return val;
			beta = (val <= beta) ? val : beta;
		}
		return beta;
	}

	public State tour(int[][] sample, int nombreTour, DiffDialog debug) { // A CHANGER
		this.sample = sample;
		int helper = PETIT, test = 0;
		int indice = 0;
		this.tour = nombreTour;
		int profondeur = getProfondeur(niveau);
		this.listCarnet = new ArrayList<Carnet>();
		Carnet first = new Carnet(sample);
		Carnet firsts;
		int [][] neSample = cloneSample(); // A CHANGER
		
		ArrayList<State> fils = getPossibleMouv(1);
		System.out.println("nombre possible de coup " + fils.size());
		if (fils.size() == 0)
			return (null);

		fils = triCoups(fils);//ORDONNANCEMENT DES FILS

		for (int i = 0, l = fils.size(); i < l; i++) {
			test = helper;
			printSampleCases(); // A CHANGER
			fils.get(i).fill();
			printSampleCases(); // A CHANGER
			firsts = new Carnet(sample, first);
			first.addSon(firsts);
			if (test != (helper = Math.max(helper,
					abMin(PETIT, GRAND, profondeur, firsts)))) {
				indice = i;
			}
			fils.get(i).retrieve();
		}
		Carnet.printListCarnet(this.listCarnet, 1); // A CHANGER
		fils.get(indice).fill();
		debug.changeTab(neSample, cloneSample()); // A CHANGER
		debug.changeTextPane(); // A CHANGER
		return (fils.get(indice));
	}

	public int [][] cloneSample() {
		int [][] ne = new int [sample.length][sample.length];
		
		for (int y = 0; y < sample.length; ++y) {
			for (int x = 0; x < sample.length; ++x) {
				ne[y][x] = sample[y][x];
			}
		}
		
		return (ne);
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
				if (sample[y][x] == -1) {
					System.out.print(sample[y][x]);
				} else {
					System.out.print(" " + sample[y][x]);
				}
			}
			System.out.println(']');
		}
	}
	
	private int getProfondeur(Niveau niveau) {
		switch(niveau) {
		   case FACILE : return 3;
		   case MOYEN : return 7;
		   case DIFFICILE : return 12;
		   default : return 0;
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<State> triCoups(ArrayList<State> fils) {
		int i = 0;
		ArrayList<State> orderedFils = new ArrayList<State>();
		ArrayList<State> copy = (ArrayList<State>) fils.clone();
		// Ajout des coins
		while (i < copy.size()) {
			if (isCoin(copy.get(i).getPrimary())) {
				orderedFils.add(copy.get(i));
				copy.remove(i);
			} else {
				i++;
			}
		}
        if (hasACoin(Jeu.tour)) {
           if (hasTopLeftCoin(Jeu.tour)) {
              i=0;
              while (i < copy.size()) {
      		     if (isCorXAtTopLeft(copy.get(i).getPrimary())) {
      			    orderedFils.add(copy.get(i));
      				copy.remove(i);
      			 } 
      		     else {
      			    i++;
      			 }
              }
      	   }
           if (hasTopRightCoin(Jeu.tour)) {
              i=0;
              while (i < copy.size()) {
       		     if (isCorXAtTopRight(copy.get(i).getPrimary())) {
       			    orderedFils.add(copy.get(i));
       				copy.remove(i);
       			 } 
       		     else {
       			    i++;
       			 }
              }
       	   }
           if (hasBottomLeftCoin(Jeu.tour)) {
              i=0;
              while (i < copy.size()) {
       		     if (isCorXAtBottomLeft(copy.get(i).getPrimary())) {
       			    orderedFils.add(copy.get(i));
       				copy.remove(i);
       			 } 
       		     else {
       			    i++;
       			 }
              }
       	   }
           if (hasBottomRightCoin(Jeu.tour)) {
              i=0;
              while (i < copy.size()) {
       		     if (isCorXAtBottomRight(copy.get(i).getPrimary())) {
       			    orderedFils.add(copy.get(i));
       				copy.remove(i);
       			 } 
       		     else {
       			    i++;
       			 }
              }
       	   }
		}
		// Ajout des cases ni coin, ni X, ni C en ajoutant en premier les cases
		// qui offriront
		// le moins de coups possibles à l'adversaire
		ArrayList<State> forSearchFaster = new ArrayList<State>();
		i = 0;
		while (i < copy.size()) {
			if (!isCorX(copy.get(i).getPrimary())) {
				forSearchFaster.add(copy.get(i));
				copy.remove(i);
			} else {
				i++;
			}
		}
		ArrayList<Integer> listNbCoups = new ArrayList<Integer>();
		for (i = 0; i < forSearchFaster.size(); i++) {
			forSearchFaster.get(i).fill();
			listNbCoups.add(this.getPossibleMouv(-1).size());
			forSearchFaster.get(i).retrieve();
		}
		while (forSearchFaster.size() != 0) {
			int indiceMinNbCoups = getIndiceMinNbCoups(listNbCoups);
			orderedFils.add(forSearchFaster.get(indiceMinNbCoups));
			forSearchFaster.remove(indiceMinNbCoups);
			listNbCoups.remove(indiceMinNbCoups);
		}
		// Ajout des cases C ou X
		for (i = 0; i < copy.size(); i++) {
			orderedFils.add(copy.get(i));
		}
		return orderedFils;
	}
	
	private boolean hasTopLeftCoin(Couleur tour) {
	   return sample[0][0] == ((tour==Couleur.NOIR) ? -1 : 1);
	}
	
	private boolean hasTopRightCoin(Couleur tour) {
	   return sample[0][7] == ((tour==Couleur.NOIR) ? -1 : 1);
	}
	
	private boolean hasBottomLeftCoin(Couleur tour) {
	   return sample[7][0] == ((tour==Couleur.NOIR) ? -1 : 1);
	}
	
	private boolean hasBottomRightCoin(Couleur tour) {
	   return sample[7][7] == ((tour==Couleur.NOIR) ? -1 : 1);
    }
	
	private boolean hasACoin(Couleur tour) {
	   return hasTopLeftCoin(tour) || hasTopRightCoin(tour) || hasBottomLeftCoin(tour) || hasBottomRightCoin(tour);
	}

	private boolean isCoin(Point c) {
		return ((c.x == 0 && c.y == 0) || (c.x == 0 && c.y == 7)
				|| (c.x == 7 && c.y == 0) || (c.x == 7 && c.y == 7));
	}
	
	private boolean isCorXAtTopLeft(Point c) {
       return (c.x == 0 && c.y == 1) || (c.x == 1 && c.y == 1) || (c.x == 1 && c.y == 0);
	}
	
    private boolean isCorXAtTopRight(Point c) {
       return (c.x == 6 && c.y == 0) || (c.x == 6 && c.y == 1) || (c.x == 7 && c.y == 1);  
	}

    private boolean isCorXAtBottomLeft(Point c) {
       return (c.x == 0 && c.y == 6) || (c.x == 1 && c.y == 6) || (c.x == 1 && c.y == 7);	
    }

    private boolean isCorXAtBottomRight(Point c) {
	   return (c.x == 6 && c.y == 7) || (c.x == 6 && c.y == 6) || (c.x == 7 && c.y == 6);
    }

	private boolean isCorX(Point c) {
		return isCorXAtTopLeft(c) || isCorXAtTopRight(c) || isCorXAtBottomLeft(c) || isCorXAtBottomRight(c); 
	}

	private int getIndiceMinNbCoups(ArrayList<Integer> listNbCoups) {
		int min = listNbCoups.get(0), indice = 0;
		for (int i = 1; i < listNbCoups.size(); i++) {
			if (listNbCoups.get(i) < min) {
				min = listNbCoups.get(i);
				indice = i;
			}
		}
		return indice;
	}
}
