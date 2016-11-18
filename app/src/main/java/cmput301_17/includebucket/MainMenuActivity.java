package cmput301_17.includebucket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The view for the main menu
 *
 * Created by Duncan on 10/22/2016.
 */

public class MainMenuActivity extends Activity {
   // private Activity activity = this;

    UserAccount user = new UserAccount();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        user = (UserAccount) getIntent().getSerializableExtra("User");

        Button riderNewButton = (Button) findViewById(R.id.newRequest);
        riderNewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, NewRiderRequestActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        Button logOutButton = (Button) findViewById(R.id.Logout);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);
                finish();
            }
        });

        Button riderRequestsButton = (Button) findViewById(R.id.MyRequests);
        riderRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);
                Intent intent = new Intent(MainMenuActivity.this, RiderCurrentRequestsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        Button driverBrowseRequestsButton = (Button) findViewById(R.id.Browse);
        driverBrowseRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, DriverBrowseRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button driverCurrentRequestsButton = (Button) findViewById(R.id.Current);
        driverCurrentRequestsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, DriverCurrentRequestsActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });



        Button EditAccountButton = (Button) findViewById(R.id.ViewAccount);
        EditAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                setResult(RESULT_OK);

                Intent intent = new Intent(MainMenuActivity.this, ViewUserDataActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }
}