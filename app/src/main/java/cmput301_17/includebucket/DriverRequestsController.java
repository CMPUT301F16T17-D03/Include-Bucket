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
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * DriverRequestsController
 *
 * A controller class for requests that a driver is involved in.
 */
public class DriverRequestsController {

    private Context context;

    private static RequestList driverRequests = null;
    private static final String DRIVER_REQUESTS_FILE = "driver_requests.sav";

    public static RequestList getDriverRequests() {

        if (driverRequests == null) {
            try {
                driverRequests = DriverRequestsFileManager.getRequestListFileManager().loadRequestList();
                driverRequests.addListener(new Listener() {
                    @Override
                    public void update() {
                        saveRequestsInLocalFile();
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
        return driverRequests;
    }

    /**
     * Gets the current context.
     * @return context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Sets the current context.
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * This adds a request to Elasticsearch and the riderRequests list.
     * @param request
     */
    public static void addRequest(Request request) {

        getDriverRequests().addRequest(request);
    }

    /**
     * This deletes a rider request from ElasticSearch and the riderRequests list.
     * @return requests
     */
    public static void deleteRequest(Request request) {

        getDriverRequests().deleteRequest(request);
    }

    /**
     * This adds a request to Elasticsearch.
     * @param request
     */
    public static void addRequestToElasticsearch(Request request) {

        ElasticsearchRequestController.CreateRequest createRequest;
        createRequest = new ElasticsearchRequestController.CreateRequest();
        createRequest.execute(request);
    }

    /**
     * This deletes a request from Elasticsearch.
     * @param request
     */
    public static void deleteRequestFromElasticsearch(Request request) {

        ElasticsearchRequestController.DeleteRequest deleteRequest;
        deleteRequest = new ElasticsearchRequestController.DeleteRequest();
        deleteRequest.execute(request);
        Log.i("FAIL", "This is " + request.getRequestID());
    }

    /**
     * Gets open requests.
     */
    public static void loadOpenRequestsFromElasticsearch() {

        // Get ALL the open requests from the server
        ElasticsearchRequestController.GetOpenRequests openRequests;
        openRequests = new ElasticsearchRequestController.GetOpenRequests();
        openRequests.execute("");

        RequestList requestList = new RequestList();
        try {
            requestList.getRequests().addAll(openRequests.get());
            driverRequests = requestList;
            Log.i("SUCCESS","list size " + requestList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of current requests the driver is involved in.
     */
    public static void loadInvolvedRequestsFromElasticsearch() {

        UserAccount user = UserController.getUserAccount();

        ElasticsearchRequestController.GetRiderRequests driverList;
        driverList = new ElasticsearchRequestController.GetRiderRequests();
        driverList.execute(user);

        Log.i("SUCCESS","Found " + user.getUniqueUserName());

        RequestList requestList = new RequestList();
        try {
            requestList.getRequests().addAll(driverList.get());
            driverRequests = requestList;
            Log.i("SUCCESS","list size " + requestList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This returns a list of requests from ElasticSearch specified by a keyword.
     * @return requests
     */
    public static void loadRequestsByKeyword(String keyword) {

        ElasticsearchRequestController.GetKeywordList requestList;
        requestList = new ElasticsearchRequestController.GetKeywordList();
        requestList.execute(keyword);

        RequestList requests = new RequestList();
        try {
            requests.getRequests().addAll(requestList.get());
            driverRequests = requests;
            Log.i("SUCCESS","keyword list size " + requests.get(0).getRiderStory());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This saves a request to DRIVER_REQUESTS_FILE.
     * @param
     */
    public static void saveRequestsInLocalFile() {
        try {
            DriverRequestsFileManager.getRequestListFileManager().saveRequestList(getDriverRequests());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not deserialize RiderRequests from RequestListFileManagaer");
        }
    }

}
