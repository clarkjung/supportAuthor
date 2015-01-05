package de.hpi.author_support.classification.dao;

public class GenderDocumentDaoMain {

	public static void main(String[] args) {

		String schemaName = "JAEYOON";
		String indexTableName = "$TA_GENDER_CORPUS_INDEX";
		GenderDocumentDao gdDao = new GenderDocumentDao();
		gdDao.fetchGenderDocuments(schemaName, indexTableName);
		
	}

}
