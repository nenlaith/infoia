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
	      Jeu.changeTour();
	      Timer t = new Timer(1000 * 1, new ActionListener() {
	    	  public void actionPerformed(ActionEvent e) {
	    		  jeu.jeuIA();
	    		  Jeu.changeTour();
	    		  jeu.setCasesJouables();
	    		  c.getPlateau().updateScores();
	    	  }
	      });
	      t.setRepeats(false);
	      t.start();
	   }
	}

}
