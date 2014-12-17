package de.hpi.author_support.classification.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionMain {

	public static void main(String[] args) {

		String userID = "JAEYOON";
		String password = "Jaeyoon54";
		String schemaName = "JAEYOON";
		String tableName = "GENDER_CORPUS";
		String indexTableName = "$TA_GENDER_CORPUS_INDEX";
		String text = "what a nice day!";
		String gender = "male";
		
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		
		Connection connection = new Connection(userID, password);
		//connection.insertValue(schemaName, tableName, 3, text, gender);
		//rows = connection.selectWhereIdIs(schemaName, indexTableName, 1);
		System.out.println(connection.selectDistinctID(schemaName, indexTableName));
		connection.close();
		
		//System.out.println(rows);
		
		
		
	}

}
