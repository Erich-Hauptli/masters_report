package connections;

import java.util.ArrayList;
import java.util.TreeSet;

/*  Test file for the connections package.  */
public class Connections_Test {
	public static void main(String[] args) {

		ConnectionsCheck connection = new ConnectionsCheck();
		
		String common_field = "title";
		String common_field_value = "Eng-Chief";
		
		TreeSet<String> ids = connection.find_same(common_field, common_field_value);  //Search for users who work as engineers, 
														   					//print out similar education, degree and city.
		
		ArrayList<String> profiles = connection.database_pull(ids, "profile");
		ArrayList<String> educations = connection.database_pull(ids, "education");
		ArrayList<String> jobs = connection.database_pull(ids, "job");
		
		ArrayList<String> edges = connection.find_edges(ids, profiles, jobs, educations);
		ArrayList<String> order = connection.find_node_order(edges);
		
		connection.print_connection_data(common_field, common_field_value, ids, edges, order);
		System.out.println("\n");
		
		for(String node_order: order){
			String[] node_split = node_order.split("\\s*,\\s*");
			System.out.println("\n\n" + node_split[0]+ "\n------------------");
			ArrayList<String> node = connection.find_node_info(5, node_split[0], profiles, jobs, educations);
			connection.print_node_data(node);
		}

	}
}
