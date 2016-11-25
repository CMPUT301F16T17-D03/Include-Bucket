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
    private static final String RIDER_REQUESTS_FILE  = "rider_requests.sav";

    public static RequestList getRiderRequests() {

        //if (riderRequests == null) {
            riderRequests = getRequestsFromElasticSearch();
            //riderRequests = getRequestsFromLocalFile();
        //}
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
     * This adds a request to Elasticsearch and the riderRequests list.
     * @param request
     */
    public static void addRequestToElasticsearch(Request request) {

        ElasticsearchRequestController.CreateRequest createRequest;
        createRequest = new ElasticsearchRequestController.CreateRequest();
        createRequest.execute(request);
    }

    /**
     * This deletes a rider request from ElasticSearch and the riderRequests list.
     * @return requests
     */
    public static void deleteRequest(Request request) {

        ElasticsearchRequestController.DeleteRequest deleteRequest;
        deleteRequest = new ElasticsearchRequestController.DeleteRequest();
        deleteRequest.execute(request);

        getRiderRequests().deleteRequest(request);
    }

    /**
     * Get list of current requests made by the rider.
     */
    public static RequestList getRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();

        ElasticsearchRequestController.GetRiderRequests riderRequests;
        riderRequests = new ElasticsearchRequestController.GetRiderRequests();
        riderRequests.execute(user);

        try {
            requestList = riderRequests.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requestList;
    }


    /************************************** OFFLINE BEHAVIOUR *************************************/
    /**
     * This loads a user account from RIDER_REQUESTS_FILE.
     */
    public static RequestList getRequestsFromLocalFile() {

        try {
            FileInputStream fis = controller.getContext().openFileInput(RIDER_REQUESTS_FILE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<Collection<Request>>() {}.getType();
            return gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            return new RequestList();
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
}
