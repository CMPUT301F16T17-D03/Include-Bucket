package cmput301_17.includebucket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestList extends ArrayList {

    protected Collection<Request> requestList;
    protected Collection<Listener> listeners;

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

    public void addAll(List<Request> requests) {
        for(Request r : requests) {
            this.add(r);
        }
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