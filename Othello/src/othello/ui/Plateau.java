package othello.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import othello.jeu.Jeu;

@SuppressWarnings("serial")
public class Plateau extends JFrame {
	
	public static final int PLATEAU_WIDTH = 8;
	public static final int PLATEAU_HEIGHT = 8;
	public static final int FRAME_WIDTH = 600;
	public static final int FRAME_HEIGHT = 500;
	public static final JTextField scoreNoirField = new JTextField("2");
	public static final JTextField scoreBlancField = new JTextField("2");
	
	private Case[][] cases;
	private Jeu jeu;
	
	public Plateau(String title, Jeu jeu) {
       super(title);
       this.setLayout(new BorderLayout());
       this.jeu = jeu;
	   initialiserCases();
	   JPanel panel = new JPanel(new BorderLayout());
	   JPanel panelScore = new JPanel(new GridLayout(2,2));
	   panelScore.setBorder(new EmptyBorder(10,10,10,10));
	   JLabel scoreNoirLabel = new JLabel("Noir");
	   JLabel scoreBlancLabel = new JLabel("Blanc");
	   panelScore.add(scoreNoirLabel);
	   panelScore.add(scoreNoirField);
	   panelScore.add(scoreBlancLabel);
	   panelScore.add(scoreBlancField);
	   scoreNoirField.setEditable(false);
	   scoreBlancField.setEditable(false);
	   panel.add(panelScore,BorderLayout.NORTH);
	   panel.setPreferredSize(new Dimension(FRAME_WIDTH/3,FRAME_HEIGHT));
	   Container container = this.getContentPane();
	   container.add(new PanelPlateau(),BorderLayout.WEST);
	   container.add(panel,BorderLayout.EAST);
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
				if (sampleCases[y][x] == -1) {System.out.print(sampleCases[y][x]);}
				else {System.out.print(" " + sampleCases[y][x]);}
			}
			System.out.println(']');
		}
	}
	
	public int[][] getSampleCases() {
		int [][] sampleCases = new int[PLATEAU_HEIGHT][PLATEAU_WIDTH];
		
		for (int y = 0; y < PLATEAU_HEIGHT; ++y) {
			for (int x = 0; x < PLATEAU_WIDTH; ++x) {
				if (cases[y][x].getCouleur() == Couleur.NONE)
					sampleCases[y][x] = 0;
				else
					sampleCases[y][x] = (cases[y][x].getCouleur() == Jeu.tour) ? 1 : -1;
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
	         cases[i][j] = new Case(this, this.jeu,i,j);
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
	
	/**
	 * Retourne tous les pions qui sont gagnés par le joueur lorsqu'il a joué son coup
	 * @param c
	 */
	public void returnAllPions(Case c) {
       for (int i=0;i<c.getDirections().size();i++) {
          returnOnTheLine(c.getI(),c.getJ(),c.getDirection(i));
       }
	}
	
	/**
	 * Fonction récursive qui va retourner tous les pions sur le chemin jusqu'à atteindre un des pions du joueur qui vient
	 * de jouer
	 * @param x Ordonnée de la case jouée
	 * @param y Abcisse de la case jouée
	 * @param direction Coordonnées de la direction dans laquelle les pions doivent être retournés
	 */
	public void returnOnTheLine(int x, int y, Point direction) {
	   if (cases[x+direction.x][y+direction.y].getCouleur()==Jeu.nonTour) {
	      cases[x+direction.x][y+direction.y].setCouleur(Jeu.tour);
		  returnOnTheLine(x+direction.x,y+direction.y,direction);
	   }
	}
	
	public void updateScores() {
	   int scoreNoir=0,scoreBlanc=0;
	   for (int i=0;i<Plateau.PLATEAU_HEIGHT;i++) {
	      for (int j=0;j<Plateau.PLATEAU_WIDTH;j++) {
	         if (cases[i][j].getCouleur()==Couleur.NOIR)
	            scoreNoir++;
	         else if (cases[i][j].getCouleur()==Couleur.BLANC)
	            scoreBlanc++;
	      }
	   }
	   scoreNoirField.setText(""+scoreNoir);
	   scoreBlancField.setText(""+scoreBlanc);
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
