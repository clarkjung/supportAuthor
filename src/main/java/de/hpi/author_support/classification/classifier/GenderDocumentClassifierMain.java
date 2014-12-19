package de.hpi.author_support.classification.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hpi.author_support.classification.dao.GenderDocumentDao;
import de.hpi.author_support.classification.dao.POS;

public class GenderDocumentClassifierMain {

	public static void main(String[] args) {

		String schemaName = "JAEYOON";
		String indexTableName = "$TA_GENDER_CORPUS_INDEX";
		GenderDocumentDao gdDao = new GenderDocumentDao();
		gdDao.fetchGenderDocuments(schemaName, indexTableName);
		
		GenderDocumentClassifier classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
		classifier.cv_classify(Arrays.asList(POS.values()));
		
//		List<POS> options = new ArrayList<POS>();
//		options.add(POS.AUXILIARY_VERB);
//		System.out.println("for auxverb");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//		}
//		options.clear();
//		
//		classifier.setDocs();
//		options.add(POS.CONJUNCTION);
//		System.out.println("for conjunction");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//			classifier.setDocs();
//		}
//		options.clear();
//		
//		classifier.setDocs();
//		options.add(POS.DETERMINER);
//		System.out.println("for determiner");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//			classifier.setDocs();
//		}
//		options.clear();
//		
//		classifier.setDocs();
//		options.add(POS.PREPOSITION);
//		System.out.println("for preposition");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//			classifier.setDocs();
//		}
//		options.clear();
//		
//		classifier.setDocs();
//		options.add(POS.PRONOUN);
//		System.out.println("for pronoun");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//			classifier.setDocs();
//		}
//		options.clear();
//		
//		classifier.setDocs();
//		System.out.println("for all");
//		for ( int i = 0 ; i < 10 ; i ++ ){
//			System.out.println(i + "th");
//			classifier = new GenderDocumentClassifier(gdDao.getMaleDocs(), gdDao.getFemaleDocs());
//			classifier.classify(Arrays.asList(POS.values()));
//			classifier.setDocs();
//		}
		
	}

}
