package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by michelletagarino on 16-11-12.
 *
 * This class controls how the Requests in Elasticsearch are added, retrieved, and deleted.
 */
public class ElasticsearchRequestController {

    private static JestDroidClient client;

    /**
     * This method creates a Request instance
     */
    public static class CreateRequest extends AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request request : requests) {
                Index index = new Index
                        .Builder(request)
                        .index("cmput301f16t17")
                        .type("request")
                        .build();

                try {
                    DocumentResult result = client.execute(index);
                } catch(Exception e){
                    Log.i("Error", "Failed to add request to elasticsearch!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }



    /**
     * This method retrieves all the requests in the database.
     */

    /*
    public static class GetRequests extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<Request>();

            String search_string = "{\"from\": 0, \"size\": 10000}";
            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t17")
                    .addType("request")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded())
                {
                    List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);
                }
                else
                {
                    Log.i("Error", "The search query did not match any requests in the database.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return requests;
        }
    }
    */

    /**
     * This method retrieves all the requests in the database.
     */
    public static class GetRequests extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... search_parameters) {
            verifySettings();

            RequestList requests = new RequestList();

            String search_string = "{\"from\": 0, \"size\": 10000}";
            Search search = new Search.Builder(search_string)
                    .addIndex("cmput301f16t17")
                    .addType("request")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded())
                {
                    List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);
                }
                else
                {
                    Log.i("Error", "The search query did not match any requests in the database.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return requests;
        }
    }

    /**
     * This method deletes a Request specified by an ID.
     */
    public static class DeleteRequest extends AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request request : requests) {

                Delete deleteRequest = new Delete
                        .Builder(request.getRequestID().toString())
                        .index("cmput301f16t17")
                        .type("request")
                        .build();

                try {
                    DocumentResult result = client.execute(deleteRequest);
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }
            return null;
        }
    }

    /**
     * Taken from https://github.com/SRomansky/lonelyTwitter/blob/lab7end/app/src/main/java/ca/ualberta/cs/lonelytwitter/ElasticsearchTweetController.java
     * Accessed November 2, 2016
     * Author: sromansky
     */
    private static void verifySettings() {
        // Create the client if it hasn't already been initialized
        if (client == null)
        {
            DroidClientConfig.Builder builder;
            builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
