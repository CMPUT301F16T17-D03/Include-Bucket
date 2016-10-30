package cmput301_17.includebucket;

/**
 * Created by michelletagarino on 16-10-30.
 */
public class UserListController {

    private static UserList userList = null;

    static public UserList getUserList() {
        if (userList == null) {
            userList = new UserList();
        }
        return userList;
    }

    static public String getUniqueUsername() {
        return "bleh";
    }

    public void addUser(UserAccount user) {
        getUserList().addUser(user);
    }
}
