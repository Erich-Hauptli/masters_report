package files;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/*  Opens and reads the contents of a file.  Returns each line of file as a String in a String array.*/

public class Files implements Files_Interface{
	private String path;
	
	public String[] OpenFile(){
		FileReader fr = null;
		int numberOfLines = 0;
		int i;
		numberOfLines = readLines();
		String[] textData = new String[numberOfLines];  //Setup String array to correct number of elements.
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader textReader = new BufferedReader(fr);

		try {
			for(i=0; i<numberOfLines; i++){
				textData[i] = textReader.readLine();	//Step through each line and push it into the String[].
			}
			textReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
		return textData;   //Return the String[] to the program calling the function.
	}
	
	public int readLines() {
		int numberOfLines = 0;
		FileReader file_to_read = null;
		try {
			file_to_read = new FileReader(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bf = new BufferedReader(file_to_read);
		
		try {
			while (( bf.readLine()) != null){
				numberOfLines++;				//open file and count number of lines in it, to be able to setup String[] size.
			}
			bf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return numberOfLines;
	}

	@Override
	public void ReadFile(String file_path) {
		path = file_path;  //Set file to be read in.
		
	}
	
	public void WriteFile(String file_path, ArrayList<String> lines){
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(file_path), "utf-8"));
		    for(String line:lines){
		    	 writer.write(line + "\n");
		    }   
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
	}

}
