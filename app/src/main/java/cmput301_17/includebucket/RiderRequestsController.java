package cmput301_17.includebucket;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-22.
 */
public class RiderRequestsController {

    private Context context;

    private static RequestList riderRequests = null;

    public static RequestList getRiderRequests() {

        if (riderRequests == null) {
            riderRequests = getRequestsFromElasticSearch();
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

        getRiderRequests().addRequest(request);

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

        getRiderRequests().deleteRequest(request);
    }

    /**
     * Get list of current requests made by the rider.
     */
    public static RequestList getRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();

        ArrayList<String> requestIds = user.getRiderRequestIds();
        for (String requestId : requestIds) {

            ElasticsearchRequestController.GetRiderRequests riderRequests;
            riderRequests = new ElasticsearchRequestController.GetRiderRequests();
            riderRequests.execute(requestId);

            try {
                requestList.add(riderRequests.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return requestList;
    }
}
