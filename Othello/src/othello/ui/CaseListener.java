package othello.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	      Jeu.changeTour();
	      jeu.jeuIA();
	      Jeu.changeTour();
	      jeu.setCasesJouables();
	   }
	}

}
