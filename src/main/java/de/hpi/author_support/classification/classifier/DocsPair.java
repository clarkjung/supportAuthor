package de.hpi.author_support.classification.classifier;

import java.util.ArrayList;
import java.util.List;

import de.hpi.author_support.classification.obj.GenderDocument;

public class DocsPair {

	private List<GenderDocument> trainingDocs;
	private List<GenderDocument> testDocs;
	
	public DocsPair(){
		this.setTrainingDocs(new ArrayList<GenderDocument>());
		this.setTestDocs(new ArrayList<GenderDocument>());
	}

	public List<GenderDocument> getTrainingDocs() {
		return trainingDocs;
	}

	public void setTrainingDocs(List<GenderDocument> trainingDocs) {
		this.trainingDocs = trainingDocs;
	}

	public List<GenderDocument> getTestDocs() {
		return testDocs;
	}

	public void setTestDocs(List<GenderDocument> testDocs) {
		this.testDocs = testDocs;
	}
	
}
