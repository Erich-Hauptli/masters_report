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
		String a = A.toLowerCase();
		String b = B.toLowerCase();
		
		if(a.equalsIgnoreCase(b)){
			result = true;
		}
		
		return result;
	}
	
	public boolean fuzzy_string_contains(String A, String B){
		boolean result = false;
		String a = A.toLowerCase();
		String b = B.toLowerCase();
		
		if(a.contains(b)){
			result = true;
		}
		return result;
	}
	
	public boolean fuzzy_contains(HashSet<String> A, String B){
		boolean result = false;
		
		if(A.contains(B)){
			result = true;
		}
		return result;
	}

	@Override
	public boolean fuzzy_contains(String A, String B) {
		// TODO Auto-generated method stub
		return false;
	}
}
