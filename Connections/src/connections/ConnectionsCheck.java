package connections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.TreeSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import user.UserProfile;
import tools.MultiReturn;
import tools.Tools;



/*  Contains commands that draws connections between users.  */
public class ConnectionsCheck implements Connections_Interface{
	
	int display_limitor = 5;  //Match percentage required for node to be displayed. 

	ArrayList<String> compares = new ArrayList<String>();
	ArrayList<String> Points = new ArrayList<String>();

	
	/*  Prints out paths taken by common users as percentages.  */
    public TreeSet<String> find_same(String common_field, String common_field_value) {
    	TreeSet<String> compare_databases = new TreeSet<String>();
    	TreeSet<String> ids = new TreeSet<String>();
    	ArrayList<String> headers = new ArrayList<String>();
    	ArrayList<String> results = new ArrayList<String>();
    	
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
        
        return ids;
    }    
        /*

        */
        
    public ArrayList<String> database_pull(TreeSet<String> ids, String database) {
        ArrayList<String> results = new ArrayList<String>();
        
        String[] user_ids = ids.toArray(new String[ids.size()]);
            
        try {												//Pull all user data for comparison elements.
        	UserProfile user = new UserProfile();
        	results = user.collect_matched_users(database,user_ids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public MultiReturn find_edges(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		int edge_intervals = 10;
	
		ArrayList<String> Connects = new ArrayList<String>();
		ArrayList<String> Connections = new ArrayList<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		ArrayList<String> User = new ArrayList<String>();

	
        for(String id : ids){
        	User.clear();
        	Nodes.clear();
        	for(String job : jobs){
        		if(job.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(job);
        			//System.out.println(job);
        		}
        	}
        	for(String education : educations){
        		if(education.toLowerCase().startsWith(id.toLowerCase() + ",")){
        			Nodes.add(education);
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
        
        int num_users = ids.size();			//Convert all node interchanges to weight of # of edge intervals.
        JSONArray ja = new JSONArray();
        for (String Connect : Connects){
        	String[] node = Connect.split("\\s*,\\s*");
        	int node_users = Integer.parseInt(node[2]);
        	float weight = (node_users / ( num_users / edge_intervals));
        	int line_weight = (int) weight;
        	if(line_weight > 0){
        		String Connect_temp = node[0] + "," + node[1] + "," + line_weight;
        		Connections.add(Connect_temp);
        		JSONObject jo = new JSONObject();
            	try {
            		jo.put("node A", node[0]);
    				jo.put("node B", node[1]);
    				jo.put("transition frequency", line_weight);
    			} catch (JSONException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
            	ja.put(jo);
        	}
        }
        JSONObject node_connections = new JSONObject();
        try {
			node_connections.put("Node Connections", ja);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        MultiReturn result = new MultiReturn(node_connections, Connections);
        return result;
	}
     
	public MultiReturn find_node_order(ArrayList<String> Connections) {
		HashSet<String> AllNodes = new HashSet<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		HashSet<String> HeavyEdges = new HashSet<String>();
		ArrayList<String> Node0 = new ArrayList<String>();
		ArrayList<String> NodeFinal = new ArrayList<String>();
		ArrayList<String> NodeTemp = new ArrayList<String>();
		ArrayList<String> NodeStore = new ArrayList<String>();
		ArrayList<String> NodeReturn = new ArrayList<String>();
		
		for(String Connect : Connections){				//Get HashSet of all nodes
			String[] node = Connect.split("\\s*,\\s*");
			AllNodes.add(node[0]);
			AllNodes.add(node[1]);
		}
		
		for(String node : AllNodes){
			int weight = 0;
			int weight_tmp =0;
			String node_tmp = null;
			String nodeSave = null;
			for(String Connect : Connections){					//Find worst edge to node
				String[] nodes = Connect.split("\\s*,\\s*");
				if(node.equalsIgnoreCase(nodes[0])){
					weight = Integer.parseInt(nodes[2]);
					node_tmp = Connect;
				}
				if(weight > weight_tmp && node_tmp != null){
					nodeSave = node_tmp;
				}
			}
			if(nodeSave != null){
				HeavyEdges.add(nodeSave);
			}
			
			for(String Connect : Connections){					//Find worst edge from node
				String[] nodes = Connect.split("\\s*,\\s*");
				if(node.equalsIgnoreCase(nodes[1])){
					weight = Integer.parseInt(nodes[2]);
					node_tmp = Connect;
				}
				if(weight > weight_tmp && node_tmp != null){
					nodeSave = node_tmp;
				}
			}
			if(nodeSave != null){
				HeavyEdges.add(nodeSave);
			}
		}
		
		if(HeavyEdges != null){
			for(String Connect : HeavyEdges){				//Find Starting Node
				int found = 0;
				String[] node = Connect.split("\\s*,\\s*");
				String nodeA = node[0];
				for(String test : Connections){
					String[] node_temp = test.split("\\s*,\\s*");
					String nodeB = node_temp[1];
					if(nodeA.equalsIgnoreCase(nodeB)){
						found = 1;
						break;
					}
				}
				if(found == 0){
					Node0.add(nodeA);
				}
			}
			for(String Connect : HeavyEdges){				//Find Final Node
				int found = 0;
				String[] node = Connect.split("\\s*,\\s*");
				String nodeB = node[1];
				String node_A = node[0];
				if(!nodeB.equalsIgnoreCase(node_A)){
					for(String test : Connections){
						String[] node_temp = test.split("\\s*,\\s*");
						String nodeA = node_temp[0];
						if(nodeB.equalsIgnoreCase(nodeA)){
							found = 1;
							break;
						}
					}
				}
				if(found == 0){
					NodeFinal.add(nodeB);
				}
			}
		}
        
        NodeStore = Node0;
        for(String node : Node0){
        	Nodes.add(node + ",0");
        }
        
       
        
        int group = 1;
        Tools tools = new Tools();
        while(tools.compare_arraylist_string(NodeStore, NodeFinal) == false){	//Find potential node ordering
        	String NS = null;
        	NS = NodeStore.toString();
        	NS = NS.substring(1, NS.length()-1);
        	String [] NSarray = null;
        	NSarray = NS.split("\\s*,\\s*");
        	for(String node : NSarray){
        		NodeTemp.clear();
        		for(String test : HeavyEdges){
        			String[] node_temp = test.split("\\s*,\\s*");
        			String nodeA = node_temp[0];
        			String nodeB = node_temp[1];
        			if(node.equalsIgnoreCase(nodeA)){
        				NodeTemp.add(nodeB);
        				Nodes.add(nodeB + "," + group);
        			}
        		}
        	}
        	
        	group++;
    		NodeStore = NodeTemp;
        }
        //System.out.println("Testing");
        
        JSONArray ja = new JSONArray();
        for(String each_node : AllNodes){
        	String node_store = null;
        	for(String node : Nodes){		//Eliminate duplicate node by using later node.
        		String[] node_temp = node.split("\\s*,\\s*");
        		if(node_temp[0].equalsIgnoreCase(each_node) && node_store == null){
        			node_store = node;
        		}else if(node_temp[0].equalsIgnoreCase(each_node)){
        			String[] node_store_split = node_store.split("\\s*,\\s*");
        			if(Integer.parseInt(node_store_split[1]) < Integer.parseInt(node_temp[1])){  //Keep the later duplicate node
        				node_store = node;
        			}
        		}
        	}
        	NodeReturn.add(node_store);
        	String[] json_split = node_store.split("\\s*,\\s*");
        	
        	JSONObject jo = new JSONObject();
        	try {
        		jo.put("node name", json_split[0]);
				jo.put("order", json_split[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	ja.put(jo);
        }
        JSONObject node_ordering = new JSONObject();
        try {
			node_ordering.put("Node Ordering", ja);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        MultiReturn result = new MultiReturn(node_ordering, NodeReturn);
        return result;
	}

	public MultiReturn find_node_info(int display_limitor, String node, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> headers = new ArrayList<String>();
		ArrayList<String> complete = new ArrayList<String>();
		ArrayList<String> relevant = new ArrayList<String>();
		HashSet<String> store = new HashSet<String>();
		
		JSONObject  json_node = new JSONObject();
		try {
			json_node.put("Node Name", node);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int user_count = 0;
		for(String profile : profiles){
			String profile_tmp = "profile," + profile;
			complete.add(profile_tmp);
			user_count++;
		}
		for(String job : jobs){
			String job_tmp = "job," + job;
			complete.add(job_tmp);
		}
		for(String education : educations){
			String education_tmp = "education," + education;
			complete.add(education_tmp);
		}
		

		float size = 0;
		int count = 0;
		String node_type = null;
		for(String nodes : complete){
			String[] split = nodes.split("\\s*,\\s*");
			count = split.length;
			if(nodes.contains(node)){
				node_type = split[0];
				size++;
				relevant.add(nodes);
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
			if(header.contains(node_type)){
				header_store = header;
			}
		}
		
		JSONArray  json_array = new JSONArray();
		for(int i=1; i<count-1; i++){
			JSONObject data_field = new JSONObject();
			JSONArray  ja = new JSONArray();
			store.clear();
			String[] column_headers = header_store.split("\\s*,\\s*");
			String column_header = column_headers[i+1];
			if(column_header.equals("start_date")){
				column_header = "Years";
				for(String nodes : relevant){
					String[] split = nodes.split("\\s*,\\s*");
					int start_date = Integer.parseInt(split[i+1]);
					int end_date = start_date;
					if(split[i+2].equalsIgnoreCase("Current")){
						end_date = Calendar.getInstance().get(Calendar.YEAR);
					}else{
						end_date = Integer.parseInt(split[i+2]);
					}
					int years = end_date - start_date;
					String value = Integer.toString(years);
					store.add(value);
				}
	
				try {
					data_field.put("Data Point Name", column_header);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				for(String value : store){
					float value_count = 0;
					for(String nodes : relevant){
						String[] split = nodes.split("\\s*,\\s*");
						int start_date = Integer.parseInt(split[i+1]);
						int end_date = start_date;
						if(split[i+2].equalsIgnoreCase("Current")){
							end_date = Calendar.getInstance().get(Calendar.YEAR);
						}else{
							end_date = Integer.parseInt(split[i+2]);
						}
						int years = end_date - start_date;
						String year = Integer.toString(years);
						if(year.contains(value)){
							value_count++;
						}
					}
					float percentage = ((value_count * 100) / size);		//Determine percentage of users match each piece of data.
					if(percentage > display_limitor){
						String output = column_header + "," + value + "," + percentage + "%";
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value);
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);
						results.add(output);
					}
				}
				i++;
			}else{
				try {
					data_field.put("Data Point Name", column_header);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(String nodes : relevant){
					String[] split = nodes.split("\\s*,\\s*");
					String value = split[i+1];
					store.add(value);
				}
			
				float other = 0;
				for(String value : store){
					float value_count = 0;
					for(String nodes : relevant){
						if(nodes.contains(value)){
							value_count++;
						}
					}
					float percentage = 0;
					if(value.equalsIgnoreCase(node)){
						percentage = ((value_count * 100) / user_count);		//Determine percentage of users match each piece of data.
					}else{
						percentage = ((value_count * 100) / size);		//Determine percentage of users match each piece of data.
					}
					if(percentage > display_limitor){
						String output = column_header + "," + value + "," + percentage + "%";
						JSONObject jo = new JSONObject();
						try {
							jo.put("name", value);
							jo.put("value", percentage + "%");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ja.put(jo);
						results.add(output);
					}else{
						other = other + percentage;
					}
				}
				if(other > 0){
					String output = column_header + ",Other," + other + "%";
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
			}
			try {
				data_field.put("Data Breakout", ja);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json_array.put(data_field);

		}
		try {
			json_node.put("Node Data", json_array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MultiReturn result = new MultiReturn(json_node, results);

		return result;
    }
	
	public void print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order){
		
        String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
		
        System.out.println("Edge Transitions");
        System.out.println("----------------");
        for (String result : Connects){
        	System.out.println(result);
        }
        
        System.out.println("\n\nNode Ordering");
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
	
	public void print_node_data(ArrayList<String> node_data){
		String header = null;
		for(String node : node_data){
			System.out.println(node);
			String[] split = node.split("\\s*,\\s*");
			if(!split[1].equalsIgnoreCase("")){
				if(!split[0].equalsIgnoreCase(header)){
					System.out.println("\n" + split[0] + "\n-----------------");
					System.out.println(split[1] + ": " + split[2]);
					header = split[0];
				}else{
					System.out.println(split[1] + ": " + split[2]);
				}
			}
		}
	}
	
	public JSONArray return_json(JSONObject search_term){
		
		ConnectionsCheck connection = new ConnectionsCheck();
		
		System.out.println(search_term);
		String search = search_term.toString();
		search = search.substring(1, search.length()-1);
		String[] search_split = search.split(":");
		String common_field = search_split[0];
		common_field = common_field.substring(1, common_field.length()-1);
		String common_field_value = search_split[1];
		common_field_value = common_field_value.substring(1, common_field_value.length()-1);

		TreeSet<String> ids = connection.find_same(common_field, common_field_value);  //Search for users who work as engineers, 
														   					//print out similar education, degree and city.
		ArrayList<String> profiles = connection.database_pull(ids, "profile");
		ArrayList<String> educations = connection.database_pull(ids, "education");
		ArrayList<String> jobs = connection.database_pull(ids, "job");
		
		MultiReturn edges = connection.find_edges(ids, profiles, jobs, educations);
		MultiReturn order = connection.find_node_order(edges.getALS());
		
		JSONArray ja_nodes = new JSONArray();
		for(String node_order: order.getALS()){
			String[] node_split = node_order.split("\\s*,\\s*");
			MultiReturn node = connection.find_node_info(5, node_split[0], profiles, jobs, educations);
			ja_nodes.put(node.getJSON());
		}
		
		JSONObject nodes = new JSONObject();
		try {
			nodes.put("Nodes Data", ja_nodes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray array = new JSONArray();
		array.put(edges.getJSON());
		array.put(order.getJSON());
		array.put(nodes);
				
		return array;		
	}
}
