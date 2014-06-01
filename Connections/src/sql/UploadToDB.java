package sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import files.ReadFile;

/*  UploadToDB provides the commands that upload/modify data in a database.  */
public class UploadToDB {
	
	/*  upload_line adds a single row to a database and creates the database if it does not already exist.  */
    public static void upload_line(String database, String[] headers, String[] args) throws Exception {
    	String Names = "";
    	String Q = null;
    	for(String Name : headers){  //Setup strings for the SQL commands below.
    		if (Names.equals("")){
    			Names = Name;
    			Q = "?";
    		}
    		else{
    			Names = Names + "," + Name;
    			Q = Q + ",?";
    		}
    	}
    	
        Class.forName("org.sqlite.JDBC");
        String manager =  "jdbc:sqlite:" + database + ".db";
        Connection conn = DriverManager.getConnection(manager);
        Statement stmt = conn.createStatement();
        String Update = "create table if not exists " + database + "(" + Names + ");";   //Create database if it does not already exist.
        stmt.executeUpdate(Update);   //Create table
        String Prepared = "insert into " + database + " values ("+ Q + ");";  //Setup SQL command to add to database.
        PreparedStatement prep = conn.prepareStatement(Prepared);
        
        for(int i = 0; i<headers.length; i++){
        	prep.setString(i+1, args[i]);			//Add a line to the database, by loading in data by each column.
        }
        prep.addBatch();   //Batch the data into the database once it is ready.
		
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);
        conn.close();
    }
    
    /*  modify_line provides the path to modifying individual pieces of data within a database.
     *  It requires a user know the id of the line to be modified.  */
    public static void modify_line(String database, String id, String field, String field_value) throws Exception {
        Class.forName("org.sqlite.JDBC");
        String manager =  "jdbc:sqlite:" + database + ".db";
        Connection conn = DriverManager.getConnection(manager);
        Statement stmt = conn.createStatement();
        String Modify = "Update " + database + " SET " + field;		//SQL command to modify line.
        Modify = Modify + "='" + field_value + "' WHERE id='" + id + "'";  //command continued.
        stmt.executeUpdate(Modify);
		
        conn.setAutoCommit(false);
        conn.setAutoCommit(true);
        conn.close();
    }
    
    /*  upload_file provides the path to upload a comma separated file into the database line by line. */
    public static void upload_file(String database, String[] headers, String filename) throws Exception{

		try{
			ReadFile file = new ReadFile(filename);
			String[] aryLines = file.OpenFile();		//Read in the file.
			
			int i;
			for(i=0; i<aryLines.length; i++){
				String line = aryLines[i];
				String[] split = line.split("\\s*,\\s*");		//Split the data based on ","s.
				UploadToDB.upload_line(database, headers, split);  //Upload each line of the file into the database.
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
    }
    
  }