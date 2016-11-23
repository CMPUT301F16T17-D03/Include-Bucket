package cmput301_17.includebucket;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

/**
 * Created by michelletagarino on 16-11-12.
 *
 * This class controls how the Requests in Elasticsearch are added, retrieved, and deleted.
 */

public class ElasticsearchRequestController {

    private static JestDroidClient client;

    /**
     * This method creates a Request instance
     * @return requestId
     */
    public static class CreateRequest extends AsyncTask<Request, Void, String> {
        @Override
        protected String doInBackground(Request... requests) {
            verifySettings();

            String requestId = null;

            for (Request request : requests) {
                Index index = new Index.Builder(request)
                        .index("cmput301f16t17")
                        .type("request")
                        .build();
                try {
                    DocumentResult result = client.execute(index);

                    if (result.isSucceeded())
                    {
                        requestId = result.getId();
                    }
                    else
                    {
                        Log.i("Error","Could not retrieve doc from Elasticsearch!");
                    }
                } catch(Exception e){
                    Log.i("Error", "Failed to add request to elasticsearch!");
                    e.printStackTrace();
                }
            }
            return requestId;
        }
    }

    /**
     * This method retrieves a request made by a particular user, specified by the request ID.
     * Will retrieve only one request at a time.
     */
    public static class GetRidersRequests extends AsyncTask<String, Void, Request> {
        @Override
        protected Request doInBackground(String... requestIds) {
            verifySettings();

            Request request = new Request();

            DocumentResult result;

            Get requests = new Get.Builder("cmput301f16t17", requestIds[0]).type("request").build();
            try {
                result = client.execute(requests);

                if(result.isSucceeded())
                {
                    request = result.getSourceAsObject(Request.class);
                }
                else
                {
                    Log.i("Error", "The search query did not match any requests in the database.");
                }

            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return request;
        }
    }

    /**
     * This method retrieves all requests. Essentially, the driver will obtain these results,
     * which will then be filtered out so that only the requests not including their own will
     * be left in the DriverCurrentRequestsActivity.
     */
    public static class GetOpenRequests extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... search_parameters) {
            verifySettings();

            RequestList requestList = new RequestList();

            SearchResult result;

            String query = "{\"from\": 0, \"size\": 10000}";
            Search search = new Search
                    .Builder(query)
                    .addIndex("cmput301f16t17")
                    .addType("request").build();

            try {
                result = client.execute(search);

                if(result.isSucceeded())
                {
                    List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                    requestList.addAll(foundRequests);
                }
                else
                {
                    Log.i("Error", "The search query did not match any requests in the database.");
                }
            } catch (IOException e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return requestList;
        }
    }

    /**
     * This method retrieves all of the rider's requests in the database.
     */
    public static class GetRiderRequests extends AsyncTask<UserAccount, Void, RequestList> {
        @Override
        protected RequestList doInBackground(UserAccount... rider) {
            verifySettings();

            RequestList requests = new RequestList();

            String search_string =
                    "{\"from\": 0, \"size\": 10000," +
                    "\"query\": { \"match\": {\"rider.uniqueUserName\" : \"" + rider[0].getUniqueUserName() + "\"}}}";

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
                    Log.i("Success", "Got the requests.");
                }
                else
                {
                    Log.i("Error", rider[0].getUniqueUserName() + " The search query did not match any requests in the database.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return requests;
        }
    }

    /**
     * This method retrieves all of the drivers requests in the database.
     */
    public static class GetDriverRequests extends AsyncTask<UserAccount, Void, RequestList> {
        @Override
        protected RequestList doInBackground(UserAccount... driver) {
            verifySettings();

            RequestList requests = new RequestList();

            //String search_string = "{\"from\": 0, \"size\": 10000}";
            String search_string =
                    "{\"from\": 0,\"size\": 10000," +
                        "\"query\": { \"match\": { \"driver.uniqueUserName\": \"" + driver[0].getUniqueUserName() + "\"}}}";

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
                    Log.i("Success", "Got the requests.");
                }
                else
                {
                    Log.i("Error", driver[0].getUniqueUserName() +" The search query did not match any requests in the database.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return requests;
        }
    }

    /**
     * This get the keyword for searching requests by keyword.
     */
    public static class GetKeywordList extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... search_param) {
            verifySettings();

            RequestList requests = new RequestList();

            //String search_string = "{\"from\": 0, \"size\": 10000}";
            String search_string = "{\"query\": { \"term\": {\"riderStory\": \"" + search_param[0] + "\" }}}}";
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
                    Log.i("Success", "Got the requests.");
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
     * Delete a Request specified by an ID.
     */
    public static class DeleteRequest extends AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            Delete deleteRequest = new Delete
                    .Builder(requests[0].getRequestID().toString())
                    .index("cmput301f16t17")
                    .type("request")
                    .build();

            try {
                client.execute(deleteRequest);
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
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