package aufgabe_1_17_18;

public class Individuum {

	private boolean sick;
	private boolean immune;
	private boolean contagious;
	
	private int number;
	
	public Individuum(int number) {
		sick = false;
		immune = false;
		contagious = false;
		this.number = number;
	}
	
	public boolean getSick() {
		return sick;
	}
	
	public void setSick(boolean sick) {
		this.sick = sick;
	}

	public boolean getImmune() {
		return immune;
	}
	
	public void setImmune(boolean immune) {
		this.immune = immune;
	}
	
	public boolean getContagious() {
		return contagious;
	}
	
	public void setContagious(boolean contagious) {
		this.contagious = contagious;
	}
	
	public int getNumber() {
		return number;
	}
}
