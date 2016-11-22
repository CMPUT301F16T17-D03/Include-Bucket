package cmput301_17.includebucket;

import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestListController {

    private static RequestList requestList = new RequestList();

    static public RequestList getRiderRequestList(UserAccount user) {
        requestList = getRiderRequestsFromElasticSearch(user);
        return requestList;
    }
    static public RequestList getDriverRequestList(UserAccount user) {
        requestList = getDriverRequestsFromElasticSearch(user);
        return requestList;
    }
    static public RequestList getKeywordList(String key) {
        requestList = getRequestsByKeyword(key);
        return requestList;
    }

    static public void deleteRequestFromList(Request request) {
        deleteRequestFromElasticSearch(request);
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

    /**
     * This returns a list of requests from ElasticSearch.
     * @return requests
     */
    public static RequestList getRiderRequestsFromElasticSearch(UserAccount user) {

        ElasticsearchRequestController.GetRiderRequests retrieveRequests;
        retrieveRequests = new ElasticsearchRequestController.GetRiderRequests();
        retrieveRequests.execute(user);

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

    /**
     * This returns a list of requests from ElasticSearch.
     * @return requests
     */
    public static RequestList getDriverRequestsFromElasticSearch(UserAccount user) {

        ElasticsearchRequestController.GetDriverRequests retrieveRequests;
        retrieveRequests = new ElasticsearchRequestController.GetDriverRequests();
        retrieveRequests.execute(user);

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

    /**
     * This deletes a request from ElasticSearch.
     * @return requests
     */
    public static void deleteRequestFromElasticSearch(Request request) {

        ElasticsearchRequestController.DeleteRequest deleteRequests;
        deleteRequests = new ElasticsearchRequestController.DeleteRequest();
        deleteRequests.execute(request);
    }

    /**
     * This will return a list of requests from a local file for offline behaviour.
     * @param
     */
    public static RequestList getRequestsFromLocalFile() {
        return null;
    }

    /**
     * This adds a request to the list.
     * @param request
     */
    public void addRequest(Request request, UserAccount user) {
        getRiderRequestList(user).addRequest(request);
    }
}
