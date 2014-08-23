package data_collection;

import java.util.ArrayList;
import java.util.TreeSet;

public interface Data_Collection_Interface {
	TreeSet<String> 	find_same(String common_field, String common_field_value);
	ArrayList<String>	find_all_node_data(String common_field, String common_field_value);
	ArrayList<String> 	database_pull(TreeSet<String> ids, String database);
}
