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
		
		connection.print_data(common_field, common_field_value, ids, edges, order);
		
		//connection.find_nodes(ids, profiles, jobs, educations);
	}
}
