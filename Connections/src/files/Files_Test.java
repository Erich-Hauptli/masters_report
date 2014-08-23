package files;

import java.io.IOException;
import java.util.ArrayList;

/*  This is the test script to test ReadFile.java. */

public class Files_Test {
	public static void main(String[] args) throws IOException {
		
		ArrayList<String> lines = new ArrayList<String>();
		String file_name = "test.arff";    // File to be read in.
		lines.add("Lukas");
		lines.add("Robert");
		lines.add("Hauptli");		 
		
		Files file = new Files();
		file.WriteFile(file_name,lines);
		/*
		file.ReadFile(file_name);
		String[] aryLines = file.OpenFile();
		
		int i;
		for(i=0; i<aryLines.length; i++){     //Step through the file line by line and print it out.
			System.out.println(aryLines[i]);
		}
		*/
		
	}
}
