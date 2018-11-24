package NeuralNetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Neurone {

	private ArrayList<Connection> connectionVersEntree;
	private ArrayList<Connection> connectionVersSortie;
	
	private double valeur;
	
	public Neurone() {
		
		this.connectionVersEntree = new ArrayList<Connection>();
		this.connectionVersSortie = new ArrayList<Connection>();
		
	}
	
	public void setValeur(double valeur) {
		
		this.valeur = valeur;
		
	}

	public void addConnectionVersEntree(Connection laConnection) {
		
		this.connectionVersEntree.add(laConnection);
		
	}
	
	public void addConnectionVersSortie(Connection laConnection) {
		
		this.connectionVersSortie.add(laConnection);
		
	}
	
	public void propagationAvant() {
		
		double x = 0.0;
		
		if (!connectionVersEntree.isEmpty()) {
			
			for (Connection connection : connectionVersEntree) {
			
				x += (connection.getValeur() * connection.getNeuroneVersEntree().getValeur());
				
			}
			
			this.valeur = this.fonctionActivation(x);
			
		}
		
	}
	
	private double fonctionActivation(double x) {
		
		/*if (x > 0) {
			return x;
		}
		else {
			return 0.01 * x;
		}*/
		
		
		return (Math.sin(x));
		
	}
	
	public double getValeur() {
		
		return this.valeur;
		
	}
	
	
	public void printNeurone() {
		
		System.out.print("valeur = "+ new DecimalFormat("#.###").format(valeur));
		
		if (!connectionVersSortie.isEmpty()) {
			
			System.out.print(" / connection = ");
			
			for (Connection connection : connectionVersSortie) {
			
				connection.printConnection();
				
			}
		}
	}


	public ArrayList<Connection> getConnectionVersSortie() {
		return this.connectionVersSortie;
	}



}
