package json_interface;

import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.MultiReturn;
import connections.ConnectionsCheck;

public class JSON_return implements JSON_interface{
	/*  Takes in a JSON search Object and returns the result of query containing data on all nodes, node transitions, and node ordering as a JSON Array  */
	public JSONArray return_json(JSONObject search_term){
		
		ConnectionsCheck connection = new ConnectionsCheck();
		
		//Parse the input JSON Object
		String common_field = null;
		String common_field_value = null;
		try {
			common_field = (String) search_term.get("field");
			common_field_value = (String) search_term.get("value");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(common_field);
		//System.out.println(common_field_value);

		TreeSet<String> ids = connection.find_same(common_field, common_field_value);  //Search for users who match similar field values, return the ids of these users

		ArrayList<String> profiles = connection.database_pull(ids, "profile");			//Pull the profile data for all users returned on previous query
		ArrayList<String> educations = connection.database_pull(ids, "education");		//Pull the education data for all users returned on previous query
		ArrayList<String> jobs = connection.database_pull(ids, "job");					//Pull the job data for all users returned on previous query
		

		MultiReturn edges = connection.find_edges(ids, profiles, jobs, educations);		//Return the node transitions for the aggregate of the above data

		MultiReturn order = connection.find_node_order(edges.getALS());					//Return the node ordering for the aggregate of the above data
		//connection.print_connection_data(common_field, common_field_value, ids, edges.getALS(), order.getALS());
		
		JSONArray ja_nodes = new JSONArray();
		for(String node_order: order.getALS()){											//Step through all the nodes
			//System.out.println(".");
			String[] node_split = node_order.split("\\s*,\\s*");
			MultiReturn node = connection.find_node_info(5, node_split[0], profiles, jobs, educations);		//Pull all the data for a node and place into an arraylist a JSON Object
			ja_nodes.put(node.getJSON());												//Place the each node data into a JSON array
			//connection.print_node_data(node.getALS());	
		}
		
		JSONObject nodes = new JSONObject();
		try {
			nodes.put("Nodes Data", ja_nodes);										
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray array = new JSONArray();		
		array.put(edges.getJSON());					//Add the node transition edges to the return JSON Array
		array.put(order.getJSON());					//Add the node ordering to the return JSON Array
		array.put(nodes);							//Add all the node data to the return JSON Array
		
		connection.generate_arff("education", educations);

		return array;		
	}

}
