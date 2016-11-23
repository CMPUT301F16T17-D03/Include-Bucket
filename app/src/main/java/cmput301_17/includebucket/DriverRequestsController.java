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
            driverRequests = getRequestsFromElasticSearch();
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
    public static String addRequest(Request request) {

        ElasticsearchRequestController.CreateRequest createRequest;
        createRequest = new ElasticsearchRequestController.CreateRequest();
        createRequest.execute(request);

        String requestId = null;

        try {
            requestId = createRequest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getDriverRequests().addRequest(request);

        return requestId;
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
     * TODO Get list of current requests made by the driver.
     */
    public static RequestList getRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();
        Request request = new Request();

        // Get ALL the requests from the server
        ElasticsearchRequestController.GetDriverRequests driverRequests;
        driverRequests = new ElasticsearchRequestController.GetDriverRequests();
        driverRequests.execute("");

        // Filter out driver's own requests
        try {
            requestList = driverRequests.get();

            if (!driverRequests.equals(user.getUserId()))
            {

            }
                requestList.add(driverRequests.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        ArrayList<String> requestIds = user.getRiderRequestIds();
        for (String requestId : requestIds) {


        }
        return requestList;
    }
}
