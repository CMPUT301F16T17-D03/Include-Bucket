package cmput301_17.includebucket;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.Collection;

/**
 * RiderCurrentRequestsActivity
 *
 * This class displays the list of the rider's requests.
 */
public class RiderCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    private UserAccount user = new UserAccount();

    private ConnectivityManager connectivityManager;

    boolean connected, messageSeen;

    private final String adbMessage = "Click More button for details.";

    /**
     * This deals with viewing the current requests and updates  when a request is added or deleted.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_requests);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        requestsListView = (ListView) findViewById(R.id.requestsListView);

        /**
         * Taken from: http://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
         * Accessed: November 26, 2016
         * Author: binnyb
         */
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            connected = Boolean.TRUE;
        }
        else connected = Boolean.FALSE;

        if (connected)
        {
            RiderRequestsController.loadRequestsFromElasticSearch();
        }
        else
        {
            Toast.makeText(RiderCurrentRequestsActivity.this, "You are offline!", Toast.LENGTH_SHORT).show();
        }

        requests = RiderRequestsController.getRiderRequests().getRequests();
        requestList = new ArrayList<>();
        requestList.addAll(requests);
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();

        RiderRequestsController.getRiderRequests().addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RiderRequestsController.getRiderRequests().getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });

        Button newButton = (Button) findViewById(R.id.newRequestButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(RiderCurrentRequestsActivity.this, NewRiderRequestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        requests = RiderRequestsController.getRiderRequests().getRequests();
        requestList = new ArrayList<>();
        requestList.addAll(requests);
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();
    }

    /**
     * This sets a listener for a long click to delete a request.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (connected)
        {
            /**
             * Creates requests offline successfully and stores them into server
             * when online again, but for some reason it takes a while for it to load
             * into the server. Sometimes it only shows up when another request is made again.
             * Not sure if this is a server issue, or issue with the code. But it works.
             *
             * @see OfflineRequestQueue
             * @see CreateRequestCommand
             *
             */
            if (!OfflineRequestQueue.getRequestQueue().isEmpty())
            {
                OfflineRequestQueue.execute();
            }
        }

        requestAdapter.notifyDataSetChanged();

        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int finalPosition = position;
                Request request = requestList.get(finalPosition);
                Intent intent;

                if(request.hasRiderAccepted() == false){
                    intent = new Intent(RiderCurrentRequestsActivity.this, RiderAcceptDriverActivity.class);
                    intent.putExtra("Request", request);
                    startActivity(intent);
                    finish();
                }
                else {
                    intent = new Intent(RiderCurrentRequestsActivity.this, RiderSingleRequestActivity.class);
                    intent.putExtra("Request", request);
                    startActivity(intent);
                    finish();
                }
            }
        });

        requestsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(RiderCurrentRequestsActivity.this);
                adb.setMessage(adbMessage);
                adb.setCancelable(true);

                final int finalPosition = position;

                adb.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Request request = requestList.get(finalPosition);
                        RiderRequestsController.getRiderRequests().deleteRequest(request);
                    }
                });
                adb.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                adb.show();
                return false;
            }
        });

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();
    }
}