package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * This class displays a request's information in full when it is invoked by the user in the
 * RiderCurrentRequestsActivity.
 */
public class RiderSingleRequestActivity extends MainMenuActivity {

    private ScrollView requestScrollView;
    private TextView requestTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_request);

        Request request = (Request) getIntent().getSerializableExtra("Request");

        requestTextView = (TextView) findViewById(R.id.requestTextView);

        Float price    = request.getFare();
        String startLoc = request.getStartLocation();
        String endLoc   = request.getEndLocation();
        String story    = request.getRiderStory();

        requestTextView.setText("Price:\n" + price + "\n\nStart Location:\n" + startLoc +
                "\n\nEnd Location:\n" + endLoc + "\n\nRequest Description:\n" + story);
    }
}
