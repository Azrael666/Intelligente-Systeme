package aufgabe_2;

import java.awt.Point;
import java.util.List;


public class Main {
	
	/**
	 * Global variables
	 */
	static private DataObject data;
	static private LabelObject label;
	
	// File names
	static private String dataFileName = "data2.csv";
	static private String labelFileName = "label2.csv";
	
	// Main parameters
	static private boolean drawData = false;
	static private boolean safeImages = false;
	static private boolean printAdditionalStatistics = false;
	
	// Algorithm parameters
	static private double columnAverageRequirement = 0.0339;
	static private int sortedColumnIndexRequirement = 606;
	static private double columnMaximumPercentageRequirement = 0.9116;
	static private int maxiumNeighborhoodRequirement = 14;
	
	// FScore evaluation parameter
	static private int correctLabelRange = 10;
	
	// Get correct found local maxima
	private static int checkCorrectLabels(List<Point> labels, List<Point> maxima, int LabelRange) {
		int correctLabels = 0;
		
		// Check every label
		for(Point p : labels) {
			double pValue = data.values[p.x][p.y];
			
			// Check every found local Maxima
			for(Point m : maxima) {
				double mValue = data.values[m.x][m.y];
				
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
	
	// Checks, if 2 points are within given range (neighborhood)
	private static boolean isInRange(Point p1, Point p2, int range) {
		
		int x = p1.x - p2.x;
		int y = p1.y - p2.y;
		
		if(x >= - range && x <= range && y >= - range && y <= range)
			return true;
		
		return false;
	}
	
	// Calculate recall, precision & fScore and print related data to the console
	private static void calcFScore(int labelRange) {
		
		double recall = 0.0;
		double precision = 0.0;
		double fScore = 0.0;
		
		double actualLabels = label.points.size();
		double totalLabeled = data.localMaxima.size();
		double correctLabeled = 0;

		
		correctLabeled = checkCorrectLabels(label.points, data.localMaxima, labelRange);
		
		
		recall = correctLabeled / actualLabels;
		
		precision = correctLabeled / totalLabeled;
		
		fScore = 2 / (1 / recall + 1 / precision);
		
		System.out.println("\nTotal Labels: " + actualLabels);
		System.out.println("Maxima found: " + totalLabeled);
		System.out.println("Labels found: " + correctLabeled + "\n");
		System.out.println("Recall: " + recall);
		System.out.println("Precision: " + precision);
		System.out.println("FScore: " + fScore);
	}
	
	// Prints additional statistic data to the console
	private static void printStatistics() {
	
		double[] labelPercent = new double[label.points.size()];
		double labelPercentSum = 0.0;
		double labelPercentMin = Double.MAX_VALUE;
		double labelPercentMax = 0.0;
		
		// Calculate  label statistics
		for(int i = 0; i < label.points.size(); i++) {
			double value = data.values[label.points.get(i).x][label.points.get(i).y];
			double colAverage = data.colAverage[label.points.get(i).y];
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
		
		System.out.println("Data rows: " + data.rows);
		System.out.println("Data cols: " + data.cols);
		System.out.println("Total Data Count : " + data.valueCount);
		
		System.out.println("\nSum of Data Values : " + data.valueSum);
		System.out.println("Average Data Value: " + data.valueAverage);
		System.out.println("Minimum Data Value: " + data.valueMin);
		System.out.println("Maximum Data Value: " + data.valueMax);

		System.out.println("\nLabel value above col average in percent");
		System.out.println("Minimum: " + labelPercentMin);
		System.out.println("Maximum: " + labelPercentMax);
		System.out.println("Average: " + averageLabelPercent);
		
	}
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		data = new DataObject(dataFileName);
		data.evalData(columnAverageRequirement, sortedColumnIndexRequirement, columnMaximumPercentageRequirement, maxiumNeighborhoodRequirement);
		
		label = new LabelObject(labelFileName);
		
		if(drawData) {
			Drawing drawing = new Drawing(data, label, safeImages);
			drawing.draw();
		}
		
		long endTime = System.currentTimeMillis();
		long computingTime = endTime - startTime;
		
		if(printAdditionalStatistics)
			printStatistics();
		
		calcFScore(correctLabelRange);
		
		System.out.println("\nComputing Time: " + computingTime + "ms");
	}
	

}