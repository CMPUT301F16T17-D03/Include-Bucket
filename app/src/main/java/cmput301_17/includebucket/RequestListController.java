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
import java.util.concurrent.ExecutionException;

/**
 * Created by michelletagarino on 16-11-12.
 */
public class RequestListController {

    private static RequestListController controller = new RequestListController();

    private static RequestList requestList = null;
    private static RequestList requestListRider  = null;
    private static RequestList requestListDriver = null;

    private static final String REQUESTS_RIDER  = "rider_requests.sav";
    private static final String REQUESTS_DRIVER = "driver_requests.sav";

    private Context context;

    public static RequestList getRequestList() {

        UserAccount user = UserController.getUserAccount();

        if (requestList == null)
        {
            requestListRider = new RequestList();
            requestListDriver = new RequestList();
        }
        else
        {
            if (user.getUserState() == (UserAccount.UserState.rider))
            {
                requestList = requestListRider;
                Log.i("Success", "This user is a rider." + user.getUniqueUserName());
            }
            else
            {
                requestList = requestListDriver;
                Log.i("Uh oh", "The fuck did you do...");
                Log.i("User", " " + user.getUserState());
            }
        }
        return requestList;
    }

    static public RequestList getKeywordList(String key) {
        RequestList requestList = getRequestsByKeyword(key);
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
    public static RequestList getRiderRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();

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
    //public static RequestList getDriverRequestsFromElasticSearch(UserAccount user) {

    /*public static RequestList getBlehRequestsFromElasticSearch(String userLogin) {
>>>>>>> origin/mctago

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
*/
    /**
     * This returns a list of ALL requests from ElasticSearch.
     * @return requests
     */
    /*
    public static RequestList getRequestsFromElasticSearch(String userLogin) {
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
    */




    /**
     * This will return a list of requests from a local file for offline behaviour.
     * @param
     */
    public static RequestList loadRequestsFromLocalFile() {

        UserAccount user = UserController.getUserAccount();
        Context context = controller.getContext();

        String file;
        if(user.getUserState()==(UserAccount.UserState.rider))
        {
            file = REQUESTS_RIDER;
            Log.i("Success","This user is a " + user.getUserState());
        }
        else
        {
            file = REQUESTS_DRIVER;
            Log.i("Uh oh","This user is a " + user.getUserState());
        }

        try {
            FileInputStream fis = context.openFileInput(file);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<RequestList>() {}.getType();
            return gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            return new RequestList();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * This will grab a list of requests from Elasticsearch and save it in a local file.
     * @param
     */
    public static void saveRequestsInLocalFile(RequestList requests, Context context) {

        UserAccount user = UserController.getUserAccount();

        controller.setContext(context);

        String file;
        if(user.getUserState() == (UserAccount.UserState.rider))
        {
            file = REQUESTS_RIDER;
            Log.i("Saving"," " + user.getUniqueUserName());
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
     * Get list of current requests made by a certain user.
     * TODO : it's getting all the requests but the UserController is sending a null object
     */
 /*   public static RequestList getRequestsFromElasticSearch() {

        UserAccount user = UserController.getUserAccount();
        RequestList requestList = new RequestList();

        ArrayList<String> requestIds = user.getRiderRequestIds();


        ElasticsearchRequestController.GetAllRequests foundRequests;
        foundRequests = new ElasticsearchRequestController.GetAllRequests();

        if (user.getUserState() == (UserAccount.UserState.rider))
        {
            // Get requests specified by user login
            foundRequests.execute(user.getUniqueUserName());
            try {
                requestListRider = foundRequests.get();
                requestList = requestListRider;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.i("The user is a ",user.getUserState() + " ELASTIC");
        }
        else
        {
            // Get all the requests
            foundRequests.execute("");
            try {
                requestListDriver = foundRequests.get();
                requestList = requestListDriver;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Log.i("The user is a ","" + user.getUserState());
        }
        return requestList;
    }



    /**
     * This adds a request to Elasticsearch and the local list.
     * @param request
     */
    public void addRequest(Request request) {

        // Add the request to Elasticsearch.
        ElasticsearchRequestController.CreateRequest createRequest;
        createRequest = new ElasticsearchRequestController.CreateRequest();
        createRequest.execute(request);

        //getRequestList().addRequest(request);
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

    public static void clearRequestLists() {
        requestListRider = null;
        requestListDriver = null;
    }
}