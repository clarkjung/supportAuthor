package de.hpi.author_support.classification.db;

import java.sql.*;
import java.util.ArrayList;

import de.hpi.author_support.classification.dao.IDnGender;

public class Connection {

	static private String connectionURL = "jdbc:sap://141.89.225.134:30315";
	private String userID;
	private String password;
	private java.sql.Connection conn;
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public Connection(String userID, String password){
		this.userID = userID;
		this.password = password;
		openConnection();
	}
	
	private void openConnection(){
		try {
			this.conn = java.sql.DriverManager.getConnection(connectionURL, this.userID, this.password);
			this.stmt = conn.createStatement();
			this.pstmt = null;
			this.rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertValue(String schemaName, String tableName, int id, String text, String gender){	
		
		String sqlCommand = String.format("INSERT INTO %s.%s VALUES (%d, '%s', '%s')", schemaName, tableName, id, text, gender);
		
		try {
			pstmt = conn.prepareStatement(sqlCommand);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<ArrayList<String>> selectWhereIdIs(String schemaName, String indexTableName, int id){

		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		
		try {
			String sqlCommand = String.format("SELECT TA_TYPE, TA_NORMALIZED FROM %s.\"%s\" WHERE ID=%d", schemaName, indexTableName, id);
			ResultSet rs = stmt.executeQuery(sqlCommand);
			while(rs.next()){
				ArrayList<String> row = new ArrayList<String>();
				row.add(0, rs.getString("TA_TYPE"));
				row.add(1, rs.getString("TA_NORMALIZED"));
				rows.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rows;
	}
	
	public ArrayList<IDnGender> selectIDAndGender(String schemaName, String tableName){
		
		ArrayList<IDnGender> rows = new ArrayList<IDnGender>();
		
		try {
			String sqlCommand = String.format("SELECT ID, GENDER FROM %s.%s", schemaName, tableName);
			ResultSet rs = stmt.executeQuery(sqlCommand);
			while(rs.next()){
				IDnGender row = new IDnGender();
				row.setId(Integer.parseInt(rs.getString("ID")));
				row.setGender(rs.getString("GENDER"));
				rows.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return rows;
	}
	
	public int selectDistinctID(String schemaName, String indexTableName){
		
		int count = -1;
		
		try{
			String sqlCommand = String.format("SELECT COUNT(DISTINCT ID) FROM %s.\"%s\"", schemaName, indexTableName);
			ResultSet rs = stmt.executeQuery(sqlCommand);
			while(rs.next()){
				count = Integer.parseInt(rs.getString("COUNT(DISTINCT ID)"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}

		return count;
	}
	
	public void closeConnection(){
		try {
			if(rs!=null) rs.close();
			if(stmt!=null) stmt.close();
			if(pstmt!=null) pstmt.close();
			if(conn!=null) conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

