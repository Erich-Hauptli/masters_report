package connections;

import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data_collection.Data_Collection;
import tools.MultiReturn;

/*  Test file for the connections package.  */
public class Connections_Test {
	public static void main(String[] args) {
		
		Data_Collection collect = new Data_Collection();
		ConnectionsCheck connection = new ConnectionsCheck();
		
		String common_field = "title";
		String common_field_value = "Platform Chief Engineer";
	

		System.out.println(common_field);
		System.out.println(common_field_value);

		TreeSet<String> ids = collect.find_same(common_field, common_field_value);  //Search for users who match similar field values, return the ids of these users

		ArrayList<String> profiles = collect.database_pull(ids, "profile");			//Pull the profile data for all users returned on previous query
		ArrayList<String> educations = collect.database_pull(ids, "education");		//Pull the education data for all users returned on previous query
		ArrayList<String> jobs = collect.database_pull(ids, "job");					//Pull the job data for all users returned on previous query
				

		MultiReturn edges = connection.find_edges(ids, profiles, jobs, educations);		//Return the node transitions for the aggregate of the above data

		MultiReturn order = connection.find_node_order(edges.getALS());					//Return the node ordering for the aggregate of the above data
		connection.print_connection_data(common_field, common_field_value, ids, edges.getALS(), order.getALS());
				
		for(String node_order: order.getALS()){											//Step through all the nodes
			String[] node_split = node_order.split("\\s*,\\s*");
			MultiReturn node = connection.find_node_info(5, node_split[0], profiles, jobs, educations);		//Pull all the data for a node and place into an arraylist a JSON Object											//Place the each node data into a JSON array
			connection.print_node_data(node.getALS());	
		}

	}
}
