package Statistique;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class FrameStats extends JFrame {

	private static final long serialVersionUID = 7395014470958008701L;
	
	private JFreeChart chartMoyenne;
	private JFreeChart chartMeilleurScore;
	
	private XYSeries seriesMoyenne;
	private XYSeries seriesMeilleurScore;
	private XYSeries seriesMoyenneDesMeilleursScore;
	private XYSeries seriesMoyenneDesMoyennes;
	
	private XYSeriesCollection datasetMoyenne;
	private XYSeriesCollection datasetMeilleurScore;

	public FrameStats() {
		this.seriesMoyenne = new XYSeries("Moyenne");
		this.seriesMeilleurScore = new XYSeries("Meilleur Score");
		this.seriesMoyenneDesMeilleursScore = new XYSeries("Moyenne des 25 derniers meilleures scores");
		this.seriesMoyenneDesMoyennes = new XYSeries("Moyenne des 25 dernières moyennes");
		
		this.datasetMoyenne = new XYSeriesCollection();
		this.datasetMoyenne.addSeries(this.seriesMoyenne);
		this.datasetMoyenne.addSeries(this.seriesMoyenneDesMoyennes);
		
		this.datasetMeilleurScore = new XYSeriesCollection();
		this.datasetMeilleurScore.addSeries(this.seriesMeilleurScore);
		this.datasetMeilleurScore.addSeries(this.seriesMoyenneDesMeilleursScore);

		this.chartMoyenne = ChartFactory.createXYLineChart("Moyenne score de la génération", "Numéro de la génération",
				"Score", this.datasetMoyenne, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel cp1 = new ChartPanel(this.chartMoyenne) {

			private static final long serialVersionUID = 3375830235868303812L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(640, 480);
			}
		};

		this.chartMeilleurScore = ChartFactory.createXYLineChart("Max score de la génération",
				"Numéro de la génération", "Score", this.datasetMeilleurScore, PlotOrientation.VERTICAL, true, true,
				false);

		ChartPanel cp2 = new ChartPanel(this.chartMeilleurScore) {

			private static final long serialVersionUID = -2375483610464033423L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(640, 480);
			}
		};


		this.setLayout(new GridLayout(2, 2));
		this.add(cp1);
		this.add(cp2);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public void addMoyenne(long moyenne) {
		this.seriesMoyenne.add(this.seriesMoyenne.getItemCount() + 1, moyenne);
		
		int nbLastIndividuForMoy = 25;
		long moyenneTotal = 0;
		int nbItem = this.seriesMoyenne.getItemCount();
		
		if (nbItem >= nbLastIndividuForMoy) {
			for (int i = nbItem - nbLastIndividuForMoy; i < nbItem - 1; i++) {
				moyenneTotal += (this.seriesMoyenne.getY(i).intValue() + this.seriesMoyenne.getY(i+1).intValue());
			}
			
			moyenneTotal /= 2;
			moyenneTotal /= nbLastIndividuForMoy;
			
			this.seriesMoyenneDesMoyennes.add(nbItem, moyenneTotal);
		}
		else if (nbItem > 1) {
			for (int i = 0; i < nbItem - 1; i++) {
				moyenneTotal += (this.seriesMoyenne.getY(i).intValue() + this.seriesMoyenne.getY(i+1).intValue());
			}
			
			moyenneTotal /= 2;
			moyenneTotal /= nbItem;
			
			this.seriesMoyenneDesMoyennes.add(nbItem, moyenneTotal);
		}
		
		
	}

	public void addMeilleurScore(int bestScore) {
		this.seriesMeilleurScore.add(this.seriesMeilleurScore.getItemCount() + 1, bestScore);

		int nbLastIndividuForMoy = 25;
		long moyenneTotal = 0;
		int nbItem = this.seriesMeilleurScore.getItemCount();
		
		if (nbItem >= nbLastIndividuForMoy) {
			for (int i = nbItem - nbLastIndividuForMoy; i < nbItem - 1; i++) {
				moyenneTotal += (this.seriesMeilleurScore.getY(i).intValue() + this.seriesMeilleurScore.getY(i+1).intValue());
			}
			
			moyenneTotal /= 2;
			moyenneTotal /= nbLastIndividuForMoy;
			
			this.seriesMoyenneDesMeilleursScore.add(nbItem, moyenneTotal);
		}
		else if (nbItem > 1) {
			for (int i = 0; i < nbItem - 1; i++) {
				moyenneTotal += (this.seriesMeilleurScore.getY(i).intValue() + this.seriesMeilleurScore.getY(i+1).intValue());
			}
			
			moyenneTotal /= 2;
			moyenneTotal /= nbItem;
			
			this.seriesMoyenneDesMeilleursScore.add(nbItem, moyenneTotal);
		}
	}

	public void recalibrer() {
		// RECALIBRAGE MOYENNE
		XYPlot xyPlot = (XYPlot) this.chartMoyenne.getPlot();

		double max = Double.MIN_VALUE;
		double mini = Double.MAX_VALUE;

		for (int i = 0; i < this.seriesMoyenne.getItemCount(); i++) {
			double x = this.seriesMoyenne.getY(i).doubleValue();
			if (x > max) {
				max = x;
			}
			else if (x < mini) {
				mini = x;
			}
		}

		ValueAxis axis = xyPlot.getRangeAxis();
		axis.setLowerBound(mini);
		axis.setUpperBound(max);

		// RECALIBRAGE MEILLEUR SCORE
		xyPlot = (XYPlot) this.chartMeilleurScore.getPlot();

		max = Integer.MIN_VALUE;
		mini = Integer.MAX_VALUE;

		for (int i = 0; i < this.seriesMeilleurScore.getItemCount(); i++) {
			int x = this.seriesMeilleurScore.getY(i).intValue();
			if (x > max) {
				max = x;
			}
			else if (x < mini) {
				mini = x;
			}
		}

		axis = xyPlot.getRangeAxis();
		axis.setLowerBound(mini);
		axis.setUpperBound(max);

	}

}
