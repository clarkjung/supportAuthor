package de.hpi.author_support.classification.obj;

public class ClassificationResult {

	private double malePrecision;
	private double femalePrecision;
	private double totalPrecision;
	
	public ClassificationResult(){
	}
	
	public double getMalePrecision() {
		return malePrecision;
	}
	
	public void setMalePrecision(double malePrecision) {
		this.malePrecision = malePrecision;
	}
	
	public double getFemalePrecision() {
		return femalePrecision;
	}
	
	public void setFemalePrecision(double femalePrecision) {
		this.femalePrecision = femalePrecision;
	}
	
	public double getTotalPrecision() {
		return totalPrecision;
	}
	
	public void setTotalPrecision() {
		this.totalPrecision = (malePrecision + femalePrecision)/2;
	}
	
}
