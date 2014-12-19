package de.hpi.author_support.classification.obj;

public class CombinedGenderDocument extends GenderDocument{

	private long combinedLength;
	
	public CombinedGenderDocument(){
		super();
	}

	public long getCombinedLength() {
		return combinedLength;
	}

	public void setCombinedLength(long combinedLength) {
		this.combinedLength = combinedLength;
	}

}
