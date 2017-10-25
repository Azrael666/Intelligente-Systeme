package aufgabe_1_17_18;

public class Disease {
	
	private final double transmissionProbability;
	final int duration;
	
	public Disease(double transmissionProbability, int duration) {
		this.transmissionProbability = transmissionProbability;
		this.duration = duration;
	}
	
	
	public double getTransmissionProbability() {
		return transmissionProbability;
	}

	public int getDuration() {
		return duration;
	}


}
