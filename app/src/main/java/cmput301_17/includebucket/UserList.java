package cmput301_17.includebucket;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by michelletagarino on 16-10-30.
 */
public class UserList {

    protected Collection<UserAccount> userList;

    public UserList(){
        userList = new ArrayList<>();
    }

    public Collection<UserAccount> getUserList() {
        return userList;
    }

    public void addUser(UserAccount newUser) {
        userList.add(newUser);
    }

    public void deleteUser(UserAccount user) {
        userList.remove(user);
    }

    public int size() {
        return userList.size();
    }

    public boolean contains(UserAccount user) {
        return userList.contains(user);
    }
}
