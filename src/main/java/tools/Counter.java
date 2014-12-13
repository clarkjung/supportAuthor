package tools;

public class Counter {

	static public int countWords(String string){
		String[] countingWords = string.split(" ");
		return countingWords.length;
	}
	
	static public int countChars(String string){
		int counter = 0;
	    for (int i = 0; i < string.length(); i++) {
	      if (Character.isLetter(string.charAt(i)))
	        counter++;
	    }
	    return counter;
	}
	
}