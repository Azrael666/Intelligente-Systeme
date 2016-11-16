package aufgabe_1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Alexandra Scheben, Dirk Teschner
 *
 */
public class Gym {

	private int lockerCount;
	private int rowCount;
	private int lockersPerRow;
	private Locker[][] lockers;
	private List<Locker> lockersFree;
	private List<Locker> lockersOccupied;
	private int openingTimeSeconds;
	private int closingTimeSeconds;
	private int arrivalTimeSeconds;
	private int changeTimeSeconds;
	private int customerFrequenzy;
	private int changeTimeTicks;
	private double customerProbability;
	private List<Integer> occupationTimes;
	private List<Double> occupationTimeProbabilities;
	private boolean strategy;
	private Random rand;
	
	private int encountersFirst = 0;
	private int encountersSecond = 0;
	private int customers = 0;
	private int customersAtFocusArrival = 0;
	
	/**
	 * Constructor
	 * @param lc
	 * @param rc
	 * @param ot
	 * @param ct
	 * @param at
	 * @param cts
	 * @param cf
	 * @param cp
	 * @param otp
	 */
	public Gym (int lc, int rc, int ot, int ct, int at, int cts, int cf, double cp, List<Integer> ot_, List<Double> otp, boolean strat) {
		lockerCount = lc;
		rowCount = rc;		
		openingTimeSeconds = ot;
		closingTimeSeconds = ct;
		arrivalTimeSeconds = at;
		changeTimeSeconds = cts;
		customerFrequenzy = cf;
		changeTimeTicks = changeTimeSeconds / customerFrequenzy;
		customerProbability = cp;
		occupationTimes = ot_;
		occupationTimeProbabilities = otp;
		strategy = strat;
		rand = new Random();
		initLockers();
	}
	
	
	/**
	 * Adds adjacent lockers as neighbors and puts every locker into the free locker list
	 */
	private void initLockers() {
		
		lockersPerRow = lockerCount / rowCount;
		lockers = new Locker[rowCount][lockersPerRow];
		
		// initiate the locker lists
		lockersFree = new LinkedList<Locker>();
		lockersOccupied = new ArrayList<Locker>();
		
		// initiate lockers array
		for(int i = 0; i < rowCount; i++) {
			for(int j = 0; j < lockersPerRow; j++) {
				lockers[i][j] = new Locker();
			}
		}		
		
		// Set neighbors for each locker and add them to the freeLocker list
		for(int i = 0; i < rowCount; i++) {
			for(int j = 0; j < lockersPerRow; j++) {				
				List<Locker> neighbors = new LinkedList<Locker>();
				
				if(i == 0) { 								// top row
					
					if(j == 0) {							// first locker
						neighbors.add(lockers[i][j+1]);		// right neighbor
						neighbors.add(lockers[i+1][j]);		// bottom neighbor
						neighbors.add(lockers[i+1][j+1]);	// bottom right neighbor
					}
					else if(j == lockersPerRow-1) {			// last locker
						neighbors.add(lockers[i][j-1]);		// left neighbor
						neighbors.add(lockers[i+1][j]);		// bottom neighbor
						neighbors.add(lockers[i+1][j-1]);	// bottom left neighbor
					}
					else {				
						neighbors.add(lockers[i][j-1]); 	// left neighbor
						neighbors.add(lockers[i][j+1]); 	// right neighbor
						neighbors.add(lockers[i+1][j-1]);   // bottom left neighbor
						neighbors.add(lockers[i+1][j]); 	// bottom neighbor
						neighbors.add(lockers[i+1][j+1]);   // bottom right neighbor
					}
				} 
				else if(i == rowCount-1) { 					// bottom row
					
					if(j == 0) {							// first locker
						neighbors.add(lockers[i][j+1]);		// right neighbor
						neighbors.add(lockers[i-1][j]);		// top neighbor
						neighbors.add(lockers[i-1][j+1]);	// top right neighbor
					}
					else if(j == lockersPerRow-1) {			// last locker
						neighbors.add(lockers[i][j-1]);		// left neighbor
						neighbors.add(lockers[i-1][j]);		// top neighbor
						neighbors.add(lockers[i-1][j-1]);	// top left neighbor
					}
					else {
						neighbors.add(lockers[i][j-1]); 	// left neighbor
						neighbors.add(lockers[i][j+1]); 	// right neighbor
						neighbors.add(lockers[i-1][j-1]);   // top left neighbor
						neighbors.add(lockers[i-1][j]); 	// top neighbor
						neighbors.add(lockers[i-1][j+1]);   // top right neighbor
					}
					
				}
				lockers[i][j].setNeighbors(neighbors);
				lockersFree.add(lockers[i][j]);				// all lockers are free
			}
			
		}
		
	}
	
	/**
	 * Calculates if a new customer arrives according to the customerProbability
	 * @return true if a new customer arrives, otherwise false
	 */
	private boolean newCustomer() {
				
		double prob = rand.nextDouble();		
		return prob < customerProbability;
	}
	
	/**
	 * Checks if there is a free locker and returns it
	 * @return if the lockersFree list contains at least 1 locker, the first locker is returned, otherwise null
	 */
	private Locker lookForFreeLocker() {
		Locker ret = null;
		if(lockersFree.size() > 0) {
			int random = rand.nextInt(lockersFree.size());
			ret = lockersFree.get(random);
		}
		
		return ret;
	}
	
