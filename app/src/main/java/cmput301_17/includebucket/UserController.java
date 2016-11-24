package cmput301_17.includebucket;

import android.content.Context;

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
 * Created by michelletagarino on 16-11-11.
 *
 * This is a controller for a UserAccount.
 *     Although somehow not implemented yet, this class will be used to control when the data of a
 *     user is changed (i.e. when the user updates their contact information) --> many changes
 *     to the Login/Register activities will be done later to follow the UML diagram.
 *         Also when a user creates or deletes a request, this class should be communicated with via
 *         an observer/listener class so that the UserAccount model is also updated).
 */
public class UserController {

    private Context context;
    private static UserAccount userAccount = null;
    private static final String USER_FILE  = "user.sav";

    private static UserController controller = new UserController();

    public static UserAccount getUserAccount() {
        if (userAccount == null) {
            userAccount = loadUserAccountFromLocalFile();
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
     * Update the user in Elasticsearch
     * @param user
     */
    public static void updateUser(UserAccount user) {

        UserAccount newUser = user;
        //newUser.setRiderRequestIds(user.getRiderRequestIds());

        // Delete old
        deleteUserFromElasticSearch(user);

        // Create new
        createUserInElasticSearch(newUser);
    }

    /**
     * Retrieve user from Elasticsearch.
     * @return userAccount
     */
    public static UserAccount retrieveUserFromElasticSearch(String userId) {

        ElasticsearchUserController.RetrieveUser retrieveUser;
        retrieveUser = new ElasticsearchUserController.RetrieveUser();
        retrieveUser.execute(userId);

        UserAccount userAccount = new UserAccount();
        try {
            userAccount = retrieveUser.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return userAccount;
    }

    /**
     * Log the user out of the app.
     * @param context
     */
    public static void logUserOut(Context context) {
        userAccount = null;
        UserController.saveUserAccountInLocalFile(userAccount, context);
    }

    /**
     * This loads a user account from USER_FILE.
     */
    public static UserAccount loadUserAccountFromLocalFile() {

        try {
            FileInputStream fis = controller.getContext().openFileInput(USER_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<UserAccount>() {}.getType();
            return gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            return new UserAccount();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This saves a user account in to USER_FILE.
     * @param
     */
    public static void saveUserAccountInLocalFile(UserAccount user, Context context) {

        controller.setContext(context);

        try {
            FileOutputStream fos = context.openFileOutput(USER_FILE, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(user, writer);
            writer.flush();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        catch (IOException e) {
            throw new RuntimeException();
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
