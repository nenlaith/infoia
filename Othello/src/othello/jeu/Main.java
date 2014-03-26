package othello.jeu;

import othello.ui.Plateau;

public class Main {
	
	public static void main (String[] args) {
		Jeu jeu = new Jeu();
		Plateau plateau = new Plateau("Jeu d'Othello", jeu);
		jeu.setPlateau(plateau);
		jeu.setCasesJouables();
	}

}
