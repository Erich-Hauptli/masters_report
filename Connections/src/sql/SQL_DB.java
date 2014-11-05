package sql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import files.Files;

public class SQL_DB implements SQL_Interface{
	
	public void print_all(String database) {
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
	
	/*  collect_matches duplicates the above class, except it returns the result as a ArrayList.  */
	public ArrayList<String> return_all(String database) {
		String Manager = "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
		try {
			conn = DriverManager.getConnection(Manager);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        Statement stat = null;
        String query = "SELECT * FROM " + database;					  //Define query as all of database.
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
	
	/*  download_matches only prints out the rows that contain the field value for a defined field.  */
	public void print_matches(String database, String field, String field_value) {
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
	
	/*  collect_matches duplicates the above class, except it returns the result as a ArrayList.  */
	public ArrayList<String> collect_matches_or(String database, String field, String[] field_values) {
		String Manager = "jdbc:sqlite:" + database + ".db";
        Connection conn = null;
		try {
			conn = DriverManager.getConnection(Manager);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int query_count = 0;
		String query_values = null;
		for(String field_value: field_values){
			if(query_count == 0){
				query_count++;
				query_values = field + " LIKE '" + field_value + "'";
			}else if(query_count < 500){
				query_count++;
				query_values = query_values + " OR " + field + " LIKE '" + field_value + "'";
			}
		}
		
        Statement stat = null;
        String query = "SELECT * FROM " + database + " WHERE " + query_values;
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

    	String Q = null;
    	for(int i=0; i<headers.length; i++){
    		if (i==0){
    			Q = "?";
    		}else{
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
        String Prepared = "insert into " + database + " values ("+ Q + ");";  //Setup SQL command to add to database.
        PreparedStatement prep = null;
        try {
        	conn = DriverManager.getConnection(manager);
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
    public void upload_file(String filename){

    	SQL_DB sql_upload = new SQL_DB();
    	SQL_DB sql_download = new SQL_DB();
    	
		Files file = new Files();
		file.ReadFile(filename);
		String[] aryLines = file.OpenFile();		//Read in the file.
		ArrayList<String> matches = null;
		
		for(int i=0; i<aryLines.length; i++){
			String database = null;
			String line = aryLines[i];
			String[] data = line.split("\\s*,\\s*");		//Split the data based on ","s.
			ArrayList<String> data_list = new ArrayList<String>();
			for(int j=0; j<data.length; j++){
				if(j==0){
					database = data[j];
				}else{
					data_list.add(data[j]);
				}
			}
			String[] filedata=data_list.toArray(new String[data_list.size()]);
			
			try {
				matches = sql_download.collect_matches("headers", "database", database);
				if(matches.size() == 0){
					throw new Exception("Please declare database: " + database + ":\n");
				}
				else if(matches.size() > 1){
					throw new Exception("headers.db broken!");
				}
				else{
					String header = matches.get(0);
					ArrayList<String> header_array = new ArrayList<String>(Arrays.asList(header.split("\\s*,\\s*")));
					header_array.remove(0);
					String[] headers = new String[header_array.size()];
					headers = header_array.toArray(headers);
					int h_size = headers.length;
					int s_size = filedata.length;
					if (h_size == s_size){
						sql_upload.upload_line(database, headers, filedata);
					}
					else{
						throw new Exception("Line does not contain correct information for database.\n   Header: " + h_size + "\n   Data: " + s_size);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}  //Upload each line of the file into the database.
		}
    }
    
    /*  upload_file provides the path to upload a comma separated file into the database line by line. */
    public void declare_database(String database, String[] headers){
    	String db = "headers";
    	
    	String all_headers = null;
    	for(String header:headers){
    		if (all_headers == null){
    			all_headers = header;
    		}else{
    			all_headers = all_headers + "," + header;
    		}
    	}
    	
    	String[] columns = {"database", "column_headers"};
    	String[] column_values = {database, all_headers};
    	
		SQL_DB sql_query = new SQL_DB();
		ArrayList<String> entries = null;
    	try{
    		entries = sql_query.collect_matches(db, "database", database);
    	
    		if(entries.size() == 0  || entries == null){
    			SQL_DB sql_upload = new SQL_DB();
    			sql_upload.upload_line(db, columns, column_values);
    		}
    		else{
    			throw new Exception("Database already defined!\n" + entries);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
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
        String Update = "create table if not exists " + database + "(" + all_headers + ");";   //Create database if it does not already exist.  
        try {
        	conn = DriverManager.getConnection(manager);
        	stmt = conn.createStatement();
        	stmt.executeUpdate(Update);	//Create table
        	conn.setAutoCommit(false);
        	conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void setup(){
        String manager =  "jdbc:sqlite:headers.db";
        Connection conn = null;
        Statement stmt = null;
        String Update = "create table if not exists headers(database, column_headers);";   //Create database if it does not already exist.  
        try {
        	conn = DriverManager.getConnection(manager);
        	stmt = conn.createStatement();
        	stmt.executeUpdate(Update);	//Create table
        	conn.setAutoCommit(false);
        	conn.setAutoCommit(true);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
