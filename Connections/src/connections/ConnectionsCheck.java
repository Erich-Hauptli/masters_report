package connections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import user.UserProfile;
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

	public ArrayList<String> find_edges(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		int edge_intervals = 10;
	
		ArrayList<String> Connects = new ArrayList<String>();
		ArrayList<String> Connections = new ArrayList<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		ArrayList<String> User = new ArrayList<String>();

	
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
        
        int num_users = ids.size();			//Convert all node interchanges to weight of # of edge intervals.
        for (String Connect : Connects){
        	String[] node = Connect.split("\\s*,\\s*");
        	int node_users = Integer.parseInt(node[2]);
        	float weight = (node_users / ( num_users / edge_intervals));
        	int line_weight = (int) weight;
        	if(line_weight > 0){
        		String Connect_temp = node[0] + "," + node[1] + "," + line_weight;
        		Connections.add(Connect_temp);
        	}
        }
        return Connections;
	}
     
	public ArrayList<String> find_node_order(ArrayList<String> Connections) {
		HashSet<String> AllNodes = new HashSet<String>();
		ArrayList<String> Nodes = new ArrayList<String>();
		ArrayList<String> HeavyEdges = new ArrayList<String>();
		ArrayList<String> Node0 = new ArrayList<String>();
		ArrayList<String> NodeFinal = new ArrayList<String>();
		ArrayList<String> NodeTemp = new ArrayList<String>();
		ArrayList<String> NodeStore = new ArrayList<String>();
		
		for(String Connect : Connections){				//Get HashSet of all nodes
			String[] node = Connect.split("\\s*,\\s*");
			AllNodes.add(node[0]);
			AllNodes.add(node[1]);
		}
		
		for(String node : AllNodes){
			System.out.println(node);
			int weight = 0;
			int weight_tmp =0;
			String node_tmp = null;
			String nodeSave = null;
			for(String Connect : Connections){
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
		}
		
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
        for(String Connect : HeavyEdges){
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
        
        Tools tools = new Tools();
        NodeStore = Node0;
        for(String node : Node0){
        	Nodes.add(node + ",0");
        }
        
        int group = 1;
        while(tools.compare_arraylist_string(NodeStore, NodeFinal) == false){
        	String NS = null;
        	NS = NodeStore.toString();
        	NS = NS.substring(1, NS.length()-1);
        	//System.out.println(NS);
        	String [] NSarray = null;
        	NSarray = NS.split("\\s*,\\s*");
        	for(String node : NSarray){
        		NodeTemp.clear();
        		//System.out.println("Group " + group + ": " + node);
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
        
        return Nodes;
	}

	public void find_nodes(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations) {
		
		
		ArrayList<String> headers = new ArrayList<String>();
        try {
        	UserProfile user = new UserProfile();
        	headers = user.return_headers();								//Queries headers for users
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void print_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order){
		
        String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
		
        System.out.println("Edge Transitions");
        for (String result : Connects){
        	System.out.println(result);
        }
        
        System.out.println("\n\nNode Ordering");
        for (String result : Order){
        	System.out.println(result);
        }
	}
}
