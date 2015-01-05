package de.hpi.author_support.classification.classifier;

import java.util.Arrays;

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
		
	}

}
