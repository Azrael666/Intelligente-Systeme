package aufgabe_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import javax.imageio.ImageIO;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;


public class Main {
	
	/**
	 * Global variables
	 */
	static private String inputFileName = "data1.csv";

	
	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		
		double[][] values = new double[0][0];
		int rows = 0;
		int cols = 0;
		
		int valueCount = 0;
		double valueMin = 1000;
		double valueMax = 0;
		double valueSum = 0;
		double valueAverage = 0;
		
		try{						
			
			LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(inputFileName)));
			lnr.skip(Long.MAX_VALUE);
			
			rows = lnr.getLineNumber();
			
			lnr.close();
			
			
			BufferedReader input = new BufferedReader(new FileReader(inputFileName));
			input.mark(10000);

			String firstLine = input.readLine();
			String[] elementCount = firstLine.split(",");
			cols = elementCount.length;
			
			values = new double[rows][cols];
			
			input.reset();
			for(int i = 0; i < rows; i++) {
				String line = input.readLine();
				String[] elements = line.split(",");
				for(int j = 0; j < cols; j++) {
					Double value = Double.parseDouble(elements[j]);
					values[i][j] = value;
					valueCount++;
					valueSum += value;
					if(valueMin >= value) valueMin = value;
					if(valueMax <= value) valueMax = value;
				}
			}
			input.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		int size = rows*cols;

		
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
			    z = values[i][j];
			    points[coordCounter] = new Coord3d(x, y, z);
			    coordCounter++;
			}
		    
		}
		
		// Create a drawable scatter with a colormap
		ColorMapRainbow colorMapRainbow = new ColorMapRainbow();
		ColorMapper colorMapper = new ColorMapper(colorMapRainbow , (int)valueMin, (int)valueMax);
		MultiColorScatter scatter = new MultiColorScatter( points, colorMapper);
		

		// Create a chart and add scatter
		Chart chart = new Chart();
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getScene().add(scatter);		
		ChartLauncher.openChart(chart);
		
		
//		File outputfile = new File("image.jpg");
//		try {
//			ImageIO.write(chart.screenshot(), "jpg", outputfile);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		valueAverage = valueSum / valueCount;
		long endTime = System.currentTimeMillis();
		long computingTime = endTime - startTime;
		
		System.out.println("Data rows: " + rows);
		System.out.println("Data cols: " + cols);
		System.out.println("Total Data Count : " + valueCount);
		System.out.println("Total Data Values : " + valueSum);
		System.out.println("Average Data Values : " + valueAverage);
		System.out.println("Minimum Data Value: " + valueMin);
		System.out.println("Maximum Data Value: " + valueMax);
		System.out.println("Barycentre: " + scatter.getBarycentre());
		System.out.println("Computing Time: " + computingTime + "ms");
		
	}
}
