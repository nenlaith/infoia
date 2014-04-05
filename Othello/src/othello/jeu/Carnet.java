package othello.jeu;

import java.util.ArrayList;
import java.util.Collections;

public class Carnet implements Comparable<Carnet> {
	
	private int [][] sample;
	private ArrayList <Carnet> listSons;
	private Carnet father;
	private int result;

	public Carnet(int [][] sample) {
		this.sample = new int [sample.length][sample.length];
		for (int y = 0; y < sample.length; ++y) { 
			for (int x = 0; x < sample.length; ++x) {
				this.sample[y][x] = sample[y][x];
			}
		}
		this.listSons = new ArrayList <Carnet> ();
		this.result = 0;
		this.father = null;
	}

	
	public Carnet(int [][] sample, Carnet father) {
		this.sample = new int [sample.length][sample.length];
		for (int y = 0; y < sample.length; ++y) { 
			for (int x = 0; x < sample.length; ++x) {
				this.sample[y][x] = sample[y][x];
			}
		}
		this.listSons = new ArrayList <Carnet> ();
		this.result = 0;
		this.father = father;
	}
	
	public Carnet(int [][] sample, int result, Carnet father) {
		this.sample = new int [sample.length][sample.length];
		for (int y = 0; y < sample.length; ++y) { 
			for (int x = 0; x < sample.length; ++x) {
				this.sample[y][x] = sample[y][x];
			}
		}
		this.listSons = new ArrayList <Carnet> ();
		this.result = result;
		System.out.println(father);
		this.father = father;
	}

	public int getResult() {
		return (this.result);
	}
	
	public void addSon(Carnet son) {
		listSons.add(son);
	}
	
	public void printGenealogy() {
		if (father != null) {
			father.printGenealogy();
			System.out.println("my father");
		}
		printSampleCases();
	}
	
	@Override
	public int compareTo(Carnet o) {
		if (this.result == o.getResult())
			return (0);
		else if (this.result > o.getResult())
			return (1);
		return (-1);
	}
	
	public Carnet addResult(int result) {
		this.result = result;
		return (this);
	}
	
	public void printSampleCases() {
		if (result != 0)
			System.out.println("result =" + result);
		System.out.print("  ");
		for (int y = 0; y < sample.length; ++y) {
			System.out.print("  " + y);
		}
		System.out.println();
		for (int y = 0; y < sample.length; ++y) {
			System.out.print(y + "[");
			for (int x = 0; x < sample.length; ++x) {
				if (sample[y][x] == -1) {System.out.print(" " + sample[y][x]);}
				else if (sample[y][x] == 0){System.out.print("  " + sample[y][x]);}
				else {System.out.print("  " +sample[y][x]);}
			}
			System.out.println(']');
		}
	}
	
	public static void printListCarnet(ArrayList<Carnet> list, int number) {
		Collections.sort(list);
		Collections.reverse(list);
		for (int y = 0; y < Math.min(number, list.size()); ++y) {
			list.get(y).printGenealogy();
		}
	}
	
}
