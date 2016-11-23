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

        UserAccount user = UserController.getUserAccount();

        if (riderRequests == null) {
            riderRequests = new RequestList();
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

        ElasticsearchRequestController.DeleteRequest deleteRequests;
        deleteRequests = new ElasticsearchRequestController.DeleteRequest();
        deleteRequests.execute(request);

        getRiderRequests().deleteRequest(request);
    }

    /**
     * TODO Get list of current requests made by the rider.
     */
    public static RequestList getRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();
        ArrayList<String> requestIds = user.getRiderRequestIds();

        if (requestIds == null) {
            requestIds = new ArrayList<>();
        }

        ElasticsearchRequestController.GetAllRequests foundRequests;
        foundRequests = new ElasticsearchRequestController.GetAllRequests();
        foundRequests.execute(user.getUniqueUserName());

        try {
            requestIds = foundRequests.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.i("The user is a ",user.getUserCategory() + " ELASTIC");

        return requestList;
    }
}
