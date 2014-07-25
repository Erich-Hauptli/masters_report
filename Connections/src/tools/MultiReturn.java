package tools;

import java.util.ArrayList;

import org.json.JSONObject;

public class MultiReturn {
	public final JSONObject json;
	public final ArrayList<String> als;
	
	public MultiReturn(JSONObject json, ArrayList<String> als) {
		this.json = json;
		this.als = als;
	}
	
	public JSONObject getJSON() {
		return json;
	}
	
	public ArrayList<String> getALS() {
		return als;
	}
}
