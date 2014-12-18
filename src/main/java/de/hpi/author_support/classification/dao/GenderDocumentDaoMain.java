package de.hpi.author_support.classification.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;


public class GenderDocumentDaoMain {

	public static void main(String[] args) {

		String schemaName = "JAEYOON";
		String indexTableName = "$TA_GENDER_CORPUS_INDEX";
		GenderDocumentDao gdDao = new GenderDocumentDao();
		gdDao.fetchGenderDocuments(schemaName, indexTableName);
		
	}

}
