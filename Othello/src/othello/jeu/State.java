package othello.jeu;

import java.awt.Point;
import java.util.ArrayList;

public class State {
	
	private Point primary;
	private ArrayList<Point> seconds;
	private int [][] sample;
	private int c;
	
	public State(Point primary, int [][] sample, int c) {
		this.primary = primary;
		this.sample = sample;
		this.seconds = new ArrayList<Point>();
		this.c = c;
		put();
	}

	private boolean isBorned(int t) {
		return ((t >= 0 && t < sample.length) ? true : false);
	}
	
	public void addSecond(Point point) {
		seconds.add(point);
	}

	public Point getPrimary() {
		return (primary);
	}

	public ArrayList <Point> getSeconds() {
		return (seconds);
	}
	
	public String toString() {
	    return "("+this.primary.x+","+this.primary.y+")";
	}
	
	public void put() {
		int g;
		boolean test;
		int x = primary.x;
		int y = primary.y;
		for (int i = -1; i <= 1; i++) {
			for (int o = -1; o <= 1; o++) {
				if (isBorned(y + i) && isBorned(x + o) && sample[y + i][x + o] == -1 * c) {
					g = 1;
					test = false;
					while (isBorned(y + g * i) && isBorned(x + g * o)
							&& sample[y + g * i][x + g * o] == -1 * c) {
						++g;
						if (isBorned(y + g * i) && isBorned(x + g * o)
								&& sample[y + g * i][x + g * o] == c)
							test = true;
					}
					for (int f = 1; test && f < g; ++f) {
						addSecond(new Point(x + f * o, y + f * i));
					}
				}
			}
		}		
	}
	
	public void fill() {
		Point helper;
		sample[primary.y][primary.x] = c;
		for (int i = 0, length = seconds.size(); i < length; i++) {
			helper = seconds.get(i);
			sample[helper.y][helper.x] = c;
		}		
	}

	public void retrieve() {
		Point helper;
		sample[primary.y][primary.x] = 0;
		for (int i = 0, length = seconds.size(); i < length; i++) {
			helper = seconds.get(i);
			sample[helper.y][helper.x] = -1 * c;
		}
	}
}
