package cmput301_17.includebucket;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private String userLogin;

    final String adbMessage = "Click More button for details.";

    /**
     * This deals with viewing the current requests and
     * updates  when a request is added or deleted.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_requests);

        user = (UserAccount) getIntent().getSerializableExtra("User");
        userLogin = user.getUniqueUserName();

        requestsListView = (ListView) findViewById(R.id.requestsListView);

        requests = RequestListController.getRiderRequestList(user);
        requestList = new ArrayList<>();
        requestList.addAll(requests);

        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        requestsListView.setAdapter(requestAdapter);

        /**
         * Updates the ArrayAdapter when a request is added or deleted.
         */
        RequestListController.getRiderRequestList(user).addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getRiderRequestList(user).getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });

        Button newButton = (Button) findViewById(R.id.newRequestButton);
        newButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(RiderCurrentRequestsActivity.this, NewRiderRequestActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * This sets a listener for a long click to delete a request.
     */
    @Override
    protected void onResume() {
        super.onResume();

        requestsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder adb = new AlertDialog.Builder(RiderCurrentRequestsActivity.this);
                adb.setMessage(adbMessage);
                adb.setCancelable(true);
                final int finalPosition = position;

                // Add Delete button to delete the request invoked
                adb.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Request request = requestList.get(finalPosition);
                        RequestListController.deleteRequestFromList(request);
                        RequestListController.getRiderRequestList(user).deleteRequest(request);
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
                        Intent intent = new Intent(RiderCurrentRequestsActivity.this, RiderSingleRequestActivity.class);
                        intent.putExtra("Request", request);
                        startActivity(intent);
                    }
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

