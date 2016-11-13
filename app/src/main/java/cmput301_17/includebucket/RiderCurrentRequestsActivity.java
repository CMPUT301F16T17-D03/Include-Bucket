package cmput301_17.includebucket;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by orlick on 11/7/16.
 */
public class RiderCurrentRequestsActivity extends MainMenuActivity {

    private ListView requestsListView;
    private ArrayList<Request> list;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_requests);

        requestsListView = (ListView) findViewById(R.id.requestsListView);

        requests = RequestListController.getRequestList().getRequests();

        list = new ArrayList<>(requests);

        requestAdapter = new ArrayAdapter<>(RiderCurrentRequestsActivity.this, android.R.layout.simple_list_item_1, list);

        requestsListView.setAdapter(requestAdapter);

        RequestListController.getRequestList().addListener(new Listener() {
            @Override
            public void update() {
                list.clear();
                Collection<Request> requests = RequestListController.getRequestList().getRequests();
                list.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }


}

