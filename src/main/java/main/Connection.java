package main;
import java.sql.*;

public class Connection {

	public static void main(String[] args) {
		try {
			//Class.forName("com.sap.db.jdbc.Driver");
			String connectionURL = "jdbc:sap://141.89.225.134:30315";
			String userId = "JAEYOON";
			String password = "Jaeyoon54";
			java.sql.Connection conn = java.sql.DriverManager.getConnection(connectionURL, userId, password);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM JAEYOON.GENDER_CORPUS");
			//ResultSet rs = stmt.executeQuery("SELECT * FROM SMA1415.JJTEST");
			//PreparedStatement pstmt = conn.prepareStatement("INSERT INTO SMA1415.JJTEST VALUES (7, 'my name is hasso plattner')");
			//pstmt.executeUpdate();
			while(rs.next()){
				System.out.println(rs.getString("ID") + ": " + rs.getString("TEXT"));
			}
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