	/**
	 * Our Strategy to find the best locker
	 * @param start arrival time in ticks of the new customer
	 * @return best locker for given time
	 */
	private Locker lookForBestLocker(int start) {
		
		// loop for each free locker
		for(Locker l : lockersFree) {
			// assume all neighbors are free (there would be no encounter)
			boolean free = true;
			
			// loop for each neighbor
			for(Locker n : l.getNeighbors()) {
				// if neighbor is occupied
				if(n.isOccupied()) {
					// if neighbor arrived less than <neccessary changing time> (5 minutes) ago, we assume, the locker is currently in use
					if(n.getOccupationTimeStart() >= (start - changeTimeTicks)) {
						free = false;
						break;
					}
				}
				
			}
			// if there would be no encounter in all neighbors, return current free locker
			if(free) return l;
		}
		// if there is no possible optimization, take an random free locker
		return lookForFreeLocker();
	}
	
	/**
	 * calculates the occupation time of a customer
	 * @return int value of occupation time in minutes
	 */
	private int calculateOccupationTime() {
		int ret = 0;
		
		double prop = rand.nextDouble();
		
		for(int i = 0; i < occupationTimeProbabilities.size(); i++) {
			if(prop < occupationTimeProbabilities.get(i)) {
				ret =  occupationTimes.get(i);
				break;
			}
		}
		
		return ret;
	}
	
	public int getEncountersFirst() {
		return encountersFirst;
	}
	
	public int getEncountersSecond() {
		return encountersSecond;
	}
	
	public int getCustomers() {
		return customers;
	}
	
	public int getCustomersAtFocusArrival() {
		return customersAtFocusArrival;
	}
	
	
	
	/**
	 * Simulates an day in the gym
	 * @return value of encounters of the focus person this day
	 */
	public int simulateDay() {
		
		// initialise lockers and set everyone to free
		initLockers();
		
		int openingTime = (closingTimeSeconds - openingTimeSeconds) / customerFrequenzy;
		int arrivalTime = (arrivalTimeSeconds - openingTimeSeconds) / customerFrequenzy;
		int occupationTimeTicks = 0;
		
		
		
		boolean focusPerson = false;
		boolean focusPersonIsPresent = false;
		Locker focusPersonLocker = null;
		boolean encounterFirst = false;
		boolean encounterSecond = false;
		
		customersAtFocusArrival = 0;
		
		for(int currentTick = 0; currentTick < openingTime; currentTick++) {
			
			// check starting and ending of changing times and occupation time
			for(int i = 0; i < lockersOccupied.size(); i++) {
				Locker temp = lockersOccupied.get(i);
				
				// check end of first changing time
				if(temp.isInUse() && (currentTick == temp.getOccupationTimeStart() + changeTimeTicks)) {
					temp.setInUse(false);
				}
				
				// check begin of second changing time
				if(currentTick == temp.getOccupationTimeEnd() - changeTimeTicks ) {
					temp.setInUse(true);
				}
				
				// check end of occupation time
				if(currentTick == temp.getOccupationTimeEnd()) {
					temp.freeLocker();
					lockersOccupied.remove(i);
					lockersFree.add(temp);
					customersAtFocusArrival--;
//								customers--;
				}
			}
			

			if(focusPerson && !focusPersonLocker.isOccupied()) {
				focusPersonIsPresent = false;
				
				// stop simulation if focus person leaves the gym
				break;
			}
						
			// new customer stuff
			if(newCustomer()) {
				
				customers++;
				customersAtFocusArrival++;
				
				int occupationTime = calculateOccupationTime();
				occupationTimeTicks = occupationTime * 60 / customerFrequenzy;
				
				Locker currentLocker;
				
				if(strategy) {
					currentLocker = lookForBestLocker(currentTick);
				} 
				else {
					currentLocker = lookForFreeLocker();
				}
				
				if(currentLocker != null) {
				
					currentLocker.occupieLocker(currentTick, (currentTick + occupationTimeTicks));
					lockersFree.remove(currentLocker);
					lockersOccupied.add(currentLocker);
					
					// arrival of focus person
					if(currentTick >= arrivalTime && !focusPerson) {
						
						focusPerson = true;
						focusPersonIsPresent = true;
						focusPersonLocker = currentLocker;
					}
				}
				else {
					customers--;
					customersAtFocusArrival--;
				}
				
			}
			
			
			// if the focus person is currently using the locker
			if(focusPerson && focusPersonIsPresent && focusPersonLocker.isInUse()) {
				
				// check for first encounter
				if(currentTick <= focusPersonLocker.getOccupationTimeStart() + changeTimeTicks)  {
					if(focusPersonLocker.neighborsInUse())
						encounterFirst = true;
				}
				// check for second encounter
				if(currentTick >= focusPersonLocker.getOccupationTimeEnd() - changeTimeTicks) {
					if(focusPersonLocker.neighborsInUse())
						encounterSecond = true;
				}
			}
			
		}
		
		int encounters = 0;
		
		if(encounterFirst) {
			encountersFirst++;
			encounters++;
		}
		if(encounterSecond) {
			encountersSecond++;
			encounters++;
		}
		
		return encounters;
	}
	
}
