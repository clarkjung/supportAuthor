package de.hpi.author_support.classification.classifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.hpi.author_support.classification.dao.POS;
import de.hpi.author_support.classification.obj.CombinedGenderDocument;
import de.hpi.author_support.classification.obj.GenderDocument;

public class GenderDocumentClassifier {

	private List<GenderDocument> maleTrainingDocs;
	private List<GenderDocument> femaleTrainingDocs;
	private List<GenderDocument> maleTestDocs;
	private List<GenderDocument> femaleTestDocs;
	private List<GenderDocument> maleDocs;
	private List<GenderDocument> femaleDocs;
	private List<List<GenderDocument>> cvMale;
	private List<List<GenderDocument>> cvFemale;
	
	public GenderDocumentClassifier(List<GenderDocument> maleDocs, List<GenderDocument> femaleDocs){
		
		this.maleTrainingDocs = new ArrayList<GenderDocument>();
		this.femaleTrainingDocs = new ArrayList<GenderDocument>();
		this.maleTestDocs = new ArrayList<GenderDocument>();
		this.femaleTestDocs = new ArrayList<GenderDocument>();
		this.maleDocs = maleDocs;
		this.femaleDocs = femaleDocs;
		
		this.cvMale = splitDocs(maleDocs);
		this.cvFemale = splitDocs(femaleDocs);
		
		//setDocs();
	}
	
	//test
	public GenderDocumentClassifier(){
		
	}
	
	private List<List<GenderDocument>> splitDocs(List<GenderDocument> genderDocs){
		
		List<List<GenderDocument>> cvGender = new ArrayList<List<GenderDocument>>();
		List<GenderDocument> allDocs = new ArrayList<GenderDocument>(genderDocs);
		Collections.shuffle(allDocs);
		
		int fromIndex = 0;
		ArrayList<Integer> assignList = assignSizeForSubset(allDocs.size());
		for ( int i = 0 ; i < 10 ; i ++ ){
			int toIndex = fromIndex + assignList.get(i);
			List<GenderDocument> list = new ArrayList<GenderDocument>(allDocs.subList(fromIndex, toIndex));
			cvGender.add(list);
			fromIndex += assignList.get(i);
		}
		
		return cvGender;
	}
	
	private ArrayList<Integer> assignSizeForSubset(int allSize){
		int baseNum = allSize/10;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for ( int i = 0 ; i < 10 ; i ++ ){
			list.add(baseNum);
		}
		
		int gap = allSize - baseNum*10;
		for ( int i = 0 ; i < gap ; i++ ){
			int value = list.get(i);
			list.set(i, value + 1);
		}
		return list;
	}
	
	public void setDocs(){
		
		maleTrainingDocs = setTrainingAndTestDocs(maleDocs).getTrainingDocs();
		femaleTrainingDocs = setTrainingAndTestDocs(femaleDocs).getTrainingDocs();
		maleTestDocs = setTrainingAndTestDocs(maleDocs).getTestDocs();
		femaleTestDocs = setTrainingAndTestDocs(femaleDocs).getTestDocs();
		
	}
	
	private DocsPair setTrainingAndTestDocs(List<GenderDocument> docsToBeSplit){
		
		DocsPair docsPair = new DocsPair();
		List<GenderDocument> docs = docsToBeSplit;
		Collections.shuffle(docs);
		int docsSize = docs.size();
		int trainingSize = docsSize*9/10;
		
		List<GenderDocument> trainingDocs = new ArrayList<GenderDocument>();
		trainingDocs = docs.subList(0, trainingSize);
		docsPair.setTrainingDocs(trainingDocs);
		
		List<GenderDocument> testDocs = new ArrayList<GenderDocument>();
		testDocs = docs.subList(trainingSize, docsSize);
		docsPair.setTestDocs(testDocs);
		
		return docsPair;
	}
	
	private int getHashMapAllValues(HashMap<String, Integer> map){
		
		int values = 0;
		
		Iterator<Entry<String, Integer>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> mapEntry = (Map.Entry<String, Integer>) iterator.next();
			values += (Integer) mapEntry.getValue();
		}
		
