package files;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

/*  Opens and reads the contents of a file.  Returns each line of file as a String in a String array.*/

public class ReadFile {
	private String path;
	
	public ReadFile(String file_path){
		path = file_path;  //Set file to be read in.
	}
	
	public String[] OpenFile() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = readLines();				//Count lines in file.
		String[] textData = new String[numberOfLines];  //Setup String array to correct number of elements.
		
		int i;
		
		for(i=0; i<numberOfLines; i++){
			textData[i] = textReader.readLine();   //Step through each line and push it into the String[].
		}
		
		textReader.close();
		return textData;   //Return the String[] to the program calling the function.
	}
	
	int readLines() throws IOException {
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		int numberOfLines = 0;
		
		while (( bf.readLine()) != null){
			numberOfLines++;				//open file and count number of lines in it, to be able to setup String[] size.
		}
		bf.close();
		
		return numberOfLines;
	}
}
