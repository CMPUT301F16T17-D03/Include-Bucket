package cmput301_17.includebucket;

import android.content.Context;

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

    private static RequestList requestList = new RequestList();
    private static final String REQUEST_LIST = "file.sav";
    private static Context context;

    static public RequestList getRequestList(String list) {
        requestList = getRequestsFromElasticSearch(list);
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
    public RequestList loadRequestsFromLocalFile() {
        try {
            FileInputStream fis = context.openFileInput(REQUEST_LIST);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Request>>() {}.getType();
            requestList = gson.fromJson(in, listType);
        }
        catch (FileNotFoundException e) {
            requestList = new RequestList();
        }
        catch (IOException e) {
            throw new RuntimeException();
        }

        return null;
    }

    /**
     * This will grab a list of requests from Elasticsearch and save it in a local file.
     * @param
     */
    public void saveRequestsInLocalFile() {



        try {
            FileOutputStream fos = context.openFileOutput(REQUEST_LIST, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(requestList, writer);
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
    public void addRequest(Request request, String userLogin) {
        getRequestList(userLogin).addRequest(request);
    }
}
