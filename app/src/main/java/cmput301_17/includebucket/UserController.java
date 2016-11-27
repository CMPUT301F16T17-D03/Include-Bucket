package cmput301_17.includebucket;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * UserController
 *
 * This is a controller for a UserAccount. A singleton class.
 */
public class UserController implements Serializable{

    //private Context context;
    private static UserAccount userAccount = null;

    public static UserAccount getUserAccount() {
        if (userAccount == null) {
            try {
                userAccount = UserFileManager.getUserFileManager().loadUser();
                Log.i("what","This user is " + userAccount.getUniqueUserName());
                userAccount.addListener(new Listener() {
                    @Override
                    public void update() {
                        saveUserAccountInLocalFile();

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not deserialize RiderRequests from RequestListFileManager");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException("Could not deserialize RiderRequests from RequestListFileManager");
            }
        }
        return userAccount;
    }

    /**
     * Create a user in ElasticSearch.
     * @param user
     */
    public static void createUserInElasticSearch(UserAccount user) {

        ElasticsearchUserController.CreateUser createUser;
        createUser = new ElasticsearchUserController.CreateUser();
        createUser.execute(user);
    }

    /**
     * Delete a user from ElasticSearch.
     * @param user
     */
    public static void deleteUserFromElasticSearch(UserAccount user) {

        ElasticsearchUserController.DeleteUser deleteUser;
        deleteUser = new ElasticsearchUserController.DeleteUser();
        deleteUser.execute(user);
    }

    /**
     * Retrieve user from Elasticsearch.
     * @return userAccount
     */
    public static void loadUserFromElasticSearch(String userId) {

        ElasticsearchUserController.RetrieveUser retrieveUser;
        retrieveUser = new ElasticsearchUserController.RetrieveUser();
        retrieveUser.execute(userId);

        try {
            UserAccount user = retrieveUser.get();
            userAccount = user;
            Log.i("SUCCESS","Got " + user.getUniqueUserName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This saves a user account in to USER_FILE.
     * @param
     */
    public static void saveUserAccountInLocalFile() {
        try {
            UserFileManager.getUserFileManager().saveUser(getUserAccount());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not deserialize User from UserFileManagaer");
        }
    }

    /**
     * Add user ride request ID's to riderRequests list.
     * @return
     */
    //public Context getContext() {return context;}

    //public void setContext(Context context) {this.context = context;}

    /**
     * Logs user out
     */
    public static void logUserOut() {
        getUserAccount().setLoginStatus(Boolean.FALSE);
        userAccount = null;
    }

}
