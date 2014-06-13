package user;

/*  Test file for user package.  */
public class User_Test {
	public static void main(String[] args) {

        // Create User objects
        UserProfile user = new UserProfile();
        
        //String database = "profile";

        user.database_setup();
        user.add_users("Users.csv");
        user.display_user("437");
        /*
        user.display_matching_users("education", "id", "2");
        System.out.println("Display all users.");
        user.display_all_users(database);
        user.modify_field("education", "1", "school", "Michigan State University");
        System.out.println("Display all users who attented MSU");
        user.display_matching_users("education", "school", "MSU");
        user.display_matching_users("job", "company", "IBM");
        System.out.println("Display all users.");
        user.display_all_users("job");
        */
    }
}
