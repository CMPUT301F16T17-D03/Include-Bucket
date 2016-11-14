package cmput301_17.includebucket;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by michelletagarino on 16-10-20.
 *
 * view for browsing requests
 *
 */
public class DriverBrowseRequestsActivity extends MainMenuActivity {

    private String key;
    private EditText keyword;
    private ListView browseRequestList;
    private ArrayList<Request> requestList;
    private ArrayAdapter<Request> requestAdapter;
    private Collection<Request> requests;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_requests);

        keyword  = (EditText) findViewById(R.id.keyword);
        browseRequestList = (ListView) findViewById(R.id.browseRequestList);

        requestList = new ArrayList<>();
        requestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestList);
        browseRequestList.setAdapter(requestAdapter);

        Button searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                key = keyword.getText().toString();
                requests = RequestListController.getKeywordList(key);
                requestList.addAll(requests);
                browseRequestList.setAdapter(requestAdapter);
                requestAdapter.notifyDataSetChanged();
            }
        });

        RequestListController.getKeywordList(key).addListener(new Listener() {
            @Override
            public void update() {
                requestList.clear();
                Collection<Request> requests = RequestListController.getKeywordList(key).getRequests();
                requestList.addAll(requests);
                requestAdapter.notifyDataSetChanged();
            }
        });
    }

}