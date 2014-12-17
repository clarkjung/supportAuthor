package machinelearning;

public class MLMain {

	public static void main(String[] args) {
		String targetWordsListPath = "data/target_words_list.txt";
        Classifier<String, String> bayes = new BayesClassifier<String, String>(targetWordsListPath);
        String formalTrainFolder = "data/train/formal/";
        String informalTrainFolder = "data/train/informal/";
        String formalTestFolder = "data/test/formal/";
        String informalTestFolder = "data/test/informal/";
        
        bayes.learnFolder("formal", formalTrainFolder);
        bayes.learnFolder("informal", informalTrainFolder);
        System.out.println("train done");
        
        System.out.println("====formal test====");
        bayes.classifyFolder("formal", formalTestFolder);
        
        System.out.println("====informal test====");
        bayes.classifyFolder("informal", informalTestFolder);
        
	}

}
