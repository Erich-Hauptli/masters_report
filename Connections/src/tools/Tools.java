package tools;

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
		Tools tools = new Tools();
		boolean result = false;
		String a = A.toLowerCase();
		String b = B.toLowerCase();
	
		if(tools.lev_dist(a, b) <= 2){
			result = true;
		}
		
		return result;
	}
	
	public boolean fuzzy_string_contains(String A, String B){
		Tools tools = new Tools();
		boolean result = false;
		String a = A.toLowerCase();
		String b = B.toLowerCase();
		a = a.replaceAll("\\s","");
		b = b.replaceAll("\\s","");
		
		String[] a_array = a.split(",");
		for(String z : a_array){
			int dist = tools.lev_dist(z, b);
			if(dist < 2){
				result = true;
			}
		}

		return result;
	}
	
	public boolean fuzzy_contains(HashSet<String> A, String B){
		boolean result = false;
		Tools tools = new Tools();
		
		for(String A_string : A){
			if (tools.fuzzy_string_contains(A_string, B)){
				result = true;
				break;
			}
		}

		return result;
	}
	
	public int lev_dist(String A, String B){
		int distance = 0;
		String a = A.toLowerCase();
		String b = B.toLowerCase();
		a = a.replaceAll("\\s","");
		b = b.replaceAll("\\s","");
		
		int n = a.length();
		char[] n_char = a.toCharArray();
		int m = b.length();
		char[] m_char = b.toCharArray();
		
		if (n == 0){
			return m;
		}else if (m == 0){
			return n;
		}
		
		int[][] matrix = new int[n+1][m+1];
		
		for(int i=0;i<=n;i++){
			matrix[i][0] = i;
		}
		for(int i=0; i<=m; i++){
			matrix[0][i] = i;
		}
		
		for(int i=1; i<=m; i++){
			for(int j=1; j<=n; j++){
				int cost = 0;
				int up = matrix[j-1][i] + 1;
				int left = matrix[j][i-1] + 1;
				if(n_char[j-1] != m_char[i-1]){	
					cost = 1;
				}
				int diag = matrix[j-1][i-1] + cost;
				int min = Math.min(diag, Math.min(up,left));
				matrix[j][i] = min;
			}
		}
		/*
		System.out.println("M: " + m + " N: " + n);
		for(int i=0; i<=m; i++){
			String line = "";
			for(int j=0; j<=n; j++){
				line = line + "," + matrix[j][i];
			}
			System.out.println(line + "\n");
		}*/
		
		distance = matrix[n][m];
		
		return distance;
	}
}
