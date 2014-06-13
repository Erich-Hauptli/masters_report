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
	ArrayList<String> profiles = new ArrayList<String>();
	ArrayList<String> jobs = new ArrayList<String>();
	ArrayList<String> educations = new ArrayList<String>();
	ArrayList<String> compares = new ArrayList<String>();
	ArrayList<String> Points = new ArrayList<String>();
	
	
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
        
        String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
        
        try {												//Pull all user data for comparison elements.
        	UserProfile user = new UserProfile();
        	profiles = user.collect_matched_users("profile", user_ids);
        	jobs = user.collect_matched_users("job", user_ids);
        	educations = user.collect_matched_users("education",user_ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
        for(String header:headers){							//Determine which database is needed to find common element
        	for(String compare_field : compare_fields){
        		int j = 0;
        		int found = 0;
        		String output = null;
        		Points.clear();
        		if (header.toLowerCase().contains(compare_field.toLowerCase())){
        			String[] headerArray = header.split(",");
        			for(int i=0; i<headerArray.length; i++){
        				if (headerArray[i].equals(compare_field.toLowerCase())){
        					System.out.println(compare_field);
        					j = i - 1;
        					found = 1;
        					if(headerArray[0].equals("profile")){
        						compares = profiles;
        					}else if (headerArray[0].equals("job")){
        						compares = jobs;
        					}else if(headerArray[0].equals("education")){
        						compares = educations;
        					}else{
        						System.out.println("Error: Need statement for " + headerArray[0]);
        					}
        					break;
        				}
        			}
        		}
        		if(found == 1){
        	        for (String compare : compares){
        	        	String[] split = compare.split("\\s*,\\s*");		//Read in each user data and split on ",".
        	        	boolean exist = false;
        	        	for (String point : Points) {
        	        	    if (point.equals(split[j])) {			
        	        	    	exist = true;							//Check if content of field has been seen before.
        	        	        break;
        	        	    }
        	        	}
        	        	if(exist == false){
        	        		Points.add(split[j]);						//If not previously seen, make note of data in field.
        	        	}
        	        }
        	        float size = ids.size();
        	        for(String point : Points){
        	        	float count = 0;
        	        	String id = "0";
        	        	String prev_id = "0";
        	        	for (String compare : compares){
        	        		String[] split = compare.split("\\s*,\\s*");
        	        		id = split[0];
        	        		if(split[j].equals(point) && !id.equals(prev_id)){
        	        			prev_id = id;
        	        			count++;								//Count number of times each new piece of data seen.
        	        		}
        	        	}
        	        	//System.out.println(count + "/" + size);
        	        	float percentage = ((count * 100) / size);		//Determine percentage of users match each piece of data.
        	        	String percent = String.format("%.2f", percentage);
        	        	output = point + ": " + percent + "%";
        	        	System.out.println(output);						//Print out results.
        	        }
        	        System.out.println("\n");
        		}
        	}
        }

    }
}
