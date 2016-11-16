package aufgabe_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 
 * @author Alexandra Scheben, Dirk Teschner
 *
 */
public class Main {

	/**
	 * @param args
	 */
	
	/**
	 * Global variables
	 */
	static private String inputFileName = "Belegungszeiten.txt";
	
	/**
	 * Simulation variables
	 */
	static private int simulationCount = 10000;
	static private int daysToSimulate = 10;	
	
	/**
	 * Gym variables
	 */
	static private int lockerCount = 150;
	static private int rowCount = 2;
	
	static private String openingTime = "10:00:00";
	static private String closingTime = "22:00:00";
	static private String arrivalTime = "15:00:00";
	
	static private int customerFrequenzySeconds = 10;
	static private double customerProbability = 0.1;
	static private int changeTimeSeconds = 300;
	
	// true = out strategy, false = random strategy
	static boolean strategy = false;
	
	static private List<Integer> occupationTimes = new ArrayList<Integer>();
	static private List<Double> occupationProbabilitiesCumulated = new ArrayList<Double>();
	
	
	/**
	 * Converts an human readable String representation of a time to the corresponding seconds
	 *  
	 * @param time String that represents the time of the day
	 * @return given time in seconds
	 */
	public static int timeToSeconds(String time) {
		
		int hours   = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(3, 5));
		int seconds = Integer.parseInt(time.substring(6));
		
		return hours * 3600 + minutes * 60 + seconds;
	}
	
	/**
	 * Reads the occupation times and amounts from an input file, given in inputFileName
	 * Then calculates the according (cumulated) probabilities
	 * 
	 * @return List of cumulated probabilities
	 */
	public static void textToProbabilities() {
		
		List<Integer> occupationAmount = new ArrayList<Integer>();
		List<Double> occupationProbabilities = new ArrayList<Double>();

		int occupationAmountSum = 0;
		
		// reads the input file
		try{
			BufferedReader input = new BufferedReader(new FileReader(inputFileName));
			input.readLine(); // skip first row of headlines
			
			String line;
			while((line = input.readLine()) != null) {
				
				String[] elements = line.split(" ");
				int minutes = Integer.parseInt(elements[0]);
				occupationTimes.add(minutes);
				int frequenzy = Integer.parseInt(elements[1]);
				occupationAmount.add(frequenzy);
			}
			input.close();
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		// calculates the sum of all occurrences
		for(int i : occupationAmount) {
			occupationAmountSum += i;
		}
		
		// calculates the probabilities for each occupation time
		for(int i = 0; i < occupationAmount.size(); i++) {
			occupationProbabilities.add(((double)(occupationAmount.get(i) * 100) / occupationAmountSum) / 100);
		}
		
		// set the first cumulated probability to the first probability
		occupationProbabilitiesCumulated.add(occupationProbabilities.get(0));
		
		// calculate the cumulated probability for each occupation time
		for(int i = 1; i < occupationProbabilities.size(); i++) {
			occupationProbabilitiesCumulated.add(occupationProbabilitiesCumulated.get(i - 1) + occupationProbabilities.get(i));
		}
		
		// set last cumulatedProbability to 100%
		occupationProbabilitiesCumulated.set(occupationProbabilitiesCumulated.size()-1, 1.0);
		
		// round up last cumulatedProbability to 100%		
		//occupationProbabilitiesCumulated.set(occupationProbabilitiesCumulated.size()-1, (double)Math.round(occupationProbabilitiesCumulated.get(occupationProbabilitiesCumulated.size()-1)));
		
	}
	
	public static void main(String[] args) {
		
		// get system time for calculation of computing time
		long start = System.currentTimeMillis();
		
		// convert human readable configuration times into seconds for calculation
		int openingTimeSeconds = timeToSeconds(openingTime);
		int closingTimeSeconds = timeToSeconds(closingTime);
		int arrivalTimeSeconds = timeToSeconds(arrivalTime);
		
		// read file and get values
		textToProbabilities();
		
		// statistic variables
		int encountersFirst = 0;
		int encountersSecond = 0;
		int customers = 0;
		int customersAtFocusArrival = 0;
		int sumOfEncounters = 0;
		
		// necessary for calculation of variance and standard deviation
		Stack<Double> encountersAll = new Stack<Double>();
//		Stack<Integer> encountersPerSimALL = new Stack<Integer>();

		
		// loop for amount of simulations
		for(int n = 0; n < simulationCount; n++){
			
			// Initialize gym for each simulation
			Gym gym = new Gym(lockerCount, rowCount, openingTimeSeconds, closingTimeSeconds, arrivalTimeSeconds, changeTimeSeconds, customerFrequenzySeconds, customerProbability, occupationTimes, occupationProbabilitiesCumulated, strategy);
			
			
			int encounters = 0;
			
			// loop for days per simulation
			for(int i = 0; i < daysToSimulate; i++){
				
				// simulate day
				int encountersThisDay = gym.simulateDay(); 
				
				encounters += encountersThisDay;
				customersAtFocusArrival += gym.getCustomersAtFocusArrival();
			}
//			encountersPerSimALL.push(encounters);
			
			// statistics
			encountersAll.push(((double)encounters/daysToSimulate));
			sumOfEncounters += encounters;
			encountersFirst += gym.getEncountersFirst();
			encountersSecond += gym.getEncountersSecond();
			customers += gym.getCustomers();
		}
		
		// statistics	
		
		/*
		try(
		FileWriter fw = new FileWriter("Test.txt", true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw))
		{
			for(int i : encountersPerSimALL) {
				out.println(i);
			}
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		*/
		
		
		
		double encountersPerSimulation = (double)sumOfEncounters / simulationCount;
		
		Stack<Double> squareDeviation = new Stack<Double>();
		
		double n;
		for(int i = 0; i < encountersAll.size(); i++) {
			n = encountersAll.pop() - encountersPerSimulation;
			squareDeviation.push(Math.pow(n, 2));
		}
		
		double varianceSum = 0.0;
		int sizeOfStack = squareDeviation.size();
		
		for(int i = 0; i < squareDeviation.size(); i++) {
			varianceSum += squareDeviation.pop();
		}
		
		double variance = varianceSum / sizeOfStack;
		variance = Math.round(variance * 100.0) / 100.0;
		
		double standardDeviation = Math.sqrt(variance); 
		standardDeviation = Math.round(standardDeviation * 100.0) / 100.0;
		
		
		// calculation of 
		long end = System.currentTimeMillis();		
		long duration = end - start;	
		
		// output
		System.out.println("Variance: " + variance);
		System.out.println("Standard deviation: " + standardDeviation + "\n");
		

		System.out.println("Total encounters in " + (simulationCount * daysToSimulate) + " days: " + sumOfEncounters);
		System.out.println("Average encounters per simulation: " + encountersPerSimulation + "\n");
		
		System.out.println("Cumulated Encounters: " + (encountersFirst + encountersSecond));
		System.out.println("First encounters: " + encountersFirst);
		System.out.println("Second encounters: " + encountersSecond + "\n");
		
		System.out.println("Total customers in " + (simulationCount * daysToSimulate) + " days: " + customers);
		System.out.println("Average customers per day: " + ((double)customers / (simulationCount * daysToSimulate)) + "\n");
		
		System.out.println("Average customers in gym at focus person arrival: " + ((double)customersAtFocusArrival / (simulationCount * daysToSimulate)));

		System.out.println("Calculation time in ms: " + duration);

	}

}

