package aufgabe_2;

import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;


public class Main {
	
	/**
	 * Global variables
	 */
	static private String dataFileName = "data0.csv";
	static private String labelFileName = "label0.csv";
	static private boolean safeImages = false;

	
	private static void drawData(double[][] array, int rows, int cols, double minValue, double maxValue) {
				
		int size = rows * cols;
		
		int x;
		int y;
		double z;
		Coord3d[] points = new Coord3d[size];

		int coordCounter = 0;
		
		// Create scatter points
		for(int i=0; i<rows; i++){
			for(int j = 0; j < cols; j++) {
				x = i;
			    y = j;
			    z = array[i][j];
			    points[coordCounter] = new Coord3d(x, y, z);
			    coordCounter++;
			}
		    
		}
		
		// Create a drawable scatter with a colormap
		ColorMapRainbow colorMapRainbow = new ColorMapRainbow();
		ColorMapper colorMapper = new ColorMapper(colorMapRainbow , (int)minValue, (int)maxValue);
		MultiColorScatter scatter = new MultiColorScatter( points, colorMapper);
		
		// Create a chart and add scatter
		Chart chart = new Chart();
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getScene().add(scatter);
		chart.getView().rotate(new Coord2d(0.0, -0.5), true);
		ChartLauncher.openChart(chart);
		
		// Safe Images
		if(safeImages) {
			for(int i = 0; i < 63; i++) {
				Coord2d rotateCoord= new Coord2d(0.1, 0.0);
				chart.getView().rotate(rotateCoord, true);
				
				File outputfile = new File("Images\\Data0\\image" + i + ".jpg");
				try {
					ImageIO.write(chart.screenshot(), "jpg", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		System.out.println("Barycentre: " + scatter.getBarycentre());
		
	}
	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		DataObject data = new DataObject(dataFileName);
		LabelObject label = new LabelObject(labelFileName);
		
		drawData(data.values, data.rows, data.cols, data.valueMin, data.valueMax);
		
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
