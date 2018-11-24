package Genetic;
import java.util.ArrayList;

import Game.FlappyBird;
import NeuralNetwork.Reseau;

public class Individu implements Comparable<Individu>{
	private float posX, posY, vx, vy;
	private final int longueurBird = 50, largeurBird = 35;
	private boolean vivant = true;
	private int point = 0;
	private Reseau leCerveau;
	
	public Individu() {
		
		posX = FlappyBird.WIDTH / 2;
		posY = FlappyBird.HEIGHT / 2;
		
		this.leCerveau = new Reseau(new int[] {2, 9, 2});
	}

	public void physics() {
		posX += vx;
		posY += vy;
		vy += 0.5f;
	}

	public void joue(double differenceDeHauteur, double distanceHorizontal) {

		leCerveau.setValeurEntree(new double[] {differenceDeHauteur, distanceHorizontal});	
		leCerveau.propagationAvant();
		
		double[] val = leCerveau.getValeurSortie();
		
		System.out.println(val[0]);
		if (val[0] > val[1]) {
			this.jump();
		}

	}
	
	public void jump() {
		vy = -8;
	}

	public void setVivant(boolean b) {
		this.vivant = b;
	}

	public boolean getVivant() {
		return this.vivant;
	}

	public float getPosY() {
		return this.posY;
	}
	
	public float getPosX() {
		return this.posX;
	}

	public int getLongueur() {
		return this.longueurBird;
	}
	
	public int getLargeur() {
		return this.largeurBird;
	}

	public void setPoint(int i) {
		this.point = i;
	}

	public void setPosY(int i) {
		this.posY = i;
	}

	public void setPosX(int i) {
		this.posX = i;
	}

	public int getPoint() {
		return this.point;
	}
	
	public Reseau getLeCerveau() {
		return this.leCerveau;
	}

	//solo
	public Individu reproduction() {
		
		Individu enfant = new Individu();
		Reseau cerveau = enfant.getLeCerveau();
		
		cerveau.setValeurDesConnections(this.leCerveau.getValeurDesConnections());
		
		return enfant;
		
	}
	
	//couple
	public Individu reproduction(Individu indivdu2) {
		
		Individu enfant = new Individu();
		ArrayList<Double> connCerveauEnfant = new ArrayList<Double>();
		
		ArrayList<Double> connDucerveau = this.leCerveau.getValeurDesConnections();
		ArrayList<Double> connDucerveau2 = indivdu2.getLeCerveau().getValeurDesConnections();
		
		for (int i = 0; i < connDucerveau.size(); i++) {
			if (Math.random() > 0.5) {
				connCerveauEnfant.add(connDucerveau.get(i));
			}
			else {
				connCerveauEnfant.add(connDucerveau2.get(i));
			}
		}
	
		enfant.getLeCerveau().setValeurDesConnections(connCerveauEnfant);
		
		return enfant;
		
	}
	

	public void mutation() {
		
		this.getLeCerveau().setValeurRandomConnection();

	}


	public void printPoint() {
		System.out.print("Point: "+this.point+" / ");
	}
	

	public void setVx(int i) {
		this.vx = i;
	}

	public void setVy(int i) {
		this.vy = i;
	}
	

	public int compareTo(Individu autreIndividu) {
		return autreIndividu.getPoint() - this.point;
	}

}
