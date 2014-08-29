package print;

import java.util.ArrayList;
import java.util.TreeSet;

public interface Print_Interface {
	void 				print_connection_data(String common_field, String common_field_value, TreeSet<String> ids, ArrayList<String> Connects, ArrayList<String> Order);
	void				print_node_data(ArrayList<String> node_data);	
}
