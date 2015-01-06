package de.hpi.author_support.classification.main;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

import de.hpi.author_support.classification.classifier.GenderDocumentClassifier;
import de.hpi.author_support.classification.dao.POS;
import de.hpi.author_support.classification.obj.ClassificationResult;

public class Main {

	public static void main(String[] args) {
		
		String schemaName = "JAEYOON";
		String indexTableName = "$TA_GENDER_CORPUS_INDEX";
		ClassificationResult result;
		NumberFormat formatter = new DecimalFormat("#0.000");
		
		GenderDocumentClassifier classifier = new GenderDocumentClassifier(schemaName, indexTableName);
		classifier.train();
		result = classifier.test(Arrays.asList(POS.values()));
		
		System.out.println("Male: " + formatter.format(result.getMalePrecision()));
		System.out.println("Female: " + formatter.format(result.getFemalePrecision()));
		System.out.println("Total: " + formatter.format(result.getTotalPrecision()));

	}

}
