package cmput301_17.includebucket;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Activity for viewing current requests a driver is involved in.
 * DriverCurrentRequestsActivity
 */
public class DriverCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests, allRequests;
    private UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_requests);

        UserFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        requestsListView = (ListView) findViewById(R.id.driverCurrentList);

        user = UserController.getUserAccount();

        DriverRequestsController.loadInvolvedRequestsFromElasticsearch();

        requests = DriverRequestsController.getDriverRequests().getRequests();
        requestList = new ArrayList<>();
        requestList.addAll(requests);
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();

        DriverRequestsController.getDriverRequests().addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = DriverRequestsController.getDriverRequests().getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        requestAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestAdapter.notifyDataSetChanged();
    }
}