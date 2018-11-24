package NeuralNetwork;

import java.text.DecimalFormat;

public class Connection {

	private Neurone neuroneVersEntree;
	private Neurone neuroneVersSortie;
	private double valeur;
	
	public Connection(Neurone neuroneVersEntree, Neurone neuroneVersSortie) {
		
		this.neuroneVersEntree = neuroneVersEntree;
		this.neuroneVersSortie = neuroneVersSortie;
		this.valeur = Math.random();
		
	}

	public Neurone getNeuroneVersEntree() {
		return this.neuroneVersEntree;
	}

	public double getValeur() {
		return this.valeur;
	}
	
	public void printConnection() {
		System.out.print(new DecimalFormat("#.####").format(valeur)+ " ");
	}

	public void setValeur(double valeur) {
		this.valeur = valeur;
	}

	public Neurone getNeuroneVersSortie() {
		return neuroneVersSortie;
	}

	public void setNeuroneVersSortie(Neurone neuroneVersSortie) {
		this.neuroneVersSortie = neuroneVersSortie;
	}

	
}
