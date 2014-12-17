package de.hpi.author_support.excel_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import de.hpi.author_support.classification.db.Connection;

public class ExcelReader {

	static public void excelToDatabase(String filePath, Connection connection){
		try{
			File myFile = new File(filePath);
	        FileInputStream fis = new FileInputStream(myFile);
	        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);
	      	XSSFSheet mySheet = myWorkBook.getSheetAt(0);
	      	Iterator<Row> rowIterator = mySheet.iterator();
	      	
	      	String schemaName = "JAEYOON";
	      	String tableName = "GENDER_CORPUS";
	       
	      	int count = 0;
	        // Traversing over each ROW of XLSX file
	        while (rowIterator.hasNext()) {
	            Row row = rowIterator.next();

	            // For each row, iterate through each COLUMN
	            Iterator<Cell> cellIterator = row.cellIterator();
	            
	            String text = "";
	            String gender = "";
	            
	            while (cellIterator.hasNext()) {

	                Cell cell = cellIterator.next();
	                
	                switch (cell.getColumnIndex()) {
	                case 0:
	                    text = fixApostrophe(cell.getStringCellValue());
	                    break;
	                case 1:
	                    gender = cell.getStringCellValue();
	                    break;
	                default :
	             
	                }
	                
	            }
	            
	            if(text.isEmpty()){
	            	System.out.println("empty string found in row " + count);
	            	continue;
	            }
	            
	            System.out.println(count + " done");
	            count++;
	            connection.insertValue(schemaName, tableName, count, text, gender.trim().toUpperCase());
	            
	        }
	        
	        connection.close();
	        myWorkBook.close();
	        fis.close();
	        
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	static private String fixApostrophe(String originalStr){
		return originalStr.replaceAll("'", "''");
	}
	
}
