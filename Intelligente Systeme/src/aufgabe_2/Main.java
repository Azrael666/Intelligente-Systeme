package aufgabe_2;

public class Main {
	
	/**
	 * Global variables
	 */
	static private String dataFileName = "data0.csv";
	static private String labelFileName = "label0.csv";
	static private DataObject data;
	static private LabelObject label;
	static private boolean safeImages = false;
	
	private static void calcFScore() {
		
		double recall = 0.0;
		double precision = 0.0;
		double fScore = 0.0;
		
		int actualLabels = label.points.size();
		int totalLabeled = data.localMaxima.size();
		int correctLabeled = 0;

		// TODO get correctly labeled labels
		
		
		
		recall = correctLabeled / actualLabels;
		
		precision = correctLabeled / totalLabeled;
		
		fScore = 2 / (1/recall + 1/precision);
		
		System.out.println("Recall: " + recall);
		System.out.println("Precision: " + precision);
		System.out.println("FScore: " + fScore);
	}
	
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		data = new DataObject(dataFileName);
		label = new LabelObject(labelFileName);
		
		data.draw(label.points, safeImages);
		
		double[] labelPercent = new double[label.points.size()];
		double labelPercentSum = 0.0;
		double labelPercentMin = Double.MAX_VALUE;
		double labelPercentMax = 0.0;
		
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
		
		calcFScore();
		
		long endTime = System.currentTimeMillis();
		long computingTime = endTime - startTime;
		
		
		System.out.println(data.colAverage[100]);
		System.out.println(data.colAverage[200]);
		System.out.println(data.colAverage[400]);
		System.out.println(data.colAverage[600]);
		System.out.println(data.colAverage[650]);
		System.out.println(data.colAverage[700]);
		System.out.println(data.colAverage[800]);
		System.out.println(data.colAverage[900]);
		
		
		System.out.println("Data rows: " + data.rows);
		System.out.println("Data cols: " + data.cols);
		System.out.println("Total Data Count : " + data.valueCount);
		System.out.println("Total Data Values : " + data.valueSum);
		System.out.println("Average Data Values : " + data.valueAverage);
		System.out.println("Minimum Data Value: " + data.valueMin);
		System.out.println("Maximum Data Value: " + data.valueMax);
		System.out.println("Found local maxima: " + data.localMaxima.size());
		System.out.println("Lowest label percent: " + labelPercentMin);
		System.out.println("Highest label percent: " + labelPercentMax);
		System.out.println("Average label percent above average: " + averageLabelPercent);
		
		System.out.println("Computing Time: " + computingTime + "ms");
	}
}