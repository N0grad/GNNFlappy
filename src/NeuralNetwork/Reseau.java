package NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

public class Reseau {

	private Neurone[][] leReseau;
	private int[] agencement;

	public Reseau(int[] agencement) {

		// sauvegarde de l'agencement
		this.agencement = agencement;

		// initialisation du réseau
		this.leReseau = new Neurone[agencement.length][];

		// init différents des autres car aucun neurone biais
		this.leReseau[0] = new Neurone[agencement[0]];

		for (int i = 1; i < agencement.length; i++) {
			this.leReseau[i] = new Neurone[agencement[i] + 1]; // +1 = ajout neurone biais
		}

		// couche cachée
		for (int indexCouche = 0; indexCouche < agencement.length; indexCouche++) {

			for (int neurone = 0; neurone < agencement[indexCouche]; neurone++) {

				this.leReseau[indexCouche][neurone] = new Neurone();
				
			}

		}

		// connection interNeurone (chaque neurone(s) est connectee(s) avec la couche
		// suivante)
		// hormis le(s) neurone(s) de sortie.
		for (int indexCoucheInf = 0; indexCoucheInf < this.leReseau.length - 1; indexCoucheInf++) {

			int indexCoucheSup = indexCoucheInf + 1;

			for (int indexNeuroneInf = 0; indexNeuroneInf < this.agencement[indexCoucheInf]; indexNeuroneInf++) {

				Neurone neuroneInf = this.leReseau[indexCoucheInf][indexNeuroneInf];

				for (int indexNeuroneSup = 0; indexNeuroneSup < this.agencement[indexCoucheSup]; indexNeuroneSup++) {

					Neurone neuroneSup = this.leReseau[indexCoucheSup][indexNeuroneSup];
					Connection laConnection = new Connection(neuroneInf, neuroneSup);

					neuroneInf.addConnectionVersSortie(laConnection);
					neuroneSup.addConnectionVersEntree(laConnection);

				}

			}
		}

		// AJOUT DES NEURONES DE BIAIS
		for (int indexCouche = 1; indexCouche < this.leReseau.length; indexCouche++) {

			Neurone neuroneBiais = new Neurone();
			neuroneBiais.setValeur(1);
			this.leReseau[indexCouche][this.leReseau[indexCouche].length - 1] = neuroneBiais;

		}

		// ON RELIE LE NEURONE DE BIAIS A TOUS LES NEURONS DE SA COUCHE
		for (int indexCouche = 1; indexCouche < this.leReseau.length; indexCouche++) {

			for (int indexNeurone = 0; indexNeurone < this.leReseau[indexCouche].length - 1; indexNeurone++) {

				Neurone neuroneBiais = this.leReseau[indexCouche][leReseau[indexCouche].length - 1];
				Neurone neuroneSup = this.leReseau[indexCouche][indexNeurone];

				Connection laConnection = new Connection(neuroneBiais, neuroneSup);

				neuroneSup.addConnectionVersEntree(laConnection);
				neuroneBiais.addConnectionVersSortie(laConnection);

			}

		}
	}

	public void setValeurEntree(double[] valeurEntree) {

		// on regarde si le nb de valeur d'entrée = au nb de neurone d'entrée
		if (valeurEntree.length == this.agencement[0]) {

			// on insère les valeurs d'entée dans les neurones d'entrée
			for (int i = 0; i < valeurEntree.length; i++) {

				this.leReseau[0][i].setValeur(valeurEntree[i]);

			}

		} else {
			System.out.println("ARRET SYSTEME : Nombre de valeur d'entrée non égal au nombre de neurone d'entrée ! ");
			System.exit(0);
		}
	}

	public void propagationAvant() {

		// On commence par un car la propagation ne modifie pas les valeurs des neurones
		// d'entrée
		for (int indexCouche = 1; indexCouche < this.leReseau.length; indexCouche++) {

			for (int indexNeurone = 0; indexNeurone < this.leReseau[indexCouche].length; indexNeurone++) {

				this.leReseau[indexCouche][indexNeurone].propagationAvant();

			}

		}

	}

	public void printReseau() {

		for (int i = 0; i < this.leReseau.length; i++) {

			System.out.println("Couche " + (i + 1) + " : ");

			for (int j = 0; j < this.leReseau[i].length; j++) {

				System.out.print("  Neurone " + (j + 1) + " : ");
				this.leReseau[i][j].printNeurone();
				System.out.println();

			}

		}

	}

	public double[] getValeurSortie() {

		double[] ret = new double[this.leReseau[this.leReseau.length - 1].length - 1];

		for (int i = 0; i < this.leReseau[this.leReseau.length - 1].length - 1; i++) {

			ret[i] = this.leReseau[this.leReseau.length - 1][i].getValeur();

		}

		return ret;

	}

	public ArrayList<Double> getValeurDesConnections() {

		ArrayList<Double> ret = new ArrayList<Double>();

		for (int indexCouche = 0; indexCouche < this.leReseau.length; indexCouche++) {

			for (int indexNeurone = 0; indexNeurone < this.leReseau[indexCouche].length; indexNeurone++) {

				for (Connection conn : this.leReseau[indexCouche][indexNeurone].getConnectionVersSortie()) {

					ret.add(conn.getValeur());

				}
			}
		}

		return ret;

	}

	public void setValeurDesConnections(ArrayList<Double> valeur) {

		int index = 0;

		for (int indexCouche = 0; indexCouche < this.leReseau.length; indexCouche++) {

			for (int indexNeurone = 0; indexNeurone < this.leReseau[indexCouche].length; indexNeurone++) {

				for (Connection conn : this.leReseau[indexCouche][indexNeurone].getConnectionVersSortie()) {

					conn.setValeur(valeur.get(index));
					index++;

				}
			}
		}

	}

	public int getNbConnection() {

		int ret = 0;

		for (int i = 0; i < this.agencement.length - 1; i++) {
			ret += (this.agencement[i] * this.agencement[i + 1]) + this.agencement[i];
		}

		return ret;

	}

	public void setValeurRandomConnection() {
		Random r = new Random();

		int randomCouche = r.nextInt(this.leReseau.length - 1);
		int randomNeurone = r.nextInt(this.leReseau[randomCouche].length);
		int randomConnection = r.nextInt(this.leReseau[randomCouche][randomNeurone].getConnectionVersSortie().size());

		double randomValue = Math.random();

		this.leReseau[randomCouche][randomNeurone].getConnectionVersSortie().get(randomConnection).setValeur(randomValue);
	}

}