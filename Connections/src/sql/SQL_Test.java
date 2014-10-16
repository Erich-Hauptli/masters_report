package sql;

import java.util.ArrayList;

/*  Test file to test out SQL Database code.  */

public class SQL_Test {
	public static void main(String[] args) throws Exception {
    	
		String file_name = "Test.csv";  	//File to be read in.
		String profile_db = "profile";			//Database to be created.
		String[] profile_headers = {"id", "first_name", "middle_name", "last_name", "gender", "birthday", "email", "phone"};  //Headers of collumns in database.
		String education_db = "education";
		String[] education_headers = {"id", "degree", "specialization", "school", "start", "end"};
		String job_db = "job";
		String[] job_headers = {"id", "company", "title", "location", "start", "end"};
		
		SQL_DB sql_upload = new SQL_DB();
		SQL_DB sql_query = new SQL_DB();
		SQL_DB sql_download = new SQL_DB();
		 
		sql_upload.setup();
		sql_upload.declare_database(profile_db, profile_headers);
		sql_upload.declare_database(education_db, education_headers);
		sql_upload.declare_database(job_db, job_headers);
		sql_download.print_all("headers");
		
		sql_upload.upload_file(file_name);  //Upload the file.
		
		/*ArrayList<String> Columns = sql_query.query_headers(profile_db);
		
		for(String Column : Columns){
			System.out.println(Column);		//Printout the headers of the database.
		}
		*/
		
		String[] ids = {"1","2","3","4"};
		
		ArrayList<String> Users = sql_query.collect_matches_or(job_db, "id", ids);
		for(String User: Users){
			System.out.println(User);
		}
    	
    	//sql_download.print_all(education_db);  //Printout the contents of the database.
    	
    	//sql_upload.modify_line(education_db, "1", "school", "UT");  //Update database, id=1  set work field.
    	
    	//sql_download.print_all(profile_db);   //Printout the contents of the database.
    	/*
		for(int i=1; i<3; i++){
			String id = Integer.toString(i);
			System.out.println("User " + id + "\n");
			sql_download.print_matches(profile_db, "id", id);
			sql_download.print_matches(education_db, "id", id);
			sql_download.print_matches(job_db, "id", id);
		}
		*/		
    }
}