package user;

import java.util.ArrayList;

import sql.DownloadFromDB;
import sql.QueryDB;
import sql.UploadToDB;

/*  Implements all Profile commands.  */
public class UserProfile implements Profile{
		String database = "user";
		String[] headers = {"id", "name", "birthday", "education", "degree", "work", "city"};

		/*  add_user  adds a single user to the database.  */
	    public void add_user(String[] user_info) {
	        try {
	        	UploadToDB.upload_line(database, headers, user_info);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
	    /* add_users add a comma separated file list of users to the database*/
	    public void add_users(String file) {
	        try {
	        	UploadToDB.upload_file(database, headers, file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  modify_field changes one element of one row to the requested value.  */
	    public void modify_field(String id, String field, String field_value) {
	        try {
	        	UploadToDB.modify_line(database, id, field, field_value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
	    /*  display_all_users prints out all users contained within a database.  */
	    public void display_all_users() {
	        try {
	        	DownloadFromDB.download_all(database);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  display_user prints out all users that meet search criteria.  */
	    public void display_user(String field, String search_term) {
	        try {
	        	DownloadFromDB.download_matches(database, field, search_term);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  collect_users returns all users that meet search criteria as an ArrayList.  */
	    public ArrayList<String> collect_users(String field, String search_term) {
	    	ArrayList<String> results = new ArrayList<String>();
	        try {
	        	results = DownloadFromDB.collect_matches(database, field, search_term);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return results;
	    }
	    
	    /*  query_collumns returns an ArrayList of all the column headers within the user database. */
	    public ArrayList<String> query_collumns() {
	    	ArrayList<String> results = new ArrayList<String>();
	        try {
	        	results = QueryDB.query_headers(database);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return results;
	    }

}
