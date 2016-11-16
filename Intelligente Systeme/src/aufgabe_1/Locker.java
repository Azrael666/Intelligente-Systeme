package aufgabe_1;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Alexandra Scheben, Dirk Teschner
 *
 */
public class Locker {

	private List<Locker> neighbors;
	private int occupationTimeStart;
	private int occupationTimeEnd;
	private boolean occupied;
	private boolean inUse;
	
	
	public Locker() {
		this.inUse = false;
		this.occupied = false;
		this.neighbors = new ArrayList<Locker>();
	}
	
	public void occupieLocker(int start, int end){
		this.occupationTimeStart = start;
		this.occupationTimeEnd = end;
		this.occupied = true;
		this.inUse = true;
	}
	
	public void freeLocker(){
		this.occupationTimeStart = 0;
		this.occupationTimeEnd = 0;
		this.occupied = false;
		this.inUse = false;
	}
	
	public List<Locker> getNeighbors() {
		return this.neighbors;
	}
	
	public int getOccupationTimeStart() {
		return this.occupationTimeStart;
	}
	
	public int getOccupationTimeEnd() {
		return this.occupationTimeEnd;
	}
	
	public void setNeighbors(List<Locker> lockers) {
		neighbors = lockers;
	}
	
	public boolean isOccupied(){
		return this.occupied;
	}
	
	public boolean isInUse(){
		return this.inUse;
	}
	
	public boolean neighborsInUse() {
		boolean inUse = false;
		
		for(Locker l : neighbors) {
			if(l.isInUse()) {
				inUse = true;
				break;
			}
		}
		
		return inUse;
	}
	
	public void setInUse(boolean use) {
		this.inUse = use;
	}
	
	public void setOccupied(boolean occu) {
		this.occupied = occu;
	}
	
}