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
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * UserController
 *
 * This is a controller for a UserAccount.
 *     This class will be used to control when the data of a
 *     user is changed (i.e. when the user updates their contact information) --> many changes
 *     to the Login/Register activities will be done later to follow the UML diagram.
 *     Also when a user creates or deletes a request, this class should be communicated with via
 *     an observer/listener class so that the UserAccount model is also updated).
 */
public class UserController {

    private Context context;
    private static UserAccount userAccount = null;
    private static final String USER_FILE  = "user.sav";

    //private static UserController controller = new UserController();

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
     * This loads a user account from USER_FILE.
     */
    /*public static void loadUserAccountFromLocalFile() {

        try {
            FileInputStream fis = controller.getContext().openFileInput(USER_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<UserAccount>() {}.getType();
            userAccount = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            userAccount = new UserAccount();
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
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
