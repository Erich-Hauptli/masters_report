package user;

/*  Test file for user package.  */
public class User_Test {
	public static void main(String[] args) {

        // Create User objects
        UserProfile user = new UserProfile();
        
        String database = "profile";

        String[] Nick = {"8", "Nick Ellis", "7/5/1982", "MSU", "BS", "Lawyer", "Detroit"};
        user.add_users(database, "Test.csv");
        System.out.println("Display all users who attented MSU");
        user.display_user(database, "education", "MSU");
        //user.add_user(Nick);
        System.out.println("Display all users.");
        user.display_all_users(database);
        System.out.println("Display all users who attented MSU");
        user.display_user(database, "education", "MSU");
        user.modify_field(database, "6", "name", "Krista Hauptli Teller");
        user.modify_field(database, "7", "birthday", "8/10/1985");
        user.modify_field(database, "2", "education", "Michigan State University");
        user.display_user(database, "work", "engineer");
        user.modify_field(database, "1", "work", "IBM");
        System.out.println("Display all users.");
        user.display_all_users(database);
    }
}
