package tools;

import java.util.HashSet;

public interface Tools_Interface {
	public boolean compare_arraylist_string(HashSet<String> arrayA, HashSet<String> arrayB);
	public boolean fuzzy_match(String A, String B);
	public boolean fuzzy_string_contains(String A, String B);
	public boolean fuzzy_contains(HashSet<String> A, String B);
	public int lev_dist(String A, String B);
}
