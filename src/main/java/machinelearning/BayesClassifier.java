package machinelearning;

import java.io.BufferedReader;
import java.io.File;
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


/*
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
    
    private float featuresProbabilityProduct(Collection<T> features, K category) {
        float product = 0.0f;
        for (T feature : features) product += Math.log10(this.featureWeighedAverage(feature, category));
        return product;
    }

    private float categoryProbability(Collection<T> features, K category) {
    	float categoryCount = (float) Math.log10((float) this.categoryCount(category));
    	float getCategoriesTotal = (float) Math.log10((float) this.getCategoriesTotal());
    	float featuresProbabilityProduct = featuresProbabilityProduct(features, category);
    	return categoryCount - getCategoriesTotal + featuresProbabilityProduct;
    }

    private SortedSet<Classification<T, K>> categoryProbabilities(Collection<T> features) {
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

    @Override
    public Classification<T, K> classify(Collection<T> features) {
        SortedSet<Classification<T, K>> probabilites = this.categoryProbabilities(features);

        if (probabilites.size() > 0) {
            return probabilites.last();
        }
        return null;
    }

    public Collection<Classification<T, K>> classifyDetailed(Collection<T> features) {
        return this.categoryProbabilities(features);
    }
    
    public void classifyFolder(K category, String folderPath){
    	int count = 0;
    	int hit = 0;
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		int totalSize = listOfFiles.length;
		
		for (File file : listOfFiles) {
			if (file.isFile()) {
				String fileNameOnly = file.getName();
				String fullFilePath = folderPath + fileNameOnly;
				
				if (fullFilePath.endsWith(".txt")) {
					count++;
					if (classify(fullFilePath).getCategory().equals(category)) hit++;
					if (count % 100 == 0) System.out.println("testing " + category + " docs: " + count + "/" + totalSize + " done");
				}
			}
		}
		System.out.println("count: " + count + ", hit: " + hit);
    }

	@SuppressWarnings("unchecked")
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
		
		return this.classify((Collection<T>) list);
	}

}
