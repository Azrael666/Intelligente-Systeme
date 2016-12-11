package aufgabe_2;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List; 	

public class DataObject {
	double[][] values;
	double[][] sortedColValues;
	double[] colAverage;
	double[] colMedian;
	double[] colMinimum;
	double[] colMaximum;
	List<Point> localMaxima;
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
			
			// Get data row count
			LineNumberReader  lnr = new LineNumberReader(new FileReader(new File(inputFileName)));
			lnr.skip(Long.MAX_VALUE);
			
			rows = lnr.getLineNumber();
			
			lnr.close();
			
			
			// Get data col count
			BufferedReader input = new BufferedReader(new FileReader(inputFileName));
			input.mark(10000);
	
			String firstLine = input.readLine();
			String[] elementCount = firstLine.split(",");
			cols = elementCount.length;
			
			// Initialize variables
			size = rows * cols;
			values = new double[rows][cols];
			sortedColValues = new double[cols][rows];
			colAverage = new double[cols];
			colMinimum = new double[cols];
			colMaximum = new double[cols];
			colMedian = new double[cols];
			localMaxima = new LinkedList<Point>();
			
			// Get data
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
			
			valueAverage = valueSum / valueCount;
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
	}
	
	// Get local maxima
	public void evalData(double columnAverage, int sortedColumnIndex, double columnMaximumPercentage, int maximumNeighborhoodRange) {
		
		calculateColAverage();
		
		getSortedValues();			
		
		getLocalMaxima(columnAverage, sortedColumnIndex, columnMaximumPercentage, maximumNeighborhoodRange);
		
		mergeLocalMaxima();
	}
	
	
	// Calculate col average
	public void calculateColAverage() {
		
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
	
	// Get value-sorted list for each col
	public void getSortedValues() {
		
		for(int i = 0; i < cols; i++) {
			List<Double> valueList= new LinkedList<Double>();
			for(int j = 0; j < rows; j++) {
				valueList.add(values[j][i]);
			}
			valueList.sort((Double a, Double b)->Double.compare(a, b));
			
			double[] temp = new double[valueList.size()];
			for(int k = 0; k < valueList.size(); k++)
				temp[k] = valueList.get(k);
			
			sortedColValues[i] = temp;
		}
	}
	
	// Get local maxima
	public void getLocalMaxima(double columnAverage, int sortedColumnIndex, double columnMaximumPercentage, int maximumNeighborhoodRange) {

		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				boolean isMaxima = true;
				double value = values[i][j];
				
				
				// If the local maximum is not at least <columnAverage> % above the column average, maximum is not valid
				if(isMaxima) {
				if(value < (colAverage[j] * (1 + columnAverage)))
					isMaxima = false;
				}
				
				// If the local maximum's value is not above the <sortedColumnIndex> th element's value, maximum is not valid
				if(isMaxima) {
				if(value < (sortedColValues[j][sortedColumnIndex]))
					isMaxima = false;
				}

				// If the local maximum's value is not at least <columnMaximumPercentage> % of col's maximum, maximum is not valid
				if(isMaxima) {
				if(value < (colMaximum[j]) * columnMaximumPercentage)
					isMaxima = false;
				}
				
				// If there is an bigger maximum within <maximumNeighborhoodRange> cells range, this maximum is not valid
				if(isMaxima) {
				if(!checkNeighbors(i, j, maximumNeighborhoodRange))
					isMaxima = false;
				}
				
				// Add point to localMaxima list, only if it is a local maximum
				if(isMaxima) {
					localMaxima.add(new Point(i, j));
				}
			}
		}
	}
	

	//Merge different local maxima, if all values between them have the same height
	public void mergeLocalMaxima() {
		
		List<Point> localMaximaTemp = new LinkedList<Point>();
		
		// Copy list
		for(Point p : localMaxima)
			localMaximaTemp.add(p);
		
		
		for(int i = 0; i < localMaxima.size(); i++) {
			Point p1 = localMaxima.get(i);
			double value1 = values[p1.x][p1.y]; 
			
			for(int j = 0; j < localMaximaTemp.size(); j++) {
				Point p2 = localMaximaTemp.get(j);
				double value2 = values[p2.x][p2.y];
				int distance1 = p1.x -  p2.x;
				int distance2 = p1.y - p2.y;
				distance1 = Math.abs(distance1);
				distance2 = Math.abs(distance2);
				
				if(value1 == value2 && (distance1 < 10 || distance2 < 10) && !(p1.equals(p2))) {
					localMaxima.remove(p2);
				}
			}
		}
	}
	
	// Check all neighbors within radius for bigger value
	public boolean checkNeighbors(int row, int col, int radius) {
		
		boolean isMaximum = true;
		double value = values[row][col];
		
		 for(int x = row-radius; x <= row+radius; x++){
			   for(int y = col-radius; y <= col+radius; y++){
				   if(x >= 0 && x < rows && y >= 0 && y < cols && !(x == row && y == col)){
					   if(values[x][y] > value ) {
						   isMaximum = false;
					   }
				   }
			   }
			}
		 return isMaximum;
	}
	
}
