package de.hpi.author_support.excel_reader;

import de.hpi.author_support.classification.db.Connection;

public class ExcelReaderMain {

	public static void main(String[] args) {

		String filePath = "data/gender_corpus/blog-gender-dataset.xlsx";
		String userID = "JAEYOON";
		String password = "Jaeyoon54";
		Connection connection = new Connection(userID, password);
		
		ExcelReader.excelToDatabase(filePath, connection);
		
	}

}
