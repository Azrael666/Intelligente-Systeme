package aufgabe_2;

import java.awt.Point;

public class Main {
	
	/**
	 * Global variables
	 */
	static private String dataFileName = "data0.csv";
	static private String labelFileName = "label0.csv";
	static private boolean safeImages = false;
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		DataObject data = new DataObject(dataFileName);
		LabelObject label = new LabelObject(labelFileName);
		
		data.draw(safeImages);
		
		long endTime = System.currentTimeMillis();
		long computingTime = endTime - startTime;
	
		// Testen, ob der Durchschnittswert der Spalte, in der das Maximum liegt unter dem Maximum ist
		boolean test[] = new boolean[label.points.size()];
		int counterFalse = 0;
		for(int i = 0; i < label.points.size(); i++) {
			if(data.values[label.points.get(i).x][label.points.get(i).y] >=    data.colAverage[label.points.get(i).y]   ) test[i] = true;
			else {
				test[i] = false;
				counterFalse++;
			}
		}
		
		for(int i = 0; i < test.length; i++) {
			if(!test[i]) System.out.println(i + ": " + label.points.get(i));
		}
			
		int look = 0;
		Point point = label.points.get(look);
		System.out.println("Point " + look + ": " + point);
		System.out.println("Point x coordinate: " + point.x);
		System.out.println("Point y coordinate: " + point.y);
		System.out.println("Data Value of Point " + look + ": " + data.values[point.x][point.y]);
		System.out.println("Col " + point.y + " Average: " + data.colAverage[point.y]);
		System.out.println("Col " + point.y + " Minimum: " + data.colMinimum[point.y]);
		System.out.println("Col " + point.y + " Maximum: " + data.colMaximum[point.y]);
			
		System.out.println("Failed Tests: " + counterFalse + " / " +test.length);
		
		
		System.out.println("Data rows: " + data.rows);
		System.out.println("Data cols: " + data.cols);
		System.out.println("Total Data Count : " + data.valueCount);
		System.out.println("Total Data Values : " + data.valueSum);
		System.out.println("Average Data Values : " + data.valueAverage);
		System.out.println("Minimum Data Value: " + data.valueMin);
		System.out.println("Maximum Data Value: " + data.valueMax);
		
		System.out.println("Computing Time: " + computingTime + "ms");
		
	}
}