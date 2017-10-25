package aufgabe_1_17_18;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

	static private String inputFileName = "Begegnungswahrscheinlichkeiten.txt";
	
	static private double[][] encounterProbabilities;
	
	public static void main(String[] args) {
		
		encounterProbabilities = textToProbabilities();
		
		printProbabilities();
	}
	
	private static void printProbabilities() {
		for(double[] list : encounterProbabilities) {
			for(double probability : list) {
				System.out.print(probability + "	");
			}
			System.out.println();
		}
	}
	
	private static double[][] textToProbabilities() {
		
		List<List<Double>> probabilityRows = new LinkedList<List<Double>>();
		
		try{
			BufferedReader input = new BufferedReader(new FileReader(inputFileName));
			
			String line;
			while((line = input.readLine()) != null) {

				List<Double> probabilities = new LinkedList<Double>();
				
				String[] elements = line.split("	");
				
				for(int i = 0; i < elements.length; i++) {
					double probability = Double.parseDouble(elements[i]);
					probabilities.add(probability);
				}
				
				probabilityRows.add(probabilities);
				
			}
			input.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		double[][] probabilities = new double[probabilityRows.size()][probabilityRows.get(0).size()];
		
		for(int i = 0; i < probabilityRows.size(); i++) {
			
			for(int j = 0; j < probabilityRows.get(i).size(); j++) {
				
				probabilities[i][j] = probabilityRows.get(i).get(j);
				
			}
		}
		
		return probabilities;
	}
	
	
}
