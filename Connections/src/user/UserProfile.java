package user;

import java.util.ArrayList;

import sql.SQL_DB;

/*  Implements all Profile commands.  */
public class UserProfile implements Profile{
		String profile_db = "profile";
		String[] profile_headers = {"id", "first_name", "middle_name", "lastname", "gender", "birthday", "e-mail", "phone"};
		String education_db = "education";
		String[] education_headers = {"id", "degree", "specialization", "school", "start_date", "end_date"};
		String job_db = "job";
		String[] job_headers = {"id", "title", "company", "location", "start_date", "end_date"};

		/*  add_user  adds a single user to the database.  */
	    public void add_user(String[] user_profile, ArrayList<String[]> user_educations, ArrayList<String[]> user_jobs) {
	    	SQL_DB sql_upload = new SQL_DB();
	        try {
	        	if(user_profile != null){
	        		sql_upload.upload_line(profile_db, profile_headers, user_profile);
	        	}
	        	if(user_educations != null){
	        		for(String[] user_education : user_educations){
	        			sql_upload.upload_line(education_db, education_headers, user_education);
	        		}
	        	}
	        	if(user_jobs != null){
	        		for(String[] user_job : user_jobs){
	        			sql_upload.upload_line(job_db, job_headers, user_job);
	        		}
	        	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		
	    /* add_users add a comma separated file list of users to the database*/
	    public void add_users(String file, String database) {
	    	SQL_DB sql_upload = new SQL_DB();
	    	String[] headers = null;
	    	if(database.equals("profile")){
	    		headers = profile_headers;
	    	}else if(database.equals("education")){
	    		headers = education_headers;
	    	}else if(database.equals("job")){
	    		headers = job_headers;
	    	}else{
	    		System.out.println("Failed to determine column headers");
	    		return;
	    	}
	        try {
	        	sql_upload.upload_file(database, headers, file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  modify_field changes one element of one row to the requested value.  */
	    public void modify_field(String id, String database, String field, String field_value) {
	    	SQL_DB sql_upload = new SQL_DB();
	        try {
	        	sql_upload.modify_line(database, id, field, field_value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	
	    /*  display_all_users prints out all users contained within a database.  */
	    public void display_all_users(String database) {
	    	SQL_DB sql_download = new SQL_DB();
	        try {
	        	sql_download.download_all(database);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  display_user prints out all users that meet search criteria.  */
	    public void display_user(String database, String field, String search_term) {
	    	SQL_DB sql_download = new SQL_DB();
	        try {
	        	sql_download.download_matches(database, field, search_term);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    /*  collect_users returns all users that meet search criteria as an ArrayList.  */
	    public ArrayList<String> collect_users(String database, String field, String search_term) {
	    	ArrayList<String> results = new ArrayList<String>();
	    	SQL_DB sql_download = new SQL_DB();
	        try {
	        	results = sql_download.collect_matches(database, field, search_term);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return results;
	    }
	    
	    /*  query_collumns returns an ArrayList of all the column headers within the user database. */
	    public ArrayList<String> query_collumns(String database) {
	    	SQL_DB sql_query = new SQL_DB();
	    	ArrayList<String> results = new ArrayList<String>();
	        try {
	        	results = sql_query.query_headers(database);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return results;
	    }

}
