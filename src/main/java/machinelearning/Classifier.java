package machinelearning;

import java.io.BufferedReader;
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


/**
 * Abstract base extended by any concrete classifier.  It implements the basic
 * functionality for storing categories or features and can be used to calculate
 * basic probabilities – both category and feature probabilities. The classify
 * function has to be implemented by the concrete classifier class.
 *
 * @author Philipp Nolte and Jaeyoon Jung
 *
 * @param <T> A feature class
 * @param <K> A category class
 */
public abstract class Classifier<T, K> implements IFeatureProbability<T, K> {

    /**
     * Initial capacity of category dictionaries.
     */
    //private static final int INITIAL_CATEGORY_DICTIONARY_CAPACITY = 16;

    /**
     * Initial capacity of feature dictionaries. It should be quite big, because
     * the features will quickly outnumber the categories. 
     */
    //private static final int INITIAL_FEATURE_DICTIONARY_CAPACITY = 32;

    /**
     * The initial memory capacity or how many classifications are memorized.
     */
    private int memoryCapacity = 10000;

    /**
     * A dictionary mapping features to their number of occurrences in each
     * known category.
     */
    private Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;

    /**
     * A dictionary mapping features to their number of occurrences.
     */
    private Dictionary<T, Integer> totalFeatureCount;
    private Dictionary<K, Integer> totalFeatureCountPerCategory;

    /**
     * A dictionary mapping categories to their number of occurrences.
     */
    private Dictionary<K, Integer> totalCategoryCount;

    /**
     * The classifier's memory. It will forget old classifications as soon as
     * they become too old.
     */
    private Queue<Classification<T, K>> memoryQueue;
    
    protected List<String> targetWordList;
    protected List<String> targetPhraseList;

    /**
     * Constructs a new classifier without any trained knowledge.
     */
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

    /**
     * Resets the <i>learned</i> feature and category counts.
     */
//    public void reset() {
//        this.featureCountPerCategory =
//                new Hashtable<K, Dictionary<T,Integer>>(
//                        Classifier.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
//        this.totalFeatureCount =
//                new Hashtable<T, Integer>(
//                        Classifier.INITIAL_FEATURE_DICTIONARY_CAPACITY);
//        this.totalCategoryCount =
//                new Hashtable<K, Integer>(
//                        Classifier.INITIAL_CATEGORY_DICTIONARY_CAPACITY);
//        this.memoryQueue = new LinkedList<Classification<T, K>>();
//    }
    
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

    /**
     * Returns a <code>Set</code> of features the classifier knows about.
     *
     * @return The <code>Set</code> of features the classifier knows about.
     */
    public Set<T> getFeatures() {
        return ((Hashtable<T, Integer>) this.totalFeatureCount).keySet();
    }

    /**
     * Returns a <code>Set</code> of categories the classifier knows about.
     *
     * @return The <code>Set</code> of categories the classifier knows about.
     */
    public Set<K> getCategories() {
        return ((Hashtable<K, Integer>) this.totalCategoryCount).keySet();
    }

    /**
     * Retrieves the total number of categories the classifier knows about.
     *
     * @return The total category count.
     */
    public int getCategoriesTotal() {
        int toReturn = 0;
        for (Enumeration<Integer> e = this.totalCategoryCount.elements();
                e.hasMoreElements();) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    /**
     * Retrieves the memory's capacity.
     *
     * @return The memory's capacity.
     */
    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    /**
     * Sets the memory's capacity.  If the new value is less than the old
     * value, the memory will be truncated accordingly.
     *
     * @param memoryCapacity The new memory capacity.
     */
    public void setMemoryCapacity(int memoryCapacity) {
        for (int i = this.memoryCapacity; i > memoryCapacity; i--) {
            this.memoryQueue.poll();
        }
        this.memoryCapacity = memoryCapacity;
    }

    /**
     * Increments the count of a given feature in the given category.  This is
     * equal to telling the classifier, that this feature has occurred in this
     * category.
     *
     * @param feature The feature, which count to increase.
     * @param category The category the feature occurred in.
     */
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

    /**
     * Increments the count of a given category.  This is equal to telling the
     * classifier, that this category has occurred once more.
     *
     * @param category The category, which count to increase.
     */
    public void incrementCategory(K category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            this.totalCategoryCount.put(category, 0);
            count = this.totalCategoryCount.get(category);
        }
       this.totalCategoryCount.put(category, ++count);
    }

