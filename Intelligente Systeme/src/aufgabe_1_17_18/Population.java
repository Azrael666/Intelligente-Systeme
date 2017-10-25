package aufgabe_1_17_18;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Population {

	private List<Individual> individuals;
	private Disease disease;
	
	private double[][] encounterProbabilities;
	private double transmissionProbability;
	private int diseaseLength;
	
	
	private int epidemicLength;
	private double percentSick;
	
	private Random random;
	
	
	public Population(double[][] encounterProbabilities, double transmissionProbability, int diseaseLength) {
		this.encounterProbabilities = encounterProbabilities;
		this.transmissionProbability = transmissionProbability;
		this.diseaseLength = diseaseLength;
		
		random = new Random();
		
		disease = new Disease(transmissionProbability, diseaseLength);
		initIndividuals();
	}
	
	
	private void initIndividuals() {
		
		individuals = new LinkedList<Individual>();
		int individualCount = encounterProbabilities.length;
		
		for(int i = 0; i < individualCount; i++) {
			Individual individual = new Individual(i);
			individuals.add(individual);
		}
		
		int randomIndividual = random.nextInt(individuals.size()-1);
		
		individuals.get(randomIndividual).setSick(true);
	}
	
	public void simulate() {
		
		
		// simulate days
		
		// check end of simulation - no sick individuals
		
		// calculate results
	}
	
	public int getResults() {
		
		return 0;
	}
}
