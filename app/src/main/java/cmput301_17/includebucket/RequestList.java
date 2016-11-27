package cmput301_17.includebucket;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RequestList
 *
 * This is a list of requests.
 */
public class RequestList extends ArrayList implements Serializable {

    protected Collection<Request> requestList = null;
    protected transient ArrayList<Listener> listeners = null;

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
        for (Listener listener : getListeners()) {
            listener.update();
        }
    }

    private ArrayList<Listener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        return listeners;
    }

    public void addRequest(Request request) {
        requestList.add(request);
        notifyListeners();
    }

    public void deleteRequest(Request request) {
        requestList.remove(request);
        notifyListeners();
    }

    public Request get(int i) {
        ArrayList<Request> list = new ArrayList<>();
        list.addAll(getRequests());
        if (list.size()>0){return list.get(i);}
        else{return null;}
    }

    public int size() {
        return requestList.size();
    }

    public boolean contains(Request request) {
        return requestList.contains(request);
    }

    public void addListener(Listener l) {
        getListeners().add(l);
    }

    public void removeListener(Listener l) { getListeners().remove(l); }
}