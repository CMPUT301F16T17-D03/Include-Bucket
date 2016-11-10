package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

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
        protected Void doInBackground(UserAccount... userList) {
            verifySettings();
            for (UserAccount user : userList) {
                Index index = new Index.Builder(user).index("cmput301f16t17").type("user").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setUid(result.getId());
                        Log.i("Yay", "User was added!");
                    }
                    else { Log.i("Error", "Elastic search was not able to add the user."); }
                }
                catch (Exception e) {
                    Log.i("Dang, fam", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    // TODO : This method retrieves a UserAccount
    public static class RetrieveUserTask extends AsyncTask<UserAccount, Void, Void> {
        @Override
        protected Void doInBackground(UserAccount... userList) {
            return null;
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
