package cmput301_17.includebucket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private ConnectivityManager connectivityManager;
    private boolean connected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_requests);

        UserFileManager.initManager(this.getApplicationContext());
        DriverRequestsFileManager.initManager(this.getApplicationContext());

        requestsListView = (ListView) findViewById(R.id.driverCurrentList);

        user = UserController.getUserAccount();

        /**
         * Taken from: http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
         * Accessed: November 26, 2016
         * Author: binnyb
         */
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            connected = Boolean.TRUE;
        }
        else connected = Boolean.FALSE;

        if (connected)
        {
            DriverRequestsController.loadInvolvedRequestsFromElasticsearch();
        }

        requests = DriverRequestsController.getDriverRequests().getRequests();

        RequestList saveRequests = new RequestList();
        saveRequests.getRequests().addAll(requests);
        try {
            DriverRequestsFileManager.getRequestListFileManager().saveRequestList(saveRequests);
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong when trying to deserialize RequestList in DriverRequestsFileManager");
        }
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

    @Override
    protected void onStop() {
        super.onStop();
    }
}