package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Formatter;


/**
 * RiderAcceptDriverActivity
 *
 * This is an activity for a rider viewing the drivers who have accepted their request
 */
public class RiderAcceptDriverActivity extends MainMenuActivity {

    private TextView startTitle, endTitle, priceTitle, driversTitle, noDrivers, start, end, price, story;
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
        endTitle   = (TextView) findViewById(R.id.endLocTextView);
        priceTitle = (TextView) findViewById(R.id.priceTextView);
        driversTitle   = (TextView) findViewById(R.id.priceTextView);
        driverListView = (ListView) findViewById(R.id.pendingDriversListView);

        start = (TextView) findViewById(R.id.DSRAStartEditText);
        end   = (TextView) findViewById(R.id.DSRAEndEditText);
        price = (TextView) findViewById(R.id.priceValueTextView);
        story = (TextView) findViewById(R.id.riderStoryTextView);

        start.setText(request.getStartAddress());

        story.setText(request.getRiderStory());
        end.setText(request.getEndAddress());

        //Formatter priceFormatter = new Formatter();
        //String p = priceFormatter.format("%.2f%n", request.getFare()).toString();
        //price.setText("$"+p);

        if (request.getDrivers().size() == 0)
        {
            noDrivers  = (TextView) findViewById(R.id.noPendingDrivers);
            noDrivers.setText("No one has accepted your request...");
        }

        start.setText(request.getStartAddress());
        end.setText(request.getEndAddress());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        price.setText(formatter.format(request.getFare()));

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

                UserAccount user = driverList.get(position);
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