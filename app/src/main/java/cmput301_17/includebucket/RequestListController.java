package cmput301_17.includebucket;

import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestListController {

    private static RequestList requestList = new RequestList();

    static public RequestList getRequestList(String userLogin) {
        requestList = getRequestsFromElasticSearch(userLogin);
        return requestList;
    }

    static public RequestList getKeywordList(String keyword) {
        requestList = getKeywordRequests(keyword);
        return requestList;
    }

    /**
     * This returns a list of requests from ElasticSearch specified by a keyword.
     * @return requests
     */
    public static RequestList getKeywordRequests(String keyword) {

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
    public static RequestList getRequestsFromElasticSearch(String userLogin) {

        ElasticsearchRequestController.GetRequests retrieveRequests;
        retrieveRequests = new ElasticsearchRequestController.GetRequests();
        retrieveRequests.execute(userLogin);

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
     * This will return a list of requests from a local file.
     * @param
     */
    public static RequestList getRequestsFromLocalFile() {
        return null;
    }

    /**
     * This adds a request to the list.
     * @param request
     */
    public void addRequest(Request request, String userLogin) {
        getRequestList(userLogin).addRequest(request);
    }
}
