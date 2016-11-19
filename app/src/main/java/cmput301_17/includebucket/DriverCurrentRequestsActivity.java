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
 * Activity for viewing current requests a driver is involved in
 *
 */
public class DriverCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    /**
     *
     * Updates the ArrayAdapter when a request is added.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_requests);

        user = (UserAccount) getIntent().getSerializableExtra("User");
        final String userLogin = user.getUniqueUserName();

        requestsListView = (ListView) findViewById(R.id.driverCurrentList);

        requests = RequestListController.getRequestList();
        requestList = new ArrayList<>();
        requestList.addAll(requests);

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);


        RequestListController.getRequestList().addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getRequestList().getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }
}
