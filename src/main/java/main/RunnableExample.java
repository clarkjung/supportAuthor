package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import machinelearning.BayesClassifier;
import machinelearning.Classifier;



public class RunnableExample {

    public static void main(String[] args) {
    	
        /*
         * Create a new classifier instance. The context features are
         * Strings and the context will be classified with a String according
         * to the featureset of the context.
         */
    	String targetWordsListPath = "data/target_words_list.txt";
        Classifier<String, String> bayes = new BayesClassifier<String, String>(targetWordsListPath);
        String formalTrainFolder = "data/train/formal/";
        String informalTrainFolder = "data/train/informal/";
        String formalTestFolder = "data/test/formal/";
        String informalTestFolder = "data/test/informal/";
        
        bayes.learnFolder("formal", formalTrainFolder);
        bayes.learnFolder("informal", informalTrainFolder);
        System.out.println("train done");
        
        System.out.println(bayes.categoryCount("formal"));
        System.out.println(bayes.categoryCount("informal"));
        System.out.println(bayes.featureCount("we will", "formal"));
        System.out.println(bayes.featureCount("we will", "informal"));
        
        System.out.println("====formal test====");
        bayes.classifyFolder("formal", formalTestFolder);
        
        System.out.println("====informal test====");
        bayes.classifyFolder("informal", informalTestFolder);
        
        
        
//        bayes.learn("positive", "data/train1.txt");
//        bayes.learn("negative", "data/train2.txt");
//        //System.out.println(bayes.featureCount("i", "positive"));
//        System.out.println(bayes.classify("data/unknowntext.txt"));

        

        /*
         * The classifier can learn from classifications that are handed over
         * to the learn methods. Imagin a tokenized text as follows. The tokens
         * are the text's features. The category of the text will either be
         * positive or negative.
         */
        //final String[] positiveText = "I love sunny days days.".split("\\s");
        //bayes.learn("positive", Arrays.asList(positiveText));
        
        //bayes.learn("negative", "data/test.txt");
        //bayes.learn("negative", "data/test.txt", "data/targetWordList.txt");
        
        //System.out.println(bayes.featureCount("I", "negative"));

        final String[] negativeText = "I hate rain".split("\\s");
        bayes.learn("negative", Arrays.asList(negativeText));

        /*
         * Now that the classifier has "learned" two classifications, it will
         * be able to classify similar sentences. The classify method returns
         * a Classification Object, that contains the given featureset,
         * classification probability and resulting category.
         */
        final String[] unknownText1 = "today is a sunny day".split("\\s");
        final String[] unknownText2 = "there will be rain".split("\\s");

        System.out.println( // will output "positive"
                bayes.classify(Arrays.asList(unknownText1)).getCategory());
        System.out.println( // will output "negative"
                bayes.classify(Arrays.asList(unknownText2)).getCategory());
        

        /*
         * The BayesClassifier extends the abstract Classifier and provides
         * detailed classification results that can be retrieved by calling
         * the classifyDetailed Method.
         *
         * The classification with the highest probability is the resulting
         * classification. The returned List will look like this.
         * [
         *   Classification [
         *     category=negative,
         *     probability=0.0078125,
         *     featureset=[today, is, a, sunny, day]
         *   ],
         *   Classification [
         *     category=positive,
         *     probability=0.0234375,
         *     featureset=[today, is, a, sunny, day]
         *   ]
         * ]
         */
        ((BayesClassifier<String, String>) bayes).classifyDetailed(
                Arrays.asList(unknownText1));

        /*
         * Please note, that this particular classifier implementation will
         * "forget" learned classifications after a few learning sessions. The
         * number of learning sessions it will record can be set as follows:
         */
        bayes.setMemoryCapacity(500); // remember the last 500 learned classifications
    }

}
