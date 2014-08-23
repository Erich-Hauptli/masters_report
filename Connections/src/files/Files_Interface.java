package files;

import java.util.ArrayList;

public interface Files_Interface {
	
	void ReadFile(String file_path);
	
	String[] OpenFile();
	
	int readLines();
	
	void WriteFile(String file_path, ArrayList<String> lines);
}
