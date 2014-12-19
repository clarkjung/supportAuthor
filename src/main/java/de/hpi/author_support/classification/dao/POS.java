package de.hpi.author_support.classification.dao;

public enum POS {
	
	AUXILIARY_VERB(0),
	PREPOSITION(1),
	PRONOUN(2),
	CONJUNCTION(3),
	DETERMINER(4);
	
	private int value;
	
	private POS(int value) {
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
}
