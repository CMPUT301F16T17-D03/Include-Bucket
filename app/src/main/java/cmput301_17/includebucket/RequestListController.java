package cmput301_17.includebucket;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestListController {

    private static RequestList requestList = null;
    private static UserAccount user = new UserAccount();
    private static RequestList requestListRider = new RequestList();
    //private static RequestList requestListDriver = new RequestList();

    private static final String REQUESTS_RIDER  = "rider_requests.sav";
    private static final String REQUESTS_DRIVER = "driver_requests.sav";

    private Context context;

    private static RequestListController controller = new RequestListController();

    public static RequestList getRequestList() {

        //UserAccount user = UserController.getUserAccount();
/*
        if (user.getUserCategory().equals(UserAccount.UserCategory.rider))
        {
            Log.i("Whoa","I did something here.");
            //requestList = getRequestsByUid();
        }
        else
        {
            Collection<Request> requests = loadRequestsFromLocalFile();
            requestList.clear();
            for (Request r : requests) {
                if (!user.getUniqueUserName().equals(r.getUser().getUniqueUserName()))
                {

                    requestList.add(r);
                }
            }
        }*/
        if (requestList == null) {
            requestList = getAllRequestsFromElasticSearch();
        }
        return requestList;
    }

    /*
    static public RequestList getRequestList(String list) {
        requestList = getRequestsFromElasticSearch(list);
        return requestList;
    }
    */
    static public RequestList getKeywordList(String key) {
        requestList = getRequestsByKeyword(key);
        return requestList;
    }

    static public void deleteRequestFromList(Request request) {
        deleteRequestFromElasticSearch(request);
    }

    public void setContext (Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
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
     * This returns a list of requests from ElasticSearch with specified user login.
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
     * This returns a list of ALL requests from ElasticSearch.
     * @return requests
     */
    public static RequestList getAllRequestsFromElasticSearch() {

        ElasticsearchRequestController.GetAllRequests retrieveRequests;
        retrieveRequests = new ElasticsearchRequestController.GetAllRequests();
        retrieveRequests.execute("");

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
     * This will return a list of requests from a local file for offline behaviour.
     * @param
     */
    public static RequestList loadRequestsFromLocalFile() {

        Context context = controller.getContext();
        try {
            FileInputStream fis = context.openFileInput(REQUESTS_RIDER);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<RequestList>() {}.getType();
            requestList = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            requestList = new RequestList();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
        return requestList;
    }

    /**
     * This will grab a list of requests from Elasticsearch and save it in a local file.
     * @param
     */
    public static void saveRequestsInLocalFile(RequestList requests, Context context) {

        String file;

        UserAccount user = UserController.getUserAccount();

        controller.setContext(context);

        if(user.getUserCategory()==(UserAccount.UserCategory.rider))
        {
            file = REQUESTS_RIDER;
        }
        else file = REQUESTS_DRIVER;

        try {
            FileOutputStream fos = context.openFileOutput(file, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(requests, writer);
            writer.flush();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This adds a request to the list.
     * @param request
     */
    public void addRequest(Request request) {
        getRequestList().addRequest(request);
    }

    /**
     * Get list of current requests made by a certain user.
     * TODO : it's getting all the requests but the UserController is sending a null object
     */
    public static RequestList getRequestsByUid() {

        RequestList requests = getAllRequestsFromElasticSearch();
        UserAccount user = UserController.getUserAccount();

        String uid = user.getUid();

        for (int i = 0; i < requests.size(); i++) {

            String userId = requests.get(i).getUser().getUid();

            if (userId.equals(uid)) {
                Log.i("Success","Got someeeee.");
                requestListRider.add(requests.get(i));
            }
        }
        return requestListRider;
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
}
