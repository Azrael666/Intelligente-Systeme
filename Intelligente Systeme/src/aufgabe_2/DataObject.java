package aufgabe_2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class DataObject {
	double[][] values;
	double[] colAverage;
	double[] colMinimum;
	double[] colMaximum;
	int rows = 0;
	int cols = 0;
	
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
	
}
