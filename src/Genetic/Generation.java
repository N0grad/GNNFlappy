package Genetic;

import java.util.Arrays;

import Game.FlappyBird;

public class Generation {

	private Individu[] population;
	private final double CHANCE_DE_MUTER = 0.05;
	private final double POURCENTAGE_ELITE_RETENU = 0.2;
	private int nbGeneration = 1;

	public Generation (int nbIndividu) {
		this.population = new Individu[nbIndividu];	
		this.fillGeneration();
	}
	
	private void fillGeneration() {
		for (int i = 0; i < this.population.length; i++) {
			this.population[i] = new Individu();
		}
	}

	
	public void classement() {
		Arrays.sort(this.population);
	}
	
	public void nextGeneration() {
		
		System.out.println("\n---------------------------------------- ( Generation : "+this.nbGeneration+" ) ----------------------------------------\n");
		System.out.print("POINT : ");
		for (Individu bird : this.population) {
			System.out.print(bird.getPoint()+ " / ");
		}
		this.printMoyenneScore();

		
		int nombreEliteRetenu = (int) (this.population.length * this.POURCENTAGE_ELITE_RETENU);
		int nbDeMutation = (int) (this.population[0].getLeCerveau().getNbConnection() * this.CHANCE_DE_MUTER) + 1; //5% de mutation (+1)


		for (int i = 0; i < nombreEliteRetenu; i++) {
			Individu enfant = this.population[i].reproduction();
			
			for (int j = 0; j < nbDeMutation ; j++) {
				enfant.mutation();
			}
			
			this.population[i+nombreEliteRetenu] = enfant;
		}
		
		for (int i = nombreEliteRetenu * 2; i < this.population.length; i++) {
			this.population[i] = new Individu();
		}
		
		this.resetElite();
		
		this.setNbGeneration(this.nbGeneration + 1);
		
	}
	
	public boolean tousMorts() {
		
		boolean ret = true;
		int i = 0;
		
		while (i < this.population.length) {
			
			if (this.population[i].getVivant()) {
				ret = false;
				i = this.population.length;
			}
			
			i++;
		}
		
		return ret;
	
		
	}
	
	public Individu[] getPopulation() {
		return this.population;
	}
	
	public int getNombreVivant() {
		int ret = 0;
		
		for (int i = 0; i < this.population.length; i++) {
			if (this.population[i].getVivant()) {
				ret++;
			}
		}
		
		return ret;
	}

	public void resetElite() {
		int nombreEliteRetenu = (int) (this.population.length * this.POURCENTAGE_ELITE_RETENU);
		
		for (int i = 0; i < nombreEliteRetenu; i++) {
			this.population[i].setPoint(0);
			this.population[i].setPosX(FlappyBird.WIDTH / 2);
			this.population[i].setPosY(FlappyBird.HEIGHT / 2);
			this.population[i].setVx(0);
			this.population[i].setVy(0);

			this.population[i].setVivant(true);
		}
	}
	
	public void printMoyenneScore() {
		System.out.println("\nMOYENNE : "+this.getMoyenneScore());
	}
	
	public long getMoyenneScore() {
		long total = 0;
		
		for (int i = 0; i < this.population.length; i++) {
			total += this.population[i].getPoint();
		}
		
		return (total / this.population.length);
	}

	public double getCHANCE_DE_MUTER() {
		return this.CHANCE_DE_MUTER;
	}
	
	public double getPOURCENTAGE_ELITE_RETENU() {
		return this.POURCENTAGE_ELITE_RETENU;
	}

	public int getNbGeneration() {
		return this.nbGeneration;
	}

	public void setNbGeneration(int nbGeneration) {
		this.nbGeneration = nbGeneration;
	}

	public int getBestScore() {
		Arrays.sort(this.population);
		return this.population[0].getPoint();
	}
}
