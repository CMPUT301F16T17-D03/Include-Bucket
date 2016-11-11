package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by michelletagarino on 16-11-02.
 *
 * This class controls how UserAccounts are added by way of using the JestDroidClient
 *     library. The user and its data is stored as an index ["cmput301f16t17"] in
 *     Elasticsearch under the type 'user'. Each index will automatically be assigned
 *     its own elasticsearch ID.
 */
public class ElasticsearchController {

    private static JestDroidClient client;

    // TODO : This method creates a UserAccount instance
    public static class CreateUserTask extends AsyncTask<UserAccount, Void, Void> {
        @Override
        protected Void doInBackground(UserAccount... userAccounts) {
            verifySettings();
            for (UserAccount user : userAccounts) {
/*
            UserAccount foundUser = new UserAccount();

            Boolean isUserNameTaken = true;

                /**
                 * Check first to see if the username is unique.
                 * If not taken, isUserNameTaken==FALSE.
                 */
/*                String search_string = "{\"query\": { \"match\": { \"_id\": \"" + user.getUniqueUserName() + "\" }}}";
                Search search = new Search.Builder(search_string)
                        .addIndex("cmput301f16t17")
                        .addType("user")
                        .build();
                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        foundUser = result.getSourceAsObject(UserAccount.class);
                    } else {
                        isUserNameTaken = false;
                        Log.i("Error", "The search query failed to find any users that matched.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
*/
                /**
                 * If username is not already taken (if isUserNameTaken==FALSE), create a user.
                 */
                //if (!isUserNameTaken) {
                    Index index = new Index.Builder(user)
                            .index("cmput301f16t17")
                            .type("user")
                            .id(user.getUniqueUserName()).build();
                    try {
                        DocumentResult result = client.execute(index);
                        if (result.isSucceeded()) {
                            user.setUid(result.getId());
                        } else {
                            Log.i("Error", "Elastic search was not able to add the user.");
                        }
                    } catch(Exception e){
                        Log.i("Error", "We failed to add a user to elastic search!");
                        e.printStackTrace();
                    }
                //} else Log.i("Error", "Username is taken!");
            }
            return null;
        }
    }

    // TODO : This method retrieves a UserAccount
    public static class RetrieveUserTask extends AsyncTask<String, Void, UserAccount> {
        @Override
        protected UserAccount doInBackground(String... userLogin) {
            verifySettings();

            UserAccount foundUser = new UserAccount();
            /**
             * This query retrieves one user instance specified by the login input in LoginActivity
             */
            String search_string = "{\"query\": { \"match\": { \"_id\": \"" + userLogin[0] + "\" }}}";

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t17")
                    .addType("user")
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    foundUser = result.getSourceAsObject(UserAccount.class);
                } else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
                return foundUser;
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                return null;
            }
        }
    }

    // TODO : This method retrieves a UserAccount
    public static class CheckUserTask extends AsyncTask<String, Void, UserAccount> {
        @Override
        protected UserAccount doInBackground(String... userLogin) {
            verifySettings();

            UserAccount foundUser = new UserAccount();
            String search_string = "{\"query\": { \"match\": { \"_id\": \"" + userLogin[0] + "\" }}}";

            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t17")
                    .addType("user")
                    .build();
            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    foundUser = result.getSourceAsObject(UserAccount.class);
                } else {
                    Log.i("Error", "The search query failed to find any users that matched.");
                }
                return foundUser;
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                return null;
            }
        }
    }
    // Taken from https://github.com/SRomansky/lonelyTwitter/blob/lab7end/app/src/main/java/ca/ualberta/cs/lonelytwitter/ElasticsearchTweetController.java
    // Accessed November 2, 2016
    // Author: sromansky
    private static void verifySettings() {
        // Create the client if it hasn't already been initialized
        if (client == null) {
            DroidClientConfig.Builder builder;
            builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
