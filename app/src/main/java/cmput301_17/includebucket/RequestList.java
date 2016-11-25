package cmput301_17.includebucket;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RequestList
 *
 * This is a list of requests.
 */
public class RequestList extends ArrayList {

    protected Collection<Request> requestList;
    protected Collection<Listener> listeners;

    /**
     * This constructor creates a new empty request list.
     */
    public RequestList(){
        requestList = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public Collection<Request> getRequests() {
        return requestList;
    }

    private void notifyListeners() {
        for (Listener listener : listeners) {
            listener.update();
        }
    }

    public void addRequest(Request request) {
        requestList.add(request);
        notifyListeners();
    }

    public void deleteRequest(Request request) {
        requestList.remove(request);
        notifyListeners();
    }

    /**
     * This adds all requests in a list to the object's list
     * //@param requests
     */
/*    public void addBulk(List<Request> requests) {
        for(int i = 0; i < requests.size(); i++ ) {
            this.add(requests.get(i));
        }
        notifyListeners();
    }
*/
    public Request get(int i) {
        ArrayList<Request> list = new ArrayList<>();
        list.addAll(getRequests());
        return list.get(i);
    }

    public int size() {
        return requestList.size();
    }

    public boolean contains(Request request) {
        return requestList.contains(request);
    }

    public void addListener(Listener l) {
        listeners.add(l);
    }

    public void removeListener(Listener l) { listeners.remove(l); }
}