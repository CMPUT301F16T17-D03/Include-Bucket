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
 * Created by michelletagarino on 16-11-22.
 */
public class RiderRequestsController {

    private Context context;
    private static RiderRequestsController controller = new RiderRequestsController();

    private static RequestList riderRequests = null;
    private static final String RIDER_REQUESTS_FILE = "rider_requests.sav";

    public static RequestList getRiderRequests() {

        if (riderRequests == null) {
            //riderRequests = getRequestsFromElasticSearch();
            loadRequestsFromLocalFile();
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
     * This adds a request to Elasticsearch and the riderRequests list.
     * @param request
     */
    public static void addRiderRequest(Request request) {

        getRiderRequests().addRequest(request);
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
     * This deletes a rider request from the riderRequests list.
     * @return requests
     */
    public static void deleteRequest(Request request) {

        getRiderRequests().deleteRequest(request);
    }

    /**
     * Get list of current requests made by the rider.
     */
    public static void loadRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();

        ElasticsearchRequestController.GetRiderRequests riderList;
        riderList = new ElasticsearchRequestController.GetRiderRequests();
        riderList.execute(user);

        Log.i("SUCCESS","Found " + user.getUniqueUserName());

        RequestList requestList = new RequestList();
        try {
            requestList.getRequests().addAll(riderList.get());
            riderRequests = requestList;
            Log.i("SUCCESS","list size " + requestList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    /************************************** OFFLINE BEHAVIOUR *************************************/
    /**
     * This loads a user account from RIDER_REQUESTS_FILE.
     */
    public static void loadRequestsFromLocalFile() {

        try {
            FileInputStream fis = controller.getContext().openFileInput(RIDER_REQUESTS_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<RequestList>() {}.getType();
            riderRequests = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            riderRequests = new RequestList();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This saves a request to USER_FILE.
     * @param
     */
    public static void saveRequestInLocalFile(Collection<Request> requestList, Context context) {

        controller.setContext(context);

        try {
            FileOutputStream fos = context.openFileOutput(RIDER_REQUESTS_FILE, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(requestList, writer);
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
     * Log the user out of the app.
     * @param context
     */
    public static void clearRiderRequests(Context context) {
        riderRequests = null;
        RiderRequestsController.saveRequestInLocalFile(riderRequests, context);
    }
}
