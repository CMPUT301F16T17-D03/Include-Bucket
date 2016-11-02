package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by michelletagarino on 16-11-02.
 */
public class ElasticsearchUserAccountController {

    private static JestDroidClient client;

    // TODO : This method creates a UserAccount instance
    public static class CreateUserTask extends AsyncTask<UserAccount, Void, Void> {

        @Override
        protected Void doInBackground(UserAccount... userList) {

            verifySettings();

            for (UserAccount user : userList) {

                Index index = new Index.Builder(user).index("testing").type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        user.setUid(result.getId());
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to add the user.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }

            }


            return null;
        }
    }

    // Taken from LonelyTwitter (Lab 7)
    // Accessed November 2, 2016
    // Author: sromansky
    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
