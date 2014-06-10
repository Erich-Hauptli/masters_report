package connections;

import java.util.ArrayList;
import java.util.HashSet;

import user.UserProfile;

/*  Contains commands that draws connections between users.  */
public class ConnectionsCheck implements Connections_Interface{
	HashSet<String> compare_databases = new HashSet<String>();
	HashSet<String> ids = new HashSet<String>();
	ArrayList<String> headers = new ArrayList<String>();
	ArrayList<String> results = new ArrayList<String>();
	ArrayList<String> jobs = new ArrayList<String>();
	
	/*  Prints out paths taken by common users as percentages.  */
    public void find_same(String common_field, String common_field_value, String[] compare_fields) {
        try {
        	UserProfile user = new UserProfile();
        	headers = user.return_headers();
        	//columns = user.query_collumns(null);								//Queries headers for users
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String common_database = null;
        for(String header:headers){							//Determine which database is needed to find common element
        	if (header.toLowerCase().contains(common_field.toLowerCase())){
        		String[] headerArray = header.split(",");
        		common_database = headerArray[0];
        	}
        	for(String compare_field : compare_fields){		//Determine which database is needed to find comparison elements
        		if(header.toLowerCase().contains(compare_field.toLowerCase())){
        			String[] headerArray = header.split(",");
        			compare_databases.add(headerArray[0]);
        		}
        	}
        }
        try {
        	UserProfile user = new UserProfile();			//Find all users with common element
        	results = user.collect_matching_users(common_database, common_field, common_field_value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for(String result : results){						//Put all user ids of users with common element into String[]
        	String[] resultArray = result.split(",");
        	ids.add(resultArray[0]);
        }
        String[] user_ids = ids.toArray(new String[ids.size()]);
        
        try {												//Pull all user data for comparison elements.
        	UserProfile user = new UserProfile();
        	jobs = user.collect_matched_users("job", user_ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        for(String job : jobs){
        	System.out.println(job);
        }


       
        /*
        for(String compare_field : compare_fields){
        	int i = 0;
        	for(String column : columns){
        		if(column.equalsIgnoreCase(compare_field)){	//Determine if column is one of the comparison fields.
        			break;
        		}
        		else i++;
        	}

	        ArrayList<String> Points = new ArrayList<String>();
	        for (String result : results){
	        	String[] split = result.split("\\s*,\\s*");		//Read in each user data and split on ",".
	        	boolean found = false;
	        	for (String point : Points) {
	        	    if (point.equals(split[i])) {			
	        	    	found = true;							//Check if content of field has been seen before.
	        	        break;
	        	    }
	        	}
	        	if(found == false){
	        		Points.add(split[i]);						//If not previously seen, make note of data in field.
	        	}
	        }
	        float size = results.size();
	        for(String point : Points){
	        	float count = 0;
	        	for (String result : results){
	        		String[] split = result.split("\\s*,\\s*");
	        		if(split[i].equals(point)){
	        			count++;								//Count number of times each new piece of data seen.
	        		}
	        	}
	        	//System.out.println(count + "/" + size);
	        	float percentage = ((count * 100) / size);		//Determine percentage of users match each piece of data.
	        	String percent = String.format("%.2f", percentage);
	        	String output = point + ": " + percent + "%";
	        	System.out.println(output);						//Print out results.
	        }
	        System.out.println("\n");
        }
    	*/
    }
	
}
