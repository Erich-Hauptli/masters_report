package connections;

import java.util.ArrayList;
import java.util.TreeSet;

/*  Defines various methods in the connections class to draw connections between users.  */
public interface Connections_Interface {

	/*find_same searches user database to pull all users with common field and then prints
	 * the percentage of users who match each comparable field.
	 */
	TreeSet<String> 	find_same(String common_field, String common_field_value);
	ArrayList<String> 	database_pull(TreeSet<String> ids, String database);
	ArrayList<String> 	find_edges(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations);
	ArrayList<String> 	find_node_order(ArrayList<String> Connections);
	ArrayList<String>	find_node_info(TreeSet<String> ids, String node, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations);

	void 				print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order);
	void				print_node_data(ArrayList<String> node_data);
	
	
	
	
}
