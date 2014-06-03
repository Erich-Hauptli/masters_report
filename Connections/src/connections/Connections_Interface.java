package connections;

/*  Defines various methods in the connections class to draw connections between users.  */
public interface Connections_Interface {

	/*find_same searches user database to pull all users with common field and then prints
	 * the percentage of users who match each comparable field.
	 */
	void find_same(String common_field, String common_field_value, String[] compare_fields);

	
}
