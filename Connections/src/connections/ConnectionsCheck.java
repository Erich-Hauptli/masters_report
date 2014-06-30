package connections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

import user.UserProfile;

/*  Contains commands that draws connections between users.  */
public class ConnectionsCheck implements Connections_Interface{
	
	int display_limitor = 5;  //Match percentage required for node to be displayed. 
	
	TreeSet<String> compare_databases = new TreeSet<String>();
	TreeSet<String> ids = new TreeSet<String>();
	ArrayList<String> headers = new ArrayList<String>();
	ArrayList<String> results = new ArrayList<String>();
	ArrayList<String> profiles = new ArrayList<String>();
	ArrayList<String> jobs = new ArrayList<String>();
	ArrayList<String> educations = new ArrayList<String>();
	ArrayList<String> compares = new ArrayList<String>();
	ArrayList<String> Points = new ArrayList<String>();
	ArrayList<String> Nodes = new ArrayList<String>();
	ArrayList<String> User = new ArrayList<String>();
	ArrayList<String> Connects = new ArrayList<String>();
	
	
	/*  Prints out paths taken by common users as percentages.  */
    public void find_same(String common_field, String common_field_value) {
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
        	String[] headerArray = header.split(",");
        	compare_databases.add(headerArray[0]);
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

        
        for(String id : ids){
        	User.clear();
        	Nodes.clear();
        	//System.out.println(id);
        	for(String job : jobs){
        		if(job.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(job);
        			//System.out.println(job);
        		}
        	}
        	for(String education : educations){
        		if(education.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(education);
        			//System.out.println(education);
        		}
        	}
        	
        	int max = Integer.MIN_VALUE;
        	String line = null;
        	for(int i=0; i<Nodes.size(); i++){
        		int min = Integer.MAX_VALUE;
        		for (String Node : Nodes){
        			String[] split = Node.split("\\s*,\\s*");
        			if(Integer.parseInt(split[3]) < min && Integer.parseInt(split[3]) > max){
        				min = Integer.parseInt(split[3]);
        				line = Node;
        			}
        		}
        		User.add(line);
        		max = min;
        	}
        	
        	String A = null;
        	String B = null;
        	for(String user : User){
        		String[] split = user.split("\\s*,\\s*");
        		if(A == null){
        			A = split[1];
        			//System.out.println(A);
        		}else{
        			int found = 0;
        			B = A;
        			A = split[1];
        			//System.out.println(B + "->" + A);
        			int count = 0;
        			if(Connects.isEmpty()){
        				Connects.add(B + "," + A + "," + "1");
        			}else{
        				for(String Connect : Connects){
        					String[] conn = Connect.split("\\s*,\\s*");
        					if(conn[0].equals(B) && conn[1].equals(A)){
        						found = 1;
        						int instances = Integer.parseInt(conn[2]);
        						instances++;
        						String replacement = conn[0] + "," + conn[1] + "," + Integer.toString(instances);
        						Connects.set(count, replacement);
        						break;
        					}else{
        						count++;
        					}
        				}
        				if(found == 0){
        					Connects.add(B + "," + A + "," + "1");
        				}
        			}
        		}
        	}
        }
        
        for (String result : Connects){
        	System.out.println(result);
        }
        
        for(String header:headers){							//Determine which database is needed to find common element
        	
        	int j = 0;
        	String output = null;
        	String[] headerArray = header.split(",");
        	for(int i=1; i<headerArray.length; i++){
        		Points.clear();
        		j = i - 1;
        		if(headerArray[0].equals("profile")){			//Determine which database needs to be searched.
        			compares = profiles;
        		}else if (headerArray[0].equals("job")){
        			compares = jobs;
        		}else if(headerArray[0].equals("education")){
        			compares = educations;
        		}else{
        			System.out.println("Error: Need statement for " + headerArray[0]);
        		}
        		System.out.println(headerArray[i]);
        		
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
        	       	if(percentage > display_limitor){
        	       		String percent = String.format("%.2f", percentage);
        	       		output = point + ": " + percent + "%";
        	       		System.out.println(output);						//Print out results.
        	       	}
        	    }
        	    System.out.println("\n");
           	}
        }
	
    }
}
