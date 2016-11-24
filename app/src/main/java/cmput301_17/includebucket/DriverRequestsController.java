package cmput301_17.includebucket;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-22.
 */
public class DriverRequestsController {

    private Context context;

    private static RequestList driverRequests = null;

    public static RequestList getDriverRequests() {

        if (driverRequests == null) {
            driverRequests = getOpenRequests();
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

        ElasticsearchRequestController.CreateRequest createRequest;
        createRequest = new ElasticsearchRequestController.CreateRequest();
        createRequest.execute(request);

        getDriverRequests().addRequest(request);
    }

    /**
     * This deletes a rider request from ElasticSearch and the riderRequests list.
     * @return requests
     */
    public static void deleteRequest(Request request) {

        ElasticsearchRequestController.DeleteRequest deleteRequest;
        deleteRequest = new ElasticsearchRequestController.DeleteRequest();
        deleteRequest.execute(request);

        getDriverRequests().deleteRequest(request);
    }

    /**
     * Get open requests
     */
    public static RequestList getOpenRequests() {

        UserAccount user = UserController.getUserAccount();
        RequestList requests = new RequestList();

        // Get ALL the open requests from the server
        ElasticsearchRequestController.GetOpenRequests openRequests;
        openRequests = new ElasticsearchRequestController.GetOpenRequests();
        openRequests.execute("");

        try {
            requests = openRequests.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requests;
    }

    /**
     * TODO Get list of current requests the driver is involved in.
     */
    public static RequestList getDriverInvolvedRequests() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();

        // Get ALL the requests from the server
        ElasticsearchRequestController.GetDriverRequests driverRequests;
        driverRequests = new ElasticsearchRequestController.GetDriverRequests();
        driverRequests.execute(user);

        return requestList;
    }

    /**
     * This returns a list of requests from ElasticSearch specified by a keyword.
     * @return requests
     */
    public static RequestList getRequestsByKeyword(String keyword) {

        ElasticsearchRequestController.GetKeywordList retrieveRequests;
        retrieveRequests = new ElasticsearchRequestController.GetKeywordList();
        retrieveRequests.execute(keyword);

        RequestList requests = new RequestList();

        try {
            requests = retrieveRequests.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return requests;
    }
}