    /**
     * Decrements the count of a given feature in the given category.  This is
     * equal to telling the classifier that this feature was classified once in
     * the category.
     *
     * @param feature The feature to decrement the count for.
     * @param category The category.
     */
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

    /**
     * Decrements the count of a given category.  This is equal to telling the
     * classifier, that this category has occurred once less.
     *
     * @param category The category, which count to increase.
     */
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

    /**
     * Retrieves the number of occurrences of the given feature in the given
     * category.
     *
     * @param feature The feature, which count to retrieve.
     * @param category The category, which the feature occurred in.
     * @return The number of occurrences of the feature in the category.
     */
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

    /**
     * Retrieves the number of occurrences of the given category.
     * 
     * @param category The category, which count should be retrieved.
     * @return The number of occurrences.
     */
    public int categoryCount(K category) {
        Integer count = this.totalCategoryCount.get(category);
        return (count == null) ? 0 : count.intValue();
    }

    /**
     * {@inheritDoc}
     */
//    public float featureProbability(T feature, K category) {
//        if (this.categoryCount(category) == 0) return 0;
//        return (float) this.featureCount(feature, category) / (float) this.categoryCount(category);
//    }
    
    public float featureProbability(T feature, K category) {
    	
    	Dictionary<T, Integer> dictionary = featureCountPerCategory.get(category);
    	int dictionarySize = (dictionary == null) ? 0 : dictionary.size();
    	
        if (this.categoryCount(category) == 0) return 0;
        return (float) (this.featureCount(feature, category) + 1) / (float) (this.featureCountPerCategory(category) + dictionarySize);
    }
    
    

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * overall weight of <code>1.0</code> and an assumed probability of
     * <code>0.5</code>. The probability defaults to the overall feature
     * probability.
     *
     * @see de.daslaboratorium.machinelearning.classifier.Classifier#featureProbability(Object, Object)
     * @see de.daslaboratorium.machinelearning.classifier.Classifier#featureWeighedAverage(Object, Object, IFeatureProbability, float, float)
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(T feature, K category) {
        return this.featureWeighedAverage(feature, category, null, 1.0f, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * overall weight of <code>1.0</code>, an assumed probability of
     * <code>0.5</code> and the given object to use for probability calculation.
     *
     * @see de.daslaboratorium.machinelearning.classifier.Classifier#featureWeighedAverage(Object, Object, IFeatureProbability, float, float)
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator) {
        return this.featureWeighedAverage(feature, category, calculator, 1.0f, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * the given weight and an assumed probability of <code>0.5</code> and the
     * given object to use for probability calculation.
     *
     * @see de.daslaboratorium.machinelearning.classifier.Classifier#featureWeighedAverage(Object, Object, IFeatureProbability, float, float)
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @param weight The feature weight.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator, float weight) {
        return this.featureWeighedAverage(feature, category, calculator, weight, 0.5f);
    }

    /**
     * Retrieves the weighed average <code>P(feature|category)</code> with
     * the given weight, the given assumed probability and the given object to
     * use for probability calculation.
     *
     * @param feature The feature, which probability to calculate.
     * @param category The category.
     * @param calculator The calculating object.
     * @param weight The feature weight.
     * @param assumedProbability The assumed probability.
     * @return The weighed average probability.
     */
    public float featureWeighedAverage(T feature, K category, IFeatureProbability<T, K> calculator, float weight, float assumedProbability) {

        /*
         * use the given calculating object or the default method to calculate
         * the probability that the given feature occurred in the given
         * category.
         */
        final float basicProbability = (calculator == null) ? this.featureProbability(feature, category) : calculator.featureProbability(feature, category);

//        Integer totals = this.totalFeatureCount.get(feature);
//        if (totals == null) totals = 0;
//        return (weight * assumedProbability + totals  * basicProbability) / (weight + totals);
        return basicProbability;
    }

    /**
     * Train the classifier by telling it that the given features resulted in
     * the given category.
     *
     * @param category The category the features belong to.
     * @param features The features that resulted in the given category.
     */
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
    
    

    /**
     * Train the classifier by telling it that the given features resulted in
     * the given category.
     *
     * @param classification The classification to learn.
     */
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

    /**
     * The classify method.  It will retrieve the most likely category for the
     * features given and depends on the concrete classifier implementation.
     *
     * @param features The features to classify.
     * @return The category most likely.
     */
    public abstract Classification<T, K> classify(Collection<T> features);
    
    public abstract Classification<T, K> classify(String unknownTextFilePath);

}
