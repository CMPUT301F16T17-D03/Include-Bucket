package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class displays a request's information in full when it is invoked by the user in the
 * RiderCurrentRequestsActivity.
 */
public class RiderSingleRequestActivity extends MainMenuActivity {

    private TextView requestTextView;
    private ListView driversListView;

    private ArrayList<UserAccount> driverList;
    private ArrayAdapter<UserAccount> driverListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_request);

        final Request request = (Request) getIntent().getSerializableExtra("Request");

        requestTextView = (TextView) findViewById(R.id.requestTextView);
        driversListView = (ListView) findViewById(R.id.driversListView);

        Double price    = request.getFare();
        String startLoc = request.getStartLocation();
        String endLoc   = request.getEndLocation();
        String story    = request.getRiderStory();

        requestTextView.setText("Price:\n" + price + "\n\nStart Location:\n" + startLoc +
                "\n\nEnd Location:\n" + endLoc + "\n\nRequest Description:\n" + story);

        //requests = RiderRequestsController.getRiderRequests();

        Collection<UserAccount> drivers = request.getDrivers();
        driverList = new ArrayList<>();
        driverList.addAll(drivers);

        driverListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driverList);
        driversListView.setAdapter(driverListAdapter);

        driversListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int finalPosition = position;

                UserAccount user = driverList.get(finalPosition);
                Intent intent = new Intent(RiderSingleRequestActivity.this, ViewDriverDataActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Request", request);
                startActivity(intent);
            }
        });
    }
}
