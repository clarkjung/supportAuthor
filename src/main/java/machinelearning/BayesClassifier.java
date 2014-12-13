package machinelearning;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A concrete implementation of the abstract Classifier class.  The Bayes
 * classifier implements a naive Bayes approach to classifying a given set of
 * features: classify(feat1,...,featN) = argmax(P(cat)*PROD(P(featI|cat)
 *
 * @author Philipp Nolte
 *
 * @see http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public class BayesClassifier<T, K> extends Classifier<T, K> {
	
    public BayesClassifier(String targetWordsListPath){
    	this();
        this.targetWordList = new ArrayList<String>();
        this.targetPhraseList = new ArrayList<String>();
        setTargetWordList(targetWordsListPath);
    }
    
    public BayesClassifier(){
    	super();
    }

    /**
     * Calculates the product of all feature probabilities: PROD(P(featI|cat)
     *
     * @param features The set of features to use.
     * @param category The category to test for.
     * @return The product of all feature probabilities.
     */
//    private float featuresProbabilityProduct(Collection<T> features, K category) {
//        float product = 1.0f;
//        for (T feature : features) product *= this.featureWeighedAverage(feature, category);
//        return product;
//    }
    
    private float featuresProbabilityProduct(Collection<T> features, K category) {
        float product = 0.0f;
        for (T feature : features) product += Math.log10(this.featureWeighedAverage(feature, category));
        //return product;
        return (float) Math.pow(10, product);
    }

    /**
     * Calculates the probability that the features can be classified as the
     * category given.
     *
     * @param features The set of features to use.
     * @param category The category to test for.
     * @return The probability that the features can be classified as the
     *    category.
     */
    private float categoryProbability(Collection<T> features, K category) {
//    	System.out.println("categoryCount-" + category + ": " + (float) this.categoryCount(category));
//    	System.out.println("getCategoriesTotal: " + (float) this.getCategoriesTotal());
//    	System.out.println("featuresProbabilityProduct(features," + category + "): " + featuresProbabilityProduct(features, category));
    	float categoryCount = (float) Math.log10((float) this.categoryCount(category));
    	float getCategoriesTotal = (float) Math.log10((float) this.getCategoriesTotal());
    	float featuresProbabilityProduct = (float) Math.log10(featuresProbabilityProduct(features, category));
    	System.out.println("categoryProbability: " + (categoryCount - getCategoriesTotal + featuresProbabilityProduct));
    	return categoryCount - getCategoriesTotal + featuresProbabilityProduct;
        //return ((float) this.categoryCount(category) / (float) this.getCategoriesTotal()) * featuresProbabilityProduct(features, category);
    }

    /**
     * Retrieves a sorted <code>Set</code> of probabilities that the given set
     * of features is classified as the available categories.
     *
     * @param features The set of features to use.
     * @return A sorted <code>Set</code> of category-probability-entries.
     */
    private SortedSet<Classification<T, K>> categoryProbabilities(Collection<T> features) {

        /*
         * Sort the set according to the possibilities. Because we have to sort
         * by the mapped value and not by the mapped key, we can not use a
         * sorted tree (TreeMap) and we have to use a set-entry approach to
         * achieve the desired functionality. A custom comparator is therefore
         * needed.
         */
        SortedSet<Classification<T, K>> probabilities =
                new TreeSet<Classification<T, K>>(
                        new Comparator<Classification<T, K>>() {

                    public int compare(Classification<T, K> o1,
                            Classification<T, K> o2) {
                        int toReturn = Float.compare(
                                o1.getProbability(), o2.getProbability());
                        if ((toReturn == 0)
                                && !o1.getCategory().equals(o2.getCategory()))
                            toReturn = -1;
                        return toReturn;
                    }
                });

        for (K category : this.getCategories())
            probabilities.add(new Classification<T, K>(features, category, this.categoryProbability(features, category)));
        return probabilities;
    }

    /**
     * Classifies the given set of features.
     *
     * @return The category the set of features is classified as.
     */
    @Override
    public Classification<T, K> classify(Collection<T> features) {
        SortedSet<Classification<T, K>> probabilites = this.categoryProbabilities(features);

        if (probabilites.size() > 0) {
            return probabilites.last();
        }
        return null;
    }

    /**
     * Classifies the given set of features. and return the full details of the
     * classification.
     *
     * @return The set of categories the set of features is classified as.
     */
    public Collection<Classification<T, K>> classifyDetailed(Collection<T> features) {
        return this.categoryProbabilities(features);
    }

	@Override
	public Classification<T, K> classify(String unknownTextFilePath) {
    	
    	boolean usingTargetWordsList = true;
    	if (targetPhraseList == null && targetWordList == null) usingTargetWordsList = false;
    	
    	String sCurrentLine;
    	List<String> list = new ArrayList<String>();
    	BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(unknownTextFilePath));
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
		
		return this.classify((Collection<T>) list);
	}

}
