package cmput301_17.includebucket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * RiderAcceptDriverActivity
 *
 * This is an activity for a rider viewing the drivers who have accepted their request
 */
public class RiderAcceptDriverActivity extends MainMenuActivity {

    private TextView startTitle, endTitle, priceTitle, start, end, price;
    private ListView driverListView;
    private ArrayList<UserAccount> driverList;
    private ArrayAdapter<UserAccount> driverAdapter;
    final String adbMessage = "Click More button for details.";
    Request request = new Request();

    /**
     * This sets the data of the request on the screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_driver);

        UserFileManager.initManager(this.getApplicationContext());
        RiderRequestsFileManager.initManager(this.getApplicationContext());

        request = (Request) getIntent().getSerializableExtra("Request");

        startTitle = (TextView) findViewById(R.id.startLocTextView);
        endTitle = (TextView) findViewById(R.id.endLocTextView);
        priceTitle = (TextView) findViewById(R.id.priceTextView);

        driverListView = (ListView) findViewById(R.id.pendingDriversListView);
        start = (TextView) findViewById(R.id.startGeoLocTextView);
        end = (TextView) findViewById(R.id.endGeoLocTextView);
        price = (TextView) findViewById(R.id.priceValueTextView);

        start.setText(request.getStartLocation());
        end.setText(request.getEndLocation());
        price.setText(request.getFare().toString());

        driverList = new ArrayList<>();
        driverList.addAll(request.getDrivers());
        driverAdapter = new ArrayAdapter<UserAccount>(this, android.R.layout.simple_list_item_1, driverList);
        driverListView.setAdapter(driverAdapter);
    }

    /**
     * This deals with a click on a driver.
     */
    @Override
    protected void onResume() {
        super.onResume();

        driverListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final int finalPosition = position;

                UserAccount user = driverList.get(finalPosition);
                Intent intent = new Intent(RiderAcceptDriverActivity.this, ViewDriverDataActivity.class);
                intent.putExtra("User", user);
                intent.putExtra("Request", request);
                startActivity(intent);
            }
        });
        driverAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, driverList);
        driverListView.setAdapter(driverAdapter);
        driverAdapter.notifyDataSetChanged();

    }
}