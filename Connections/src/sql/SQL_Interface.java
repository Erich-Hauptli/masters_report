package sql;

import java.util.ArrayList;

public interface SQL_Interface {

	void download_all(String database);
	
	void download_matches(String database, String field, String field_value);
	
	ArrayList<String> collect_matches(String database, String field, String field_value);
	
	ArrayList<String> query_headers(String database);
	
	void upload_line(String database, String[] headers, String[] args);
	
	void modify_line(String database, String id, String field, String field_value);
	
	void upload_file(String database, String[] headers, String filename);
}
