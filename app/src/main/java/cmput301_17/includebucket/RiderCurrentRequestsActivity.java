package cmput301_17.includebucket;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import java.util.Collection;

/**
 * This class displays the list of the rider's requests.
 */
public class RiderCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_requests);

        requestsListView = (ListView) findViewById(R.id.requestsListView);

        requests = RequestListController.getRequestList();
        requestList = new ArrayList<>();
        requestList.addAll(requests);

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);

        /**
         * Updates the ArrayAdapter when a request is added.
         */
        RequestListController.getRequestList().addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getRequestList().getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
/*
        list = new ArrayList<>();

        ElasticSearchRequestController.GetRequests getRequests = new ElasticSearchRequestController.GetRequests();
        getRequests.execute("");
        try {
            list = getRequests.get();
        }
        catch (Exception e) {
            Log.i("Error", "Failed to get the requests out of the async object.");
        }
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        requestsListView.setAdapter(requestAdapter);
*/
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}

