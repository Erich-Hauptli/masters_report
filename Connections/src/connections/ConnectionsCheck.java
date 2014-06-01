package connections;

import java.util.ArrayList;

import user.UserProfile;

/*  Contains commands that draws connections between users.  */
public class ConnectionsCheck implements Connections{
	
	ArrayList<String> results = new ArrayList<String>();
	ArrayList<String> collumns = new ArrayList<String>();
	
	/*  Prints out paths taken by common users as percentages.  */
    public void find_same(String common_field, String common_field_value, String[] compare_fields) {
        try {
        	UserProfile user = new UserProfile();
        	results = user.collect_users(common_field, common_field_value);  //Queries common users
        	collumns = user.query_collumns();								//Queries headers for users
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for(String compare_field : compare_fields){
        	int i = 0;
        	for(String collumn : collumns){
        		if(collumn.equalsIgnoreCase(compare_field)){	//Determine if column is one of the comparison fields.
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
    }

}
