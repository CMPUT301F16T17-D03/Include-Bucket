package cmput301_17.includebucket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ScrollView;

/**
 * This class displays a request's information in full when it is invoked by the user in the
 * RiderCurrentRequestsActivity.
 */
public class RiderSingleRequestActivity extends MainMenuActivity {

    private ScrollView requestScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_request);

        Request request = (Request) getIntent().getSerializableExtra("Request");

        requestScrollView = (ScrollView) findViewById(R.id.requestScrollView);
        

    }

}
