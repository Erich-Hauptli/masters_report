package sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import files.ReadFile;

public class SQL_DB implements MYsqlDB{
	
	public void download_all(String database) {
		String Manager = "jdbc:sqlite:" + database + ".db";			  //Define database as sqlite.
        Connection conn = null;
        Statement stat = null;
        String query = "SELECT * FROM " + database;					  //Define query as all of database.
        ArrayList<String> headers = null;
        SQL_DB sql_query = new SQL_DB();
		try {
			conn = DriverManager.getConnection(Manager);
			headers = sql_query.query_headers(database);				//Query the column headers.
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);	//Query the database for all content.
            
            while (rs.next()) {
            	String result = "";
            	for(String header : headers){
            		result = result + header + "= " +  rs.getString(header) + "  ";		//Generate String for each database row.
            	}
            System.out.println(result);			//Printout database line by line.
            }
        } catch (SQLException e ) {
        } finally {
            if (stat != null) { try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
        }
    }
	
	/*  download_matches only prints out the rows that contain the field value for a defined field.  */
	public void download_matches(String database, String field, String field_value) {
		String Manager = "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
        Statement stat = null;
        String query = "SELECT * FROM " + database + " WHERE " + field + " LIKE '" + field_value + "'";
        ArrayList<String> headers = null;
        SQL_DB sql_query = new SQL_DB();
		try {
			conn = DriverManager.getConnection(Manager);
			headers = sql_query.query_headers(database);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            
            while (rs.next()) {
            	String result = "";
            	for(String header : headers){
            		result = result + header + "= " +  rs.getString(header) + "  ";
            	}
            	System.out.println(result);
            }
        } catch (SQLException e ) {
        } finally {
            if (stat != null) { try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
        }
    }
	
	/*  collect_matches duplicates the above class, except it returns the result as a ArrayList.  */
	public ArrayList<String> collect_matches(String database, String field, String field_value) {
		String Manager = "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
		try {
			conn = DriverManager.getConnection(Manager);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Statement stat = null;
        String query = "SELECT * FROM " + database + " WHERE " + field + " LIKE '" + field_value + "'";
        ArrayList<String> results = new ArrayList<String>();
        ArrayList<String> headers = null;
        SQL_DB sql_query = new SQL_DB();
		try {
			headers = sql_query.query_headers(database);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
                        
            while (rs.next()) {
            	int i = 0;
            	String result = null;
            	for(String header : headers){
            		if (i == 0){
            			result = rs.getString(header);
            			i++;
            		}
            		else{
            			result = result + "," + rs.getString(header);
            		}
            	}
            	results.add(result);
            }
        } catch (SQLException e ) {
        } finally {
            if (stat != null) { try {
				stat.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} }
        }
		return results;
    }
	
	/*  query_headers pulls the column headers from a database and returns them as an ArrayList*/
	public ArrayList<String> query_headers(String database) {
		ArrayList<String> Collumns = new ArrayList<String>();
        try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String manager =  "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
        DatabaseMetaData meta = null;
        ResultSet rs = null;
        try {
        	conn = DriverManager.getConnection(manager);
			meta = conn.getMetaData();
        	rs = meta.getColumns(null, null, database, null);
			while (rs.next()) {
				String result = rs.getString(4);
				Collumns.add(result);
			}
			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return Collumns;
	}

	
	/*  upload_line adds a single row to a database and creates the database if it does not already exist.  */
    public void upload_line(String database, String[] headers, String[] args) {
    	String Names = "";
    	String Q = null;
    	for(String Name : headers){  //Setup strings for the SQL commands below.
    		if (Names.equals("")){
    			Names = Name;
    			Q = "?";
    		}else{
    			Names = Names + "," + Name;
    			Q = Q + ",?";
    		}
    	}
    	
        try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String manager =  "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
        Statement stmt = null;
        String Prepared = "insert into " + database + " values ("+ Q + ");";  //Setup SQL command to add to database.
        PreparedStatement prep = null;
        String Update = "create table if not exists " + database + "(" + Names + ");";   //Create database if it does not already exist.  
        try {
        	conn = DriverManager.getConnection(manager);
        	stmt = conn.createStatement();
        	stmt.executeUpdate(Update);	//Create table
        	prep = conn.prepareStatement(Prepared);
        	for(int i = 0; i<headers.length; i++){
        		prep.setString(i+1, args[i]);		//Add a line to the database, by loading in data by each column.
        	}
        	prep.addBatch();		//Batch the data into the database once it is ready.
        	conn.setAutoCommit(false);
        	prep.executeBatch();
        	conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*  modify_line provides the path to modifying individual pieces of data within a database.
     *  It requires a user know the id of the line to be modified.  */
    public void modify_line(String database, String id, String field, String field_value) {
        try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String manager =  "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
		try {
			conn = DriverManager.getConnection(manager);
			Statement stmt = conn.createStatement();
			String Modify = "Update " + database + " SET " + field;		//SQL command to modify line.
			Modify = Modify + "='" + field_value + "' WHERE id='" + id + "'";  //command continued.
			stmt.executeUpdate(Modify);
		
			conn.setAutoCommit(false);
			conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*  upload_file provides the path to upload a comma separated file into the database line by line. */
    public void upload_file(String database, String[] headers, String filename){

    	SQL_DB sql_upload = new SQL_DB();
    	
		try{
			ReadFile file = new ReadFile(filename);
			String[] aryLines = file.OpenFile();		//Read in the file.
			
			int i;
			for(i=0; i<aryLines.length; i++){
				String line = aryLines[i];
				String[] split = line.split("\\s*,\\s*");		//Split the data based on ","s.
				try {
					sql_upload.upload_line(database, headers, split);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  //Upload each line of the file into the database.
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
    }
}
