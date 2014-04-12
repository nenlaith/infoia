package othello.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import othello.jeu.Jeu;

public class CaseListener implements ActionListener {
	
	private Case c;
	private Jeu jeu;
	
	public CaseListener(Case c, Jeu jeu) {
	   this.c=c;
	   this.jeu = jeu;
	}
	
	public void actionPerformed(ActionEvent e) {
	   if (c.isJouable()) {
	      c.setCouleur(Jeu.tour);
	      c.getPlateau().returnAllPions(c);
	      c.getPlateau().setAllCasesNonJouable();
	      c.getPlateau().updateScores();
	      if (jeu.getGagnant()!=null) {
	         c.getPlateau().endGame(true);
	      }
	      else {
	         Jeu.changeTour();
	         tourIA();
	      }
	   }
	}
	
	private void tourIA() {
	   Timer t = new Timer(1000 * 1, new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	         boolean iaAJoue = jeu.jeuIA();
	    	 Jeu.changeTour();
	         jeu.setCasesJouables();
	    	 c.getPlateau().updateScores();
	    	 if (jeu.getGagnant()!=null) {
		        c.getPlateau().endGame(true);
		     }
	    	 //Plus aucun coup a jouer pour le joueur -> l'IA rejoue
	    	 else if (c.getPlateau().getNumberCasesJouables()==0) {
	    		Jeu.changeTour();
	    	    tourIA();
	    	 }
	    	 //Plus aucun coup a jouer pour le joueur et pour l'IA
	    	 else if (c.getPlateau().getNumberCasesJouables()==0 && !iaAJoue) {
	    		 c.getPlateau().endGame(false);
	    	 }
	      }
	   });
	   t.setRepeats(false);
	   t.start();
	}

}
