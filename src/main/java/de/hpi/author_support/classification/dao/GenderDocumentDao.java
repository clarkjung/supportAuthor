package de.hpi.author_support.classification.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hpi.author_support.classification.db.Connection;
import de.hpi.author_support.classification.obj.GenderDocument;

public class GenderDocumentDao {

	final private String userID = "JAEYOON";
	final private String password = "Jaeyoon54";
	private enum pos {auxiliary verb, preposition, pronoun, conjunction, article};
	
	List<GenderDocument> genderDocs;
	Connection connection;
	
	public GenderDocumentDao(){
		genderDocs = new ArrayList<GenderDocument>();
		connection = new Connection(this.userID, this.password);
	}
	
	private void addGenderDocument(GenderDocument doc){
		genderDocs.add(doc);
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
		
		for ( int id = 1 ; id <= totalRows ; id ++ ){
			
			HashMap<String, Integer> auxVerbs = new HashMap<String, Integer>();
			HashMap<String, Integer> prepositions = new HashMap<String, Integer>();
			HashMap<String, Integer> pronouns = new HashMap<String, Integer>();
			HashMap<String, Integer> conjunctions = new HashMap<String, Integer>();
			HashMap<String, Integer> articles = new HashMap<String, Integer>();
			
			ArrayList<ArrayList<String>> rows = connection.selectWhereIdIs(schemaName, indexTableName, id);
			for ( ArrayList<String> pair : rows){
				switch (pos.valueOf(pair.get(1))) {
				case auxVerb:
					auxVerbs = addToHashMap(addToHashMap
					break;
				case preposition:
					break;
				case pronoun:
					break;
				case conjunction:
					break;
				case article:
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
			
			addGenderDocument(genderDocument);
		}
		
	}
	
	
	
	
	
}
