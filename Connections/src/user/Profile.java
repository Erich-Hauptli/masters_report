package user;

import java.util.ArrayList;

/*  Interface to define various profile tasks that can be completed.  */
public interface Profile {
	void add_user(String[] user_profile, ArrayList<String[]> user_educations, ArrayList<String[]> user_jobs);	//Add a user to the database.
	
	void add_users(String file, String database);    //Add multiple users to the database.
	
    void modify_field(String id, String database, String field, String field_value);  //Modify the a user's profile.
    
    void display_all_users(String database);		//Print out all users.
    
    void display_user(String database, String field, String search_term);	//Display a specific set of users.
    
    ArrayList<String> collect_users(String database, String field, String search_term);  //Produce an ArrayList of specific users.
    
    ArrayList<String> query_collumns(String database);  //Determine user categories.
}
