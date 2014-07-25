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
		String common_field_value = "Eng-Chief";
		
		String json_string = "{" + '"' + common_field + '"' + ":" + '"' + common_field_value + '"' + "}";  //Generate JSON Object to pass to program.
		
		JSONObject search_term = null;
		try {
			search_term = new JSONObject(json_string);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		JSONArray array = connection.return_json(search_term);	//Calls main program and generates JSON Array conaining Data about nodes, node interconnects, and node ordering. 
		
		System.out.println(array);
	}
}
