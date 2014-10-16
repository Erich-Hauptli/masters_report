package tools;

import java.util.HashSet;


public class Tools_Test {
	public static void main(String[] args) {
		Tools tools = new Tools();
		
	/*
		HashSet<String> A = new HashSet<String>();
		A.add("A");
		A.add("B");
		A.add("C");
		
		HashSet<String> B = new HashSet<String>();
		B.add("B");
		B.add("C");
		B.add("A");
		
		HashSet<String> C = new HashSet<String>();
		C.add("B");
		C.add("C");
		C.add("C");
		
		boolean results = tools.compare_arraylist_string(A, B);
		System.out.println("A = B is " + results);
		
		results = tools.compare_arraylist_string(A, C);
		System.out.println("A = C is " + results);
		
		results = tools.compare_arraylist_string(B, C);
		System.out.println("B = C is " + results);
		
		*/
		HashSet<String> Test = new HashSet<String>();
		Test.clear();
		Test.add("BobtheBuilder");
		Test.add("abcd");
		Test.add("xyz");
		String Z = "bob";
		
		System.out.println("Fuzzy Contains: " + tools.fuzzy_contains(Test, Z));
		
		String A = "BobtheBuilder";
		String B = "aheBuildes";
		
		int diff = tools.lev_dist(A, B);
		
		System.out.println(A + " contains " + B + ": "  + tools.fuzzy_string_contains(A, B)  + "diff: " + diff);
	}
	
}