package aufgabe_2;

import java.awt.Point;
import java.util.List;

/**
 * 
 * @author Alexandra Scheben, Dirk Teschner
 *
 */
public class Main {
	
	static private DataObject data;
	static private LabelObject label;
	
	// File names
	static private String[] dataFileName = {"data0.csv", "data1.csv", "data2.csv"} ;
	static private String[] labelFileName = {"label0.csv", "label1.csv", "label2.csv"} ;
	
	// Main parameters
	static private int calculateData = 2;
	static private boolean drawData = false;
	static private boolean safeImages = false;
	static private boolean printAdditionalStatistics = false;
	
	// Algorithm parameters
	static private double columnAverageRequirement = 0.0481;
	static private int sortedColumnIndexRequirement = 750;
	static private double columnMaximumPercentageRequirement = 0.96;
	static private int maxiumNeighborhoodRequirement = 15;
	
	// FScore evaluation parameter
	static private int correctLabelRange = 5;
	
	/**
	 * checks if labels found are correct labels
	 * @param labels labels
	 * @param maxima maxima found by our algorithm
	 * @param LabelRange how far a maxima can be from a lable to be viewed as correctly labeled
	 * @return number of correctly found labels
	 */
	private static int checkCorrectLabels(List<Point> labels, List<Point> maxima, int LabelRange) {
		int correctLabels = 0;
		
		// Check every label
		for(Point p : labels) {
			double pValue = data.getValues()[p.x][p.y];
			
			// Check every found local Maxima
			for(Point m : maxima) {
				double mValue = data.getValues()[m.x][m.y];
				
				// If the found local Maxima is within <LabelRange> range of the label
				// and it's value is greater or equal to the value of the label
				// then this label was found correctly
				if(isInRange(p, m, LabelRange) && pValue >= mValue)
				{
					correctLabels++;
					break;
				}
			}
		}
		
		return correctLabels;
	}
	
	/**
	 * Checks if 2 points are within given range (neighborhood)
	 * @param p1 first point
	 * @param p2 second point
	 * @param range
	 * @return true if points are in range, otherwise false
	 */
	private static boolean isInRange(Point p1, Point p2, int range) {
		
		int x = p1.x - p2.x;
		int y = p1.y - p2.y;
		
		if(x >= - range && x <= range && y >= - range && y <= range)
			return true;
		
		return false;
	}
	
	/**
	 * Calculates recall, precision & fScore and print related data to the console
	 * @param labelRange how far apart a maxima can be from a lable to be viewed as correctly labeled
	 */
	private static double[] calcFScore(int labelRange) {
		
		double recall = 0.0;
		double precision = 0.0;
		double fScore = 0.0;
		
		double actualLabels = label.getPoints().size();
		double totalLabeled = data.getLocalMaxima().size();
		double correctLabeled = 0;

		
		correctLabeled = checkCorrectLabels(label.getPoints(), data.getLocalMaxima(), labelRange);
		
		
		recall = correctLabeled / actualLabels;
		
		precision = correctLabeled / totalLabeled;
		
		fScore = 2 / (1 / recall + 1 / precision);
		
		System.out.println("\nTotal Labels: " + actualLabels);
		System.out.println("Maxima found: " + totalLabeled);
		System.out.println("Labels found: " + correctLabeled + "\n");
		System.out.println("Recall: " + recall);
		System.out.println("Precision: " + precision);
		System.out.println("FScore: " + fScore);
		
		double[] ret = {recall, precision, fScore};
		return ret;
	}
	
	/**
	 * Prints additional statistic data to the console
	 */
	private static void printStatistics() {
	
		double[] labelPercent = new double[label.getPoints().size()];
		double labelPercentSum = 0.0;
		double labelPercentMin = Double.MAX_VALUE;
		double labelPercentMax = 0.0;
		
		// Calculate  label statistics
		for(int i = 0; i < label.getPoints().size(); i++) {
			double value = data.getValues()[label.getPoints().get(i).x][label.getPoints().get(i).y];
			double colAverage = data.getColAverage()[label.getPoints().get(i).y];
			if(colAverage >= 530) {
				double labelPercentage = (value / colAverage)-1;
				if(labelPercentMin >= labelPercentage)
					labelPercentMin = labelPercentage;
				if(labelPercentMax <= labelPercentage)
					labelPercentMax = labelPercentage;
				labelPercentSum += labelPercentage;			
				labelPercent[i] = (labelPercentage);
			}
		}
		
		double averageLabelPercent = (labelPercentSum / (double)labelPercent.length);
		
		System.out.println("Data rows: " + data.getRows());
		System.out.println("Data cols: " + data.getCols());
		System.out.println("Total Data Count : " + data.getValueCount());
		
		System.out.println("\nSum of Data Values : " + data.getValueSum());
		System.out.println("Average Data Value: " + data.getValueAverage());
		System.out.println("Minimum Data Value: " + data.getValueMin());
		System.out.println("Maximum Data Value: " + data.getValueMax());

		System.out.println("\nLabel value above col average in percent");
		System.out.println("Minimum: " + labelPercentMin);
		System.out.println("Maximum: " + labelPercentMax);
		System.out.println("Average: " + averageLabelPercent);
		
	}
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		double[] eval = {0.0, 0.0, 0.0};
		int i;
		for(i = 0; i < calculateData; i++) {
			System.out.println("Data " + i + ":");
			data = new DataObject(dataFileName[i]);
			data.evalData(columnAverageRequirement, sortedColumnIndexRequirement, columnMaximumPercentageRequirement, maxiumNeighborhoodRequirement);
			
			label = new LabelObject(labelFileName[i]);
			
			if(drawData) {
				Drawing drawing = new Drawing(data, label, safeImages);
				drawing.draw();
			}
			
			if(printAdditionalStatistics)
				printStatistics();
			
			double[] scores = calcFScore(correctLabelRange);
			eval[0] += scores[0];
			eval[1] += scores[1];
			eval[2] += scores[2];
			
			System.out.println("----------\n");
		}
		
		for(int j = 0; j < eval.length; j++) {
			eval[j] /= i;
		}
		
		System.out.println("Average Recall: " + eval[0]);
		System.out.println("Average Precsion: " + eval[1]);
		System.out.println("Average FScore: " + eval[2]);
		
		
		long endTime = System.currentTimeMillis();
		long computingTime = endTime - startTime;
		
		
		
		System.out.println("\nComputing Time: " + computingTime + "ms");
	}
	

}