package othello.ui;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import othello.jeu.Jeu;

@SuppressWarnings("serial")
public class Case extends JButton {
	
	public static final Color backgroundCase = new Color(0,128,0);
	public static final Color backgroundJouable = new Color(0,49,0);
	
	private int i,j;
	private Plateau plateau;
	private Couleur couleur;
	private boolean jouable;
	private ArrayList<Point> directions;
	private Jeu jeu;
	
	public Case(Plateau plateau, Jeu jeu, int i, int j) {
	   super();
	   this.i=i;
	   this.j=j;
	   this.jeu = jeu;
	   this.plateau=plateau;
	   this.setBackground(backgroundCase);
	   this.setBorderPainted(false);
	   this.setFocusPainted(false);
	   this.addActionListener(new CaseListener(this, this.jeu));
	   this.jouable = false;
	   directions = new ArrayList<Point>();
	}
	
	public int getI() {
	   return i;
	}
	
	public int getJ() {
	   return j;
	}
	
	public Plateau getPlateau() {
	   return plateau;
	}
	
	public Couleur getCouleur() {
	   return couleur;
	}
	
	public ArrayList<Point> getDirections() {
	   return directions;
	}
	
	public Point getDirection(int position) {
	   return directions.get(position);
	}
	
	public void addDirection(Point direction) {
	   this.directions.add(direction);
	}
	
	public void setCouleur(Couleur couleur) {
	   this.couleur=couleur;
	   if (couleur==Couleur.NOIR)
	      this.setIcon(new ImageIcon("pion_noir.png"));
	   else
	      this.setIcon(new ImageIcon("pion_blanc.png"));
	}
	
	public boolean isJouable() {
	   return jouable;
	}
	
	public void setJouable(boolean jouable) {
	   this.jouable = jouable;
	   if (jouable)
	      this.setBackground(backgroundJouable);
	   else
	      this.setBackground(backgroundCase);
	}
	
}
