package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by michelletagarino on 16-10-20.
 *
 * driver request view?
 *
 */
public class DriverCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_requests);

        user = (UserAccount) getIntent().getSerializableExtra("User");
        final String userLogin = user.getUniqueUserName();

        requestsListView = (ListView) findViewById(R.id.driverCurrentList);

        requests = RequestListController.getRequestList(userLogin);
        requestList = new ArrayList<>();
        requestList.addAll(requests);

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);

        /**
         * Updates the ArrayAdapter when a request is added.
         */
        RequestListController.getRequestList(userLogin).addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getRequestList(userLogin).getRequests();
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
