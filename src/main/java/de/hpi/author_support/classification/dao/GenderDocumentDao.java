package de.hpi.author_support.classification.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;

import de.hpi.author_support.classification.db.Connection;
import de.hpi.author_support.classification.obj.GenderDocument;

public class GenderDocumentDao {

	final private String userID = "JAEYOON";
	final private String password = "Jaeyoon54";
	
	List<GenderDocument> genderDocs;
	Connection connection;
	
	public GenderDocumentDao(){
		genderDocs = new ArrayList<GenderDocument>();
		connection = new Connection(this.userID, this.password);
	}
	
	private void addGenderDocument(int docID, GenderDocument doc){
		genderDocs.add(docID-1, doc);
	}
	
	private GenderDocument getGenderDocumentByID(int docID){
		return genderDocs.get(docID-1);
	}
	
	private HashMap<String, Integer> addToHashMap(HashMap<String, Integer> map, String str){
		
		HashMap<String, Integer> tempMap = map;
		
		int value = 1;
		if(tempMap.containsKey(str)) value = tempMap.get(str) + 1;
		tempMap.put(str, value);
		
		return tempMap;
	}
	
	public void fetchGenderDocuments(String schemaName, String indexTableName){
		
		int totalRows = connection.selectDistinctID(schemaName, indexTableName);
		String[] articleArray = {"a", "an", "the"};
		List<String> articleList = Arrays.asList(articleArray);  
		
		for ( int id = 1 ; id <= totalRows ; id ++ ){
			if(id % 100==0) System.out.println(id + "done");
			
			HashMap<String, Integer> auxVerbs = new HashMap<String, Integer>();
			HashMap<String, Integer> prepositions = new HashMap<String, Integer>();
			HashMap<String, Integer> pronouns = new HashMap<String, Integer>();
			HashMap<String, Integer> conjunctions = new HashMap<String, Integer>();
			HashMap<String, Integer> articles = new HashMap<String, Integer>();
			
			ArrayList<ArrayList<String>> rows = connection.selectWhereIdIs(schemaName, indexTableName, id);
			for ( ArrayList<String> row : rows){
				String key = row.get(0).replaceAll(" ", "_").toUpperCase();
				if (!EnumUtils.isValidEnum(POS.class, key)) continue;
				switch (POS.valueOf(key)) {
				case AUXILIARY_VERB:
					addToHashMap(auxVerbs, row.get(1));
					break;
				case PREPOSITION:
					addToHashMap(prepositions, row.get(1));
					break;
				case PRONOUN:
					addToHashMap(pronouns, row.get(1));
					break;
				case CONJUNCTION:
					addToHashMap(conjunctions, row.get(1));
					break;
				case DETERMINER:
					if(!articleList.contains(row.get(1))) break;
					addToHashMap(articles, row.get(1));
					break;
				default:
					break;
				}
			}
			
			GenderDocument genderDocument = new GenderDocument();
			genderDocument.setId(id);
			genderDocument.setLength(rows.size());
			genderDocument.setAuxVerbs(auxVerbs);
			genderDocument.setPrepositions(prepositions);
			genderDocument.setPronouns(pronouns);
			genderDocument.setConjunctions(conjunctions);
			genderDocument.setArticles(articles);
			
			addGenderDocument(id, genderDocument);
		}
		
		setGender(schemaName);
		
		System.out.println(genderDocs);
		
	}
	
	private void setGender(String schemaName){
		
		String tableName = "GENDER_CORPUS";
		ArrayList<IDnGender> rows = connection.selectIDAndGender(schemaName, tableName);
		
		for (IDnGender row : rows){
			getGenderDocumentByID(row.getId()).setGender(row.getGender());
		}
	}
	
}
