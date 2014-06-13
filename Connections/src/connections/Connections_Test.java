package connections;

/*  Test file for the connections package.  */
public class Connections_Test {
	public static void main(String[] args) {

		ConnectionsCheck connection = new ConnectionsCheck();
		
		String[] fields = {"company", "degree", "school"}; 
		connection.find_same("title", "Eng-Chief", fields);  //Search for users who work as engineers, 
														   //print out similar education, degree and city.
	}
}
