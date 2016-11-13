package cmput301_17.includebucket;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestListController {

    private static RequestList requestList = null;

    static public RequestList getRequestList() {
        if (requestList == null) {
            requestList = new RequestList();
        }

        return requestList;
    }

    public void addRequest(Request request) {
        getRequestList().addRequest(request);
    }
}
