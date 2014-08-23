package json_interface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON_Test {
	public static void main(String[] args) {
	
		JSON_return json_return = new JSON_return();
	
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
	
	
		JSONArray array = json_return.return_json(search_term);	//Calls main program and generates JSON Array conaining Data about nodes, node interconnects, and node ordering.
	
		System.out.println(array);
	}
}