		return values;
	}

	//remove static
	static private HashMap<String, Integer> combineMap(HashMap<String, Integer> map1, HashMap<String, Integer> map2){
		
		HashMap<String, Integer> combinedMap;
		if(map1==null){
			combinedMap = new HashMap<String, Integer>(map2); 
			return combinedMap;
		}
		combinedMap = new HashMap<String, Integer>(map1);
		
		Iterator<Entry<String, Integer>> iterator = map2.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> mapEntry = (Map.Entry<String, Integer>) iterator.next();
			if(map1.containsKey(mapEntry.getKey())){
				int value = map1.get(mapEntry.getKey());
				value += mapEntry.getValue();
				combinedMap.put(mapEntry.getKey(), value);
			}else{
				combinedMap.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		
		return combinedMap;
	}
	
	
	//remove static and private 
	static public HashMap<Integer, HashMap<String, Integer>> combineTargetList(HashMap<Integer, HashMap<String, Integer>> list1, HashMap<Integer, HashMap<String, Integer>> list2){
		
		HashMap<Integer, HashMap<String, Integer>> combinedTargetList = new HashMap<Integer, HashMap<String, Integer>>(list1);
		
		Iterator<Entry<Integer, HashMap<String, Integer>>> iterator = list2.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<Integer, HashMap<String, Integer>> mapEntry = (Map.Entry<Integer, HashMap<String,Integer>>) iterator.next();
			combinedTargetList.put(mapEntry.getKey(), combineMap(list1.get(mapEntry.getKey()), mapEntry.getValue()));
		}
		
		return combinedTargetList;
	}
	
	private CombinedGenderDocument combineDocuments(List<GenderDocument> docs){
		
		CombinedGenderDocument combinedGenderDocument = new CombinedGenderDocument();
		
		for (GenderDocument doc : docs){
			
			//combine TargetList
			HashMap<Integer, HashMap<String, Integer>> targetList = combinedGenderDocument.getTargetList();
			targetList = combineTargetList(targetList, doc.getTargetList());
			combinedGenderDocument.setTargetList(targetList);
			
			//combine length
			combinedGenderDocument.setCombinedLength(combinedGenderDocument.getCombinedLength() + (long)doc.getLength());
		}
		
		//set gender
		combinedGenderDocument.setGender(docs.get(0).getGender());
		
		return combinedGenderDocument;
	}
	
	private double getProductFromCombinedDocForOption(GenderDocument testDoc, CombinedGenderDocument combinedDoc, int posIndex){
		
		double product = 0.0d;
		long trainSize = combinedDoc.getCombinedLength();
		HashMap<String, Integer> trainMap = combinedDoc.getTargetList().get(posIndex);
		int mapSize = trainMap.size();
		
		Iterator<Entry<String, Integer>> iterator =  testDoc.getTargetList().get(posIndex).entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Integer> mapEntry = (Map.Entry<String, Integer>) iterator.next();
			String word = (String) mapEntry.getKey();
			int freq = (Integer) mapEntry.getValue();
			
			int hit = 0;
			if(trainMap.containsKey(word)) hit = trainMap.get(word);
			product += (Math.log10((double)(hit+1)/(double)(trainSize + mapSize)))*freq;
		}
		
		return product;
	}
	
	private double getProductFromCombinedDocForOptions(GenderDocument testDoc, CombinedGenderDocument combinedDoc, List<POS> options){
		
		double product = 0.0d;
		for(POS pos : options){
			product += getProductFromCombinedDocForOption(testDoc, combinedDoc, pos.getValue());
		}
		
		return product;
	}
	
	public void cv_classify(List<POS> options){
		
		double maleSum = 0;
		double femaleSum = 0;
		
		for ( int i = 0 ; i < 10 ; i ++ ){
			maleTestDocs = new ArrayList<GenderDocument>(cvMale.get(i));
			maleTrainingDocs = combineLists(i, cvMale);
			femaleTestDocs = new ArrayList<GenderDocument>(cvFemale.get(i));
			femaleTrainingDocs = combineLists(i, cvFemale);
			
			CombinedGenderDocument combinedMale = combineDocuments(maleTrainingDocs);
			CombinedGenderDocument combinedFemale = combineDocuments(femaleTrainingDocs);
					
			//for male
			int count = 0;
			int hit = 0;
			for(GenderDocument doc : maleTestDocs){
				count++;
				
				double temp = (double)maleTrainingDocs.size()/(double)(maleTrainingDocs.size() + femaleTrainingDocs.size());
				double maleProduct = getProductFromCombinedDocForOptions(doc, combinedMale, options) + Math.log10(temp);
				temp = (double)femaleTrainingDocs.size()/(double)(maleTrainingDocs.size() + femaleTrainingDocs.size());
				double femaleProduct = getProductFromCombinedDocForOptions(doc, combinedFemale, options) + Math.log10(temp);
				
				if(maleProduct >= femaleProduct) hit++;
			}
			
			System.out.println("For Male");
			System.out.println("count: " + count + ", hit: " + hit + ", precise: " + hit*100/count);
			maleSum += (double)hit/(double)count;
			
			//for female
			count = 0;
			hit = 0;
			for(GenderDocument doc : femaleTestDocs){
				count++;
				
				double maleProduct = getProductFromCombinedDocForOptions(doc, combinedMale, options);
				double femaleProduct = getProductFromCombinedDocForOptions(doc, combinedFemale, options);
				
				if(femaleProduct >= maleProduct) hit++;
			}
			
			System.out.println("For Female");
			System.out.println("count: " + count + ", hit: " + hit + ", precise: " + hit*100/count);
			femaleSum += (double)hit/(double)count;
		}
		
		System.out.println("==================");
		System.out.println("male: " + maleSum/10);
		System.out.println("female: " + femaleSum/10);
	}
	
	private List<GenderDocument> combineLists(int i, List<List<GenderDocument>> cvGender){
		
		List<GenderDocument> list = new ArrayList<GenderDocument>();
		for (int index = 0 ; index < cvGender.size() ; index++ ){
			if(index==i) continue;
			list.addAll(cvGender.get(index));
		}
		
		return list;
	}
	
	public void classify(List<POS> options){
		
		CombinedGenderDocument combinedMale = combineDocuments(maleTrainingDocs);
		CombinedGenderDocument combinedFemale = combineDocuments(femaleTrainingDocs);
				
		//for male
		int count = 0;
		int hit = 0;
		for(GenderDocument doc : maleTestDocs){
			count++;
			
			double temp = (double)maleTrainingDocs.size()/(double)(maleTrainingDocs.size() + femaleTrainingDocs.size());
			double maleProduct = getProductFromCombinedDocForOptions(doc, combinedMale, options) + Math.log10(temp);
			temp = (double)femaleTrainingDocs.size()/(double)(maleTrainingDocs.size() + femaleTrainingDocs.size());
			double femaleProduct = getProductFromCombinedDocForOptions(doc, combinedFemale, options) + Math.log10(temp);
			
			if(maleProduct >= femaleProduct) hit++;
		}
		
//		System.out.println("For Male");
//		System.out.println("count: " + count + ", hit: " + hit + ", precise: " + hit*100/count);
		
		//for female
		count = 0;
		hit = 0;
		for(GenderDocument doc : femaleTestDocs){
			count++;
			
			double maleProduct = getProductFromCombinedDocForOptions(doc, combinedMale, options);
			double femaleProduct = getProductFromCombinedDocForOptions(doc, combinedFemale, options);
			
			if(femaleProduct >= maleProduct) hit++;
		}
		
//		System.out.println("For Female");
//		System.out.println("count: " + count + ", hit: " + hit + ", precise: " + hit*100/count);
	}
	
	
	
	
	
	
	
}
