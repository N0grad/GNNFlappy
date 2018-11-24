package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Genetic.Generation;
import Genetic.Individu;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 261875679211664356L;
	
	private FlappyBird fb;
	private Generation generation;
	private Individu[] tabBirds;
	private ArrayList<Rectangle> rects;
	
	private Font scoreFont;
	public final Color bg = new Color(0, 158, 158);
	public static final int PIPE_W = 50, PIPE_H = 30;
	private Image imgPipeHead, imgPipeLength, imgBird;

	public GamePanel(FlappyBird fb, Generation generation, ArrayList<Rectangle> rects) {
		this.generation = generation;
		this.fb = fb;
		this.tabBirds = generation.getPopulation();
		this.rects = rects;
		this.scoreFont = new Font("Comic Sans MS", Font.BOLD, 18);

		try {
			this.imgPipeHead = ImageIO.read(new File("image/78px-Pipe.png"));
			this.imgPipeLength = ImageIO.read(new File("image/pipe_part.png"));
			this.imgBird = ImageIO.read(new File("image/bird.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		
		//Couleur du background du terrain + Affichage de l'oiseau
		g.setColor(bg);
		g.fillRect(0, 0, FlappyBird.WIDTH, FlappyBird.HEIGHT);
		for (Individu bird : tabBirds) {
			if (bird.getVivant()) {
				g.drawImage(this.imgBird, (int) bird.getPosX(), (int) bird.getPosY(), null);
			}
		}
		
		//Pour tous les tuyaux
		for (Rectangle r : rects) {
			Graphics2D g2d = (Graphics2D) g;
			
			AffineTransform old = g2d.getTransform();
			g2d.translate(r.x + PIPE_W / 2, r.y + PIPE_H / 2);
			
			//Si c'est le tuyaux du haut, on le retourne et on le place en haut
			if (r.y < FlappyBird.HEIGHT / 2) {
				g2d.translate(0, r.height);
				g2d.rotate(Math.PI);
			}
			
			//On affiche les images
			g2d.drawImage(imgPipeHead, -PIPE_W / 2, -PIPE_H / 2, GamePanel.PIPE_W, GamePanel.PIPE_H, null);
			g2d.drawImage(imgPipeLength, -PIPE_W / 2, PIPE_H / 2, GamePanel.PIPE_W, r.height, null);
			g2d.setTransform(old);
		}
		
		//Score
		g.setFont(scoreFont);
		g.setColor(Color.BLACK);
		g.drawString("Score: " + fb.getScore(), 10, 20);
		g.drawString("Generation n° : " + generation.getNbGeneration(), 10, 40);
		g.drawString("Vivants : " + generation.getNombreVivant(), 10, 60);
		
		/*g.drawString("Moyenne : "+generation.getMoyenneScore(), 10, 80);
		g.drawString("Chance de mutation : " + (int) (generation.getCHANCE_DE_MUTER() * 100) + " % ", 10, 100);
		g.drawString("Nombre d'élite retenu : " + (int) (generation.getPOURCENTAGE_ELITE_RETENU() * this.tabBirds.length), 10, 120);
		g.drawString("Nombre d'individu par gen : " + this.tabBirds.length, 10, 140);*/
		

	}
}
