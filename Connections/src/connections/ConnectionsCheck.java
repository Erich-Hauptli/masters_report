package connections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import files.Files;
import user.UserProfile;
import tools.MultiReturn;
import tools.Tools;



/*  Contains commands that draws connections between users.  */
public class ConnectionsCheck implements Connections_Interface{
	
	int display_limitor = 5;  //Match percentage required for node to be displayed. 

	ArrayList<String> compares = new ArrayList<String>();
	ArrayList<String> Points = new ArrayList<String>();

	
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

	public void generate_arff(String name, ArrayList<String> input){
		String file_name = name + ".arff";
		
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("@" + name);
		
		Files file = new Files();
		file.WriteFile(file_name,lines);
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

    /*  Returns all the connections between nodes and the weight/frequency an connecting edge is taken.  */
	public MultiReturn find_edges(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		int edge_intervals = 10;	//Breaks the edges into 10 categories, can redifine to any number of groupings
	
		ArrayList<String> Connects = new ArrayList<String>();
		ArrayList<String> Connections = new ArrayList<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		ArrayList<String> User = new ArrayList<String>();
		
		if(ids.isEmpty()){
			System.out.println("No IDs Provided for Find Edges");
			System.exit(0);
		}else if(profiles.isEmpty()){
			System.out.println("No Profiles Provided for Find Edges");
			System.exit(0);
		}else if(jobs.isEmpty()){
			System.out.println("No Jobs Provided for Find Edges");
			System.exit(0);
		}else if(educations.isEmpty()){
			System.out.println("No Educations Provided for Find Edges");
			System.exit(0);
		}

        for(String id : ids){
        	User.clear();
        	Nodes.clear();
        	for(String job : jobs){			//Pull Data from jobs database and add to Nodes
        		if(job.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(job);						
        		}
        	}
        	for(String education : educations){		//Pull Data from educations database and add to Nodes
        		if(education.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(education);
        		}
        	}
        	
        	//Sorting user nodes, by date, earliest first in array list
        	int max = Integer.MIN_VALUE;				//Set max to the absolute min
        	String line = null;
        	for(int i=0; i<Nodes.size(); i++){			//Step through all data
        		int min = Integer.MAX_VALUE;			//Set min to the absolute max
        		for (String Node : Nodes){				//Step through each row of data
        			String[] split = Node.split("\\s*,\\s*"); 
        			if(Integer.parseInt(split[3]) < min && Integer.parseInt(split[3]) > max){ //Check to see if current data is earliest seen node
        				min = Integer.parseInt(split[3]);									  //If it is set this node, as the current earliest
        				line = Node;														  //Store this node
        			}
        		}
        		User.add(line);																  //After all nodes considered, store the earliest node
        		max = min;																	  //Set Max to this value
        	}
        	
        	//Gather a list of all edges
        	String A = null;
        	String B = null;
        	for(String user : User){							//For each user
        		String[] split = user.split("\\s*,\\s*");		
        		if(A == null){									//Start out with initial node as null, and set the first value to A
        			A = split[1];
        		}else{
        			int found = 0;
        			B = A;										//Push A into B
        			A = split[1];								//Then override A with user data
        			//System.out.println(B + "->" + A);
        			int count = 0;
        			if(Connects.isEmpty()){						//If this transition has not been seen before initialize it to 1
        				Connects.add(B + "," + A + "," + "1");
        			}else{
        				for(String Connect : Connects){
        					String[] conn = Connect.split("\\s*,\\s*");
        					if(conn[0].equals(B) && conn[1].equals(A)){		//Otherwise search for the previously stored instance
        						found = 1;									//When it is found, indicate so by setting found integer
        						int instances = Integer.parseInt(conn[2]);	//Pull the current counter for that transition
        						instances++;								//Increment it
        						String replacement = conn[0] + "," + conn[1] + "," + Integer.toString(instances);	//Create a new value to load in place of the old value
        						Connects.set(count, replacement);			//Then replace the data in the array list
        						break;
        					}else{
        						count++;									//If the data does not match, increment the pointer to check next row of data
        					}
        				}
        				if(found == 0){
        					Connects.add(B + "," + A + "," + "1");			//If the data was not previously stored in the array list, add it
        				}
        			}
        		}
        	}
        }
        
      //Convert all node interchanges to weight of # of edge intervals.
        int num_users = ids.size();			//pull total number of users	
        JSONArray ja = new JSONArray();
        for (String Connect : Connects){	//Step through all the node to node connections
        	String[] node = Connect.split("\\s*,\\s*");
        	int node_users = Integer.parseInt(node[2]);		//Pull the number of transitions for each node to node transition
        	float weight = (node_users / ( num_users / edge_intervals));	//Calculate the weight, by dividing the number of transitions by the number of users, then divide by the weight factor
        	int line_weight = (int) weight;		//Convert line weight into an integer
        	String Connect_temp = node[0] + "," + node[1] + "," + line_weight;		//Add it to the array list
        	Connections.add(Connect_temp);
        	JSONObject jo = new JSONObject();
            try {
            	jo.put("node A", node[0]);					//Also add the data to a JSON Object
    			jo.put("node B", node[1]);
    			jo.put("transition frequency", line_weight);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            ja.put(jo);										//Then add the data to a JSON Array
        }
        JSONObject node_connections = new JSONObject();
        try {
			node_connections.put("Node Connections", ja);		//Finally add all the node transitions to a JSON Object
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        MultiReturn result = new MultiReturn(node_connections, Connections);		//Return both the ArrayList and the JSON Object containing the node transitions
        return result;
	}
    
	/*  Returns the oder a node should be displayed so that a path can be mapped from node to node.  */
	public MultiReturn find_node_order(ArrayList<String> Connections) {
		HashSet<String> AllNodes = new HashSet<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		HashSet<String> HeavyEdges = new HashSet<String>();
		HashSet<String> Node0 = new HashSet<String>();
		HashSet<String> NodeFinal = new HashSet<String>();
		HashSet<String> NodeTemp = new HashSet<String>();
		HashSet<String> NodeRemove = new HashSet<String>();
		HashSet<String> NodeStore = new HashSet<String>();
		ArrayList<String> NodeReturn = new ArrayList<String>();
		
		if(Connections.isEmpty()){
			System.out.println("No Data Provided for Node Ordering");
			System.exit(0);
		}
		for(String Connect : Connections){				//Get HashSet of all nodes
			String[] node = Connect.split("\\s*,\\s*");
			AllNodes.add(node[0]);
			AllNodes.add(node[1]);
		}
		System.out.println("\n\n\n");
		for(String node : AllNodes){			//For each node
			int weight = -1;
			int weight_tmp = -1;
			String node_tmp = null;
			String nodeSave_in = null;
			String nodeSave_out = null;
			for(String Connect : Connections){	
				String[] nodes = Connect.split("\\s*,\\s*");
				if(node.equalsIgnoreCase(nodes[0])){		//If the row is applicable to the node in current for loop
					weight = Integer.parseInt(nodes[2]);	//Set the weight to the frequency traveled in this row
					node_tmp = Connect;
				}
				if(weight > weight_tmp && node_tmp != null){	//If the frequency traveled is the greatest seen traveling to this node, 
					nodeSave_out = node_tmp;						//Store the data as the most frequent traveling to the node.
				}
			}
			if(nodeSave_out != null){
				HeavyEdges.add(nodeSave_out);						//After all rows considered, store the most traveled to the node transition for the node in a permanent location.
			}
			
			for(String Connect : Connections){					//Repeat for the most traveled edge from the node.
				String[] nodes = Connect.split("\\s*,\\s*");
				if(node.equalsIgnoreCase(nodes[1])){
					weight = Integer.parseInt(nodes[2]);
					node_tmp = Connect;
				}
				if(weight > weight_tmp && node_tmp != null){
					nodeSave_in = node_tmp;
				}
			}
			if(nodeSave_in != null){
				HeavyEdges.add(nodeSave_in);						//Also add the most traveled edge from the node to the permanent location.
			}
		}
		
		if(HeavyEdges != null){
			for(String Connect : HeavyEdges){				//Step through the most traveled edges to find the starting node
				int found = 0;
				String[] node = Connect.split("\\s*,\\s*");
				String nodeA = node[0];
				for(String test : Connections){				//Searches to see if a starting node is also a destination node
					String[] node_temp = test.split("\\s*,\\s*");
					String nodeB = node_temp[1];
					if(nodeA.equalsIgnoreCase(nodeB)){		//If it is, break and move onto the next node
						found = 1;
						break;
					}
				}
				if(found == 0){
					Node0.add(nodeA);						//If it is not, this is a starting node
					
				}
			}
			
			for(String Connect : HeavyEdges){				//Step through the most traveled edges to find the Final Node
				int found = 0;
				String[] node = Connect.split("\\s*,\\s*");
				String nodeB = node[1];
				String node_A = node[0];
				if(!nodeB.equalsIgnoreCase(node_A)){		
					for(String test : Connections){			//Searches to see if a destination node is also a starting node
						String[] node_temp = test.split("\\s*,\\s*");
						String nodeA = node_temp[0];
						if(nodeB.equalsIgnoreCase(nodeA)){		//If it is, break and move onto the next node
							found = 1;
							break;
						}
					}
				}
				if(found == 0){
					NodeFinal.add(nodeB);						//If it is not, this is a ending node.
				}
			}
		}
        
        NodeStore = Node0;
        for(String node : Node0){
        	Nodes.add(node + ",0");								//Add the starting nodes to the order as 0.
        }
        
        HashSet<String> RemainingNodes = new HashSet<String>(AllNodes);
        for(String start: Node0){
        	RemainingNodes.remove(start);
        }
        
        int group = 1;
        while(RemainingNodes.isEmpty() == false){	//Work through this loop until the NodeStore node matches the final node
        	String NS = null;
        	NS = NodeStore.toString();						//Store node as a string
        	NS = NS.substring(1, NS.length()-1);			//Chop off brackets
        	String [] NSarray = null;
        	NSarray = NS.split("\\s*,\\s*");				//Split on ,
        	NodeTemp.clear();
        	NodeRemove.clear();
        	for(String node : NSarray){
        		for(String test : HeavyEdges){				//Step through list of most traveled edges
        			//System.out.println("Heavy Edge: " + test);
        			String[] node_temp = test.split("\\s*,\\s*");
        			String nodeA = node_temp[0];			//Set starting node in transition point to A
        			String nodeB = node_temp[1];			//Set ending node in transition point to B
        			if(node.equalsIgnoreCase(nodeA)){		//Check if the starting node matches the previous group node
        				NodeTemp.add(nodeB);				//If it does, add the ending node to the next group
        			}
        		}
        	}
        	for(String temp: NodeTemp){
        		for(String test : HeavyEdges){				//Step through list of most traveled edges
        			String[] node_temp = test.split("\\s*,\\s*");
        			String nodeA = node_temp[0];			//Set starting node in transition point to A
        			String nodeB = node_temp[1];			//Set ending node in transition point to B
        			if(temp.equalsIgnoreCase(nodeA) && NodeTemp.contains(nodeB)){		//Check if the starting node matches the previous group node
        				NodeRemove.add(nodeB);				//If it does, add the ending node to the next group
        			}
        		}
        	}
        	for(String remove:NodeRemove){
        		NodeTemp.remove(remove);
        	}
        	for(String nodeB:NodeTemp){
        		if(RemainingNodes.contains(nodeB)){
        			Nodes.add(nodeB + "," + group);
        			RemainingNodes.remove(nodeB);
        			//System.out.println("Group " + group + ": " + nodeB);
        		}
        	}
        	
        	group++;				//increment the group number
    		NodeStore = NodeTemp;	//Set the ending node to the new previous group node
        }
        
        JSONArray ja = new JSONArray();
        for(String each_node : AllNodes){		//Step through list of all nodes, looping until each one hit
        	String node_store = null;
        	for(String node : Nodes){		//Eliminate duplicate node by using later node.
        		String[] node_temp = node.split("\\s*,\\s*");	//Split list of ending node and group
        		if(node_temp[0].equalsIgnoreCase(each_node) && node_store == null){		//Check if node B equals currently stored node and referenced node
        			node_store = node;													//If it does, store it
        		}else if(node_temp[0].equalsIgnoreCase(each_node)){						//If it does not equal currently stored node, but does equal referenced node
        			String[] node_store_split = node_store.split("\\s*,\\s*");
        			if(Integer.parseInt(node_store_split[1]) < Integer.parseInt(node_temp[1])){  //Check if the stored node is in earlier group than current node
        				node_store = node;														  //If it is not, store the current node
        			}
        		}
        	}
        	NodeReturn.add(node_store);								//Add this stored node to an array
        	String[] json_split = node_store.split("\\s*,\\s*");
        	
        	JSONObject jo = new JSONObject();
        	try {
        		jo.put("node name", json_split[0]);					//Also add this stored node to a JSON Object
				jo.put("order", json_split[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	ja.put(jo);		//Then add the JSON Object to an array
        }
        JSONObject node_ordering = new JSONObject();	//Finally create a JSON Object containing the array of nodes with their defined ordering
        try {
			node_ordering.put("Node Ordering", ja);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        MultiReturn result = new MultiReturn(node_ordering, NodeReturn);  //Return both the JSON Object and the Array List<String> containing the node ordering
        return result;
	}

	/*  Returns the all the information about a node in a JSON Object and an ArrayList<String> */
	public MultiReturn find_node_info(int display_limitor, String node, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> results_all = new ArrayList<String>();
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String> complete = new ArrayList<String>();
		ArrayList<String> relevant = new ArrayList<String>();
		HashSet<String> store = new HashSet<String>();
		HashSet<String> store_all = new HashSet<String>();
		
		JSONObject  json_node = new JSONObject();
		try {
			json_node.put("Node Name", node);		//Setup the JSON Return Object with the node name.
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int user_count = 0;
		for(String profile : profiles){		//For each piece of profile data, tag it as profile data, then add it to the complete array list.
			String profile_tmp = "profile," + profile;
			complete.add(profile_tmp);
			user_count++;			//Since there will only be one piece of profile data per user, adding a count here to determine how many users are returned in query
		}
		for(String job : jobs){				//For each piece of job data, tag it as job data, then add it to the complete array list.
			String job_tmp = "job," + job;
			complete.add(job_tmp);
		}
		for(String education : educations){	//For each piece of education data, tag it as education data, then add it to the complete array list.
			String education_tmp = "education," + education;
			complete.add(education_tmp);
		}
		

		float size = 0;
		int count = 0;
		String node_type = null;
		for(String nodes : complete){		//Step through complete list of data for all users
			String[] split = nodes.split("\\s*,\\s*");
			if(nodes.contains(node)){		//Check if data is for the current node being surveyed
				node_type = split[0];
				size++;
				relevant.add(nodes);		//If it is add it to the relevant list
				count = split.length;
			}
		}
		
		try {
        	UserProfile user = new UserProfile();
        	headers = user.return_headers();								//Queries headers for users
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String header_store = null;
		for(String header : headers){
			if(header.contains(node_type)){		//Find the header associated with the data being looked at for the node
				header_store = header;
			}
		}
		
		String[]find_node_category = header_store.split("\\s*,\\s*");
		String node_category = find_node_category[2];
		
		ConnectionsCheck connection = new ConnectionsCheck();
		ArrayList<String> All_Node_Data = new ArrayList<String>();
		All_Node_Data = connection.find_all_node_data(node_category, node);
		int size_all = All_Node_Data.size();
		
		JSONArray  json_array = new JSONArray();
		for(int i=1; i<count-1; i++){
			JSONObject data_field = new JSONObject();
			JSONArray  ja = new JSONArray();
			store.clear();
			store_all.clear();
			String[] column_headers = header_store.split("\\s*,\\s*");
			String column_header = column_headers[i+1];					//Step through each column of database data, based on header data
			if(column_header.equals("start_date")){						//Separate section for years spent at a node, due to the fact that starting and end years are provided
				column_header = "Years";
				for(String nodes : relevant){							//Step through array list containing the previously pulled relevant data
					String[] split = nodes.split("\\s*,\\s*");
					int start_date = Integer.parseInt(split[i+1]);		//Pull the start and end dates
					int end_date = start_date;
					if(split[i+2].equalsIgnoreCase("Current")){			//If the end date is "current" substitue the current year into the data
						end_date = Calendar.getInstance().get(Calendar.YEAR);
					}else{
						end_date = Integer.parseInt(split[i+2]);
					}
					int years = end_date - start_date;				//Calculate the years spent at node
					String value = Integer.toString(years);
					store.add(value);								//Add this value to a hash set to get all the various possible years spent at a node
				}
				for(String nodes : All_Node_Data){						//Step through array list containing all the data for a node
					String[] split = nodes.split("\\s*,\\s*");
					int start_date = Integer.parseInt(split[i+1]);		//Pull the start and end dates
					int end_date = start_date;
					if(split[i+2].equalsIgnoreCase("Current")){			//If the end date is "current" substitue the current year into the data
						end_date = Calendar.getInstance().get(Calendar.YEAR);
					}else{
						end_date = Integer.parseInt(split[i+2]);
					}
					int years = end_date - start_date;				//Calculate the years spent at node
					String value = Integer.toString(years);
					store_all.add(value);								//Add this value to a hash set to get all the various possible years spent at a node
				}
	
				try {
					data_field.put("Data Point Name", column_header);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//For Relevant Data only
				//This section is trying to group the number of users who have equivalent time spent at a node, to gather a percentage for each amount of time spent
				for(String value : store){						//Step through all the possible values of years spent at a node
					float value_count = 0;
					for(String nodes : relevant){				//Compare the possible years spent on a node, to each user
						String[] split = nodes.split("\\s*,\\s*");
						int start_date = Integer.parseInt(split[i+1]);		//Pull the start and end dates
						int end_date = start_date;
						if(split[i+2].equalsIgnoreCase("Current")){
							end_date = Calendar.getInstance().get(Calendar.YEAR);	//Fix Current to be the current year
						}else{
							end_date = Integer.parseInt(split[i+2]);
						}
						int years = end_date - start_date;			//Calculate the years at a node
						String year = Integer.toString(years);
						if(year.contains(value)){
							value_count++;							//Increment counter, to count number of users spent equal time for each possible time span
						}
					}
					float percentage = ((value_count * 100) / size);		//Determine percentage of users match each piece of data.
					if(percentage > display_limitor){						//Check if we want to display this data, or if it is not significant enough
						String output = column_header + "," + value + "," + percentage + "%";  //If significant enough setup an output string for the array list
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value);							//Also add the data to a JSON Object
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);											//Then push this data into a JSON Array containing the data for all different year possibilities
						results.add(output);								//Add the array list data to the output array list data
					}
				}
				//For ALL NODE DATA
				
				for(String value : store_all){						//Step through all the possible values of years spent at a node
					float value_count = 0;
					for(String nodes : All_Node_Data){				//Compare the possible years spent on a node, to each user
						String[] split = nodes.split("\\s*,\\s*");
						int start_date = Integer.parseInt(split[i+1]);		//Pull the start and end dates
						int end_date = start_date;
						if(split[i+2].equalsIgnoreCase("Current")){
							end_date = Calendar.getInstance().get(Calendar.YEAR);	//Fix Current to be the current year
						}else{
							end_date = Integer.parseInt(split[i+2]);
						}
						int years = end_date - start_date;			//Calculate the years at a node
						String year = Integer.toString(years);
						if(year.contains(value)){
							value_count++;							//Increment counter, to count number of users spent equal time for each possible time span
						}
					}
					float percentage = ((value_count * 100) / size_all);		//Determine percentage of users match each piece of data.
					if(percentage > display_limitor){						//Check if we want to display this data, or if it is not significant enough
						String output = column_header + "," + value + "_all," + percentage + "%";  //If significant enough setup an output string for the array list
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value + "_all");							//Also add the data to a JSON Object
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);											//Then push this data into a JSON Array containing the data for all different year possibilities
						results_all.add(output);								//Add the array list data to the output array list data
					}
				}
				i++;
			}else{							//Step through each column of database data for all data except start/end date, based on header data
				try {
					data_field.put("Data Point Name", column_header);	//Add the column name to the JSON Object containing the array of data
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(String nodes : relevant){					//Step through the relevant data
					String[] split = nodes.split("\\s*,\\s*");	//Split it based on commas
					String value = split[i+1];					//Then add the data from the relevant column to a 
					store.add(value);							//Hash Set to get a list of the possible values
				}
				for(String nodes : All_Node_Data){					//Step through the relevant data
					String[] split = nodes.split("\\s*,\\s*");	//Split it based on commas
					String value = split[i+1];					//Then add the data from the relevant column to a 
					store_all.add(value);							//Hash Set to get a list of the possible values
				}
			
				float other = 0;
				for(String value : store){						//Step through all the possible values
					float value_count = 0;						//Setup a counter to count the number of times a value is seen
					for(String nodes : relevant){				//Step through the data again and see if the value shows up, if so increment the counter
						if(nodes.contains(value)){
							value_count++;
						}
					}
					float percentage = 0;
					if(value.equalsIgnoreCase(node)){			//Then calculate the percentage of total users match that possible data point
						percentage = ((value_count * 100) / user_count);		//Determine percentage of users who enter this node
					}else{
						percentage = ((value_count * 100) / size);		//Determine percentage of users match each piece of data within a node
					}
					if(percentage > display_limitor){					//Set display limitor, so that if a instance is below a threshold it is not displayed
						String output = column_header + "," + value + "," + percentage + "%";	//Add data to a String which will be added to an Array List
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value);						//Add data to a JSON Object
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);										//Then place it in a JSON Array to be added to the JSON node Object
						results.add(output);							//Add String to Array List to be used if printing method is chosen
					}else{
						other = other + percentage;						//If the data does not exceed the minimum display limit, add it to the "other" category
					}
				}
				if(other > 0){											//Once all data points are gathered for a column, check to see if an "other" grouping exists
					String output = column_header + ",Other," + other + "%";  //If it does, add it to the JSON Array for the collumn and the array list
					JSONObject jo = new JSONObject();
					try {
						jo.put("name", "Other");
						jo.put("value", other + "%");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					results.add(output);
					ja.put(jo);
				}
				
				float other_all = 0;
				for(String value : store_all){						//Step through all the possible values
					float value_count = 0;						//Setup a counter to count the number of times a value is seen
					for(String nodes : All_Node_Data){				//Step through the data again and see if the value shows up, if so increment the counter
						if(nodes.contains(value)){
							value_count++;
						}
					}
					float percentage = 0;
					if(value.equalsIgnoreCase(node)){			//Then calculate the percentage of total users match that possible data point
						percentage = ((value_count * 100) / size_all);		//Determine percentage of users who enter this node
					}else{
						percentage = ((value_count * 100) / size_all);		//Determine percentage of users match each piece of data within a node
					}
					if(percentage > display_limitor){					//Set display limitor, so that if a instance is below a threshold it is not displayed
						String output = column_header + "," + value + "_all," + percentage + "%";	//Add data to a String which will be added to an Array List
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value + "_all");						//Add data to a JSON Object
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);										//Then place it in a JSON Array to be added to the JSON node Object
						results_all.add(output);							//Add String to Array List to be used if printing method is chosen
					}else{
						other_all = other_all + percentage;						//If the data does not exceed the minimum display limit, add it to the "other" category
					}
				}
				if(other_all > 0){											//Once all data points are gathered for a column, check to see if an "other" grouping exists
					String output = column_header + ",Other_all," + other_all + "%";  //If it does, add it to the JSON Array for the collumn and the array list
					JSONObject jo = new JSONObject();
					try {
						jo.put("name", "Other_all");
						jo.put("value", other_all + "%");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					results_all.add(output);
					ja.put(jo);
				}
			}
			try {
				data_field.put("Data Breakout", ja);			//Finally, for the column, add the JSON Array to a JSON Object
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json_array.put(data_field);							//Then push this Object into a JSON Array for the node

		}
		
		JSONObject sig = new JSONObject();
		JSONArray  jasig = new JSONArray();
		for(String all_result : results_all){
			JSONObject jo = new JSONObject();
			String[] result_split = all_result.split("\\s*,\\s*");
			for(String rel_result : results){
				String[] rel_result_split = rel_result.split("\\s*,\\s*");
				if(result_split[0].toLowerCase().contains(rel_result_split[0].toLowerCase()) && result_split[1].toLowerCase().contains(rel_result_split[1].toLowerCase())){
					int all_int = Integer.parseInt(result_split[2].split("\\.")[0]);
					int rel_int = Integer.parseInt(rel_result_split[2].split("\\.")[0]);
					if(rel_int > all_int + 5 && !rel_result_split[1].toLowerCase().contains("other".toLowerCase())){
						try {
							jo.put("category", rel_result_split[0]);
							jo.put("value", rel_result_split[1]);
							jasig.put(jo);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//System.out.println(rel_result_split[1]);
					}
				}
			}
		}
		try {
			sig.put("Significant", jasig);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println("\n\n");
		
		json_array.put(sig);	//Add in significant data
		
		try {
			json_node.put("Node Data", json_array);				//Then push the JSRON Array for a Node into a JSON Object for the node
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MultiReturn result = new MultiReturn(json_node, results);	//Return the JSON Object for the node and an Array List of data for the node

		return result;
    }
	
	/*  Prints out the high level node interconnect data.  */
	public void print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order){
		/*  Pass in the search value, the array lists containing the node ordering, and the node interconnects.  Also pass in the list of user ids for data gathered.  */
		
        String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";		//Prints data about the query
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
		
        System.out.println("Edge Transitions");		//Prints out data gathered about the transtitions between nodes
        System.out.println("----------------");
        for (String result : Connects){
        	System.out.println(result);
        }
        
        System.out.println("\n\nNode Ordering");	//Prints out data about the order the nodes should be displayed
        System.out.println("-------------");
        for(int i=0; i<Order.size(); i++){
        	for (String result : Order){
        		String[] result_split = result.split("\\s*,\\s*");
        		if(Integer.parseInt(result_split[1]) == i){				//Sort into numerical order
        			System.out.println(result);
        		}
        	}
        }   
	}
	
	/*  Prints out the data about a particular node.  */
	public void print_node_data(ArrayList<String> node_data){
		/*  Pass in the array list containing the data about a particular node.  */
		String header = null;
		for(String node : node_data){	//Step through the array list
			//System.out.println(node);
			String[] split = node.split("\\s*,\\s*");	//Split each line on commas
			if(!split[1].equalsIgnoreCase("")){			//Ensure the 2nd piece of data isn't blank
				if(!split[0].equalsIgnoreCase(header)){	//Check to see if entering a new column of data for a node
					System.out.println("\n" + split[0] + "\n-----------------");	//If we are, print out column header and then data
					System.out.println(split[1] + ": " + split[2]);
					header = split[0];
				}else{
					System.out.println(split[1] + ": " + split[2]);					//Otherwise, just print out data
				}
			}
		}
	}
}
