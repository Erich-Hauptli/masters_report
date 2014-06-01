package user;

import java.util.ArrayList;

/*  Interface to define various profile tasks that can be completed.  */
public interface Profile {
	void add_user(String[] user);	//Add a user to the database.
	
	void add_users(String file);    //Add multiple users to the database.
	
    void modify_field(String id, String field, String field_value);  //Modify the a user's profile.
    
    void display_all_users();		//Print out all users.
    
    void display_user(String field, String search_term);	//Display a specific set of users.
    
    ArrayList<String> collect_users(String field, String search_term);  //Produce an ArrayList of specific users.
    
    ArrayList<String> query_collumns();  //Determine user categories.
}
