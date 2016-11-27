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
 * RiderRequestsController
 *
 * This is a controller class for a rider's request.
 */
public class RiderRequestsController {

    private Context context;
    private static RequestList riderRequests = null;

    /**
     * This returns a list of requests.
     * @return RequestList
     */
    public static RequestList getRiderRequests() {

        if (riderRequests == null) {
            try {
                riderRequests = RiderRequestsFileManager.getRequestListFileManager().loadRequestList();
                riderRequests.addListener(new Listener() {
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
        return riderRequests;
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
     * This adds a request to the riderRequests list.
     * @param request
     */
    public static void addRiderRequest(Request request) {

        getRiderRequests().addRequest(request);
    }

    /**
     * This deletes a rider request from the riderRequests list.
     * @return requests
     */
    public static void deleteRequest(Request request) {

        getRiderRequests().deleteRequest(request);
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
    }

    /**
     * Get list of current requests made by the rider.
     */
    public static void loadRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();

        ElasticsearchRequestController.GetRiderRequests riderList;
        riderList = new ElasticsearchRequestController.GetRiderRequests();
        riderList.execute(user);

        RequestList requestList = new RequestList();
        try {
            requestList.getRequests().addAll(riderList.get());
            riderRequests = requestList;
            Log.i("SUCCESS","list size " + riderRequests.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This saves a request to USER_FILE.
     * @param
     */
    public static void saveRequestsInLocalFile() {

        try {
            RiderRequestsFileManager.getRequestListFileManager().saveRequestList(getRiderRequests());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not deserialize RiderRequests from RequestListFileManagaer");
        }
    }
}
