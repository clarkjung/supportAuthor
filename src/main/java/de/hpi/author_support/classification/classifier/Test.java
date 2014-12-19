package de.hpi.author_support.classification.classifier;

import java.util.ArrayList;
import java.util.HashMap;

public class Test {

	public static void main(String[] args) {

		HashMap<String, Integer> map1 = new HashMap<String, Integer>();
		map1.put("key1", 1);
		map1.put("key2", 2);
		
		HashMap<String, Integer> map2 = new HashMap<String, Integer>();
		map2.put("key2_1", 1);
		map2.put("key2_2", 2);
		
		HashMap<Integer, HashMap<String, Integer>> list1 = new HashMap<Integer, HashMap<String, Integer>>();
		list1.put(1, map1);
		list1.put(2, map2);
		
		HashMap<String, Integer> map11 = new HashMap<String, Integer>();
		map11.put("key1", 3);
		map11.put("key3", 6);
		
		HashMap<String, Integer> map12 = new HashMap<String, Integer>();
		map12.put("key2_1", 5);
		map12.put("key2_3", 4);
		
		HashMap<Integer, HashMap<String, Integer>> list2 = new HashMap<Integer, HashMap<String, Integer>>();
		list2.put(1, map11);
		list2.put(2, map12);
		
		GenderDocumentClassifier rm = new GenderDocumentClassifier();
		rm.combineTargetList(list1, list2);
		System.out.println(rm);	
		
//		HashMap<String, Integer> map = new HashMap<String, Integer>();
//		map.put("test", 1);
//		System.out.println(map);
		
		
		
		
	}
	
	static public void foo(HashMap<String, Integer> map){
		HashMap<String, Integer> copyMap = new HashMap<String, Integer>(map);
		copyMap.put("added", 2);
	}

}
