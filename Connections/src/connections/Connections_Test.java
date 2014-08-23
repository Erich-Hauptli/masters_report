package connections;

import java.util.ArrayList;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*  Test file for the connections package.  */
public class Connections_Test {
	public static void main(String[] args) {
		
		ConnectionsCheck connection = new ConnectionsCheck();
		
		String common_field = "title";
		String common_field_value = "Platform Chief Engineer";
		
		JSONObject search_term = new JSONObject();
		try {
			search_term.put("field", common_field);
			search_term.put("value", common_field_value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		JSONArray array = connection.return_json(search_term);	//Calls main program and generates JSON Array conaining Data about nodes, node interconnects, and node ordering. 
		
		System.out.println(array);
	}
}
