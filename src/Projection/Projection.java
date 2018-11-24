package Projection;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import NeuralNetwork.Reseau;

public class Projection {

	public static void writeAndLoad(Reseau cerveau, double minAbscisse, double maxAbscisse, double minOrdonnee, double maxOrdonnee) {

		System.out.println("Projection : ");
		
		BufferedImage imageTest = new BufferedImage((Math.abs((int)minOrdonnee) + Math.abs((int)maxOrdonnee)),
													(Math.abs((int)minAbscisse) + Math.abs((int)maxAbscisse)), 
													BufferedImage.TYPE_INT_RGB);
		
		String pathFinal = "C:\\Users\\John_DOE\\eclipse-workspace\\GNNFlappy\\src\\Image\\test.png";
		
		for (int i = 0; i < imageTest.getWidth(); i++) {
			for (int j = 0; j < imageTest.getHeight(); j++) {

				double[] val = new double[] { (j - maxAbscisse) / maxAbscisse, i / maxOrdonnee};

				cerveau.setValeurEntree(val);
				cerveau.propagationAvant();

				double[] sortie = cerveau.getValeurSortie();
				Color color = null;

				if (sortie[0] > sortie[1]) {
					color = new Color(0, 0, 255); //BLUE = JUMP
				} else {
					color = new Color(255, 0, 0);
				}

				imageTest.setRGB(i, j, color.getRGB());
			}

		}

		// write final image
		File file = null;
		try {
			 file = new File(pathFinal);
			 ImageIO.write(imageTest, "png", file);
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}

		if (file != null) {
			try {
				 Desktop.getDesktop().browse(file.toURI());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("FIN");
	}
}