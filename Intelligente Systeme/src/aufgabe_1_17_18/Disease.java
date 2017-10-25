package aufgabe_1_17_18;

public class Disease {
	
	private double transmissionProbability;
	int duration;
	
	public Disease(double transProb, int dur) {
		transmissionProbability = transProb;
		duration = dur;
	}
	
	
	public double getTransmissionProbability() {
		return transmissionProbability;
	}

	public int getDuration() {
		return duration;
	}

	public void setTransmissionProbability(double transmissionProbability) {
		this.transmissionProbability = transmissionProbability;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}


}
