package de.hpi.author_support.classification.obj;

import java.util.HashMap;

public class GenderDocument {
	
	private int id;
	private String gender;
	private int length;
	private HashMap<String, Integer> auxVerbs;
	private HashMap<String, Integer> prepositions;
	private HashMap<String, Integer> pronouns;
	private HashMap<String, Integer> conjunctions;
	private HashMap<String, Integer> articles;

	public GenderDocument(){
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public HashMap<String, Integer> getAuxVerbs() {
		return auxVerbs;
	}

	public void setAuxVerbs(HashMap<String, Integer> auxVerbs) {
		this.auxVerbs = auxVerbs;
	}

	public HashMap<String, Integer> getPrepositions() {
		return prepositions;
	}

	public void setPrepositions(HashMap<String, Integer> prepositions) {
		this.prepositions = prepositions;
	}

	public HashMap<String, Integer> getPronouns() {
		return pronouns;
	}

	public void setPronouns(HashMap<String, Integer> pronouns) {
		this.pronouns = pronouns;
	}

	public HashMap<String, Integer> getConjunctions() {
		return conjunctions;
	}

	public void setConjunctions(HashMap<String, Integer> conjunctions) {
		this.conjunctions = conjunctions;
	}

	public HashMap<String, Integer> getArticles() {
		return articles;
	}

	public void setArticles(HashMap<String, Integer> articles) {
		this.articles = articles;
	}
	
	
	
	
	
	
}
