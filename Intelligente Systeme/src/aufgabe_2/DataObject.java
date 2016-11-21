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
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;

public class DataObject {
	double[][] values;
	double[] colAverage;
	double[] colMinimum;
	double[] colMaximum;
	int rows = 0;
	int cols = 0;
	int size = 0;
	
	int valueCount = 0;
	double valueMin = Double.MAX_VALUE;
	double valueMax = Double.MIN_VALUE;
	double valueSum = 0;
	double valueAverage = 0;
	
	public DataObject(String inputFileName) {
		
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
			
			size = rows * cols;
			
			values = new double[rows][cols];
			colAverage = new double[cols];
			colMinimum = new double[cols];
			colMaximum = new double[cols];
			
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
			
			valueAverage = valueSum / valueCount;
			
			// Calculate col Average
			for(int i = 0; i < cols; i++) {
				double colSum = 0;
				double colMin = Double.MAX_VALUE;
				double colMax = 0;
				for(int j = 0; j < rows; j++) {
					double valueTemp = values[j][i];
					colSum += valueTemp;
					if(colMin >= valueTemp) colMin = valueTemp;
					if(colMax <= valueTemp) colMax = valueTemp;
				}
				double currentColAverage = colSum / (double)rows;
				colAverage[i] = currentColAverage;
				colMinimum[i] = colMin;
				colMaximum[i] = colMax;
			}
	}
	
	public void draw(boolean safeImages) {
		
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
	
}
