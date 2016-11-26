package cmput301_17.includebucket;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by michelletagarino on 16-11-26.
 */
public class UserAccountTest {
    @Test
    public void testUserAccount() {
        String userName = "UserLogin";
        String userEmail = "User@email.com";
        String userPhone = "1-800-user";
        UserAccount user = new UserAccount(userName,userEmail,userPhone);
        assertTrue("User username is not equal", userName.equals(user.getUniqueUserName()));
        assertTrue("User email is not equal", userEmail.equals(user.getEmail()));
        assertTrue("User phone is not equal", userPhone.equals(user.getPhoneNumber()));
    }
}
