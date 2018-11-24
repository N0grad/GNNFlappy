package Game;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import Genetic.Generation;
import Genetic.Individu;
import Projection.Projection;
import Statistique.FrameStats;

public class FlappyBird extends KeyAdapter implements ActionListener{

	public static final int WIDTH = 640, HEIGHT = 480;
	
	private final int NBJOUEUR = 300;
	private final int CONDITIONARRET = 200000000;
	
	private Generation lesIndividus;
	private Individu[] tabBirds;
	
	private JFrame frame;
	private JPanel panel;
	private ArrayList<Rectangle> rects;
	private int time, scroll = 0;
	private Timer t;
	
	private FrameStats chart;

	public static void main(String[] args) {

		//Lancement du jeu
		new FlappyBird().go();

	}
	
	public void go() {
		
		//Création du tableau graphique des stats
		this.chart = new FrameStats();
		
		//Creation Frame
		this.frame = new JFrame("Flappy Bird - Neuroevolution");
		this.frame.setSize(FlappyBird.WIDTH, FlappyBird.HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Generation des individus
		this.lesIndividus = new Generation(NBJOUEUR);
		this.tabBirds = this.lesIndividus.getPopulation();
		
		//ArrayList contenant les tuyaux
		this.rects = new ArrayList<Rectangle>();
		
		//Panel (parametre = frame, oiseau, les tuyaux)
		this.panel = new GamePanel(this, this.lesIndividus, this.rects);
		
		//Ajout du panel et du listener dans la frame
		this.frame.add(this.panel);
		this.frame.addKeyListener(this);
		
		//Afichage
		this.frame.setVisible(true);

		//Création et run du timer (1000 secondes divisé par 60 frames par secondes)
		t = new Timer(10, this);
		t.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		//Si une distance de 90px a été parcouru on ajoute deux nouveaux rectangles (haut et bas)
		if (scroll % 90 == 0) {
			Rectangle r = new Rectangle(WIDTH, 0, GamePanel.PIPE_W, (int) (( Math.random() * HEIGHT) / 5f + (0.175f) * HEIGHT));
			int h2 = (int) (( Math.random() * HEIGHT) / 5f + (0.175f) * HEIGHT);
			Rectangle r2 = new Rectangle(WIDTH, HEIGHT - h2, GamePanel.PIPE_W, h2);
			rects.add(r);
			rects.add(r2);
		}
		

		Rectangle lePlusProcheRectHaut = null;
		Rectangle lePlusProcheRectBas = null;
		
		//On prend les premiers devant 
		for (int i = 0; i < rects.size(); i+=2) {
			Rectangle r = rects.get(i);
			//si il est devant lui
			if (r.getMaxX() > tabBirds[0].getPosX()) {
			
				lePlusProcheRectHaut = r;
				lePlusProcheRectBas = rects.get(i+1);
				break;
			
			}
		}
		
		
		//On regarde les autres
		for (int i = 0; i < rects.size(); i+=2) {
			Rectangle r = rects.get(i);
			//si il est devant lui
			if (r.getMaxX() > tabBirds[0].getPosX()) {
				
				//si c'est le PLUS PROCHE (devant lui)
				if (r.getMaxX() < lePlusProcheRectHaut.getMaxX()) {
					lePlusProcheRectHaut = r;
					lePlusProcheRectBas = rects.get(i+1);
				}
			}
		}
		
		//RECUPERE la distance horizontal entre l'oiseau et le trou le plus proche + normalisation
		double distanceHorizontal = lePlusProcheRectHaut.getMaxX() - tabBirds[0].getPosX();
		distanceHorizontal /= 370.0;

		//Variable dans le calcul de la différence de hauteur (millieu de l'oiseau et millieu du trou)
		double milieuDuTrou = (lePlusProcheRectBas.getY() + lePlusProcheRectHaut.getMaxY()) / 2.0;
		
		//L'oiseau joue puis on lui applique la gravité
		for (Individu bird : tabBirds) {
			if (bird.getVivant()) {
				
				//Calcul la différence de hauter (milieu oiseau - milieu trou) + normalisation
				double differenceDeHauteur = bird.getPosY() + (bird.getLargeur() / 2.0) - milieuDuTrou;
				differenceDeHauteur /= 269.5;

				bird.joue(differenceDeHauteur, distanceHorizontal);
				bird.physics();
			}
		}
		

		//Update du panel
		this.panel.repaint();

		//ArrayList contenant les rectangles à enlever
		ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
		
		//Pour tous les rectangles présents dans la liste des tuayx, on les fait reculer de 3 px :
		//- Si le tuyaux est hors champs, on l'ajotue dans ce à enlever
		//- Si le tuyaux contient l'oiseau, boolean true de fin de partie	
		for (Rectangle r : rects) {
			r.x -= 3;
			if (r.x + r.width <= 0) {
				toRemove.add(r);
			}
			
			else {

				//si l'oiseau est disposé de façon qu'il peut toucher l'un des deux tuyaux --> On check TOUS les birds vivants
				Individu unBirdCheck = this.tabBirds[0];
				if (unBirdCheck.getPosX() + (unBirdCheck.getLongueur() / 1.2) >= r.getX() && unBirdCheck.getPosX() <= r.getMaxX()) {
					for (Individu bird : tabBirds) {
						
						if (bird.getVivant()) {
								
							//check si il a touché le tuyau du haut
							if (r.getY() == 0) {
								if ((bird.getPosY() - 25) < r.getMaxY()) {
									bird.setVivant(false);
									bird.setPoint(this.getScore() + Math.abs((int) (lePlusProcheRectHaut.getMaxY() - bird.getPosY())));
								}
							}
							//check si il a touché le tuyau du bas
							else if((bird.getPosY() + bird.getLargeur()) > r.getY()) {
								bird.setVivant(false);
								bird.setPoint(this.getScore() + Math.abs((int) (lePlusProcheRectBas.getY() - bird.getPosY())));
							}
							
							if (bird.getPoint() > CONDITIONARRET) {
								System.out.println("Arrêt simulation - Max Score : "+bird.getPoint());
								System.exit(1);
							}
						}
					}
				}
			}
		}

		//check si il s'est mangé le plafond ou le sol
		for (Individu bird : tabBirds) {
			
			if (bird.getVivant()) {
				
				if (bird.getPosY() + bird.getLargeur() > HEIGHT || bird.getPosY() < 0) {
					
					bird.setVivant(false);
					bird.setPoint(this.getScore());
				}
			}
		}

		//On enlève les tuyaux hors champ
		rects.removeAll(toRemove);
		
		//Incrémentation du temps et de la distance parcouru
		time+=1000;
		scroll++;
		
		//Si tous morts, on reste : on enlève tous les rectangles, on fait la prochaine gen, on reset le tps et distance parcouru
		if (lesIndividus.tousMorts()) {
			rects.clear();
			this.chart.addMoyenne(this.lesIndividus.getMoyenneScore());
			this.chart.addMeilleurScore(this.lesIndividus.getBestScore());
			this.chart.recalibrer();
			this.lesIndividus.nextGeneration();
			time = 0;
			scroll = 0;
		}

		
	}

	public int getScore() {
		return this.time;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_RIGHT) {
			if (t.getDelay() > 9) {
				t.setDelay( t.getDelay() - 10);
			}
		}
		else if (keyCode == KeyEvent.VK_LEFT) {
			t.setDelay( t.getDelay() + 10);
		}
	}

}
