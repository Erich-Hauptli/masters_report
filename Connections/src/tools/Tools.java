package tools;

import java.util.ArrayList;
import java.util.HashSet;

public class Tools implements Tools_Interface{
	public boolean compare_arraylist_string(HashSet<String> arrayA, HashSet<String> arrayB){
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
	
	public boolean fuzzy_match(String A, String B){

		boolean result = false;
		
		if(A.equalsIgnoreCase(B)){
			result = true;
		}
		
		return result;
	}
}
