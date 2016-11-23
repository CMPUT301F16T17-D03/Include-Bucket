package cmput301_17.includebucket;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Activity for viewing current requests a driver is involved in
 */
public class DriverCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    private DriverRequestsController driverRequestsController = new DriverRequestsController();
    private UserController userController = new UserController();
    private UserAccount user = new UserAccount();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_requests);

        requestsListView = (ListView) findViewById(R.id.driverCurrentList);

        driverRequestsController.setContext(DriverCurrentRequestsActivity.this);
        userController.setContext(DriverCurrentRequestsActivity.this);

        user = UserController.getUserAccount();

        requests = DriverRequestsController.getDriverRequests(); // Get all requests
        requestList = new ArrayList<>();
        requestList.addAll(requests);

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);

        DriverRequestsController.getDriverRequests().addListener(new Listener() {
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
