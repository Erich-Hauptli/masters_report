package data_collection;

import java.util.ArrayList;
import java.util.TreeSet;

import user.UserProfile;

public class Data_Collection implements Data_Collection_Interface{
	/*  Returns all user ids who match a common field value.  */
    public TreeSet<String> find_same(String common_field, String common_field_value) {
    	TreeSet<String> ids = new TreeSet<String>();
    	ArrayList<String> headers = new ArrayList<String>();
    	ArrayList<String> results = new ArrayList<String>();
    	
        try {
        	UserProfile user = new UserProfile();
        	headers = user.return_headers();					//Queries headers for users
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String common_database = null;
        for(String header:headers){							//Determine which database is needed to find common element
        	if (header.toLowerCase().contains(common_field.toLowerCase())){  //Search for matches
        		String[] headerArray = header.split(",");
        		common_database = headerArray[0];			//Pull database that is needed to search for common element
        	}			
        }
        try {
        	UserProfile user = new UserProfile();			//Find all database rows with common element
        	results = user.collect_matching_users(common_database, common_field, common_field_value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for(String result : results){
        	//System.out.println(result);
        	String[] resultArray = result.split(",");		//Split data from each matching database row
        	ids.add(resultArray[0]);						//Push the user id into a Tree Set
        }
        
        return ids;
    }    
    
	/*  Returns all user ids who match a common field value.  */
    public ArrayList<String> find_all_node_data(String common_field, String common_field_value) {
    	ArrayList<String> headers = new ArrayList<String>();
    	ArrayList<String> result = new ArrayList<String>();
    	ArrayList<String> results = new ArrayList<String>();
    	
        try {
        	UserProfile user = new UserProfile();
        	headers = user.return_headers();					//Queries headers for users
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String common_database = null;
        for(String header:headers){							//Determine which database is needed to find common element
        	if (header.toLowerCase().contains(common_field.toLowerCase())){  //Search for matches
        		String[] headerArray = header.split(",");
        		common_database = headerArray[0];			//Pull database that is needed to search for common element
        	}			
        }
        
        try {
        	UserProfile user = new UserProfile();			//Find all database rows with common element
        	result = user.collect_matching_users(common_database, common_field, common_field_value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(String data: result){
        	String data_return = common_database + "," + data;
        	results.add(data_return);
        }
        
        return results;
    }  
    
    /*  Returns all data associated with a specific database and a set of user ids.  */
    public ArrayList<String> database_pull(TreeSet<String> ids, String database) {
        ArrayList<String> results = new ArrayList<String>();
        String[] user_ids = ids.toArray(new String[ids.size()]);  //Convert Tree Set of ids into a String array.
            
        try {												//Pull all user data for comparison elements.
        	UserProfile user = new UserProfile();
        	results = user.collect_matched_users(database,user_ids);  //Step through database to pull all data for the users identified within the Tree Set of ids
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;		//Return an array list of all the pulled data
	}
}
