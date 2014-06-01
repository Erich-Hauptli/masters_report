package files;

import java.io.IOException;

/*  This is the test script to test ReadFile.java. */

public class FileData {
	public static void main(String[] args) throws IOException {
		String file_name = "people.csv";    // File to be read in.
		
		try{
			ReadFile file = new ReadFile(file_name);
			String[] aryLines = file.OpenFile();
			
			int i;
			for(i=0; i<aryLines.length; i++){     //Step through the file line by line and print it out.
				System.out.println(aryLines[i]);
			}
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
