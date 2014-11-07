package json_interface;

import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.MultiReturn;
import connections.ConnectionsCheck;
import data_collection.Data_Collection;

public class JSON_return implements JSON_interface{
	/*  Takes in a JSON search Object and returns the result of query containing data on all nodes, node transitions, and node ordering as a JSON Array  */
	public JSONArray return_json(JSONObject search_term){
		
		Data_Collection collect = new Data_Collection();
		ConnectionsCheck connection = new ConnectionsCheck();
		
		long start_time = System.nanoTime();
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

		long collect_time = System.nanoTime();
		TreeSet<String> ids = collect.find_same(common_field, common_field_value);  //Search for users who match similar field values, return the ids of these users

		ArrayList<String> profiles = collect.database_pull(ids, "profile");			//Pull the profile data for all users returned on previous query
		ArrayList<String> educations = collect.database_pull(ids, "education");		//Pull the education data for all users returned on previous query
		ArrayList<String> jobs = collect.database_pull(ids, "job");					//Pull the job data for all users returned on previous query
		

		long edge_time = System.nanoTime();
		MultiReturn edges = connection.find_edges(ids, profiles, jobs, educations);		//Return the node transitions for the aggregate of the above data

		long order_time = System.nanoTime();
		MultiReturn order = connection.find_node_order(edges.getALS());					//Return the node ordering for the aggregate of the above data
		//connection.print_connection_data(common_field, common_field_value, ids, edges.getALS(), order.getALS());
		
		long node_time = System.nanoTime();
		JSONArray ja_nodes = new JSONArray();
		int count = 0;
		for(String node_order: order.getALS()){											//Step through all the nodes
			count++;
			//System.out.println(".");
			String[] node_split = node_order.split("\\s*,\\s*");
			MultiReturn node = connection.find_node_info(5, node_split[0], profiles, jobs, educations);		//Pull all the data for a node and place into an arraylist a JSON Object
			ja_nodes.put(node.getJSON());												//Place the each node data into a JSON array
			//connection.print_node_data(node.getALS());	
		}
		
		long json_time = System.nanoTime();
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
		
		long arff_time = System.nanoTime();
		connection.generate_arff("education", ids);

		long end_time = System.nanoTime();
		
		long total_collect_time = edge_time - collect_time;
		long total_edge_time = order_time - edge_time;
		long total_order_time = node_time - order_time;
		long total_return_time = node_time - start_time + arff_time - json_time;
		long total_node_time = json_time - node_time;
		long average_node_time = total_node_time / count;
		long total_arff_time = end_time - arff_time;
		
		System.out.println("Data Collection Time: " + total_collect_time);
		System.out.println("Edge Time: " + total_edge_time);
		System.out.println("Order Time: " + total_order_time);
		System.out.println("Total Time: " + total_return_time);
		System.out.println("Node Time: " + total_node_time);
		System.out.println("Average Node Time: " + average_node_time + " for " + count + " nodes.");
		System.out.println("Arff Time: " + total_arff_time);
		
		String printout_header = "For " + ids.size() + " users who had " + common_field +  " = ";		//Prints data about the query
        printout_header = printout_header + common_field_value + " at some point in their career.\n";
        System.out.println(printout_header);
        System.out.println("\n\n");
		return array;		
	}

}
