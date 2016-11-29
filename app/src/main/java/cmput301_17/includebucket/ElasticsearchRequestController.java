package cmput301_17.includebucket;

import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;

import static java.lang.Math.cos;

/**
 * ElasticsearchRequestController
 *
 * This class controls how the Requests in ElasticSearch are added, retrieved, and deleted.
 */

public class ElasticsearchRequestController {

    private static JestDroidClient client;

    /**
     * This method creates a Request instance
     * @return requestId
     */
    public static class CreateRequest extends AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            String requestId = null;

            for (Request request : requests) {
                Index index = new Index.Builder(request)
                        .index("cmput301f16t17")
                        .type("request")
                        .build();
                try {
                    client.execute(index);
                } catch(Exception e){
                    Log.i("Error", "Failed to add request to elasticsearch!");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * This method retrieves all open requests where the user is not the rider.
     */
    public static class GetOpenRequests extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... search_params) {
            verifySettings();
            UserAccount driver = UserController.getUserAccount();
            RequestList requestList = new RequestList();

            SearchResult result;

            //String query ="{\"from\": 0, \"size\": 10000," +
                    //"\"query\": { \"match\": {\"riderAccepted\" : \"false\"}}}";
            String query =
                    "{\"query\": { \"bool\": { \"must\": [{\"term\":"+
                            "{\"riderAccepted\": \"false\"}}],\"mustNot\":[{\"term\": {\"rider.uniqueUserName\":\""
                    + driver.getUniqueUserName() + "\"}}]}}}";

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
                    requestList = new RequestList();
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
                    requests = new RequestList();
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
     * This method retrieves all of the rider's requests in the database.
     */
    public static class GetRiderRequestsById extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... rider) {
            verifySettings();

            RequestList requests = new RequestList();

            String search_string =
                    "{\"from\": 0, \"size\": 10000," +
                            "\"query\": { \"match\": {\"rider.uniqueUserName\" : \"" + rider[0] + "\"}}}";

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
                    requests = new RequestList();
                    Log.i("Error", rider[0] + " The search query did not match any requests in the database.");
                }
            }
            catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }
            return requests;
        }
    }

    /**
     * This method retrieves all of the driver's requests in the database.
     */
    public static class GetDriverRequests extends AsyncTask<UserAccount, Void, RequestList> {
        @Override
        protected RequestList doInBackground(UserAccount... driver) {
            verifySettings();

            RequestList requests = new RequestList();

            String search_string =
                        "{\"query\": { \"bool\": { \"should\": [{\"term\": {\"driver.uniqueUserName\": \""
                                + driver[0].getUniqueUserName() +
                                "\"}},{\"term\": {\"pendingDrivers.uniqueUserName\":\""
                                + driver[0].getUniqueUserName() + "\"}}]}}}";

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

            String search_string;

            if (search_param[0].length() == 0)
            {
                search_string = "{\"from\": 0, \"size\": 10000}";
            }
            else search_string = "{\"from\": 0, \"size\": 10000," +
                    "\"query\": { \"match\": {\"riderStory\": \"" + search_param[0] + "\" }}}";

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
                    requests = new RequestList();
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
     * Gets a list of addresses nearby a specified location
     */
    public static class GetNearbyList extends AsyncTask<String, Void, RequestList> {
        @Override
        protected RequestList doInBackground(String... search_param) {

            verifySettings();
            String ret;
            Address location=new Address(Locale.CANADA);
            try {
                GeocoderNominatim geoCoder = new GeocoderNominatim("Include-Bucket");
                List<Address> address = geoCoder.getFromLocationName(search_param[0], 1);
                if (address==null){
                    ret = null;

                }
                location = address.get(0);
                ret = location.getLatitude()+","+location.getLongitude();
                Log.i("Success", "Found address");
            }
            catch(Exception e){
                ret = null;
                Log.i("Error", "Could not find address");
            }
            RequestList requests = new RequestList();
            String search_string;
            if(ret == null){
                search_string = "{\"from\": 0, \"size\": 10000}";

            }
            else if (search_param[0].length() == 0)
            {
                search_string = "{\"from\": 0, \"size\": 10000}";
            }

            //else search_string ="{\"query\":{\"filtered\":{\"query\":{\"match_all\":{}},\"filter\":{\"geo_distance\":{\"distance\" : \"5km\",\"startLocation\":\""+search_param[0]+"\"}}}}}";
            else {search_string = "{\"from\": 0,\"size\": 10000,\"query\" : {\"match_all\" : {}},\"filter\" : {\"range\" : { \"location.mLatitude\" : {\"gte\":"+(location.getLatitude()-0.05)+", \"lte\":"+(location.getLatitude()+0.05)+"}}}, \"filter\" : {\"range\" : {\"location.mLongitude\" : {\"gte\":"+(location.getLongitude()-0.05)+",\"lte\":"+(location.getLongitude()+0.05/cos(location.getLatitude()))+"}}}}";}
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
                    requests = new RequestList();
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

            for (Request r : requests) {
                Delete deleteRequest = new Delete
                        .Builder(r.getRequestID().toString())
                        .index("cmput301f16t17")
                        .type("request")
                        .build();

                try {
                    client.execute(deleteRequest);
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