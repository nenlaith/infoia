package othello.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import othello.jeu.Jeu;

@SuppressWarnings("serial")
public class Plateau extends JFrame {
	
	public static final int PLATEAU_WIDTH = 8;
	public static final int PLATEAU_HEIGHT = 8;
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 500;
	
	private Case[][] cases;
	private Jeu jeu; // CHANGEMENT
	
	public Plateau(String title, Jeu jeu) {
       super(title);
       this.setLayout(new SpringLayout());
       this.jeu = jeu;// CHANGEMENT
	   initialiserCases();
	   JPanel panel = new JPanel();
	   panel.setPreferredSize(new Dimension(FRAME_WIDTH/3,FRAME_HEIGHT));
	   Container container = this.getContentPane();
	   container.add(new PanelPlateau(),SpringLayout.WEST);
	   container.add(panel,SpringLayout.EAST);
	   this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   this.setSize(FRAME_WIDTH,FRAME_HEIGHT);
	   this.setVisible(true);
	   cases[3][3].setCouleur(Couleur.BLANC);
	   cases[3][4].setCouleur(Couleur.NOIR);
	   cases[4][3].setCouleur(Couleur.NOIR);
	   cases[4][4].setCouleur(Couleur.BLANC);
	}
	
	public void printSampleCases() {
		int [][] sampleCases = getSampleCases();
		
		for (int y = 0; y < PLATEAU_HEIGHT; ++y) {
			System.out.print('[');
			for (int x = 0; x < PLATEAU_WIDTH; ++x) {
				if (sampleCases[y][x] == -1) {System.out.println(sampleCases[y][x]);}
				else {System.out.println(" " + sampleCases[y][x]);}
			}
			System.out.println(']');
		}
	}
	
	public int[][] getSampleCases() {
		int [][] sampleCases = new int[PLATEAU_HEIGHT][PLATEAU_WIDTH];
		
		for (int y = 0; y < PLATEAU_HEIGHT; ++y) {
			for (int x = 0; x < PLATEAU_WIDTH; ++x) {
				if (cases[y][x].isJouable())
					sampleCases[y][x] = 0;
				else
					sampleCases[y][x] = (cases[y][x].getCouleur() == Jeu.tour) ? -1 : 1;
			}
		}
		return (sampleCases);
	} 
	
	public Case getCase(int y, int x) {
		return (cases[y][x]);
	}
	
	private void initialiserCases() {
	   cases = new Case[PLATEAU_HEIGHT][PLATEAU_WIDTH];
	   for (int i=0;i<cases.length;i++) {
	      for (int j=0;j<cases[i].length;j++) {
	         cases[i][j] = new Case(this, this.jeu); // CHANGEMENT
	      }
	   }
	}
	
	public Case[][] getCases() {
	   return cases;
	}
	
	public void setAllCasesNonJouable() {
	   for (int i=0;i<Plateau.PLATEAU_HEIGHT;i++) {
	      for (int j=0;j<Plateau.PLATEAU_WIDTH;j++) {
	         cases[i][j].setJouable(false);    
	      }
	   }
	}
	
	class PanelPlateau extends JPanel {
		
		public PanelPlateau() {
		   super(new GridLayout(Plateau.PLATEAU_HEIGHT,Plateau.PLATEAU_WIDTH,1,1));
		   this.setPreferredSize(new Dimension(2*FRAME_WIDTH/3,FRAME_HEIGHT-28));
		   this.setBackground(Color.BLACK);
		   for (int i=0;i<cases.length;i++)
		      for (int j=0;j<cases[i].length;j++)
			     this.add(cases[i][j]);
		}
		
	}
		
}
