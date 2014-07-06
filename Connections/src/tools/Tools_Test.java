package tools;

import java.util.ArrayList;


public class Tools_Test {
	public static void main(String[] args) {
		Tools tools = new Tools();
		
	
		ArrayList<String> A = new ArrayList<String>();
		A.add("A");
		A.add("B");
		A.add("C");
		
		ArrayList<String> B = new ArrayList<String>();
		B.add("B");
		B.add("C");
		B.add("A");
		
		ArrayList<String> C = new ArrayList<String>();
		C.add("B");
		C.add("C");
		C.add("C");
		
		boolean results = tools.compare_arraylist_string(A, B);
		System.out.println("A = B is " + results);
		
		results = tools.compare_arraylist_string(A, C);
		System.out.println("A = C is " + results);
		
		results = tools.compare_arraylist_string(B, C);
		System.out.println("B = C is " + results);
	}
}