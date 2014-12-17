package machinelearning;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import tools.Counter;


/*
 * @param <T> A feature class
 * @param <K> A category class
 */
public abstract class Classifier<T, K> implements IFeatureProbability<T, K> {

    private int memoryCapacity = 10000000;
    private Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;
    private Dictionary<T, Integer> totalFeatureCount;
    private Dictionary<K, Integer> totalFeatureCountPerCategory;
    private Dictionary<K, Integer> totalCategoryCount;
    private Queue<Classification<T, K>> memoryQueue;   
    protected List<String> targetWordList;
    protected List<String> targetPhraseList;

    public Classifier() {
        this.reset();
    }
    
    public Classifier(String targetWordsListPath){
    	this();
        this.targetWordList = new ArrayList<String>();
        this.targetPhraseList = new ArrayList<String>();
        setTargetWordList(targetWordsListPath);
    }
    
    protected void setTargetWordList(String targetWordsListPath){
    	List<String> targetWordsList = createWordList(targetWordsListPath);
    	for (String str : targetWordsList){
    		if (Counter.countWords(str) == 1) targetWordList.add(str);
    		else targetPhraseList.add(str);
    	}
    }
    
    public void reset() {
        this.featureCountPerCategory = new Hashtable<K, Dictionary<T,Integer>>();
        this.totalFeatureCountPerCategory = new Hashtable<K, Integer>();
        this.totalFeatureCount = new Hashtable<T, Integer>();
        this.totalCategoryCount = new Hashtable<K, Integer>();
        this.memoryQueue = new LinkedList<Classification<T, K>>();
        this.targetWordList = null;
        this.targetPhraseList = null;
    }
    
    public List<String> getTargetWordList(){
    	return targetWordList;
    }
    
    public List<String> getTargetPhraseList(){
    	return targetPhraseList;
    }

    public Set<T> getFeatures() {
        return ((Hashtable<T, Integer>) this.totalFeatureCount).keySet();
    }

    public Set<K> getCategories() {
        return ((Hashtable<K, Integer>) this.totalCategoryCount).keySet();
    }

    public int getCategoriesTotal() {
        int toReturn = 0;
        for (Enumeration<Integer> e = this.totalCategoryCount.elements();
                e.hasMoreElements();) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(int memoryCapacity) {
        for (int i = this.memoryCapacity; i > memoryCapacity; i--) {
            this.memoryQueue.poll();
        }
        this.memoryCapacity = memoryCapacity;
    }

    public void incrementFeature(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null) {
            //this.featureCountPerCategory.put(category, new Hashtable<T, Integer>(Classifier.INITIAL_FEATURE_DICTIONARY_CAPACITY));
        	this.featureCountPerCategory.put(category, new Hashtable<T, Integer>());
            features = this.featureCountPerCategory.get(category);
        }
        Integer count = features.get(feature);
        if (count == null) {
            features.put(feature, 0);
            count = features.get(feature);
        }
        features.put(feature, ++count);

        Integer totalCount = this.totalFeatureCount.get(feature);
        if (totalCount == null) {
            this.totalFeatureCount.put(feature, 0);
            totalCount = this.totalFeatureCount.get(feature);
        }
        this.totalFeatureCount.put(feature, ++totalCount);
        
        Integer totalCountPerCategory = this.totalFeatureCountPerCategory.get(category);
        if (totalCountPerCategory == null) {
        	this.totalFeatureCountPerCategory.put(category, 0);
        	totalCountPerCategory = this.totalFeatureCountPerCategory.get(category);
        }
        this.totalFeatureCountPerCategory.put(category, ++totalCountPerCategory);
    }

