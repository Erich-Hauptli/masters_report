package sql;

import java.util.ArrayList;

/*  Test file to test out SQL Database code.  */

public class DBDemo {
	public static void main(String[] args) throws Exception {

    	
		String file_name = "Test.csv";  	//File to be read in.
		String database = "user";			//Database to be created.
		String[] headers = {"id", "name", "birthday", "education", "degree", "work", "city"};  //Headers of collumns in database.
		
		SQL_DB sql_upload = new SQL_DB();
		SQL_DB sql_query = new SQL_DB();
		SQL_DB sql_download = new SQL_DB();
		
		sql_upload.upload_file(database, headers, file_name);  //Upload the file.
		
		ArrayList<String> Collumns = sql_query.query_headers(database);
		
		for(String Collumn : Collumns){
			System.out.println(Collumn);		//Printout the headers of the database.
		}
    	
    	sql_download.download_all(database);  //Printout the contents of the database.
    	
    	sql_upload.modify_line(database, "1", "work", "ESPN");  //Update database, id=1  set work field.
    	
    	sql_download.download_all(database);   //Printout the contents of the database.
    	
    	sql_download.download_matches(database, "education", "MSU");  //Printout only the entries that education == MSU.
				
    }
}
