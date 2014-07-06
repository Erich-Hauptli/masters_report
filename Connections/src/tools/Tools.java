package tools;

import java.util.ArrayList;

public class Tools implements Tools_Interface{
	public boolean compare_arraylist_string(ArrayList<String> arrayA, ArrayList<String> arrayB){
		boolean match = false;
		
		for(String A : arrayA){
			match = false;
			for(String B : arrayB){
				if(A.equalsIgnoreCase(B)){
					match = true;
					break;
				}
			}
			if(match == false){
				break;
			}
		}
		
		return match;
	}
}