    public void incrementCategory(K category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            this.totalCategoryCount.put(category, 0);
            count = this.totalCategoryCount.get(category);
        }
       this.totalCategoryCount.put(category, ++count);
    }

    public void decrementFeature(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null) {
            return;
        }
        Integer count = features.get(feature);
        if (count == null) {
            return;
        }
        if (count.intValue() == 1) {
            features.remove(feature);
            if (features.size() == 0) {
                this.featureCountPerCategory.remove(category);
            }
        } else {
            features.put(feature, --count);
        }

        Integer totalCount = this.totalFeatureCount.get(feature);
        if (totalCount == null) {
            return;
        }
        if (totalCount.intValue() == 1) {
            this.totalFeatureCount.remove(feature);
        } else {
            this.totalFeatureCount.put(feature, --totalCount);
        }
        
        Integer totalCountPerCategory = this.totalFeatureCountPerCategory.get(category);
        if (totalCountPerCategory == null) {
        	return;
        }
        if (totalCountPerCategory.intValue() == 1){
        	this.totalFeatureCountPerCategory.remove(category);
        }else{
        	this.totalFeatureCountPerCategory.put(category, --totalCountPerCategory);
        }
    }

    public void decrementCategory(K category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            return;
        }
        if (count.intValue() == 1) {
            this.totalCategoryCount.remove(category);
        } else {
            this.totalCategoryCount.put(category, --count);
        }
    }

    public int featureCount(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null)
            return 0;
        Integer count = features.get(feature);
        return (count == null) ? 0 : count.intValue();
    }
    
    private int featureCountPerCategory(K category){
    	Integer count = this.totalFeatureCountPerCategory.get(category);
    	return (count == null) ? 0 : count.intValue();
    }

    public int categoryCount(K category) {
        Integer count = this.totalCategoryCount.get(category);
        return (count == null) ? 0 : count.intValue();
    }
    
    public float featureProbability(T feature, K category) {
    	
    	Dictionary<T, Integer> dictionary = featureCountPerCategory.get(category);
    	int dictionarySize = (dictionary == null) ? 0 : dictionary.size();
    	
        if (this.categoryCount(category) == 0) return 0;
        return (float) (this.featureCount(feature, category) + 1) / (float) (this.featureCountPerCategory(category) + dictionarySize);
    }
    
    public float featureWeighedAverage(T feature, K category) {
        return this.featureWeighedAverage(feature, category, null, 1.0f, 0.5f);
    }

    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator) {
        return this.featureWeighedAverage(feature, category, calculator, 1.0f, 0.5f);
    }

    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator, float weight) {
        return this.featureWeighedAverage(feature, category, calculator, weight, 0.5f);
    }

    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator, float weight, float assumedProbability) {

        final float basicProbability = (calculator == null) ? this.featureProbability(feature, category) : calculator.featureProbability(feature, category);
        return basicProbability;
    }

    public void learn(K category, Collection<T> features) {
        this.learn(new Classification<T, K>(features, category));
    }
    
    private List<String> createWordList(String wordListPath){
    	List<String> list = new ArrayList<String>();
    	String sCurrentLine;
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(wordListPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			while ((sCurrentLine = br.readLine()) != null) {
				list.add(sCurrentLine.toLowerCase());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
    }
    
    public void learnFolder(K category, String folderPath){
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		int totalSize = listOfFiles.length;
		int count = 0;
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String fileNameOnly = file.getName();
				String fullFilePath = folderPath + fileNameOnly;
				
				if (fullFilePath.endsWith(".txt")) {
					count++;
					learn(category, fullFilePath);
					if ( count % 100 == 0 ) System.out.println("learning " + category + " docs: " + count + "/" + totalSize + " done");
				}
			}
		}
    }
        
    @SuppressWarnings("unchecked")
	public void learn(K category, String textFilePath){
    	
    	boolean usingTargetWordsList = true;
    	if (targetPhraseList == null && targetWordList == null) usingTargetWordsList = false;
    	
    	String sCurrentLine;
    	List<String> list = new ArrayList<String>();
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(textFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.trim().isEmpty()) continue;
				if(usingTargetWordsList){
					List<String> tempList = filterList(sCurrentLine.toLowerCase(), targetPhraseList);
					list.addAll(tempList);
				}
				
				String[] line = sCurrentLine.toLowerCase().split("\\s");
				line[line.length-1] = line[line.length-1].replace(".", "");
				List <String> tempList = Arrays.asList(line);
				List<String> buffList = tempList;
				if(usingTargetWordsList){
					buffList = filterList(tempList, targetWordList);
				}
				list.addAll(buffList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.learn(category, (Collection<T>) list);
    }
    
    protected List<String> filterList(List<String> tempList, List<String> targetWordList){
    	List<String> buffList = new ArrayList<String>();
    	for (String str : tempList){
    		if (targetWordList.contains(str)) buffList.add(str);
    	}
    	return buffList;
    }
    
    protected List<String> filterList(String sCurrentLine, List<String> targetPhraseList){
    	List<String> buffList = new ArrayList<String>();
    	for (String str : targetPhraseList){
    		int count = countMatches(sCurrentLine, str);
    		for ( int i = 0 ; i < count ; i ++ ){
    			buffList.add(str);
    		}
    	}
    	return buffList;
    }
    
    private int countMatches(String str, String findStr){
        int lastIndex = 0;
        int count = 0;

        while ((lastIndex = str.indexOf(findStr, lastIndex)) != -1) {
            count++;
            lastIndex += findStr.length() - 1;
        }
        
        return count;
    }
    
    public void learn(Classification<T, K> classification) {

        for (T feature : classification.getFeatureset())
            this.incrementFeature(feature, classification.getCategory());
        this.incrementCategory(classification.getCategory());

        this.memoryQueue.offer(classification);
        if (this.memoryQueue.size() > this.memoryCapacity) {
            Classification<T, K> toForget = this.memoryQueue.remove();

            for (T feature : toForget.getFeatureset())
                this.decrementFeature(feature, toForget.getCategory());
            this.decrementCategory(toForget.getCategory());
        }
    }

    public abstract Classification<T, K> classify(Collection<T> features);
    public abstract Classification<T, K> classify(String unknownTextFilePath);
    public abstract void classifyFolder(K category, String folderPath);

}
