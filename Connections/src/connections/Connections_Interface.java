package connections;

import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import tools.MultiReturn;

/*  Defines various methods in the connections class to draw connections between users.  */
public interface Connections_Interface {

	void				generate_arff(String name, ArrayList<String> input);
	MultiReturn 		find_edges(TreeSet<String> ids, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations);
	MultiReturn	 		find_node_order(ArrayList<String> Connections);
	MultiReturn			find_node_info(int display_limitor, String node, ArrayList<String> profiles, ArrayList<String> jobs, ArrayList<String> educations);

	void 				print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order);
	void				print_node_data(ArrayList<String> node_data);	
	
}
