package cmput301_17.includebucket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;

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

    private RiderRequestsController riderRequestsController = new RiderRequestsController();
    private UserController userController = new UserController();
    private UserAccount user = new UserAccount();

    boolean connected;

    final String adbMessage = "Click More button for details.";

    /**
     * This deals with viewing the current requests and updates  when a request is added or deleted.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_requests);

        requestsListView = (ListView) findViewById(R.id.requestsListView);

        riderRequestsController.setContext(RiderCurrentRequestsActivity.this);
        userController.setContext(RiderCurrentRequestsActivity.this);

        user = UserController.getUserAccount();

        RiderRequestsController.loadRequestsFromElasticSearch();

        RiderRequestsController.saveRequestInLocalFile(RiderRequestsController.getRiderRequests().getRequests(), RiderCurrentRequestsActivity.this);

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

        requests = RiderRequestsController.getRiderRequests().getRequests();
        requestList = new ArrayList<>();
        requestList.addAll(requests);
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();

        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(RiderCurrentRequestsActivity.this);
                adb.setMessage(adbMessage);
                adb.setCancelable(true);
                final int finalPosition = position;

                // Add Delete button to delete the request invoked
                adb.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    //riderRequestsController.setContext(RiderCurrentRequestsActivity.this);

                    Request request = requestList.get(finalPosition);
                    RiderRequestsController.deleteRequest(request);
                    RiderRequestsController.deleteRequestFromElasticsearch(request);
                    RiderRequestsController.loadRequestsFromElasticSearch();
                    RiderRequestsController.saveRequestInLocalFile(RiderRequestsController.getRiderRequests().getRequests(), RiderCurrentRequestsActivity.this);

                    requestList.remove(request);
                    requestsListView.setAdapter(requestAdapter);
                    requestAdapter.notifyDataSetChanged();
                    }
                });
                // Add Cancel button to exit the dialog box
                adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                // Add More button to view request
                adb.setPositiveButton("More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Request request = requestList.get(finalPosition);
                        Intent intent;

                        if(request.hasRiderAccepted() == false){
                            intent = new Intent(RiderCurrentRequestsActivity.this, RiderAcceptDriverActivity.class);
                            intent.putExtra("Request", request);
                            startActivity(intent);
                        }
                        else {
                            intent = new Intent(RiderCurrentRequestsActivity.this, RiderSingleRequestActivity.class);
                            intent.putExtra("Request", request);
                            startActivity(intent);
                        }
                    }
                });
                adb.show();
            }
        });
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);
        requestAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}